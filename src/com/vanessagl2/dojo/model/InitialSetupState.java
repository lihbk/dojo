package com.vanessagl2.dojo.model;

import java.util.ArrayList;

public class InitialSetupState implements VendingMachineState {

  public VendingMachine vendingMachine;

  public InitialSetupState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
    vendingMachine.updateDisplayMessage("$ CURRENT BALANCE IS 0. SETUP IS REQUIRED");
  }

  @Override
  public void setupCurrentMoneyAndProductAmount(ArrayList<String> coins, ArrayList<String> products) {
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
