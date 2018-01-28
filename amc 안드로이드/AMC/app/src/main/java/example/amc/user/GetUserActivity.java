package example.amc.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amc.service.domain.User;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.net.URLDecoder;
import java.util.Map;

import example.amc.R;
import example.amc.thread.UpdateUserThread;
import example.amc.util.EndAlertDialog;

public class GetUserActivity extends AppCompatActivity {

	///Field
	private EditText userIdEditText;
	private EditText userNameEditText;
	private EditText genderEditText;
	private EditText addrEditText;
	private EditText addrDetailEditText;
	private EditText phone1EditText;
	private EditText phone2EditText;
	private EditText phone3EditText;
	private EditText userRegDateEditText;
	/*private Button modify;
	private Button apply;*/
	private BootstrapButton modify;
	private BootstrapButton apply;

	private User user;

	private UpdateUserThread updateUserThread;

	//영화 리스트 시도시 웹 통신결과
	private Handler updateUserHandler = new Handler(){

		// Call Back Method Definition
		public void handleMessage(Message message){

			// Application Protocol : 100  ==> 정상
			if(message.what == 100){

				String fromHostData = (String)((Map<String,Object>)message.obj).get("result");
				System.out.println("프롬 호스트 데이타2 : " + fromHostData);


				if(fromHostData.indexOf("실패") != -1){

					Toast.makeText(GetUserActivity.this, "회원정보 수정 실패", Toast.LENGTH_LONG).show();

					editTextClear();


				}else{
					Toast.makeText(GetUserActivity.this, "회원정보 수정 성공", Toast.LENGTH_LONG).show();

					editTextClear();
				}
			}

			if(message.what == 999){

				editTextClear();
				new EndAlertDialog(GetUserActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getuser);

		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user");

		userIdEditText = (EditText)findViewById(R.id.userIdEditText);
		userNameEditText = (EditText)findViewById(R.id.userNameEditText);
		genderEditText = (EditText)findViewById(R.id.genderEditText);
		addrEditText = (EditText)findViewById(R.id.addrEditText);
		addrDetailEditText = (EditText)findViewById(R.id.addrDetailEditText);
		phone1EditText = (EditText) findViewById(R.id.phone1EditText);
		phone2EditText = (EditText)findViewById(R.id.phone2EditText);
		phone3EditText = (EditText)findViewById(R.id.phone3EditText);
		userRegDateEditText = (EditText)findViewById(R.id.userRegDateEditText);

		modify = (BootstrapButton)findViewById(R.id.modify);
		apply = (BootstrapButton)findViewById(R.id.apply);

		apply.setVisibility(View.GONE);
		modify.setVisibility(View.VISIBLE);


        try{
            userIdEditText.setText(URLDecoder.decode(user.getUserId(),"UTF-8"));
			userIdEditText.setFocusable(false);
			userIdEditText.setClickable(false);

            userNameEditText.setText(URLDecoder.decode(user.getUserName(),"UTF-8"));
			userNameEditText.setFocusable(false);
			userNameEditText.setClickable(false);

            genderEditText.setText(URLDecoder.decode(user.getGender(),"UTF-8"));
			genderEditText.setFocusable(false);
			genderEditText.setClickable(false);

            addrEditText.setText(URLDecoder.decode(user.getAddr(),"UTF-8"));
			addrEditText.setFocusable(false);
			addrEditText.setClickable(false);

            addrDetailEditText.setText(URLDecoder.decode(user.getAddrDetail(),"UTF-8"));
			addrDetailEditText.setFocusable(false);
			addrDetailEditText.setClickable(false);

            phone1EditText.setText(URLDecoder.decode(user.getPhone1(),"UTF-8"));
			phone1EditText.setFocusable(false);
			phone1EditText.setClickable(false);

            phone2EditText.setText(URLDecoder.decode(user.getPhone2(),"UTF-8"));
			phone2EditText.setFocusable(false);
			phone2EditText.setClickable(false);

            phone3EditText.setText(URLDecoder.decode(user.getPhone3(),"UTF-8"));
			phone3EditText.setFocusable(false);
			phone3EditText.setClickable(false);

            userRegDateEditText.setText(URLDecoder.decode(user.getUserRegDate().toString(),"UTF-8"));
			userRegDateEditText.setFocusable(false);
			userRegDateEditText.setClickable(false);
        }catch(Exception e){
            e.printStackTrace();
        }

        //수정 클릭
        modify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//수정 가능한 것 표시
				int color = Color.parseColor("#ff0000");

				userNameEditText.setFocusableInTouchMode(true);
				userNameEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				addrEditText.setFocusableInTouchMode(true);
				addrEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				addrDetailEditText.setFocusableInTouchMode(true);
				addrDetailEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				phone1EditText.setFocusableInTouchMode(true);
				phone1EditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				phone2EditText.setFocusableInTouchMode(true);
				phone2EditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				phone3EditText.setFocusableInTouchMode(true);
				phone3EditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				//수정 불가능한 것 표시
				color = Color.parseColor("#cccccc");
				userIdEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				genderEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				userRegDateEditText.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

				view.setVisibility(View.GONE);
				apply.setVisibility(View.VISIBLE);
			}
		});

		//적용클릭릭
	apply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try{

					User user = new User();

					user.setUserId(userIdEditText.getText().toString());
					user.setUserName(userNameEditText.getText().toString());
					user.setAddr(addrEditText.getText().toString());
					user.setAddrDetail(addrDetailEditText.getText().toString());
					user.setPhone1(phone1EditText.getText().toString());
					user.setPhone2(phone2EditText.getText().toString());
					user.setPhone3(phone3EditText.getText().toString());

					updateUserThread = new UpdateUserThread(updateUserHandler,user);
					updateUserThread.start();

					view.setVisibility(View.GONE);
					modify.setVisibility(View.VISIBLE);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		System.out.println("겟유저 디스트로이");
		super.onDestroy();
	}

	public void editTextClear(){

		//수정 가능한 것들 터치 불가능상태+밑줄상태 초기화
		userNameEditText.setFocusableInTouchMode(false);
		userNameEditText.clearFocus();
		userNameEditText.getBackground().clearColorFilter();

		addrEditText.setFocusableInTouchMode(false);
		addrEditText.clearFocus();
		addrEditText.getBackground().clearColorFilter();

		addrDetailEditText.setFocusableInTouchMode(false);
		addrDetailEditText.clearFocus();
		addrDetailEditText.getBackground().clearColorFilter();

		phone1EditText.setFocusableInTouchMode(false);
		phone1EditText.clearFocus();
		phone1EditText.getBackground().clearColorFilter();

		phone2EditText.setFocusableInTouchMode(false);
		phone2EditText.clearFocus();
		phone2EditText.getBackground().clearColorFilter();

		phone3EditText.setFocusableInTouchMode(false);
		phone3EditText.clearFocus();
		phone3EditText.getBackground().clearColorFilter();

		//수정 불가능한 것들 밑줄상태 초기화
		userIdEditText.getBackground().clearColorFilter();
		genderEditText.getBackground().clearColorFilter();
		userRegDateEditText.getBackground().clearColorFilter();
	}

}// end of Activity
