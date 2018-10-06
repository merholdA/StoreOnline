package com.store.bean;

public class CartItemBean {
	private ProductBean product;
	private int count;
	
	public ProductBean getProduct() {
		return product;
	}
	public void setProduct(ProductBean product) {
		this.product = product;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		if (count<0) count = 0;
		
		this.count = count;
	}
	public double getSubTotal() {
		double subTotal = 0;
		if (product!=null) {
			subTotal = product.getShop_price()*count;
		}
		if (subTotal<0) subTotal = 0;
		
		return subTotal;
	}
}
