package mua;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    Map<String, Integer> operationNames = new HashMap<>();
    // 命名空间，用来储存数据
    NameSpace nSpace = new NameSpace();
    // 操作栈，用来放置操作及其当前得到的param个数
    OperationStack opStack = new OperationStack();
    // 数据栈，用来放置除了操作的其他输入，当作操作的param
    DataStack dStack = new DataStack();
    // 操作函数类
    Operation ops = new Operation();

    public void go(String input) {
        // 判断是输入是操作与否
        if(isOperation(input)) {
            Pair newPair = new Pair(input, 0);
            opStack.opData.push(newPair);
        }
        else {
            dStack.dData.push(input);
            addToOperationStackTop();
        }
        // 持续判断是否会触发操作
        while (isTriggerOperation()) {
            // pop掉顶层的操作
            Pair topOp = opStack.opData.pop();
            String opName = topOp.first;
            // 开始执行该操作
            if(opName.equals("print")) {
                String topdata = dStack.dData.pop();
                ops.doPrint(evalValue(topdata));
            }
            else if(opName.equals("thing")) {
                String topdata = dStack.dData.pop();
                String boundValue = evalValue(topdata);
                dStack.dData.push(boundValue);
                addToOperationStackTop();
            }
            else if(opName.equals("erase")) {
                String topdata = dStack.dData.pop();
                nSpace.myNameSpace.remove(topdata.substring(1)); // "以后的部分
            }
            else if(opName.equals("make")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                // 将data1绑定到data2上
                String variableName = data2.substring(1);
                String variableValue = data1;
                // 如果有这个变量了，那就先删掉
                if(nSpace.myNameSpace.containsKey(variableName))
                {
                    nSpace.myNameSpace.remove(variableName);
                }
                // 绑定变量
                nSpace.myNameSpace.put(variableName,variableValue);
            }
            else if(opName.equals("isname")) {
                String topdata = dStack.dData.pop();
                String result = doIsname(topdata);
                dStack.dData.push(result);
                addToOperationStackTop();
            }
            else if(opName.equals("read")) {
                addToOperationStackTop();
            }
            else if(opName.equals("add")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                Double d1 = Double.parseDouble(evalValue(data1));
                Double d2 = Double.parseDouble(evalValue(data2));
                String addResult = String.valueOf(d1+d2);
                dStack.dData.push(addResult);
                addToOperationStackTop();
            }
            else if(opName.equals("sub")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                Double d1 = Double.parseDouble(evalValue(data1));
                Double d2 = Double.parseDouble(evalValue(data2));
                String subResult = String.valueOf(d2-d1);
                dStack.dData.push(subResult);
                addToOperationStackTop();
            }
            else if(opName.equals("mul")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                Double d1 = Double.parseDouble(evalValue(data1));
                Double d2 = Double.parseDouble(evalValue(data2));
                String mulResult = String.valueOf(d1*d2);
                dStack.dData.push(mulResult);
                addToOperationStackTop();
            }
            else if(opName.equals("div")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                Double d1 = Double.parseDouble(evalValue(data1));
                Double d2 = Double.parseDouble(evalValue(data2));
                String divResult = String.valueOf(d2/d1);
                dStack.dData.push(divResult);
                addToOperationStackTop();
            }
            else if(opName.equals("mod")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                Double d1 = Double.parseDouble(evalValue(data1));
                Double d2 = Double.parseDouble(evalValue(data2));
                String modResult = String.valueOf(d2%d1);
                dStack.dData.push(modResult);
                addToOperationStackTop();
            }
            else if(opName.equals("and")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                String andResult = doAnd(evalValue(data1),evalValue(data2));
                dStack.dData.push(andResult);
                addToOperationStackTop();
            }
            else if(opName.equals("or")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                String orResult = doOr(evalValue(data1),evalValue(data2));
                dStack.dData.push(orResult);
                addToOperationStackTop();
            }
            else if(opName.equals("not")) {
                String topData = dStack.dData.pop();
                String notResult = doNot(evalValue(topData));
                dStack.dData.push(notResult);
                addToOperationStackTop();
            }
            else if(opName.equals("eq")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                String eqResult = doEq(evalValue(data1),evalValue(data2));
                dStack.dData.push(eqResult);
                addToOperationStackTop();
            }
            else if(opName.equals("gt")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                String gtResult = doGt(evalValue(data1),evalValue(data2));
                dStack.dData.push(gtResult);
                addToOperationStackTop();
            }
            else if(opName.equals("lt")) {
                String data1 = dStack.dData.pop();
                String data2 = dStack.dData.pop();
                String ltResult = doLt(evalValue(data1),evalValue(data2));
                dStack.dData.push(ltResult);
                addToOperationStackTop();
            }
        }
    }

    // 使操作栈top的操作的param数增加1
    void addToOperationStackTop() {
        Pair topOp = opStack.opData.pop();
        topOp.second++;
        opStack.opData.push(topOp);
    }

    public Interpreter() {
        initOperationNames();
    }

    void initOperationNames() {
        operationNames.put("make",2);
        operationNames.put("thing",1);
        operationNames.put("erase",1);
        operationNames.put("isname",1);
        operationNames.put("print",1);
        operationNames.put("read",1);
        operationNames.put("add",2);
        operationNames.put("sub",2);
        operationNames.put("mul",2);
        operationNames.put("div",2);
        operationNames.put("mod",2);
        operationNames.put("eq",2);
        operationNames.put("gt",2);
        operationNames.put("lt",2);
        operationNames.put("and",2);
        operationNames.put("or",2);
        operationNames.put("not",1);
    }

    boolean isOperation(String input) {
        return operationNames.keySet().contains(input);
    }

    boolean isTriggerOperation() {
        if(opStack.opData.empty()) return false;
        Pair topOp = opStack.opData.peek();
        // 已经有的指令数等于所需的指令数，触发操作
        return operationNames.get(topOp.first).equals(topOp.second);
    }

    String evalValue(String data) {
        //System.out.println("eval value: "+data);
        if (data.charAt(0) == ':' || data.charAt(0) == '\"') {
            String nextData = nSpace.myNameSpace.get(data.substring(1));
            return evalValue(nextData);
        }
        else {
            return data;
        }
    }

    String doIsname(String data) {
        if(data.charAt(0)!='\"') return "false";
        if(nSpace.myNameSpace.containsKey(data.substring(1))) {
            return "true";
        }
        return "false";
    }

    String doAnd(String s1, String s2) {
        Boolean res;
        res = Boolean.parseBoolean(s1) && Boolean.parseBoolean(s1);
        return String.valueOf(res);
    }

    String doOr(String s1, String s2) {
        Boolean res;
        res = Boolean.parseBoolean(s1) || Boolean.parseBoolean(s1);
        return String.valueOf(res);
    }

    String doNot(String s) {
        Boolean res;
        res = !Boolean.parseBoolean(s);
        return String.valueOf(res);
    }

    String doEq(String s1, String s2) {
        return String.valueOf(s1.equals(s2));
    }
    String doGt(String s1, String s2) {
        // s2大于s1则返回"true"
        Double d1 = Double.parseDouble(s1);
        Double d2 = Double.parseDouble(s2);
        int cp = d1.compareTo(d2);
        if(cp < 0) return "true";
        else return "false";
    }
    String doLt(String s1, String s2) {
        // s1大于s2则返回"true"
        Double d1 = Double.parseDouble(s1);
        Double d2 = Double.parseDouble(s2);
        int cp = d2.compareTo(d1);
        if(cp < 0) return "true";
        else return "false";
    }
}
