package com.toedter.calendar.demo;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.toedter.calendar.JDateChooserCellEditor;

public class DemoTable extends JPanel {
	public DemoTable() {
		super(new GridLayout(1, 0));

		setName("DemoTable");

		JTable table = new JTable(new DemoTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(180, 32));
		table.setDefaultEditor(Date.class, new JDateChooserCellEditor());
		
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
	}

	class DemoTableModel extends AbstractTableModel {
		private String[] columnNames = { "Empty Date", "Date set" };

		private Object[][] data = { 
				{ null, new Date() },
				{ null, new Date() }
	    };

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, 1).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
				return true;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);

		}
	}
}