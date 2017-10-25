package com.njq.nongfadai.command.account;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.njq.lcfarm.dto.frontend.component.enums.CertificationType;
import com.njq.lcfarm.dto.frontend.component.enums.CustomerType;
import com.njq.nongfadai.common.util.FileUtils;
import com.njq.nongfadai.common.util.IConstants;
import com.njq.nongfadai.common.util.OSInfo;
import com.njq.nongfadai.dto.user.account.CommonAccountDto;
import com.njq.nongfadai.dto.user.account.UserBaseInfoDto;
import com.njq.nongfadai.dubbo.consumer.util.ContextUtil;
import com.njq.nongfadai.exception.ServiceException;
import com.njq.nongfadai.service.IUserCapitalAccountService;
import com.njq.nongfadai.service.IUserService;

/**
 * Copyright 2017 lcfarm All Rights Reserved 
 *  请添加类/接口的说明：针对投资人批量开户
 * @Package: com.njq.nongfadai.command.batch 
 * @author: Jerrik   
 * @date: 2017年10月23日 上午11:32:25
 */
@Component
public class InvestorAccount implements CommandMarker {
	private static Logger LOGGER = LoggerFactory.getLogger(InvestorAccount.class);

	private static final AtomicLong counter = new AtomicLong(0);
	private static final AtomicLong realCounter = new AtomicLong(0);// 实际执行的个数
	private static final AtomicLong succCounter = new AtomicLong(0);
	private static final AtomicLong failCounter = new AtomicLong(0);
	private static final AtomicLong skipCounter = new AtomicLong(0);

	private static IUserCapitalAccountService userCapitalAccountService;
	private static IUserService userService;

	@CliCommand(value = "investor", help = "投资人开户(--sleep xx --from xx --to xx)")
	public String account(@CliOption(key = "sleep", mandatory = false, help = "休眠时间") final Integer sleep,
			@CliOption(key = "from", mandatory = false, help = "开始行数-从1开始") final Integer from,
			@CliOption(key = "to", mandatory = false, help = "结束行数") final Integer to) throws Exception {
		init();
		String filePath = OSInfo.isWindows() ? IConstants.DEFAULT_INVESTOR_FILE_WINDOWS
				: IConstants.DEFAULT_INVESTOR_FILE_LINUX;
		List<String> userList = Files.readLines(new File(filePath), Charset.forName("UTF-8"));
		int total = userList.size();

		println("系统自动统计用户总数: 【%s】", total);
		for (int i = 1, len = userList.size() + 1; i < len; i++) {
			counter.incrementAndGet();

			String userIds = StringUtils.trim(userList.get(i - 1)).replace("\r\n", "").replace("\n", "");
			int userId = NumberUtils.toInt(userIds, -1);

			if (null != from && from > 0 && i < from) {
				skipCounter.incrementAndGet();
				continue;
			}

			if (null != to && to > 0 && i == to) {
				println("系统已运行到--to指定行数,自动退出");
				break;
			}

			realCounter.incrementAndGet();
			println("现在执行第【%s】条数据", i);

			if (-1 == userId) {
				skipCounter.incrementAndGet();
				println("userId: %s ,只能是数字,系统自动跳过执行", userIds);
				continue;
			}

			try {
				UserBaseInfoDto userBaseInfoDto = userService.selectUserBaseInfoById(userId);
				CommonAccountDto commonAccountDto = new CommonAccountDto();
				commonAccountDto.setUserId(userId);
				commonAccountDto.setRealName(userBaseInfoDto.getUserName());
				commonAccountDto.setMobileNo(userBaseInfoDto.getMobileNo());
				commonAccountDto.setCustomerType(CustomerType.NO0);
				commonAccountDto.setCertificationType(CertificationType.NO1);
				commonAccountDto.setCertificationNo(userBaseInfoDto.getCertificationNo());
				userCapitalAccountService.inverstorOrBusinessAccount(commonAccountDto);
				succCounter.incrementAndGet();

				println("  ->用户: %s 开户成功!", userId);
			} catch (Throwable e) {
				FileUtils.writeErrorInfo(userId + IConstants.SEP, IConstants.INVESTOR_ERROR_FILE);
				failCounter.incrementAndGet();
				if (e instanceof ServiceException) {
					ServiceException se = (ServiceException) e;
					println("  ->用户: %s 开户失败.错误原因-> code: %s ,message: %s", userId, se.getCode(), se.getMessage());
				} else {
					println("  ->用户: %s 开户失败.错误原因-> errorMessage: %s", userId, e.getMessage());
					LOGGER.error(e.getMessage(), e);
				}
			} finally {
				if (null != sleep) {
					println("--系统自动休眠--");
					TimeUnit.SECONDS.sleep(sleep);
				}
			}
		}

		printBanner();
		return "borrow = sleep [" + sleep + "] from = [" + from + "]" + " to = [" + to + "]";
	}

	private void printBanner() {
		System.out.println();
		System.out.println();
		println("************************投资人开户结果******************************");
		println("本次批量开户成功用户个数: %s", succCounter.get());
		println("本次批量开户失败用户个数: %s", failCounter.get());
		println("本次批量开户自动跳过个数: %s", skipCounter.get());
		println("本次批量开户用户总个数: %s", realCounter.get());
		println("本次批量开户成功百分数: %s", (succCounter.get() / (realCounter.get() * 1.0)) * 100.0);
		println("******************************************************************");
	}

	private void init() {
		userCapitalAccountService = ContextUtil.getContext().getBean(IUserCapitalAccountService.class);
		userService = ContextUtil.getContext().getBean(IUserService.class);
		if (Arrays.asList(userCapitalAccountService, userService).contains(null)) {
			throw new RuntimeException("获取服务异常!!!");
		}
	}

	public static void println(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	public static void main(String[] args) throws Exception {
		new InvestorAccount().account(null, 4, 8);
	}
}
