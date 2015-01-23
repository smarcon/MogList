package com.isep.moglistapp;

public class BeanTask {
	private String idTask;
	private String nameTask;
	private String idMogList;
	private String termDate;

	public BeanTask(String theTaskId, String theTaskName, String theMogListId,
			String theTermDate) {
		idTask = theTaskId;
		nameTask = theTaskName;
		termDate = theTermDate;
		idMogList = theMogListId;
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
		return termDate == null ? "" : termDate;
	}

	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}

	@Override
	public String toString() {
		return this.getNameTask() + "\n" + this.getTermDate();
	}

}
