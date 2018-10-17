package expressionOperations;


import java.util.Stack;

public class ExpressionEvaluator {

    public String infixToPostfix(String expression) {
        if (expression.length() == 0) {
            throw null;
        }
        Stack<Character> stack = new Stack<>();
        StringBuilder res = new StringBuilder();
        int bracesCounter = 0;
        expression = expression.replaceAll("\\s+", " ");
        for (int i = 0; i < expression.length(); i++) {
            char z = expression.charAt(i);
            if (z == '|') {
                while (stack.size() != 0 && (stack.peek().equals('&') || stack.peek().equals('>'))) {
                    res.append(stack.pop() + " ");
                }
                while (stack.size() != 0
                        && ((stack.peek().equals('|')))) {
                    res.append(stack.pop() + " ");
                }
                stack.push(z);
            } else if (z == '&' || z == '>') {
                while (stack.size() != 0
                        && ((stack.peek().equals('&') && z == '>') || (stack.peek().equals('>') && z == '&'))) {
                    res.append(stack.pop() + " ");
                }
                if (stack.size() != 0 && (stack.peek().equals('&') || stack.peek().equals('>'))) {
                    res.append(stack.pop() + " ");
                }
                stack.push(z);
            } else if (z == '!') {
                stack.push(z);
            } else if (z == '(') {
                stack.push(z);
                bracesCounter++;
            } else if (z == ')') {
                while (!stack.peek().equals('(')) {
                    res.append(stack.pop() + " ");
                    if (stack.size() == 0) {
                        throw new RuntimeException();
                    }
                }
                stack.pop();
                bracesCounter--;
            } else {
                res.append(z + " ");
            }
        }
        if (bracesCounter != 0) {
            throw new RuntimeException();
        }
        while (stack.size() > 0) {
            res.append(stack.pop() + " ");
        }
        return res.toString();
    }

    public Byte evaluate(String expression) {
        if (expression.length() == 0 || expression == null) {
            throw null;
        }
        expression = expression.trim();
        String[] values = expression.split("\\s+");
        Stack<Byte> stack = new Stack<>();
        for (int i = 0; i < values.length; i++) {
            char z = values[i].charAt(0);
            if (z == '&') {
                stack.push((byte) (stack.pop() & stack.pop()));
            } else if (z == '|') {
                stack.push(((byte) (stack.pop() |  stack.pop())));
            } else if (z == '>') {
                stack.push((byte) (stack.pop() | stack.pop().byteValue() ^ 1));
            } else if (z == '!') {
                stack.push((byte) (stack.pop() ^ 1));
            } else {
                stack.push(Byte.parseByte(String.valueOf(z)));
            }
        }
        if (stack.size() != 1) {
            throw new RuntimeException();
        }
        return  stack.pop();
    }

    private boolean isOp(char z) {
        if (z == '&' || z == '|' || z == '!' || z == '>') {
            return true;
        }
        return false;
    }
}
