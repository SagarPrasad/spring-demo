/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sagar
 */
@Slf4j
@Aspect
@Component
public class DynamicConfigInterceptor {

  private static final String REGEX = "get|is";

  boolean excludeFromRequestScope = true;

  @Around("@within(EnableDynamicConfig) && (execution(* get*(..)) || execution(* is*(..)))")
  public Object dynamicConfigChange(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      if (null != RequestContextHolder.getRequestAttributes()) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String debug = request.getHeader("x-debug");
        String prop = joinPoint.getSignature().getName().replaceFirst(REGEX, "").toLowerCase();
        String propValue = request.getHeader(prop);
        if (Boolean.valueOf(debug) && excludeFromRequestScope && null != propValue) {
          Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
          PropertyEditor editor = PropertyEditorManager.findEditor(method.getReturnType());
          editor.setAsText(propValue);
          log.info("op=dynamicConfigChange, status=OK, desc=updating config for: {} to: {}", prop, propValue);
          return editor.getValue();
        }
      }
    } catch (Throwable ex) {
      log.warn("op=dynamicConfigChange, status=KO, desc=config update failed, error={}", ExceptionUtils.getMessage(ex));
    }
    return joinPoint.proceed();
  }

  @Around("@within(EnableDynamicConfig) && (@annotation(EnableDynamicMockValue))")
  public Object dynamicConfigChangeWithAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      if (null != RequestContextHolder.getRequestAttributes()) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String debug = request.getHeader("x-debug");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String prop = method.getAnnotation(EnableDynamicMockValue.class).value();
        String propValue = request.getHeader(prop);
        if (Boolean.valueOf(debug) && excludeFromRequestScope && null != propValue) {
          PropertyEditor editor = PropertyEditorManager.findEditor(method.getReturnType());
          // refactor - properly
          if(null == editor) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(propValue, method.getReturnType());
          }
          editor.setAsText(propValue);
          log.info("op=dynamicConfigChangeWithAnnotation, status=OK, desc=updating config for: {} to: {}", prop, propValue);
          return editor.getValue();
        }
      }
    } catch (Throwable ex) {
      log.warn("op=dynamicConfigChangeWithAnnotation, status=KO, desc=config update failed, error={}", ExceptionUtils.getMessage(ex));
    }
    return joinPoint.proceed();
  }

  /* -- Crap Idea
  @Before("@within(EnableDynamicConfig) && (@annotation(EnableDynamicMockValue))")
  public void dynamicConfigChangeOnField(JoinPoint joinPoint) throws Throwable {
    try {
      if (null != RequestContextHolder.getRequestAttributes()) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String debug = request.getHeader("x-debug");
        for (Field field : joinPoint.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          if (field.isAnnotationPresent(EnableDynamicMockValue.class)) {
            String name = field.getAnnotation(EnableDynamicMockValue.class).value();
            String propValue = request.getHeader(name);
            if (!StringUtils.isEmpty(propValue)) {
              field.set(field.getType(), propValue);
              log.info("op=dynamicConfigChangeWithAnnotation, status=OK, desc=updating config for: {} to: {}", name, propValue);
            }
          }
        }
        }
    } catch (Throwable ex) {
      log.warn("op=dynamicConfigChangeWithAnnotation, status=KO, desc=config update failed, error={}", ExceptionUtils.getMessage(ex));
    }
  }*/

}
