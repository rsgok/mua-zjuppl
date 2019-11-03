package mua;

import java.util.Stack;

public class OperationStack {
    Stack<Pair> opData = new Stack<>();

    public boolean empty() {
        return opData.empty();
    }
    public Pair peek() {
        return opData.peek();
    }
    public Pair pop() {
        return opData.pop();
    }
    public void push(Pair p) {
        opData.push(p);
    }

    // 使操作栈top的操作的param数增加1
    public void addToOperationStackTop() {
        Pair topOp = opData.pop();
        topOp.second++;
        opData.push(topOp);
    }
}
