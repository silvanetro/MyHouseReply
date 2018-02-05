package ro.diamondtech.myhousereply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

//class where is introduced/edit name and code of the house
//houses list and delete option  is not implemented yet.....
public class EditHouseActivity extends AppCompatActivity {

    private String edit_house_code_house;
    private String edit_house_name_house;
    private EditText editTextHouseName ;
    private EditText editTextHouseCode ;
    private  TextView textTitleHouse;
    private  TextView textListTitleHouse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_house);
        editTextHouseName =  findViewById(R.id.editTextHouseName);
        editTextHouseCode =  findViewById(R.id.editTextHouseCode);
        textTitleHouse =  findViewById(R.id.textViewEditRoomTitleHouse);
        textListTitleHouse =  findViewById(R.id.textViewListNameHouse);

        //cData must be collected in the Preferences
        getPreferences();
        showMyStatusDataView();
        //button for save house data
        final Button buttonHouseSaveData = (Button) findViewById(R.id.buttonHouseSaveData);
        buttonHouseSaveData.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               setupPreferences();
                                               Context  context = EditHouseActivity.this;
                                               Toast.makeText(context,getResources().getString(R.string.msg_save_house),Toast.LENGTH_SHORT).show();
                                           }
                                       }

        );


    }


    public void getPreferences() {
        //get code user , house and room from Preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //get code house
        edit_house_code_house = prefs.getString(getResources().getString(R.string.pref_code_house_key),
                getResources().getString(R.string.pref_code_house_default));
        //get name house
        edit_house_name_house = prefs.getString(getResources().getString(R.string.pref_name_house_key),
                getResources().getString(R.string.name_first_house));
        //show this data
        textTitleHouse.setText(edit_house_name_house);
        textListTitleHouse.setText(edit_house_name_house);

    }


        //save data changed
        public void setupPreferences(){
            String strTemp;//ajuta la reducerea dimensiunilor sirului
            //set code user , cod ehouse and code room in the preferences for all Activities
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            //set  code , name house in the preferences
            strTemp = editTextHouseCode.getText().toString();
            editor.putString(getResources().getString(R.string.pref_code_house_key), strTemp);
            //sset room code, name in the preferences
            strTemp=editTextHouseName.getText().toString();
            editor.putString(getResources().getString(R.string.pref_name_house_key),strTemp );
            textTitleHouse.setText(editTextHouseName.getText().toString());
            textListTitleHouse.setText(edit_house_name_house);
            editor.commit();

    }


    private void showMyStatusDataView() {

        //if the code is default 00000 then hide house list and set initial data : name My House and code house001
        if (edit_house_code_house.equals(getResources().getString(R.string.pref_code_house_default))) {
            editTextHouseName.setText(getResources().getString(R.string.name_first_house));
            editTextHouseCode.setText(getResources().getString(R.string.code_first_house));

        } else {
            // se afiseaza numele si codul din preferences
            editTextHouseName.setText(edit_house_name_house);
            editTextHouseCode.setText(edit_house_code_house);

        }

    }
}