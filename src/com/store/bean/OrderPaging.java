package com.store.bean;

import java.util.List;

public class OrderPaging {
	
	private int pageSize;//每页数据大小
	private int pageCount;//总页数
	private int pageIndex;//当前页码
	private List<OrderBean> orderList;//当前页对应的数据
	
	public OrderPaging() {}
	
	public OrderPaging(int pageSize, int pageCount, int pageIndex, List<OrderBean> orderList) {
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.pageIndex = pageIndex;
		this.orderList = orderList;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public List<OrderBean> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderBean> orderList) {
		this.orderList = orderList;
	}
}
