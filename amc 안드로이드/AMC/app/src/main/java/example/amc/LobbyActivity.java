package example.amc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import com.amc.service.domain.Alarm;
import com.amc.service.domain.Movie;
import com.amc.service.domain.ScreenContent;
import com.amc.service.domain.User;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import example.amc.alarm.AlarmListActivity;
import example.amc.booking.GetScreenMovieListActivity;
import example.amc.movie.MovieListActivity;
import example.amc.movie.PreviewListActivity;
import example.amc.thread.AddPushTokenThread;
import example.amc.thread.AlarmListThread;
import example.amc.thread.LoginCheckThread;
import example.amc.thread.MovieListThread;
import example.amc.thread.PreviewListThread;
import example.amc.user.GetUserActivity;
import example.amc.user.LoginActivity;
import example.amc.util.EndAlertDialog;

/**
 * Created by bitcamp on 2017-10-31.
 */

public class LobbyActivity extends Activity {

    private String userId;
    private String token;
    private Boolean isCommingSoon;
    //private Button addPushButton; //토큰 등록 버튼
    /*private Button cancelAlarmButton; //취소표 알리미 리스트 버튼
    private Button logOutButton; //로그아웃 버튼
    private Button movieListButton; //현재 상영영화 버튼
    private Button commingSoonListButton;
    private Button getUserButton; //내 정보 보기 버튼
    private Button openAlarmButton; //티켓오픈 알리미 버튼
    private Button previewListButton; //시사회 리스트 버튼
    private Button movieBookingButton;*/
    private BootstrapButton addPushButton;
    private BootstrapButton cancelAlarmButton;
    private BootstrapButton logOutButton;
    private BootstrapButton movieListButton;
    private BootstrapButton commingSoonListButton;
    private BootstrapButton getUserButton;
    private BootstrapButton openAlarmButton;
    private BootstrapButton previewListButton;
    private BootstrapButton movieBookingButton;


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    //현재시간 구하기용
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    String time = sdfNow.format(new Date(System.currentTimeMillis()));

    //Thread 목록
    private AddPushTokenThread addPushTokenThread;
    private MovieListThread movieListThread;
    private LoginCheckThread loginCheckThread;
    private AlarmListThread alarmListThread;
    private PreviewListThread previewListThread;


    //토큰 등록 시도시 웹 통신결과
    private Handler tokenHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = ((Map<String,String>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);


                if(fromHostData.indexOf("실패") != -1){

                    new EndAlertDialog(LobbyActivity.this)
                            .showDialog("[ 토큰 등록 실패. ]","잠시 후 다시 이용해 주세요");
                }else{

                    new EndAlertDialog(LobbyActivity.this)
                            .showDialog("[ 토큰 등록 성공. ]","이제부터 푸쉬알림을 받으실 수 있습니다");
                }
            }

