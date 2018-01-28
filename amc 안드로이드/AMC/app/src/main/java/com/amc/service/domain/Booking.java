package com.amc.service.domain;

import java.io.Serializable;

public class Booking implements Serializable {

    private String bookingNo;
    private String userId;
    private int screenContentNo;
    private String bookingRegDate;
    private int headCount;
    private int totalTicketPrice;
    private String bookingSeatNo;
    private String impId;
    private String qrUrl;

    private Movie movie;
    private ScreenContent screenContent;



    public Booking() {
        super();
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getImpId() {
        return impId;
    }

    public void setImpId(String impId) {
        this.impId = impId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScreenContentNo() {
        return screenContentNo;
    }

    public void setScreenContentNo(int screenContentNo) {
        this.screenContentNo = screenContentNo;
    }

    public String getBookingRegDate() {
        return bookingRegDate;
    }

    public void setBookingRegDate(String bookingRegDate) {
        this.bookingRegDate = bookingRegDate;
    }

    public int getHeadCount() {
        return headCount;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public int getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public void setTotalTicketPrice(int totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public String getBookingSeatNo() {
        return bookingSeatNo;
    }

    public void setBookingSeatNo(String bookingSeatNo) {
        this.bookingSeatNo = bookingSeatNo;
    }

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }


    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public ScreenContent getScreenContent() {
        return screenContent;
    }

    public void setScreenContent(ScreenContent screenContent) {
        this.screenContent = screenContent;
    }

    @Override
    public String toString() {
        return "Booking [bookingNo=" + bookingNo + ", impId=" + impId + ", userId=" + userId + ", screenContentNo="
                + screenContentNo + ", bookingRegDate=" + bookingRegDate + ", headCount=" + headCount
                + ", totalTicketPrice=" + totalTicketPrice + ", bookingSeatNo=" + bookingSeatNo + ", qrUrl=" + qrUrl
                + ", movie=" + movie + ", screenContent=" + screenContent + "]";
    }




}
