package com.helios.slack;

public class SlackField {
	
	
	private String title;
	private String value;
	private boolean Short;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isShort() {
		return Short;
	}
	public void setShort(boolean s) {
		Short = s;
	}
	@Override
	public String toString() {
		return "SlackField [title=" + title + ", value=" + value + ", short=" + Short + "]";
	}
	
	
	
	
	
	
	
	
}
