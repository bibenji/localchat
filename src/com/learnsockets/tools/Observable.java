package com.learnsockets.tools;

public interface Observable {
	public void addObserver(Observer obs);
	public void clearObservers();
	public void notifyObservers(String str);
}
