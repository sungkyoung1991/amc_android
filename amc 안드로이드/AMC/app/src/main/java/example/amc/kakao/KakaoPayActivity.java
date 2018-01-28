package example.amc.kakao;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.amc.service.domain.Booking;
import com.amc.service.domain.Movie;
import com.amc.service.domain.ScreenContent;

import java.net.URLEncoder;

import example.amc.R;
import example.amc.importsdk.KakaoWebViewClient;

public class KakaoPayActivity extends AppCompatActivity {

    private WebView mainWebView;
    private final String APP_SCHEME = "amc://";
    private String myUrl = "http://183.98.215.171:8000";
    public static Activity KaoActivity;

    //bookingInfo
    private Movie movie;
    private ScreenContent screenContent;
    private String bookingSeatNo;
    private int headCount;
    private int totalTicketPrice;
    private String title;
    private Booking booking;
    private Booking tempBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);
        KaoActivity = KakaoPayActivity.this;

        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new KakaoWebViewClient(this)); //현재 액티비티를 기억(메인액티비티)
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        Intent bookingIntent = getIntent();
        tempBooking = (Booking)bookingIntent.getSerializableExtra("booking");
        if(tempBooking!=null){
            booking = tempBooking;
            this.movie = booking.getMovie();
            this.screenContent = booking.getScreenContent();
            this.bookingSeatNo = booking.getBookingSeatNo();
            this.headCount = booking.getHeadCount();
            this.totalTicketPrice = booking.getTotalTicketPrice();

            //시사회 인지 유무를 체크하여 제목을 넣음
            if(screenContent.getPreviewFlag().equals("Y")){
                this.title = screenContent.getPreviewTitle();
            }else{
                this.title = movie.getMovieNm();
            }


            try{
                System.out.println("확인확인확인 타이틀"+URLEncoder.encode(title,"UTF-8"));
                mainWebView.loadUrl(myUrl+"/cinema/mobileKakaoPay?title="+ URLEncoder.encode(title,"UTF-8")+
                        "&screenOpenTime="+screenContent.getScreenDate()+"   "+screenContent.getScreenOpenTime()+
                        "&theater="+screenContent.getScreenTheater()+"&bookingSeatNo="+getSeatNo(booking.getBookingSeatNo())+
                        "&headCount="+booking.getHeadCount()+
                        "&totalTicketPrice="+booking.getTotalTicketPrice()+
                        "&screenContentNo="+booking.getScreenContentNo()+
                        "&originSeat="+booking.getBookingSeatNo());
            }catch(Exception e){
                e.printStackTrace();
            }
        }



        /*mainWebView.loadUrl("http://www.iamport.kr/demo"); //내창을 뛰울꺼임*/
        /*mainWebView.loadUrl(myUrl+"/cinema/completePay?impUid=imp_213578321038");*/
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if ( intent != null ) {
            Uri intentData = intent.getData();

            if ( intentData != null ) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();
                System.out.println("★★★★★★★★★★★★");
                System.out.println(url);
                System.out.println("★★★★★★★★★★★★");

                if ( url.startsWith(APP_SCHEME) ) {
                    String path = url.substring(APP_SCHEME.length());
                    System.out.println(url);
                    System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                    System.out.println(path);
                    System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                    System.out.println(booking);


                    if ( "process".equalsIgnoreCase(path.split("/")[0]) ) {
                        System.out.println("process!!!!!!!!");
                        mainWebView.loadUrl(myUrl+"/cinema/completePay?"+
                                            "impUid="+path.split("/")[1]+
                                            "&screenContentNo="+path.split("/")[2]+
                                            "&bookingSeatNo="+path.split("/")[3]);
                    } else {
                        System.out.println("cancel!!!!!!!!!");
                        mainWebView.loadUrl(myUrl+"/cinema/failPay.jsp");
                    }
                }
            }
        }

    }

    public String getSeatNo(String seatNo) throws Exception{
        String[] strArray = seatNo.split(",");
        String displaySeat = "";
        int k=0;
        for(int i=0;i<(strArray.length/2);i++){
            // 아스키 코드를 문자형으로 변환
            int no = Integer.parseInt(strArray[k])+65;
            String displaySeatNo = Character.toString ((char) no);
            displaySeat += displaySeatNo + strArray[k+1]+" ";
            System.out.println("k : "+k+", displaySeat : "+displaySeat);
            k+=2;
        }
        return displaySeat;
    }
}
