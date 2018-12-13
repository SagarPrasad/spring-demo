/*
 * Copyright (c) 2016 Sagar Co. All rights reserved.
 *
 */

package com.example.springdemo.togglz;

import org.togglz.core.Feature;
import org.togglz.core.activation.GradualActivationStrategy;
import org.togglz.core.annotation.ActivationParameter;
import org.togglz.core.annotation.DefaultActivationStrategy;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

/**
 * @author sagar
 */
public enum CustomEnumTogglzFeature implements Feature {

  /**
   * Feature1
   */
  @Label("First Feature")
  @EnabledByDefault
  FEATURE_ONE,

  /**
   * Feature2
   */
  @Label("Second Feature")
  @DefaultActivationStrategy(id = GradualActivationStrategy.ID,
          parameters = {@ActivationParameter(name = GradualActivationStrategy.PARAM_PERCENTAGE, value = "30")})
  FEATURE_TWO;

  public boolean isActive() {
    return FeatureContext.getFeatureManager().isActive(this);
  }

}
