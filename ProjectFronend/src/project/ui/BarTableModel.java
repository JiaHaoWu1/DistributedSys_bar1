package project.ui;

import project.model.Bar;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BarTableModel extends AbstractTableModel {
    private final String[] COLUMNS = {"ID", "name", "weight", "cals", "manufacturer"};
    private List<Bar> bars = new ArrayList<>();

    public void setBars(List<Bar> bars) {
        this.bars = bars;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return bars == null ? 0 : bars.size(); // 双重保险
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
    	
    	if (bars == null || bars.isEmpty()) return null; // 空数据保护
        Bar bar = bars.get(row);
        switch (col) {
            case 0: return bar.getId();
            case 1: return bar.getName();
            case 2: return bar.getWeight();
            case 3: return bar.getCals();
            case 4: return bar.getManufacturer();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }
}