package com.riact.ricart;

import android.database.DataSetObserver;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by anton on 06/01/17.
 */

public class CheckBoxClick implements AdapterView.OnItemClickListener {



   static ArrayList<String> selectedStrings = new ArrayList<String>();
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

//        CheckedTextView ctv = (CheckedTextView)arg1;
        System.out.print("hello:");
        /*if(ctv.isChecked()){
            selectedStrings.add(ctv.getText().toString());

            System.out.println("checked");

        }else{
            selectedStrings.remove(ctv.getText().toString());
            //Toast.makeText(MainActivity.this, "now it is checked", Toast.LENGTH_SHORT).show();
            System.out.println("unchecked");
        }*/

        }


}

