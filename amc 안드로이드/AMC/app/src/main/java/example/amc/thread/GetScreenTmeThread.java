package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.ScreenContent;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import example.amc.rest.BookingRestHttpClient;

/*
 *  Server 와 통신하는 Thread
 */
public class GetScreenTmeThread extends Thread{

    ///Field
    private BufferedReader  fromServer;
    private PrintWriter toServer;
    private Handler handler;
    private String screeDate;
    private int timeOut =3000;

    ///Constructor
    public GetScreenTmeThread(){
    }
    public GetScreenTmeThread(Handler handler , String screeDate){
        this.handler = handler;
        this.screeDate = screeDate;
    }

    ///Method
    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try{


            ///////////////////////////////////////////
            // 회원 확인위해 추가된부분....
            BookingRestHttpClient restHttpClient = new BookingRestHttpClient();

            List<ScreenContent> timeList = restHttpClient.getScreenTime(screeDate);

            if(timeList == null){
                Message message = new Message();
                message.what = 100;
                message.obj =" movie데이터를 받지 못했습니다.";
                this.handler.sendMessage(message);

            }else{
                System.out.println("받아온 timeList : "+timeList);

                Message message = new Message();
                message.what = 200;
                message.obj = timeList;
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

        System.out.println(":: close() start...");

        try{

            if( toServer != null){
                toServer.close();
                toServer = null;
            }

            if( fromServer != null){
                fromServer.close();
                fromServer = null;
            }

            //Server 에서 정상적으로 close 할때 까지 대기
            Thread.sleep(timeOut);

        }catch(Exception e){
            System.out.println( e.toString() );
            //e.printStackTrace();
        }
        System.out.println(":: close() end...");
    }


    // Activity Life Cycle 이해 / Activity.onDestiry() 에서 Call
    // Server 로부터 Data 받는 무한 Loop 종료
    public void onDestroy() {
        System.out.println(":: ChatClientSocketThread.onDestroy()");
    }

}//end of class