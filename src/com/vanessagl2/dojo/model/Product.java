package com.vanessagl2.dojo.model;

import java.math.BigDecimal;

public enum Product {

  COLA("COLA", new BigDecimal("1")),
  CHIPS("CHIPS", new BigDecimal("0.5")),
  CANDY("CANDY", new BigDecimal("0.65"));

  private String name;

  private BigDecimal price;

  Product(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
  }

  public String getName() { return name; }

  public BigDecimal getPrice() { return price; }

  public static Product getValueOf(String productName) {
    for (Product product : Product.values()) {
      if (product.name.equals(productName)) {
        return product;
      }
    }
    return null;
  }
}
