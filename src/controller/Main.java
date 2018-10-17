package controller;

import expressionOperations.Engine;
import expressionOperations.ExpressionEvaluator;

import java.util.Set;

/**
 * Created by khaledabdelfattah on 11/26/17.
 */
public class Main {
    public static void main(String[] args) {
        Engine e = new Engine();
        Byte[] tmp = e.getValues("(A > B | !C) & A");
        for (int i = 0; i < tmp.length; i++)
            System.out.println(tmp[i]);

        Set<Character> termsSet = e.getTerms();
        for (Character c : termsSet){
            System.out.print(c + " ");
        }
    }

}
