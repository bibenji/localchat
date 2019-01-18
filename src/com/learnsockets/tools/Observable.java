package com.learnsockets.tools;

import com.learnsockets.common.ChatMessage;

public interface Observable {
	public void addObserver(Observer obs);
	public void clearObservers();
	public void notifyObservers(ChatMessage message);
}
