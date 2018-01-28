package com.amc.service.domain;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable{

	private int userNo;
	private String userId;
	private String userName;
	private String password;
	private String role;
	private String addr;
	private String addrDetail;
	private Date userRegDate;
	private String birth;
	private String phone1;
	private String phone2;
	private String phone3;
	private String calendarType;
	private String gender;
	private String deleteUserFlag;
	private String uuId;
	private String snsLogin;
 	

	public User() {
	}
	
	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getAddrDetail() {
		return addrDetail;
	}
	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}
	public Date getUserRegDate() {
		return userRegDate;
	}
	public void setUserRegDate(Date userRegDate) {
		this.userRegDate = userRegDate;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getPhone3() {
		return phone3;
	}
	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}
	public String getCalendarType() {
		return calendarType;
	}
	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDeleteUserFlag() {
		return deleteUserFlag;
	}
	public void setDeleteUserFlag(String deleteUserFlag) {
		this.deleteUserFlag = deleteUserFlag;
	}
	public String getSnsLogin() {
		return snsLogin;
	}

	public void setSnsLogin(String snsLogin) {
		this.snsLogin = snsLogin;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	@Override
	public String toString() {
		return "User{" +
				"userNo=" + userNo +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				", addr='" + addr + '\'' +
				", addrDetail='" + addrDetail + '\'' +
				", userRegDate=" + userRegDate +
				", birth='" + birth + '\'' +
				", phone1='" + phone1 + '\'' +
				", phone2='" + phone2 + '\'' +
				", phone3='" + phone3 + '\'' +
				", calendarType='" + calendarType + '\'' +
				", gender='" + gender + '\'' +
				", deleteUserFlag='" + deleteUserFlag + '\'' +
				", uuId='" + uuId + '\'' +
				", snsLogin='" + snsLogin + '\'' +
				'}';
	}
}
