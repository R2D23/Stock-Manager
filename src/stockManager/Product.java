package stockManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import stockManager.product.*;

/**
 *
 * @author R2D2 3
 */
public class Product {

    private static Map<Integer, Product> idMap;
    private static ArrayList<ProductListener> pls = new ArrayList();

    public static void addListener(ProductListener p) {
	pls.add(p);
    }

    /**
     * Updates the map that models the Product list from the Database.
     * @throws SQLException 
     */
    public synchronized static void updateAllProducts() throws SQLException {
	if (idMap == null || idMap.keySet().size() != getCount()) {
	    System.out.println("Getting all Products form Database!");
	    ResultSet rs = DBManager.createStatement().executeQuery("SELECT * FROM PRODUCTS");
	    idMap = new HashMap<>();
	    while (rs.next()) {
		int idAux = rs.getInt("ID");
		String nameAux = rs.getNString("NAME");
		String tagsAux = rs.getNString("TAGS");
		if (tagsAux == null)
		    tagsAux = "";
		String bAux = rs.getNString("BARCODE");
		double pAux = rs.getDouble("PRICE");
		Product p = new Product(idAux, nameAux, tagsAux, bAux, pAux);
		idMap.put(idAux, p);
	    }
	    rs.close();
	    for (ProductListener pl : pls)
		pl.updatedProducts(new ProductEvent(null, ProductEvent.UPDATED_ALL_PRODUCTS));
	}
    }

    public synchronized static int getCount() throws SQLException {
	ResultSet rsc = DBManager.createStatement().executeQuery("SELECT COUNT(ID) FROM PRODUCTS");
	rsc.next();
	int aux = rsc.getInt(1);
	rsc.close();
	return aux;
    }

    public static ArrayList<Integer> getIDs(){
	ArrayList<Integer> aux = new ArrayList<>();
	Iterator<Integer> i = idMap.keySet().iterator();
	while (i.hasNext())
	    aux.add(i.next());
	return aux;
    }

    public static Product getProductByID(int i) {
	return idMap.get(i);
    }
    
    private String barcode;
    private int id = 0;
    private String name;
    private double price;
    private String tags;

    public Product() {
	name = "Default";
	tags = "";
	barcode = "";
	price = 0;
    }

    public Product(int i, String n, String t, String b, double p) {
	if(n == null || t == null || b == null)
	    throw new IllegalArgumentException();
	id = i;
	name = n;
	tags = t;
	barcode = b;
	price = p;
    }

    public synchronized void addProduct() throws SQLException {
	if(isRegistered())
	    return;
	if (validateName() && validateTags() && validatePrice()) {
	    if (validateBarcode())
		DBManager.createStatement().executeUpdate("INSERT INTO PRODUCTS "
			+ "(NAME, TAGS, BARCODE, PRICE) "
			+ "VALUES "
			+ "('" + name.replace("'", "''") + "', '" +
			tags.replace("'", "''") + "', '" +
			barcode + "', " + price + ")");
	    else
		DBManager.createStatement().executeUpdate("INSERT INTO PRODUCTS "
			+ "(NAME, TAGS, PRICE) "
			+ "VALUES "
			+ "('" + name.replace("'", "''") +
			"', '" + tags.replace("'", "''") +
			"', " + price + ")");
	    ResultSet rs = DBManager.createStatement().executeQuery("SELECT ID FROM PRODUCTS ORDER BY ID DESC");
	    rs.next();
	    id = rs.getInt("ID");
	    rs.close();
	    {
		ResultSet rsb = DBManager.createStatement().executeQuery("SELECT * FROM PRODUCTS WHERE ID = " + id);
		rsb.next();
		barcode = rsb.getNString("BARCODE");
		rsb.close();
	    }
	    idMap.put(id, this);
	    for (ProductListener pl : pls)
		pl.addedProduct(new ProductEvent(this, ProductEvent.ADDED_PRODUCT));
	} else
	    throw new IllegalArgumentException("The Product " + this + " is not valide!");
    }

