package example.amc.movie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amc.service.domain.Movie;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Arrays;
import java.util.List;

import example.amc.util.DeveloperKey;
import example.amc.R;

public class GetMovieActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	///Field
	private String serverUrl;
	private Movie movie;
	private ImageView posterImageView;
	private TextView movieNmTextView;
	private TextView directorsTextView;
	private TextView actorsTextView;
	private TextView genresTextView;
	private TextView watchGradeNmTextView;
	private TextView synopsisTextView;
	private ImageView steelCutFirstImageView;
	private ImageView steelCutSecondImageView;
	private ImageView steelCutThirdImageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getmovie);

		Intent intent = getIntent();
		serverUrl = "http://192.168.0.32:8080/images/movie/";
		movie = (Movie)intent.getSerializableExtra("movie");

		posterImageView = (ImageView)findViewById(R.id.poster);
		movieNmTextView = (TextView)findViewById(R.id.movieNm);
		directorsTextView = (TextView)findViewById(R.id.directors);
		actorsTextView = (TextView)findViewById(R.id.actors);
		genresTextView = (TextView)findViewById(R.id.genres);
		watchGradeNmTextView = (TextView)findViewById(R.id.watchGradeNm);
		synopsisTextView = (TextView)findViewById(R.id.synopsis);
		steelCutFirstImageView = (ImageView)findViewById(R.id.steelCutFirst);
		steelCutSecondImageView = (ImageView)findViewById(R.id.steelCutSecond);
		steelCutThirdImageView = (ImageView)findViewById(R.id.steelCutThird);

		//이미지 파일주소이름이 없거나, null일 경우
		if(movie.getPostUrl().equals("") || movie.getPostUrl() == null){
			posterImageView.setImageResource(R.mipmap.amc);
		}
		Glide.with(GetMovieActivity.this).load(movie.getPostUrl()).into(posterImageView);

		movieNmTextView.setText(movie.getMovieNm());
		directorsTextView.setText(movie.getDirectors());
		actorsTextView.setText(movie.getActors());
		genresTextView.setText(movie.getGenres());
		watchGradeNmTextView.setText(movie.getWatchGradeNm());
		synopsisTextView.setText(movie.getSynopsis());


		String steelCuts = movie.getSteelCut();
		System.out.println("steelCut" + steelCuts);

		steelCuts = null2str(steelCuts);
		System.out.println("steelCuts null converted :: what ??" + steelCuts);

		if (!steelCuts.isEmpty()) {
			List<String> steelCutList = Arrays.asList(steelCuts.split(","));

			System.out.println("steelCutList :: " + steelCutList);
			System.out.println("steelCutList length :: " + steelCutList.size());
			System.out.println("steelCutList isEmpty() :: " + steelCutList.isEmpty());

			if (!steelCutList.isEmpty()) {
				if (steelCutList.size() == 1) {
					System.out.println("사이즈 1");
					String steelCut1 = steelCutList.get(0).toString();
					movie.setSteelCut1(steelCut1);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut1()).into(steelCutFirstImageView);

				} else if (steelCutList.size() == 2) {
					System.out.println("사이즈 2");
					String steelCut1 = steelCutList.get(0).toString();
					movie.setSteelCut1(steelCut1);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut1()).into(steelCutFirstImageView);

					String steelCut2 = steelCutList.get(1).toString();
					movie.setSteelCut2(steelCut2);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut2()).into(steelCutSecondImageView);

				} else if (steelCutList.size() == 3) {
					System.out.println("사이즈 3");
					String steelCut1 = steelCutList.get(0).toString();
					movie.setSteelCut1(steelCut1);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut1()).into(steelCutFirstImageView);

					String steelCut2 = steelCutList.get(1).toString();
					movie.setSteelCut2(steelCut2);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut2()).into(steelCutSecondImageView);

					String steelCut3 = steelCutList.get(2).toString();
					movie.setSteelCut3(steelCut3);
					Glide.with(GetMovieActivity.this).load(serverUrl+movie.getSteelCut3()).into(steelCutThirdImageView);
				}
			}
		}

		//이미지 파일주소이름이 없거나, null일 경우
		if(movie.getSteelCut1() == null || movie.getSteelCut1().equals("")){
			posterImageView.setImageResource(R.mipmap.amc);
		}


		//이미지 파일주소이름이 없거나, null일 경우
		if( movie.getSteelCut2() == null || movie.getSteelCut2().equals("")){
			posterImageView.setImageResource(R.mipmap.amc);
		}


		//이미지 파일주소이름이 없거나, null일 경우
		if(movie.getSteelCut3() == null || movie.getSteelCut3().equals("")){
			posterImageView.setImageResource(R.mipmap.amc);
		}

		Log.d("youtube Test",
				"사용가능여부:"+ YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this)); //SUCCSESS

		getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY,this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
										YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			//player.cueVideo("wKJ9KzGQq0w"); //video id.



			if(movie.getTrailer().startsWith("http://www.youtube.com/embed/")){
				String movieId ="";
				System.out.println(movie.getTrailer().split("embed/")[1]);
				movieId = movie.getTrailer().split("embed/")[1];
				player.cueVideo(movieId);  //http://www.youtube.com/watch?v=IA1hox-v0jQ
			}

			//cueVideo(String videoId)
			//지정한 동영상의 미리보기 이미지를 로드하고 플레이어가 동영상을 재생하도록 준비하지만
			//play()를 호출하기 전에는 동영상 스트림을 다운로드하지 않습니다.
			//https://developers.google.com/youtube/android/player/reference/com/google/android/youtube/player/YouTubePlayer
		}
	}

	//플레이어가 초기화되지 못할 때 호출됩니다.
	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
										YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
		}
	}

	public static String null2str(String org, String converted) {
		if (org == null || org.trim().length() == 0)
			return converted;
		else
			return org.trim();
	}

	public static String null2str(String org) {
		return null2str(org, "");
	}

	@Override
	protected void onDestroy() {
		System.out.println("겟무비 디스트로이");
		super.onDestroy();
	}

}// end of Activity
