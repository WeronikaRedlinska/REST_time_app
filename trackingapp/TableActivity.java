package com.example.trackingapp;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_main);
        final Object[] temp = new Object[1];
        getSupportActionBar().setTitle("Powrót");
        String[] app_names;
        String[] percentage;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GetData data = new GetData();
        data.execute();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONArray json = data.getData();
        System.out.println(json.length());
        if (json != null) {
            try {
                ArrayList<JSONObject> obiekty = new ArrayList<JSONObject>();
                for(int i=0;i<json.length();i++) {
                    obiekty.add(json.getJSONObject(i));
                }
                ArrayList<String> nazwy = new ArrayList<String>();
                ArrayList<Integer> czasy = new ArrayList<Integer>();
                for(JSONObject obj:obiekty) {
                    nazwy.add((String) obj.get("nazwa_aplikacji"));
                    czasy.add((Integer) obj.get("sum_czas"));
                }
                app_names = new String[nazwy.size()];
                percentage = new String[czasy.size()];
                for (int i = 0; i < nazwy.size(); i++) {
                    app_names[i] = nazwy.get(i);
                    percentage[i] = String.valueOf(czasy.get(i));
                }
                TableLayout tbl = (TableLayout) findViewById(R.id.table);
                tbl.setColumnShrinkable(0,true);
                tbl.bringToFront();
                TableRow tr1 = new TableRow(this);
                tr1.layout(200,200,200,200);
                TextView c11 = new TextView(this);
                c11.setText("Nazwa aplikacji");
                TextView c21 = new TextView(this);
                c21.setText("Czas (ms)");
                tr1.addView(c11);
                tr1.addView(c21);
                tbl.addView(tr1);
                for (int i = 0; i < app_names.length; i++) {
                    TableRow tr = new TableRow(this);
                    TextView c1 = new TextView(this);
                    c1.setText(app_names[i]);
                    TextView c2 = new TextView(this);
                    c2.setText(percentage[i]);
                    tr.layout(200,200,200,200);
                    tr.addView(c1);
                    tr.addView(c2);
                    tbl.addView(tr);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
