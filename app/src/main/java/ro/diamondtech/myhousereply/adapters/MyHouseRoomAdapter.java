package ro.diamondtech.myhousereply.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import ro.diamondtech.myhousereply.EditRoomActivity;
import ro.diamondtech.myhousereply.StatusActivity;
import ro.diamondtech.myhousereply.data.MyHouseRoomContract;
import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

public class MyHouseRoomAdapter extends RecyclerView.Adapter<MyHouseRoomAdapter.MyHouseRoomAdapterViewHolder> {


    private final Context mContext;
    private Cursor mCursor;
    private final MyHouseRoomAdapterOnClickHandler mClickHandler;



    public interface MyHouseRoomAdapterOnClickHandler {
        void onClick(String idDevice, String value);
    }

    //constructor
    public MyHouseRoomAdapter (Context context,MyHouseRoomAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }


    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
       notifyDataSetChanged();
    }

    //intro layout
    @Override
    public MyHouseRoomAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.activity_detail_object_room, parent, false);

        view.setFocusable(true);

        return new MyHouseRoomAdapterViewHolder(view);
    }


    //display data in every item
    @Override
    public void onBindViewHolder(MyHouseRoomAdapterViewHolder roomholder, int position) {
        //daca nu sunt date in pozitia position atunci exit
        if (!mCursor.moveToPosition(position)) return;

        // Read data from the cursor
        String device_value = mCursor.getString(EditRoomActivity.INDEX_DEVICE_VALUE);
        String device_um = mCursor.getString(EditRoomActivity.INDEX_DEVICE_UM);
        String device_name = mCursor.getString(EditRoomActivity.INDEX_DEVICE_NAME);
        String device_details = mCursor.getString(EditRoomActivity.INDEX_DEVICE_DETAILS);
        String device_state = mCursor.getString(EditRoomActivity.INDEX_DEVICE_STATE);
        int device_select = mCursor.getInt(EditRoomActivity.INDEX_DEVICE_SELECT);
        String device_limit_on = mCursor.getString(EditRoomActivity.INDEX_DEVICE_LIMIT_ON);
        String device_limit_off = mCursor.getString(EditRoomActivity.INDEX_DEVICE_LIMIT_OFF);
        String device_image_name = mCursor.getString(EditRoomActivity.INDEX_DEVICE_IMAGE_NAME);
        String device_code = mCursor.getString(EditRoomActivity.INDEX_DEVICE_CODE);
        String device_type = mCursor.getString(EditRoomActivity.INDEX_DEVICE_TYPE);
        String room_code = mCursor.getString(EditRoomActivity.INDEX_ROOM_CODE);

         //showing on items
        roomholder.txtdevice_name.setText(device_name);
        roomholder.txtdevice_details.setText(device_details);
        roomholder.imgdevice_image.setImageResource(mContext.getResources().getIdentifier(device_image_name, "drawable", mContext.getPackageName()));
        int color_text_green=mContext.getResources().getColor(R.color.colorTextOk);
        int color_text_yellow=mContext.getResources().getColor(R.color.colorTextAtention);
        int color_image_green=mContext.getResources().getColor(R.color.colorGood);
        int color_image_yellow= color_text_yellow;
        int color_inactive=mContext.getResources().getColor(R.color.colorTextInactiv);
        //if the device is online set yellow text
        //test if device is on line (active) if online then change color of the device image
        if (device_state.equals(mContext.getResources().getString(R.string.device_status_on))){
            roomholder.txtdevice_status.setTextColor(color_text_yellow);
            if (device_type.equals(mContext.getResources().getString(R.string.title_device_exec))){
                //if the device is online and OFF set green text and image
                if (device_value.equals(mContext.getResources().getString(R.string.device_limit_off))) {
                    roomholder.imgdevice_image.setColorFilter(color_image_green);
                    roomholder.txtdevice_value.setTextColor(color_text_green);
                    roomholder.txtdevice_um.setTextColor(color_text_green);
                } else {
                    //if the device is online and ON set yellow text and image
                    roomholder.imgdevice_image.setColorFilter(color_image_yellow);
                    roomholder.txtdevice_value.setTextColor(color_text_yellow);
                    roomholder.txtdevice_um.setTextColor(color_text_yellow);
                }
            } else {
               // if the device is sensor type, online and ON set yellow text
                roomholder.imgdevice_image.setColorFilter(color_image_green);
                roomholder.txtdevice_value.setTextColor(color_text_yellow);
                roomholder.txtdevice_um.setTextColor(color_text_yellow);
            }
            //set value
            roomholder.txtdevice_value.setText(device_value);
            roomholder.txtdevice_um.setText(device_um);

        } else {
            //if the device is off line set inactive colors and values
            roomholder.imgdevice_image.setColorFilter(color_inactive);
            roomholder.txtdevice_status.setTextColor(color_inactive);
            roomholder.txtdevice_value.setTextColor(color_inactive);
            roomholder.txtdevice_um.setTextColor(color_inactive);
            roomholder.txtdevice_value.setText(mContext.getResources().getString(R.string.device_status_no_value)+" /");
            roomholder.txtdevice_um.setText(mContext.getResources().getString(R.string.device_status_no_value));
        }
        //set the device state
        roomholder.txtdevice_status.setText(device_state);

        //test if device is selected in this room
        boolean selected =false;
        if (device_select==1) selected=true;
        roomholder.switchdevice_select.setChecked(selected);
        //set the limits for alarm and notifications
        roomholder.editdevice_on.setText(device_limit_on.toString());
        roomholder.editdevice_off.setText(device_limit_off.toString());

        //test if device_value is into limits (not implemented yet...)
        //.............................//

        roomholder.txtdevice_code.setText(device_code);

    }



    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    //cache child view with items
     class MyHouseRoomAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


       // Items for display in ViewHolder;
        final TextView txtdevice_name;
        final ImageView imgdevice_image;
        final TextView txtdevice_status;
        final TextView txtdevice_code;
        final Switch switchdevice_select;
        final TextView txtdevice_details;
        final EditText editdevice_on;
        final EditText editdevice_off;
        final TextView txtdevice_value;
        final TextView txtdevice_um;

        //constructor
        MyHouseRoomAdapterViewHolder(View view) {
            super(view);

           // weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);
            txtdevice_name = (TextView) view.findViewById(R.id.textViewDeviceName);
            imgdevice_image = (ImageView) view.findViewById(R.id.imageViewImageDevice);
            txtdevice_status = (TextView) view.findViewById(R.id.textViewDeviceStatus1);
            txtdevice_code = (TextView) view.findViewById(R.id.textViewDeviceCode);
            switchdevice_select = (Switch) view.findViewById(R.id.switchSelectDivece1);
            txtdevice_details = (TextView) view.findViewById(R.id.textViewDetailDevice);
            editdevice_on = (EditText) view.findViewById(R.id.editTextLimitON);
            editdevice_off = (EditText) view.findViewById(R.id.editTextLimitOFF);
            txtdevice_value = (TextView) view.findViewById(R.id.textViewValueDevice);
            txtdevice_um = (TextView) view.findViewById(R.id.textViewUMDevice);

            //set the device ON or OFF if is not sensor type
            view.setOnClickListener(this);
            switchdevice_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     int adapterPosition = getAdapterPosition();
                    mCursor.moveToPosition(adapterPosition);
                    int id_device = mCursor.getInt(0);
                    int disp_activ=mCursor.getInt(EditRoomActivity.INDEX_DEVICE_SELECT);
                    if (isChecked & disp_activ==0) {
                        int update = UpdateDeviceSelected(Integer.toString(id_device),1);
                   }
                    if (!isChecked & disp_activ==1){
                        int update = UpdateDeviceSelected(Integer.toString(id_device),0);
                    }


                }
            });
        }

       //not yet implemented : Limits for device editText onChange
        //............................//




        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param view the View that was clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);


            //If there is not sensor type device when it may change color to click
            if (mCursor.getString(EditRoomActivity.INDEX_DEVICE_TYPE).equals(mContext.getResources().getString(R.string.title_device_exec))) {
                //if device state is ON then change to OFF and store
                String real_state = mCursor.getString(EditRoomActivity.INDEX_DEVICE_VALUE);
                String state_on = mContext.getResources().getString(R.string.device_limit_on);
                String state_off = mContext.getResources().getString(R.string.device_limit_off);
                if (real_state.equals(state_on)){
                    real_state = state_off;
                } else {
                    real_state = state_on;
                }
                //get _ID from database for this device
                int id = mCursor.getInt(0);
                //send to click procedure in EditRoomActivity
                mClickHandler.onClick(Integer.toString(id),real_state);

            }
        }
    }

    //The procedure used to update the status on or off for devices when image is pressed down
    public int UpdateDeviceSelected(String id_device,int value){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_SELECT,value);
        Uri Uriupdatedataroom = MyHouseRoomContract.MyHouseRoomEntry.CONTENT_URI;
        Uriupdatedataroom = Uriupdatedataroom.buildUpon().appendPath(id_device).build();
        int update = mContext.getApplicationContext().getContentResolver().update(Uriupdatedataroom,contentValues,null,null);
        String mesaj_update=mContext.getResources().getString(R.string.msg_no_update_room);
        //if is same update..
        if (update>0) {
            switch (value) {
                case 0: mesaj_update= mContext.getResources().getString(R.string.msg_no_showing_device_room);
                    break;
                case 1:mesaj_update= mContext.getResources().getString(R.string.msg_showing_device_room);
                    break;
            }
        }
        Toast.makeText(mContext, mesaj_update,Toast.LENGTH_SHORT).show();
        return update;
    }


}
