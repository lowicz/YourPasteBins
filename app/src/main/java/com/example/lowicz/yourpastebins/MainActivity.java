package com.example.lowicz.yourpastebins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        ArrayList<HashMap<String, String>> pasteList = database.getAll();

        if (pasteList.size()!=0) {
            ListView listview = (ListView) findViewById(R.id.listView);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pasteID = (TextView) view.findViewById(R.id.pasteID);

                }
            });

            ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    pasteList, R.layout.one_paste,
                    new String[] {"pasteID", "url", "paste_text"},
                    new int[] {R.id.pasteID, R.id.url, R.id.paste_text3});
            listview.setAdapter(adapter);

        }

    }

    public void showAddPaste(View v) {
        Intent intent = new Intent(this, AddPaste.class);
        startActivity(intent);
    }
}
