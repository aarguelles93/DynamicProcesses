package com.uninorte.andresarguelles.dynamicprocesses;

/**
 * Created by Andres Arguelles on 11/05/2015.
 */
public class Category {

    public int group_id;
    public String name;
    public String infoUrl;

    public Category(int group_id, String name, String nextUrl) {

        this.group_id = group_id;
        this.name = name;
        this.infoUrl = nextUrl;
    }
}
