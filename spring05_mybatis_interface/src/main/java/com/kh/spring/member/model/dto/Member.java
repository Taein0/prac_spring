package com.kh.spring.member.model.dto;

import java.sql.Date;

public class Member {

	private String userId;
	private String password;
	private String email;
	private String grade;
	private String tell;
	private Date rentableDate;
	private Date regDate;
	private int isLeave;

	public String getUserId() {
	      return userId;
	   }
	   
	   public void setUserId(String userId) {
	      this.userId = userId;
	   }
	   
	   public String getPassword() {
	      return password;
	   }
	   
	   public void setPassword(String password) {
	      this.password = password;
	   }
	   
	   public String getEmail() {
	      return email;
	   }
	   
	   public void setEmail(String email) {
	      this.email = email;
	   }
	   public String getGrade() {      
	      return grade;
	   }
	   
	   public void setGrade(String grade) {
	      this.grade = grade;
	   }
	   
	   public String getTell() {
	      return tell;
	   }
	   
	   public void setTell(String tell) {
	      this.tell = tell;
	   }
	   
	   public Date getRentableDate() {
	      return rentableDate;
	   }
	   
	   public void setRentableDate(Date rentableDate) {
	      this.rentableDate = rentableDate;
	   }
	   
	   public Date getRegDate() {
	      return regDate;
	   }
	   public void setRegDate(Date regDate) {
	      this.regDate = regDate;
	   }
	   
	   public int getIsLeave() {
	      return isLeave;
	   }
	   
	   public void setIsLeave(int isLeave) {
	      this.isLeave = isLeave;
	   }
	   
	   @Override
	   public String toString() {
	      return "Member [userId=" + userId + ", password=" + password + ", email=" + email + ", grade=" + grade
	            + ", tell=" + tell + ", rentableDate=" + rentableDate + ", regDate=" + regDate + ", isLeave=" + isLeave
	            + "]";
	   }

}
