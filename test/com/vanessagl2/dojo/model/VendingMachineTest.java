package com.vanessagl2.dojo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendingMachineTest {

  @Mock
  private VendingMachineState vendingMachineState;

  @Mock
  private VendingMachine vendingMachine;

  @Test
  void shouldVendingMachineStateBeSetupToInitialSetupStateOnceMVendingMachineStartsRunning() {
    when(vendingMachine.getVendingMachineState()).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).setInitialState();

    vendingMachine.setInitialState();

    VendingMachineState expectedState = new InitialSetupState(new VendingMachine());
    VendingMachineState actualState = vendingMachine.getVendingMachineState();

    assertEquals(expectedState.getClass(), actualState.getClass());
  }

  @Test
  void shouldAcceptValidCoins() {
    ArrayList<String> coins = new ArrayList<>();
    coins.add("D");
    coins.add("Q");
    coins.add("N");

    doCallRealMethod().when(vendingMachine).setInitialState();
    doCallRealMethod().when(vendingMachine).insertCoins(coins);
    doCallRealMethod().when(vendingMachine).getAvailableCoins();

    ArrayList<Coin> expectedAvailableCoins = new ArrayList<>();
    expectedAvailableCoins.add(Coin.DIME);
    expectedAvailableCoins.add(Coin.QUARTER);
    expectedAvailableCoins.add(Coin.NICKEL);

    vendingMachine.setInitialState();
    vendingMachine.insertCoins(coins);

    ArrayList<Coin> actualAvailableCoins = vendingMachine.getAvailableCoins();

    assertEquals(expectedAvailableCoins, actualAvailableCoins);
  }

  @Test
  void shouldAddInvalidCoinsToInvalidCoinsArray() {
    ArrayList<String> coins = new ArrayList<>();
    coins.add("A");
    coins.add("B");
    coins.add("C");

    doCallRealMethod().when(vendingMachine).setInitialState();
    doCallRealMethod().when(vendingMachine).insertCoins(coins);
    doCallRealMethod().when(vendingMachine).getInvalidCoins();

    ArrayList<String> expectedInvalidCoins = new ArrayList<>();
    expectedInvalidCoins.add("A");
    expectedInvalidCoins.add("B");
    expectedInvalidCoins.add("C");

    vendingMachine.setInitialState();
    vendingMachine.insertCoins(coins);

    ArrayList<String> actualInvalidCoins = vendingMachine.getInvalidCoins();

    assertEquals(expectedInvalidCoins, actualInvalidCoins);
  }

  @Test
  void shouldSumTheCorrectAmountOfMoneyGivenValidCoinsAreAdded() {
    ArrayList<String> coins = new ArrayList<>();
    coins.add("D");
    coins.add("Q");
    coins.add("N");

    doCallRealMethod().when(vendingMachine).setInitialState();
    doCallRealMethod().when(vendingMachine).insertCoins(coins);
    doCallRealMethod().when(vendingMachine).getCurrentAmount();
    doCallRealMethod().when(vendingMachine).updateAmount(any());


    vendingMachine.setInitialState();
    vendingMachine.insertCoins(coins);

    BigDecimal expectedAmount = new BigDecimal("0.40");
    BigDecimal actualAmount = vendingMachine.getCurrentAmount();

    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  void shouldInsertValidProductsDuringSetup() {
    ArrayList<String> products = new ArrayList<>();
    products.add("COLA");
    products.add("CHIPS");
    products.add("CANDY");

    doCallRealMethod().when(vendingMachine).setInitialState();
    doCallRealMethod().when(vendingMachine).insertProducts(products);
    doCallRealMethod().when(vendingMachine).getAvailableProducts();

    vendingMachine.setInitialState();
    vendingMachine.insertProducts(products);

    ArrayList<Product> expectedAvailableProducts = new ArrayList<>();
    expectedAvailableProducts.add(Product.COLA);
    expectedAvailableProducts.add(Product.CHIPS);
    expectedAvailableProducts.add(Product.CANDY);

    ArrayList<Product> actualAvailableProducts = vendingMachine.getAvailableProducts();

    assertEquals(expectedAvailableProducts, actualAvailableProducts);
  }

  @ParameterizedTest
  @MethodSource("productParams")
  void shouldDispenseColGivenColaIsSelectedAndThereIsEnoughMoney(String productName, Product product) {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("10"));
    when(vendingMachine.retrieveProduct(productName)).thenCallRealMethod();

    Product expectedProduct = product;
    Product actualProduct = vendingMachine.retrieveProduct(productName);

    assertEquals(expectedProduct, actualProduct);
  }

  @ParameterizedTest
  @MethodSource("productParams")
  void shouldDisplayMessageWithProductNameGivenProductHasBeenDispensed(String productName, Product product) {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("10"));
    when(vendingMachine.retrieveProduct(productName)).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).updateDisplayMessageWithProduct(true, product);
    when(vendingMachine.getDisplayMessage()).thenCallRealMethod();

    vendingMachine.retrieveProduct(productName);

    String expectedMessage = productName;
    String actualMessage = vendingMachine.getDisplayMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @ParameterizedTest
  @MethodSource("productParams")
  void shouldDisplayMessageWithProductPriceGivenThereIsNotEnoughMoneyToDispenseProduct(String productName, Product product) {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("0"));
    when(vendingMachine.retrieveProduct(productName)).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).updateDisplayMessageWithProduct(false, product);
    when(vendingMachine.getDisplayMessage()).thenCallRealMethod();

    vendingMachine.retrieveProduct(productName);

    String expectedMessage = "PRICE: " + product.getPrice();
    String actualMessage = vendingMachine.getDisplayMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  private static Stream<Arguments> productParams() {
    return Stream.of(
        Arguments.of("COLA", Product.COLA),
        Arguments.of("CANDY", Product.CANDY),
        Arguments.of("CHIPS", Product.CHIPS)
    );
  }

  @Test
  void shouldCallUpdateCurrentAmountGivenProductIsSelectedAndThereIsEnoughMoneyToBuyProduct() {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("1"));
    when(vendingMachine.retrieveProduct("COLA")).thenCallRealMethod();

    vendingMachine.retrieveProduct("COLA");

    verify(vendingMachine, times(1)).subtractProductPriceFromCurrentAmount(Product.COLA);
  }

  @Test
  void shouldUpdateCurrentAmountGivenProductIsSelectedAndThereIsEnoughMoneyToBuyProduct() {
    doCallRealMethod().when(vendingMachine).setInitialState();
    when(vendingMachine.updateAmount(Coin.QUARTER)).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).subtractProductPriceFromCurrentAmount(Product.COLA);
    when(vendingMachine.getCurrentAmount()).thenCallRealMethod();

    vendingMachine.setInitialState();

    for(int i = 0; i < 6; i++) {
      vendingMachine.updateAmount(Coin.QUARTER);
    }

    vendingMachine.subtractProductPriceFromCurrentAmount(Product.COLA);

    BigDecimal expectedAmount = new BigDecimal("0.50");
    BigDecimal actualAmount = vendingMachine.getCurrentAmount();

    assertEquals(expectedAmount, actualAmount);
  }

//  @Test
//  void shouldDisplayInsertCoinGivenThereAreNoCoinsInserted() {
//    when(vendingMachine.updateDisplayMessage()).thenCallRealMethod();
//
//    String expectedDisplayMessage = "INSERT COIN";
//    String actualDisplayMessage = vendingMachine.updateDisplayMessage();
//
//    assertEquals(expectedDisplayMessage, actualDisplayMessage);
//  }
//
//  @Test
//  void shouldDisplayUpdatedAmountGivenAmountIsUpdated() {
//    when(vendingMachine.getCurrentAmount()).thenReturn(0.5);
//    when(vendingMachine.updateDisplayMessage()).thenCallRealMethod();
//
//    String expectedDisplayMessage = "CURRENT AMOUNT: " + 0.5;
//    String actualDisplayMessage = vendingMachine.updateDisplayMessage();
//
//    assertEquals(expectedDisplayMessage, actualDisplayMessage);
//  }

}