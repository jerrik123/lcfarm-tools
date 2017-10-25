
package com.njq.nongfadai.common.util;
/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.common.util 
 * @author: Jerrik   
 * @date: Oct 24, 2017 4:56:12 PM
 */
public interface IConstants {

	
	/**
	 * 投资人
	 */
	public static final String DEFAULT_INVESTOR_FILE_WINDOWS = "D:\\p2p\\investor-list.txt";
	public static final String DEFAULT_INVESTOR_FILE_LINUX = "/home/webapps/p2p/investor-list.txt";
	
	/**
	 * 错误文件目录
	 */
	public static final String DEFAULT_DIR_WINDOWS = "D:\\p2p";
	public static final String DEFAULT_DIR_LINUX = "/home/webapps/p2p";
	
	/**
	 * 错误开户文件
	 */
	public static final String INVESTOR_ERROR_FILE = "error-investor-list.txt";
	
	public static final String SEP = "\r\n";
}

