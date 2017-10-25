package com.njq.nongfadai.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.common.util 
 * @author: Jerrik   
 * @date: 2017年10月23日 下午8:32:19
 */
public class PropertiesUtils {
	private static final Map<String, String> propsMap = new HashMap<String, String>();

	public static void addAllProperties(Properties props) {
		for (Entry<Object, Object> entry : props.entrySet()) {
			propsMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
	}

	public static String getValue(String propsKey) {
		return propsMap.get(propsKey);
	}
}
