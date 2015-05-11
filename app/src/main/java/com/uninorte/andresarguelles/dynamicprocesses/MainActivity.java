package com.uninorte.andresarguelles.dynamicprocesses;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ListView mListView;

    private String urlAPI;

    ArrayList<Category> categories;
    ArrayList<String> categoryName;
    ArrayList<String> urlNext;

    HandleJSON obj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.mListView);

        // Cuando arranca la app, obtiene las categorías disponibles
        urlAPI = "https://dynamicformapi.herokuapp.com/groups.json";
        String msg="Connecting to the server...";
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();

        categories = new ArrayList<Category>();
        categoryName = new ArrayList<String>();

        obj = new HandleJSON(urlAPI, 1);//EDITAR REMOVER 2do Parametro!!!
        obj.fetchJSON();

        while (!obj.parsingComplete);

        categoryName = obj.getName();
        for (int i = 0; i<obj.getId().size(); i++){
            Category cat = new Category(obj.getId().get(i), obj.getGroup_id().get(i), obj.getName().get(i), obj.getUrlNext().get(i));
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
                String str=(String)o.name;//As you are using Default String Adapter
                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
            }
        });





        /*TextView tv = new TextView(getApplicationContext());
        tv.setText("Hola");
        mLinearLayout.addView(tv);*/
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
