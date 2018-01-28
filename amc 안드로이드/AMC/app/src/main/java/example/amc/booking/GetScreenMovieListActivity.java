package example.amc.booking;

/**
 * Created by koh on 2017-11-06.
 */

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.amc.service.domain.Movie;

import example.amc.R;
import example.amc.adapter.MyListAdapter;
import example.amc.thread.MovieClientSocketThread;

public class GetScreenMovieListActivity extends Activity implements OnClickListener {
    ///Field
    // 메세지 좌우배치를 위해 message.xml 를 add 할 Layout
    private LinearLayout messageInLayout;
    private String clientName;
    private Button buttonSend;
    private Button button_title;
    private EditText editTextMessage;
    private ScrollView scrollView;

    ListView listView;
    MyListAdapter myListAdapter;
    List<Movie> movieList;

    ///Constructor !
    public GetScreenMovieListActivity(){
    }

    // Thread / Thread 사이의 통신을 추상화한 Handler Definition
    // - 다른 Thread 가 Message 전달 sendMessage(Message) 호출 시
    // - Looper 가 호출 하는 CallBack Method  handleMessage() O/R
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){


                scrollView.post(new Runnable(){
                    public void run(){
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            }else if(message.what==200){
                System.out.println("핸들러에 온 메시지가 200프로토콜로 왔습니다.");

                movieList = (List<Movie>)message.obj;

                myListAdapter = new MyListAdapter(GetScreenMovieListActivity.this, movieList);
                listView.setAdapter(myListAdapter);

                //onItemClickListener를 추가
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        System.out.println(" ♠movieList.get(i).getMovieNo()"+ movieList.get(i).getMovieNo());

                        //다음화면으로 이동하기
                        Intent intent = new Intent(GetScreenMovieListActivity.this, SelectDateActivity.class);
                        intent.putExtra("movieNo",movieList.get(i).getMovieNo()+"");
                        intent.putExtra("flag", 1);
                        startActivity(intent);
                    }
                });

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_screen_movie_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.listView = (ListView)findViewById(R.id.my_listview);

        /* ChatClientSocketThread 생성 및 Thread.start(); */
        new MovieClientSocketThread(handler, clientName).start();

    }



    @Override
    public void onClick(View view) {

    }
}
