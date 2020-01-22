package mua;

import java.util.Scanner;
import java.util.Stack;

public class InputCtrl {
    Scanner in = new Scanner(System.in);

    public boolean hasNext() {
        return in.hasNext();
    }

    public String getLine() {
        String resStr = new String();
        // for bracket check
        Stack<Character> brackets = new Stack<>();
        while (in.hasNext()) {
            String tempStr = in.nextLine();
            // check for commet sentence and substract from it
            if (tempStr.indexOf("//") >= 0) {
                tempStr = tempStr.substring(0, tempStr.indexOf("//"));
            }
            resStr += tempStr + ' ';
            // check the bracket
            for (int i = 0; i < tempStr.length(); i++) {
                char ch = tempStr.charAt(i);
                switch (ch) {
                    case '[':
                    case '(':
                        brackets.push(ch);
                        break;
                    case ']':
                    case ')':
                        brackets.pop();
                }
            }
            // only bracket check over can break
            if (brackets.empty()) {
                break;
            }
        }
        return resStr.trim();
    }

    public String getWord() {
        String resStr = new String();
        Stack<Character> brackets = new Stack<Character>();
        while (in.hasNext()) {
            String tempStr = in.next();
            resStr += tempStr + ' ';
            for (int i = 0; i < tempStr.length(); i++) {
                char ch = tempStr.charAt(i);
                switch (ch) {
                    case '[':
                    case '(':
                        brackets.push(ch);
                        break;
                    case ']':
                    case ')':
                        brackets.pop();
                }
            }
            if (brackets.empty()) {
                break;
            }
        }
        return resStr.trim();
    }
}
