package ro.diamondtech.myhousereply.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ro.diamondtech.myhousereply.data.MyHouseRoomContract.MyHouseRoomEntry;

/**
 * Created by user1 on 24/01/2018.
 */

public class MyHouseReplyDBHelper extends SQLiteOpenHelper{

    // the name of myhouse database (only for device for now)
    public static final String DATABASE_NAME = "myhousereplydevice.db";

    // now version of database is set to 1 then 2 then 3 if I change database structure. That until I uninstall app from device then I start with 1 again.
    private static final int DATABASE_VERSION = 1;

    //constructor
    public MyHouseReplyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create de sql table and use MyHouseContract entries
        final String SQL_CREATE_MYHOUSE_TABLE =

                "CREATE TABLE " + MyHouseRoomEntry.TABLE_NAME + " (" +


                        MyHouseRoomEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MyHouseRoomEntry.COLUMN_DEVICE_CODE       + " TEXT NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_NO       + " INTEGER NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_HOUSE_CODE       + " TEXT NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_HOUSE_NAME       + " TEXT NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_TYPE       + " TEXT NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_NAME       + " TEXT NOT NULL, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_DETAILS       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_ORDER       + " INTEGER , "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_SELECT       + " INTEGER , "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_STATE       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_PRE_STATE_       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_VALUE       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_PRE_VALUE       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_UM       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_ON       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_IMAGE_NAME       + " TEXT, "                 +

                        MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_OFF + " TEXT,"                  +

                        MyHouseRoomEntry.COLUMN_ROOM_CODE + " TEXT,"                  +

                        MyHouseRoomEntry.COLUMN_ROOM_NAME + " TEXT,"                  +

                        MyHouseRoomEntry.COLUMN_USER_CODE + " TEXT,"                  +

                        MyHouseRoomEntry.COLUMN_USER_NAME + " TEXT,"                  +

                        MyHouseRoomEntry.COLUMN_DEVICE_UPDATE_DATE   + " NUMERIC); ";


        // create real databases on local
        sqLiteDatabase.execSQL(SQL_CREATE_MYHOUSE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyHouseRoomEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
