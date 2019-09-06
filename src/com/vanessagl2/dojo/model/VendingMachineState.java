package com.vanessagl2.dojo.model;

import java.util.ArrayList;

interface VendingMachineState {

  void setupCurrentMoneyAndProductAmount(ArrayList<String> coins, ArrayList<String> products);

  void insertMoneyAndSelectProduct(ArrayList<Coin> coins, Product product);

  void dispenseProduct();
}
