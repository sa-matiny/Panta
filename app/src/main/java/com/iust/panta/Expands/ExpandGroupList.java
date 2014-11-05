package com.iust.panta.Expands;
import java.lang.String;
import java.util.ArrayList;

public class ExpandGroupList {
    private String name;
    private ArrayList<ExpandChildList> Itemes;

    public String getName(){
        return name;
    }
    public void SetName(String name){
        this.name=name;
    }
    public ArrayList<ExpandChildList> getItemes(){
        return  Itemes;
    }
    public void setItemes(ArrayList<ExpandChildList> Items){
        this.Itemes=Items;
    }

}