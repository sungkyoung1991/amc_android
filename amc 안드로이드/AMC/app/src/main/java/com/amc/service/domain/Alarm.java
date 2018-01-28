package com.amc.service.domain;

import java.io.Serializable;

public class Alarm implements Serializable{

		int alarmNo;
		String alarmRegDate;
		String alarmSeatNo;
		String alarmFlag;
		User user;
		ScreenContent screenContent;
		public int getAlarmNo() {
			return alarmNo;
		}
		public void setAlarmNo(int alarmNo) {
			this.alarmNo = alarmNo;
		}
		public String getAlarmRegDate() {
			return alarmRegDate;
		}
		public void setAlarmRegDate(String alarmRegDate) {
			this.alarmRegDate = alarmRegDate;
		}
		public String getAlarmSeatNo() {
			return alarmSeatNo;
		}
		public void setAlarmSeatNo(String alarmSeatNo) {
			this.alarmSeatNo = alarmSeatNo;
		}
		public String getAlarmFlag() {
			return alarmFlag;
		}
		public void setAlarmFlag(String alarmFlag) {
			this.alarmFlag = alarmFlag;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public ScreenContent getScreenContent() {
			return screenContent;
		}
		public void setScreenContent(ScreenContent screenContent) {
			this.screenContent = screenContent;
		}
		@Override
		public String toString() {
			return "Alarm [alarmNo=" + alarmNo + ", alarmRegDate=" + alarmRegDate + ", alarmSeatNo=" + alarmSeatNo
					+ ", alarmFlag=" + alarmFlag + ", user=" + user + ", screenContent=" + screenContent + "]";
		}

}
