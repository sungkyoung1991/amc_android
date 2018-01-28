package example.amc.rest;



import com.amc.service.domain.Alarm;
import com.amc.service.domain.Movie;
import com.amc.service.domain.ScreenContent;
import com.amc.service.domain.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class RestHttpClient {
	///Field
	String serverUrl = "http://183.98.215.171:8000";
	///Constructor
	public RestHttpClient(){
	}
	///Method
	//1.2 Http Protocol GET Request : JsonSimple + codehaus 3rd party lib 사용
	public User getJsonUser(String email, String password) throws Exception{

		// HttpClient : Http Protocol 의 client 추상화
		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/user/json/androidGetUser?" +
					"email="+email.trim()+"&password="+password.trim();

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String decodeJson = br.readLine();
		if(decodeJson == null){
			return null;
		}
		decodeJson = URLDecoder.decode(decodeJson,"UTF-8");
		JSONObject jsonobj = (JSONObject)JSONValue.parse(decodeJson);

		System.out.println("JSON Simple Object : " + jsonobj);

		if( jsonobj == null){
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(jsonobj.toString(), User.class);
		System.out.println(user);

		return user;
	}

	public User getJsonUserKakao(String email, Boolean kakaoLogin) throws Exception{

		// HttpClient : Http Protocol 의 client 추상화
		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url = serverUrl+"/user/json/androidGetUser?" +
				"email=" + email.trim() + "&kakaoLogin=" + kakaoLogin;

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);

		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");
			// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		//==> 다른 방법으로 serverData 처리
		/*System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		String decodeJson = br.readLine();
		decodeJson = URLDecoder.decode(decodeJson,"UTF-8");
		JSONObject jsonobj = (JSONObject)JSONValue.parse(decodeJson);

		//==> API 확인 : Stream 객체를 직접 전달
		System.out.println("JSON Simple Object : " + jsonobj);

		if (jsonobj == null) {
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(jsonobj.toString(), User.class);
		System.out.println(user);

		return user;
	}

	public String addPushToken(String userId, String token) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/user/json/addUuid?" +
				"token="+token.trim()+"&userId="+userId.trim();

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String result = br.readLine();
		System.out.println("addPushToken 요청 결과 " + result);

		if( !result.equals("success")){
			return null;
		}

		return result;
	}

	public List<Movie> getMovieList(String userId, String menu) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/movie/json/androidGetMovieList?" +
				"userId="+userId.trim()+"&menu="+menu.trim();

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String result = br.readLine();

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("겟무비리스트 리절트::"+result);

		if(result.equals("")){
			return null;
		}

		JSONObject jsonObject = (JSONObject)JSONValue.parse(result);

		ObjectMapper objectMapper = new ObjectMapper();

		JSONArray jsonArray = (JSONArray)jsonObject.get("list");

		List<Movie> movieList = new ArrayList<Movie>();

		for(int i = 0; i<jsonArray.size(); i++){
			movieList.add(objectMapper.readValue(jsonArray.get(i).toString(), Movie.class));
		}

		System.out.println("받은 movieList " + movieList);

		if( movieList == null){
			return null;
		}

		return movieList;
	}

	public List<Alarm> getAlarmList(String userId, String alarmFlag) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url = "";

		if(alarmFlag.equals("O")){
			url = serverUrl+"/alarm/json/getAndroidOpenAlarmList/" + userId.trim()+"?alarmFlag="+alarmFlag;
		}else{
			url = serverUrl+"/alarm/json/getAndroidCancelAlarmList/" + userId.trim()+"?alarmFlag="+alarmFlag;
		}

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpPost httpPost = new HttpPost(url);

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpPost);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String result = br.readLine();

		System.out.println("알람에서 result :: " + result);

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("알람에서 result 디코딩 ::" + result);

		System.out.println("겟알람리스트 리절트::"+result);

		if(result.equals("")){
			return null;
		}

		JSONObject jsonObject = (JSONObject)JSONValue.parse(result);

		System.out.println(JSONValue.parse(result));
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println(jsonObject.toString());

		JSONArray jsonArray = (JSONArray)jsonObject.get("list");

		List<Alarm> alarmList = new ArrayList<Alarm>();

		ObjectMapper objectMapper = new ObjectMapper();

		for(int i = 0; i<jsonArray.size(); i++){
			alarmList.add(objectMapper.readValue(jsonArray.get(i).toString(), Alarm.class));
		}

		System.out.println("받은 alarmList " + alarmList);

		if( alarmList == null){
			return null;
		}

		return alarmList;
	}

	public Movie getMovie(String menu, String movieNo) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url = serverUrl+"/movie/json/androidGetMovie?menu="+menu+"&movieNo="+movieNo;

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpPost httpPost = new HttpPost(url);

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpPost);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String result = br.readLine();

		System.out.println("겟무비 result :: " + result);

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("겟무비 result 디코딩 ::" + result);

		if(result.equals("")){
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();

		Movie movie = objectMapper.readValue(result,Movie.class);

		System.out.println("받은 movie " + movie);

		if( movie == null){
			return null;
		}

		return movie;
	}

	public List<ScreenContent> getPreviewList(String userId, String menu) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/movie/json/androidGetPreviewList?";

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		//==> 다른 방법으로 serverData 처리
/*		System.out.println("[ Server 에서 받은 Data 확인 ] ");
		String serverData = br.readLine();
		System.out.println(serverData);*/

		//==> API 확인 : Stream 객체를 직접 전달
		String result = br.readLine();

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("겟무비리스트 리절트::"+result);

		if(result.equals("")){
			return null;
		}

		JSONObject jsonObject = (JSONObject)JSONValue.parse(result);

		ObjectMapper objectMapper = new ObjectMapper();

		JSONArray jsonArray = (JSONArray)jsonObject.get("list");

		List<ScreenContent> previewList = new ArrayList<ScreenContent>();

		for(int i = 0; i<jsonArray.size(); i++){
			previewList.add(objectMapper.readValue(jsonArray.get(i).toString(), ScreenContent.class));
		}

		System.out.println("받은 preivewList " + previewList);

		if( previewList == null){
			return null;
		}

		return previewList;
	}

	public String deleteAlarm(int alarmNo) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/alarm/json/deleteAndroidAlarm/"+alarmNo;

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		//httpGet.setHeader("Accept", "application/json");
		//httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		String result = br.readLine();

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("딜리트 알람 리절트::"+result);

		if(result.equals("")){
			return null;
		}

		if( result == null){
			return null;
		}
		return result;
	}

	public String updateUser(User user) throws Exception{

		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setParameter("http.connection.timeout", 5000);
		httpClient.getParams().setParameter("http.socket.timeout", 5000);

		String url=	serverUrl+"/user/json/updateAndroidUser/";

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpPost httpPost = new HttpPost(url);
		//httpGet.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

		ObjectMapper objectMapper01 = new ObjectMapper();
		String jsonValue = objectMapper01.writeValueAsString(user);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user",jsonValue);

		HttpEntity requestHttpEntity = new StringEntity(jsonObject.toString(),"UTF-8");
		httpPost.setEntity(requestHttpEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);

		System.out.println(httpResponse);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		String result = br.readLine();

		result = URLDecoder.decode(result,"UTF-8");

		System.out.println("유저 업데이트 리절트::"+result);

		if(result.equals("")){
			return null;
		}

		if( result == null){
			return null;
		}
		return result;
	}

}