package mua;

public class Main {
    public static void main(String[] args) throws Exception {
        InputCtrl in = new InputCtrl();
        NameSpace ns = new NameSpace();
        Operation op = new Operation(ns, in);
        while (in.hasNext()) {
            op.runInstruction(in.getLine());
        }
    }
}
