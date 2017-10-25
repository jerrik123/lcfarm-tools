package com.njq.nongfadai.command.account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.njq.lcfarm.dto.frontend.component.enums.CertificationType;
import com.njq.lcfarm.dto.frontend.component.enums.CustomerType;
import com.njq.nongfadai.common.constant.IConstants;
import com.njq.nongfadai.common.util.CommonUtils;
import com.njq.nongfadai.common.util.PropertiesUtils;
import com.njq.nongfadai.dto.mobile.MsgCodeApplyDto;
import com.njq.nongfadai.dto.mobile.MsgCodeVerifyDto;
import com.njq.nongfadai.dto.user.account.CommonAccountDto;
import com.njq.nongfadai.dubbo.consumer.util.ContextUtil;
import com.njq.nongfadai.enums.PhoneTypeUtil;
import com.njq.nongfadai.service.IMessageService;
import com.njq.nongfadai.service.IUserCapitalAccountService;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：
 * @Package: com.njq.nongfadai.command.account 
 * @author: Jerrik   
 * @date: 2017年10月23日 下午5:57:22
 */
@Component
public class CommonAccount implements CommandMarker {
	public static final Map<Integer, String> containerMap = new ConcurrentHashMap<Integer, String>();

	@CliCommand(value = "account", help = "普通投资用户开户(例如: account --userId 13581 --userName xx --mobileNo xx --idCard xx)")
	public String account(@CliOption(key = "userId", mandatory = true, help = "用户Id") final Integer userId,
			@CliOption(key = "userName", mandatory = true, help = "姓名") final String userName,
			@CliOption(key = "mobileNo", mandatory = true, help = "手机号") final String mobileNo,
			@CliOption(key = "idCard", mandatory = true, help = "身份证号") final String idCard) {
		try {
			System.out.println("zk: " + PropertiesUtils.getValue(IConstants.ZK_ADDRESS));
			CommonAccountDto commonAccountDto = new CommonAccountDto();
			commonAccountDto.setUserId(userId);
			commonAccountDto.setRealName(userName);
			commonAccountDto.setMobileNo(mobileNo);
			commonAccountDto.setCustomerType(CustomerType.NO0);
			commonAccountDto.setCertificationType(CertificationType.NO1);
			commonAccountDto.setCertificationNo(idCard);
			ContextUtil.getContext().getBean(IUserCapitalAccountService.class)
					.inverstorOrBusinessAccount(commonAccountDto);
		} catch (Exception e) {
			return "开户失败-" + e.getMessage();
		}
		return "开户成功";
	}

	@CliCommand(value = "sms", help = "发送短信(sms --userId 13581 --mobileNo xx)")
	public String sms(@CliOption(key = "userId", mandatory = true, help = "用户id") final Integer userId,
			@CliOption(key = "mobileNo", mandatory = true, help = "手机号") final String mobileNo) {
		String uuid = CommonUtils.generateSpecifyLengthUUIDStr(8);
		System.out.println("uuid: " + uuid);
		try {
			IMessageService messageService = ContextUtil.getContext().getBean(IMessageService.class);
			MsgCodeApplyDto msgCodeApplyDto = new MsgCodeApplyDto();
			msgCodeApplyDto.setUserId(userId);
			msgCodeApplyDto.setPhone(mobileNo);
			msgCodeApplyDto.setType(19);
			msgCodeApplyDto.setUuid(uuid);
			containerMap.put(userId, uuid);
			messageService.zjcgBusinessApply(msgCodeApplyDto);
		} catch (Exception e) {
			return "发送短信失败-" + e.getMessage();
		}
		return "发送短信成功-UUID: " + uuid;
	}

	@CliCommand(value = "bind", help = "绑卡(bind --userId 13581 --mobileNo xx --vCode 123456)")
	public String bind(@CliOption(key = "userId", mandatory = true, help = "用户id") final Integer userId,
			@CliOption(key = "mobileNo", mandatory = true, help = "电话号码") final String mobileNo,
			@CliOption(key = "vCode", mandatory = false, help = "短信校验码(默认是123456)") final String vCode) {
		try {
			MsgCodeVerifyDto mcd = new MsgCodeVerifyDto();
			mcd.setCode(StringUtils.isBlank(vCode) ? "123456" : vCode);// 默认验证码
			mcd.setPhone(mobileNo);
			mcd.setType(PhoneTypeUtil.ZJCGOPENACCOUNT.getType());
			mcd.setUserId(userId);
			mcd.setUuid(containerMap.get(userId));
			String transNo = ContextUtil.getContext().getBean(IMessageService.class)
					.getRefValueByRule(mobileNo, PhoneTypeUtil.ZJCGOPENACCOUNT.getType(), containerMap.get(userId));
			mcd.setReference(transNo);// 设置流水号
			System.out.println("uuid: " + containerMap.get(userId));
			System.out.println("生成流水号->" + transNo);
			boolean isSucc = ContextUtil.getContext().getBean(IMessageService.class).sendMsgCodeVerify(mcd);
			if (!isSucc) {
				return "短信校验失败";
			}
			ContextUtil.getContext().getBean(IUserCapitalAccountService.class).bindSHBankCard(userId, transNo);
		} catch (Exception e) {
			return "绑卡失败-" + e.getMessage();
		}
		return "绑卡成功";
	}
}
