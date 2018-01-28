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

public class BookingInfoListAdapter extends BaseAdapter {

    ///Field
    Context context;
    List<String> infoList;
    TextView movieTitle_textView;

    //Constructor
    public BookingInfoListAdapter(Context context, List<String> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return this.infoList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.infoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            movieTitle_textView = (TextView)convertView.findViewById(R.id.title_textView);
            //여기에 원하는 것 더 추가하기
        }
        movieTitle_textView.setText(infoList.get(i));
        return convertView;
    }
}
