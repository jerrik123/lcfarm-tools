package com.njq.nongfadai.common.provider;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.extention 
 * @author: Jerrik   
 * @date: 2017年10月23日 上午10:55:45
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BannerProvider extends DefaultBannerProvider {

	public String getBanner() {
		StringBuffer buf = new StringBuffer();
		buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
		buf.append("*                                      *" + OsUtils.LINE_SEPARATOR);
		buf.append("*              Lcfarm Tools            *" + OsUtils.LINE_SEPARATOR);
		buf.append("*                                      *" + OsUtils.LINE_SEPARATOR);
		buf.append("=======================================" + OsUtils.LINE_SEPARATOR);
		buf.append("Version:" + this.getVersion());
		return buf.toString();
	}

	public String getVersion() {
		return "1.0.0";
	}

	public String getWelcomeMessage() {
		return "Welcome to use njq command tools";
	}

	@Override
	public String getProviderName() {
		return "Njq Utility Banner";
	}

}
