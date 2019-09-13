package com.vanessagl2.dojo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

public class VendingMachine implements VendingMachineState {

  private BigDecimal currentAmount;
  private BigDecimal availableAmount;
  private String displayMessage;
  private List<Coin> availableCoins;
  private List<String> invalidCoins;
  private List<Product> availableProducts;

  private VendingMachineState vendingMachineState;

  public VendingMachine() {
    setInitialState();
  }

  public void setInitialState() {
    vendingMachineState = new InitialSetupState(this);
    availableCoins = new ArrayList<>();
    invalidCoins = new ArrayList<>();
    availableAmount = new BigDecimal("0");
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
  public void setupCurrentMoneyAmount(List<String> coins) {
    vendingMachineState.setupCurrentMoneyAmount(coins);
  }

  @Override
  public void setupCurrentProductAmount(List<String> products) {
    vendingMachineState.setupCurrentProductAmount(products);
    VendingMachineState insertMoneyAndDispenseProductState = new InsertMoneyAndDispenseProductState(this);

    setVendingMachineState(insertMoneyAndDispenseProductState);
  }

  @Override
  public void insertMoneyAndSelectProduct(List<String> coins, String product) {
    vendingMachineState.insertMoneyAndSelectProduct(coins, product);
  }

  @Override
  public void dispenseProduct() {

  }

  public void insertCoins(List<String> coins, boolean isSetup) {
    for (String coinString:coins) {
      Coin coin = Coin.getValueOf(coinString);
      if (!isNull(coin)) {
        availableCoins.add(coin);
        updateAmount(coin, isSetup);
      } else {
        invalidCoins.add(coinString);
      }
    }
  }

  public BigDecimal updateAmount(Coin coin, boolean isSetup) {
    if (isSetup) {
      availableAmount = availableAmount.add(coin.getAmount());
      return availableAmount;
    } else {
      currentAmount = currentAmount.add(coin.getAmount());
      return currentAmount;
    }
  }

  public List<Coin> getAvailableCoins() {
    return availableCoins;
  }

  public List<String> getInvalidCoins() {
    return invalidCoins;
  }

  public BigDecimal getCurrentAmount() {
    return currentAmount;
  }

  public BigDecimal getAvailableAmount() {
    return availableAmount;
  }

  public void insertProducts(List<String> products) {
    for (String productString:products) {
      Product product = Product.getValueOf(productString);
      if (!isNull(product)) {
        availableProducts.add(product);
      }
    }
  }

  public List<Product> getAvailableProducts() {
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

  public void updateDisplayMessageWithProduct(Boolean isEnoughMoney, Product product) {
    if(isEnoughMoney) {
      displayMessage = product.getName();
    } else {
      displayMessage = "PRICE: " + product.getPrice() + ", BALANCE: " + getCurrentAmount();
    }
  }

  public void updateDisplayMessage(String displayMessage) {
    this.displayMessage = displayMessage;
  }

  public void printDisplayMessage() {
    System.out.println(getDisplayMessage());
  }

  public void getCoinChange(Coin coin, List<Coin> coinChange) {
    BigDecimal amount = this.getCurrentAmount();
    List<Coin> coins = this.getAvailableCoins();

    long coinAmount = coins.stream().filter( c -> c.getName() == coin.getName() ).count();

    BigDecimal coinChangeAmount = new BigDecimal("0");

    if (coinAmount > 0) {
      coinChangeAmount = amount.divideToIntegralValue(coin.getAmount());
      if(coinChangeAmount.compareTo(new BigDecimal(coinAmount)) == 1) {
        coinChangeAmount = new BigDecimal(coinAmount);
      }
      amount = amount.subtract(coinChangeAmount.multiply(coin.getAmount()));
      this.updateAmount(amount);
    }

    if(coinChangeAmount.intValue() > 0) {
      for (int i=0; i < coinChangeAmount.intValue(); i++) {
        coinChange.add(coin);
      }
    }
  }

  public String getChange() {
    List<Coin> coinChange = new ArrayList<>();
    Arrays.stream(Coin.values()).forEach( c -> this.getCoinChange(c, coinChange));
    final StringBuilder changeStringBuilder = new StringBuilder();

    coinChange.forEach(coin -> changeStringBuilder.append(coin.getName() + ", "));

    String changeString = changeStringBuilder.toString();
    changeString = changeString.substring(0, changeString.length()-2);
    return changeString;
  }

  public void updateAmount(BigDecimal amount) {
    this.currentAmount = amount;
  }
}
