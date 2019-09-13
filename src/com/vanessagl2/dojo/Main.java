package com.vanessagl2.dojo;

import com.vanessagl2.dojo.model.VendingMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class Main {

    static final String INSERT_COIN_AND_PRODUCT_REGEX = "(?<coin>([A-Z]){1}(, [A-Z])*) (?<action>((GET-(COLA|CANDY|CHIPS))|COIN-RETURN)?)";
    static final String SETUP_MONEY_AND_PRODUCT_REGEX = "(?<setupMoney>SETUP-MONEY: ([A-Z]){1}(, [A-Z])*)|(?<setupProduct>SETUP-PRODUCT: (COLA|CANDY|CHIPS){1}(, (COLA|CANDY|CHIPS))*)";

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String input;

        boolean isSetUp = false;

        VendingMachine vendingMachine = new VendingMachine();

        while (true) {
            if (!isSetUp) {
                input = in.nextLine();
                String matchedInputCoin = matchInputToSetupPattern(input);
                String[] coins = matchedInputCoin.split(",");
                List coinList = new ArrayList(Arrays.asList(coins));
                vendingMachine.setupCurrentMoneyAmount(coinList);

                input = in.nextLine();
                String matchedInputProduct = matchInputToSetupPattern(input);
                String[] products = matchedInputProduct.split(",");
                ArrayList productList = new ArrayList(Arrays.asList(products));
                vendingMachine.setupCurrentProductAmount(productList);

                isSetUp = true;

            } else {
                input = in.nextLine();
                ArrayList<String> matchedInput = matchInputToPattern(input);
                String[] coins = matchedInput.get(0).split(",");
                ArrayList coinList = new ArrayList(Arrays.asList(coins));

                vendingMachine.insertMoneyAndSelectProduct(coinList, matchedInput.get(1));
            }
        }
    }

    public static ArrayList<String> matchInputToPattern(String input) {
        ArrayList<String> result = new ArrayList<>();
        Pattern compile = Pattern.compile(INSERT_COIN_AND_PRODUCT_REGEX);
        Matcher matcher = compile.matcher(input);
        matcher.find();
        String coinString = matcher.group("coin");
        String actionString = matcher.group("action");

        coinString = coinString.replaceAll(" ", "");
        actionString = actionString.trim();

        if(actionString.contains("GET-")) {
            actionString = actionString.replace("GET-", "");
        }

        result.add(coinString);
        result.add(actionString);

        return result;
    }

    public static String matchInputToSetupPattern(String input) {
        Pattern compile = Pattern.compile(SETUP_MONEY_AND_PRODUCT_REGEX);
        Matcher matcher = compile.matcher(input);
        matcher.find();
        String coinSetupString = matcher.group("setupMoney");
        String productSetupString = matcher.group("setupProduct");

        if (!isNull(coinSetupString)) {
            coinSetupString = coinSetupString.replaceAll(" ", "");
            coinSetupString = coinSetupString.replace("SETUP-MONEY:", "");
            return coinSetupString;
        }

        productSetupString = productSetupString.replaceAll(" ", "");
        productSetupString = productSetupString.replace("SETUP-PRODUCT:", "");

        return productSetupString;
    }

}
