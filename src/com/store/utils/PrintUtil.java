package com.store.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class PrintUtil {

	public static void printServletRequestParameterMap(HttpServletRequest req) {
		Map<String, String[]> map = req.getParameterMap();
		Set<String> set = map.keySet();
		if (set!=null && set.size()>0) {
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				String[] strings = map.get(key);
				StringBuilder sb = new StringBuilder();
				for (String str : strings) {
					sb.append(str).append("  ");
				}
				System.out.println(key+" : "+sb.toString());
			}
		}
	}
}
