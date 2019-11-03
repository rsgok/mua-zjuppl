package mua;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    // 操作名字空间，保留字
    Map<String, Integer> operationNames = new HashMap<>();
    // 命名空间，用来储存数据
    NameSpace nSpace = new NameSpace();
    // 操作栈，用来放置操作及其当前得到的param个数
    OperationStack opStack = new OperationStack();
    // 数据栈，用来放置除了操作的其他输入，当作操作的param
    DataStack dStack = new DataStack();
    // 操作函数类
    Operation ops = new Operation();

    Interpreter() {
        initOperationNames();
        initOperation();
    }

    public void go(String input) {
        // 判断是输入是操作与否
        if (isOperation(input)) {
            Pair newPair = new Pair(input, 0);
            opStack.push(newPair);
        } else {
            dStack.push(input);
            opStack.addToOperationStackTop();
        }
        // 判断触发操作动作
        Analysis();
    }

    void Analysis() {
        // 持续判断是否会触发操作
        while (isTriggerOperation()) {
            // pop掉顶层的操作
            Pair topOp = opStack.pop();
            String opName = topOp.first;
            // 开始执行该操作
            ops.doOperation(opName);
        }
    }

    void initOperationNames() {
        operationNames.put("make", 2);
        operationNames.put("thing", 1);
        operationNames.put("erase", 1);
        operationNames.put("isname", 1);
        operationNames.put("print", 1);
        operationNames.put("read", 1);
        operationNames.put("add", 2);
        operationNames.put("sub", 2);
        operationNames.put("mul", 2);
        operationNames.put("div", 2);
        operationNames.put("mod", 2);
        operationNames.put("eq", 2);
        operationNames.put("gt", 2);
        operationNames.put("lt", 2);
        operationNames.put("and", 2);
        operationNames.put("or", 2);
        operationNames.put("not", 1);
    }

    void initOperation() {
        ops.dStack = this.dStack;
        ops.nSpace = this.nSpace;
        ops.operationNames = this.operationNames;
        ops.opStack = this.opStack;
    }

    boolean isOperation(String input) {
        return operationNames.containsKey(input);
    }

    boolean isTriggerOperation() {
        if (opStack.empty()) return false;
        Pair topOp = opStack.peek();
        // 已经有的指令数等于所需的指令数，触发操作
        return operationNames.get(topOp.first).equals(topOp.second);
    }
}
