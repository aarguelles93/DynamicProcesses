package com.uninorte.andresarguelles.dynamicprocesses;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andres Arguelles on 10/05/2015.
 */
public class HandleJSON {

    /**
    * typeOfQuery es un id que depende del tipo de consulta que se realizará
    * 1 = obtener todas las categorias
    * 2 = obtiene todos los procesos
    * 3 = obtiene todos los pasos
    */


    private ArrayList<Integer> id; // Este es obligatorio en todos los casos

    private ArrayList<Integer> group_id;
    private ArrayList<Integer> procedure_id;
    private ArrayList<Integer> step_id;
    private ArrayList<String> name;


    private ArrayList<String> description;

    //private ArrayList<String> content;
    private ArrayList<JSONArray> fields;
    private ArrayList<JSONArray> decisions;
    private ArrayList<Integer> typeOfField;

    private ArrayList<String> infoURLArray; // Este es obligatorio en todos los casos

    public volatile boolean parsingComplete = false;

    public String urlAPI;

    /**
     *   Constructor
     */
    public HandleJSON(String urlAPI){
        this.urlAPI = urlAPI;


        id = new ArrayList<Integer>();

        group_id = new ArrayList<Integer>();
        name = new ArrayList<String>();

        procedure_id = new ArrayList<Integer>();
        description = new ArrayList<String>();

        step_id = new ArrayList<Integer>();
        //content = new ArrayList<String>();
        fields = new ArrayList<JSONArray>();
        decisions = new ArrayList<JSONArray>();
        typeOfField = new ArrayList<Integer>();

        infoURLArray = new ArrayList<String>();
    }



    /**
     *
     * Getters
     */
    public ArrayList<Integer> getId() {
        return id;
    }
    public ArrayList<String> getName() {
        return name;
    }

    public ArrayList<Integer> getGroup_id() {
        return group_id;
    }

    public ArrayList<Integer> getProcedure_id() {
        return procedure_id;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public ArrayList<Integer> getStep_id() {
        return step_id;
    }

    /*public ArrayList<String> getContent() {
        return content;
    }*/

    public ArrayList<JSONArray> getFields() {
        return fields;
    }

    public ArrayList<JSONArray> getDecisions() {
        return decisions;
    }

    public ArrayList<Integer> getTypeOfField() {
        return typeOfField;
    }

    public ArrayList<String> getInfoURLArray() {
        return infoURLArray;
    }


    /**
     * METHODS
     */

    public void fetchJSON (){
        Thread thread = new Thread (new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL (urlAPI);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);

                    readAndParseJSONCurrent(data);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @SuppressLint("NewApi")
    public void readAndParseJSONCurrent(String in) {
        parsingComplete = false;
        try{
            //JSONObject reader = new JSONObject(in);
            JSONArray reader = new JSONArray(in);
            int tam = reader.length();
            JSONObject item;
            for (int i=0; i<tam; i++){
                item = reader.getJSONObject(i);
                id.add(item.getInt("id"));
                infoURLArray.add(item.getString("url"));
                // Category Query
                if (!item.has("procedure_id")){
                    group_id.add(item.getInt("group_id"));
                    name.add(item.getString("name"));
                }//Procedures Query
                if (item.has("procedure_id") && item.has("group_id")){
                    group_id.add(item.getInt("group_id"));
                    procedure_id.add(item.getInt("procedure_id"));
                    name.add(item.getString("name"));
                    description.add(item.getString("description"));
                }
                //Steps query
                if (item.has("step_id")){
                    step_id.add(item.getInt("step_id"));
                    procedure_id.add(item.getInt("procedure_id"));
                    //content.add(item.getString("content"));
                    JSONObject jContent = new JSONObject(item.getString("content"));
                    JSONArray jFields = jContent.getJSONArray("Fields");
                    fields.add(jFields);
                    JSONObject field = jFields.getJSONObject(0);
                    typeOfField.add(field.getInt("field_type"));
                    JSONArray jDecisions = jContent.getJSONArray("Decisions");
                    decisions.add(jDecisions);
                }
            }

            parsingComplete = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
