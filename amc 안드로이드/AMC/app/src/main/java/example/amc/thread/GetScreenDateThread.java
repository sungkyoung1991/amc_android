package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import example.amc.rest.BookingRestHttpClient;

/*
 *  Server 와 통신하는 Thread
 */
public class GetScreenDateThread extends Thread{

    ///Field
    private BufferedReader  fromServer;
    private PrintWriter toServer;
    private Socket socket;
    private int timeOut = 3000;
    // 무한루프 제어용 Flag
    private boolean loopFlag = false;
    private Handler handler;
    private String movieNo;
    private int flag;

    ///Constructor
    public GetScreenDateThread(){
    }
    public GetScreenDateThread(Handler handler , String movieNo, int flag){
        this.handler = handler;
        this.movieNo = movieNo;
        this.flag = flag;
    }

    ///Method
    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try{


            ///////////////////////////////////////////
            // 회원 확인위해 추가된부분....
            BookingRestHttpClient restHttpClient = new BookingRestHttpClient();

            List<String> dateList = restHttpClient.getScreenDate(movieNo, flag);

            if(dateList == null){
                // 다른 Thread 의 UI 수정시 Thread 간 통신을 담당하는 Handler 사용
                // Handler 통해 전달 할 Data 는 Message Object 생성 전달
                Message message = new Message();
                // Application Protocol : 100 ==> 정상
                message.what = 100;
                // 전달할 Data 가 많으면 : Domain Object
                message.obj =" movie데이터를 받지 못했습니다.";
                // Hander 에게 Message 전달
                this.handler.sendMessage(message);
            }else{
               // toServer.println("100:"+movieList);
                System.out.println("받아온 dateList : "+dateList);

                Message message = new Message();
                message.what = 200;
                message.obj = dateList;
                this.handler.sendMessage(message);
            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

            if( socket != null){
                socket.close();
                socket = null;
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
        loopFlag = false;
    }

}//end of class