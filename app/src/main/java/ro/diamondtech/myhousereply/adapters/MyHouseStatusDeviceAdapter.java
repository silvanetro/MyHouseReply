package ro.diamondtech.myhousereply.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ro.diamondtech.myhousereply.EditRoomActivity;
import ro.diamondtech.myhousereply.StatusActivity;
import ro.diamondtech.myhousereply.data.MyHouseRoomContract;
import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 25/01/2018.
 */

public class MyHouseStatusDeviceAdapter extends RecyclerView.Adapter<MyHouseStatusDeviceAdapter.MyHouseStatusAdapterViewHolder> {


    private final Context mContext;
    private Cursor mCursor;
    private String mCodeRoom;
    private final MyHouseStatusDeviceAdapterOnClickHandler mClickHandler;

    public interface MyHouseStatusDeviceAdapterOnClickHandler {
        void ondeviceClick(String id_device,String value);
    }

    //constructor
    public MyHouseStatusDeviceAdapter(Context context, MyHouseStatusDeviceAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }






   public void swapCursor(Cursor newCursor,String coderoom) {
        mCursor = newCursor;
        mCodeRoom = coderoom;
        notifyDataSetChanged();
    }

    //intro layout
    @Override
    public MyHouseStatusAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.activity_detail_object_house, parent, false);
        view.setFocusable(true);
        return new MyHouseStatusAdapterViewHolder(view);
    }


    //display data in every item
    @Override
    public void onBindViewHolder(MyHouseStatusAdapterViewHolder statusholder, int position) {

        //If they are not given in the position then exit
        if (!mCursor.moveToPosition(position)) return;

        String room_code = mCursor.getString(StatusActivity.INDEX_ROOM_CODE);

        //If the code room is not the same as the one sent then then view is gone
        if (!mCodeRoom.equals(room_code)) {
            statusholder.txtViewDeviceNameStatus.setVisibility(View.GONE);
            statusholder.imgViewBoxImageDeviceStatus.setVisibility(View.GONE);
            statusholder.viewLineHoriz.setVisibility(View.GONE);
            statusholder.constraintLayoutdevice.setVisibility(View.GONE);
            statusholder.constraintLayoutdevice.setVisibility(View.GONE);
            return;
        }
            //for hidden device
            final ConstraintLayout constraintLayoutdevice;
            final  TextView   txtViewDeviceNameStatus;

            final ImageView imgViewBoxImageDeviceStatus;
            final View viewLineHoriz;

            // Items for showing in ViewHolder;
            final TextView txtdevice_name;
            final ImageView imgdevice_image;
            final TextView txtdevice_value;


        // Read data from the cursor
        String device_value = mCursor.getString(StatusActivity.INDEX_DEVICE_VALUE);
        String device_um = mCursor.getString(StatusActivity.INDEX_DEVICE_UM);
        String device_name = mCursor.getString(StatusActivity.INDEX_DEVICE_NAME);
        String device_details = mCursor.getString(StatusActivity.INDEX_DEVICE_DETAILS);
        String device_state = mCursor.getString(StatusActivity.INDEX_DEVICE_STATE);
        int device_select = mCursor.getInt(StatusActivity.INDEX_DEVICE_SELECT);
        String device_limit_on = mCursor.getString(StatusActivity.INDEX_DEVICE_LIMIT_ON);
        String device_limit_off = mCursor.getString(StatusActivity.INDEX_DEVICE_LIMIT_OFF);
        String device_image_name = mCursor.getString(StatusActivity.INDEX_DEVICE_IMAGE_NAME);
        String device_code = mCursor.getString(StatusActivity.INDEX_DEVICE_CODE);
        String device_type = mCursor.getString(EditRoomActivity.INDEX_DEVICE_TYPE);

        //showing the name
        statusholder.txtdevice_name.setText(device_name);

        statusholder.imgdevice_image.setImageResource(mContext.getResources().getIdentifier(device_image_name, "drawable", mContext.getPackageName()));
        int color_text_green=mContext.getResources().getColor(R.color.colorTextOk);
        int color_text_yellow=mContext.getResources().getColor(R.color.colorTextAtention);
        int color_image_green=mContext.getResources().getColor(R.color.colorGood);
        int color_image_yellow= color_text_yellow;
        int color_inactive=mContext.getResources().getColor(R.color.colorTextInactiv);


        //test if device is on line (active) then change color of the device image
        if (device_state.equals(mContext.getResources().getString(R.string.device_status_on))) {
            //if is not sensor type and online
            if (device_type.equals(mContext.getResources().getString(R.string.title_device_exec))){
                //if is OFF then set green color
                if (device_value.equals(mContext.getResources().getString(R.string.device_limit_off))) {
                    statusholder.imgdevice_image.setColorFilter(color_image_green);
                    statusholder.txtdevice_value.setTextColor(color_text_green);

                } else {
                    //if is ON then set yellow color
                    statusholder.imgdevice_image.setColorFilter(color_image_yellow);
                    statusholder.txtdevice_value.setTextColor(color_image_yellow);
                }
            } else {
                    //if is sensor type and on line set yellow for text and green for image
                    statusholder.imgdevice_image.setColorFilter(color_image_green);
                    statusholder.txtdevice_value.setTextColor(color_image_yellow);
             }
            //show de value and units
            statusholder.txtdevice_value.setText(device_value + " " + device_um);
        } else {
            //if offline set inactive color
            statusholder.imgdevice_image.setColorFilter(color_inactive);
            statusholder.txtdevice_value.setTextColor(color_inactive);
            String no_value=mContext.getResources().getString(R.string.device_status_no_value);
            statusholder.txtdevice_value.setText(no_value+" / "+no_value);

        }
    }



    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    //cache child view with items
    class MyHouseStatusAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //for hidden device
        final ConstraintLayout constraintLayoutdevice;
        final  TextView   txtViewDeviceNameStatus;

        final ImageView imgViewBoxImageDeviceStatus;
        final View viewLineHoriz;

       // Items for display in ViewHolder;
        final TextView txtdevice_name;
        final ImageView imgdevice_image;
        final TextView txtdevice_value;

        //constructor
        MyHouseStatusAdapterViewHolder(View view) {
            super(view);

        //for view device
            constraintLayoutdevice = (ConstraintLayout) view.findViewById(R.id.constraint_status_device);
            txtViewDeviceNameStatus = (TextView) view.findViewById(R.id.textViewDeviceNameStatus);
            imgViewBoxImageDeviceStatus = (ImageView) view.findViewById(R.id.imageViewBoxImageDeviceStatus);
            viewLineHoriz = (View) view.findViewById(R.id.viewLineHoriz);

            txtdevice_name = (TextView) view.findViewById(R.id.textViewDeviceNameStatus);
            imgdevice_image = (ImageView) view.findViewById(R.id.imageViewImageDeviceStatus);
            txtdevice_value = (TextView) view.findViewById(R.id.textViewValueDeviceStatus);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
         //if is not sensor type change colors
            if (mCursor.getString(EditRoomActivity.INDEX_DEVICE_TYPE).equals(mContext.getResources().getString(R.string.title_device_exec))) {
                //if device state is ON then change to OFF and reverse
                String real_state = mCursor.getString(EditRoomActivity.INDEX_DEVICE_VALUE);
                String state_on = mContext.getResources().getString(R.string.device_limit_on);
                String state_off = mContext.getResources().getString(R.string.device_limit_off);
                if (real_state.equals(state_on)){
                    real_state = state_off;
                } else {
                    real_state = state_on;
                }

                int id = mCursor.getInt(0);
                //send to click procedure in EditRoomActivity
                mClickHandler.ondeviceClick(Integer.toString(id),real_state);
                v.requestFocus();
            }

        }

    }




}
