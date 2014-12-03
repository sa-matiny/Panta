package com.iust.panta.Expands;

import java.io.Serializable;
import java.util.ArrayList;


// Please read ,this file is for passing complex data Types

/**
 * Created by nastsan on 12/3/2014.
 */
public class DataHelper implements Serializable {

    private ArrayList<ArrayList<String>> floors;

    public DataHelper(ArrayList<ArrayList<String>> floors)
    {
        this.floors = floors;
    }

    public ArrayList<ArrayList<String>> getList()
    {
        return this.floors;
    }
}
