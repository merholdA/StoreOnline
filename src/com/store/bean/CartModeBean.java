package com.store.bean;

import java.util.Collection;
import java.util.HashMap;

public class CartModeBean {
	private HashMap<String, CartItemBean> cartItemMap = new HashMap<String, CartItemBean>();
	
	public void addCartItem(CartItemBean cartItem) {
		if (cartItem!=null && cartItem.getProduct()!=null) {
			String pid = cartItem.getProduct().getPid();
			if (cartItemMap.containsKey(pid)) {//包含该产品
				CartItemBean cartItemOld = cartItemMap.get(pid);
				cartItemOld.setCount(cartItemOld.getCount()+cartItem.getCount());
			} else {//不包含该产品
				if (pid!=null && pid.length()>0) cartItemMap.put(pid, cartItem);
			}
		}
	}
	
	public void delCartItem(String pid) {
		if (pid!=null && pid.length()>0) cartItemMap.remove(pid);
	}
	
	public void clearCartMode() {
		cartItemMap.clear();
	}

	public HashMap<String, CartItemBean> getCartItemMap() {
		return cartItemMap;
	}

	public void setCartItemMap(HashMap<String, CartItemBean> cartItemMap) {
		this.cartItemMap = cartItemMap;
	}

	public Collection<CartItemBean> getCartItems() {
		return cartItemMap.values();
	}
	
	public double getTotalAmount() {
		double totalAmount = 0;
		Collection<CartItemBean> collection = cartItemMap.values();
		for (CartItemBean item : collection) {
			ProductBean product = item.getProduct();
			if (product!=null) totalAmount += product.getShop_price()*item.getCount();
		}
		return totalAmount;
	}

	public double getPoints() {
		long points = 0;
		Collection<CartItemBean> collection = cartItemMap.values();
		for (CartItemBean item : collection) {
			ProductBean product = item.getProduct();
			if (product!=null) points+=product.getShop_price()*item.getCount();
		}
		return points;
	}
}
