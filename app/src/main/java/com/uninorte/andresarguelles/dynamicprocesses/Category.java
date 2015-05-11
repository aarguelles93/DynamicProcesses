package com.uninorte.andresarguelles.dynamicprocesses;

/**
 * Created by Andres Arguelles on 11/05/2015.
 */
public class Category {
    public int id;
    public String group_id;
    public String name;
    public String nextUrl;

    public Category(int id, String group_id, String name, String nextUrl) {
        this.id = id;
        this.group_id = group_id;
        this.name = name;
        this.nextUrl = nextUrl;
    }
}
