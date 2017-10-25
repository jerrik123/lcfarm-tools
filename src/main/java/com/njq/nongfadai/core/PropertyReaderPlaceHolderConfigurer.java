package com.njq.nongfadai.core;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.njq.nongfadai.common.util.PropertiesUtils;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.core 
 * @author: Jerrik   
 * @date: 2017年10月23日 下午8:34:04
 */
public class PropertyReaderPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected Properties mergeProperties() throws IOException {
		PropertiesUtils.addAllProperties(super.mergeProperties());
		return super.mergeProperties();
	}
}
