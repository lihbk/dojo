package com.vanessagl2.dojo.model;

import java.math.BigDecimal;

public enum Coin {

  Q("Q", new BigDecimal("0.25")),
  D("D", new BigDecimal("0.1")),
  N("N", new BigDecimal("0.05"));

  private String name;

  private BigDecimal amount;

  Coin(String name, BigDecimal amount) {
    this.name = name;
    this.amount = amount;
  }

  public BigDecimal getAmount() { return amount; }

  public String getName() { return name; }

  public static Coin getValueOf(String coinName) {
    for (Coin coin : Coin.values()) {
      if (coin.name.equals(coinName)) {
        return coin;
      }
    }
    return null;
  }

}
