package ro.diamondtech.myhousereply.models;

import android.content.Context;

import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 31/01/2018.
 */

//Sets the properties of the devices used in custom system for initial introduction
public class MyHouseCustomDevices {

    private Context mContext;

    public   String DEVICE_ROOM_CODE ;

    public   String DEVICE_ROOM_NAME ;

    public   String DEVICE_NO ;

    public   String DEVICE_HOUSE_CODE;

    public   String DEVICE_HOUSE_NAME ;

    private   String DEVICE_CODE ;

    public    String DEVICE_CODE_UNIQUE ;

    public   String DEVICE_TYPE ;

    public   String DEVICE_NAME ;

    public   String DEVICE_DETAILS ;

    public   int DEVICE_ORDER ;

    public   int DEVICE_SELECT;

    public   String DEVICE_STATE ;

    public   String DEVICE_PRE_STATE_;

    public   String DEVICE_VALUE;

    public   String DEVICE_PRE_VALUE ;

    public   String DEVICE_UM ;

    public   String DEVICE_LIMIT_ON ;

    public   String DEVICE_LIMIT_OFF ;

    public   String DEVICE_USER_CODE ;

    public   String DEVICE_USER_NAME ;

    public   String DEVICE_UPDATE_DATE;

    public   String DEVICE_IMAGE_NAME ;

    public   String DEVICE_IMAGE_COLOR_ONLINE ;

    public   String DEVICE_IMAGE_COLOR_OFFLINE ;

    public MyHouseCustomDevices(Context context, String deviceModel, String[] identity, int device_no){
        this.mContext = context;

        //only 5 devices is known in Custom system

        if (deviceModel.equals(context.getResources().getString(R.string.object_bulb))){
            setDeviceBulbProperties();
        }

        if (deviceModel.equals(context.getResources().getString(R.string.object_temperature))){
            setDeviceTemperatureProperties();
        }

        if (deviceModel.equals(context.getResources().getString(R.string.object_gas))){
            setDeviceGasProperties();
        }

        if (deviceModel.equals(context.getResources().getString(R.string.object_soil_humidity))){
            setDeviceSoilHumidityProperties();
        }

        if (deviceModel.equals(context.getResources().getString(R.string.object_yale))){
            setDeviceDoorProperties();
        }


        setIdentity(identity);
        this.DEVICE_CODE_UNIQUE=setCode(device_no);

    }



    //Device identification data sets
    public void setIdentity (String[] identity){
        this.DEVICE_USER_CODE = identity[0];
        this.DEVICE_USER_NAME = identity[1];

        this.DEVICE_HOUSE_CODE = identity[2];
        this.DEVICE_HOUSE_NAME = identity[3];

        this.DEVICE_ROOM_CODE = identity[4];
        this.DEVICE_ROOM_NAME = identity[5];


    }

    //Set the order in which to appear in the list of devices (not implemented yet....)
    public void setOrder(int number){
        this.DEVICE_ORDER = number;
    }

    // codul se seteaza ca fiind partea de DEVICE_CODE din propietatile device si numarul maxim de dispoitive existent in camera (trimis la introducerea dispozotivului)
    public String setCode(int number){
        String codUnicDevice;
        this.DEVICE_NO = Integer.toString(number);
        codUnicDevice = this.DEVICE_CODE+this.DEVICE_NO;
        return codUnicDevice;
    }


    public void setDeviceBulbProperties() {
        this.DEVICE_NAME = mContext.getResources().getString(R.string.object_bulb);
        this.DEVICE_DETAILS = mContext.getResources().getString(R.string.object_bulb_description);
        this.DEVICE_TYPE = mContext.getResources().getString(R.string.title_device_exec);
        this.DEVICE_UM = "";
        this.DEVICE_STATE= mContext.getResources().getString(R.string.device_status_on);
        this.DEVICE_PRE_STATE_= mContext.getResources().getString(R.string.device_status_off);
        this.DEVICE_VALUE="OFF";
        this.DEVICE_PRE_VALUE="OFF";
        this.DEVICE_IMAGE_NAME="ic_bec";
        this.DEVICE_LIMIT_ON = "ON";
        this.DEVICE_LIMIT_OFF = "OFF";
        this.DEVICE_SELECT = 1;
        this.DEVICE_CODE="10001";//This is the device code (which should be unique! for the system used)
                                 //and add the serial number of the room in the subsequent procedure

    }






