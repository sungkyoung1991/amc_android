package example.amc.thread;

import android.os.Handler;
import android.os.Message;

import com.amc.service.domain.User;

import org.apache.http.conn.ConnectTimeoutException;

import java.util.HashMap;
import java.util.Map;

import example.amc.rest.RestHttpClient;

/*
 *  Server 와 통신하는 Thread
 */
public class LoginCheckThread extends Thread{

	///Field
	private Handler handler;
	private String userId;
	private String password;
	private Boolean kakaoLogin;

	///Constructor
	private LoginCheckThread(){
	}

	public LoginCheckThread(Handler handler , String userId, String password){
		this.handler = handler;
		this.userId = userId;
		this.password = password;
        this.kakaoLogin = false;
	}

	public LoginCheckThread(Handler handler , String userId, Boolean kakaoLogin){
		this.handler = handler;
		this.userId = userId;
		this.kakaoLogin = kakaoLogin;
        this.password = null;
	}

	///Method
	public void run(){

		System.out.println("[Client Thread ] : "+getClass().getSimpleName()+".run()  START.................");

		try {
			RestHttpClient restHttpClient = new  RestHttpClient();

			User user = null;

			if(kakaoLogin){
				user = restHttpClient.getJsonUserKakao(userId, kakaoLogin);
			}else{
				user = restHttpClient.getJsonUser(userId,password);
			}

			System.out.println("쓰레드 안의 유저 : "+user);

			if(user == null){
				System.out.println("유저가 널입니다");
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
				System.out.println("유저가 널이 아닙니다 : "+user.toString());

				Message message = new Message();
				message.what = 100;

				Map<String, Object> map = new HashMap<>();
				map.put("result","성공");
				map.put("userId",this.userId);
				map.put("user",user);
				message.obj = map;

				this.handler.sendMessage(message);
				//toServer.println("100:"+clientName);
				//loopFlag = true;
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

	// Activity Life Cycle 이해 / Activity.onDestiry() 에서 Call
	// Server 로부터 Data 받는 무한 Loop 종료
	public void onDestroy() {
		System.out.println(":: LoginCheckThread.onDestroy()");
	}

}//end of class