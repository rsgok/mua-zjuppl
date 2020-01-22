package mua;

import java.io.*;
import java.util.*;

public class Operation {
    NameSpace nowNameSpace;
    InputCtrl input;
    Data outputTmp;
    HashMap<String, Integer> argNumber;
    Util uu = new Util();
    Stack<Integer> argCount = new Stack<Integer>();
    Stack<Integer> argN = new Stack<>();
    Stack<Data> dataStack = new Stack<Data>();

    public Operation(NameSpace ns, InputCtrl in) {
        this.nowNameSpace = ns;
        this.input = in;
        this.argNumber = uu.argNumber;
    }

    // start the operation
    public void runInstruction(String instruction) throws Exception {
        // : sugar syntax to thing "
        instruction = instruction.replace(" :", " thing \"");
        instruction = instruction.replace("["," [");
        instruction = instruction.trim();

        Vector<String> instArray = uu.splitInstruction(instruction);

//        argCount.clear();
//        dataStack.clear();
        for (String inst : instArray) {
            if (uu.isOperation(inst)) {
                Integer x = argNumber.get(inst);
                argCount.push(x);
                argN.push(x);
                dataStack.push(new Data(inst));
            } else if (uu.isFunction(inst)) {
                Integer x = funcArgNumber(inst);
                argCount.push(x);
                argN.push(x);
                dataStack.push(new Data(inst));
            } else {
                Data tmp;
                if(inst.charAt(0)=='(') tmp = new Data(runExpression(inst));
                else tmp = new Data(inst);
                if (!argCount.empty()) {
                    Integer top = argCount.pop() - 1;
                    argCount.push(top);
                    dataStack.push(tmp);
                }
            }
            while (!argCount.empty() && argCount.peek() == 0) {
                argCount.pop();
                Integer top = argCount.empty() ? -1 : argCount.pop();
                int rets = runOperation(dataStack, argN.pop());
                if (rets < 0)
                    return;
                top = top - rets;
                if (top >= 0)
                    argCount.push(top);
            }
        }
    }

    public int funcArgNumber(String funcName) throws Exception{
        String str = nowNameSpace.get(funcName).toString();
        Vector<String> tmp;
        tmp = uu.splitInstruction(str.substring(1, str.length() - 1));
        str = tmp.get(0);
        tmp = uu.splitInstruction(str.substring(1, str.length() - 1));
        return tmp.size();
    }

    public int runFunction(Stack<Data> dataStack, Vector<Data> arg) throws Exception {
        Data funcName, funcBody;
        String func = nowNameSpace.get(arg.lastElement().toString()).getList();
        arg.remove(arg.size() - 1);
        Vector<String> funTmp = uu.splitInstruction(func);

        funcName = new Data(funTmp.get(0));
        funcBody = new Data(funTmp.get(1));
        // init namespace
        NameSpace funcNameSpace = new NameSpace(nowNameSpace.fatherName);
        funTmp = uu.splitInstruction(funcName.getList());

        for (String i : funTmp) {
            funcNameSpace.insert(i, arg.lastElement());
            arg.remove(arg.size() - 1);
        }
        Operation funcOperation = new Operation(funcNameSpace, input);
        funcOperation.outputTmp = null;
        funcOperation.runInstruction(funcBody.getList());
        if (funcOperation.outputTmp != null) {
            dataStack.push(funcOperation.outputTmp);
            return 1;
        } else
            return 0;

    }

