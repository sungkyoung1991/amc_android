package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.Booking;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import example.amc.rest.BookingRestHttpClient;

/*
 *  Server 와 통신하는 Thread
 */
public class GetBookingInfoThread extends Thread{

    ///Field
    private int timeOut = 3000;
    // 무한루프 제어용 Flag
    private Handler handler;
    private int screenContentNo;
    private String selectedSeats;


    ///Constructor
    public GetBookingInfoThread(){
    }
    public GetBookingInfoThread(Handler handler , int screenContentNo, String selectedSeats){
        this.handler = handler;
        this.screenContentNo = screenContentNo;
        this.selectedSeats = selectedSeats;
    }

    ///Method
    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try{

            BookingRestHttpClient restHttpClient = new BookingRestHttpClient();
            Booking booking = restHttpClient.getBookingInfo(screenContentNo, selectedSeats);

            if(booking == null){
                Message message = new Message();
                message.what = 100;
                message.obj =" movie데이터를 받지 못했습니다.";
                this.handler.sendMessage(message);
            }else{
                System.out.println("서버에서 받은 결제전 예매정보 : "+booking.toString());
                Message message = new Message();
                message.what = 200;
                message.obj = booking;
                this.handler.sendMessage(message);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        this.close();

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run() END.................");

    }//end of run()


    // 각종 객체 close()
    public void close(){
        System.out.println(":: close() end...");
    }

    public void onDestroy() {
        System.out.println(":: ChatClientSocketThread.onDestroy()");
    }

}//end of class