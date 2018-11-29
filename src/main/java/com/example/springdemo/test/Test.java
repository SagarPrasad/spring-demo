/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.test;

/**
 * @author sagar
 */
public class Test {
  public static void main(String[] args) {
    int i = 2147483647;

    for(int j = 0;j < 1000;j++) {
      System.out.println(i++);
    }
  }
}
