package project.dao;

import project.model.Bar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库访问对象（DAO），处理BARS和MAN表的CRUD操作
 */
public class BarDao {
	
	 static {
	        try {
	            Class.forName("org.hsqldb.jdbc.JDBCDriver"); // 显式注册驱动
	        } catch (ClassNotFoundException e) {
	            throw new RuntimeException("HSQLDB驱动加载失败！", e);
	        }
	    }
    // HSQLDB 连接配置（根据实际修改）
    private static final String JDBC_URL = "jdbc:hsqldb:hsql://localhost/oneDB";
    private static final String JDBC_USER = "SA";
    private static final String JDBC_PASSWORD = "Passw0rd";

    // 获取数据库连接
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // 查询所有巧克力棒（关联BARS和MAN表）
    public List<Bar> getAllBars() throws SQLException {
        List<Bar> bars = new ArrayList<>();
        String sql = "SELECT BARS.Id, BARS.Name, BARS.Weight, BARS.Cals, MAN.Manufacturer " +
                     "FROM BARS JOIN MAN ON BARS.Id = MAN.Id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Bar bar = new Bar();
                bar.setId(rs.getInt("Id"));
                bar.setName(rs.getString("Name"));
                bar.setWeight(rs.getString("Weight"));
                bar.setCals(rs.getInt("Cals"));
                bar.setManufacturer(rs.getString("Manufacturer"));
                bars.add(bar);
            }
        }
        return bars;
    }

    // 添加新巧克力棒（需同时插入BARS和MAN表）
    public void addBar(String name, String weight, int cals, String manufacturer) throws SQLException {
        String sqlBars = "INSERT INTO BARS (Name, Weight, Cals) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            
            // 插入BARS表并获取生成的主键
            try (PreparedStatement stmtBars = conn.prepareStatement(sqlBars, Statement.RETURN_GENERATED_KEYS)) {
                stmtBars.setString(1, name);
                stmtBars.setString(2, weight);
                stmtBars.setInt(3, cals);
                stmtBars.executeUpdate();
                
                // 获取自动生成的ID
                try (ResultSet rs = stmtBars.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        
                        // 插入MAN表
                        try (PreparedStatement stmtMan = conn.prepareStatement(
                                "INSERT INTO MAN (Id, Manufacturer) VALUES (?, ?)")) {
                            stmtMan.setInt(1, generatedId);
                            stmtMan.setString(2, manufacturer);
                            stmtMan.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // 其他方法（根据需求实现）
    public Bar getBarByName(String name) throws SQLException {
        String sql = "SELECT BARS.*, MAN.Manufacturer FROM BARS JOIN MAN ON BARS.Id = MAN.Id WHERE BARS.Name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Bar bar = new Bar();
                bar.setId(rs.getInt("Id"));
                bar.setName(rs.getString("Name"));
                bar.setWeight(rs.getString("Weight"));
                bar.setCals(rs.getInt("Cals"));
                bar.setManufacturer(rs.getString("Manufacturer"));
                return bar;
            }
        }
        return null;
    }
    
    public void fillTables() throws SQLException {
        try (Connection conn = getConnection()) {
            // 清空现有数据
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM MAN");
                stmt.executeUpdate("DELETE FROM BARS");
                stmt.executeUpdate("ALTER TABLE BARS ALTER COLUMN Id RESTART WITH 0");
                stmt.executeUpdate("ALTER TABLE MAN ALTER COLUMN Id RESTART WITH 0");
            }
            
           
            addBar("Twix", "57g", 286, "Mars");
            addBar("Crunchie", "40g", 187, "Cadbury");
            addBar("Mars Bar", "58g", 120, "Mars");
        }
    }

    public void createTables() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
           
            stmt.executeUpdate("DROP TABLE IF EXISTS MAN");
            stmt.executeUpdate("DROP TABLE IF EXISTS BARS");
            
          
            stmt.executeUpdate("CREATE TABLE BARS(Id INTEGER IDENTITY PRIMARY KEY, Name VARCHAR(30) NOT NULL, Weight VARCHAR(30) NOT NULL, Cals INTEGER NOT NULL)");
            stmt.executeUpdate("CREATE TABLE MAN(Id INTEGER IDENTITY PRIMARY KEY, Manufacturer VARCHAR(30))");
        }
    }

    public void deleteAllBars() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM MAN");
            stmt.executeUpdate("DELETE FROM BARS");
            stmt.executeUpdate("ALTER TABLE BARS ALTER COLUMN Id RESTART WITH 0");
            stmt.executeUpdate("ALTER TABLE MAN ALTER COLUMN Id RESTART WITH 0");
        }
    }

    public void deleteBar(int id) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtMan = conn.prepareStatement("DELETE FROM MAN WHERE Id = ?");
                 PreparedStatement stmtBars = conn.prepareStatement("DELETE FROM BARS WHERE Id = ?")) {
                stmtMan.setInt(1, id);
                stmtBars.setInt(1, id);
                stmtMan.executeUpdate();
                stmtBars.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
    public void updateBar(int id, String newName) throws SQLException {
        String sql = "UPDATE BARS SET Name = ? WHERE Id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating bar failed, no rows affected.");
            }
        }
    }

    /**
     * 通过名称更新巧克力棒名称
     */
    public void updateBarByName(String currentName, String newName) throws SQLException {
        Bar bar = getBarByName(currentName);
        if (bar == null) {
            throw new SQLException("No bar found with name: " + currentName);
        }
        updateBar(bar.getId(), newName);
    }
}