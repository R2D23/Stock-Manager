package com.tlahuicode.stockManager.sale;

import java.awt.Component;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import com.tlahuicode.stockManager.CashPanel;
import com.tlahuicode.stockManager.Product;
import com.tlahuicode.stockManager.product.ProductEvent;
import com.tlahuicode.stockManager.product.ProductListener;

/**
 *
 * @author LuisArturo
 */
public class SaleTable extends JTable {

    private String filter;
    private static ArrayList<Integer> products;
    private static Product newAux;
    private boolean isMinimalist;

    public void setFilter(String filter) {
	this.filter = filter;
	updateFilter();
    }

    public SaleTable() throws SQLException {
	this(false);
    }

    public SaleTable(boolean isMinimalist) throws SQLException {
	super();
	newAux = new Product();
	this.isMinimalist = isMinimalist;
	setModel(new SaleTableModel(isMinimalist));
	if (products == null)
	    Product.updateAllProducts();
	if (!isMinimalist) {
	    this.setRowHeight(30);
	    {//Set ID Column
		getColumnModel().getColumn(0).setMaxWidth(40);
		getColumnModel().getColumn(0).setMinWidth(40);
		getColumnModel().getColumn(0).setPreferredWidth(40);
	    }
	    {//Set Barcode Column
		getColumnModel().getColumn(3).setMaxWidth(110);
		getColumnModel().getColumn(3).setMinWidth(110);
		getColumnModel().getColumn(3).setPreferredWidth(110);
	    }
	    {//Set Price Column
		getColumnModel().getColumn(4).setMaxWidth(100);
		getColumnModel().getColumn(4).setMinWidth(60);
		getColumnModel().getColumn(4).setPreferredWidth(60);
		getColumnModel().getColumn(4).setCellRenderer(new PriceRenderer());
	    }
	    {//Set Quantity Column
		getColumnModel().getColumn(5).setMaxWidth(60);
		getColumnModel().getColumn(5).setMinWidth(60);
		getColumnModel().getColumn(5).setPreferredWidth(60);
		getColumnModel().getColumn(5).setCellEditor(new SpinnerEditor());
	    }
	    {//Set SemiTotal Column
		getColumnModel().getColumn(6).setMaxWidth(80);
		getColumnModel().getColumn(6).setMinWidth(80);
		getColumnModel().getColumn(6).setPreferredWidth(80);
		this.getColumnModel().getColumn(6).setCellRenderer(new PriceRenderer());
	    }
	} else {
	    {//Set Quantity Column
		getColumnModel().getColumn(1).setMaxWidth(60);
		getColumnModel().getColumn(1).setMinWidth(60);
		getColumnModel().getColumn(1).setPreferredWidth(60);
	    }
	    {//Set SemiTotal Column
		getColumnModel().getColumn(2).setMaxWidth(80);
		getColumnModel().getColumn(2).setMinWidth(80);
		getColumnModel().getColumn(2).setPreferredWidth(80);
		this.getColumnModel().getColumn(2).setCellRenderer(new PriceRenderer());
	    }
	}
	updateFilter();
    }

    public synchronized void put(Product p) {
	boolean isAlreadyIn = false;
	for (Integer i : products)
	    if (i.equals(p.getId()))
		isAlreadyIn = true;
	if (isAlreadyIn)
	    throw new IllegalArgumentException("Is Already In!");
	else {
	    products.add(p.getId());
	    ((SaleTableModel) getModel()).fireTableRowsInserted(products.size() - 1, products.size() - 1);
	}
    }

    public void remove(Product p) {
	int aux = products.indexOf(p.getId());
	products.remove(p.getId());
	((SaleTableModel) this.getModel()).fireTableRowsDeleted(aux, aux);
    }

