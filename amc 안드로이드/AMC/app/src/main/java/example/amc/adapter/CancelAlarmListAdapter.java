package example.amc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amc.service.domain.Alarm;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.amc.R;

/**
 * Created by bitcamp on 2017-11-06.
 */

public class CancelAlarmListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Alarm> alarmArrayList;
    ViewHolder viewHolder;


    public CancelAlarmListAdapter(Context context, ArrayList<Alarm> alarmArrayList) {
        this.context = context;
        this.alarmArrayList = alarmArrayList;
    }

    @Override
    public int getCount() {
        return this.alarmArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.alarmArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.cancelalarm,null);

            viewHolder = new ViewHolder();

            viewHolder.alarmNo_textview = (TextView)view.findViewById(R.id.alarmNo_textview);
            viewHolder.openTime_textview = (TextView)view.findViewById(R.id.openTime_textview);
            viewHolder.previewTitle_textview = (TextView)view.findViewById(R.id.previewTitle_textview);
            viewHolder.alarmSeatNo_textview = (TextView)view.findViewById(R.id.alarmSeatNo_textview);
            viewHolder.poster = (ImageView)view.findViewById(R.id.poster);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.alarmNo_textview.setText(alarmArrayList.get(i).getAlarmNo()+"");
        viewHolder.openTime_textview.setText(dateChange(alarmArrayList.get(i).getScreenContent().getScreenOpenTime()));
        if(alarmArrayList.get(i).getScreenContent().getPreviewFlag().equals("Y")){
            viewHolder.previewTitle_textview.setText(alarmArrayList.get(i).getScreenContent().getPreviewTitle());
        }else{
            viewHolder.previewTitle_textview.setText(alarmArrayList.get(i).getScreenContent().getMovie().getMovieNm());
        }
        viewHolder.alarmSeatNo_textview.setText(alarmArrayList.get(i).getAlarmSeatNo());

        //이미지 파일주소이름이 없거나, null일 경우
        if(alarmArrayList.get(i).getScreenContent().getMovie().getPostUrl().equals("") || alarmArrayList.get(i).getScreenContent().getMovie().getPostUrl() == null){
            viewHolder.poster.setImageResource(R.mipmap.ic_launcher);
        }
            Glide.with(context).load(alarmArrayList.get(i).getScreenContent().getMovie().getPostUrl()).into(viewHolder.poster);

        return view;
    }

    class ViewHolder{

        TextView alarmNo_textview;
        TextView openTime_textview;
        TextView previewTitle_textview;
        TextView alarmSeatNo_textview;
        ImageView poster;

    }

    public String dateChange(String oldDate){

        String[] dateDiv = oldDate.split(" ");

        String[] ymd = dateDiv[0].split("-");
        String[] hms = dateDiv[1].split(":");

        /*return ymd[0]+"년 "+ymd[1]+"월 "+ymd[2]+"일 "+" "+hms[0]+"시 "+hms[1]+"분 "+hms[2].substring(0,2)+"초 ";*/
        return ymd[0]+"-"+ymd[1]+"-"+ymd[2]+" "+hms[0]+":"+hms[1]+":"+hms[2].substring(0,2);
    }
}
