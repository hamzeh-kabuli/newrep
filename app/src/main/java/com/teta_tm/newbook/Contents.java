package com.teta_tm.newbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Contents extends AppCompatActivity {
    ListView lv_content;
    ArrayList<String> chapters;
    CustomAdapter adapter;
    DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        chapters = new ArrayList<>();
        dbHelper = new DbHelper(this);
        lv_content = findViewById(R.id.lv_contents);
        refresher();
        adapter = new CustomAdapter(this,R.layout.chapters_row,chapters);
        lv_content.setAdapter(adapter);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Contents.this,Sections.class).
                        putExtra("chapter",chapters.get(position)));
            }
        });
    }

    public void refresher(){
        dbHelper.opendatabase();
        int num = dbHelper.shomaresh_field("chapter");
        for (int i = 0 ; i < num; i++){
            chapters.add(dbHelper.show_field("chapter",i,"chapter"));
        }
        dbHelper.close();
    }
}
