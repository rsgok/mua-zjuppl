package mua;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Interpreter ip = new Interpreter();
        while(in.hasNext())
        {
            String str = (String)in.next();
            if(str.startsWith("//"))
            {
                str = in.nextLine();
                continue;
            }
            ip.go(str);
        }
    }
}
