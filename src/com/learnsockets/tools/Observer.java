package com.learnsockets.tools;

import com.learnsockets.common.ChatMessage;

public interface Observer {
	public void update(ChatMessage message);
}
