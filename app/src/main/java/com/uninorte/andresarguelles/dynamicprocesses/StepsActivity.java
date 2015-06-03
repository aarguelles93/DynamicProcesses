package com.uninorte.andresarguelles.dynamicprocesses;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;


public class StepsActivity extends ActionBarActivity implements Handler.Callback {

    // StepsCollectionPagerAdapter mStepsCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    // ViewPager mViewPager;

    // Custom variables

    public ArrayList<Step> steps;

    String urlSteps;

    int selectedProcedure_id;

    int currentStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        //Obteniendo por intent los datos de la categoria padre
        Intent intent = getIntent();
        selectedProcedure_id = intent.getIntExtra("procedure_id", -1);
        //

        //Consultando los steps del procedure actual
        urlSteps = "https://dynamicformapi.herokuapp.com/steps/by_procedure/"+selectedProcedure_id+".json";

        steps = new ArrayList<Step>();




        HandleJSON jhandler = new HandleJSON(urlSteps);
        jhandler.fetchJSON();

        while (!jhandler.parsingComplete);
        for (int i = 0; i < jhandler.getId().size(); i++){
            //Step step = new Step(jhandler.getStep_id().get(i), jhandler.getProcedure_id().get(i), jhandler.getContent().get(i),jhandler.getInfoURLArray().get(i));
            Step step = new Step(jhandler.getStep_id().get(i), jhandler.getProcedure_id().get(i), jhandler.getInfoURLArray().get(i), jhandler.getFields().get(i), jhandler.getDecisions().get(i));
            steps.add(step);
        }

        StepFragment stfg = (StepFragment) StepFragment.instantiate(getApplicationContext(), StepFragment.class.getName(), null);

        // El proceso tiene al menos 1 step (No está vacío)
        if (steps.size()>0){
            stfg.step = steps.get(0);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_main_container, stfg, null)
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Ups... Parece que este procedimiento aún no tiene pasos definidos.", Toast.LENGTH_LONG);
            toast.show();
            this.finish();
        }



    }

    public void addStep(int step_id){
        Bundle args = new Bundle();
        args.putInt("step_id", step_id);

        StepFragment stfg = (StepFragment) StepFragment.instantiate(getApplicationContext(), StepFragment.class.getName(), args);
        //stfg.step = steps.get(currentStep);

        for(int i = 0; i < steps.size(); i++){
            if(steps.get(i).step_id == step_id){
                stfg.step = steps.get(i);
                break;
            }
        }
        getSupportFragmentManager().beginTransaction()
                //.add(R.id.step_main_container, stfg, null)
                .replace(R.id.step_main_container,stfg, null)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_steps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}

