package ro.diamondtech.myhousereply;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ro.diamondtech.myhousereply.adapters.*;

import ro.diamondtech.myhousereply.data.MyHouseRoomContract;
import ro.diamondtech.myhousereply.utilities.FakeDataUtils;
import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

//main class with the status for the rooms in the hause selected

public class StatusActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
SharedPreferences.OnSharedPreferenceChangeListener,
        MyHouseStatusRoomAdapter.MyHouseStatusRoomAdapterOnClickHandler

{


    //the columns that we displaying in the device in room
    public static final String[] MAIN_MYSTATUS_DEVICE_PROJECTION = {
            MyHouseRoomContract.MyHouseRoomEntry._ID,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_CODE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_TYPE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_NAME,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_DETAILS,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_ORDER,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_SELECT,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_STATE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_VALUE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_UM,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_ON,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_OFF,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_IMAGE_NAME,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_NAME,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE


    };
    // index in the array to access the data from query (please respect order from MAIN_MYROOM_PROJECTION
    public static final int INDEX_HOUSE_CODE = 1;
    public static final int INDEX_DEVICE_CODE = 2;
    public static final int INDEX_DEVICE_TYPE = 3;
    public static final int INDEX_DEVICE_NAME = 4;
    public static final int INDEX_DEVICE_DETAILS = 5;
    public static final int INDEX_DEVICE_ORDER = 6;
    public static final int INDEX_DEVICE_SELECT = 7;
    public static final int INDEX_DEVICE_STATE = 8;
    public static final int INDEX_DEVICE_VALUE = 9;
    public static final int INDEX_DEVICE_UM = 10;
    public static final int INDEX_DEVICE_LIMIT_ON = 11;
    public static final int INDEX_DEVICE_LIMIT_OFF= 12;
    public static final int INDEX_DEVICE_IMAGE_NAME= 13;
    public static final int INDEX_ROOM_CODE= 14;
    public static final int INDEX_ROOM_NAME= 15;
    public static final int INDEX_USER_CODE= 16;

    //this ID will be used to identify the Loader responsible for loading our data device
    private static final int ID_MYDEVICE_LOADER = 34;
    //get data from laoder for devices
   public Cursor mCursorDevice;

    //get data from laoder for rooms
    public Cursor mCursorRoom;
    //Retain the room code which has been pressed down which is sent to the room settings page
    public String status_CodeRoom;
    public String status_NameRoom;

    //Retain the list of codes of rooms which is sent to the editing page of room and to check its uniqueness in the house
    public String[] status_ListRoom;

    //set house code in the preference
    public String status_code_house ;
    //set house name in the preference
    public String status_name_house ;

    //set user code in the preference unique
    public String main_code_user;



    //the columns that we displaying in the device in room
    public static final String[] MAIN_MYSTATUS_ROOM_PROJECTION = {
            MyHouseRoomContract.MyHouseRoomEntry._ID,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_NAME,
            MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE


    };
    // index in the array to access the data from query (please respect order from MAIN_MYROOM_PROJECTION
    public static final int INDEX_ROOM_HOUSE_CODE = 1;
    public static final int INDEX_ROOM_ROOM_CODE= 2;
    public static final int INDEX_ROOM_ROOM_NAME= 3;
    public static final int INDEX_ROOM_USER_CODE= 4;


    //this ID will be used to identify the Loader responsible for loading our data room
    private static final int ID_MYROOM_LOADER = 33;

 //   private MyHouseStatusDeviceAdapter mMyHouseStatusDeviceAdapter;

    private MyHouseStatusRoomAdapter mMyHouseStatusRoomAdapter;

    //for all devices selected in the room
    private RecyclerView mStatusDeviceRecyclerView ;

    //for all rooms in the house
    private RecyclerView mStatusRoomRecyclerView;

    private int mDevicePosition = RecyclerView.NO_POSITION;

    private int mRoomPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;

    //text for the title where is name of the house if exists
    private  TextView titlu_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBarStatus);

        getPreferences();


        titlu_status = (TextView) findViewById(R.id.textViewStatusTitleHouse);

        //if empty house set for add new house
        if (status_code_house==null) {
            titlu_status.setText(getString(R.string.button_add_house));
        } else {
            titlu_status.setText(status_name_house);
            //show indicator
            showLoading();

        }

        //dispaly all rooms in the house in RecyclerView and adapter MyHouseStatusRoomAdapter
        mStatusRoomRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_status_rooms);
        LinearLayoutManager layoutStatusRoomManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mStatusRoomRecyclerView.setLayoutManager(layoutStatusRoomManager);

        mMyHouseStatusRoomAdapter = new MyHouseStatusRoomAdapter(this, this);

        mStatusRoomRecyclerView.setAdapter(mMyHouseStatusRoomAdapter);

        //start getting data from database for rooms
        getSupportLoaderManager().initLoader(ID_MYROOM_LOADER,null,this);
        //start getting data from database for devices in selected room
        getSupportLoaderManager().initLoader(ID_MYDEVICE_LOADER,null,this);



    }


    public void getPreferences(){
        //get cod_user , house and room from preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        main_code_user = prefs.getString(getResources().getString(R.string.pref_code_user_key),
                getResources().getString(R.string.pref_code_user_default));
        //the default room code is "00000" in preferences
        status_CodeRoom = prefs.getString(getResources().getString(R.string.pref_code_room_key),
                getResources().getString(R.string.pref_code_room_default));

        status_NameRoom = prefs.getString(getResources().getString(R.string.pref_name_room_key),
                getResources().getString(R.string.name_first_room));

        //Manually enter the code of the house is to be chosen through a subsequent procedure
        status_code_house= prefs.getString(getResources().getString(R.string.pref_code_house_key),
                getResources().getString(R.string.pref_code_house_default));

        status_name_house= prefs.getString(getResources().getString(R.string.pref_name_house_key),
                getResources().getString(R.string.name_first_house));

    }

    //for future implementations.............
    public void setupPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getResources().getString(R.string.pref_code_house_key), getResources().getString(R.string.pref_code_house_default));
        editor.putString(getResources().getString(R.string.pref_name_house_key), getResources().getString(R.string.name_first_house));
        editor.putString(getResources().getString(R.string.pref_code_room_key), getResources().getString(R.string.pref_code_room_default));
        editor.putString(getResources().getString(R.string.pref_code_device_key), getResources().getString(R.string.pref_code_device_default));
        editor.commit();
        prefs.registerOnSharedPreferenceChangeListener(this);

    }


    //for showing indicator on loading
    private void showLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_status_page,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Context context = StatusActivity.this;
        switch (id) {

            case R.id.mstatus_add_room :
                if (status_code_house.equals(getResources().getString(R.string.pref_code_house_default))){
                    Toast.makeText(StatusActivity.this,getResources().getString(R.string.msg_no_house),Toast.LENGTH_SHORT).show();
                } else {

                    Class editRoomActivity = EditRoomActivity.class;
                    Intent intenttoStartActivityRoom = new Intent(context,editRoomActivity);
                    //Send a list of the code of the rooms to intenttoStartActivityRoom for test in a new room
                    intenttoStartActivityRoom.putExtra(Intent.EXTRA_TEXT,status_ListRoom);
                    startActivity(intenttoStartActivityRoom);
                }
                break;
            case R.id.mstatus_add_house :
                Class editHouseActivity = EditHouseActivity.class;
                Intent intenttoStartActivityHouse = new Intent(context,editHouseActivity);
                startActivity(intenttoStartActivityHouse);
                break;


        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //only user and house selected
        switch (id){
            case ID_MYDEVICE_LOADER : {
                showLoading();
                String selectionDevices = MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE+" LIKE '" +status_code_house+ "' and "+
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE+" LIKE '" +main_code_user+"' and "+
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_SELECT+"=1";
                Uri Uriquerydatastatus = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
                return new CursorLoader(this,
                        Uriquerydatastatus,
                        MAIN_MYSTATUS_DEVICE_PROJECTION,
                        selectionDevices,
                        null,
                        null
                );

            }

            case ID_MYROOM_LOADER : {
                showLoading();
                String selectionRooms = MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE+" LIKE '" +status_code_house+ "' and "+
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE+" LIKE '" +main_code_user+"' GROUP BY "+MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE;
                Uri Uriquerydatastatus = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;

                return new CursorLoader(this,
                        Uriquerydatastatus,
                        MAIN_MYSTATUS_ROOM_PROJECTION,
                        selectionRooms,
                        null,
                        null
                );

            }

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data!=null) {
            int idLoader = loader.getId();
            switch (idLoader) {
                case ID_MYDEVICE_LOADER: {
                    //the update is on hold if both data are not complete
                   if (mCursorRoom!=null) {
                       StoreListRooms(mCursorRoom);
                       mMyHouseStatusRoomAdapter.swapCursor(mCursorRoom,data);
                        //stop show loadin only when is finish both Cursors
                       showMyStatusDataView();
                   }
                    int nrcount = data.getCount();
                    if (nrcount != 0) {
                        mCursorDevice=data;
                    } else {
                        mCursorDevice=data;
                        showMyStatusDataView();
                    }
                    break;
                }
                case ID_MYROOM_LOADER : {
                    //the update is on hold if both data are not complete
                   if (mCursorDevice!=null) {
                       StoreListRooms(data);
                       mMyHouseStatusRoomAdapter.swapCursor(data,mCursorDevice);
                       //stop show loadin only when is finish both Cursors
                       showMyStatusDataView();
                   }

                    if (mRoomPosition == RecyclerView.NO_POSITION) mRoomPosition = 0;
                    mStatusRoomRecyclerView.smoothScrollToPosition(mRoomPosition);
                    int nrcount = data.getCount();
                    if (nrcount != 0){
                        mCursorRoom = data;
                       showMyStatusDataView();
                    } else {
                        titlu_status.setText(getResources().getString(R.string.msg_room_no_data)+" "+status_NameRoom);
                        showMyStatusDataView();
                    }
                    break;
                }
            }

        } else {
            String msgnodata = getResources().getString(R.string.msg_room_no_data);
            Toast.makeText(StatusActivity.this,msgnodata,Toast.LENGTH_SHORT).show();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    //The procedure which forms a list of the codes of rooms to be trasmited to edit room page
    public void StoreListRooms(Cursor data){
        status_ListRoom= new String[1];
        status_ListRoom[0]="";
        if (data!=null) {
            int size= data.getCount();
            if (size>0){
                int position=0;
            data.moveToFirst();
            String cod=data.getString(StatusActivity.INDEX_ROOM_ROOM_CODE);
            //Iterate through the entire list and retain only the camera code
            status_ListRoom= new String[size];
            status_ListRoom[position] = cod;

            try {
                while (data.moveToNext()) {
                    position = data.getPosition();
                    status_ListRoom[position] = data.getString(StatusActivity.INDEX_ROOM_ROOM_CODE);
                }
           } finally {
                data.moveToFirst();
           }
            }
        }


    }



    //show the data on display in recyclerview
    private void showMyStatusDataView() {
        // hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        //daca nu sunt date atunci
        //If it is the original home "00000" then message appears for add new house
        if (status_code_house.equals(getResources().getString(R.string.pref_code_house_default))){
            titlu_status.setText(getResources().getString(R.string.msg_no_house));

        } else {
            if (status_CodeRoom.equals(getResources().getString(R.string.pref_code_room_default))){
            titlu_status.setText(getResources().getString(R.string.msg_no_room_in_this_house)+" "+status_name_house);
            } else {
                titlu_status.setText(status_name_house);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMyHouseStatusRoomAdapter.swapCursor(null,null);
    }


    //only for clicks on room image and start edit room
    @Override
    public void onClick() {
        status_CodeRoom=mCursorRoom.getString(StatusActivity.INDEX_ROOM_ROOM_CODE);
        status_NameRoom=mCursorRoom.getString(StatusActivity.INDEX_ROOM_ROOM_NAME);
        Class editRoomActivity = EditRoomActivity.class;
        //set code user , code house and code room in preferences for all activities
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //se introduce codul casei in preference           pref_code_room_key
        editor.putString(getResources().getString(R.string.pref_code_room_key), status_CodeRoom);
        editor.putString(getResources().getString(R.string.pref_name_room_key), status_NameRoom);
        editor.commit();
        Intent intenttoStartActivityRoom = new Intent(this,editRoomActivity);
        //Send a list of the rooms code to the correct set room in intenttoStartActivityRoom
        intenttoStartActivityRoom.putExtra(Intent.EXTRA_TEXT,status_ListRoom);
        startActivity(intenttoStartActivityRoom);
    }

    //only for clicks on device image and change device status for non sensor type
    @Override
    public void ondeviceClick(String id_device,String value) {
        int no_update=UpdateDeviceClickImage(id_device,value);


    }


    //change device status ON/OFF for non sensor type
    public int UpdateDeviceClickImage(String id_device,String value){
        ContentValues contentValues = new ContentValues();
        //se seteaza ON pentru test
        contentValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_VALUE,value);
        Uri Uriupdatedataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
        Uriupdatedataroom = Uriupdatedataroom.buildUpon().appendPath(id_device).build();
        int update = getContentResolver().update(Uriupdatedataroom,contentValues,null,null);

        if (update>0) {
            getSupportLoaderManager().restartLoader(ID_MYDEVICE_LOADER, null, StatusActivity.this);
              String mesaj_update = getResources().getString(R.string.msg_device_set) + " "+value;
               Toast.makeText(StatusActivity.this, mesaj_update,Toast.LENGTH_SHORT).show();
        }

        return update;
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferences();
        getSupportLoaderManager().restartLoader(ID_MYDEVICE_LOADER, null, StatusActivity.this);
        getSupportLoaderManager().restartLoader(ID_MYROOM_LOADER, null, StatusActivity.this);
    }
}
