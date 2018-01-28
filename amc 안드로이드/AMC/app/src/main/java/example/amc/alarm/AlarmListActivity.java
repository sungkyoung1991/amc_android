package example.amc.alarm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amc.service.domain.Alarm;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.Map;

import example.amc.R;
import example.amc.adapter.CancelAlarmListAdapter;
import example.amc.adapter.OpenAlarmListAdapter;
import example.amc.thread.DeleteAlarmThread;
import example.amc.util.EndAlertDialog;

/**
 * Created by bitcamp on 2017-11-09.
 */

public class AlarmListActivity extends Activity {

    ListView listView;
    OpenAlarmListAdapter openAlarmListAdapter;
    CancelAlarmListAdapter cancelAlarmListAdapter;
    String title;
    Boolean isCancelAlarm;
    DeleteAlarmThread deleteAlarmThread;
    int subPosition;
    BootstrapButton alarmListHeader;


    ArrayList<Alarm> alarmList;

    android.app.AlertDialog.Builder alertDialogBuilder;

    //토큰 등록 시도시 웹 통신결과
    private Handler deleteHandler = new Handler(){

        // Call Back Method Definition
        public void handleMessage(Message message){

            // Application Protocol : 100  ==> 정상
            if(message.what == 100){

                String fromHostData = ((Map<String,String>)message.obj).get("result");
                System.out.println("프롬 호스트 데이타2 : " + fromHostData);

                if(fromHostData.indexOf("실패") != -1){
                    Toast.makeText(AlarmListActivity.this, "삭제에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }else{
                    if(((Map<String, String>) message.obj).get("isDelete").equals("1")){
                        alarmList.remove(subPosition);
                        //어댑터에 항목이 바뀜을 알리기
                        if(isCancelAlarm){
                            cancelAlarmListAdapter.notifyDataSetChanged();
                        }else{
                            openAlarmListAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(AlarmListActivity.this, "이미 삭제된 목록입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(message.what == 999){
                new EndAlertDialog(AlarmListActivity.this).showDialog("[실패]","서버가 정상작동중이 아닙니다 잠시후 다시 시도해주세요");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmlist);

        listView = (ListView)findViewById(R.id.alarmList_listView);
        alarmListHeader = (BootstrapButton)findViewById(R.id.alarmListHeader);
        isCancelAlarm = false;

        Intent intent = getIntent();
        alarmList = (ArrayList<Alarm>)intent.getSerializableExtra("alarmList");

        System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        System.out.println(alarmList);

        //취소표알림 리스트인지, 오픈알림 리스트인지 체크한다(좌석정보가 있으면 취소표알림)
        //알림리스트가 있으면 보여주고 없으면 토스트 메시지 출력
        if(alarmList.size()>0){
            if(alarmList.get(0).getAlarmSeatNo() != null ){
                if(!alarmList.get(0).getAlarmSeatNo().equals("")){
                    cancelAlarmListAdapter = new CancelAlarmListAdapter(AlarmListActivity.this,alarmList);
                    listView.setAdapter(cancelAlarmListAdapter);
                    isCancelAlarm = true;
                    alarmListHeader.setText("취소표 알리미 리스트");
                }
            }else{
                openAlarmListAdapter = new OpenAlarmListAdapter(AlarmListActivity.this,alarmList);
                listView.setAdapter(openAlarmListAdapter);
                alarmListHeader.setText("티켓오픈 알리미 리스트");
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(AlarmListActivity.this, alarmList.get(i).getAlarmNo()+"", Toast.LENGTH_SHORT).show();

                    //타이틀 설정
                    if(alarmList.get(i).getScreenContent().getPreviewFlag().equals("Y")){
                        title = alarmList.get(i).getScreenContent().getPreviewTitle();
                    }else{
                        title = alarmList.get(i).getScreenContent().getMovie().getMovieNm();
                    }

                    //메세지 기초작업
                    String message = "제목 : "+ title +
                                     "\n상영일자 : \n"+dateChange(alarmList.get(i).getScreenContent().getScreenOpenTime());

                    //취소표 알리미면 좌석정보까지 추가
                    if(isCancelAlarm){
                        message += "\n신청좌석 : "+alarmList.get(i).getAlarmSeatNo();
                    }

                    message += "\n취소 하시겠습니까?";

                    alertDialogBuilder = new android.app.AlertDialog.Builder(AlarmListActivity.this);

                    // AlertDialog 상태값 SET
                    alertDialogBuilder.setTitle("알리미 취소");
                    alertDialogBuilder.setMessage(message);

                    //==> alert 창 이후 취소버튼 불가
                    alertDialogBuilder.setCancelable(false);

                    final int position = i;

                    alertDialogBuilder.setPositiveButton(	"예" ,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAlarm(alarmList.get(position).getAlarmNo());
                                    subPosition = position;
                                }
                            });

                    alertDialogBuilder.setNegativeButton(	"아니요" ,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            } );

                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });//아이템 클릭 리스너 End
        }else{ //알림리스트가 없음으로 토스트 메시지 출력
            Toast.makeText(AlarmListActivity.this, "등록된 알리미가 없습니다", Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }

    public String dateChange(String oldDate){

        String[] dateDiv = oldDate.split(" ");

        String[] ymd = dateDiv[0].split("-");
        String[] hms = dateDiv[1].split(":");

        /*return ymd[0]+"년 "+ymd[1]+"월 "+ymd[2]+"일 "+" "+hms[0]+"시 "+hms[1]+"분 "+hms[2].substring(0,2)+"초 ";*/
        return ymd[0]+"년 "+ymd[1]+"월 "+ymd[2]+"일 "+" "+hms[0]+"시 "+hms[1]+"분 ";
    }


    public void deleteAlarm(int alarmNo){

        deleteAlarmThread = new DeleteAlarmThread(deleteHandler,alarmNo);
        deleteAlarmThread.start();

    }
}
