package mua;

import java.util.HashMap;

// store the variables
public class NameSpace {
    // variables map
    public HashMap<String, Data> nameMap = new HashMap<>();
    // father namespace
    NameSpace fatherName;

    NameSpace(NameSpace father) {
        fatherName = father;
        if(father == null) {
            fatherName = this;
        }
    }
    NameSpace() {
        fatherName = this;
        nameMap.put("pi",new Data(3.14159));
        nameMap.put("run",new Data("[ [x] [if true :x [] ] ]"));
    }

    public boolean hasName(String name) {
        // search local variables
        if (nameMap.containsKey(name)) {
            return true;
        }
        // if no father then game over
        if (fatherName == this) {
            return false;
        }
        // search the father namespace then
        return fatherName.hasName(name);
    }

    public void insert(String name, Data value) {
        nameMap.put(name, value);
    }

    public void erase(String name) {
        nameMap.remove(name);
    }

    public Data get(String name) {
        // similarily with hasName()
        if (nameMap.containsKey(name)) {
            return nameMap.get(name);
        }
        if (fatherName == this) {
            return null;
        }
        return fatherName.get(name);
    }

//    public void export() {
//        if (fatherName != this) {
//            fatherName.nameMap.putAll(this.nameMap);
//        }
//    }

    public void export(Data oneData) {
        if (fatherName != this) {
            String name = oneData.getWord();
            fatherName.nameMap.put(name, nameMap.get(name));
        }
    }

    public void eraseAllNames(){
        nameMap.clear();
    }
    public void printAllNames(){
        for(String s: nameMap.keySet()){
            System.out.println(s);
        }
    }
}