    public void setDeviceTemperatureProperties() {
        this.DEVICE_NAME = mContext.getResources().getString(R.string.object_temperature);
        this.DEVICE_DETAILS = mContext.getResources().getString(R.string.object_temperature_description);
        this.DEVICE_TYPE = mContext.getResources().getString(R.string.title_device_sensor);
        this.DEVICE_UM = " C ";
        this.DEVICE_STATE= mContext.getResources().getString(R.string.device_status_on);
        this.DEVICE_PRE_STATE_= mContext.getResources().getString(R.string.device_status_off);
        this.DEVICE_VALUE="25";
        this.DEVICE_PRE_VALUE="24";
        this.DEVICE_IMAGE_NAME="ic_temperatura_mica";
        this.DEVICE_LIMIT_ON = "0";
        this.DEVICE_LIMIT_OFF = "28";
        this.DEVICE_SELECT = 1;
        this.DEVICE_CODE="10002";//This is the device code (which should be unique! for the system used)
                                //and add the serial number of the room in the subsequent procedure


    }


    public void setDeviceGasProperties() {
        this.DEVICE_NAME = mContext.getResources().getString(R.string.object_gas);
        this.DEVICE_DETAILS = mContext.getResources().getString(R.string.object_gas_description);
        this.DEVICE_TYPE = mContext.getResources().getString(R.string.title_device_sensor);
        this.DEVICE_UM = " % ";
        this.DEVICE_STATE= mContext.getResources().getString(R.string.device_status_on);
        this.DEVICE_PRE_STATE_= mContext.getResources().getString(R.string.device_status_off);
        this.DEVICE_VALUE="0";
        this.DEVICE_PRE_VALUE="24";
        this.DEVICE_IMAGE_NAME="ic_gaz_if_fire";
        this.DEVICE_LIMIT_ON = "20";
        this.DEVICE_LIMIT_OFF = "5";
        this.DEVICE_SELECT = 1;
        this.DEVICE_CODE="10003";//This is the device code (which should be unique! for the system used)
                                //and add the serial number of the room in the subsequent procedure


    }


    public void setDeviceSoilHumidityProperties() {
        this.DEVICE_NAME = mContext.getResources().getString(R.string.object_soil_humidity);
        this.DEVICE_DETAILS = mContext.getResources().getString(R.string.object_soil_humidity_description);
        this.DEVICE_TYPE = mContext.getResources().getString(R.string.title_device_sensor);
        this.DEVICE_UM = " % ";
        this.DEVICE_STATE= mContext.getResources().getString(R.string.device_status_off);
        this.DEVICE_PRE_STATE_= mContext.getResources().getString(R.string.device_status_off);
        this.DEVICE_VALUE="68";
        this.DEVICE_PRE_VALUE="75";
        this.DEVICE_IMAGE_NAME="ic_umiditate_plante";
        this.DEVICE_LIMIT_ON = "35";
        this.DEVICE_LIMIT_OFF = "35";
        this.DEVICE_SELECT = 0;
        this.DEVICE_CODE="10004";//This is the device code (which should be unique! for the system used)
                                //and add the serial number of the room in the subsequent procedure

    }

    public void setDeviceDoorProperties() {
        this.DEVICE_NAME = mContext.getResources().getString(R.string.object_yale);
        this.DEVICE_DETAILS = mContext.getResources().getString(R.string.object_yale_description);
        this.DEVICE_TYPE = mContext.getResources().getString(R.string.title_device_exec);
        this.DEVICE_UM = "";
        this.DEVICE_STATE= mContext.getResources().getString(R.string.device_status_on);
        this.DEVICE_PRE_STATE_= mContext.getResources().getString(R.string.device_status_on);
        this.DEVICE_VALUE="OFF";
        this.DEVICE_PRE_VALUE="OFF";
        this.DEVICE_IMAGE_NAME="ic_incuietoare";
        this.DEVICE_LIMIT_ON = "ON";
        this.DEVICE_LIMIT_OFF = "OFF";
        this.DEVICE_SELECT = 1;
        this.DEVICE_CODE="10005";//This is the device code (which should be unique! for the system used)
                                 //and add the serial number of the room in the subsequent procedure

    }

}
