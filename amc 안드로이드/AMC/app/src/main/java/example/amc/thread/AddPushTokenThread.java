package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.User;

import org.apache.http.conn.ConnectTimeoutException;

import java.util.HashMap;
import java.util.Map;

import example.amc.rest.RestHttpClient;

/**
 * Created by bitcamp on 2017-10-31.
 */

public class AddPushTokenThread extends Thread {

    private String token;
    private String userId;
    private Handler handler;

    public AddPushTokenThread(){

    }

    public AddPushTokenThread(Handler handler,String userId, String token){
        this.handler = handler;
        this.userId = userId;
        this.token = token;
    }

    public void run(){

        System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

        try {
            RestHttpClient restHttpClient = new  RestHttpClient();

            //==> getJsonUser01() :  JsonSimple 3rd party lib 사용
            //==> getJsonUser02() : JsonSimple + codehaus 3rd party lib 사용
            //==> 주석처리하여 두 경우 다 사용해 보세요.
            //User user = restHttpClient.getJsonUser01(clientName);
            String result = restHttpClient.addPushToken(userId,token);
            System.out.println("쓰레드 안의 Result : "+result);

            if(result.equals("fail")){
                System.out.println("애드푸쉬가 널입니다");
                // 다른 Thread 의 UI 수정시 Thread 간 통신을 담당하는 Handler 사용
                // Handler 통해 전달 할 Data 는 Message Object 생성 전달
                Message message = new Message();
                // Application Protocol : 100 ==> 정상
                message.what = 100;
                // 전달할 Data 가 많으면 : Domain Object
                Map<String, String> map = new HashMap<>();
                map.put("result","실패");
                message.obj = map;
                // Hander 에게 Message 전달
                this.handler.sendMessage(message);
            }else{
                System.out.println("애드푸쉬가 널이 아닙니다 : "+result);
                Message message = new Message();
                message.what = 100;
                Map<String, String> map = new HashMap<>();
                map.put("result","성공");
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
