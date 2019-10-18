package com.vanessagl2.dojo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public enum Coin {

  QUARTER("Q", new BigDecimal("0.25")),
  DIME("D", new BigDecimal("0.1")),
  NICKEL("N", new BigDecimal("0.05"));

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
