package com.company;

import java.util.*;
import java.util.regex.Pattern;

public class Calculator {

    public String evaluate(String statement) {

        if (!check(statement))
            return null;
        try {
            double res = result(statement);
            if (res % 1 == 0) {
                return String.valueOf((int) res);
            }
            return String.valueOf(res);
        } catch (Exception exception) {
            return null;
        }
    }

    private final List<String> operators = Arrays.asList("(", ")", "+", "-", "*", "/");

    public boolean check(String statement) {
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

    private List<String> Separate(String input) {
        List<String> result = new ArrayList<>();
        int pos = 0;
        while (pos < input.length()) {
            String s = String.valueOf(input.charAt(pos));
            if (!operators.contains(String.valueOf(input.charAt(pos)))) {
                if (Character.isDigit(input.charAt(pos)))
                    for (int i = pos + 1; i < input.length() &&
                            (Character.isDigit(input.charAt(i)) || input.charAt(i) == ',' || input.charAt(i) == '.'); i++)
                        s += input.charAt(i);
                else if (Character.isAlphabetic(input.charAt(pos)))
                    for (int i = pos + 1; i < input.length() &&
                            (Character.isAlphabetic(input.charAt(i)) || Character.isDigit(input.charAt(i))); i++)
                        s += input.charAt(i);
            }
            result.add(s);
            pos += s.length();
        }
        return result;
    }

    private byte GetPriority(String s) {
        switch (s) {
            case "(":
            case ")":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return 3;
        }
    }

    public List<String> ConvertToPostfixNotation(String input) {
        List<String> outputSeparated = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (String c : Separate(input)) {
            if (operators.contains(c)) {
                if (stack.size() > 0 && !c.equals("(")) {
                    if (c.equals(")")) {
                        String s = stack.pop();
                        while (s != "(") {
                            outputSeparated.add(s);
                            s = stack.pop();
                        }
                    } else if (GetPriority(c) > GetPriority(stack.peek())) {
                        String buf = stack.pop();
                        stack.push(c);
                        stack.push(buf);
                    } else {
                        while (stack.size() > 0 && GetPriority(c) <= GetPriority(stack.peek()))
                            outputSeparated.add(stack.pop());
                        stack.push(c);
                    }
                } else
                    stack.push(c);
            } else
                outputSeparated.add(c);
        }
        if (stack.size() > 0)
            outputSeparated.addAll(stack);

        return outputSeparated;
    }

    public double result(String input) {
        Stack<String> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>(ConvertToPostfixNotation(input));
        String str = queue.poll();
        while (queue.size() >= 0) {
            if (!operators.contains(str)) {
                stack.push(str);
                str = queue.poll();
            } else {
                double summ = 0;
                try {

                    switch (str) {

                        case "+": {
                            double a = Double.parseDouble(stack.pop());
                            double b = Double.parseDouble(stack.pop());
                            summ = a + b;
                            break;
                        }
                        case "-": {
                            double a = Double.parseDouble(stack.pop());
                            double b = Double.parseDouble(stack.pop());
                            summ = b - a;
                            break;
                        }
                        case "*": {
                            double a = Double.parseDouble(stack.pop());
                            double b = Double.parseDouble(stack.pop());
                            summ = b * a;
                            break;
                        }
                        case "/": {
                            double a = Double.parseDouble(stack.pop());
                            double b = Double.parseDouble(stack.pop());
                            if (a == 0.)
                                throw new Exception();
                            summ = b / a;
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                stack.push(String.valueOf(summ));
                if (queue.size() > 0)
                    str = queue.poll();
                else
                    break;
            }
        }
        return Double.parseDouble(stack.pop());
    }
}
