package com.store.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.store.bean.CategoryBean;
import com.store.bean.ProductBean;
import com.store.bean.ProductPagingBean;
import com.store.service.DatabaseService;
import com.store.service.impl.DatabaseServiceImpl;
import com.store.utils.UUIDUtils;
import com.store.utils.UploadUtils;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private String getAllCategories(HttpServletRequest req, HttpServletResponse resp) {
		//从数据库中读取所有分类
		DatabaseService service = new DatabaseServiceImpl();
		List<CategoryBean> categoryList = service.getAllCategory();
		
		req.setAttribute("categoryList", categoryList);
		return "admin/category/list.jsp";
	}
	
	private String intoAddCategoryUI(HttpServletRequest req, HttpServletResponse resp) {
		return "admin/category/add.jsp";
	}
	
	private String addCategory(HttpServletRequest req, HttpServletResponse resp) {
		String cname = req.getParameter("cname");
		//重要: 应判断 是否 重复 或 为空
		if (null==cname || cname.length()<=0) {
			req.setAttribute("msg", "分类名称不能为空~");
			return "jsp/info.jsp";
		}
		CategoryBean category = new CategoryBean();
		category.setCid(UUIDUtils.getCode());
		category.setCname(cname);
		DatabaseService service = new DatabaseServiceImpl();
		int result = service.addCategory(category);
		if (result<=0) {
			req.setAttribute("msg", "分类信息添加失败~");
			return "jsp/info.jsp";
		}
		//分类信息插入成功
		/**
		 * 此处需 删除 Redis中的分类缓存信息  以便重新获取MySQL中的分类信息 然后再次保存到Redis中
		 */
		try {
			resp.sendRedirect(getServletContext().getContextPath()+"/AdminServlet?method=getAllCategories");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String getProductsByPaging(HttpServletRequest req, HttpServletResponse resp) {
		int pageSize = 10;//每页大小为12项
		int pageIndex = Integer.valueOf(req.getParameter("pageIndex"));
		DatabaseService service = new DatabaseServiceImpl();
		ProductPagingBean ppBean = service.getProductsByPaging(pageSize, pageIndex);
		req.setAttribute("page", ppBean);
		return "admin/product/list.jsp";
	}
	
	private String intoAddProductUI(HttpServletRequest req, HttpServletResponse resp) {
		//查询所有分类
		DatabaseService service = new DatabaseServiceImpl();
		List<CategoryBean> categoryList = service.getAllCategory();
		req.setAttribute("categoryList", categoryList);
		return "admin/product/add.jsp";
	}
	
	private String addProduct(HttpServletRequest req, HttpServletResponse resp) {
		Map<String, String> productMap = new HashMap<>();
		ProductBean product = new ProductBean();
		
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		ServletFileUpload sfUpload = new ServletFileUpload(dfif);
		
		try {
			List<FileItem> itemList = sfUpload.parseRequest(req);
			for (FileItem item : itemList) {
				if (item.isFormField()) {
					productMap.put(item.getFieldName(), item.getString("utf-8"));
				} else {
					String srcFileName = item.getName();//源 文件 名
					String desFileName = UploadUtils.getUUIDName(srcFileName);//目标 文件 名 
					//存放路径 很重要 使文件均匀分布 提高 读写 效率
					String domainPath = getServletContext().getRealPath("/products/3/");//开始地址
					String indexPath = UploadUtils.getDir(desFileName);//相对路径 只是路径 没有文件名
					String filePath = domainPath+indexPath;//全路径
					File filePathDir = new File(filePath);
					if (!filePathDir.isDirectory()) {
						filePathDir.mkdirs();
					}
					
					File desFile = new File(filePathDir, desFileName);
					if (!desFile.isFile()) {
						desFile.createNewFile();
					}
					InputStream is = item.getInputStream();
					OutputStream os = new FileOutputStream(desFile);
					//保存文件
					IOUtils.copy(is, os);
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
					
					productMap.put("pimage", "products/3/"+indexPath+"/"+desFileName);
				}
			}
			
			//7_利用BeanUtils将MAP中的数据填充到Product对象上
			BeanUtils.populate(product, productMap);
			product.setPid(UUIDUtils.getId());
			product.setPdate(new Date());
			product.setPflag(0);
			
			//8_调用servcie_dao将user上携带的数据存入数据仓库,重定向到查询全部商品信息路径
			DatabaseService service=new DatabaseServiceImpl();
			service.addProduct(product);
			
			resp.sendRedirect(getServletContext().getContextPath()+"/AdminServlet?method=getProductsByPaging&pageIndex=1");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return "";
	}
}
