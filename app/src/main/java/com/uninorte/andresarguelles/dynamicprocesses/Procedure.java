package com.uninorte.andresarguelles.dynamicprocesses;

/**
 * Created by Andres Arguelles on 26/05/2015.
 */
public class Procedure {

    public int procedure_id;
    public int group_id;
    public String name;
    public String description;
    public String infoURL;

    public Procedure (int procedure_id, int group_id, String name, String description, String infoURL){

        this.procedure_id = procedure_id;
        this.group_id = group_id;
        this.name = name;
        this.description = description;
        this.infoURL = infoURL;
    }
}