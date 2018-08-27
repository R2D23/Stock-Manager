package stockManager.product;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import stockManager.MainFrame;
import stockManager.Product;
import stockManager.sale.SaleInfo;

/**
 *
 * @author LuisArturo
 */
public class ProductPanel extends javax.swing.JPanel {

    private Product product;
    private double semiTotal;

    /**
     * Creates new form ProductPanel
     */
    public ProductPanel() {
	if (product == null)
	    product = new Product();
	initComponents();
	ActionListener al = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		update();
	    }
	};
	nameField.addActionListener(al);
	tagsField.addActionListener(al);
	priceField.addActionListener(al);
	barcodeField.addActionListener(al);
	pzs.getModel().addChangeListener((ChangeEvent e) -> {
	    semiTotal = product.getPrice() * (double) pzs.getValue();
	    semiTotalLabel.setText("$" + semiTotal);
	    SpinnerModel sp = (SpinnerModel) e.getSource();
	    if ((Double) sp.getValue() > 0) {
		this.setBackground(Color.blue);
//		MainFrame.addItemSaleInfo(
//			product,
//			new SaleInfo((double) sp.getValue(),
//				(double) sp.getValue() * product.getPrice()));
	    } else {
		this.setBackground(Color.gray);
		//MainFrame.removeItemSaleInfo(product);
	    }
	});
    }

    public ProductPanel(Product p) {
	this();
	changeProduct(p);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        nameField = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        barcodeField = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        priceField = new javax.swing.JTextField();
        tagsField = new javax.swing.JTextField();
        pzs = new javax.swing.JSpinner();
        semiTotalLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 204, 0));

        idLabel.setText("#");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        nameField.setText("Name");
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        barcodeField.setText("Barcode");

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("$");

        priceField.setText("1000");

        tagsField.setText("Tags");

        pzs.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 99.9d, 1.0d));
        pzs.setEnabled(false);
        pzs.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pzsPropertyChange(evt);
            }
        });

        semiTotalLabel.setText("$0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(idLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tagsField, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barcodeField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pzs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(semiTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(idLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(nameField)
            .addComponent(jSeparator2)
            .addComponent(jSeparator3)
            .addComponent(barcodeField)
            .addComponent(jSeparator4)
            .addComponent(tagsField)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(pzs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(semiTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
	update();
    }//GEN-LAST:event_nameFieldActionPerformed

    private void pzsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pzsPropertyChange
	semiTotal = product.getPrice() * (double) pzs.getValue();
	semiTotalLabel.setText("$" + semiTotal);
    }//GEN-LAST:event_pzsPropertyChange

    private void update() {
	try {
	    if (!product.isRegistered()) {
		product.setName(nameField.getText());
		product.setTags(tagsField.getText());
		product.setPrice(Double.parseDouble(priceField.getText()));
		product.setBarcode(barcodeField.getText());
		if (product.validateName()
			&& product.validateTags()
			&& product.validatePrice()) {
		    product.addProduct();
		    barcodeField.setText(product.getBarcode());
		    this.setBackground(Color.gray);
		    pzs.setEnabled(true);
		    idLabel.setText("#" + product.getId());
		}
	    } else if (product.validateName(nameField.getText())
		    && product.validateBarcode(barcodeField.getText()) && product.validatePrice(Double.parseDouble(priceField.getText()))) {
		product.setName(nameField.getText());
		product.setTags(tagsField.getText());
		product.setPrice(Double.parseDouble(priceField.getText()));
		product.setBarcode(barcodeField.getText());
	    } else {
		nameField.setText(product.getName());
		tagsField.setText(product.getTags());
		barcodeField.setText(product.getBarcode());
		priceField.setText(String.valueOf(product.getPrice()));
	    }
	} catch (IllegalArgumentException | SQLException ex) {
	    Logger.getLogger(ProductPanel.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void changeProduct(Product p) {
	if (p == null)
	    throw new IllegalArgumentException();
	nameField.setText(p.getName());
	tagsField.setText(p.getTags());
	barcodeField.setText(p.getBarcode());
	priceField.setText(String.valueOf(p.getPrice()));
	if (p.isRegistered()) {
	    this.setBackground(Color.gray);
	    idLabel.setText("#" + p.getId());
	    pzs.setEnabled(true);
	} else {
	    this.setBackground(Color.green);
	    pzs.setEnabled(false);
	}
	product = p;
    }

    public Product getProduct() {
	return product;
    }

    @Override
    public String toString() {
	return product + " (" + super.toString() + ")";
    }

    public class PInputVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
	    if (!(((JSpinner) input).getValue() instanceof Number))
		return false;
	    int aux = ((int) ((JSpinner) input).getValue());
	    return aux >= 0 && aux < 10;
	}

    }

    public double getSemiTotal() {
	return semiTotal;
    }

    public void setPzs(double p) {
	pzs.setValue(p);
    }

    public double getPzs() {
	return (double) pzs.getValue();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barcodeField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField priceField;
    private javax.swing.JSpinner pzs;
    private javax.swing.JLabel semiTotalLabel;
    private javax.swing.JTextField tagsField;
    // End of variables declaration//GEN-END:variables
}