package com.teta_tm.newbook;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class Setting extends AppCompatActivity {
    private TextView sample;
    private RadioGroup rg_mode;
    private SeekBar sb_fontsize,sb_linespace;
    private RadioButton rb_day,rbnight;
    private Spinner sp_fonts;
    private String[] fonts;
    private static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static Typeface typeface;
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setPreferences();
        rg_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_mode_day:
                        sample.setBackgroundColor(Color.WHITE);
                        sample.setTextColor(Color.BLACK);
                        editor.putString("readmode","day");
                        editor.commit();
                        break;
                    case R.id.rb_mode_night:
                        sample.setBackgroundColor(Color.BLACK);
                        sample.setTextColor(Color.WHITE);
                        editor.putString("readmode","night");
                        editor.commit();
                        break;
                }
            }
        });
        sp_fonts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editor.putString("font",fonts[0]);
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/lotus.ttf");
                        break;
                    case 1:
                        editor.putString("font",fonts[1]);
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/nazanin.ttf");
                        break;
                    case 2:
                        editor.putString("font",fonts[2]);
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/koodak.ttf");
                        break;
                    case 3:
                        editor.putString("font",fonts[3]);
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/yekan.ttf");
                        break;
                }
                editor.commit();
                sample.setTypeface(typeface);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sb_linespace.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("linespace",progress);
                float size = (float)progress;
                sample.setLineSpacing(size,1);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_fontsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("fontsize",progress);
                editor.commit();
                sample.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void init(){
        mContext = this;
        sample = findViewById(R.id.tv_txt_preview);
        rg_mode = findViewById(R.id.rg_mode);
        sb_fontsize = findViewById(R.id.sb_fontsize);
        sb_linespace = findViewById(R.id.sb_linespace);
        sp_fonts = findViewById(R.id.sp_select_font);
        fonts = getResources().getStringArray(R.array.fonts);
        rb_day = findViewById(R.id.rb_mode_day);
        rbnight = findViewById(R.id.rb_mode_night);
    }
    public void setPreferences(){
        sample.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/"+sharedPreferences.getString("font","nazanin")+".ttf"));
        sample.setTextSize(sharedPreferences.getInt("fontsize",sb_fontsize.getProgress()));
        float size = (float)sharedPreferences.getInt("linespace",sb_linespace.getProgress());
        sample.setLineSpacing(size,1);
        if(sharedPreferences.getString("readmode","day").equals("day")){
            sample.setTextColor(Color.BLACK);
            sample.setBackgroundColor(Color.WHITE);
            rg_mode.clearCheck();
            rb_day.setSelected(true);
        }else{
            sample.setTextColor(Color.WHITE);
            sample.setBackgroundColor(Color.BLACK);
            rg_mode.clearCheck();
            rbnight.setSelected(true);
        }
        if(sharedPreferences.getString("font","nazanin").equals("lotus")){
            sp_fonts.setSelection(0);
        } else if (sharedPreferences.getString("font","nazanin").equals("nazanin")){
            sp_fonts.setSelection(1);
        }else if(sharedPreferences.getString("font","nazanin").equals("koodak")){
            sp_fonts.setSelection(2);
        }else{
            sp_fonts.setSelection(3);
        }


    }

    public static void setTypeface(TextView tv,Context mContext,SharedPreferences shared){
        typeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/"+shared.getString("font","nazanin")+".ttf");
        tv.setTypeface(typeface);
    }
}
