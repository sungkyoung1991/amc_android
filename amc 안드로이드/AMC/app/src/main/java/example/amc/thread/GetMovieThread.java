package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.Movie;

import org.apache.http.conn.ConnectTimeoutException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.amc.rest.RestHttpClient;

/**
 * Created by bitcamp on 2017-10-31.
 */

public class GetMovieThread extends Thread {

    private String menu;
    private String movieNo;
    private Handler handler;

    public GetMovieThread(){

    }

    public GetMovieThread(Handler handler, String menu, String movieNo){
        this.handler = handler;
        this.movieNo = movieNo;
        this.menu = menu;
    }

    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try {
            RestHttpClient restHttpClient = new  RestHttpClient();

            //==> getJsonUser01() :  JsonSimple 3rd party lib 사용
            //==> getJsonUser02() : JsonSimple + codehaus 3rd party lib 사용
            //==> 주석처리하여 두 경우 다 사용해 보세요.
            Movie movie = restHttpClient.getMovie(menu,movieNo);
            System.out.println("쓰레드 안의 Result : "+movie);

            if(movie == null){
                System.out.println("영화 리스트가 NULL");
                // 다른 Thread 의 UI 수정시 Thread 간 통신을 담당하는 Handler 사용
                // Handler 통해 전달 할 Data 는 Message Object 생성 전달
                Message message = new Message();
                // Application Protocol : 100 ==> 정상
                message.what = 100;
                // 전달할 Data 가 많으면 : Domain Object
                Map<String, Object> map = new HashMap<>();
                map.put("result","없음");
                message.obj = map;
                // Hander 에게 Message 전달
                this.handler.sendMessage(message);
            }else{
                System.out.println("영화 NOT NULL :: "+movie);
                Message message = new Message();
                message.what = 100;
                Map<String, Object> map = new HashMap<>();
                map.put("result","있음");
                map.put("movie",movie);
                message.obj = map;
                this.handler.sendMessage(message);
            }
        }catch(ConnectTimeoutException e){
            Message message = new Message();
            message.what = 999;

            this.handler.sendMessage(message);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run() END.................");

    }//end of run()

}
