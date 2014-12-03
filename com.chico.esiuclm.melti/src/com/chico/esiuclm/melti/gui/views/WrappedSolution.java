package com.chico.esiuclm.melti.gui.views;

import com.chico.esiuclm.melti.model.Solution;

/**
 * Objetos para adaptar a la vista SolutionsView
 */
public class WrappedSolution extends Solution {

	private String course_title;
	private String task_title;
	private String task_class_name;
	private String student_name_family;
	private String student_email;
	
	public WrappedSolution(Solution s, String ct, String tt, String tcn, String snf, String se) {
		super(s.getStudentID(), s.getTaskID(), s.getCourseID(),
				s.getSvdCode(), s.getCalification(), s.getCComment());
		this.course_title = ct;
		this.task_title = tt;
		this.task_class_name = tcn;
		this.student_name_family = snf;
		this.student_email = se;
	}

	public String getCourse_title() {
		return course_title;
	}

	public void setCourse_title(String course_title) {
		this.course_title = course_title;
	}

	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}

	public String getTaskClassName() {
		return this.task_class_name;
	}
	
	public String getStudent_name_family() {
		return student_name_family;
	}

	public void setStudent_name_family(String student_name_family) {
		this.student_name_family = student_name_family;
	}

	public String getStudent_email() {
		return student_email;
	}

	public void setStudent_email(String student_email) {
		this.student_email = student_email;
	}
	
}
