package com.mrinalraj.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Home extends AppCompatActivity {

    Spinner yearSpin, branchSpin, sectionSpin;
    List<String> yearList,sectionList;
    private ArrayAdapter yearAdapter, sectionAdapter;
    JSONObject branchObj;
    String[] yearsArray;
    String brSelected, yrSelected, secSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        branchSpin = findViewById(R.id.branch_spinner);
        yearSpin = findViewById(R.id.year_spinner);
        sectionSpin = findViewById(R.id.section_spinner);

        yearList =new LinkedList<String>();
        sectionList =new LinkedList<String>();

        yearAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(yearAdapter);

        sectionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sectionList);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpin.setAdapter(sectionAdapter);

        try {
            branchObj = new JSONObject(loadJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        branchSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String branch = adapterView.getSelectedItem().toString();
                try {
                    switch (branch) {
                        case "Information Technology":
                            setYearSpin(yearSpin,"IT");
                            break;
                        case "Computer Science Engineering":
                            setYearSpin(yearSpin,"CSE");
                            break;
                        case "Electronics Engineering":
                            setYearSpin(yearSpin,"EN");
                            break;
                        case "Electronics and Telecommunication Engineering":
                            setYearSpin(yearSpin,"ET");
                            break;
                        case "Mechanical engineering":
                            setYearSpin(yearSpin,"ME");
                            break;
                        case "Civil Engineering":
                            setYearSpin(yearSpin,"CE");
                            break;
                        case "Applied Electronics and Instrumentation":
                            setYearSpin(yearSpin,"AEI");
                            break;
                        case "Plastic and Polymer Engineering":
                            setYearSpin(yearSpin,"PPE");
                            break;
                        default:
                    }
                }
                catch (Exception e){e.printStackTrace();}
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String yearsSelected = adapterView.getSelectedItem().toString();
                switch (yearsSelected){
                    case "1st year":
                        setSectionSpin(sectionSpin,brSelected,"1st year");
                        break;
                    case "2nd year":
                        setSectionSpin(sectionSpin,brSelected,"2nd year");
                        break;
                    case "3rd year":
                        setSectionSpin(sectionSpin,brSelected,"3rd year");
                        break;
                    case "4th year":
                        setSectionSpin(sectionSpin,brSelected,"4th year");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        sectionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secSelected = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this,yrSelected+"\n"+brSelected+"\n"+secSelected , Toast.LENGTH_SHORT).show();
            }
        });

    }

    Map<String,Object> getYears(String br){
        try {
            JSONObject yearObj = branchObj.getJSONObject(br);
            Iterator it = yearObj.keys();
            Map<String, Object> years = new TreeMap<String, Object>();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object value = yearObj.get(key);
                years.put(key, value);
            }
            return years;
        }
        catch (Exception e){
            return null;
        }
    }

    public void setYearSpin(Spinner yearSpin, String br) {
        this.yearSpin = yearSpin;
        brSelected = br;
        yearList.clear();
        Set<String> years = getYears(br).keySet();
        yearsArray = years.toArray(new String[years.size()]);
        for(String y:yearsArray){
            yearList.add(y);
        }
        yearAdapter.notifyDataSetChanged();
    }

    public void setSectionSpin(Spinner branchSpin, String br, String year) {
        this.branchSpin = branchSpin;
        yrSelected = year;
        sectionList.clear();
        String sections= getYears(br).get(year).toString();
        try {
            JSONObject sectionObject = new JSONObject(sections);
            JSONArray section = sectionObject.getJSONArray("sections");
            for (int i=0;i<section.length(); i++){
                sectionList.add(section.optString(i));
            }
            sectionAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("branchSectionList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
