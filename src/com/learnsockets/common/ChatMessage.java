package com.learnsockets.common;

public class ChatMessage {
	private String content;
	private String to;
	private String canal;
	private String key;
	
	public ChatMessage() {}
	
	public ChatMessage(String c1, String t, String c2, String k) {		
		content = c1;		
		to = t;
		canal = c2;
		key = k;
	}
	
	public void setKey(String k) {
		key = k;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getCanal() {
		return canal;
	}
	
	public String getKey() {
		return key;
	}
	
	public String toString() {
		return "content::"+content+";to::"+to+";canal::"+canal+";key::"+key;
	}
	
	public void recoverFromString(String str) {
		String[] split = str.split(";");
		for (String part : split) {
			String[] subSplit = part.split("::");
			
			String value = subSplit.length > 1 ? subSplit[1] : "";
			
			switch (subSplit[0]) {
				case "content":
					content = value;
					break;
				case "to":
					to = value;
					break;
				case "canal":
					canal = value;
					break;
				case "key":
					key = value;
					break;
			}
		}
	}
}
