package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.responses.EndGameResponse;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.Statistic;

import java.util.Optional;
import java.util.UUID;

public class PlayerSession {
	private RequestManager requestManager;
	private RoomSession roomSession;
	private Player player;
	private boolean ready = false;
	private Bomberman bomberman;
	private Long rank;
	private UUID readyUuid;
	private UUID startUuid;
	private UUID moveUuid;
	private UUID addUuid;
	private UUID destroyUuid;
	private UUID endUuid;

	public PlayerSession(Player player, RoomSession roomSession, UUID readyUuid, RequestManager requestManager) throws Exception {
		this.player = player;
		this.roomSession = roomSession;
		this.readyUuid = readyUuid;
		this.requestManager = requestManager;
	}

	public synchronized void close(){
		if(roomSession.getPlayers().contains(this)) {
			if (roomSession.isRunning()) {
				rank = roomSession.getPlayers().stream().filter(p -> !p.getRank().isPresent()).count();
				requestManager.send(new EndGameResponse(endUuid, new Statistic(rank)));

			}
			roomSession.removePlayer(this);
			if(bomberman != null)
				roomSession.getArena().destroy(bomberman);
		}
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