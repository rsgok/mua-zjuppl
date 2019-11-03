package mua;

import java.util.HashMap;
import java.util.Map;

public class Operation {
    public Map<String, Integer> operationNames = new HashMap<>();
    public NameSpace nSpace = new NameSpace();
    public OperationStack opStack = new OperationStack();
    public DataStack dStack = new DataStack();

    String evalValue(String data) {
        //System.out.println("eval value: "+data);
        String newData;
        if (data.charAt(0) == ':' || data.charAt(0) == '\"') {
            newData = data.substring(1);
            if (isNumeric(newData) || isBool(newData)) {
                return newData;
            }
            if (nSpace.containsKey(newData)) {
                String nextData = nSpace.get(newData);
                return evalValue(nextData);
            } else {
                return newData;
            }
        } else {
            return data;
        }
    }

    String evalVariable(String str) {
        if (str.charAt(0) == ':') {
            return evalVariable(nSpace.get(str.substring(1)));
        }
        return str;
    }

    boolean isBool(String str) {
        String newStr = str;
        if (str.charAt(0) == '\"') newStr = newStr.substring(1);
        return newStr.equals("true") || newStr.equals("false");
    }

    boolean isNumeric(String str) {
        String newStr = str;
        if (str.charAt(0) == '\"') newStr = newStr.substring(1);
        for (int i = 0; i < newStr.length(); i++) {
            if (!Character.isDigit(newStr.charAt(i)) && newStr.charAt(i) != '-' && newStr.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    // 暴露操作统一调配接口
    public void doOperation(String opName) {
        switch (opName) {
            case "print":
                doPrint();
                break;
            case "thing":
                doThing();
                break;
            case "erase":
                doErase();
                break;
            case "make":
                doMake();
                break;
            case "isname":
                doIsname();
                break;
            case "read":
                doRead();
                break;
            case "add":
                doAdd();
                break;
            case "sub":
                doSub();
                break;
            case "mul":
                doMul();
                break;
            case "div":
                doDiv();
                break;
            case "mod":
                doMod();
                break;
            case "and":
                doAnd();
                break;
            case "or":
                doOr();
                break;
            case "not":
                doNot();
                break;
            case "eq":
                doEq();
                break;
            case "gt":
                doGt();
                break;
            case "lt":
                doLt();
                break;
        }
    }

    // operations
    void doPrint() {
        String str = evalValue(dStack.pop());
        if (str == null) return;
        if (isNumeric(str)) {
            Double temp = Double.parseDouble(str);
            System.out.println(temp);
        } else System.out.println(str);
    }

    void doThing() {
        String topdata = dStack.pop();
        String boundValue = evalValue(topdata);
        dStack.push(boundValue);
        opStack.addToOperationStackTop();
    }

    void doErase() {
        String topdata = dStack.pop();
        nSpace.remove(evalVariable(topdata).substring(1)); // "以后的部分
    }

    void doMake() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        // 将data1绑定到data2上
        String variableName = data2.substring(1);
        String variableValue = data1;
        // 如果有这个变量了，那就先删掉
        nSpace.remove(variableName);
        // 绑定变量
        nSpace.put(variableName, variableValue);
    }

    void doIsname() {
        String topdata = dStack.pop();
        String result = "true";
        if (topdata.charAt(0) != '\"') result = "false";
        if (!nSpace.containsKey(topdata.substring(1))) {
            result = "false";
        }
        dStack.push(result);
        opStack.addToOperationStackTop();
    }

    void doRead() {
        opStack.addToOperationStackTop();
    }

    void doAdd() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        String addResult = String.valueOf(d1 + d2);
        dStack.push(addResult);
        opStack.addToOperationStackTop();
    }

    void doSub() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        String subResult = String.valueOf(d2 - d1);
        dStack.push(subResult);
        opStack.addToOperationStackTop();
    }

    void doMul() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        String mulResult = String.valueOf(d1 * d2);
        dStack.push(mulResult);
        opStack.addToOperationStackTop();
    }

    void doDiv() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        String divResult = String.valueOf(d2 / d1);
        dStack.push(divResult);
        opStack.addToOperationStackTop();
    }

    void doMod() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        String modResult = String.valueOf(d2 % d1);
        dStack.push(modResult);
        opStack.addToOperationStackTop();
    }

    void doAnd() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        String s1 = evalValue(data1);
        String s2 = evalValue(data2);
        Boolean res;
        Boolean b1 = Boolean.parseBoolean(s1);
        Boolean b2 = Boolean.parseBoolean(s2);
        res = b1 && b2;
        String andResult = String.valueOf(res);
        dStack.push(andResult);
        opStack.addToOperationStackTop();
    }

    void doOr() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        String s1 = evalValue(data1);
        String s2 = evalValue(data2);
        Boolean res;
        Boolean b1 = Boolean.parseBoolean(s1);
        Boolean b2 = Boolean.parseBoolean(s2);
        res = b1 || b2;
        String orResult = String.valueOf(res);
        dStack.push(orResult);
        opStack.addToOperationStackTop();
    }

    void doNot() {
        String data = dStack.pop();
        String s = evalValue(data);
        Boolean res;
        Boolean b = Boolean.parseBoolean(s);
        res = !b;
        String notResult = String.valueOf(res);
        dStack.push(notResult);
        opStack.addToOperationStackTop();
    }

    void doEq() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        String eqResult = String.valueOf(evalValue(data1).equals(evalValue(data2)));
        dStack.push(eqResult);
        opStack.addToOperationStackTop();
    }

    void doGt() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        // s2大于s1则返回"true"
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        int cp = d1.compareTo(d2);
        String gtResult;
        if (cp < 0) gtResult = "true";
        else gtResult = "false";
        dStack.push(gtResult);
        opStack.addToOperationStackTop();
    }

    void doLt() {
        String data1 = dStack.pop();
        String data2 = dStack.pop();
        // s1大于s2则返回"true"
        Double d1 = Double.parseDouble(evalValue(data1));
        Double d2 = Double.parseDouble(evalValue(data2));
        int cp = d2.compareTo(d1);
        String ltResult;
        if (cp < 0) ltResult = "true";
        else ltResult = "false";
        dStack.push(ltResult);
        opStack.addToOperationStackTop();
    }
}
