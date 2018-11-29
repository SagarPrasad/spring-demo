/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author sagar
 */
@Component
public class CustomConnectionBean implements FactoryBean<CustomBeanConnectionFactory> {

  boolean primary = false;

  @Override
  public CustomBeanConnectionFactory getObject() throws Exception {
    // u can use bean
    return primary ? new CustomConnectA() : new CustomConnectB();
  }

  @Override
  public Class<?> getObjectType() {
    return CustomBeanConnectionFactory.class;
  }
}


class CustomConnectA implements  CustomBeanConnectionFactory {
  @Override
  public String getBeanName() {
    return "CustomConnectA";
  }
}

class CustomConnectB implements  CustomBeanConnectionFactory {

  @Override
  public String getBeanName() {
    return "CustomConnectB";
  }

}