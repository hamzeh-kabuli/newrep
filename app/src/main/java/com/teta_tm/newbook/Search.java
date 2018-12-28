package com.teta_tm.newbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private ListView lv_search;
    private RadioGroup rg_search;
    private RadioButton rb_titles,rb_texts;
    private ImageView img_search;
    private EditText et_search_query;
    private static String SELECT_TITLES="chapter,section",
            SELECT_TEXTS="substr(text,1,50)";
    private String select;
    private DbHelper dbHelper;
    private CustomAdapter adapter;
    private ArrayList<String> chapters,sections,texts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_texts){
                    select = SELECT_TEXTS;
                }else if(checkedId == R.id.rb_titles){
                    select = SELECT_TITLES;
                }
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkQuery()){
                    search(et_search_query.getText().toString());
                }else{
                    Toast.makeText(Search.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(){
        lv_search = findViewById(R.id.lv_result);
        rg_search = findViewById(R.id.rg_search);
        rb_texts = findViewById(R.id.rb_texts);
        rb_titles = findViewById(R.id.rb_titles);
        img_search = findViewById(R.id.img_search);
        et_search_query = findViewById(R.id.et_query);
        sections = new ArrayList<>();
        chapters = new ArrayList<>();
        texts = new ArrayList<>();
        dbHelper = new DbHelper(this);
        select = SELECT_TITLES;
        rb_titles.setSelected(true);
    }

    public void search(String query){
        dbHelper.opendatabase();
        if (select.equalsIgnoreCase(SELECT_TITLES)){
            int count = dbHelper.search_titles_counter(query,select);
            for (int i = 0; i < count; i++){
                chapters.add(dbHelper.search_titles(query,select,i)[0]);
                sections.add(dbHelper.search_titles(query,select,i)[1]);
            }
            adapter = new CustomAdapter(this,R.layout.row_result,chapters,sections);
        }else{
            int count = dbHelper.search_texts_counter(query,select);
            for (int i = 0; i < count; i++){
                texts.add(dbHelper.search_texts(query,select,i));
            }
            adapter = new CustomAdapter(this,R.layout.chapters_row,texts);
        }
        lv_search.setAdapter(adapter);
        dbHelper.close();
    }

    public boolean checkQuery(){
        boolean isFilled = true;
        if(et_search_query.getText().length() == 0){
            isFilled = false;
        }
        return isFilled;
    }
}
