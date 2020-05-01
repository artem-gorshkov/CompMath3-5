package ru.itmo.gorshkov;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;

public class Parser {
    public static String parseEquation(String str) throws IllegalArgumentException {
        String str1 = str.substring(0, str.indexOf('='));
        String str2 = str.substring(str.indexOf('=') + 1);
        Expression e = new Expression(str1 + "-(" + str2 + ")");
        if(e.checkSyntax())
            throw new IllegalArgumentException();
        return str1 + "-(" + str2 + ")";
    }
}
