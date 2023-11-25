package com.petproject.motelservice.domain.inventory;

public enum EPostStatus {
	IN_PROGRESS("IN_PROGRESS"), 
	APPROVED("APPROVED"), 
	REJECTED("REJECTED"), 
	REMOVED("REMOVED");
	
	private String value;

	private EPostStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
