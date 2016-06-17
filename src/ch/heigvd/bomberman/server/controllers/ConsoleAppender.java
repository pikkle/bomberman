package ch.heigvd.bomberman.server.controllers;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by matthieu.villard on 14.06.2016.
 */
public class ConsoleAppender extends WriterAppender {

	private static TextArea console = null;

	public static void setConsole(TextArea console) {
		ConsoleAppender.console = console;
	}

	@Override
	public void append(LoggingEvent loggingEvent) {
		if (console != null) {
			final String message = this.layout.format(loggingEvent);

			Platform.runLater(() -> console.appendText(message));
		}
	}
}