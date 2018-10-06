package com.store.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mchange.v2.c3p0.util.TestUtils;
import com.store.bean.CategoryBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;
import com.store.utils.JedisUtils;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

public class CategoryServlet extends BaseServlet {
	private static final long serialVersionUID = 4L;
	
	/**
	 * 获取所有商品分类
	 * @param request
	 * @param response
	 * @return
	 */
	private String getAllCategory(HttpServletRequest request, HttpServletResponse response) {
		String categoryJsonStr = "";
		//一. 先尝试从redis中获取分类信息的json字符串
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getJedis();
			categoryJsonStr = jedis.get("AllCategory");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("0. categoryJsonStr="+categoryJsonStr);
		if (null==categoryJsonStr || categoryJsonStr.length()<=0) {
			//二. redis中不存在的话 从mySQL中获取
			DatabaseService service = new DatabaseServiceImpl();
			List<CategoryBean> categoryList = service.getAllCategory();
			if (categoryList!=null && categoryList.size()>0) {
				categoryJsonStr = JSONArray.fromObject(categoryList).toString();
			}
			System.out.println("1. categoryJsonStr="+categoryJsonStr);
			try {
				if (jedis!=null && categoryJsonStr!=null && categoryJsonStr.length()>0) jedis.set("AllCategory", categoryJsonStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//三.返回给客户端
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(categoryJsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (jedis!=null) jedis.close();
		return "";
	}
}
