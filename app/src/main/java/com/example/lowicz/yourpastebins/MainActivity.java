package com.example.lowicz.yourpastebins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    Database database = new Database(this);
    TextView pasteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Paste> pasteList = database.getAll();

        if (pasteList.size()!=0) {
            ListView listview = (ListView) findViewById(R.id.listView);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            ListAdapter adapter = new PasteAdapter(MainActivity.this, pasteList);
            listview.setAdapter(adapter);

        }

        ImageButton button = (ImageButton) findViewById(R.id.add_paste_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPaste.class);
                startActivity(intent);
            }
        });

    }
}
