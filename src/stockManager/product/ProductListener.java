package stockManager.product;

/**
 *
 * @author LuisArturo
 */
public abstract class ProductListener {
    public abstract void addedProduct(ProductEvent evt);
    public abstract void updatedProduct(ProductEvent evt);
    public abstract void erasedProduct(ProductEvent evt);
    public abstract void updatedProducts(ProductEvent evt);
}
