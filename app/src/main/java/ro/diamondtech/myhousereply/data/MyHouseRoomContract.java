package ro.diamondtech.myhousereply.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by user1 on 24/01/2018.
 */

//data for connect to SQLite database
public class MyHouseRoomContract {
   // this is is a name for the my content provider
    public  static final String CONTENT_AUTHORITY = "ro.diamondtech.myhousereply";

    // URI to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    //this is the PATH side
    public static final String PATH_MYHOUSEREPLY = "myhousereply";


    //class to defines the table contents
    public static final class MyHouseRoomEntry implements BaseColumns {

        //add the PATH for the base URI used to query the Weather table from the content provider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYHOUSEREPLY).build();

        //the name for the table of myhouse data where is only data for devices
        public static final String TABLE_NAME = "myhousereplydevice";

        //enum colums in the table
     //   public static final String COLUMN_DEVICE_ID = "_id";
        public static final String COLUMN_ROOM_CODE = "cod_camera";

        public static final String COLUMN_ROOM_NAME = "nume_camera";

        public static final String COLUMN_DEVICE_NO = "cod_nr_dispozitiv";

        public static final String COLUMN_HOUSE_CODE = "cod_casa_dispozitiv";

        public static final String COLUMN_HOUSE_NAME = "nume_casa_dispozitiv";

        public static final String COLUMN_DEVICE_CODE = "cod_dispozitiv";

        public static final String COLUMN_DEVICE_TYPE = "tip_dispozitiv";

        public static final String COLUMN_DEVICE_NAME = "denumire_dispozitiv";

        public static final String COLUMN_DEVICE_DETAILS = "descriere_dispozitiv";

        public static final String COLUMN_DEVICE_ORDER = "ordine_dispozitiv";

        public static final String COLUMN_DEVICE_SELECT = "activat";

        public static final String COLUMN_DEVICE_STATE = "stare_actuala_dispozitiv";

        public static final String COLUMN_DEVICE_PRE_STATE_ = "stare_anterioara_dispozitiv";

        public static final String COLUMN_DEVICE_VALUE = "valoare_actuala_dispozitiv";

        public static final String COLUMN_DEVICE_PRE_VALUE = "valoare_anterioara_dispozitiv";

        public static final String COLUMN_DEVICE_UM = "um";

        public static final String COLUMN_DEVICE_LIMIT_ON = "valoare_limita_pt_on";

        public static final String COLUMN_DEVICE_LIMIT_OFF = "valoare_limita_pt_off";

        public static final String COLUMN_USER_CODE = "cod_user";

        public static final String COLUMN_USER_NAME = "nume_user";

        public static final String COLUMN_DEVICE_UPDATE_DATE = "ultima_actualizare";

        public static final String COLUMN_DEVICE_IMAGE_NAME = "imagine_dispozitiv";


    }

}
