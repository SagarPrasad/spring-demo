/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.factorybean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author sagar
 */
@Slf4j
@Component
class ConnectionFactoryDemo implements InitializingBean {

	@Autowired
	public CustomConnectionBean customConnectionBean;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(customConnectionBean, "customFactoryBean can't be null");
		log.info("--------->> " + customConnectionBean.getObject().getBeanName()); ;
	}
}
