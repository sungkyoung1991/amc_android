package example.amc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amc.service.domain.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

import example.amc.R;

/**
 * Created by koh on 2017-11-07.
 */

public class MyListAdapter extends BaseAdapter {

    ///Field
    Context context;
    List<Movie> movieList;
    TextView movieTitle_textView;
    ViewHolder viewHolder;
    String serverUrl;

    //Constructor
    public MyListAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.serverUrl = "http://192.168.0.32:8080";
    }

    @Override
    public int getCount() {
        return this.movieList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.movieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item,null);

            viewHolder = new ViewHolder();

            viewHolder.titleTextView = (TextView)view.findViewById(R.id.title_textView);
            viewHolder.genreTextView = (TextView)view.findViewById(R.id.genre_textView);
            viewHolder.ratingImageView = (ImageView)view.findViewById(R.id.rating_imageview);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.titleTextView.setText(movieList.get(i).getMovieNm()+"");
        viewHolder.genreTextView.setText(movieList.get(i).getGenres());

        //이미지 파일주소이름이 없거나, null일 경우
        if(movieList.get(i).getWatchGradeNm().equals("") || movieList.get(i).getWatchGradeNm() == null){
            viewHolder.ratingImageView.setImageResource(R.mipmap.ic_launcher);
        }

        if(movieList.get(i).getWatchGradeNm().indexOf("12세")>=0){
            System.out.println("♠♠♠movieList.get(i).getWatchGradeNm().indexOf(\"12세\")>=에 들어옴");
            Glide.with(context).load(serverUrl+"/images/movie/12.png" ).into(viewHolder.ratingImageView);
        }else if(movieList.get(i).getWatchGradeNm().indexOf("15세")>=0){
            Glide.with(context).load(serverUrl+"/images/movie/15.png" ).into(viewHolder.ratingImageView);
        }else if(movieList.get(i).getWatchGradeNm().indexOf("청소년")>=0){
            Glide.with(context).load(serverUrl+"/images/movie/19.png" ).into(viewHolder.ratingImageView);
        }else{
            Glide.with(context).load(serverUrl+"/images/movie/all.png"
            ).into(viewHolder.ratingImageView);
        }

        return view;
    }

    private class ViewHolder {
        TextView genreTextView;
        TextView titleTextView;
        ImageView ratingImageView;
    }
}
