package com.vanessagl2.dojo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
  private VendingMachine vendingMachine;

  @BeforeEach
  void setUp() {
    doCallRealMethod().when(vendingMachine).setInitialState();
    vendingMachine.setInitialState();
  }

  @Test
  void shouldVendingMachineStateBeSetupToInitialSetupStateOnceMVendingMachineStartsRunning() {
    when(vendingMachine.getVendingMachineState()).thenCallRealMethod();

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

    doCallRealMethod().when(vendingMachine).insertCoins(coins, true);
    doCallRealMethod().when(vendingMachine).getAvailableCoins();

    ArrayList<Coin> expectedAvailableCoins = new ArrayList<>();
    expectedAvailableCoins.add(Coin.D);
    expectedAvailableCoins.add(Coin.Q);
    expectedAvailableCoins.add(Coin.N);

    vendingMachine.insertCoins(coins, true);

    List<Coin> actualAvailableCoins = vendingMachine.getAvailableCoins();

    assertEquals(expectedAvailableCoins, actualAvailableCoins);
  }

  @Test
  void shouldAddInvalidCoinsToInvalidCoinsArray() {
    ArrayList<String> coins = new ArrayList<>();
    coins.add("A");
    coins.add("B");
    coins.add("C");

    doCallRealMethod().when(vendingMachine).insertCoins(coins, true);
    doCallRealMethod().when(vendingMachine).getInvalidCoins();

    ArrayList<String> expectedInvalidCoins = new ArrayList<>();
    expectedInvalidCoins.add("A");
    expectedInvalidCoins.add("B");
    expectedInvalidCoins.add("C");

    vendingMachine.insertCoins(coins, true);

    List<String> actualInvalidCoins = vendingMachine.getInvalidCoins();

    assertEquals(expectedInvalidCoins, actualInvalidCoins);
  }

  @Test
  void shouldSumTheCorrectAmountOfMoneyGivenValidCoinsAreAdded() {
    ArrayList<String> coins = new ArrayList<>();
    coins.add("D");
    coins.add("Q");
    coins.add("N");

    doCallRealMethod().when(vendingMachine).insertCoins(coins, true);
    doCallRealMethod().when(vendingMachine).getAvailableAmount();
    doCallRealMethod().when(vendingMachine).updateAmount(any(), any(boolean.class));

    vendingMachine.insertCoins(coins, true);

    BigDecimal expectedAmount = new BigDecimal("0.40");
    BigDecimal actualAmount = vendingMachine.getAvailableAmount();

    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  void shouldInsertValidProductsDuringSetup() {
    ArrayList<String> products = new ArrayList<>();
    products.add("COLA");
    products.add("CHIPS");
    products.add("CANDY");

    doCallRealMethod().when(vendingMachine).insertProducts(products);
    doCallRealMethod().when(vendingMachine).getAvailableProducts();

    vendingMachine.insertProducts(products);

    ArrayList<Product> expectedAvailableProducts = new ArrayList<>();
    expectedAvailableProducts.add(Product.COLA);
    expectedAvailableProducts.add(Product.CHIPS);
    expectedAvailableProducts.add(Product.CANDY);

    List<Product> actualAvailableProducts = vendingMachine.getAvailableProducts();

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
  void shouldDisplayMessageWithProductPriceAndCurrentBalanceGivenThereIsNotEnoughMoneyToDispenseProduct(String productName, Product product) {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("0"));
    when(vendingMachine.retrieveProduct(productName)).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).updateDisplayMessageWithProduct(false, product);
    when(vendingMachine.getDisplayMessage()).thenCallRealMethod();

    vendingMachine.retrieveProduct(productName);

    String expectedMessage = "PRICE: " + product.getPrice() + ", BALANCE: 0" ;
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
    when(vendingMachine.updateAmount(Coin.Q, false)).thenCallRealMethod();
    doCallRealMethod().when(vendingMachine).subtractProductPriceFromCurrentAmount(Product.COLA);
    when(vendingMachine.getCurrentAmount()).thenCallRealMethod();

    for(int i = 0; i < 6; i++) {
      vendingMachine.updateAmount(Coin.Q, false);
    }

    vendingMachine.subtractProductPriceFromCurrentAmount(Product.COLA);

    BigDecimal expectedAmount = new BigDecimal("0.50");
    BigDecimal actualAmount = vendingMachine.getCurrentAmount();

    assertEquals(expectedAmount, actualAmount);
  }

  @Test
  void shouldInformCurrentBalanceMessageGivenMoneySetupIsCompleted() {
    List<Coin> coins = new ArrayList<>();
    coins.add(Coin.D);
    coins.add(Coin.Q);
    coins.add(Coin.N);
    coins.add(Coin.D);
    coins.add(Coin.Q);
    coins.add(Coin.N);

    List<String> coinsString = new ArrayList<>();
    coinsString.add("D");
    coinsString.add("Q");
    coinsString.add("N");
    coinsString.add("D");
    coinsString.add("Q");
    coinsString.add("N");

    doCallRealMethod().when(vendingMachine).setupCurrentMoneyAmount(any());
    when(vendingMachine.getAvailableAmount()).thenReturn(new BigDecimal("0.80"));
    when(vendingMachine.getAvailableCoins()).thenReturn(coins);
    doCallRealMethod().when(vendingMachine).updateDisplayMessage(any());
    doCallRealMethod().when(vendingMachine).getDisplayMessage();

    vendingMachine.setupCurrentMoneyAmount(coinsString);

    String expectedMessage = "Current balance is $0.80: D, Q, N, D, Q, N\n";

    String actualMessage = vendingMachine.getDisplayMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldInformCurrentProductsMessageGivenProductSetupIsCompleted() {
    List<Product> products = new ArrayList<>();
    products.add(Product.CANDY);
    products.add(Product.CANDY);
    products.add(Product.CHIPS);
    products.add(Product.CANDY);
    products.add(Product.CANDY);
    products.add(Product.CHIPS);
    products.add(Product.CHIPS);
    products.add(Product.COLA);
    products.add(Product.COLA);

    List<String> productsString = new ArrayList<>();
    productsString.add("CANDY");
    productsString.add("CANDY");
    productsString.add("CHIPS");
    productsString.add("CANDY");
    productsString.add("CANDY");
    productsString.add("CHIPS");
    productsString.add("COLA");
    productsString.add("COLA");

    doCallRealMethod().when(vendingMachine).setupCurrentProductAmount(any());
    when(vendingMachine.getAvailableProducts()).thenReturn(products);
    doCallRealMethod().when(vendingMachine).updateDisplayMessage(any());
    doCallRealMethod().when(vendingMachine).getDisplayMessage();

    vendingMachine.setupCurrentProductAmount(productsString);

    String expectedMessage = "Current products are: 4 CANDY, 3 CHIPS, 2 COLA\n";

    String actualMessage = vendingMachine.getDisplayMessage();

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shouldSetInsertMoneyAndDispenseProductStateOnceProductSetupIsCompleted() {
    List<String> productsString = new ArrayList<>();
    productsString.add("CANDY");
    productsString.add("CANDY");
    productsString.add("CHIPS");
    productsString.add("CANDY");
    productsString.add("CANDY");
    productsString.add("CHIPS");
    productsString.add("COLA");
    productsString.add("COLA");

    doCallRealMethod().when(vendingMachine).setupCurrentProductAmount(any());
    doCallRealMethod().when(vendingMachine).setVendingMachineState(any());
    doCallRealMethod().when(vendingMachine).getVendingMachineState();

    vendingMachine.setupCurrentProductAmount(productsString);

    VendingMachineState expectedState = new InsertMoneyAndDispenseProductState(new VendingMachine());
    VendingMachineState actualState = vendingMachine.getVendingMachineState();

    assertEquals(expectedState.getClass(), actualState.getClass());
  }

  @Test
  void shouldReturnChangeGivenMoneyAddedWasMoreThanNecessary() {
    List<Coin> availableCoins = new ArrayList<>();
    availableCoins.add(Coin.Q);
    availableCoins.add(Coin.Q);
    availableCoins.add(Coin.Q);
    availableCoins.add(Coin.Q);
    availableCoins.add(Coin.D);
    availableCoins.add(Coin.N);
    availableCoins.add(Coin.N);
    availableCoins.add(Coin.N);
    availableCoins.add(Coin.N);

    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("0.70")).thenCallRealMethod().thenCallRealMethod();
    when(vendingMachine.getAvailableCoins()).thenReturn(availableCoins);
    doCallRealMethod().when(vendingMachine).getCoinChange(any(), any());
    doCallRealMethod().when(vendingMachine).updateAmount(any());
    when(vendingMachine.getChange()).thenCallRealMethod();

    String expectedChange = "Q, Q, D, N, N";
    String actualChange = vendingMachine.getChange();

    assertEquals(expectedChange, actualChange);
  }

  @Test
  void shouldCallGetChangeGivenThereIsChangeAfterPurchase() {
    when(vendingMachine.getCurrentAmount()).thenReturn(new BigDecimal("10"));
    when(vendingMachine.retrieveProduct("COLA")).thenCallRealMethod();

    vendingMachine.retrieveProduct("COLA");

    verify(vendingMachine).getChange();
  }
}