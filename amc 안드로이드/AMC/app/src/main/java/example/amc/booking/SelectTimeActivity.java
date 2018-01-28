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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.amc.service.domain.ScreenContent;

import example.amc.R;
import example.amc.adapter.TimeListAdapter;
import example.amc.thread.GetScreenTmeThread;

public class SelectTimeActivity extends Activity {
    ///Field
    private ScrollView scrollView;

    ListView listView;
    TimeListAdapter timeListAdapter;
    List<ScreenContent> timeList;

    ///Constructor !
    public SelectTimeActivity(){
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

                timeList = (List<ScreenContent>)message.obj;

                timeListAdapter = new TimeListAdapter(SelectTimeActivity.this, timeList);
                listView.setAdapter(timeListAdapter);

                //onItemClickListener를 추가
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        //Toast.makeText(SelectTimeActivity.this, timeList.get(i).getScreenOpenTime(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SelectTimeActivity.this, "상영번호 : "+timeList.get(i).getScreenContentNo(), Toast.LENGTH_SHORT).show();
                        //다음화면으로 이동하기
                        Intent intent = new Intent(SelectTimeActivity.this, SelectSeatActivity.class);
                        intent.putExtra("screenContentNo",timeList.get(i).getScreenContentNo());
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
        String screenDate = intent.getStringExtra("screenDate");
        System.out.println(getClass().getSimpleName()+"::고객님이 선택하신 상영일자:: "+screenDate);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.listView = (ListView)findViewById(R.id.my_listview);

        /* ChatClientSocketThread 생성 및 Thread.start(); */
        new GetScreenTmeThread(handler, screenDate).start();

    }

}
