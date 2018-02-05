package ro.diamondtech.myhousereply;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ro.diamondtech.myhousereply.adapters.*;

import ro.diamondtech.myhousereply.data.MyHouseRoomContract;
import ro.diamondtech.myhousereply.models.MyHouseCustomDevices;
import ro.diamondtech.myhousereply.utilities.FakeDataUtils;
import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

//class where is introduced all devices in the selected room (menu or click) and showing same properties of the device

public class EditRoomActivity extends AppCompatActivity
                implements LoaderManager.LoaderCallbacks<Cursor>,
                MyHouseRoomAdapter.MyHouseRoomAdapterOnClickHandler
{
    //the columns that we displaying in the room
    public static final String[] MAIN_MYROOM_PROJECTION = {
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
            // index in the array to access the data from query (please respect order from MAIN_MYROOM_PROJECTION)
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

    //this ID will be used to identify the Loader responsible for loading our data room
    public static final int ID_MYROOM_LOADER = 33;

    private MyHouseRoomAdapter mMyHouseRoomAdapter;

    private RecyclerView mRoomRecyclerView;

    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;

    //get user code from MainActivity and set in Preference
    private String mCodeUserPref;

    //same for all data
    private String mCodeHousePref;
    private String mNameHousePref;
    private String mCodeRoomPref;
    private String mNameRoomPref;

    //Retain a list of all the codes of room in the house to check the uniqueness of the room code in one house
    private String[] mListeCodeRoom;

    //Retain the old name for the camera to check for updates the name room
    private String mOldNameRoom;
    private String mCodeDevicePref;
    private TextView textViewEditTitleRoom;
    private EditText editTextRoomName ;
    private TextView textViewListNameRoom;
    private EditText editTextRoomCode ;
    //It sets to true to enter false FakeDataUtils devices only in the new room
    private boolean mEmptyRoom;
    private int mCountDevices=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);
        getSupportActionBar().setElevation(0f);
        editTextRoomName = findViewById(R.id.editTextRoomName);
        editTextRoomCode = findViewById(R.id.editTextCodeRoom);
        textViewEditTitleRoom = findViewById(R.id.textViewEditTitleRoom);
        textViewListNameRoom = findViewById(R.id.textViewListNameRoom);

        //Retrieve the list of codes of the room for the verification of the uniqueness of the room
        Intent intentEditRoomActivity = getIntent();
        if (intentEditRoomActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String[] textEntered = intentEditRoomActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
            mListeCodeRoom=textEntered;

        };


        mRoomRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_room);

        //indicator for waiting load device data in room
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBarLoadingRoom);

        LinearLayoutManager layoutRoomManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRoomRecyclerView.setLayoutManager(layoutRoomManager);

        mRoomRecyclerView.setHasFixedSize(false);

        mMyHouseRoomAdapter = new MyHouseRoomAdapter(this, this);

        mRoomRecyclerView.setAdapter(mMyHouseRoomAdapter);
        //show indicator
        showLoading();


        //get data for user house and room from the Preferences
        getPreferences();
        showMyRoomDataView();

        getSupportLoaderManager().initLoader(ID_MYROOM_LOADER, null, this);


        //button for save the data of the room
        final Button buttonRoomSaveData = (Button) findViewById(R.id.buttonSaveRoom);
        buttonRoomSaveData.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       mOldNameRoom=mNameRoomPref;
                                                       setupPreferences();
                                                       getPreferences();
                                                       Context  context = EditRoomActivity.this;
                                                       String newcode_room= editTextRoomCode.getText().toString();
                                                       String newname_room= editTextRoomName.getText().toString();

                                                      //If it is found that there is a new room then place 5 tempoarar false devices (up to the new not implemented real device procedure)
                                                       if(!FindRoom(newcode_room)||mEmptyRoom) {

                                                           //Enter only 5 device that are only 5 sets of properties with these device in Class MyHouseCustomDevices
                                                           //for more device is required to be introduced new methods with properties in that class for each new device

                                                          String device_bulb = getResources().getString(R.string.object_bulb);
                                                          String device_temperature = getResources().getString(R.string.object_temperature);
                                                          String device_gas = getResources().getString(R.string.object_gas);
                                                          String device_soilhumidity = getResources().getString(R.string.object_soil_humidity);
                                                          String device_yale = getResources().getString(R.string.object_yale);

                                                          String[] identity = new String[]{
                                                                  mCodeUserPref,
                                                                  "user1",
                                                                  mCodeHousePref,
                                                                  mNameHousePref,
                                                                  mCodeRoomPref,
                                                                  mNameRoomPref
                                                           };
                                                          showLoading();
                                                          //light
                                                          FakeDataUtils.insertFakeDataDevice(context,device_bulb,identity,mCountDevices);
                                                          //door
                                                          FakeDataUtils.insertFakeDataDevice(context,device_yale,identity,mCountDevices);
                                                          //air temperature sensor
                                                          FakeDataUtils.insertFakeDataDevice(context,device_temperature,identity,(mCountDevices+1));
                                                          //gas sensor
                                                          FakeDataUtils.insertFakeDataDevice(context,device_gas,identity,mCountDevices);
                                                          //for plant
                                                          FakeDataUtils.insertFakeDataDevice(context,device_soilhumidity,identity,mCountDevices);
                                                          showMyRoomDataView();
                                                          //restart loader for showing new data in the page
                                                          getSupportLoaderManager().restartLoader(ID_MYROOM_LOADER, null, EditRoomActivity.this);
                                                          Toast.makeText(context,getResources().getString(R.string.msg_save_room),Toast.LENGTH_SHORT).show();
                                                      } else {
                                                          //if the room exists then set new name if it is changed
                                                              if (!mOldNameRoom.equals(newname_room)) {
                                                             int update= UpdateRoom(newcode_room,newname_room);
                                                             if (update>0){
                                                                 getSupportLoaderManager().restartLoader(ID_MYROOM_LOADER, null, EditRoomActivity.this);
                                                              }

                                                          }
                                                      }

                                                   }
                                               }

        );


        //button for delete room
        final Button buttonDeleteRoom = (Button) findViewById(R.id.buttonDeleteRoom);
        buttonDeleteRoom.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    Context context = EditRoomActivity.this;

                                                    //if it is found when avoid deleting
                                                    if (FindRoom(mCodeRoomPref)) {

                                                        new AlertDialog.Builder(context)
                                                                .setTitle(getResources().getString(R.string.btn_delete))
                                                                .setMessage(getResources().getString(R.string.delete_room_dialog_msg))
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                                                    public void onClick(DialogInterface dialog, int whichButton) {

                                                                        showLoading();
                                                                        //delete all devices with code room
                                                                        int delete_no = DeleteRoom();
                                                                        if (delete_no > 0) {
                                                                            showMyRoomDataView();
                                                                            //restart loader for showing than change
                                                                            getSupportLoaderManager().restartLoader(ID_MYROOM_LOADER, null, EditRoomActivity.this);
                                                                         }
                                                                    }})
                                                                .setNegativeButton(android.R.string.no, null).show();



                                                    }
                                                }
                                            }
        );


    }


        //Check whether it is a new code of the room
        private boolean FindRoom(String code_room){
             boolean find_room=false;
             if (mListeCodeRoom!=null){
                    for( int i = 0; i <= mListeCodeRoom.length - 1; i++)
                    {
                        //if is find then return true
                        if (code_room.equals(mListeCodeRoom[i])) find_room=true;
                    }
             }
             return find_room;
        }



    //get the user and house data from the Preferences
    private void getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mCodeUserPref = prefs.getString(getResources().getString(R.string.pref_code_user_key),
                getResources().getString(R.string.pref_code_user_default));

     //   mNameUserPref = prefs.getString(getResources().getString(R.string.pref_name_user_key),
      //          getResources().getString(R.string.pref_code_user_default));

        mCodeHousePref = prefs.getString(getResources().getString(R.string.pref_code_house_key),
                getResources().getString(R.string.pref_code_house_default));

        mNameHousePref = prefs.getString(getResources().getString(R.string.pref_name_house_key),
                getResources().getString(R.string.name_first_house));

        mCodeRoomPref = prefs.getString(getResources().getString(R.string.pref_code_room_key),
                getResources().getString(R.string.pref_code_room_default));

        mNameRoomPref = prefs.getString(getResources().getString(R.string.pref_name_room_key),
                getResources().getString(R.string.name_first_room));

        textViewEditTitleRoom.setText(mNameRoomPref);
        textViewListNameRoom.setText(mNameRoomPref);

        mCodeDevicePref = prefs.getString(getResources().getString(R.string.pref_code_device_key),
                getResources().getString(R.string.pref_code_device_default));
    }


    public void setupPreferences(){
        String strTemp;//string
        //set data for all activities
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //set room code in the Preferences
        strTemp = editTextRoomCode.getText().toString();
        editor.putString(getResources().getString(R.string.pref_code_room_key), strTemp);
        //If there is another room then is allowed to adding devices by default
        if (!strTemp.equals(mCodeRoomPref)){
            mEmptyRoom=true;
        }
        mCodeRoomPref=strTemp;
        //set room name in the Preferences
        strTemp=editTextRoomName.getText().toString();
        editor.putString(getResources().getString(R.string.pref_name_room_key),strTemp );
        textViewEditTitleRoom.setText(strTemp);
        editor.commit();

    }




    //for showing indicator on loading
    private void showLoading() {

        mRoomRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        
        switch (id){
            case ID_MYROOM_LOADER : {
                //get only devices with the same user house and roo code
                String selectionDevicesRooms = MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE+" LIKE '" +mCodeHousePref+ "' and "+
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE+" LIKE '" + mCodeUserPref+ "' and "+
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE+" LIKE '" + mCodeRoomPref+"'";
                Uri Uriquerydataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
                   return new CursorLoader(this,
                        Uriquerydataroom,
                        MAIN_MYROOM_PROJECTION,
                           selectionDevicesRooms,
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
           mMyHouseRoomAdapter.swapCursor(data);
           if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
           mRoomRecyclerView.smoothScrollToPosition(mPosition);
           mCountDevices = data.getCount();
           if (mCountDevices != 0) {
               data.moveToPosition(0);
               //It sets to false mEmptyRoom in order not to introduce other false FakeDataUtils devices because this being already completed
               mEmptyRoom=false;
               showMyRoomDataView();

           } else {
                //add all device from this new room
                //mEmptyRoom it sets to true to enter false FakeDataUtils devices
               mEmptyRoom=true;
               showMyRoomDataView();
           }
       } else {
           String msgnodata = getResources().getString(R.string.msg_room_no_data);
           Toast.makeText(EditRoomActivity.this,msgnodata,Toast.LENGTH_SHORT).show();
           mLoadingIndicator.setVisibility(View.INVISIBLE);
       }
    }

    //show the data on display in recyclerview
    private void showMyRoomDataView() {
        //if it is default 00000 code for new room then add initial name and code My Roome and room001
        if (mCodeRoomPref.equals(getResources().getString(R.string.pref_code_room_default))) {
            editTextRoomName.setText(getResources().getString(R.string.name_first_room));
            editTextRoomCode.setText(getResources().getString(R.string.code_first_room));

        } else {
            // showing the data
            editTextRoomName.setText(mNameRoomPref);
            editTextRoomCode.setText(mCodeRoomPref);

        }
        // hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
       //make the device data visible
        mRoomRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMyHouseRoomAdapter.swapCursor(null);
    }


    @Override
    public void onClick(String id_device,String value) {
       //Calls up the procedure to update not sensor type device
        int no_update=UpdateDeviceClickImage(id_device,value);
        getSupportLoaderManager().restartLoader(ID_MYROOM_LOADER, null, EditRoomActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        //set code room in the Preferences  pref_code_room_key
        editor.putString(getResources().getString(R.string.pref_code_room_key), mCodeRoomPref);
        editor.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    //The procedure to be used to update the status on or off for non sensor type devices when the switch is pressed down on the image
    public int UpdateDeviceClickImage(String id_device,String value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_VALUE,value);
        Uri Uriupdatedataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
        Uriupdatedataroom = Uriupdatedataroom.buildUpon().appendPath(id_device).build();
        int update = getContentResolver().update(Uriupdatedataroom,contentValues,null,null);
        if (update>0) {
            String msg_update = getResources().getString(R.string.msg_device_set) + " "+value;
            Toast.makeText(this, msg_update,Toast.LENGTH_SHORT).show();
        }
        return update;//for future implementations.....
    }


    //The procedure to be used to delete the room with all device with the same room code
    public int DeleteRoom(){
        String selectionDevicesRooms = MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE+" LIKE '" +mCodeHousePref+ "' and "+
                MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE+" LIKE '" + mCodeUserPref+ "' and "+
                MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE+" LIKE '" + mCodeRoomPref+"'";

        Uri Uriupdatedataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
        int delete = getContentResolver().delete(Uriupdatedataroom,selectionDevicesRooms,null);
        //if deleted then is display number or devices deleted
        if (delete>0) {
            String msq_delete = getResources().getString(R.string.msg_delete_room) + "("+delete+")";
            Toast.makeText(this, msq_delete,Toast.LENGTH_SHORT).show();
        }
        return delete;//for future implementations.....
    }


    //The procedure to be used to update the room. All devices is update with new code and name of the room
    public int UpdateRoom(String old_code_room,String new_name_room){
        String selectionDevicesRoom = MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE+" LIKE '" +mCodeHousePref+ "' and "+
                MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE+" LIKE '" + mCodeUserPref+ "' and "+
                MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE+" LIKE '" + old_code_room+"'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_NAME,new_name_room);
        Uri Uriupdatedataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
        int update = getContentResolver().update(Uriupdatedataroom,contentValues,selectionDevicesRoom,null);
        //if updated then is display number or devices updated
        if (update>0) {
            String msq_update = getResources().getString(R.string.msg_update_room) + "("+update+")";
            Toast.makeText(this, msq_update,Toast.LENGTH_SHORT).show();
        }
        return update;//for future implementations.....
    }

}
