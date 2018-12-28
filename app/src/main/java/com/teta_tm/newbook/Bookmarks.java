package com.teta_tm.newbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Bookmarks extends AppCompatActivity {
    private static ListView lv_bookmark;
    private static Context mContext;
    private static DbHelper dbHelper;
    private static CustomAdapter adapter;
    private static ArrayList<String> sections;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        lv_bookmark = findViewById(R.id.lv_bookmark);
        dbHelper = new DbHelper(this);
        mContext = this;
        refresher();

    }
    public static void refresher(){
        dbHelper.opendatabase();
        sections = new ArrayList<>();
        int count = dbHelper.count_bookmark();
        for (int i = 0 ; i < count ; i++){
            sections.add(dbHelper.get_bookmark(i));
        }
        adapter = new CustomAdapter(R.layout.section_row,sections,null,mContext);
        lv_bookmark.setAdapter(adapter);
        dbHelper.close();
    }

}
