/*
 * Copyright (c) 2018 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.rx;


import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sagar
 */
public class RxSample {

  public static void main(String[] args) {
    System.out.println("Starting Rx 2");
    observableJust();
    observableCreate();
  }

  public static void observableJust() {
    System.out.println("Observable Just");
    Observable<String> hello = Observable.just("Hello");
    hello.subscribe(System.out::println);
  }

  public static void observableCreate() {
    System.out.println("Observable Create");
    Observable<String> stringObservable = Observable.create(
            emitter -> {
              for (String s : getStringArray()) {
                emitter.onNext(s);
              }
              emitter.onComplete();
            }
    );
    stringObservable.subscribe(System.out::println);
  }


  public static List<String> getStringArray() {
    List<String> strings = new ArrayList<>();
    strings.add("A");
    strings.add("B");
    strings.add("C");
    strings.add("D");
    return strings;
  }

}
