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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import example.amc.R;
import example.amc.adapter.DateListAdapter;
import example.amc.thread.GetScreenDateThread;

public class SelectDateActivity extends Activity {
    ///Field
    // 메세지 좌우배치를 위해 message.xml 를 add 할 Layout
    private LinearLayout messageInLayout;
    private ScrollView scrollView;

    ListView listView;
    DateListAdapter dateListAdapter;
    List<String> dateList;

    ///Constructor !
    public SelectDateActivity(){
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

                dateList = (List<String>)message.obj;
                System.out.println("★dateList : "+dateList);

                dateListAdapter = new DateListAdapter(SelectDateActivity.this, dateList);
                listView.setAdapter(dateListAdapter);

                //onItemClickListener를 추가
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        //Toast.makeText(SelectDateActivity.this, dateList.get(i)+"일", Toast.LENGTH_SHORT).show();
                        //다음화면으로 이동하기
                        Intent intent = new Intent(SelectDateActivity.this, SelectTimeActivity.class);
                        intent.putExtra("screenDate",dateList.get(i)+"");
                        startActivity(intent);
                    }
                });

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_screen_movie_list);//여기가 아마 ChatActivity를 보여주는 부분

        // 다른 Activity 에서 호출시 Intent 에 Message 유무를 확인위해
        // Intent instance GET
        Intent intent = this.getIntent();

        // Intent 의 Message GET :==> editText View  출력
        String movieNo = intent.getStringExtra("movieNo");
        // getStringExtra 대신에 getData 등 다른 method 사용해보자!
        int flag = intent.getIntExtra("flag",1);
        System.out.println(getClass().getSimpleName()+"::고객님이 선택하신 영화번호:: "+movieNo);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.listView = (ListView)findViewById(R.id.my_listview);

        /* ChatClientSocketThread 생성 및 Thread.start(); */
        new GetScreenDateThread(handler, movieNo, flag).start();

    }

}
