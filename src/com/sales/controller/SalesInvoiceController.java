package com.sales.controller;

import com.sales.model.InvoiceHeader;
import com.sales.model.InvoiceLine;
import com.sales.model.InvoicesTableModel;
import com.sales.ui.CreateInvoiceDialog;
import com.sales.ui.SalesInvoiceJFrame;
import com.sales.model.InvoiceHeader;
import com.sales.model.LineTableModel;
import com.sales.ui.CreateLineDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class SalesInvoiceController implements ActionListener, ListSelectionListener {

    private SalesInvoiceJFrame myFrame;
    private CreateInvoiceDialog invoiceDialog;
    private CreateLineDialog lineDialog;

    public SalesInvoiceController(SalesInvoiceJFrame myFrame) {
        this.myFrame = myFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceOK":
                createInvoiceOK();
                break;
            case "createLineOK":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = myFrame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            InvoiceHeader currentInvoice = myFrame.getInvoices().get(selectedIndex);
            myFrame.getInvoiceNumLabel().setText("" + currentInvoice.getInvoiceNum());
            myFrame.getInvoiceDateLabel().setText(currentInvoice.getInvoiceDate());
            myFrame.getCustomerNameLabel().setText(currentInvoice.getCustomerName());
            myFrame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            LineTableModel linesTableModel = new LineTableModel(currentInvoice.getInvoiceLine());
            myFrame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(myFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("Invoices have been read");
                ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] headerParts = headerLine.split(",");
                    int invoiceNum = Integer.parseInt(headerParts[0]);
                    String invoiceDate = headerParts[1];
                    String customerName = headerParts[2];

                    InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                    invoicesArray.add(invoice);
                }
                result = fc.showOpenDialog(myFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    for (String lineLine : lineLines) {
                        String lineParts[] = lineLine.split(",");
                        int invoiceNum = Integer.parseInt(lineParts[0]);
                        String itemName = lineParts[1];
                        double itemPrice = Double.parseDouble(lineParts[2]);
                        int count = Integer.parseInt(lineParts[3]);
                        InvoiceHeader inv = null;
                        for (InvoiceHeader invoice : invoicesArray) {
                            if (invoice.getInvoiceNum() == invoiceNum) {
                                inv = invoice;
                                break;
                            }
                        }

                        InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                        inv.getInvoiceLine().add(line);
                    }

                }
                myFrame.setInvoices(invoicesArray);
                InvoicesTableModel invoicesTableModel = new InvoicesTableModel(invoicesArray);
                myFrame.setInvoicesTableModel(invoicesTableModel);
                myFrame.getInvoiceTable().setModel(invoicesTableModel);
                myFrame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = myFrame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getInvoiceLine()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Check point");
        try {
            JFileChooser myFileChooser = new JFileChooser();
            int result = myFileChooser.showSaveDialog(myFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = myFileChooser.getSelectedFile();
                FileWriter myFileWriter = new FileWriter(headerFile);
                myFileWriter.write(headers);
                myFileWriter.flush();
                myFileWriter.close();
                result = myFileChooser.showSaveDialog(myFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = myFileChooser.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void createNewInvoice() {
        invoiceDialog = new CreateInvoiceDialog(myFrame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = myFrame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            myFrame.getInvoices().remove(selectedRow);
            myFrame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new CreateLineDialog(myFrame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = myFrame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            LineTableModel linesTableModel = (LineTableModel) myFrame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            myFrame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = myFrame.getNextInvoiceNum();

        InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
        myFrame.getInvoices().add(invoice);
        myFrame.getInvoicesTableModel().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = myFrame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = myFrame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getInvoiceLine().add(line);
            LineTableModel linesTableModel = (LineTableModel) myFrame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            myFrame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

}
