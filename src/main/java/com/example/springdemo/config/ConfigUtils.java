/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author sagar
 */
@Component
@Slf4j
public class ConfigUtils {
  
  private static final String CGLIB_CLASS_SEPARATOR = "$$";
  private static final String CGLIB_RENAMED_FIELD_PREFIX = "CGLIB$";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment configurableEnvironment;


  public void updateAppConfigs(Map<String, Object> updateKeyValue) {
    Map<String, Field> configs = getValueFieldList();
    configs.putAll(getConfigList());
    for(String key : updateKeyValue.keySet()) {
      try {
        Field field = configs.get(key);
        if (null != field) {
          boolean accessibility = field.isAccessible();
          PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
          Object ultimateTarget = getUltimateTargetBeanOfField(field);
          field.setAccessible(true);
          if (null != editor) {
            editor.setAsText((String)updateKeyValue.get(key));
            field.set(ultimateTarget, editor.getValue());
          } else {
            if (field.getType().getName().equalsIgnoreCase("java.util.Set")) {
              List<String> stringList = (List<String>) updateKeyValue.get(key);
              Set<String> stringSet = new HashSet<>(stringList);
              field.set(ultimateTarget, stringSet);
            }
            if (field.getType().getName().equalsIgnoreCase("java.util.List")) {
              List<String> stringList = (List<String>) updateKeyValue.get(key);
              field.set(ultimateTarget, stringList);
            }
            if (field.getType().getName().equalsIgnoreCase("java.util.Map")) {
              Map<String, String> mapList = (Map<String, String>) updateKeyValue.get(key);
              field.set(ultimateTarget, mapList);
            }
            if (field.getType().getName().equalsIgnoreCase("java.math.BigDecimal")) {
              BigDecimal stringBigDecimal = new BigDecimal((Integer)updateKeyValue.get(key)) ;
              field.set(ultimateTarget, stringBigDecimal);
            }
            if (field.getType().getName().equalsIgnoreCase("java.lang.Integer")) {
              Integer stringInteger = (Integer) updateKeyValue.get(key) ;
              field.set(ultimateTarget, stringInteger);
            }
          }
          field.setAccessible(accessibility);
        }
      } catch (Exception ex) {
        log.error("op=updateConfig, status=KO, desc=Config update failed.", ex);
        throw new RuntimeException("Oops! Failed to update config."); //NOPMD
      }
    }
  }


  public Map<String, Object> getAppConfigs() {
    Map<String, Object> response =new TreeMap<>();
    Map<String, Field> configs = getValueFieldList();
    configs.putAll(getConfigList());
    configs.forEach((key, field) -> {
      try {
        Object ultimateTarget = getUltimateTargetBeanOfField(field);
        boolean accessibility = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(ultimateTarget);
        field.setAccessible(accessibility);
        response.put(key, value);
      } catch (Exception ex) {
        log.warn("op=getConfig, status=KO, desc=Failed for configs field={}, error={}", field.getName(), ex);
      }
    });
    return response;
  }

  private Map<String, Field> getConfigList() {
    Map<String, Field> configs = new HashMap<>();
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Configuration.class);
    beansWithAnnotation.forEach((key, value) ->
            configs.putAll(getUltimateTargetField(FieldUtils.getAllFieldsList(value.getClass()))));
    return configs;
  }

  private Map<String, Field> getValueFieldList() {
    Map<String, Field> configs = new HashMap<>();
    for (String beanName : applicationContext.getBeanDefinitionNames()) {
      Object bean = applicationContext.getBean(beanName);
      if (bean != null) {
        configs.putAll(getUltimateTargetField(FieldUtils.getFieldsListWithAnnotation(bean.getClass(), Value.class)));
      }
    }
    return configs;
  }

  private Map<String, Field> getUltimateTargetField(List<Field> fields) {
    return Optional.ofNullable(fields)
            .orElseGet(Collections::emptyList).stream()
            .filter(field -> field.getDeclaringClass().getCanonicalName() != null
                    && field.getDeclaringClass().getCanonicalName().startsWith("com.example")
                    && !field.getName().startsWith(CGLIB_CLASS_SEPARATOR)
                    && !field.getName().startsWith(CGLIB_RENAMED_FIELD_PREFIX)
                    && !Modifier.isFinal(field.getModifiers()))
            .collect(Collectors.toMap(field -> field.getDeclaringClass().getSimpleName() + "." + field.getName(),
                    field -> field, (oldValue, newValue) -> oldValue));
  }

  private Object getUltimateTargetBeanOfField(Field field) {
    Object bean;
    try {
      bean = applicationContext.getBean(StringUtils.uncapitalize(field.getDeclaringClass().getSimpleName()));
    } catch (Exception ex) {
      bean = applicationContext.getBean(field.getDeclaringClass());
    }
    bean = getUltimateTargetBean(bean);
    return bean;
  }

  private Object getUltimateTargetBean(Object bean) {
    try {
      if (AopUtils.isAopProxy(bean) && (bean instanceof Advised)) {
        return getUltimateTargetBean(((Advised) bean).getTargetSource().getTarget());
      }
    } catch (Exception ex) {
      log.warn("op=getUltimateTargetBean, status=KO, desc=Failed to get target bean, error={}", ex);
    }
    return bean;
  }

}


@Slf4j
class DynamicConfigService {

  private static final String CGLIB_CLASS_SEPARATOR = "$$";
  private static final String CGLIB_RENAMED_FIELD_PREFIX = "CGLIB$";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private ConfigurableEnvironment configurableEnvironment;

  /*static {
    //Registering custom type to convert from String to target type
    PropertyEditorManager.registerEditor(BigDecimal.class, BigDecimalEditor.class);
  }*/


