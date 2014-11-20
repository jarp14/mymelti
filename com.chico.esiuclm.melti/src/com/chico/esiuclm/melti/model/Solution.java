package com.chico.esiuclm.melti.model;

public class Solution {
	private String user_id, task_id, course_id;
	private String solved_code;
	private double calification_grade;
	private String calification_comment;
	
	public Solution(String uid, String tid, String cid, String scode, double cgrade, String ccomment) {
		this.user_id = uid;
		this.task_id = tid;
		this.course_id = cid;
		this.calification_grade = cgrade;
		this.calification_comment = ccomment;
	}

	public String getStudentID() {
		return this.user_id;
	}
	
	public String getTaskID() {
		return this.task_id;
	}
	
	public String getCourseID() {
		return this.course_id;
	}
	
	public String getSvdCode() {
		return this.solved_code;
	}
	
	public double getCalification() {
		return this.calification_grade;
	}
	
	public String getCComment() {
		return this.calification_comment;
	}
	
}
