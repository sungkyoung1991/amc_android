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

import java.util.ArrayList;

import example.amc.R;

/**
 * Created by bitcamp on 2017-11-06.
 */

public class MovieListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Movie> movieArrayList;
    ViewHolder viewHolder;


    public MovieListAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieArrayList = movieList;
    }

    @Override
    public int getCount() {
        return this.movieArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.movieArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.movie,null);

            viewHolder = new ViewHolder();

            viewHolder.movieNo_textview = (TextView)view.findViewById(R.id.movieNo_textview);
            viewHolder.title_textview = (TextView)view.findViewById(R.id.previewTitle_textview);
            viewHolder.date_textview = (TextView)view.findViewById(R.id.ticketOpenDate_textview);
            viewHolder.genre_textview = (TextView)view.findViewById(R.id.genre_textView);
            viewHolder.poster = (ImageView)view.findViewById(R.id.poster);


            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.movieNo_textview.setText(movieArrayList.get(i).getMovieNo()+"");
        viewHolder.title_textview.setText(movieArrayList.get(i).getMovieNm());
        viewHolder.date_textview.setText(movieArrayList.get(i).getOpenDt());
        viewHolder.genre_textview.setText(movieArrayList.get(i).getGenres());

        //이미지 파일주소이름이 없거나, null일 경우
        if(movieArrayList.get(i).getPostUrl().equals("") || movieArrayList.get(i).getPostUrl() == null){
            viewHolder.poster.setImageResource(R.mipmap.ic_launcher);
        }
            Glide.with(context).load(movieArrayList.get(i).getPostUrl()).into(viewHolder.poster);

        return view;
    }

    class ViewHolder{
        TextView movieNo_textview;
        TextView title_textview;
        TextView date_textview;
        TextView genre_textview;
        ImageView poster;
    }
}
