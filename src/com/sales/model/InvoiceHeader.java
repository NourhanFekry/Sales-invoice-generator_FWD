package com.sales.model;

import java.util.ArrayList;

public class InvoiceHeader {
    private int invoiceNum;
    private String invoiceDate;
    private String customerName;
    private ArrayList<InvoiceLine> InvoiceLines;

    public InvoiceHeader() {
    }

    public InvoiceHeader(int invoiceNum, String invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLine() {
        if (InvoiceLines == null){
            InvoiceLines = new ArrayList<>();
        }
        return InvoiceLines;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

   public double getInvoiceTotal (){
      double invoiceTotal=0.0;
      for(InvoiceLine InvoiceLines : getInvoiceLine()){
      invoiceTotal += InvoiceLines.getLineTotal();
      }
       return invoiceTotal;
   }
     
         public String getAsCSV() {
        return invoiceNum + "," + invoiceDate + "," + customerName;
    }
    
}
