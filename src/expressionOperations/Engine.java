package expressionOperations;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by khaledabdelfattah on 11/26/17.
 */
public class Engine {
    private Set<Character> termsSet;
    private int num;
    public Byte[] getValues(String expression) {
        Byte[] table;
        try {
            ExpressionEvaluator evaluator = new ExpressionEvaluator();
            String postFix = evaluator.infixToPostfix(expression);
            num = numOfVariables(expression);
            table = new Byte[(int) Math.pow(2, num)];
            for (int i = 0; i < table.length; i ++) {
                table[i] = evaluator.evaluate(processing(postFix, i));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return table;
    }

    public Set<Character> getTerms (){
        return this.termsSet;
    }

    private int numOfVariables(String expression) {
        termsSet = new HashSet<>();
        for (Character c : expression.toCharArray()) {
            if(Character.isLetter(c))
                termsSet.add(c);
            else if (!c.equals('&') && !c.equals('|') && !c.equals('!')
                    && !c.equals('>') && !c.equals(' ') && !c.equals('(') &&
                    !c.equals(')'))
                throw new RuntimeException();
        }
        return termsSet.size();
    }

    private String processing(String expression, int rowNum) {
        int flag = 1 << (num - 1);
        for (Iterator<Character> it = termsSet.iterator(); it.hasNext(); ) {
            int tmp = rowNum & flag;
            expression = expression.replaceAll(it.next().toString(),
                    String.valueOf((tmp != 0) ? 1 : 0));
            flag >>= 1;
        }
        return expression;
    }

}
