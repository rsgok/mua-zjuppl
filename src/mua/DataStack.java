package mua;

import java.util.Stack;

public class DataStack {
    Stack<String> dData = new Stack<>();
    public void push(String input) {
        dData.push(input);
    }
    public String pop() {
        return dData.pop();
    }
}
