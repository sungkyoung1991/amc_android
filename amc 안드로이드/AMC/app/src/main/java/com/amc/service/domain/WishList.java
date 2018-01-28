package com.amc.service.domain;

import java.io.Serializable;

public class WishList implements Serializable{

	int wishNo;
	User user;
	Movie movie;
	String wishFlag;
	String wishRegDate;

	public int getWishNo() {
		return wishNo;
	}

	public void setWishNo(int wishNo) {
		this.wishNo = wishNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getWishFlag() {
		return wishFlag;
	}

	public void setWishFlag(String wishFlag) {
		this.wishFlag = wishFlag;
	}

	public String getWishRegDate() {
		return wishRegDate;
	}

	public void setWishRegDate(String wishRegDate) {
		this.wishRegDate = wishRegDate;
	}

	@Override
	public String toString() {
		return "WishList [wishNo=" + wishNo + ", user=" + user + ", movie=" + movie + ", wishFlag=" + wishFlag
				+ ", wishRegDate=" + wishRegDate + "]";
	}

}
