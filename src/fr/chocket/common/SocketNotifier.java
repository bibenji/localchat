package fr.chocket.common;

public interface SocketNotifier {
	public void addSocketListener(SocketListener listener);
	public void notifyListeners(ChatMessage message);
	public void clearListeners();
}
