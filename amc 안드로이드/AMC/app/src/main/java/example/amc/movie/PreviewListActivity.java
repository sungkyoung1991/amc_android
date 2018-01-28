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
import com.amc.service.domain.ScreenContent;

import java.util.ArrayList;
import java.util.Map;

import example.amc.R;
import example.amc.adapter.PreviewListAdapter;
import example.amc.thread.GetMovieThread;
import example.amc.util.EndAlertDialog;

/**
 * Created by bitcamp on 2017-11-09.
 */

public class PreviewListActivity extends Activity {

    ListView listView;
    PreviewListAdapter previewListAdapter;
    ArrayList<ScreenContent> previewList;

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
                    final Intent intent = new Intent(PreviewListActivity.this, GetMovieActivity.class);
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(PreviewListActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previewlist);

        listView = (ListView)findViewById(R.id.previewList);


        Intent intent = getIntent();
        previewList = (ArrayList<ScreenContent>)intent.getSerializableExtra("previewList");
        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        System.out.println(previewList);

        previewListAdapter = new PreviewListAdapter(PreviewListActivity.this,previewList);
        listView.setAdapter(previewListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getMovieThread = new GetMovieThread(getMovieHandler, "movie", previewList.get(i).getMovie().getMovieNo()+"");
                getMovieThread.start();
            }
        });
    }
}
