package example.amc.rest;

import com.amc.service.domain.Booking;
import com.amc.service.domain.Movie;
import com.amc.service.domain.ScreenContent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookingRestHttpClient {
	///Field
	public ObjectMapper objectMapper;
	private String url = "http://183.98.215.171";

	///Constructor
	public BookingRestHttpClient(){
		this.objectMapper = new ObjectMapper();
	}
	///Method
	public BufferedReader httpGetRequest(String url) throws Exception{

		// HttpClient : Http Protocol 의 client 추상화
		HttpClient httpClient = new DefaultHttpClient();

		// HttpGet : Http Protocol 의 GET 방식 Request
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json");

		// HttpResponse : Http Protocol 응답 Message 추상화
		HttpResponse httpResponse = httpClient.execute(httpGet);

		//==> Response 중 entity(DATA) 확인
		HttpEntity httpEntity = httpResponse.getEntity();

		//==> InputStream 생성
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

		if( br == null){
			System.out.println("br is null !");
		}

		return br;

	}

	//1.2 Http Protocol GET Request : JsonSimple + codehaus 3rd party lib 사용
	public List<Movie> getScreenMovie() throws Exception{

		BufferedReader br = httpGetRequest(url+":8000/booking/json/getScreenMovieList");

		//==> API 확인 : Stream 객체를 직접 전달
		JSONArray jsonobj = (JSONArray)JSONValue.parse(br);
		List<Movie> movieList = objectMapper.readValue(jsonobj.toString(), new TypeReference<List<Movie>>(){});
		System.out.println("영화제목이 오나 : "+movieList.get(0).getMovieNm());

		return movieList;
	}

	public List<String> getScreenDate(String movieNo, int flag) throws Exception {

		BufferedReader br = httpGetRequest(url+":8000/booking/json/getScreenDateJSON/"+movieNo.trim()+"/"+flag);
		JSONArray jsonobj = (JSONArray) JSONValue.parse(br);
		List<String> dateList = objectMapper.readValue(jsonobj.toString(), new TypeReference<List<String>>(){});


		return dateList;
	}

	public List<ScreenContent> getScreenTime(String screenDate) throws Exception {

		BufferedReader br  = httpGetRequest(url+":8000/booking/json/getScreenTimeJSON/"+screenDate);
		JSONArray jsonobj = (JSONArray) JSONValue.parse(br);
		List<ScreenContent> timeList = objectMapper.readValue(jsonobj.toString(), new TypeReference<List<ScreenContent>>(){});

		return timeList;
	}

	public Map<String, Object> getSeats(String screenContentNo) throws Exception {

		BufferedReader br = httpGetRequest(url+":52273/JSONselectSeat?screenNo="+screenContentNo);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(br);
		List<String> seats = (List<String>)jsonObject.get("seats");
		System.out.println(":::: long rowLength : "+(long)jsonObject.get("rowLength")+":::: long colLength : "
				+(long)jsonObject.get("colLength")+":::: List<String> seats : "+seats.toString());

		Map<String, Object> seatMap = objectMapper.readValue(jsonObject.toString(), new TypeReference<Map<String, Object>>(){});
		return seatMap;
	}

	public Booking getBookingInfo(int screenContentNo, String selectedSeats) throws Exception {

		BufferedReader br = httpGetRequest(url+":8000/booking/json/getBookingInfo/"+screenContentNo+"/"+selectedSeats);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(br);
		Booking booking = objectMapper.readValue(jsonObject.toString(), new TypeReference<Booking>(){});
		return booking;
	}

	public Booking addBookingConfirm(Booking booking) throws Exception{

		String tpUrl = booking.getUserId().trim()+"/"+booking.getScreenContentNo()+"/"+booking.getBookingSeatNo()+"/"+booking.getImpId();

		BufferedReader br = httpGetRequest(url+":8000/booking/json/addBookingConfirm/"+tpUrl);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(br);
		Booking addedBooking = objectMapper.readValue(jsonObject.toString(), new TypeReference<Booking>(){});
		System.out.println("◆◆◆ addedBooking from RestClient.java : "+addedBooking);

		return addedBooking;
	}

    /*public Booking addBookingConfirm(Booking booking) throws Exception{

		Booking addedBooking = new Booking();
		//서버에서 받아올때 json이 아니어도 받아올 수 있다.
		String postUrl = url+":8080/booking/json/addBookingConfirm";
		HttpClient http = new DefaultHttpClient();
		try {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("bookingSeatNo", booking.getBookingSeatNo()));
			nameValuePairs.add(new BasicNameValuePair("impId", booking.getImpId()));
			nameValuePairs.add(new BasicNameValuePair("screenContentNo", Integer.toString(booking.getScreenContentNo())));

			HttpParams params = http.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);

			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "utf-8");

			httpPost.setEntity(entityRequest);

			HttpResponse responsePost = http.execute(httpPost);
			HttpEntity httpEntity = responsePost.getEntity();

			InputStream is = httpEntity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

			//==> API 확인 : Stream 객체를 직접 전달
			JSONArray jsonobj = (JSONArray) JSONValue.parse(br);
			System.out.println("JSON Simple Object : " + jsonobj);

			if( jsonobj == null){
				System.out.println("jsonObj가 null이군요");
			}

			JSONObject jsonObject = (JSONObject) JSONValue.parse(br);
			addedBooking = objectMapper.readValue(jsonObject.toString(), new TypeReference<Booking>(){});

		}catch(Exception e){e.printStackTrace();}

		return addedBooking;
	}*/

}
