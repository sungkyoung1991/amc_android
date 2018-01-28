package com.amc.service.domain;

import java.io.Serializable;

public class ScreenContent implements Serializable{

	private WishList wishList;
	private int screenContentNo;
	// 스크린이 삭제되고 movie가 생김
	private Movie movie;
	private String screenDate;
	private String screenOpenTime;
	private String screenEndTime;
	private String screenTheater;
	private int ticketPrice;
	private String previewFlag;
	private String previewTitle;
	private String ticketOpenDate;
	private String inviteActor;
	private String screenContentRegDate;

	public int getScreenContentNo() {
		return screenContentNo;
	}
	public void setScreenContentNo(int screenContentNo) {
		this.screenContentNo = screenContentNo;
	}
	public Movie getMovie() {
		return movie;
	}
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public String getScreenDate() {
		return screenDate;
	}
	public void setScreenDate(String screenDate) {
		this.screenDate = screenDate;
	}
	public String getScreenOpenTime() {
		return screenOpenTime;
	}
	public void setScreenOpenTime(String screenOpenTime) {
		this.screenOpenTime = screenOpenTime;
	}
	public String getScreenEndTime() {
		return screenEndTime;
	}
	public void setScreenEndTime(String screenEndTime) {
		this.screenEndTime = screenEndTime;
	}
	public String getScreenTheater() {
		return screenTheater;
	}
	public void setScreenTheater(String screenTheater) {
		this.screenTheater = screenTheater;
	}
	public int getTicketPrice() {
		return ticketPrice;
	}
	public void setTicketPrice(int ticketPrice) {
		this.ticketPrice = ticketPrice;
	}
	public String getPreviewFlag() {
		return previewFlag;
	}
	public void setPreviewFlag(String previewFlag) {
		this.previewFlag = previewFlag;
	}
	public String getPreviewTitle() {
		return previewTitle;
	}
	public void setPreviewTitle(String previewTitle) {
		this.previewTitle = previewTitle;
	}
	public String getTicketOpenDate() {
		return ticketOpenDate;
	}
	public void setTicketOpenDate(String ticketOpenDate) {
		this.ticketOpenDate = ticketOpenDate;
	}
	public String getInviteActor() {
		return inviteActor;
	}
	public void setInviteActor(String inviteActor) {
		this.inviteActor = inviteActor;
	}
	public String getScreenContentRegDate() {
		return screenContentRegDate;
	}
	public void setScreenContentRegDate(String screenContentRegDate) {
		this.screenContentRegDate = screenContentRegDate;
	}
	public WishList getWishList() {
		return wishList;
	}
	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}
	@Override
	public String toString() {
		return "ScreenContent [wishList=" + wishList + ", screenContentNo=" + screenContentNo + ", movie=" + movie
				+ ", screenDate=" + screenDate + ", screenOpenTime=" + screenOpenTime + ", screenEndTime="
				+ screenEndTime + ", screenTheater=" + screenTheater + ", ticketPrice=" + ticketPrice + ", previewFlag="
				+ previewFlag + ", previewTitle=" + previewTitle + ", ticketOpenDate=" + ticketOpenDate
				+ ", inviteActor=" + inviteActor + ", screenContentRegDate=" + screenContentRegDate + "]";
	}



}
