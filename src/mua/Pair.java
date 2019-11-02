package mua;

// 为了使用类似stl中的pair自定义的类2333
// 只能使用<String, Integer>形式，没有做泛化

public class Pair {
    public String first;
    public Integer second;
    public Pair(String str, Integer i) {
        this.first = str;
        this.second = i;
    }
    public Pair() {
        this.first= "";
        this.second=0;
    }

}
