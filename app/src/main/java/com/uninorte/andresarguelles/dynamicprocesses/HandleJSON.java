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
    int typeOfQuery;

    private ArrayList<Integer> id; // Este es obligatorio en todos los casos

    private ArrayList<String> group_id;
    private ArrayList<String> procedure_id;
    private ArrayList<String> step_id;
    private ArrayList<String> name;
    private ArrayList<String> generalInfoTitle;
    private ArrayList<String> generalInfo;

    private ArrayList<String> urlNext; // Este es obligatorio en todos los casos

    public volatile boolean parsingComplete = false;

    public String urlAPI;

    /**
     *   Constructor
     */
    public HandleJSON(String urlAPI,int ToQ){
        this.urlAPI = urlAPI;
        this.typeOfQuery =ToQ;

        id = new ArrayList<Integer>();

        group_id = new ArrayList<String>();
        name = new ArrayList<String>();
        generalInfoTitle = new ArrayList<String>();
        generalInfo = new ArrayList<String>();


        urlNext = new ArrayList<String>();
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

    public ArrayList<String> getGroup_id() {
        return group_id;
    }

    public ArrayList<String> getUrlNext() {
        return urlNext;
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
                urlNext.add(item.getString("url"));
                // Category Query
                if (!item.has("procedure_id")){
                    group_id.add(item.getString("group_id"));
                    name.add(item.getString("name"));
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
