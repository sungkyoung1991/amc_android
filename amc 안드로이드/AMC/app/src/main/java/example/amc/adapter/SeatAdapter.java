package example.amc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.amc.R;

/**
 * Created by koh on 2017-11-07.
 */

public class SeatAdapter extends BaseAdapter {

    ///Field
    Context context;
    List<String> seatList;
    TextView seat_textView_hall;
    TextView seat_textView_enable;
    TextView seat_textView_disable;

    //Constructor
    public SeatAdapter(Context context, List<String> seatList) {
        this.context = context;
        this.seatList = seatList;
    }

    @Override
    public int getCount() {
        return this.seatList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.seatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        System.out.println(":::::seatList.get(i) : "+seatList.get(i).toString());
        if(seatList.get(i).toString().equals("0")){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_hall, null);
            seat_textView_hall = (TextView)convertView.findViewById(R.id.seat_textView_hall);
            //seat_textView_hall.setText(seatList.get(i));
        }else if(seatList.get(i).toString().equals("1")){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_enable, null);
            seat_textView_enable = (TextView)convertView.findViewById(R.id.seat_textView_enable);

        }else if(seatList.get(i).toString().equals("3")){
            System.out.println("::in else:::seatList.get(i) : "+seatList.get(i));
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_selected, null);
            seat_textView_disable = (TextView)convertView.findViewById(R.id.seat_textview_slected);

        }else{
            System.out.println("::in else:::seatList.get(i) : "+seatList.get(i));
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_disable, null);
            seat_textView_disable = (TextView)convertView.findViewById(R.id.seat_textView_disable);

        }

        return convertView;
    }
}
