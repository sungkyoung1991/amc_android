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

public class DateListAdapter extends BaseAdapter {

    ///Field
    Context context;
    List<String> dateList;
    TextView date_textView;

    //Constructor
    public DateListAdapter(Context context, List<String> dateList) {
        this.context = context;
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        return this.dateList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.dateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.date_item, null);
            date_textView = (TextView)convertView.findViewById(R.id.date_textView);
            //여기에 원하는 것 더 추가하기
        }
        date_textView.setText(dateList.get(i)+"일");
        return convertView;
    }
}
