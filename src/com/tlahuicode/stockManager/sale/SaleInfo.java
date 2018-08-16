package com.tlahuicode.stockManager.sale;

/**
 *
 * @author LuisArturo
 */
public class SaleInfo {

    private double pzs = 0;
    private double value = 0;

    public SaleInfo(double z, double v) {
	pzs = z;
	value = v;
    }

    public double getPzs() {
	return pzs;
    }

    public double getValue() {
	return value;
    }

}
