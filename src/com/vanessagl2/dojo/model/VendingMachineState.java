package com.vanessagl2.dojo.model;

import java.util.List;

interface VendingMachineState {

  void setupCurrentMoneyAmount(List<String> coins);

  void setupCurrentProductAmount(List<String> products);

  void insertMoneyAndSelectProduct(List<String> coins, String product);

  void dispenseProduct();
}
