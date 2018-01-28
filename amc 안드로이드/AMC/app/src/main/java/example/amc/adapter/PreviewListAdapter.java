package example.amc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amc.service.domain.ScreenContent;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import example.amc.R;

/**
 * Created by bitcamp on 2017-11-06.
 */

public class PreviewListAdapter extends BaseAdapter{

    Context context;
    ArrayList<ScreenContent> previewArrayList;
    ViewHolder viewHolder;


    public PreviewListAdapter(Context context, ArrayList<ScreenContent> previewList) {
        this.context = context;
        this.previewArrayList = previewList;
    }

    @Override
    public int getCount() {
        return this.previewArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.previewArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.preview,null);

            viewHolder = new ViewHolder();

            TextView movieNo_textview;
            TextView title_textview;
            TextView ticketOpenDate_textview;
            TextView screenOpenTime_textview;

            viewHolder.movieNo_textview = (TextView)view.findViewById(R.id.movieNo_textview);
            viewHolder.title_textview = (TextView)view.findViewById(R.id.previewTitle);
            viewHolder.ticketOpenDate_textview = (TextView)view.findViewById(R.id.ticketOpenDate);
            viewHolder.screenOpenTime_textview = (TextView)view.findViewById(R.id.screenOpenTime);
            viewHolder.poster = (ImageView)view.findViewById(R.id.poster);


            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.movieNo_textview.setText(previewArrayList.get(i).getMovie().getMovieNo()+"");
        viewHolder.title_textview.setText(previewArrayList.get(i).getPreviewTitle());
        viewHolder.ticketOpenDate_textview.setText(previewArrayList.get(i).getTicketOpenDate());
        viewHolder.screenOpenTime_textview.setText(previewArrayList.get(i).getScreenOpenTime());

        //이미지 파일주소이름이 없거나, null일 경우
        if(previewArrayList.get(i).getMovie().getPostUrl().equals("") || previewArrayList.get(i).getMovie().getPostUrl() == null){
            viewHolder.poster.setImageResource(R.mipmap.ic_launcher);
        }
            Glide.with(context).load(previewArrayList.get(i).getMovie().getPostUrl()).into(viewHolder.poster);

        return view;
    }

    class ViewHolder{
        TextView movieNo_textview;
        TextView title_textview;
        TextView ticketOpenDate_textview;
        TextView screenOpenTime_textview;
        ImageView poster;
    }
}
