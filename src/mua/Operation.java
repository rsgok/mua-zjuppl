package mua;

public class Operation {
    public void doPrint(String str)
    {
        if(isNumber(str)){
            Double temp = Double.parseDouble(str);
            System.out.println(temp);
        }
        else System.out.println(str);
    }
    boolean isNumber(String str) {
        try
        {
            Double t = Double.parseDouble(str);
        }
        catch(NumberFormatException e)
        {
            //not a double
            return false;
        }
        return true;
    }
}
