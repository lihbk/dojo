package com.vanessagl2.dojo;

import com.vanessagl2.dojo.model.VendingMachine;

import java.util.ArrayList;
import java.util.Arrays;
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
                System.out.println("$ CURRENT BALANCE IS 0. SETUP IS REQUIRED");
                input = in.nextLine();
                String matchedInputCoin = matchInputToSetupPattern(input);
                System.out.println(matchedInputCoin);
                ArrayList coinList = new ArrayList(Arrays.asList(matchedInputCoin));

                System.out.println("$ CURRENT AMOUNT OF PRODUCTS ARE []. SETUP IS REQUIRED");
                input = in.nextLine();
                String matchedInputProduct = matchInputToSetupPattern(input);
                System.out.println(matchedInputProduct);
                ArrayList productList = new ArrayList(Arrays.asList(matchedInputProduct));

                vendingMachine.setupCurrentMoneyAndProductAmount(coinList, productList);

                isSetUp = true;

            } else {
                System.out.println("$ INSERT COIN AND PRODUCT");
                input = in.nextLine();
                ArrayList<String> matchedInput = matchInputToPattern(input);
                String[] coins = matchedInput.get(0).split(",");
                ArrayList coinList = new ArrayList(Arrays.asList(coins));

                System.out.println(matchedInput.get(0));
                System.out.println(matchedInput.get(1));
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
