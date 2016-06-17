package ch.heigvd.bomberman.client;

import javafx.beans.NamedArg;
import javafx.event.EventType;

/**
 * Created by julien on 19.05.16.
 */
public class ServerEvent extends javafx.event.Event {
	public ServerEvent(@NamedArg("eventType") EventType<? extends javafx.event.Event> eventType) {
		super(eventType);
	}
}
