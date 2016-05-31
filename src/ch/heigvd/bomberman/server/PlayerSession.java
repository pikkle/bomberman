package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerSession {
	private Bomberman bomberman;
	private RoomSession roomSession;
	private boolean ready = false;
	private Long rank;
	private Player player;
	private UUID readyUuid;
	private UUID startUuid;
	private UUID moveUuid;
	private UUID addUuid;
	private UUID destroyUuid;
	private UUID endUuid;
	private RequestManager requestManager;

	public PlayerSession(Player player, RoomSession roomSession, UUID readyUuid, RequestManager requestManager){
		this.player = player;
		this.roomSession = roomSession;
		this.readyUuid = readyUuid;
		this.requestManager = requestManager;
	}

	public Bomberman getBomberman() {
		return bomberman;
	}

	public void setBomberman(Bomberman bomberman) {
		this.bomberman = bomberman;
	}

	public RoomSession getRoomSession() {
		return roomSession;
	}

	public void setRoomSession(RoomSession roomSession) {
		this.roomSession = roomSession;
	}

	public Player getPlayer() {
		return player;
	}

	public UUID getReadyUuid(){
		return  readyUuid;
	}

	public void setReadyUuid(UUID readyUuid){
		this.readyUuid = readyUuid;
	}

	public UUID getStartUuid(){
		return  startUuid;
	}

	public void setStartUuid(UUID startUuid){
		this.startUuid = startUuid;
	}

	public UUID getMoveUuid(){
		return  moveUuid;
	}

	public void setMoveUuid(UUID moveUuid){
		this.moveUuid = moveUuid;
	}

	public UUID getAddUuid(){
		return  addUuid;
	}

	public void setAddUuid(UUID addUuid){
		this.addUuid = addUuid;
	}

	public UUID getDestroyUuid(){
		return  destroyUuid;
	}

	public void setDestroyUuid(UUID destroyUuid){
		this.destroyUuid = destroyUuid;
	}

	public UUID getEndUuid(){
		return  endUuid;
	}

	public void setEndUuid(UUID endUuid){
		this.endUuid = endUuid;
	}

	public RequestManager getRequestManager(){
		return requestManager;
	}

	public boolean isReady() {
		return ready;
	}

	public void ready(boolean state) {
		ready = state;
		roomSession.update();
	}

	public Optional<Long> getRank(){
		return Optional.ofNullable(rank);
	}

	public void setRank(long rank){
		this.rank = rank;
	}
}