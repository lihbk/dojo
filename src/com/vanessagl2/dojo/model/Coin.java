package com.vanessagl2.dojo.model;

import java.math.BigDecimal;

public enum Coin {

  NICKEL("N", new BigDecimal("0.05")),
  DIME("D", new BigDecimal("0.1")),
  QUARTER("Q", new BigDecimal("0.25"));

  private String name;

  private BigDecimal amount;

  Coin(String name, BigDecimal amount) {
    this.name = name;
    this.amount = amount;
  }

  public BigDecimal getAmount() { return amount; }

  public static Coin getValueOf(String coinName) {
    for (Coin coin : Coin.values()) {
      if (coin.name.equals(coinName)) {
        return coin;
      }
    }
    return null;
  }

}
