package com.uninorte.andresarguelles.dynamicprocesses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andres Arguelles on 28/05/2015.
 */
public class Step {
    public int step_id;
    public int procedure_id;
    //public String content;
    public String infoUrl;

    public JSONArray jfields;
    public JSONArray jdecisions;

    public ArrayList<StepField> fields;
    public ArrayList<StepDecision> decisions;


    public Step(int step_id, int procedure_id, /*String content,*/ String infoURL, JSONArray jfields, JSONArray jdecisions/*, int typeOfField*/) {
        this.step_id = step_id;
        this.procedure_id = procedure_id;
        //this.content = content;
        this.infoUrl = infoURL;

        this.jfields = jfields;
        this.jdecisions = jdecisions;
        //this.typeOfField = typeOfField;

        // Recorro el array de Fields, buscando cada caption, field_type y valores opcionales (Posible values)
        fields = new ArrayList<StepField>();
        for (int i = 0; i< jfields.length(); i++ ){
           try {
               JSONObject tmpField = jfields.getJSONObject(i);
               int ToF = tmpField.getInt("field_type");
               String caption =tmpField.getString("caption");

               // Declarar posible values excepto cuando field_type sea 3 = "Label"
               String[] possiblevalues = {};
               if(ToF != 3){
                   JSONArray jpossiblevalues = tmpField.getJSONArray("possible_values");

                   possiblevalues = new String[jpossiblevalues.length()];
                   for (int j = 0; j < jpossiblevalues.length(); j++){
                       possiblevalues[j]= jpossiblevalues.getString(j);
                   }
               }
               fields.add(new StepField(ToF,caption, possiblevalues));
           }catch (JSONException e){
               e.printStackTrace();
           }
        }

        //Instancio y recorro el array de decisiones, obteniendo el go_to_step y sus branches, si los hay
        decisions = new ArrayList<StepDecision>();
        for (int i = 0; i < jdecisions.length(); i++){
            try{
                JSONObject tmpDecision = jdecisions.getJSONObject(i);
                int nextStep = tmpDecision.getInt("go_to_step");
                ArrayList<Branch> branches = new ArrayList<Branch>();
                JSONArray jbranches = tmpDecision.getJSONArray("branch");
                for (int j = 0; j < jbranches.length(); j++){
                    JSONObject jbranch = jbranches.getJSONObject(j);
                    int field_id = jbranch.getInt("field_id"); // Asocia con el field correspondiente al branch

                    String comparison_type = jbranch.getString("comparison_type"); // Este campo necesita un switch más adelante
                    COMPARISION comparision;
                    switch (comparison_type){
                        case "<": comparision = COMPARISION.LESS;
                            break;
                        case ">": comparision = COMPARISION.GREATER;
                            break;
                        case "=":   default:
                            comparision = COMPARISION.EQUAL;
                            break;
                    }
                    String value = jbranch.getString("value");

                    branches.add(new Branch(field_id,comparision, value));
                }
                decisions.add(new StepDecision(nextStep, branches));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }


    class StepField {
        /**
         * field_type
         * 0 = Seleccion multiple.
         * 1 = Boolean
         * 2 = Numerico <,> e =
         * 3 = Label
         */


        public int typeOfField; //
        public String caption;
        public final String[] possible_values;

        public StepField(int typeOfField, String caption, String[] possible_values) {
            this.typeOfField = typeOfField;
            this.caption = caption;
            this.possible_values = possible_values;
        }
    }

    public class StepDecision {

        int go_to_step;
        ArrayList<Branch> branches;

        public StepDecision(int go_to_step, ArrayList<Branch> branches){
            this.go_to_step = go_to_step;
            this.branches = branches;
        }


    }
    public class Branch {
        public int field_id;
        public COMPARISION comparision_type;
        public String Value;
        public Branch(int field_id, COMPARISION comparision_type, String value) {
            this.field_id = field_id;
            this.comparision_type = comparision_type;
            Value = value;
        }
    }
    public enum COMPARISION {
        GREATER, LESS, EQUAL
    }


}

