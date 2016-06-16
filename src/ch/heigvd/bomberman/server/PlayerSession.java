package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.responses.EndGameResponse;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.Statistic;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Player session inside a game.
 */
public class PlayerSession {
	private RequestManager requestManager;
	private RoomSession roomSession;
	private Player player;
	private boolean ready = false;
	private Bomberman bomberman;
	private UUID readyUuid;
	private UUID startUuid;
	private UUID moveUuid;
	private UUID addUuid;
	private UUID destroyUuid;
	private UUID endUuid;
	private Statistic statistic;

    /**
     * Constructs the player when he enters a room.
     * @param player the player entering a room
     * @param roomSession the room he enters
     * @param readyUuid the id of the ready request
     * @param requestManager the request manager
     * @throws Exception
     */
	public PlayerSession(Player player, RoomSession roomSession, UUID readyUuid, RequestManager requestManager) throws Exception {
		this.player = player;
		this.roomSession = roomSession;
		this.readyUuid = readyUuid;
		this.requestManager = requestManager;
		statistic = new Statistic(player, roomSession.getGame());
	}

    /**
     * Gets the statistic object.
     * @return the statistic
     */
	public Statistic getStatistic(){
		return statistic;
	}

    /**
     * Closes the player session (when he quits the room or ends the game.
     */
	public synchronized void close(){
		if(roomSession.getPlayers().contains(this)) {
			if (roomSession.isRunning()) {
				statistic.setSurvivalTime(Duration.between(roomSession.getStart(), Instant.now()));
				player.addStatistic(statistic);
				Server.getInstance().getDatabase().games().createOrUpdate(roomSession.getGame());
				requestManager.send(new EndGameResponse(endUuid, statistic));

			}
			roomSession.removePlayer(this);
			if(bomberman != null)
				bomberman.delete();
		}
	}

    /**
     * Gets the bomberman object of the player.
     * @return the bomberman from the game
     */
	public Bomberman getBomberman() {
		return bomberman;
	}

    /**
     * Updates the bomberman of the player.
     * @param bomberman the new bomberman
     */
	public void setBomberman(Bomberman bomberman) {
		this.bomberman = bomberman;
	}

    /**
     * Gets the room session in which the player is.
     * @return the room session
     */
	public RoomSession getRoomSession() {
		return roomSession;
	}

    /**
     * Updates the room session
     * @param roomSession the new room session
     */
	public void setRoomSession(RoomSession roomSession) {
		this.roomSession = roomSession;
	}

    /**
     * Gets the player.
     * @return the player affiliated by the session.
     */
	public Player getPlayer() {
		return player;
	}

    /**
     * Gets the ready request id.
     * @return the ready request uuid
     */
	public UUID getReadyUuid(){
		return  readyUuid;
	}

    /**
     * Updates the ready request id.
     * @param readyUuid the new ready uuid
     */
	public void setReadyUuid(UUID readyUuid){
		this.readyUuid = readyUuid;
	}

    /**
     * Gets the start request id.
     * @return the start request uuid
     */
	public UUID getStartUuid(){
		return  startUuid;
	}

    /**
     * Updates the start request id.
     * @param startUuid the new start uuid
     */
	public void setStartUuid(UUID startUuid){
		this.startUuid = startUuid;
	}

    /**
     * Gets the move request id.
     * @return the move request uuid
     */
	public UUID getMoveUuid(){
		return  moveUuid;
	}

    /**
     * Updates the move request id.
     * @param moveUuid the new move uuid
     */
	public void setMoveUuid(UUID moveUuid){
		this.moveUuid = moveUuid;
	}

    /**
     * Gets the add request id.
     * @return the add request uuid
     */
	public UUID getAddUuid(){
		return  addUuid;
	}

    /**
     * Updates the add request id.
     * @param addUuid the new add uuid
     */
	public void setAddUuid(UUID addUuid){
		this.addUuid = addUuid;
	}

    /**
     * Gets the destroy request id.
     * @return the destroy request uuid
     */
	public UUID getDestroyUuid(){
		return  destroyUuid;
	}

    /**
     * Updates the destroy request id.
     * @param destroyUuid the new destroy uuid
     */
	public void setDestroyUuid(UUID destroyUuid){
		this.destroyUuid = destroyUuid;
	}

    /**
     * Gets the end request id.
     * @return the end request uuid
     */
	public UUID getEndUuid(){
		return  endUuid;
	}

    /**
     * Updates the end request id.
     * @param endUuid the new end uuid
     */
	public void setEndUuid(UUID endUuid){
		this.endUuid = endUuid;
	}

    /**
     * Gets the request manager affiliated by the request manager.
     * @return the request manager caller
     */
	public RequestManager getRequestManager(){
		return requestManager;
	}

    /**
     * Gets the player status if he's ready or not.
     * @return the player's ready status
     */
	public boolean isReady() {
		return ready;
	}

    /**
     * Updates the player ready status.
     * @param state the new ready status
     */
	public void ready(boolean state) {
		ready = state;
		roomSession.update();
	}
}