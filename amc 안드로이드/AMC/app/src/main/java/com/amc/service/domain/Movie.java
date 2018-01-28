package com.amc.service.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Serializable{

	private WishList wishList;
	private String movieCd;
	private int    movieNo; //movie_no
	
	public WishList getWishList() {
		return wishList;
	}
	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}

	private String movieRegDate; //movie_regdate
	private String movieNm;
	
	private String movieNmEn;
	
	private String prdtYear;
	private String openDt; //movie_opendate
	private String endDt; //movie_enddate
	private String synopsis; //synopsis
	
	private String trailer;
	private String typeNm;
	private String prdtStatNm;
	private String nationAlt;
	private String genres; //genre	
	
	private String repNationNm;
	private String repGenreNm;
	private String directors; //director	
	
	//additional field for movie post
	private String showTm; //runningtime
	private String watchGradeNm; //rating
	private String postUrl; //poster
	private String actors; //actor
	private String steelCut; //steelcut
	private String steelCut1;
	private String steelCut2;
	private String steelCut3;
	
	public String getSteelCut1() {
		return steelCut1;
	}
	public void setSteelCut1(String steelCut1) {
		this.steelCut1 = steelCut1;
	}
	public String getSteelCut2() {
		return steelCut2;
	}
	public void setSteelCut2(String steelCut2) {
		this.steelCut2 = steelCut2;
	}
	public String getSteelCut3() {
		return steelCut3;
	}
	public void setSteelCut3(String steelCut3) {
		this.steelCut3 = steelCut3;
	}

	
	
	
	//additional filed for statistics 
	private int	   maleCnt;
	private int	   femaleCnt;
	private int    age10s;
	private int    age20s;
	private int    age30s;
	private int    age40s;
	private int    age50s;
	private int    age60s;
	private int    age60sMore;
	

	public int getMovieNo() {
		return movieNo;
	}
	public void setMovieNo(int movieNo) {
		this.movieNo = movieNo;
	}
	
	public String getMovieRegDate() {
		return movieRegDate;
	}
	public void setMovieRegDate(String movieRegDate) {
		this.movieRegDate = movieRegDate;
	}
	
	public int getMaleCnt() {
		return maleCnt;
	}
	public void setMaleCnt(int maleCnt) {
		this.maleCnt = maleCnt;
	}
	public int getFemaleCnt() {
		return femaleCnt;
	}
	public void setFemaleCnt(int femaleCnt) {
		this.femaleCnt = femaleCnt;
	}
	public int getAge10s() {
		return age10s;
	}
	public void setAge10s(int age10s) {
		this.age10s = age10s;
	}
	public int getAge20s() {
		return age20s;
	}
	public void setAge20s(int age20s) {
		this.age20s = age20s;
	}
	public int getAge30s() {
		return age30s;
	}
	public void setAge30s(int age30s) {
		this.age30s = age30s;
	}
	public int getAge40s() {
		return age40s;
	}
	public void setAge40s(int age40s) {
		this.age40s = age40s;
	}
	public int getAge50s() {
		return age50s;
	}
	public void setAge50s(int age50s) {
		this.age50s = age50s;
	}
	public int getAge60s() {
		return age60s;
	}
	public void setAge60s(int age60s) {
		this.age60s = age60s;
	}
	public int getAge60sMore() {
		return age60sMore;
	}
	public void setAge60sMore(int age60sMore) {
		this.age60sMore = age60sMore;
	}
	
	
	public String getSteelCut() {
		return steelCut;
	}
	public void setSteelCut(String steelCut) {
		this.steelCut = steelCut;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public  String getMovieCd() {
		return movieCd;
	}
	public void setMovieCd(String movieCd) {
		this.movieCd = movieCd;
	}
	public String getMovieNm() {
		return movieNm;
	}
	public void setMovieNm(String movieNm) {
		this.movieNm = movieNm;
	}
	public String getMovieNmEn() {
		return movieNmEn;
	}
	public void setMovieNmEn(String movieNmEn) {
		this.movieNmEn = movieNmEn;
	}
	public String getPrdtYear() {
		return prdtYear;
	}
	public void setPrdtYear(String prdtYear) {
		this.prdtYear = prdtYear;
	}
	public String getOpenDt() {
		return openDt;
	}
	public void setOpenDt(String openDt) {
		this.openDt = openDt;
	}
	public String getTypeNm() {
		return typeNm;
	}
	public void setTypeNm(String typeNm) {
		this.typeNm = typeNm;
	}
	public String getPrdtStatNm() {
		return prdtStatNm;
	}
	public void setPrdtStatNm(String prdtStatNm) {
		this.prdtStatNm = prdtStatNm;
	}
	public String getNationAlt() {
		return nationAlt;
	}
	public void setNationAlt(String nationAlt) {
		this.nationAlt = nationAlt;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres ) {
		this.genres = genres;
	}
	public String getRepNationNm() {
		return repNationNm;
	}
	public void setRepNationNm(String repNationNm) {
		this.repNationNm = repNationNm;
	}
	public String getRepGenreNm() {
		return repGenreNm;
	}
	public void setRepGenreNm(String repGenreNm) {
		this.repGenreNm = repGenreNm;
	}
	public String  getDirector() {
		return directors;
	}
	public void setDirector(String directors) {
		this.directors = directors;
	}
	public String getShowTm() {
		return showTm;
	}
	public void setShowTm(String showTm) {
		this.showTm = showTm;
	}
	public String getWatchGradeNm() {
		return watchGradeNm;
	}
	public void setWatchGradeNm(String watchGradeNm) {
		this.watchGradeNm = watchGradeNm;
	}
	public String getPostUrl() {
		return postUrl;
	}
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	
	//toString haerimg modify

	@Override
	public String toString() {
		return "Movie [wishList=" + wishList + ", movieCd=" + movieCd + ", movieNo=" + movieNo + ", movieRegDate="
				+ movieRegDate + ", movieNm=" + movieNm + ", movieNmEn=" + movieNmEn + ", prdtYear=" + prdtYear
				+ ", openDt=" + openDt + ", endDt=" + endDt + ", synopsis=" + synopsis + ", trailer=" + trailer
				+ ", typeNm=" + typeNm + ", prdtStatNm=" + prdtStatNm + ", nationAlt=" + nationAlt + ", genres="
				+ genres + ", repNationNm=" + repNationNm + ", repGenreNm=" + repGenreNm + ", directors=" + directors
				+ ", showTm=" + showTm + ", watchGradeNm=" + watchGradeNm + ", postUrl=" + postUrl + ", actors="
				+ actors + ", steelCut=" + steelCut + ", steelCut1=" + steelCut1 + ", steelCut2=" + steelCut2
				+ ", steelCut3=" + steelCut3 + ", maleCnt=" + maleCnt + ", femaleCnt=" + femaleCnt + ", age10s="
				+ age10s + ", age20s=" + age20s + ", age30s=" + age30s + ", age40s=" + age40s + ", age50s=" + age50s
				+ ", age60s=" + age60s + ", age60sMore=" + age60sMore + "]";
	}

}