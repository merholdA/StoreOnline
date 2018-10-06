package com.store.bean;

import java.util.List;

public class ProductPagingCgyBean {
	private String cid;
	private String cname;
	private int pageSize;//每页数据大小
	private int pageCount;//总页数
	private int pageIndex;//当前页码
	private List<ProductBean> productList;//当前页对应的数据
	
	
	public ProductPagingCgyBean(String cid, String cname, int pageSize, int pageCount, int pageIndex) {
		this.cid = cid;
		this.cname = cname;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.pageIndex = pageIndex;
	}
	
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
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
	public List<ProductBean> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductBean> productList) {
		this.productList = productList;
	}
}
