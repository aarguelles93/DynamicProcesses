package com.uninorte.andresarguelles.dynamicprocesses;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class StepFragment extends android.support.v4.app.Fragment {

    public Step step;

    /*
     * Visual Variables
     */
    ArrayList<Spinner> spinnersDecisionArray;
    ArrayList<String> valuesSelectedStringArray;
    //

    public static StepFragment newInstance(String param1, String param2) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step, null);
        rootView.setBackgroundColor(Color.parseColor("#f0f0f0"));

        final LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.stepsLayout);

        spinnersDecisionArray = new ArrayList<Spinner>();
        valuesSelectedStringArray = new ArrayList<String>();


        // Contenido a vistas
        // Recorro los campos del step y obtengo sy type of field
        for (int i = 0; i < step.fields.size(); i++)
        {
            // Mostrar caption (Todos los steps tienen caption)
            View step_field_mchoisesView = inflater.inflate(R.layout.step_field_mchoises,null);
            stepsLayout.addView(step_field_mchoisesView);

            TextView mTextViewField = (TextView)step_field_mchoisesView.findViewById(R.id.mTextViewCaption);
            mTextViewField.setText(step.fields.get(i).caption);


            int ToF = step.fields.get(i).typeOfField;
            // Si TypeOfField es 0 = Opcion multiple, poblar el Spinner con las posibles opciones
            if (ToF == 0 || ToF == 1){
                Spinner mSpinnerPossibleValues = (Spinner)step_field_mchoisesView.findViewById(R.id.mSpinnerPossibleValues);
                String[] possValstmp= step.fields.get(i).possible_values;
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, possValstmp );
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerPossibleValues.setAdapter(spinnerAdapter);
                spinnersDecisionArray.add(mSpinnerPossibleValues);
                mSpinnerPossibleValues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        int spinnerIndex = spinnersDecisionArray.indexOf(parentView);
                        if (valuesSelectedStringArray.size()==spinnerIndex){
                            valuesSelectedStringArray.add(spinnerIndex, parentView.getItemAtPosition(position).toString());
                        }else{
                            valuesSelectedStringArray.set(spinnerIndex, parentView.getItemAtPosition(position).toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            }
        }

        Button mButtonNextStep = (Button) rootView.findViewById(R.id.mButtonNextStep);
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextStep = -1;
                //Recorro todas las decisiones

                for (int i = 0 ; i < step.decisions.size(); i++){
                    Step.StepDecision tmpDecision = step.decisions.get(i);

                    // Si ToF es 3 ó Label:
                    if (tmpDecision.branches.size()==0){
                        nextStep = tmpDecision.go_to_step;
                    }

                    // Si ToF es cualquier otro diferente de 3:
                    Boolean foundDecision = false; // Booleano que me define cual será el step correcto más adelante
                    // Recorro todos los branch de la decision que estoy chequeando
                    for (int j = 0; j < tmpDecision.branches.size(); j++){
                        Step.Branch tmpBranch = tmpDecision.branches.get(j);
                        // Si el tipo de comparación es igual, y
                        if(tmpBranch.comparision_type == Step.COMPARISION.EQUAL){
                            if (tmpBranch.Value.compareTo(valuesSelectedStringArray.get(j))==0 ){
                                foundDecision = true;
                            }else{
                                foundDecision = false;
                            }
                        }
                    }
                    if (foundDecision){
                        nextStep = tmpDecision.go_to_step;
                        break;
                    }

                }
                // Navegar al sgte Step. Si es -1, terminar la actividad y el fragment
                StepsActivity stpactTMP = (StepsActivity) getActivity();
                if (nextStep != -1) {
                    stpactTMP.addStep(nextStep);
                }else{
                    stpactTMP.finish();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (Handler.Callback)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }



    private Handler.Callback callback;
}
