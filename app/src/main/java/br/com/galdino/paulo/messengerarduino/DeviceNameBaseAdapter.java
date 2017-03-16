package br.com.galdino.paulo.messengerarduino;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by paulo on 15/03/17.
 */

public class DeviceNameBaseAdapter extends BaseAdapter {

    private ArrayList<String> deviceNames;
    private LayoutInflater inflater;
    private Context context;

    //this constructor get a array list of device names
    public DeviceNameBaseAdapter(Context context, ArrayList<String> deviceNames){
        this.deviceNames = deviceNames;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return deviceNames.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<String> getData(){
        return deviceNames;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        //inflate the layout if it is null
        if(view == null) {
            view = inflater.inflate(R.layout.listitemanamedevicebluetooh, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        //retrieve the holder
        else{
            holder = (ViewHolder)view.getTag();
        }

        //set the text on the inflated layout
        if(getItem(position) != null) {
            holder.mText.setText(getItem(position).toString());
        }

        //return the view to list
        return view;
    }

    //reuse the view to hold the device name layout
    private class ViewHolder{

        TextView mText;

        ViewHolder(View view){
            mText = (TextView)view.findViewById(R.id.deviceName);
        }
    }
}
