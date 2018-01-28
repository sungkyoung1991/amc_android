package example.amc.booking;

/**
 * Created by koh on 2017-11-06.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amc.service.domain.Booking;

import example.amc.R;
import example.amc.adapter.BookingInfoListAdapter;
import example.amc.kakao.KakaoPayActivity;
import example.amc.thread.GetBookingInfoThread;

public class AddBookingActivity extends Activity {
    ///Field
    private ScrollView scrollView;
    private ListView inforListView;

    Activity act = this;
    BookingInfoListAdapter bookingInfoListAdapter;

    ///Constructor !
    public AddBookingActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_layout);

        //Intent 받아온 정보 확인
        Intent intent = this.getIntent();
        String selectedSeats = intent.getStringExtra("selectedSeats");
        int screenContentNo = intent.getIntExtra("screenContentNo", 0);
        System.out.println(getClass().getSimpleName()+"::고객님이 선택하신 좌석번호:: "+selectedSeats+" ::상영번호 : "+screenContentNo);

        new GetBookingInfoThread(handler, screenContentNo, selectedSeats).start();
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
                System.out.println(":: booking.getMovieNm() in Activity  : "+booking.getMovie().getMovieNm());

                RelativeLayout relativeLayout = (RelativeLayout)View.inflate(AddBookingActivity.this, R.layout.booking_layout, null );

                TextView selectedTitle = (TextView)findViewById(R.id.selected_title_textview);
                selectedTitle.setText(booking.getMovie().getMovieNm());

                TextView selectedTime = (TextView)findViewById(R.id.selected_time_textview);
                selectedTime.setText(booking.getScreenContent().getScreenOpenTime());

                TextView selectedCount = (TextView)findViewById(R.id.selected_count_textview);
                selectedCount.setText(Integer.toString(booking.getHeadCount())+"명");

                TextView selectedPrice = (TextView)findViewById(R.id.selected_price_textview);
                selectedPrice.setText(Integer.toString(booking.getTotalTicketPrice())+"원");

                Button button = (Button)findViewById(R.id.request_pay_button);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddBookingActivity.this, KakaoPayActivity.class);
                        intent.putExtra("booking", booking);
                        startActivity(intent);
                    }
                });
            }
        }
    };

}
