package project.ui;

import project.client.BarClient;
import project.model.Bar;
import project.model.Bars;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
public class MainWindow extends JFrame {
    private final BarClient client = new BarClient();
    private JTable table;
    private BarTableModel tableModel;
    private final String studentInfo = "A00268870 - Jahao Wu";

    public MainWindow() {
        // 初始化表格组件
        this.tableModel = new BarTableModel();
        this.table = new JTable(tableModel);
        
        setTitle("Bar Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 添加菜单栏
        createMenuBar();
        
        // 主面板分为左右两部分
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setLeftComponent(createLeftPanel());
        splitPane.setRightComponent(createRightPanel());
        
        add(splitPane, BorderLayout.CENTER);
        refreshData();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // 主菜单
        JMenu menu = new JMenu("Menu Bar");
        menu.setFont(new Font("Arial", Font.BOLD, 12));
        
        // 菜单项
        JMenuItem fillTablesItem = new JMenuItem("Fill Tables");
        JMenuItem createTablesItem = new JMenuItem("Create Tables");
        JMenuItem projectInfoItem = new JMenuItem("Project info");
        JMenuItem deleteAllItem = new JMenuItem("Delete All");
        JMenuItem printExcelItem = new JMenuItem("Print To Excel");
        
        // 添加事件监听
        fillTablesItem.addActionListener(e -> fillTables());
        createTablesItem.addActionListener(e -> createTables());
        projectInfoItem.addActionListener(e -> showProjectInfo());
        deleteAllItem.addActionListener(e -> deleteAllBars());
        printExcelItem.addActionListener(e -> printToExcel());
        
        // 添加菜单项到菜单
        menu.add(fillTablesItem);
        menu.add(createTablesItem);
        menu.add(projectInfoItem);
        menu.addSeparator();
        menu.add(deleteAllItem);
        menu.add(printExcelItem);
        
        // 添加菜单到菜单栏
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
    
    private void fillTables() {
        try {
            client.fillTables(); // 需要在BarClient中添加此方法
            refreshData();
            JOptionPane.showMessageDialog(this, "Tables filled with sample data");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filling tables: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void createTables() {
        try {
            client.createTables(); // 需要在BarClient中添加此方法
            JOptionPane.showMessageDialog(this, "Tables created successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating tables: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showProjectInfo() {
        JOptionPane.showMessageDialog(this, studentInfo, "Project Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }


    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 添加标题（重新定义局部变量）
        JLabel titleLabel = new JLabel("Distributed System Project");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        // 添加现有组件
        leftPanel.add(createGetByNamePanel());
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(createDeleteByIdPanel());
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(createUpdatePanel());
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createPostNewBarPanel());
        
        // 添加垂直间距将按钮推到底部
        leftPanel.add(Box.createVerticalGlue());
        
        // 添加底部按钮面板
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Delete All按钮（红色）
        JButton deleteAllButton = new JButton("Delete All");
        deleteAllButton.setBackground(Color.RED);
        deleteAllButton.setForeground(Color.WHITE);
        deleteAllButton.addActionListener(e -> deleteAllBars());
        
        // Print to Excel按钮（绿色）
        JButton printExcelButton = new JButton("Print To Excel");
        printExcelButton.setBackground(new Color(0, 180, 0)); // 更鲜艳的绿色
        printExcelButton.addActionListener(e -> printToExcel());
        
        bottomPanel.add(deleteAllButton);
        bottomPanel.add(printExcelButton);
        
        leftPanel.add(bottomPanel);
        
        return leftPanel;
    }

    private JPanel createGetByNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // 查询输入部分
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Get Bar By Name:"));
        JTextField nameField = new JTextField(15);
        searchPanel.add(nameField);
        
        JButton getButton = new JButton("Get");
        getButton.setBackground(Color.YELLOW);
        searchPanel.add(getButton);
        
        // 结果显示区域
        JTextArea resultArea = new JTextArea(3, 30);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Single 'Get' Area"));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        
        getButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "A bar name must be entered", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Bar bar = client.getBarByName(name);
                if (bar != null) {
                    resultArea.setText(String.format(
                        "ID: %d\nName: %s\nWeight: %s\nCalories: %d\nManufacturer: %s",
                        bar.getId(), bar.getName(), bar.getWeight(), 
                        bar.getCals(), bar.getManufacturer()));
                } else {
                    resultArea.setText(""); // 清空结果区域
                    JOptionPane.showMessageDialog(this, "Bar not found", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                resultArea.setText(""); // 清空结果区域
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(searchPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resultScrollPane);
        
        return panel;
    }

    private JPanel createDeleteByIdPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField idField = new JTextField(15);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        
        deleteButton.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "An ID must be entered", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete bar with ID " + id + "?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    client.deleteBar(id);
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Bar deleted successfully");
                    idField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be a valid number", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel("Delete Bar By ID:"));
        panel.add(idField);
        panel.add(deleteButton);
        return panel;
    }

    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // 添加粗体标题
        JLabel title = new JLabel("Update By Name:");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        
        // Current Name row
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameRow.add(new JLabel("Current Name:"));
        JTextField currentNameField = new JTextField(15);
        nameRow.add(currentNameField);
        
        // New Name row
        JPanel newNameRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newNameRow.add(new JLabel("New Name:"));
        JTextField newNameField = new JTextField(15);
        newNameRow.add(newNameField);
        
        JButton updateButton = new JButton("Put");
        updateButton.setBackground(new Color(173, 216, 230)); // Light blue
        updateButton.addActionListener(e -> {
            try {
                String currentName = currentNameField.getText().trim();
                String newName = newNameField.getText().trim();
                
                if (currentName.isEmpty() || newName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Both names must be entered", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                client.updateBar(currentName, newName);
                refreshData();
                JOptionPane.showMessageDialog(this, "Update successful");
                
                // 清空输入字段
                currentNameField.setText("");
                newNameField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        newNameRow.add(updateButton);
        
        panel.add(nameRow);
        panel.add(newNameRow);
        return panel;
    }

    private JPanel createPostNewBarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Post New Bar:");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        // Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(15);
        namePanel.add(nameField);
        panel.add(namePanel);

        // Weight
        JPanel weightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        weightPanel.add(new JLabel("Weight:"));
        JTextField weightField = new JTextField(15);
        weightPanel.add(weightField);
        panel.add(weightPanel);

        // Calories
        JPanel calsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        calsPanel.add(new JLabel("Calories:"));
        JTextField calsField = new JTextField(15);
        calsPanel.add(calsField);
        panel.add(calsPanel);

        // Manufacturer
        JPanel manuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        manuPanel.add(new JLabel("Manufacturer:"));
        JTextField manuField = new JTextField(15);
        manuPanel.add(manuField);
        panel.add(manuPanel);

        // Post button
        JButton postButton = new JButton("Post");
        postButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        postButton.addActionListener(e -> {
            try {
                // 验证所有字段
                if (nameField.getText().trim().isEmpty() || 
                    weightField.getText().trim().isEmpty() || 
                    calsField.getText().trim().isEmpty() || 
                    manuField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All Post Fields must be filled", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Bar newBar = new Bar();
                newBar.setName(nameField.getText());
                newBar.setWeight(weightField.getText());
                newBar.setCals(Integer.parseInt(calsField.getText()));
                newBar.setManufacturer(manuField.getText());
                
                client.addBar(newBar);
                refreshData();
                JOptionPane.showMessageDialog(this, "Bar added successfully");
                
                // 清空输入字段
                nameField.setText("");
                weightField.setText("");
                calsField.setText("");
                manuField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Calories must be a valid number", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(Box.createVerticalStrut(10));
        panel.add(postButton);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 使用已初始化的table
        table.setPreferredScrollableViewportSize(new Dimension(550, 700));
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    private void deleteAllBars() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete ALL bars?", 
            "Confirm Delete All", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                client.deleteAllBars(); // 需要在BarClient中添加此方法
                refreshData();
                JOptionPane.showMessageDialog(this, "All bars deleted");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting bars: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".csv")) {
                    filePath += ".csv";
                }
                client.exportToExcel(filePath);
                JOptionPane.showMessageDialog(this, "Data exported to: " + filePath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting to Excel: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        try {
            Bars bars = client.getAllBars();
            java.util.List<Bar> barList = (bars != null && bars.getBars() != null) ? 
                                      bars.getBars() : new ArrayList<>();
            tableModel.setBars(barList);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}