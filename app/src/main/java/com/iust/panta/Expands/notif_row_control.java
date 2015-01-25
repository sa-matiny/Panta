package com.iust.panta.Expands;

/**
 * Created by Rayehe on 1/21/2015.
 */
public class notif_row_control {


    public notif_row_control(){}
    private String Title;
    private String short_exp;
    private int ID;
    public String getTitle() {

        return Title;
    }

    public void setTitle(String Name) {

        this.Title = Name;

    }

    public void setID(int ID ) {

        this.ID =ID;

    }
    public String getShort_exp () {

        return short_exp;
    }
    public int getID() {

        return ID;

    }
    public void setShort_exp(String short_exp) {
        this.short_exp = short_exp;
    }
}
