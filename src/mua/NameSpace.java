package mua;

import java.util.HashMap;
import java.util.Map;

public class NameSpace {
    Map<String, String> myNameSpace = new HashMap<>();
    public void put(String s1, String s2) {
        myNameSpace.put(s1, s2);
    }
    public void remove(String s) {
        myNameSpace.remove(s);
    }
    public boolean containsKey(String s) {
        return myNameSpace.containsKey(s);
    }
    public String get(String s) {
        return myNameSpace.get(s);
    }
    public Map<String, String> getNameSpace() {
        return myNameSpace;
    }
}
