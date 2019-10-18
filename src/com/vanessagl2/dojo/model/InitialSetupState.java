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

    BigDecimal availableAmount = vendingMachine.getAvailableAmountOfMoneyAfterSetup();

    List<Coin> availableCoins = vendingMachine.getAvailableCoins();

    String displayMessage = "Current balance is $" + availableAmount.toString() + ": ";

    for(Coin coin : availableCoins) {
      displayMessage = displayMessage.concat(coin.getName() + ", ");
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

    long availableAmountOfCandy = availableProducts.stream().filter( c -> c.getName().equals("CANDY")).count();
    long availableAmountOfChips = availableProducts.stream().filter( c -> c.getName().equals("CHIPS")).count();
    long availableAmountOfCola = availableProducts.stream().filter( c -> c.getName().equals("COLA")).count();

    String displayMessage = "Current products are: " + availableAmountOfCandy + " CANDY, "
        + availableAmountOfChips + " CHIPS, "
        + availableAmountOfCola + " COLA\n";

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