    public void updateFilter() {
	if (!isMinimalist) {
	    RowFilter<SaleTableModel, Object> rf;
	    if (filter == null)
		filter = "";
	    rf = new RowFilter() {

		@Override
		public boolean include(RowFilter.Entry entry) {
		    if ((int) entry.getIdentifier() == products.size())
			return true;
		    Product p = Product.getProductByID(products.get((int) entry.getIdentifier()));
		    String auxS = Normalizer.normalize(filter, Normalizer.Form.NFD).toUpperCase();
		    String auxN = Normalizer.normalize(p.getName(), Normalizer.Form.NFD).toUpperCase();
		    String auxT = Normalizer.normalize(p.getTags(), Normalizer.Form.NFD).toUpperCase();
		    String auxB = p.getBarcode();
		    String auxID = String.valueOf(p.getId());
		    boolean aux = true;
		    for(String s : auxS.split(" "))
			if (auxID.contains(s) || auxN.contains(s) || auxT.contains(s) || auxB.contains(s))
			    aux = aux && true;
			else
			    aux = false;
		    return aux;
		}
	    };
	    TableRowSorter<SaleTableModel> sorter;
	    sorter = new TableRowSorter<>((SaleTableModel) getModel());
	    sorter.setRowFilter(rf);
	    this.setRowSorter(sorter);
	} else {
	    RowFilter<SaleTableModel, Object> rf;
	    rf = new RowFilter() {
		@Override
		public boolean include(RowFilter.Entry entry) {
		    return ((SaleTableModel) entry.getModel()).getPieces(Product.getProductByID(products.get((int) entry.getIdentifier()))) > 0;
		}
	    };
	    TableRowSorter<SaleTableModel> sorter;
	    sorter = new TableRowSorter<>((SaleTableModel) getModel());
	    sorter.setRowFilter(rf);
	    setRowSorter(sorter);
	}
    }

    private static class PriceRenderer extends DefaultTableCellRenderer {

	public PriceRenderer() {
	    super();
	}

	@Override
	public void setValue(Object value) {
	    if (value == null || !(value instanceof Number))
		value = 0;
	    setText("$ " + ((Number) value).doubleValue());
	    this.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
	}
    }

    private class SaleTableModel extends AbstractTableModel {

	private boolean isMinimalist;

	public SaleTableModel() {
	    this(false);
	}

	public SaleTableModel(boolean isMinimalist) {
	    super();
	    this.isMinimalist = isMinimalist;
	    Product.addListener(new ProductListener() {

		@Override
		public void addedProduct(ProductEvent evt) {
		    newAux = new Product();
		    put(evt.getReference());
		}

		@Override
		public void erasedProduct(ProductEvent evt) {
		    remove(evt.getReference());
		}

		@Override
		public void updatedProduct(ProductEvent evt) {
		}

		@Override
		public void updatedProducts(ProductEvent evt) {
		    products = Product.getIDs();
		    fireTableDataChanged();
		}

	    });
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (!isMinimalist)
		switch (columnIndex) {
		    default://ID
			return Integer.class;
		    case 1://Name
		    case 2://Tags
		    case 3://Barcode
			return String.class;
		    case 4://Price
		    case 5://Pieces
		    case 6://Semi-Total
			return Double.class;
		}
	    else
		switch (columnIndex) {
		    default://Name
			return String.class;
		    case 1://Pieces
		    case 2://Semi-Total
			return Double.class;
		}
	}

	@Override
	public int getColumnCount() {
	    if (!isMinimalist)
		return 7;
	    else
		return 3;
	}

	@Override
	public String getColumnName(int column) {
	    if (!isMinimalist)
		switch (column) {
		    default:
			return "ID";
		    case 1:
			return "Name";
		    case 2:
			return "Tags";
		    case 3:
			return "Barcode";
		    case 4:
			return "Price";
		    case 5:
			return "Pieces";
		    case 6:
			return "Semi-Total";
		}
	    else
		switch (column) {
		    default:
			return "Name";
		    case 1:
			return "Pieces";
		    case 2:
			return "Semi-Total";
		}
	}

	@Override
	public int getRowCount() {
	    if (products == null)
		return 0;
	    if (isMinimalist)
		return products.size();
	    else
		return products.size() + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	    Product p;
	    if (rowIndex == products.size())
		p = newAux;
	    else
		p = Product.getProductByID(products.get(rowIndex));
	    if (!isMinimalist)
		switch (columnIndex) {
		    default:
			return p.getId();
		    case 1:
			return p.getName();
		    case 2:
			return p.getTags();
		    case 3:
			return p.getBarcode();
		    case 4:
			return p.getPrice();
		    case 5:
			return getPieces(p);
		    case 6:
			return getValue(p);
		}
	    else
		switch (columnIndex) {
		    default:
			return p.getName();
		    case 1:
			return getPieces(p);
		    case 2:
			return getValue(p);
		}
	}

	public double getPieces(Product p) {
	    if (CashPanel.getSale().get(p) == null)
		return 0;
	    else
		return CashPanel.getSale().get(p).getPzs();
	}

