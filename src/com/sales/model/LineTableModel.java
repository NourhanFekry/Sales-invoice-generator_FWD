package com.sales.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class LineTableModel extends AbstractTableModel {

    private ArrayList<InvoiceLine> invoiceslines;
    private String[] tableItems = {"No.", "Item Name", "Item Price", "Count", "Item Total"};

    public LineTableModel(ArrayList<InvoiceLine> invoiceslines) {
        this.invoiceslines = invoiceslines;
    }

    public ArrayList<InvoiceLine> getLines() {
        return invoiceslines;
    }

    @Override
    public int getRowCount() {
        return invoiceslines.size();
    }

    @Override
    public int getColumnCount() {
        return tableItems.length;
    }
     @Override
    public String getColumnName(int x) {
        return tableItems[x];
    }
     

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
          InvoiceLine invoiceline = invoiceslines.get(rowIndex);
        
        switch(columnIndex) {
            case 0: return invoiceline.getInvoice().getInvoiceNum();
            case 1: return invoiceline.getItemName();
            case 2: return invoiceline.getItemPrice();
            case 3: return invoiceline.getCount();
            case 4: return invoiceline.getLineTotal();
            default : return "";
        }
    }

}
