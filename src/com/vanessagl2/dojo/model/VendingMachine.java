package com.vanessagl2.dojo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class VendingMachine implements VendingMachineState {

  private BigDecimal amountOfMoneyInsertedByBuyer;
  private BigDecimal availableAmountOfMoneyAfterSetup;
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
    availableAmountOfMoneyAfterSetup = new BigDecimal("0");
    amountOfMoneyInsertedByBuyer = new BigDecimal("0");
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
      if (nonNull(coin)) {
        availableCoins.add(coin);
        updateAmountOfMoneyInsideMachine(coin, isSetup);
      } else {
        invalidCoins.add(coinString);
      }
    }
  }

//  public List<String> getInvalidCoins(List<String> coins) {
//    List<String> invalidCoins = Coin.getInvalidCoins(coins);
//    return invalidCoins;
//  }
//
//  public ArrayList<Coin> getValidCoins(List<String> coins) {
//    ArrayList<Coin> validCoins = Coin.getValidCoins(coins);
//    return validCoins;
//  }
//
//  public void insertValidCoins(List<Coin> coins) {
//    coins.forEach(coin->availableCoins.add(coin));
//  }
//
//  public void updateAmountOfMoneyInsideMachineDuringSetup(List<Coin> coins) {
//    coins.forEach(coin->availableAmountOfMoneyAfterSetup.add(coin.getAmount()));
//  }
//
//  public void updateAmountOfMoneyInsertedByBuyer(List<Coin> coins) {
//    coins.forEach(coin->amountOfMoneyInsertedByBuyer.add(coin.getAmount()));
//  }

  public BigDecimal updateAmountOfMoneyInsideMachine(Coin coin, boolean isSetup) {
    if (isSetup) {
      availableAmountOfMoneyAfterSetup = availableAmountOfMoneyAfterSetup.add(coin.getAmount());
      return availableAmountOfMoneyAfterSetup;
    } else {
      amountOfMoneyInsertedByBuyer = amountOfMoneyInsertedByBuyer.add(coin.getAmount());
      return amountOfMoneyInsertedByBuyer;
    }
  }

  public void insertProducts(List<String> products) {
    for (String productString:products) {
      Product product = Product.getValueOf(productString);
      if (!isNull(product)) {
        availableProducts.add(product);
      }
    }
  }

  public Product retrieveProduct(String productName) {
    Product requestedProduct;
    try {
      requestedProduct = Product.getValueOf(productName);
      if(getAmountOfMoneyInsertedByBuyer().compareTo(requestedProduct.getPrice()) >= 0) {
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
    amountOfMoneyInsertedByBuyer = amountOfMoneyInsertedByBuyer.subtract(product.getPrice());
  }

  public void updateDisplayMessageWithProduct(Boolean isEnoughMoney, Product product) {
    if(isEnoughMoney) {
      displayMessage = product.getName();
    } else {
      displayMessage = "PRICE: " + product.getPrice() + ", BALANCE: " + getAmountOfMoneyInsertedByBuyer();
    }
  }

  public void getCoinChange(Coin coin, List<Coin> coinChange) {
    BigDecimal amount = this.getAmountOfMoneyInsertedByBuyer();
    List<Coin> coins = this.getAvailableCoins();

    long coinAmount = coins.stream().filter( c -> c.getName() == coin.getName() ).count();

    BigDecimal coinChangeAmount = new BigDecimal("0");

    if (coinAmount > 0) {
      coinChangeAmount = amount.divideToIntegralValue(coin.getAmount());
      if(coinChangeAmount.compareTo(new BigDecimal(coinAmount)) == 1) {
        coinChangeAmount = new BigDecimal(coinAmount);
      }
      amount = amount.subtract(coinChangeAmount.multiply(coin.getAmount()));
      this.updateAmountOfMoneyInsertedByBuyer(amount);
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

  public void updateAmountOfMoneyInsertedByBuyer(BigDecimal amount) {
    this.amountOfMoneyInsertedByBuyer = amount;
  }

  public List<Coin> getAvailableCoins() { return availableCoins; }

  public List<String> getInvalidCoins() {
    return invalidCoins;
  }

  public BigDecimal getAmountOfMoneyInsertedByBuyer() {
    return amountOfMoneyInsertedByBuyer;
  }

  public BigDecimal getAvailableAmountOfMoneyAfterSetup() {
    return availableAmountOfMoneyAfterSetup;
  }

  public void updateDisplayMessage(String displayMessage) {
    this.displayMessage = displayMessage;
  }

  public String getDisplayMessage() {
    return displayMessage;
  }

  public void printDisplayMessage() {
    System.out.println(getDisplayMessage());
  }

  public List<Product> getAvailableProducts() {
    return availableProducts;
  }
}
