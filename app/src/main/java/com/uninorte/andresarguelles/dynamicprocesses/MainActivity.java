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


public class MainActivity extends ActionBarActivity {

    ListView mListView;

    private String urlAPI;

    ArrayList<Category> categories;

    HandleJSON obj;

    int selectedCategoryId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.mListView);

        // Cuando arranca la app, obtiene las categorías disponibles
        urlAPI = "https://dynamicformapi.herokuapp.com/groups.json";


        categories = new ArrayList<Category>();


        obj = new HandleJSON(urlAPI);
        obj.fetchJSON();

        while (!obj.parsingComplete);


        for (int i = 0; i<obj.getId().size(); i++){
            Category cat = new Category (obj.getGroup_id().get(i), obj.getName().get(i), obj.getInfoURLArray().get(i));
            categories.add(cat);
        }


        // Poblando listview
        /*
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryName);
        mListView.setAdapter(itemsAdapter);*/
        CategoriesAdapter adapter = new CategoriesAdapter(this, categories);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Category o = (Category)mListView.getItemAtPosition(position);
                selectedCategoryId = o.group_id;
                displayDialog(o.name,o.group_id, o.name, o.infoUrl);//
            }
        });
    }

    public void displayDialog(String title, final int category_id, final String name, final String url){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title)
                .setPositiveButton(R.string.dialog_access, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String msg = "Cargando";
                        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        toast.show();

                        Intent navToProcedure = new Intent(MainActivity.this,ProceduresActivity.class);
                        navToProcedure.putExtra("category_id",category_id );
                        navToProcedure.putExtra("name", name);
                        navToProcedure.putExtra("url", url);
                        startActivity(navToProcedure);

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
