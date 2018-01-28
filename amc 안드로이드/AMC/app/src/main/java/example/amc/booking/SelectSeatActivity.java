package example.amc.booking;

/**
 * Created by koh on 2017-11-06.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import example.amc.R;
import example.amc.adapter.SeatAdapter;
import example.amc.thread.GetSeatThread;

public class SelectSeatActivity extends Activity {
    ///Field
    private ScrollView scrollView;
    private int numCol;
    private int numRow;
    private int headCount =0;
    private String selectedSeats;

    private int seatCount=0;

    Activity act = this;
    GridView gridView;
    TextView payTextView;
    SeatAdapter seatAdapter;
    List<String> seatList;
    Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    ///Constructor !
    public SelectSeatActivity(){
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){
                scrollView.post(new Runnable(){
                    public void run(){
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            }else if(message.what==200){
                System.out.println("핸들러에 온 메시지가 200프로토콜로 왔습니다.");

                Map<String, Object> seatMap = (Map<String, Object>)message.obj;
                seatList = (List<String>) seatMap.get("seats");
                System.out.println("▶seatList : "+seatMap.toString());
                numCol = (int)seatMap.get("rowLength");
                numCol = (int)seatMap.get("colLength");
                gridView.setNumColumns(numCol);
                System.out.println("▶seatList : "+seatList);
                System.out.println("▶colLength : "+numRow);
                System.out.println("▶rowLength : "+numCol);

                seatAdapter = new SeatAdapter(SelectSeatActivity.this, seatList);
                gridView.setAdapter(seatAdapter);

                //좌석 선택 : onItemClickListener를 추가
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        int seatY = i/numCol;
                        int seatX = i%numCol;


                        if (seatList.get(i).equals("1")) {
                            //Toast.makeText(SelectSeatActivity.this, "::i : " + i + " seatY : " + seatY + " :: seatX : " + seatX, Toast.LENGTH_SHORT).show();
                        }

                        if (seatList.get(i).equals("3")) {
                            //gridView.getChildAt(i).setBackgroundColor(Color.GREEN);
                            if(seatCount>0) {
                                view.setBackgroundResource(R.color.color_enable);
                                seatList.set(i, "1");
                                seatAdapter.notifyDataSetChanged();
                                seatCount--;
                            }

                        } else if (seatList.get(i).equals("1")) {
                            if(seatCount<4) {
                                view.setBackgroundResource(R.color.color_selected);
                                seatList.set(i, "3");
                                seatAdapter.notifyDataSetChanged();
                                seatCount++;
                            }else{
                                Toast.makeText(SelectSeatActivity.this, "4석까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }else{


                        }
                    }
                });

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_main);

        Intent intent = this.getIntent();

        // Intent 의 Message GET :==> editText View  출력
        final int screenContentNo  = intent.getIntExtra("screenContentNo", 0);
        System.out.println(getClass().getSimpleName()+"::고객님이 선택하신 상영번호:: "+screenContentNo);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.gridView = (GridView)findViewById(R.id.gridView1);

        //인원수를 먼저 선택하는 부분
        /*spinner = (Spinner) findViewById(R.id.spinner1); //butterknife 없을경우
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.question, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(SelectSeatActivity.this,
                        spinnerAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                headCount = position;
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });*/

        this.payTextView = (TextView)findViewById(R.id.pay_textview);
        payTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSeats="";
                for(int i=0;i<seatList.size();i++){
                    if(seatList.get(i).equals("3")){
                        int y = i/numCol;
                        int x =i%numCol;
                        selectedSeats += ","+y+","+x;

                    }
                }
                if(selectedSeats.length()>0){
                    Toast.makeText(SelectSeatActivity.this, "예매를 요청합니다:: "+selectedSeats.substring(1), Toast.LENGTH_SHORT).show();
                    //다음화면으로 이동하기
                    Intent intent = new Intent(SelectSeatActivity.this, AddBookingActivity.class);
                    intent.putExtra("selectedSeats",selectedSeats);
                    intent.putExtra("screenContentNo", screenContentNo);
                    startActivity(intent);
                }else{
                    Toast.makeText(SelectSeatActivity.this, "좌석을 선택하시기 바랍니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* ChatClientSocketThread 생성 및 Thread.start(); */
        new GetSeatThread(handler, screenContentNo+"").start();

    }

}
