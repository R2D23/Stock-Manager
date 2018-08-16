package stockManager.sale;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import stockManager.DBManager;
import stockManager.Product;

/**
 *
 * @author LuisArturo
 */
public class Sale extends HashMap<Product, SaleInfo> {

    private int id = 0;

    public Sale(){
	this(0);
    }
    
    public Sale(int i) {
	id = i;
    }

    public void add() throws SQLException {
	if (isRegistered())
	    throw new IllegalArgumentException("This sale is already registered!");
	else {
	    Statement stmt = DBManager.createStatement();
	    stmt.executeUpdate("INSERT INTO SALES VALUES(NULL, NULL)");
	    ResultSet rs = DBManager.createStatement().executeQuery("SELECT ID FROM SALES ORDER BY ID DESC");
	    rs.next();
	    id = rs.getInt("ID");
	    rs.close();
	    Iterator i = this.keySet().iterator();
	    Product p;
	    while (i.hasNext()) {
		p = (Product) i.next();
		DBManager.createStatement().executeUpdate("INSERT INTO SALES_ITEMS "
			+ "VALUES(" + id + ", " + p.getId() + ", "
			+ get(p).getPzs() + ", " + get(p).getValue() + ")");
	    }
	}
    }

    public int getId() {
	return id;
    }

    public double getTotal() {
	double aux = 0;
	Iterator i = keySet().iterator();
	while (i.hasNext())
	    aux += get(i.next()).getValue();
	return aux;
    }

    public boolean isRegistered() {
	return id > 0;
    }

    @Override
    public String toString() {
	String out = "Miscelánea Mi Casa:\n\n";
	for (Product p : this.keySet())
	    out += p.getName() + " x" + get(p).getPzs() + " @ $" + get(p).getValue() + "\n\n";
	return out;
    }
    
    public void print(BufferedWriter bw) throws IOException{
	bw.write("Miscelánea Mi Casa:");
	bw.newLine();
	bw.newLine();
	for (Product p : this.keySet()){
	    bw.write(p.getName() + " x" + get(p).getPzs() + " @ $" + get(p).getValue());
	    bw.newLine();
	}
    }
}
