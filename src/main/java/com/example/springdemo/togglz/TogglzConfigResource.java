/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.togglz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sagar
 */
@RestController
public class TogglzConfigResource {


/*
  private FeatureManager manager;

  public TogglzConfigResource(FeatureManager manager) {
    this.manager = manager;
  }
*/

  @GetMapping("/rest-togglz")
  public String config() {


    if (CustomEnumTogglzFeature.FEATURE_ONE.isActive()) {
      return CustomEnumTogglzFeature.FEATURE_ONE.toString();
    }
    if (CustomEnumTogglzFeature.FEATURE_TWO.isActive()) {
      return CustomEnumTogglzFeature.FEATURE_TWO.toString();
    }


    return "default";
  }


  /*@PostConstruct
  void ss() {
    Set<Feature> features = FeatureContext.getFeatureManager().getFeatures();
    for (Feature feature : features) {
      System.out.println(feature.name());
      FeatureState featureState = FeatureContext.getFeatureManager().getFeatureState(feature);
      if ("FEATURE_ONE".equalsIgnoreCase(feature.name())) {
        featureState.setParameter(GradualActivationStrategy.PARAM_PERCENTAGE, "50");
      }
    }
  }*/

}
