package org.calculator;

import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorController {
    String textFieldText = null;
    // private boolean isStillCalculating = false;

    // NOTE: WE MUST NOT STORE THE TEXTFIELD TEXT IN THE BUTTON CONTROLLER CLASS
    // If I ever want to make the calculator multi-threaded, I will need to make this method synchronized
    public synchronized void calculate(TextField textField){
        textFieldText = textField.getText().trim(); // Trim is a method that removes the spaces at the start and end of the string https://www.w3schools.com/java/ref_string_trim.asp
        textField.clear(); // Clear the textField

        // If there is no operation to perform, return, even if we do not return the app will work anyway, but by doing this we use less resources
        if (textFieldText.isEmpty()) {
            return;
        }

        // Handle minus at the start: -3+6*9 -> 0-3+6*9
        // So that we always get an ODD number of digits (this is because in the tokens loop i do get(i + 1) and I increase i by 2 each time
        if (textFieldText.startsWith("-")) {
            textFieldText = "0" + textFieldText;
        }

        // Parse the textFieldText and calculate the result (tokenization using +-*/ as tokens)
        // https://www.w3schools.com/java/java_regex.asp
        // OR: String[] tokens = textFieldText.split("(?<=[+\\-*/])|(?=[+\\-*/])");
        /** REGEX PATTERNS
         * Because \ is also an escape in Java source, you have to double escape it "\\"
         * A | B is an OR operator
         * \d = any digit ([0–9]).
         * + = one or more.
         * \d+ = “one or more digits”
         * (...) = a group
         * ? = find zero or one times.
         * ? (after the group) = the whole group is optional (0 or 1 times)
         * ?: = non-capturing group (don't capture group index)
         * (?:\.\d+)? = “optionally, a dot followed by one or more digits”
         * So we tokenize either a number or a single operator
         */
        Pattern pattern = Pattern.compile("\\d+(?:\\.\\d+)?|[+\\-*/]");
        Matcher matcher = pattern.matcher(textFieldText);

        ArrayList<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        // If the last token is an operand then delete it
        String lastToken = tokens.getLast();
        if (Objects.equals(lastToken, "+") || Objects.equals(lastToken, "-") || Objects.equals(lastToken, "*")
                || Objects.equals(lastToken, "/") || Objects.equals(lastToken, ".")){
            tokens.removeLast();
        }


        // Do a first pass to execute multiplications and divisions
        ArrayList<String> prioritizedTokens = new ArrayList<>(); // List of tokens in order of execution
        prioritizedTokens.add(tokens.getFirst());

        for (int i = 1; i < tokens.size(); i += 2) {
            String operator = tokens.get(i);
            String nextToken = tokens.get(i + 1);

            if (Objects.equals(operator, "*") || Objects.equals(operator, "/")) {
                double leftOperand = Double.parseDouble(prioritizedTokens.removeLast());
                double rightOperand = Double.parseDouble(nextToken);

                if (Objects.equals(operator, "/") && rightOperand == 0) {
                    textField.setText("Error: division by zero");
                    return;
                }

                double partialResult = Objects.equals(operator, "*")
                        ? leftOperand * rightOperand
                        : leftOperand / rightOperand;

                prioritizedTokens.add(String.valueOf(partialResult));
            } else {
                prioritizedTokens.add(operator);
                prioritizedTokens.add(nextToken);
            }
        }
        tokens = prioritizedTokens;

        // Second pass to execute additions and subtractions
        for (String token : tokens) {
            System.out.println("[" + token + "]");
        }

        // The operation part:
        Result.add(Double.parseDouble(tokens.get(0))); // The initial number is not anticipated by a token so we need to sum it by default
        // Remaining operations
        for (int i = 1; i < tokens.size(); i += 2) {
            String operator = tokens.get(i);
            double number = Double.parseDouble(tokens.get(i + 1));
            switch (operator) {
                case "+": {
                    Result.add(number);
                    break;
                }
                case "-": {
                    Result.add(-number);
                    break;
                }
                case "*": {
                    Result.mul(number);
                    break;
                }
                case "/": {
                    try {
                        Result.div(number);
                    } catch (Exception e) {
                        // throw new RuntimeException(e);
                        textField.setText("Error: division by zero");
                    }
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected token: " + operator);
                }

            }
        }
        // Show the result of the calculation
        Result.showResult(textField);

    }
}
