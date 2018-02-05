package ro.diamondtech.myhousereply.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class MyHouseStatusRoomAdapter extends RecyclerView.Adapter<MyHouseStatusRoomAdapter.MyHouseStatusRoomAdapterViewHolder>
        implements  MyHouseStatusDeviceAdapter.MyHouseStatusDeviceAdapterOnClickHandler
{


    private final Context mContext;
    private Cursor mCursorRoom;
    private int mRoomPosition = RecyclerView.NO_POSITION;

    private Cursor mCursorDevice;
    private final MyHouseStatusRoomAdapterOnClickHandler mClickHandler;
    private  MyHouseStatusDeviceAdapter itemListDeviceAdapter;



    public interface MyHouseStatusRoomAdapterOnClickHandler {
        void onClick();
        void ondeviceClick(String id_device,String value);
    }

    //constructor
    public MyHouseStatusRoomAdapter(Context context, MyHouseStatusRoomAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public void ondeviceClick(String id_device,String value) {
        mClickHandler.ondeviceClick(id_device,value);
    }




    public void swapCursor(Cursor newRoomCursor,Cursor newDeviceCursor) {
        mCursorRoom = newRoomCursor;
        mCursorDevice = newDeviceCursor;
        notifyDataSetChanged();

    }

    //intro layout
    @Override
    public MyHouseStatusRoomAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.activity_detail_rooms, parent, false);

        view.setFocusable(true);
        return new MyHouseStatusRoomAdapterViewHolder(view);
    }


    //display data in every item
    @Override
    public void onBindViewHolder(MyHouseStatusRoomAdapterViewHolder statusholder, int position) {

        if (!mCursorRoom.moveToPosition(position)) return;

         // Read data from the cursor
        String room_code = mCursorRoom.getString(StatusActivity.INDEX_ROOM_ROOM_CODE);
        String room_name = mCursorRoom.getString(StatusActivity.INDEX_ROOM_ROOM_NAME);

        //showing the items
        statusholder.txtroom_name.setText(room_name);
        statusholder.txtroom_code.setText(room_code);

        //set adapter for devices
        itemListDeviceAdapter = new MyHouseStatusDeviceAdapter(mContext, this);

        LinearLayoutManager layoutStatusDeviceManager;
        layoutStatusDeviceManager = new LinearLayoutManager( mContext,LinearLayoutManager.HORIZONTAL,false);

        statusholder.rw_device_room.setLayoutManager(layoutStatusDeviceManager);
       // statusholder.rw_device_room.getLayoutManager().scrollToPosition(10);
      //  statusholder.rw_device_room.setHasFixedSize(false);
       // statusholder.rw_device_room.setNestedScrollingEnabled(true);

        statusholder.rw_device_room.setAdapter(itemListDeviceAdapter);




        itemListDeviceAdapter.swapCursor(mCursorDevice,room_code);

    }

    @Override
    public int getItemCount() {
        if (null == mCursorRoom) return 0;
        return mCursorRoom.getCount();
    }

    //cache child view with items
    class MyHouseStatusRoomAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


       // Items for display in ViewHolder;
        final TextView txtroom_name;
        final ImageView imgroom_image;

        //get recyclerview for devices
        RecyclerView rw_device_room;

        final TextView txtroom_code;

        //constructor
        MyHouseStatusRoomAdapterViewHolder(View view) {
            super(view);

            this.txtroom_name = (TextView) view.findViewById(R.id.textViewRoomNameStatus);
            this.imgroom_image = (ImageView) view.findViewById(R.id.imageViewImageRoomStatus);

            //set layout for devices in room
            this.rw_device_room= (RecyclerView) view.findViewById(R.id.recyclerview_status_device);

            this.txtroom_code = (TextView) view.findViewById(R.id.textViewValueRoomStatus);

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
            mCursorRoom.moveToPosition(adapterPosition);
            mClickHandler.onClick();


        }
    }

}
