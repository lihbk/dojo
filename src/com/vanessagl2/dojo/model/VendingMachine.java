package com.vanessagl2.dojo.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.Objects.isNull;

public class VendingMachine implements VendingMachineState {

  private BigDecimal currentAmount;
  private String displayMessage;
  private ArrayList<Coin> availableCoins;
  private ArrayList<String> invalidCoins;
  private ArrayList<Product> availableProducts;

  private VendingMachineState vendingMachineState;

  public VendingMachine() {
    setInitialState();
  }

  public void setInitialState() {
    vendingMachineState = new InitialSetupState(this);
    availableCoins = new ArrayList<>();
    invalidCoins = new ArrayList<>();
    currentAmount = new BigDecimal("0");
    availableProducts = new ArrayList<>();
  }

  public VendingMachineState getVendingMachineState() {
    return vendingMachineState;
  }

  public void setVendingMachineState(VendingMachineState vendingMachineState) {
    this.vendingMachineState = vendingMachineState;
  }

  @Override
  public void setupCurrentMoneyAndProductAmount(ArrayList<String> coins, ArrayList<String> products) {
    vendingMachineState.setupCurrentMoneyAndProductAmount(coins, products);
    VendingMachineState insertMoneyAndDispenseProductState = new InsertMoneyAndDispenseProductState(this);

    setVendingMachineState(insertMoneyAndDispenseProductState);
  }

  @Override
  public void insertMoneyAndSelectProduct(ArrayList<Coin> coins, Product product) {
    vendingMachineState.insertMoneyAndSelectProduct(coins, product);
  }

  @Override
  public void dispenseProduct() {

  }

  public void insertCoins(ArrayList<String> coins) {
    for (String coinString:coins) {
      Coin coin = Coin.getValueOf(coinString);
      if (!isNull(coin)) {
        availableCoins.add(coin);
        updateAmount(coin);
      } else {
        invalidCoins.add(coinString);
      }
    }
  }

  public BigDecimal updateAmount(Coin coin) {
    currentAmount = currentAmount.add(coin.getAmount());
    return currentAmount;
  }

  public ArrayList<Coin> getAvailableCoins() {
    return availableCoins;
  }

  public ArrayList<String> getInvalidCoins() {
    return invalidCoins;
  }

  public BigDecimal getCurrentAmount() {
    return currentAmount;
  }

  public void insertProducts(ArrayList<String> products) {
    for (String productString:products) {
      Product product = Product.getValueOf(productString);
      if (!isNull(product)) {
        availableProducts.add(product);
      }
    }
  }

  public ArrayList<Product> getAvailableProducts() {
    return availableProducts;
  }

  public Product retrieveProduct(String productName) {
    Product requestedProduct;
    try {
      requestedProduct = Product.getValueOf(productName);
      if(getCurrentAmount().compareTo(requestedProduct.getPrice()) >= 0) {
        updateDisplayMessageWithProduct(true, requestedProduct);
        subtractProductPriceFromCurrentAmount(requestedProduct);
        return requestedProduct;
      }
      updateDisplayMessageWithProduct(false, requestedProduct);
    } catch(Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }
    return null;
  }

  public void subtractProductPriceFromCurrentAmount(Product product) {
    currentAmount = currentAmount.subtract(product.getPrice());
  }

  public String getDisplayMessage() {
    return displayMessage;
  }
//
//  public String updateDisplayMessage() {
//    double amount = this.getCurrentAmount();
//    if(amount == 0.0) {
//      displayMessage = "INSERT COIN";
//    } else {
//      displayMessage = "CURRENT AMOUNT: " + amount;
//    }
//    return displayMessage;
//  }

  public void updateDisplayMessageWithProduct(Boolean isEnoughMoney, Product product) {
    if(isEnoughMoney) {
      displayMessage = product.getName();
    } else {
      displayMessage = "PRICE: " + product.getPrice();
    }
  }

  public void updateDisplayMessage(String displayMessage) {
    this.displayMessage = displayMessage;
  }

}
