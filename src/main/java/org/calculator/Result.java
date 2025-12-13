package org.calculator;

import javafx.scene.control.TextField;

public class Result {
    static boolean isDouble = false;
    static double dResult;
    static long lResult;

    // Private constructor because this class is not meant to be instantiated, it's just an utility to calculate the result
    private Result(){}

    public static void showResult(TextField textField){
        if (isDouble) {
            textField.setText(String.valueOf(dResult));
        }
        else {
            textField.setText(String.valueOf(lResult));
        }
        printResult();

        // Clear the result for future usage
        dResult = 0;
        lResult = 0;
        isDouble = false;
    }

    private static void printResult(){
        System.out.println("lResult " + lResult + " dResult: " + dResult + " isDouble: " + isDouble);
    }

    public static void add(double dOperand){
        if (isDouble) {
            dResult += dOperand;
        }
        else { // Converting to double
            isDouble = true;
            dResult += dOperand + ((double) lResult);
        }
        System.out.println("dOperand: " + dOperand);
        printResult();
    }

    public static void add(long lOperand){
        if (!isDouble) {
            lResult += lOperand;
        }
        else {
            dResult += ((double) lOperand);
        }
        printResult();
    }

    public static void mul(double dOperand)
    {
        if (!isDouble) { // Converting to double
            isDouble = true;
            dResult += ((double) lResult);
        }
        dResult *= dOperand;
        System.out.println("dOperand: " + dOperand);
        printResult();
    }

    public static void mul(long lOperand)
    {
        if (!isDouble) {
            lResult *= lOperand;
        }
        else {
            dResult *= ((double) lOperand);
        }
        printResult();
    }


    public static void div(double dOperand) throws ArithmeticException
    {
        if (dOperand == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (!isDouble) { // Converting to double
            isDouble = true;
            dResult += ((double) lResult);
        }
        dResult /= dOperand;
        System.out.println("dOperand: " + dOperand);
        printResult();
    }

    public static void div(long lOperand) throws ArithmeticException
    {
        if (lOperand == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (!isDouble) {
            lResult /= lOperand;
        }
        else {
            dResult /= ((double) lOperand);
        }
        printResult();
    }
}
