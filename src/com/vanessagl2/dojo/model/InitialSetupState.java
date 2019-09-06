package com.vanessagl2.dojo.model;

import java.util.ArrayList;

public class NoMoneyAndProductState implements VendingMachineState {

  public VendingMachine vendingMachine;

  public NoMoneyAndProductState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  @Override
  public void setupCurrentMoneyAndProductAmount(ArrayList<Coin> coins, ArrayList<Product> products) {
    vendingMachine.insertCoins(coins);
    vendingMachine.insertProducts(products);
  }

  @Override
  public void insertMoneyAndSelectProduct(ArrayList<Coin> coins, Product product) {

  }

  @Override
  public void dispenseProduct() {

  }

}
