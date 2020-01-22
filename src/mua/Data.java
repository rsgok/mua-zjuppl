package mua;

public class Data {
    private String value;

    public Data(Data inputData) {
        this.value = inputData.value;
    }

    public Data(String value) {
        this.value = value;
        if (value.charAt(0) == '"') {
            this.value = this.value.substring(1);
        }
    }

    public Data(double value) {
        this.value = Double.toString(value);
    }

    public Data(boolean value) {
        this.value = value ? "true" : "false";
    }

    // type judge
    public boolean isWord() {
        return value.charAt(0) == '\"';
    }

    public boolean isNumber() {
        try {
            Double.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBool() {
        if (value.equals("true") || value.equals("false"))
            return true;
        else
            return false;
    }

    public boolean isList() {
        value = value.trim();
        return value.charAt(0) == '[' && value.charAt(value.length() - 1) == ']';
    }

    public String getWord() {
        return value;
    }

    public double getNumber() {
        return Double.valueOf(value);
    }

    // get value
    public String toString() {
        return value;
    }

    public boolean getBool() {
        return value.equals("true");
    }

    public String getOpt() {
        return value;
    }

    public String getList() {
        return this.value.substring(1, value.length() - 1);
    }
}
