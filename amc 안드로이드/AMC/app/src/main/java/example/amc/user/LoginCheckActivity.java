package example.amc.user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.Map;

import example.amc.LobbyActivity;
import example.amc.R;
import example.amc.thread.LoginCheckThread;
import example.amc.util.EndAlertDialog;

/*
 *  Android Client Application
 */
public class LoginCheckActivity extends AppCompatActivity {

    ///Field
    // 메세지 좌우배치를 위해 message.xml 를 add 할 Layout
    private LinearLayout messageInLayout;
    private String userId;
    private String password;
    private Button buttonSend;
    private EditText editTextMessage;
    private ScrollView scrollView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Boolean kakaoLogin;


    private LoginCheckThread loginCheckThread;

    // Thread / Thread 사이의 통신을 추상화한 Handler Definition
    // - 다른 Thread 가 Message 전달 sendMessage(Message) 호출 시
    // - Looper 가 호출 하는 CallBack Method  handleMessage() O/R
    private Handler handler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message) {

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타 : " + fromHostData);

                if(fromHostData.indexOf("실패") != -1){
                    if(!kakaoLogin){ //카카오 로그인 아니라면
                        if(pref.getBoolean("autoLogin",false)){ //자동로그인을 체크했다면
                            editor.putString("userId",userId);
                            editor.putString("password",password);
                            editor.putBoolean("autoLogin",true);
                            editor.putBoolean("loginFail",true);
                            editor.commit();
                        }
                    }
                    new EndAlertDialog(LoginCheckActivity.this)
                            .showEndDialogToActivity("[ 로그인 실패. ]","서버에 일치하는 데이터가 없습니다",LoginActivity.class);
                }else{

                    SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    if(!kakaoLogin){ //카카오로그인이 아니라면
                        if(pref.getBoolean("autoLogin",false)){ //자동로그인을 했다면
                            editor.putString("userId",userId);
                            editor.putString("password",password);
                            editor.putBoolean("autoLogin",true);
                            editor.putBoolean("loginFail",false);
                            editor.commit();
                        }else{ //자동로그인이 아니니까 모든 정보 클리어
                            editor.clear();
                            editor.commit();
                        }
                    }else{
                        editor.putBoolean("kakaoLogin",true);
                        editor.commit();
                    }


                    final Intent intent = new Intent(LoginCheckActivity.this, LobbyActivity.class);
                    intent.putExtra("userId",(String)((Map<String,Object>)message.obj).get("userId"));

                    new EndAlertDialog(LoginCheckActivity.this)
                        .showEndDialogToActivity("[ 로그인 성공. ]","알림 신청(토큰등록)을 신청해주세요",intent);

                }

            }

            // Application Protocol : 999  ==> Server 응답없음
            if(message.what == 999){
                editor.putBoolean("loginFail",true);
                editor.putBoolean("kakaoLogin",false);
                editor.commit();

                new EndAlertDialog(LoginCheckActivity.this)
                        .showEndDialogToActivity("[ 로그인 실패. ]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요",LoginActivity.class);
            }

            // Application Protocol : 500  ==> Server 강제종료
            if(message.what == 500){

                String endMessage = (String)message.obj;

                //append(endMessage);

                // ScrollView 화면 아래로 이동(두가지 방법)
                //scrollView.scrollTo(0, scrollView.getHeight());
                //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView.post(new Runnable(){
                    public void run(){
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

                // View 비활성화
                buttonSend.setEnabled(false);
                editTextMessage.setEnabled(false);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincheck);

        // 다른 Activity 에서 호출시 Intent 에 Message 유무를 확인위해
        // Intent instance GET
        Intent intent = this.getIntent();

        // Intent 의 Message GET :==> editText View  출력
        this.userId = intent.getStringExtra("userId");
        this.password = intent.getStringExtra("password");
        this.kakaoLogin = intent.getBooleanExtra("kakaoLogin",false);
        this.pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
        this.editor = pref.edit();

        System.out.println(getClass().getSimpleName()+"::이메일아이디:: "+this.userId);
        System.out.println(getClass().getSimpleName()+"::비밀번호:: "+this.password);

        if(kakaoLogin) {
            this.loginCheckThread = new LoginCheckThread(handler, userId, kakaoLogin);
            loginCheckThread.start();
        }else{
            this.loginCheckThread = new LoginCheckThread(handler, userId, password);
            loginCheckThread.start();
        }
    }


    // Activity Life Cycle 이해
    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("LoginCheckActivity.onDestory()");

        if( loginCheckThread != null){
            loginCheckThread.onDestroy();
        }
    }

    // Call Back Method 이용 취소버튼이용 App. 종료
    @Override
    public void onBackPressed() {

        // 종료 EndAlertDialog Bean 사용
        new EndAlertDialog(this).showEndDialog();

    }
}