            if(message.what == 999){
                new EndAlertDialog(LobbyActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    //영화 리스트 시도시 웹 통신결과
    private Handler movieListHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);


                if(fromHostData.indexOf("없음") != -1){

                    if(!isCommingSoon){
                        Toast.makeText(LobbyActivity.this, "상영 예정 영화가 없습니다", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LobbyActivity.this, "현재 상영 영화가 없습니다", Toast.LENGTH_LONG).show();
                    }

                }else{
                    ArrayList<Movie> movieList = (ArrayList<Movie>)((Map<String, Object>) message.obj).get("movieList");
                    final Intent intent = new Intent(LobbyActivity.this, MovieListActivity.class);
                    intent.putExtra("movieList",movieList);
                    intent.putExtra("isCommingSoon",isCommingSoon);

                    if(!isCommingSoon){
                        Toast.makeText(LobbyActivity.this, time+"\n현재 상영 영화 리스트", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LobbyActivity.this, time+"\n상영 예정 영화 리스트", Toast.LENGTH_LONG).show();
                    }
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(LobbyActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    //영화 리스트 시도시 웹 통신결과
    private Handler previewListHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);


                if(fromHostData.indexOf("없음") != -1){

                    Toast.makeText(LobbyActivity.this, "현재 상영중인 시사회가 없습니다", Toast.LENGTH_LONG).show();

                }else{
                    ArrayList<ScreenContent> previewList = (ArrayList<ScreenContent>)((Map<String, Object>) message.obj).get("previewList");
                    final Intent intent = new Intent(LobbyActivity.this, PreviewListActivity.class);
                    intent.putExtra("previewList",previewList);

                    Toast.makeText(LobbyActivity.this, time+"\n현재 상영하는 시사회 리스트", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(LobbyActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    //유저 정보 시도시 웹 통신결과
    private Handler getUserHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);


                if(fromHostData.indexOf("없음") != -1){
                }else{
                    User user = (User)((Map<String, Object>) message.obj).get("user");
                    final Intent intent = new Intent(LobbyActivity.this, GetUserActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(LobbyActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    //알람 리스트 시도시 웹 통신결과
    private Handler alarmListHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타alarm : " + fromHostData);


                if(fromHostData.indexOf("없음") != -1){
                }else{
                    ArrayList<Alarm> alarmList = (ArrayList<Alarm>)((Map<String, Object>) message.obj).get("alarmList");
                    final Intent intent = new Intent(LobbyActivity.this, AlarmListActivity.class);
                    intent.putExtra("alarmList",alarmList);
                    startActivity(intent);
                }
            }

            if(message.what == 999){
                new EndAlertDialog(LobbyActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);

        LoginActivity la = (LoginActivity)LoginActivity.logActivity;
        la.finish();

        Intent intent = this.getIntent();
        this.userId = intent.getStringExtra("userId");
        this.token = FirebaseInstanceId.getInstance().getToken();
        this.addPushButton = (BootstrapButton) findViewById(R.id.addPushButton);
        this.cancelAlarmButton = (BootstrapButton)findViewById(R.id.cancelAlarmButton);
        this.logOutButton = (BootstrapButton)findViewById(R.id.logOutbutton);
        this.movieListButton = (BootstrapButton)findViewById(R.id.movieListButton);
        this.getUserButton = (BootstrapButton)findViewById(R.id.getUserbutton);
        this.openAlarmButton = (BootstrapButton)findViewById(R.id.openAlarmButton);
        this.commingSoonListButton = (BootstrapButton)findViewById(R.id.commingSoonListButton);
        this.previewListButton = (BootstrapButton)findViewById(R.id.previewListButton);
        this.movieBookingButton = (BootstrapButton)findViewById(R.id.movieBookingButton);

        //app 종속데이터
        this.pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        this.editor = pref.edit();

        editor.putString("userId",userId);
        editor.commit();


        //사용자에게 보여주는 현재 접속 아이디 및 토큰
        Toast.makeText(this, "환영합니다 ["+userId+"]님.", Toast.LENGTH_SHORT).show();

        FirebaseMessaging.getInstance().subscribeToTopic("notice");

        //푸쉬 토큰 등록 버튼 리스너
        addPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPushTokenThread = new AddPushTokenThread(tokenHandler,userId,token);
                addPushTokenThread.start();
            }
        });

        //현재 상영 영화 조회 버튼 리스너
        movieListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCommingSoon = false;
                movieListThread = new MovieListThread(movieListHandler,userId,"movie");
                movieListThread.start();
            }
        });

        //상영 예정 영화 조회 버튼 리스너
        commingSoonListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCommingSoon = true;
                movieListThread = new MovieListThread(movieListHandler,userId,"commingsoon");
                movieListThread.start();
            }
        });

        //시사회 조회 버튼 리스너
        previewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewListThread = new PreviewListThread(previewListHandler,userId,"preview");
                previewListThread.start();
            }
        });


        //내정보 보기 버튼 리스너
        getUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCheckThread = new LoginCheckThread(getUserHandler,userId,true);
                loginCheckThread.start();
            }
        });


        //티켓오픈 알리미 버튼 리스너
        openAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmListThread = new AlarmListThread(alarmListHandler,userId,"O");
                alarmListThread.start();
            }
        });

        //취소표 알리미 버튼 리스너
        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmListThread = new AlarmListThread(alarmListHandler,userId,"C");
                alarmListThread.start();
            }
        });

        //예매 버튼 리스너
        movieBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LobbyActivity.this,GetScreenMovieListActivity.class));
            }
        });


        //로그아웃 버튼 리스너
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LobbyActivity.this, R.style.AlertDialogCustom);*/
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LobbyActivity.this);

                // AlertDialog 상태값 SET
                alertDialogBuilder.setTitle("[로그아웃]");
                alertDialogBuilder.setMessage("로그아웃 하시겠습니까?");

                //==> alert 창 이후 취소버튼 불가
                alertDialogBuilder.setCancelable(false);


                alertDialogBuilder.setPositiveButton(	"예" ,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!pref.getBoolean("kakaoLogin",false)){ //일반유저가 로그아웃하면
                                    Intent intent = new Intent(LobbyActivity.this,LoginActivity.class);
                                    editor.putBoolean("loginFail",true);
                                    editor.commit();
                                    startActivity(intent);
                                    LobbyActivity.this.finish();
                                }else{ //카카오유저가 로그아웃하면
                                    editor.putBoolean("kakaoLogin",false);
                                    editor.putBoolean("loginFail",true);
                                    editor.putString("userId","");
                                    editor.putString("password","");
                                    editor.commit();
                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                            final Intent intent = new Intent(LobbyActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            LobbyActivity.this.finish();
                                            /*redirectLoginActivity(); //로그인 액티비티로 이동하는 함수*/
                                        }
                                    });
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton(	"아니요" ,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        } );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /*protected void redirectLoginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        LobbyActivity.this.finish();
    }*/

    @Override
    public void onBackPressed() {

        // 종료 EndAlertDialog Bean 사용
        new EndAlertDialog(this).showEndDialog();

    }

    @Override
    protected void onDestroy() {
        System.out.println("로비 액티비티 디스트로이");
        super.onDestroy();
    }
}
