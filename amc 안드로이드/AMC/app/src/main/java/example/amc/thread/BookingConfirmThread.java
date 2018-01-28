package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.Booking;

import example.amc.rest.BookingRestHttpClient;

/*
 *  Server 와 통신하는 Thread
 */
public class BookingConfirmThread extends Thread{

    ///Field
    private int timeOut = 3000;
    // 무한루프 제어용 Flag
    private Handler handler;
    private String impUid;
    private int screenContentNo;
    private String bookingSeatNo;
    private String userId;


    ///Constructor
    public BookingConfirmThread(){
    }
    public BookingConfirmThread(Handler handler , String impUid, int screenContentNo, String bookingSeatNo, String userId){
        this.handler = handler;
        this.impUid = impUid;
        this.screenContentNo = screenContentNo;
        this.bookingSeatNo = bookingSeatNo;
        this.userId = userId;
    }

    ///Method
    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try{

            Booking booking = new Booking();
            booking.setScreenContentNo(screenContentNo);
            booking.setImpId(impUid);
            booking.setBookingSeatNo(bookingSeatNo);
            booking.setUserId(userId);
            System.out.println("결제 후 받은 booking 정보 : "+booking);

            BookingRestHttpClient restHttpClient = new BookingRestHttpClient();
            Booking addedBooking = restHttpClient.addBookingConfirm(booking);

            if(addedBooking == null){
                Message message = new Message();
                message.what = 100;
                message.obj =" movie데이터를 받지 못했습니다.";
                this.handler.sendMessage(message);
            }else{
                System.out.println("서버에서 받은 addedBooking : "+addedBooking.toString());
                Message message = new Message();
                message.what = 200;
                message.obj = addedBooking;
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