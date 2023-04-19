package edu.northeastern.info6205.tspsolver.model;

public class Action<T> {
	private String actionType;
	private T payload;

	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}
	
	// Commenting it because it can get too verbose for large payloads
	/*
	@Override
	public String toString() {
		return "Action [actionType=" + actionType + ", payload=" + payload + "]";
	}
	*/
	
}
