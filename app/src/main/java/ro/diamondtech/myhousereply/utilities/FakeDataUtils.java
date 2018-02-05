package ro.diamondtech.myhousereply.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.diamondtech.myhousereply.data.MyHouseRoomContract;
import ro.diamondtech.myhousereply.models.MyHouseCustomDevices;

/**
 * Created by user1 on 25/01/2018.
 */

//Because it is not implemented with real devices are inserted fictitious initial data

public class FakeDataUtils {

// get the model name from @string/object_.... ,


    public static void insertFakeDataDevice(Context context,String modeldevice,String[] identity,int device_no){

    MyHouseCustomDevices newdevice = new MyHouseCustomDevices(context,modeldevice,identity,device_no);


    ContentValues fakeMyHouseValues = new ContentValues();
    //Get today's normalized date for COLUMN_DEVICE_UPDATE_DATE (not implemented yet.....)
    long today = MyHouseDateUtils.normalizeDate(System.currentTimeMillis());

    List<ContentValues> fakeValues = new ArrayList<ContentValues>();
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_CODE,newdevice.DEVICE_CODE_UNIQUE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_TYPE,newdevice.DEVICE_TYPE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_CODE, newdevice.DEVICE_HOUSE_CODE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_HOUSE_NAME,newdevice.DEVICE_HOUSE_NAME);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_CODE,newdevice.DEVICE_ROOM_CODE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_ROOM_NAME,newdevice.DEVICE_ROOM_NAME);
    //set order display for every device (not implemented yet....)
    try {
       int dev_no = Integer.parseInt(newdevice.DEVICE_NO);
        fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_NO,dev_no);
    } catch(NumberFormatException nfe) {
        fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_NO, 0);
    }
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_NAME, newdevice.DEVICE_NAME);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_DETAILS, newdevice.DEVICE_DETAILS);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_CODE, newdevice.DEVICE_USER_CODE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_USER_NAME, newdevice.DEVICE_USER_NAME);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_IMAGE_NAME,newdevice.DEVICE_IMAGE_NAME);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_SELECT, newdevice.DEVICE_SELECT);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_STATE, newdevice.DEVICE_STATE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_PRE_STATE_, newdevice.DEVICE_PRE_STATE_);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_VALUE, newdevice.DEVICE_VALUE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_PRE_VALUE,newdevice.DEVICE_PRE_VALUE);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_UM, newdevice.DEVICE_UM);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_ON, newdevice.DEVICE_LIMIT_ON);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_LIMIT_OFF, newdevice.DEVICE_LIMIT_OFF);
    fakeMyHouseValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_UPDATE_DATE, "25.01.2018 : 19.39");

    fakeValues.add(fakeMyHouseValues);
    // bulkInsert our new device data into custom database
    context.getContentResolver().bulkInsert(
            MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI,
            fakeValues.toArray(new ContentValues[1]));



}




}
