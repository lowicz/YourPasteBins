package com.example.lowicz.yourpastebins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ViewAnimator;

import java.util.ArrayList;


public class MainActivity extends Activity {

    Database database = new Database(this);
    ArrayList<Paste> pasteList;

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pasteList = database.getAll();

        if (pasteList.size()!=0) {
            ListView listview = (ListView) findViewById(R.id.listView);
            final PasteAdapter adapter = new PasteAdapter(MainActivity.this, pasteList);
            listview.setAdapter(adapter);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Paste paste = (Paste) adapter.getItem(position);
                    Intent intent = new Intent(MainActivity.this, EditPaste.class);
                    intent.putExtra("message", paste);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();


        } else {
            ViewAnimator va = (ViewAnimator) findViewById(R.id.va);
            va.setDisplayedChild(1);
        }


        ImageButton button = (ImageButton) findViewById(R.id.add_paste_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPaste.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

    }
}
