package com.helios.slack;

import java.util.ArrayList;

public class SlackMessage {
	private String text;
	private String channel;
	private ArrayList<SlackAttachment> attachments;
	private String ts;
	
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public ArrayList<SlackAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(ArrayList<SlackAttachment> attachments) {
		this.attachments = attachments;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "SlackMessage [text=" + text + ", channel=" + channel + ", attachments=" + attachments + ", ts=" + ts
				+ "]";
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
}
