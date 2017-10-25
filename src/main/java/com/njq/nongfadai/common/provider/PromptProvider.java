package com.njq.nongfadai.common.provider;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.extention 
 * @author: Jerrik   
 * @date: 2017年10月23日 上午10:55:04
 */
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PromptProvider extends DefaultPromptProvider {

	@Override
	public String getPrompt() {
		return "njq>";
	}

	@Override
	public String getProviderName() {
		return "#";
	}

}