    public Integer runOperation(Stack<Data> dataStack, int argN) throws Exception {
        Vector<Data> tmp = new Vector<Data>();
        while (argN >= 0) {
            argN -= 1;
            tmp.add(dataStack.pop());
        }
        switch (tmp.lastElement().getOpt()) {
            case "make":
                make(tmp.elementAt(1), tmp.elementAt(0));
                return 0;
            case "thing":
            case ":":
                dataStack.push(thing(tmp.elementAt(0)));
                return 1;
            case "erase":
                erase(tmp.elementAt(0));
                return 0;
            case "isname":
                dataStack.push(isname(tmp.elementAt(0)));
                return 1;
            case "print":
                print(tmp.elementAt(0));
                return 0;
            case "read":
                dataStack.push(read());
                return 1;
            case "readlist":
                dataStack.push(readlist());
                return 1;
            case "add":
            case "sub":
            case "mul":
            case "div":
            case "mod":
                dataStack.push(calculateOpt(tmp.elementAt(2).getOpt(), tmp.elementAt(1), tmp.elementAt(0)));
                return 1;
            case "eq":
            case "gt":
            case "lt":
                dataStack.push(compareOpt(tmp.elementAt(2).getOpt(), tmp.elementAt(1), tmp.elementAt(0)));
                return 1;
            case "and":
            case "or":
                dataStack.push(logicOpt(tmp.elementAt(2).getOpt(), tmp.elementAt(1), tmp.elementAt(0)));
                return 1;
            case "not":
                dataStack.push(not(tmp.elementAt(0)));
                return 1;
            case "output":
                outputTmp = tmp.elementAt(0);
                return 0;
            case "stop":
                return -1;
            case "export":
                nowNameSpace.export(tmp.elementAt(0));
                return 0;
            case "if":
                ifOpt(tmp.elementAt(2).getBool(), tmp.elementAt(1), tmp.elementAt(0));
                return 0;
            case "isnumber":
            case "isword":
            case "islist":
            case "isbool":
            case "isempty":
                dataStack.push(typeOpt(tmp.elementAt(1).getOpt(), tmp.elementAt(0)));
                return 1;
            case "repeat":
                repeatOpt(tmp.elementAt(1), tmp.elementAt(0));
                return 0;
            case "word":
                dataStack.push(wordOpt(tmp.elementAt(1),tmp.elementAt(0)));
                return 1;
            case "sentence":
                dataStack.push(sentOpt(tmp.elementAt(1),tmp.elementAt(0)));
                return 1;
            case "list":
                dataStack.push(listOpt(tmp.elementAt(1),tmp.elementAt(0)));
                return 1;
            case "join":
                dataStack.push(joinOpt(tmp.elementAt(1),tmp.elementAt(0)));
                return 1;
            case "first":
                dataStack.push(firstOpt(tmp.elementAt(0)));
                return 1;
            case "last":
                dataStack.push(lastOpt(tmp.elementAt(0)));
                return 1;
            case "butfirst":
                dataStack.push(butfirstOpt(tmp.elementAt(0)));
                return 1;
            case "butlast":
                dataStack.push(butlastOpt(tmp.elementAt(0)));
                return 1;
            case "random":
                dataStack.push(randomOpt(tmp.elementAt(0)));
                return 1;
            case "floor":
                dataStack.push(floorOpt(tmp.elementAt(0)));
                return 1;
            case "sqrt":
                dataStack.push(sqrtOpt(tmp.elementAt(0)));
                return 1;
            case "wait":
                waitOpt(tmp.elementAt(0));
                return 0;
            case "save":
                saveFile(tmp.elementAt(0));
                return 0;
            case "load":
                loadFile(tmp.elementAt(0));
                return 0;
            case "erall":
                nowNameSpace.eraseAllNames();
                return 0;
            case "poall":
                nowNameSpace.printAllNames();
                return 0;
            default:
                return runFunction(dataStack, tmp);
        }
    }

    public void make(Data name, Data value) {
        nowNameSpace.insert(name.getWord(), value);
    }

    public Data thing(Data name) {
        return nowNameSpace.get(name.getWord());
    }

    public void erase(Data name) {
        nowNameSpace.erase(name.getWord());
    }

    public Data isname(Data name) {
        if (nowNameSpace.hasName(name.getWord())) {
            return new Data("true");
        } else {
            return new Data("false");
        }
    }

    public void print(Data info) {
        if(info.isList())
            System.out.println(info.getList());
        else
            System.out.println(info.toString());
    }

    public Data read() {
        return new Data(input.getWord());
    }

    public Data readlist() {
        return new Data("[" + input.getLine() + "]");
    }

