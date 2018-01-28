package example.amc.booking;

/**
 * Created by koh on 2017-11-06.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amc.service.domain.Booking;
import com.beardedhen.androidbootstrap.BootstrapButton;

import example.amc.LobbyActivity;
import example.amc.R;
import example.amc.kakao.KakaoPayActivity;
import example.amc.thread.BookingConfirmThread;

public class BookingConfirmActivity extends Activity {
    ///Field
    private TextView impUid;
    private TextView screenContentNo_textview;
    private TextView bookingSeatNo_textview;
    private final String APP_SCHEME = "confirm://";
    private String userId;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ScrollView scrollView;

    ///Constructor !
    public BookingConfirmActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_layout);

        /*impUid = (TextView)findViewById(R.id.impUid);
        screenContentNo_textview = (TextView)findViewById(R.id.screenContentNo);
        bookingSeatNo_textview = (TextView)findViewById(R.id.bookingSeatNo);*/

        //웹뷰에서 부터 전달받은 impUid 파라미터 받기
        Intent intent = getIntent();
        if ( intent != null ) {
            Uri intentData = intent.getData();

            if ( intentData != null ) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();

                if ( url.startsWith(APP_SCHEME) ) {
                    String path = url.substring(APP_SCHEME.length());
                    String[] temp = path.split("=");
                    temp = temp[1].split("/");
                    System.out.println(temp[0]);
                    System.out.println(temp[1]);
                    /*impUid.setText(temp[0]);
                    screenContentNo_textview.setText(temp[1]);  //>>>>>>>>>>REST로 값보내고 핸들러에 SET TEXT하기
                    bookingSeatNo_textview.setText(temp[2]);*/

                    this.pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
                    this.editor = pref.edit();
                    userId = pref.getString("userId","");

                    new BookingConfirmThread(handler, temp[0], Integer.parseInt(temp[1]), temp[2], userId).start();
                }
            }
        }

        //뒤로가기로 가는것을 막기위해 결제하는 액티비티 Finish
        KakaoPayActivity ka = (KakaoPayActivity) KakaoPayActivity.KaoActivity; //웹뷰에서 왔기때문에 웹뷰쪽 액티비티 kill
        ka.finish();

    }


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

                final Booking booking = (Booking)message.obj;
                System.out.println(":★: booking.getMovieNm() in Activity  : "+booking.getMovie().getMovieNm());

                TextView selectedTitle = (TextView)findViewById(R.id.selected_title_textview);
                selectedTitle.setText(booking.getMovie().getMovieNm());

                TextView selectedTime = (TextView)findViewById(R.id.selected_time_textview);
                selectedTime.setText(booking.getScreenContent().getScreenOpenTime().subSequence(0,19));

                TextView selectedTheater = (TextView)findViewById(R.id.selected_theater_textview);
                selectedTheater.setText(Integer.toString(booking.getHeadCount())+"상영관");

                TextView selctedCount = (TextView)findViewById(R.id.selected_count_textview);
                selctedCount.setText(Integer.toString(booking.getHeadCount())+"명");

                TextView selectedSeats = (TextView)findViewById(R.id.selected_seats_textview);
                selectedSeats.setText(booking.getBookingSeatNo());

                TextView selectedPrice = (TextView)findViewById(R.id.selected_price_textview);
                selectedPrice.setText(Integer.toString(booking.getTotalTicketPrice())+"원");

                BootstrapButton button = (BootstrapButton)findViewById(R.id.request_pay_button);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BookingConfirmActivity.this, LobbyActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);
                    }
                });
            }
        }
    };

}
