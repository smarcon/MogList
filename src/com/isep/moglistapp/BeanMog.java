package com.isep.moglistapp;

public class BeanMog {

	private String listId;
	private String listName;

	public BeanMog(String theListId, String theListName) {
        listId = theListId;
        listName = theListName;
    }
	
	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
	
	@Override
	public String toString() {
	    return this.getListName();
	}

}
