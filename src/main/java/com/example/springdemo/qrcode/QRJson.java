/*
 * Copyright (c) 2018 JCPenney Co. All rights reserved.
 *
 */

package com.example.springdemo.qrcode;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author sagar
 */
@Data
public class QRJson {
  Map<String, String> rewards;
  String accountID;
  Date date;
  String metaHash;
}
