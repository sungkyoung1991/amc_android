package example.amc.movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amc.service.domain.Movie;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.Map;

import example.amc.R;
import example.amc.adapter.MovieListAdapter;
import example.amc.booking.GetScreenMovieListActivity;
import example.amc.thread.GetMovieThread;
import example.amc.util.EndAlertDialog;

/**
 * Created by bitcamp on 2017-11-09.
 */

public class MovieListActivity extends Activity {

    ListView listView;
    MovieListAdapter movieListAdapter;
    ArrayList<Movie> movieList;
    BootstrapButton goBookingButton;
    BootstrapButton movieListHeader;
    Boolean isCoomingSoon;

    private GetMovieThread getMovieThread;

    private Handler getMovieHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);


                if(fromHostData.indexOf("없음") != -1){
                }else{
                    Movie movie = (Movie)((Map<String, Object>) message.obj).get("movie");
                    final Intent intent = new Intent(MovieListActivity.this, GetMovieActivity.class);
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(MovieListActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielist);

        listView = (ListView)findViewById(R.id.movieList);
        goBookingButton = (BootstrapButton)findViewById(R.id.goBookingButton);
        movieListHeader = (BootstrapButton)findViewById(R.id.movieListHeader);


        //인텐트의 정보 가지고오기
        Intent intent = getIntent();

        //가지고 온 영화정보 확인
        movieList = (ArrayList<Movie>)intent.getSerializableExtra("movieList");
        //영화가 현재상영인지 상영예정인지 확인
        isCoomingSoon = intent.getBooleanExtra("isCommingSoon",false);
        if(!isCoomingSoon){
            movieListHeader.setText("현재 상영 영화");
        }else{
            movieListHeader.setText("상영 예정 영화");
        }

        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        System.out.println(movieList);

        movieListAdapter = new MovieListAdapter(MovieListActivity.this,movieList);
        listView.setAdapter(movieListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getMovieThread = new GetMovieThread(getMovieHandler, "movie", movieList.get(i).getMovieNo()+"");
                getMovieThread.start();
            }
        });

        goBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieListActivity.this, GetScreenMovieListActivity.class);
                startActivity(intent);
            }
        });
    }

}
