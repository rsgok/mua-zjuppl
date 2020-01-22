package mua;

import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

/**
 * Util
 */
public class Util {
    public HashMap<String, Integer> argNumber = new HashMap<String, Integer>() {
        {
            put("make", 2);
            put("thing", 1);
            put(":", 1);
            put("name", 1);
            put("erase", 1);
            put("isname", 1);
            put("print", 1);
            put("read", 0);
            put("add", 2);
            put("sub", 2);
            put("mul", 2);
            put("div", 2);
            put("mod", 2);
            put("eq", 2);
            put("gt", 2);
            put("lt", 2);
            put("and", 2);
            put("or", 2);
            put("not", 1);
            put("output", 1);
            put("stop", 0);
            put("export", 1);
            put("if", 3);
            put("isnumber", 1);
            put("isword", 1);
            put("islist", 1);
            put("isbool", 1);
            put("isempty", 1);
            put("readlist", 0);
            put("repeat", 2);
            put("word", 2);
            put("sentence", 2);
            put("list", 2);
            put("join", 2);
            put("first", 1);
            put("last", 1);
            put("butfirst", 1);
            put("butlast", 1);
            put("random", 1);
            put("floor", 1);
            put("sqrt", 1);
            put("wait", 1);
            put("save", 1);
            put("load", 1);
            put("erall", 0);
            put("poall", 0);
        }
    };

    // judge is function
    public boolean isFunction(String str) {
        if (str.charAt(0) == '\"' || str.charAt(0) == '(')
            return false;
        Data x = new Data(str);
        return !x.isList() && !x.isBool() && !x.isNumber();
    }

    // judge is operation
    public boolean isOperation(String str) {
        return argNumber.containsKey(str);
    }

    // split into instruction parts
    public Vector<String> splitInstruction(String instruction) {
        if (instruction.equals("")) return new Vector<>();

        Vector<String> instArray = new Vector<>();
        instruction = instruction.trim();
        String[] splitStr = instruction.split("\\s+");
        for (int i = 0; i < splitStr.length; i++) {
            if (judgeBrackets(splitStr[i]))
                instArray.add(splitStr[i]);
            else
                splitStr[i + 1] = splitStr[i] + " " + splitStr[i + 1];
        }
        return instArray;
    }

    // check the brackets
    public boolean judgeBrackets(String str) {
        Stack<Character> brackets = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            Character ch = str.charAt(i);
            if (ch == '[' || ch == '(') {
                brackets.push(ch);
            } else if (ch == ']') {
                if (brackets.empty() || '[' != brackets.pop())
                    return false;
            } else if (ch == ')') {
                if (brackets.empty() || '(' != brackets.pop())
                    return false;
            }
        }
        return brackets.empty();
    }

    public Integer getCalculateExpressionSplit(String exp){
        Integer res = 0;
        if(exp.charAt(0)=='-'||exp.charAt(0)=='+') res++;
        while(exp.length()>res && (!"+-*/%".contains(""+exp.charAt(res)) || !judgeBrackets(exp.substring(0,res+1))))
            res++;
        return res;
    }

}