    public synchronized void delete() throws SQLException {
	if (isRegistered())
	    DBManager.createStatement().executeUpdate(
		    "DELETE FROM PRODUCTS "
		    + "WHERE ID = " + id);
	id = 0;
	idMap.remove(id);
	for (ProductListener pl : pls)
	    pl.erasedProduct(new ProductEvent(this, ProductEvent.DELETED));
    }

    public String getBarcode() {
	return barcode;
    }

    public synchronized void setBarcode(String b) throws SQLException {
	if (b == null)
	    b = "";
	if (!validateBarcode(b) && isRegistered())
	    throw new IllegalArgumentException();
	if (isRegistered())
	    DBManager.createStatement().executeUpdate(
		    "UPDATE PRODUCTS "
		    + "SET BARCODE = '" + b + "' "
		    + "WHERE ID = " + id);
	barcode = b;
	pls.stream().forEach((pl) -> {
	    pl.updatedProduct(new ProductEvent(this, ProductEvent.UPDATED_BARCODE));
	});
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public synchronized void setName(String n) throws SQLException {
	if (n == null || !validateName(n))
	    throw new IllegalArgumentException("The name is not valid!");
	if (isRegistered())
	    DBManager.createStatement().executeUpdate(
		    "UPDATE PRODUCTS "
		    + "SET NAME = '" + n.replace("'", "''") + "' "
		    + "WHERE ID = " + id);
	name = n;
	for (ProductListener pl : pls)
	    pl.updatedProduct(new ProductEvent(this, ProductEvent.UPDATED_NAME));
    }

    public double getPrice() {
	return price;
    }

    public synchronized void setPrice(double p) throws SQLException {
	if (!validatePrice(p))
	    throw new IllegalArgumentException("The price is not valid!");
	if (isRegistered())
	    DBManager.createStatement().executeUpdate(
		    "UPDATE PRODUCTS "
		    + "SET PRICE = '" + p + "' "
		    + "WHERE ID = " + id);
	price = p;
	for (ProductListener pl : pls)
	    pl.updatedProduct(new ProductEvent(this, ProductEvent.UPDATED_PRICE));
    }

    public String getTags() {
	return tags;
    }

    public synchronized void setTags(String t) throws SQLException {
	if (t == null)
	    t = "";
	if (isRegistered())
	    DBManager.createStatement().executeUpdate(
		    "UPDATE PRODUCTS "
		    + "SET TAGS = '" + t.replace("'", "''") + "' "
		    + "WHERE ID = " + id);
	tags = t;
	for (ProductListener pl : pls)
	    pl.updatedProduct(new ProductEvent(this, ProductEvent.UPDATED_TAGS));
    }

    public boolean isRegistered() {
	return id > 0;
    }

    @Override
    public String toString() {
	return "#" + id + " " + name + " [" + tags + "] " + barcode + " @ $ " + price;
    }
    
    public synchronized boolean validate() throws SQLException{
	return validateName() && validateTags() && validateBarcode() && validatePrice();
    }

    public synchronized boolean validateBarcode(String b) throws SQLException {
	if (isRegistered() && barcode.equals(b))
	    return true;
	ResultSet rs = DBManager.createStatement().executeQuery(
		"SELECT COUNT(ID) FROM PRODUCTS "
		+ "WHERE BARCODE = '" + b + "'");
	rs.next();
	boolean aux = rs.getInt(1) <= 0;
	rs.close();
	return aux;
    }

    public boolean validateBarcode() throws SQLException {
	return validateBarcode(this.barcode);
    }

    public synchronized boolean validateName(String n) throws SQLException {
	if (isRegistered() && name.equals(n))
	    return true;
	ResultSet rs = DBManager.createStatement().executeQuery(
		"SELECT COUNT(ID) FROM PRODUCTS "
		+ "WHERE NAME = '" + n.replace("'", "''") + "'");
	rs.next();
	boolean auxB = rs.getInt(1) <= 0;
	rs.close();
	return auxB;
    }

    public boolean validateName() throws SQLException {
	return validateName(this.name);
    }

    public boolean validatePrice(double price) {
	return price > 0;
    }

    public boolean validatePrice() throws SQLException {
	return validatePrice(this.price);
    }

    public boolean validateTags() throws SQLException {
	return validateName(this.tags);
    }
}
