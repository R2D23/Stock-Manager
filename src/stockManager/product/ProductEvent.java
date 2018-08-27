package stockManager.product;
import stockManager.Product;

/**
 *
 * @author LuisArturo
 */
public class ProductEvent {
    public static final int ADDED_PRODUCT = 0;
    public static final int UPDATED_NAME = 1;
    public static final int UPDATED_TAGS = 2;
    public static final int UPDATED_BARCODE = 3;
    public static final int UPDATED_PRICE = 4;
    public static final int UPDATED_ALL = 5;
    public static final int UPDATED_ALL_PRODUCTS = 6;
    public static final int DELETED = 9;
    private Product reference;
    private int event;
    
    public ProductEvent(Product ref, int e){
	reference = ref;
	event = e;
    }

    public Product getReference() {
	return reference;
    }

    public int getEvent() {
	return event;
    }
}
