package com.isep.moglistapp;

import java.util.Date;

public class BeanTask {
	private String idTask;
	private String nameTask;
	private String idMogList;
	private String termDate;
	private Date dt;

	public BeanTask(String theTaskId, String theTaskName, String theMogListId,
			String theTermDate, Date theDt) {
		idTask = theTaskId;
		nameTask = theTaskName;
		termDate = theTermDate;
		idMogList = theMogListId;
		setDt(theDt);
	}

	public String getIdTask() {
		return idTask;
	}

	public void setIdTask(String idTask) {
		this.idTask = idTask;
	}

	public String getNameTask() {
		return nameTask;
	}

	public void setNameTask(String nameTask) {
		this.nameTask = nameTask;
	}

	public String getIdMogList() {
		return idMogList;
	}

	public void setIdMogList(String idMogList) {
		this.idMogList = idMogList;
	}

	public String getTermDate() {
		return termDate == null ? "" : "\n" + termDate.substring(0, termDate.length()-9);
	}

	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}

	@Override
	public String toString() {
		return this.getNameTask() + this.getTermDate();
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}
