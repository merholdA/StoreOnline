package com.store.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

import com.store.bean.CategoryBean;
import com.store.bean.ProductBean;
import com.store.bean.UserBean;
import com.store.dao.CategoryDao;
import com.store.dao.ProductDao;
import com.store.dao.UserDao;
import com.store.dao.impl.CategoryDaoImpl;
import com.store.dao.impl.ProductDaoImpl;
import com.store.dao.impl.UserDaoImpl;

public class TestDemo {

	@Test
	public void testUser() {
		UserDao dao = new UserDaoImpl();
		try {
			UserBean user = new UserBean();
			user.setUid("f55b7d3a352a4f0782c910b2c70f1ea4");
			user.setUsername("aaa");
			user.setPassword("aaa");
			user.setName("小王");
			user.setEmail("aaa@store.com");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			user.setBirthday(format.parse("2000-02-03"));
			user.setSex("女");
			user.setState(0);
			user.setCode("1258e96181a9457987928954825189000bae305094a042d6bd9d2d35676684ee");
			user.setTelephone("15712344888");
			int id = dao.insertUser(user);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCategory() {
		CategoryDao dao = new CategoryDaoImpl();
		try {
			CategoryBean category = new CategoryBean();
			category.setCid("afdba41a139b4320a74904485bdb7719");
			category.setCname("汽车用品");
			int id = dao.insertCategory(category);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testProduct() {
		ProductDao dao = new ProductDaoImpl();
		try {
			ProductBean product = new ProductBean();
			product.setPid("99");
			product.setPname("肖申克的救赎HD蓝光版");
			product.setMarket_price(999);
			product.setShop_price(799);
			product.setPimage("products/1/c_0999.jpg");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			product.setPdate(format.parse("2016-11-02"));
			product.setIs_hot(1);
			product.setPdesc("经典电影~经典蓝光典藏版！");
			product.setPflag(0);
			product.setCid("5");
			int id = dao.insertProduct(product);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