  public Map<String, Object> getConfig(String clientKey, boolean includeYml) {
    Map<String, Object> configs = new TreeMap<>();
    getConfigMap(clientKey).forEach((key, field) -> {
      try {
        Object ultimateTarget = getUltimateTargetBeanOfField(field);
        boolean accessibility = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(ultimateTarget);
        field.setAccessible(accessibility);
        configs.put(key, value);
      } catch (Exception ex) {
        log.warn("op=getConfig, status=KO, desc=Failed for configs field={}, error={}", field.getName(), ex);
      }
    });
    if (includeYml) {
      configs.put("ZZZ - APPLICATION YML PROPERTIES - ZZZ" ,getYmlConfig());
    }
    return configs;
  }

  public Map<String, Object> getConfigPropKey(String propKey, String id) {
    Map<String, Field> configMap = getConfigMap("dcp");
    Map<String, Object> configs = new TreeMap<>();
    if (configMap.containsKey(propKey)) {
      Field field = configMap.get(propKey);
      try {
        Object ultimateTarget = getUltimateTargetBeanOfField(field);
        boolean accessibility = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(ultimateTarget);
        field.setAccessible(accessibility);
        configs.put(id + " : " + InetAddress.getLocalHost().getHostAddress(), value);
      } catch (Exception ex) {
        log.warn("op=getConfig, status=KO, desc=Failed for configs field={}, error={}", field.getName(), ex);
      }
    }
    return configs;
  }

  private Map<String, Map<String, Object>> getYmlConfig() {
    MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
    Map<String, Map<String, Object>> applicationConfig = new HashMap<>();
    propertySources.forEach(propertySource -> {
      if (propertySource.getName().contains("applicationConfig") && propertySource instanceof MapPropertySource) {
        applicationConfig.put(propertySource.getName(), ((MapPropertySource) propertySource).getSource());
      }
    });
    return applicationConfig;
  }

  public boolean updateConfig(String clientKey, String propKey, String propValue) {
    Map<String, Field> configs = getConfigMap(clientKey);
    boolean isUpdated = false;
    if (configs.containsKey(propKey)) {
      try {
        Field field = configs.get(propKey);
        boolean accessibility = field.isAccessible();
        PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
        editor.setAsText(propValue);
        Object ultimateTarget = getUltimateTargetBeanOfField(field);
        field.setAccessible(true);
        field.set(ultimateTarget, editor.getValue());
        field.setAccessible(accessibility);
        isUpdated = true;
      } catch (Exception ex) {
        log.error("op=updateConfig, status=KO, desc=Config update failed.", ex);
        throw new RuntimeException("Oops! Failed to update config."); //NOPMD
      }
    } else {
      for (Map.Entry<String, Map<String, Object>> entry : getYmlConfig().entrySet()) {
        if (entry.getValue().containsKey(propKey)) {
          entry.getValue().put(propKey, propValue);
          MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
          propertySources.replace(entry.getKey(), new MapPropertySource(entry.getKey(), entry.getValue()));
          isUpdated = true;
        }
      }
    }
    if (!isUpdated) {
      throw new RuntimeException("Oops! Please enter correct key."); //NOPMD
    }
    return true;
  }



  private Object getUltimateTargetBeanOfField(Field field) {
    Object bean;
    try {
      bean = applicationContext.getBean(StringUtils.uncapitalize(field.getDeclaringClass().getSimpleName()));
    } catch (Exception ex) {
      bean = applicationContext.getBean(field.getDeclaringClass());
    }
    bean = getUltimateTargetBean(bean);
    return bean;
  }

  private Object getUltimateTargetBean(Object bean) {
    try {
      if (AopUtils.isAopProxy(bean) && (bean instanceof Advised)) {
        return getUltimateTargetBean(((Advised) bean).getTargetSource().getTarget());
      }
    } catch (Exception ex) {
      log.warn("op=getUltimateTargetBean, status=KO, desc=Failed to get target bean, error={}", ex);
    }
    return bean;
  }

  private Map<String, Field> getUltimateTargetField(List<Field> fields, boolean isDcpClient) {
    return Optional.ofNullable(fields)
            .orElseGet(Collections::emptyList).stream()
            .filter(field -> field.getDeclaringClass().getCanonicalName() != null
                    && field.getDeclaringClass().getCanonicalName().startsWith("com.example")
                    && !field.getName().startsWith(CGLIB_CLASS_SEPARATOR)
                    && !field.getName().startsWith(CGLIB_RENAMED_FIELD_PREFIX)
                    && !Modifier.isFinal(field.getModifiers())
                    && isValidField(field, isDcpClient))
            .collect(Collectors.toMap(field -> field.getDeclaringClass().getSimpleName() + "." + field.getName(),
                    field -> field, (oldValue, newValue) -> oldValue));
  }

  private boolean isValidField(Field field, boolean isDcpClient) {
    //Filtering only Boolean or boolean fields for ops client
    return isDcpClient ? BeanUtils.isSimpleValueType(field.getType())
            : ClassUtils.isAssignable(Boolean.class, field.getType());
  }

  private Map<String, Field> getConfigMap(String clientKey) {
    boolean isDcpClient = !StringUtils.isEmpty(clientKey) && "dcp".equals(clientKey);
    Map<String, Field> configs = new HashMap<>();
    for (String beanName : applicationContext.getBeanDefinitionNames()) {
      Object bean = applicationContext.getBean(beanName);
      if (bean != null) {
        configs.putAll(getUltimateTargetField(
                FieldUtils.getFieldsListWithAnnotation(bean.getClass(), Value.class), isDcpClient));
      }
    }
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Configuration.class);
    beansWithAnnotation.forEach((key, value) ->
            configs.putAll(getUltimateTargetField(FieldUtils.getAllFieldsList(value.getClass()), isDcpClient)));
    return configs;
  }


}
