package com.vanessagl2.dojo.model;

import java.util.List;

import static java.util.Objects.isNull;

public class InsertMoneyAndDispenseProductState implements VendingMachineState  {

  public VendingMachine vendingMachine;

  public InsertMoneyAndDispenseProductState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
  }

  @Override
  public void setupCurrentMoneyAmount(List<String> coins) {

  }

  @Override
  public void setupCurrentProductAmount(List<String> products) {

  }

  @Override
  public void insertMoneyAndSelectProduct(List<String> coins, String productString) {
    vendingMachine.insertCoins(coins, false);
    Product product = vendingMachine.retrieveProduct(productString);

    vendingMachine.printDisplayMessage();

    if(isNull(product)) {
      vendingMachine.updateDisplayMessage("$ INSERT COIN OR HIT COIN-RETURN");
      vendingMachine.printDisplayMessage();
    }

    vendingMachine.updateDisplayMessage(vendingMachine.getChange());
    vendingMachine.printDisplayMessage();
  }

  @Override
  public void dispenseProduct() {

  }
}
