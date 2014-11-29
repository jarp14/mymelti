package com.chico.esiuclm.melti.model;

import java.sql.SQLException;

import com.chico.esiuclm.melti.persistence.DAOStudent;

public class Solution {
	private String user_id, task_id, course_id;
	private String solved_code;
	private double calification_grade;
	private String calification_comment;

	public Solution(String uid, String tid, String cid, String scode) {
		this.user_id = uid;
		this.task_id = tid;
		this.course_id = cid;
		this.solved_code = scode;
		this.calification_grade = -1.0; // Inicialmente sin calificacion
		this.calification_comment = ""; // Inicialmente sin calificacion
	}
	
	public Solution(String uid, String tid, String cid, String scode, double cg, String cc) {
		this.user_id = uid;
		this.task_id = tid;
		this.course_id = cid;
		this.solved_code = scode;
		this.calification_grade = cg;
		this.calification_comment = cc;
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

	public void add() throws ClassNotFoundException, SQLException {
		DAOStudent.addSolutionDB(this);
	}
	
}
