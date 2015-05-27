package com.uninorte.andresarguelles.dynamicprocesses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ProceduresActivity extends ActionBarActivity {

    ListView mListViewProcedures;

    Category parentCategory;

    String urlProcedures;

    ArrayList<Procedure> procedures;

    int selectedProcedureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);

        mListViewProcedures = (ListView)findViewById(R.id.mListViewProcedures);

        //Obteniendo por intent los datos de la categoria padre
        Intent intent = getIntent();
        int category_id = intent.getIntExtra("category_id",-1);
        String category_name = intent.getStringExtra("name");
        String category_inforUrl = intent.getStringExtra("url");
        parentCategory = new Category(category_id, category_name, category_inforUrl);

        //Querying al JSON de procesos(se pasa el id de la categoria como paramentro en la URL)
        urlProcedures= "https://dynamicformapi.herokuapp.com/procedures/by_group/"+parentCategory.group_id+".json";

        procedures = new ArrayList<Procedure>();

        HandleJSON handler = new HandleJSON(urlProcedures);
        handler.fetchJSON();

        while (!handler.parsingComplete);

        for (int i=0; i<handler.getId().size(); i++){
            Procedure proc = new Procedure(handler.getProcedure_id().get(i), handler.getGroup_id().get(i), handler.getName().get(i), handler.getDescription().get(i), handler.getInfoURLArray().get(i));
            procedures.add(proc);
        }

        //Poblando ListView
        ProceduresAdapter adapter = new ProceduresAdapter(this, procedures);
        mListViewProcedures.setAdapter(adapter);
        mListViewProcedures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Procedure o = (Procedure)mListViewProcedures.getItemAtPosition(position);
                selectedProcedureId = o.procedure_id;

                displayDialog(o.name,o.description);//
            }
        });

    }

    public void displayDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProceduresActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_access, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String msg = "Cargando";
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();


                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_procedure, menu);
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
}
