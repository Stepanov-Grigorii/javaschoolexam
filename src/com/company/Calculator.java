package com.company;

import java.util.*;
import java.util.regex.Pattern;

public class Calculator {

    static final HashMap<String, Integer> precedence;

    static {
        precedence = new HashMap<>();
        precedence.put("*", 2);
        precedence.put("/", 2);
        precedence.put("+", 1);
        precedence.put("-", 1);
    }

    public String evaluate(String statement) {

        if (!checkLineCorrectness(statement))
            return null;

        Queue<String> infixQueue = new LinkedList<>();
        Character c;
        String input;
        String multiDigit = "";

        input = statement;
        input = input.replaceAll(" ", "");

        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);

            if (c.equals('(') || c.equals(')')) {
                infixQueue.add(c.toString());
            } else if (!Character.isDigit(c)) {
                infixQueue.add(c.toString());
            } else if (Character.isDigit(c)) {
                if (i + 1 < input.length() && input.charAt(i + 1) == '.')
                {
                    int j = i + 1;
                    multiDigit = c.toString() + input.charAt(j);
                    while (j + 1 <= input.length() - 1 && Character.isDigit(input.charAt(j + 1))) {
                        multiDigit = multiDigit + input.charAt(j + 1);
                        j++;
                    }
                    i = j;
                    infixQueue.add(multiDigit);
                    multiDigit = "";
                } else if (i + 1 <= input.length() - 1 && Character.isDigit(input.charAt(i + 1))) {
                    int j = i;
                    while (j <= input.length() - 1 && Character.isDigit(input.charAt(j))) {
                        multiDigit = multiDigit + input.charAt(j);
                        j++;
                    }
                    i = j - 1;
                    infixQueue.add(multiDigit);
                    multiDigit = "";
                } else {
                    infixQueue.add(c.toString());
                }

            }
        }
        return infixToPostfix(infixQueue);
    }

    public boolean checkLineCorrectness(String statement) {
        String regexRepeat = "[./\\-*+]{2,}";
        String regex = "[^0-9./\\-*+()]";
        Pattern repeatPattern = Pattern.compile(regexRepeat);
        Pattern pattern = Pattern.compile(regex);
        if (statement == null || statement.isEmpty()) {
            return false;
        }
        if (pattern.matcher(statement).find()) {
            return false;
        }
        if (repeatPattern.matcher(statement).find()) {
            return false;
        }
        return true;
    }

    public static String infixToPostfix(Queue<String> infixQueue) {
        Stack<String> operatorStack = new Stack<>();
        Queue<String> postQueue = new LinkedList<>();
        String t;
        while (!infixQueue.isEmpty()) {
            t = infixQueue.poll();
            try {
                Double.parseDouble(t);
                postQueue.add(t);
            } catch (NumberFormatException nfe) {
                if (operatorStack.isEmpty()) {
                    operatorStack.add(t);
                } else if (t.equals("(")) {
                    operatorStack.add(t);
                } else if (t.equals(")")) {
                    while (!operatorStack.peek().equals("(")) {
                        postQueue.add(operatorStack.peek());
                        operatorStack.pop();
                    }
                    operatorStack.pop();
                } else {
                    while (!operatorStack.empty() && !operatorStack.peek().equals("(") && precedence.get(t) <= precedence.get(operatorStack.peek())) {
                        postQueue.add(operatorStack.peek());
                        operatorStack.pop();
                    }
                    operatorStack.push(t);
                }
            }
        }
        while (!operatorStack.empty()) {
            postQueue.add(operatorStack.peek());
            operatorStack.pop();
        }

        if(postQueue.contains("(") || postQueue.contains(")"))
            return null;

        return postfixEvaluation(postQueue);
    }

    public static String postfixEvaluation(Queue<String> postQueue) {
        Stack<String> eval = new Stack<>();
        String t;
        Double headNumber, nextNumber, result = 0.0;
        while (!postQueue.isEmpty()) {
            t = postQueue.poll();
            try {
                Double.parseDouble(t);
                eval.add(t);
            } catch (NumberFormatException nfe) {
                headNumber = Double.parseDouble(eval.peek());
                eval.pop();
                nextNumber = Double.parseDouble(eval.peek());
                eval.pop();

                switch (t) {
                    case "+":
                        result = nextNumber + headNumber;
                        break;
                    case "-":
                        result = nextNumber - headNumber;
                        break;
                    case "*":
                        result = nextNumber * headNumber;
                        break;
                    case "/":
                        if (headNumber == 0) {
                            return null;
                        } else {
                            result = nextNumber / headNumber;
                            break;
                        }
                }
                eval.push(result.toString());
            }
        }
        if(Double.parseDouble(eval.peek()) % 1 == 0){
            eval.add(String.valueOf((int) Double.parseDouble(eval.pop())));
        }
        return eval.peek();
    }
}