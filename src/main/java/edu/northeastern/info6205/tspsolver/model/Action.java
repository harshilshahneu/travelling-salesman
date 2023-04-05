package edu.northeastern.info6205.tspsolver.model;

public class Action<T extends Payload> {
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
	
	@Override
	public String toString() {
		return "Action [actionType=" + actionType + ", payload=" + payload + "]";
	}
	
}