	public double getValue(Product p) {
	    if (CashPanel.getSale().get(p) == null)
		return 0;
	    else
		return CashPanel.getSale().get(p).getValue();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	    if (!isMinimalist)
		if (rowIndex == products.size())
		    if (columnIndex > 0 && columnIndex < 5)
			return true;
		    else
			return false;
		else if (columnIndex != 0 && columnIndex != 6)
		    return true;
		else
		    return false;
	    else
		return false;
	}

	@Override
	public synchronized void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	    try {
		if (aValue == null)
		    throw new IllegalArgumentException();
		Product p;
		if (rowIndex == products.size())
		    p = newAux;
		else
		    p = Product.getProductByID(products.get(rowIndex));
		switch (columnIndex) {
		    default:
			if (!(aValue instanceof Long))
			    throw new IllegalArgumentException();
			//This should not happpen. LOL!
			break;
		    case 1:
			if (!(aValue instanceof String))
			    throw new IllegalArgumentException();
			p.setName((String) aValue);
			break;
		    case 2:
			if (!(aValue instanceof String))
			    throw new IllegalArgumentException();
			p.setTags((String) aValue);
			break;
		    case 3:
			if (!(aValue instanceof String))
			    throw new IllegalArgumentException();
			p.setBarcode((String) aValue);
			break;
		    case 4:
			if (!(aValue instanceof Double))
			    throw new IllegalArgumentException();
			p.setPrice((double) aValue);
			break;
		    case 5:
			if (!(aValue instanceof Double)) {
			    System.out.println(aValue.getClass());
			    throw new IllegalArgumentException();
			}
			if ((double) aValue <= 0)
			    CashPanel.getSale().remove(p);
			else
			    CashPanel.getSale().put(p, new SaleInfo((double) aValue, (double) aValue * p.getPrice()));
			break;
		}
		if (rowIndex == products.size()) {
		    if (!p.isRegistered() && p.validate())
			p.addProduct();
		} else
		    fireTableCellUpdated(rowIndex, columnIndex);
	    } catch (SQLException ex) {
		Logger.getLogger(SaleTable.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IllegalArgumentException ex) {
		//do nothing, lol
	    }

	}
    }

    /**
     * Fetched at
     * https://stackoverflow.com/questions/3736993/use-jspinner-like-jtable-cell-editor
     */
    private static class SpinnerEditor extends DefaultCellEditor {

	JSpinner.DefaultEditor editor;
	JSpinner spinner;
	JTextField textField;
	boolean valueSet;

	// Initializes the spinner.
	public SpinnerEditor() {
	    super(new JTextField());
	    spinner = new JSpinner();
	    spinner.setModel(new SpinnerNumberModel((double) 0, 0, 100, 1));
	    editor = ((JSpinner.DefaultEditor) spinner.getEditor());
	    textField = editor.getTextField();
	    textField.addFocusListener(new FocusListener() {
		@Override
		public void focusGained(FocusEvent fe) {
		    //System.err.println("Got focus");
		    //textField.setSelectionStart(0);
		    //textField.setSelectionEnd(1);
		    SwingUtilities.invokeLater(() -> {
			if (valueSet)
			    textField.setCaretPosition(1);
		    });
		}

		@Override
		public void focusLost(FocusEvent fe) {
		}
	    });
	    textField.addActionListener((ActionEvent ae) -> {
		stopCellEditing();
	    });
	}

	// Returns the spinners current value.
	@Override
	public Object getCellEditorValue() {
	    return spinner.getValue();
	}

	// Prepares the spinner component and returns it.
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    if (!valueSet)
		spinner.setValue(value);
	    SwingUtilities.invokeLater(() -> {
		textField.requestFocus();
	    });
	    return spinner;
	}

	@Override
	public boolean isCellEditable(EventObject eo) {
	    //System.err.println("isCellEditable");
	    if (eo instanceof KeyEvent) {
		KeyEvent ke = (KeyEvent) eo;
		//System.err.println("key event: " + ke.getKeyChar());
		textField.setText(String.valueOf(ke.getKeyChar()));
		//textField.select(1,1);
		//textField.setCaretPosition(1);
		//textField.moveCaretPosition(1);
		valueSet = true;
	    } else
		valueSet = false;
	    return true;
	}

	@Override
	public boolean stopCellEditing() {
	    //System.err.println("Stopping edit");
	    try {
		editor.commitEdit();
		spinner.commitEdit();

	    } catch (java.text.ParseException e) {
		JOptionPane.showMessageDialog(null,
			"Invalid value, discarding.");
	    }
	    return super.stopCellEditing();
	}
    }
}
