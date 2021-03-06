package com.tlahuicode.stockManager;

import com.tlahuicode.stockManager.sale.SaleTable;
import com.tlahuicode.stockManager.sale.Sale;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;

/**
 *
 * @author LuisArturo
 */
public class CashPanel extends javax.swing.JPanel {

    private static CashPanel cp;
    private static JPanel productsPanel;
    private static Sale sale = new Sale(0);
    private static SaleTable st;
    private static SaleTable minTable;
    private static double received;

    /**
     * This method returns the current, static sale.
     * <p>
     * Planned to be recoded, this method implies that there is only one,
     * workable, <code>Sale</code> main instance. Future releases may implement
     * multiple, workable <code>Sale</code> instances.
     * <p>
     * @return The main Sale instance.
     */
    public static Sale getSale() {
	return sale;
    }

    /**
     * This method will update the displayed total amount needed to be paid.
     * <p>
     * Simply enough, calling this method will take the current total of the
     * main <code>Sale</code> instance.
     */
    public static void updateTotal() {
	NumberFormat df = DecimalFormat.getCurrencyInstance();
	df.setCurrency(Currency.getInstance(Locale.US));
	cp.totalField.setText(df.format(sale.getTotal()));
    }

    /**
     * This method executes all needed sequences in order to register a sale.
     */
    public static void sale() {
	try {
	    sale.add();
	    String fName = "Ticket" + sale.getId() + ".txt";
	    FileWriter f = new FileWriter(fName, true);
	    BufferedWriter writer = new BufferedWriter(f);
	    sale.print(writer);
	    writer.close();
	    Desktop.getDesktop().open(new File(fName));
	    ArrayList<Product> aux = new ArrayList<>();
	    Iterator i = sale.keySet().iterator();
	    while (i.hasNext())
		aux.add((Product) i.next());
	    sale = new Sale(0);
	    updateTotal();
	} catch (SQLException | IOException ex) {
	    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * Creates new form CashPanel
     * <p>
     * @throws java.sql.SQLException
     */
    public CashPanel() throws SQLException {
	sale = new Sale(0);
	initComponents();
	cp = this;
	value.setFormatterFactory(new AbstractFormatterFactory() {
	    @Override
	    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
		return new AbstractFormatter() {

		    @Override
		    public Object stringToValue(String text) throws ParseException {
			String aux = text.replaceAll("[\\D&&[^\\.\\,]]", "");
			try {
			    return Double.parseDouble(aux);
			} catch (NumberFormatException e) {
			    return 0;
			}
		    }

		    @Override
		    public String valueToString(Object value) throws ParseException {
			if (value == null || !(value instanceof Number))
			    value = 0;
			return DecimalFormat.getCurrencyInstance().format(value);
		    }

		};
	    }
	}
	);
	productsPanel = new JPanel();
	productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
	st = new SaleTable();
	sPane.setViewportView(st);
	sPane.getVerticalScrollBar().setUnitIncrement(20);
	minTable = new SaleTable(true);
	min.setViewportView(minTable);
	st.getModel().addTableModelListener((TableModelEvent e) -> {
	    updateTotal();
	    minTable.updateFilter();
	    minTable.repaint();
	});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searcher = new javax.swing.JTextField();
        sPane = new javax.swing.JScrollPane();
        payButton = new javax.swing.JButton();
        totalField = new javax.swing.JLabel();
        min = new javax.swing.JScrollPane();
        jButton1 = new javax.swing.JButton();
        value = new JFormattedTextField(NumberFormat.getCurrencyInstance());
        changeLabel = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        searcher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searcherActionPerformed(evt);
            }
        });
        searcher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searcherKeyTyped(evt);
            }
        });

        sPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        payButton.setText("Pagar");
        payButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payButtonActionPerformed(evt);
            }
        });

        totalField.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalField.setText("$ 0");

        jButton1.setText("+");

        value.setText("0");
        value.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        value.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valueActionPerformed(evt);
            }
        });

        changeLabel.setText("Change:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sPane, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searcher, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(min, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searcher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(changeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(min, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(sPane))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searcherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searcherActionPerformed
	st.setFilter(searcher.getText());
    }//GEN-LAST:event_searcherActionPerformed

    private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
	sale();
	minTable.updateFilter();
	value.postActionEvent();
    }//GEN-LAST:event_payButtonActionPerformed

    private void valueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valueActionPerformed
	received = Double.valueOf(value.getText().replaceAll("[\\D&&[^\\.\\,]]", ""));
	changeLabel.setText("Change: "
		+ NumberFormat.getCurrencyInstance().format(received - sale.getTotal()));
    }//GEN-LAST:event_valueActionPerformed

    private void searcherKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searcherKeyTyped
        st.setFilter(searcher.getText());
    }//GEN-LAST:event_searcherKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel changeLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane min;
    private javax.swing.JButton payButton;
    private javax.swing.JScrollPane sPane;
    private javax.swing.JTextField searcher;
    private javax.swing.JLabel totalField;
    private javax.swing.JFormattedTextField value;
    // End of variables declaration//GEN-END:variables

}
