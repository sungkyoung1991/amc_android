package example.amc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amc.service.domain.ScreenContent;

import java.util.List;

import example.amc.R;

/**
 * Created by koh on 2017-11-07.
 */

public class TimeListAdapter extends BaseAdapter {

    ///Field
    Context context;
    List<ScreenContent> timeList;
    TextView date_textView;

    //Constructor
    public TimeListAdapter(Context context, List<ScreenContent> timeList) {
        this.context = context;
        this.timeList = timeList;
    }

    @Override
    public int getCount() {
        return this.timeList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.timeList.get(i);
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
        date_textView.setText("상영 시작 시간 : "+timeList.get(i).getScreenOpenTime());
        return convertView;
    }
}
