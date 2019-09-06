package com.vanessagl2.dojo.model;

import java.util.ArrayList;

public class InsertMoneyAndDispenseProductState implements VendingMachineState  {

  public VendingMachine vendingMachine;

  public InsertMoneyAndDispenseProductState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  @Override
  public void setupCurrentMoneyAndProductAmount(ArrayList<String> coins, ArrayList<String> products) {

  }

  @Override
  public void insertMoneyAndSelectProduct(ArrayList<Coin> coins, Product product) {

  }

  @Override
  public void dispenseProduct() {

  }
}
