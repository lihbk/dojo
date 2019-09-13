package com.vanessagl2.dojo.model;

import java.math.BigDecimal;
import java.util.List;

public class InitialSetupState implements VendingMachineState {

  public VendingMachine vendingMachine;

  public InitialSetupState(VendingMachine vendingMachine) {
    this.vendingMachine = vendingMachine;
    vendingMachine.updateDisplayMessage("$ CURRENT BALANCE IS 0. SETUP IS REQUIRED");
    vendingMachine.printDisplayMessage();
  }

  @Override
  public void setupCurrentMoneyAmount(List<String> coins) {
    vendingMachine.insertCoins(coins, true);

    BigDecimal availableAmount = vendingMachine.getAvailableAmount();

    List<Coin> availableCoins = vendingMachine.getAvailableCoins();

    String displayMessage = "Current balance is $" + availableAmount.toString() + ": ";

    for(Coin coin : availableCoins) {
      displayMessage = displayMessage.concat(coin.name() + ", ");
    }

    displayMessage = displayMessage.substring(0, displayMessage.length() - 2) + "\n";

    vendingMachine.updateDisplayMessage(displayMessage);
    vendingMachine.printDisplayMessage();

    vendingMachine.updateDisplayMessage("$ CURRENT AMOUNT OF PRODUCTS ARE []. SETUP IS REQUIRED");
    vendingMachine.printDisplayMessage();

  }

  @Override
  public void setupCurrentProductAmount(List<String> products) {
    vendingMachine.insertProducts(products);

    List<Product> availableProducts = vendingMachine.getAvailableProducts();

    long candyAmount = availableProducts.stream().filter( c -> c.getName() == "CANDY" ).count();
    long chipsAmount = availableProducts.stream().filter( c -> c.getName() == "CHIPS" ).count();
    long colaAmount = availableProducts.stream().filter( c -> c.getName() == "COLA" ).count();

    String displayMessage = "Current products are: " + candyAmount + " CANDY, " + chipsAmount + " CHIPS, " + colaAmount + " COLA\n";

    vendingMachine.updateDisplayMessage(displayMessage);
    vendingMachine.printDisplayMessage();

    vendingMachine.updateDisplayMessage("$ INSERT COIN AND PRODUCT");
    vendingMachine.printDisplayMessage();
  }

  @Override
  public void insertMoneyAndSelectProduct(List<String> coins, String product) {

  }

  @Override
  public void dispenseProduct() {

  }

}
