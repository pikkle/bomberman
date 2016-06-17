package ch.heigvd.bomberman.server.database;

/**
 * Represents Exceptions thrown by the Data Access Layer.
 */
public class DataAccessLayerException extends Exception {
	public DataAccessLayerException() {
	}

	/**
	 * If an exception occurs with the DataAccessLayer.
	 *
	 * @param message the message
	 */
	public DataAccessLayerException(String message) {
		super(message);
	}

	/**
	 * If an exception occurs with the DataAccessLayer.
	 *
	 * @param cause the cause
	 */
	public DataAccessLayerException(Throwable cause) {
		super(cause);
	}

	/**
	 * If an exception occurs with the DataAccessLayer.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public DataAccessLayerException(String message, Throwable cause) {
		super(message, cause);
	}
}