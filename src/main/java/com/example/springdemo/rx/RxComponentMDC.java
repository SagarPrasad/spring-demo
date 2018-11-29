/*
 * Copyright (c) 2016 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.rx;

import io.reactivex.Flowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * @author sagar
 */
@Component
@Slf4j
public class RxComponentMDC {


  @Autowired
  LocalBean localBean;

  @Autowired
  @Qualifier("ReqBean")
  LocalBean requestBean;

  public static void main(String[] args) throws InterruptedException {

    // Write Code to
    // Confirm all schedulars have spring context awareness
    // request scope awareness

    RxComponentMDC rxComponentMDC = new RxComponentMDC();
    Map<String , String> testMap = new HashMap<>();
    testMap.put("KEY", "Parent-MDC");

    MDC.setContextMap(testMap);
    RxJavaPlugins.setScheduleHandler(new ThreadLocalPropogator());
    System.out.println(MDC.get("KEY"));
    rxComponentMDC.backCallable();


    testMap.put("KEY", "Flowable-MDC");
    MDC.setContextMap(testMap);
    rxComponentMDC.printbreak();
    rxComponentMDC.flowableJust();

    testMap.put("KEY", "Concurrency-MDC");
    MDC.setContextMap(testMap);
    rxComponentMDC.printbreak();
    rxComponentMDC.concurrency();

    testMap.put("KEY", "Parallel-MDC");
    MDC.setContextMap(testMap);
    rxComponentMDC.printbreak();
    rxComponentMDC.parallel();

    testMap.put("KEY", "IO-MDC");
    MDC.setContextMap(testMap);
    rxComponentMDC.printbreak();
    rxComponentMDC.io();

    Thread.sleep(2000);
  }

  public String defaultM() {
    System.out.println(requestBean.getLocalString());
    return requestBean.getLocalString();
  }

  public void backCallable() {
    localBean.setLocalString("Local -" + MDC.get("KEY"));
    Flowable.fromCallable(() -> {
      Thread.sleep(1000); //  imitate expensive computation
      return Thread.currentThread().getId() + "--" + MDC.get("KEY") + "--" + "Callable -- Done" + " --" + localBean
              .getLocalString();
    })
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.single())
    .subscribe(System.out::println, Throwable::printStackTrace);
}


  public void parallel() {
    localBean.setLocalString("Local -" + MDC.get("KEY"));
    Flowable.range(1, 10)
            .parallel()
            .runOn(Schedulers.computation())
            .map(v -> Thread.currentThread().getId() + "-P-" + "--"  + MDC.get("KEY") + "--"+ v * v + " --" +
                    localBean.getLocalString()  )
            .sequential()
            .blockingSubscribe(System.out::println);
  }

  public void concurrency() {
   // localBean.setLocalString("Local -" + MDC.get("KEY"));
    Flowable.range(1, 10)
            .observeOn(Schedulers.computation())
            .map(v -> Thread.currentThread().getId() + "-C-"+ "--"  + MDC.get("KEY") + "--" + v * v + " --" +
                    localBean.getLocalString())
            .blockingSubscribe(System.out::println);
  }

  public void io() {
    localBean.setLocalString("Local -" + MDC.get("KEY"));
    Flowable.range(1, 10)
            .observeOn(Schedulers.io())
            .map(v -> Thread.currentThread().getId() + "-IO-"+ "--"  + MDC.get("KEY") + "--" + v * v + " --" +
                    localBean.getLocalString())
            .blockingSubscribe(System.out::println);
  }

  public void printbreak() {
    Flowable.range(1, 50).doOnComplete(() -> {
      System.out.println("\n" + MDC.get("KEY") + "--\n" );
    }).subscribe(System.out::print);
  }


  public void flowableJust() {

    localBean.setLocalString("Local -" + MDC.get("KEY"));

    Flowable.just(Thread.currentThread().getId() + "--" + "Flowable -> Java 8" + " --" +
            localBean.getLocalString()).subscribe(System.out::println);

    Flowable.just(Thread.currentThread().getId() + "--" + "Flowable" + " --" +
            localBean.getLocalString()).subscribe(s -> System.out.println(Thread
            .currentThread().getId() + "Consumer -> " + s));

  }

  @PostConstruct
  void init() {
    System.out.println("Initializing  Schedular Handler");
    RxJavaPlugins.setScheduleHandler(new ThreadLocalPropogator());
  }

}