    public Data calculateOpt(String operation, Data number1, Data number2) {

        double num1 = number1.getNumber();
        double num2 = number2.getNumber();

        switch (operation) {
            case "add":
                return new Data(num1 + num2);
            case "sub":
                return new Data(num1 - num2);
            case "mul":
                return new Data(num1 * num2);
            case "div":
                return new Data(num1 / num2);
            case "mod":
                return new Data(num1 % num2);
        }

        return new Data(0);
    }

    public Data compareOpt(String operation, Data value1, Data value2) {
        if (value1.isNumber() && value2.isNumber()) {
            switch (operation) {
                case "eq":
                    return new Data(value1.getNumber() == value2.getNumber());
                case "gt":
                    return new Data(value1.getNumber() > value2.getNumber());
                case "lt":
                    return new Data(value1.getNumber() < value2.getNumber());
            }
        } else {
            switch (operation) {
                case "eq":
                    return new Data(value1.getWord().equals(value2.toString()));
                case "gt":
                    return new Data(value1.getWord().compareTo(value2.getWord()) > 0);
                case "lt":
                    return new Data(value1.getWord().compareTo(value2.getWord()) < 0);
            }
        }
        return new Data(true);
    }

    public Data logicOpt(String operation, Data value1, Data value2) {
        boolean number1 = value1.getBool();
        boolean number2 = value2.getBool();

        switch (operation) {
            case "and":
                return new Data(number1 && number2);
            case "or":
                return new Data(number1 || number2);
        }
        return new Data(true);
    }

    public Data not(Data value) {
        return value.getBool() ? new Data("false") : new Data("true");
    }

    public void ifOpt(boolean o, Data list1, Data list2) throws Exception {
        String inst = o ? list1.getList() : list2.getList();
        runInstruction(inst);
        return;
    }

    public Data typeOpt(String opt, Data value) {
        switch (opt) {
            case "isword":
                return new Data(value.isWord());
            case "isnumber":
                return new Data(value.isNumber());
            case "islist":
                return new Data(value.isList());
            case "isbool":
                return new Data(value.isBool());
            case "isempty":
                if (value.isWord())
                    return new Data(value.getWord().equals(""));
                else if (value.isList())
                    return new Data(value.getList().equals(""));
        }
        return new Data(value.isWord());
    }

    public void repeatOpt(Data number, Data list) throws Exception {
        int n = Integer.valueOf(number.toString());
        for(int i=0; i<n; ++i){
            runInstruction(list.getList());
        }
    }

    Double runExpression(String exp) throws Exception {
        exp = exp.replace(":","thing \"");
        exp.trim();
        exp = exp.substring(1,exp.length()-1);

        Stack<Double> numberStack = new Stack<>();
        Stack<Character> oprationStack = new Stack<>();

        int len = uu.getCalculateExpressionSplit(exp);
        numberStack.push(getCalculateExpressionValue(exp.substring(0,len)));
        exp = exp.substring(len).trim();
        while(!exp.isEmpty()){
            oprationStack.push(exp.charAt(0));
            exp = exp.substring(1).trim();
            len = uu.getCalculateExpressionSplit(exp);
            numberStack.push(getCalculateExpressionValue(exp.substring(0,len)));
            exp = exp.substring(len).trim();
            if(oprationStack.peek().equals('*')) {
                Double b = numberStack.pop();
                Double a = numberStack.pop();
                numberStack.push(a * b);
                oprationStack.pop();
            } else if(oprationStack.peek().equals('/')){
                Double b = numberStack.pop();
                Double a = numberStack.pop();
                numberStack.push(a / b);
                oprationStack.pop();
            }else if(oprationStack.peek().equals('%')){
                Double b = numberStack.pop();
                Double a = numberStack.pop();
                numberStack.push(a % b);
                oprationStack.pop();
            }
        }
        double ans=0;
        while(!oprationStack.empty()){
            if(oprationStack.pop().equals('+'))
                ans += numberStack.pop();
            else ans += numberStack.pop()*-1;
        }
        ans += numberStack.pop();
        return ans;
    }

