/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sagar
 */
public class Test {
  public static void main(String[] args) {
   /* Max Int Value Test *

    int i = 2147483647;

    for(int j = 0;j < 1000;j++) {
      System.out.println(i++);
    }*/
   InnerClass a = new InnerClass();
   a.setSomeval("A");
   InnerClass b = new InnerClass();
   b.setSomeval("B");
   InnerClass c = new InnerClass();
   c.setSomeval("C");
   InnerClass d = new InnerClass();
   d.setSomeval("D");

   List<InnerClass> list = new ArrayList<>();
   list.add(a);
   list.add(b);
   list.add(c);
   list.add(d);


    list.parallelStream().forEach(innerClass -> {
      System.out.println(innerClass.getSomeval());
      innerClass.setSomeval(innerClass.getSomeval() + innerClass.getSomeval());
    });

    System.out.println("999999999999999");

    for(InnerClass cl : list) {
      System.out.println(cl.getSomeval());
    }

  }


  @Data
  static class InnerClass {
    private String someval;
  }
}
