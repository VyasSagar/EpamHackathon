package com.helios.slack;

import java.util.List;

public class SlackAttachment {
	
	private String fallback;
	private String color;
	private String pretext;
	private String text;
	private String title;
	private List<SlackField> fields;
	public String getFallback() {
		return fallback;
	}
	public void setFallback(String fallback) {
		this.fallback = fallback;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPretext() {
		return pretext;
	}
	public void setPretext(String pretext) {
		this.pretext = pretext;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<SlackField> getFields() {
		return fields;
	}
	public void setFields(List<SlackField> fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		return "SlackAttachment [fallback=" + fallback + ", color=" + color + ", pretext=" + pretext + ", text=" + text
				+ ", title=" + title + ", fields=" + fields + "]";
	}
	
	
	
	
	
	
	
}
