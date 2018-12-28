package com.teta_tm.newbook;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> chapters;
    private ArrayList<String> sections;
    private ArrayList<Integer> stars;
    private DbHelper dbHelper;

    public CustomAdapter(Context mContext,int layout,ArrayList<String> chapters){
        super(mContext,layout,chapters);
        this.mContext = mContext;
        this.chapters = chapters; }
    public CustomAdapter(Context mContext,int layout,ArrayList<String> chapters,ArrayList<String> sections){
        super(mContext,layout,chapters);
        this.mContext = mContext;
        this.chapters = chapters;
        this.sections = sections; }
    public CustomAdapter(int layout,ArrayList<String> sections,ArrayList<Integer> stars,Context mContext){
        super(mContext,layout,sections);
        this.mContext = mContext;
        this.sections = sections;
        this.stars = stars;
    }
    @SuppressLint("ViewHolder") @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater in = LayoutInflater.from(mContext);
        final View row ;
        if(sections == null && stars == null && chapters != null){
            row = in.inflate(R.layout.chapters_row,parent,false);
            TextView name = row.findViewById(R.id.chapter_title);
            name.setText(chapters.get(position));
        }else if(sections != null && stars == null && chapters != null){
            row = in.inflate(R.layout.row_result,parent,false);
            TextView chapter = row.findViewById(R.id.tv_result_chapter);
            TextView section = row.findViewById(R.id.tv_result_section);
            chapter.setText(chapters.get(position));
            section.setText(sections.get(position));
        }else if(sections != null && stars != null && chapters == null){
            row = in.inflate(R.layout.section_row,parent,false);
            final ImageView star = row.findViewById(R.id.section_row_star);
            TextView title = row.findViewById(R.id.section_row_title);
            if(stars.get(position) == 0){
                star.setImageResource(android.R.drawable.star_big_off);
            }else{
                star.setImageResource(android.R.drawable.star_big_on);
            }
            title.setText(sections.get(position));
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stars.get(position)==1){
                        updateStar(0, sections.get(position),position);
                        star.setImageResource(android.R.drawable.star_big_off);
                    }else{
                        updateStar(1, sections.get(position),position);
                        star.setImageResource(android.R.drawable.star_big_on);
                    }
                }
            });
        }else{
            row = in.inflate(R.layout.section_row,parent,false);
            final ImageView delete = row.findViewById(R.id.section_row_star);
            delete.setImageResource(android.R.drawable.ic_delete);
            TextView title = row.findViewById(R.id.section_row_title);
            title.setText(sections.get(position));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog(position);
                }
            });

        }

//        name.setTypeface(Typeface.createFromAsset(getAssets(), "font/koodak.ttf"));
        return (row);
    }
    public void updateStar(int value, String section,int position){
        dbHelper = new DbHelper(mContext);
        dbHelper.opendatabase();
        dbHelper.updateBookmark(value,section);
        if(position != -1)
            stars.add(position,value);
        dbHelper.close();
    }
    private  void dialog(final int position){
        final Dialog di = new Dialog(mContext);
        di.requestWindowFeature(Window.FEATURE_NO_TITLE);
        di.setContentView(R.layout.dialog_delete);
        di.setCanceledOnTouchOutside(true);
        di.setCancelable(true);
        di.show();
        Button btn_cancel,btn_ok;
        btn_cancel = di.findViewById(R.id.btn_cancel);
        btn_ok = di.findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                di.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStar(0,sections.get(position),-1);
                Bookmarks.refresher();
                di.dismiss();
            }
        });
    }
}
