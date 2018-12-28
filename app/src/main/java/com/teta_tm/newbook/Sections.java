package com.teta_tm.newbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Sections extends AppCompatActivity {
    private ListView lv_sections;
    private DbHelper dbHelper;
    private CustomAdapter adapter;
    private String chapter;
    private ArrayList<String> sections;
    private ArrayList<Integer> stars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);
        Intent intent = getIntent();
        chapter = intent.getStringExtra("chapter");
        lv_sections = findViewById(R.id.lv_sections);
        dbHelper = new DbHelper(this);
        refresher(chapter);
        adapter = new CustomAdapter(R.layout.section_row,sections,stars,this);
        lv_sections.setAdapter(adapter);
        lv_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Sections.this, PageActivity.class)
                        .putExtra("section",sections.get(position)).putExtra("chapter",chapter));
            }
        });
    }

    public void refresher(String chapter){
        dbHelper.opendatabase();
        sections = new ArrayList<>();
        stars = new ArrayList<>();
        int numOfSections = dbHelper.section_counter("section",chapter);
        for (int i = 0 ; i < numOfSections ; i++){
            sections.add(dbHelper.section_getter("section",chapter,i));
            stars.add(Integer.parseInt(dbHelper.show_field_star("bookmark",i,"section",chapter)));
        }
        dbHelper.close();
    }
}
