package example.amc.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;

import example.amc.LobbyActivity;
import example.amc.R;
import example.amc.util.EndToast;

public class LoginActivity extends AppCompatActivity {

	///Field
	private BootstrapButton buttonLogin;
	private BootstrapButton buttonSignUp;
	private EditText editTextEmail;
	private EditText editTextPassword;
	private CheckBox checkBoxAutoLogin;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	Boolean autoLogin;
	public static Activity logActivity;

	//카카오
	private SessionCallback callback;
	Boolean kakaoLogin;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		logActivity = LoginActivity.this;

		String token = FirebaseInstanceId.getInstance().getToken();
		System.out.println("여기는 로그인 토큰 "+token);

		this.getAppKeyHash();

		this.buttonLogin = (BootstrapButton) findViewById(R.id.loginButton);
		this.buttonSignUp = (BootstrapButton) findViewById(R.id.signButton);
		this.editTextEmail = (EditText)findViewById(R.id.editText_email);
		this.editTextPassword = (EditText)findViewById(R.id.editText_password);
		this.checkBoxAutoLogin = (CheckBox)findViewById(R.id.checkBox);

		//app종속 데이터
		this.pref = getSharedPreferences("login", Activity.MODE_PRIVATE);
		this.editor = pref.edit();
		this.autoLogin = pref.getBoolean("autoLogin",false);
		this.kakaoLogin = pref.getBoolean("kakaoLogin",false);




		callback = new SessionCallback();
		Session.getCurrentSession().addCallback(callback);
		LoginButton loginButton;

		loginButton = (LoginButton)findViewById(R.id.com_kakao_login);
		loginButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(!isConnected()){
						Toast.makeText(LoginActivity.this,"인터넷 연결을 확인해주세요",Toast.LENGTH_SHORT).show();
					}
				}

				if(isConnected()){
					return false;
				}else{
					return true;
				}
			}
		});


		//현재 로그인 상태를 확인하는 토스트
		/*Toast.makeText(this, "kakao Status->"+kakaoLogin+"\n autoLogin->"+autoLogin, Toast.LENGTH_LONG).show();*/

		//카카오 로그인 상태인지 체크
		if(kakaoLogin){
			Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
			startActivity(intent);
		}

		//자동 로그인 상태인지 체크(로그인 성공,실패 상관없이 아이디,비번,비밀번호를 기억할 수 있도록 함)
		if (autoLogin) {
			editTextEmail.setText(pref.getString("userId", ""));
			editTextPassword.setText(pref.getString("password", ""));
			checkBoxAutoLogin.setChecked(true);
			if(!pref.getBoolean("loginFail",false)){
				Intent intent = new Intent(LoginActivity.this, LoginCheckActivity.class);

				// :: Activity 이동시 전달할 정보(Message) 가 있다면 저장
				//	  API 확인 : putExtra(name, value)
				if(autoLogin){
					editor.putString("userId",editTextEmail.getText().toString());
					editor.putString("password",editTextPassword.getText().toString());
					editor.putBoolean("autoLogin",checkBoxAutoLogin.isChecked());
					editor.commit();
				}
				intent.putExtra("userId", editTextEmail.getText().toString());
				intent.putExtra("password",editTextPassword.getText().toString());

				// :: intent 객체가 갖는 정보의 컴포넌트 호출
				startActivity(intent);
			}
		} else {

		}



		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// :: 다른 컴포넌트로 이동 위해
				//    컴포넌트를 생산할수 있는 Class 정보를
				//    Intent 생성자를 통해전달 intent instance 생성
				Intent intent = new Intent(LoginActivity.this, LoginCheckActivity.class);

				// :: Activity 이동시 전달할 정보(Message) 가 있다면 저장
				//	  API 확인 : putExtra(name, value)
				if(autoLogin){
					editor.putString("userId",editTextEmail.getText().toString());
					editor.putString("password",editTextPassword.getText().toString());
					editor.putBoolean("autoLogin",checkBoxAutoLogin.isChecked());
					editor.commit();
				}
				intent.putExtra("userId", editTextEmail.getText().toString());
				intent.putExtra("password",editTextPassword.getText().toString());

				// :: intent 객체가 갖는 정보의 컴포넌트 호출
				startActivity(intent);
			}
		});

		buttonSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Uri uri = Uri.parse("http://192.168.0.32:8080");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				startActivity(intent);
			}
		});

		//체크박스 상태 확인하기
		checkBoxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					autoLogin = true;
				} else {
					// if unChecked, removeAll
					autoLogin = false;
					editor.clear();
					editor.commit();
				}
			}
		});

	}



	private void getAppKeyHash() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				System.out.println("Hash key : "+something);
			}
		} catch (Exception e) {
	// TODO Auto-generated catch block
			System.out.println("name not found :" + e.toString());
		}
	}



/*	@Override
	// Activity Life Cycle 의 이해
	protected void onPause() {
		super.onPause();
		System.out.println(getClass().getSimpleName()+"LogonActivity.onPause()");
		//Activity 종료
		finish();
	}*/

	////////////////////////////////////////////////////////////////////////////////////
	// 종료 EndToast Bean 사용
	EndToast endToast = new EndToast(this);

	// Call Back Method 이용 취소버튼이용 App. 종료
	@Override
	public void onBackPressed() {

		// 종료 EndToast Bean 사용
		endToast.showEndToast("'취소' 버튼 한번더 누르시면 종료합니다. ");

	}
	////////////////////////////////////////////////////////////////////////////////////


	//인터넷 연결상태 확인
	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	private class SessionCallback implements ISessionCallback {

		@Override
		public void onSessionOpened() {
			//access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
			if(Session.getCurrentSession().isOpened()){ // 한 번더 세션을 체크해주었습니다.
				requestMe();
			}
		}

		@Override
		public void onSessionOpenFailed(KakaoException exception) {
			if(exception != null) {
				Logger.e(exception);
			}
		}
	}


	private void requestMe() {

		UserManagement.requestMe(new MeResponseCallback() {
			@Override
			public void onFailure(ErrorResult errorResult) {
				Log.e("onFailure", errorResult + "");
			}

			@Override
			public void onSessionClosed(ErrorResult errorResult) {
				Log.e("onSessionClosed",errorResult + "");
			}

			@Override
			public void onSuccess(UserProfile userProfile) {
				System.out.println("■■■■■■■■■■성공성공성공■■■■■■■■■■■");
				Log.e("onSuccess",userProfile.toString());
				System.out.println(userProfile.getEmail());

				Intent intent = new Intent(LoginActivity.this, LoginCheckActivity.class);
				intent.putExtra("userId",userProfile.getEmail());
				intent.putExtra("kakaoLogin",true);
				startActivity(intent);
				finish();
			}

			@Override
			public void onNotSignedUp() {
				Log.e("onNotSignedUp","onNotSignedUp");
			}
		});
	}


	@Override
	protected void onDestroy() {
		System.out.println("디스트로잉");
		super.onDestroy();
		Session.getCurrentSession().removeCallback(callback);
	}



}// end of Activity