    Double getCalculateExpressionValue(String exp) throws Exception {
        exp = exp.trim();
        Double res = (double) 0;
        Boolean fushuFlag = false;
        if(exp.charAt(0)=='-'){
            fushuFlag = true;
            exp = exp.substring(1);
        }
        if(exp.charAt(0)<='9'&&exp.charAt(0)>='0'){
            for(int i=0; i<exp.length(); ++i)
                res = res*10+exp.charAt(i)-'0';
            if(fushuFlag){
                return 0-res;
            }else {
                return res;
            }
        }
        if(exp.charAt(0) == '('){
            return runExpression(exp);
        }
        Operation expOpration = new Operation(nowNameSpace,input);
        expOpration.runInstruction(exp);
        res = expOpration.dataStack.pop().getNumber();
        return res;
    }

    Data wordOpt(Data w1, Data w2) {
        return new Data(w1.getWord()+w2.toString());
    }

    Data sentOpt(Data v1, Data v2){
        String s1 = v1.toString().trim();
        String s2 = v2.toString().trim();
        if(s1.charAt(0)=='[') s1 = s1.substring(1,s1.length()-1);
        if(s2.charAt(0)=='[') s2 = s2.substring(1,s2.length()-1);
        return new Data("[" + s1 + " " + s2 + "]");
    }

    Data listOpt(Data v1, Data v2){
        return new Data("[" + v1.toString() + " " + v2.toString() + "]");
    }

    Data joinOpt(Data v1, Data v2) {
        String listValue = v1.getList().trim();
        return new Data("[" + listValue.trim() + " " + v2.toString().trim() + "]");
    }

    Data firstOpt(Data value) {
        if(value.isList()){
            String str = value.getList();
            int i=1;
            while(str.length()>i&&!(str.charAt(i)==' '&&uu.judgeBrackets(str.substring(0,i+1))))
                i++;
            return new Data(str.substring(0,i));
        }
        else return new Data(value.getWord().substring(0,1));
    }

    Data lastOpt(Data value) {
        if(value.isList()){
            String str = value.getList();
            int i=str.length()-1;
            while(i>=0&&!(str.charAt(i)==' '&&uu.judgeBrackets(str.substring(i))))
                i--;
            return new Data(str.substring(i));
        }
        else return new Data(value.getWord().substring(value.getWord().length()-1));
    }

    Data butfirstOpt(Data value) {
        if(value.isList()){
            String str = value.getList();
            str = str.trim();
            int i=0;
            while(str.length()>i&&!(str.charAt(i)==' '&&uu.judgeBrackets(str.substring(0,i+1))))
                i++;
            return new Data("[" + str.substring(i) + "]");
        }
        else return new Data(value.getWord().substring(1));
    }

    Data butlastOpt(Data value) {
        if(value.isList()){
            String str = value.getList();
            int i=str.length()-1;
            while(i>=0&&!(str.charAt(i)==' '&&uu.judgeBrackets(str.substring(i))))
                i--;
            return new Data("[" + str.substring(0,i) + "]");
        }
        else return new Data(value.getWord().substring(0,value.getWord().length()-1));
    }

    Data randomOpt(Data num) {
        Double range = num.getNumber();
        Double res = Math.random()*range;
        return new Data(res);
    }
    Data floorOpt(Data num) {
        return new Data(Math.floor(num.getNumber()));
    }
    Data sqrtOpt(Data num) {
        return new Data(Math.sqrt(num.getNumber()));
    }
    void waitOpt(Data num) throws InterruptedException {
        wait((long)num.getNumber());
    }
    void saveFile(Data name) {
        String fileName = name.getWord();
        File f = new File("fileName");
        try{
            if(!f.exists())
                f.createNewFile();
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            for(HashMap.Entry<String, Data> entry : nowNameSpace.nameMap.entrySet()){
                bw.write("make \""+entry.getKey()+" "+entry.getValue().toString()+"\n");
            }
            bw.close();
            fw.close();
        }catch ( IOException e){
            e.printStackTrace();
        }
    }
    void loadFile(Data name) {
        String fileName = name.getWord();
        File f = new File("fileName");
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while(true){
                String line = br.readLine();
                if(line == null){
                    break;
                }
                runInstruction(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
