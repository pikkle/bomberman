package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Player;

import java.util.UUID;

/**
 * Created by matthieu.villard on 02.06.2016.
 */
public class PlayerResponse extends Response<Player> {

    private Player player;

    public PlayerResponse(UUID uuid, Player player) {
        super(uuid);
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public Player accept(ResponseVisitor visitor) {
        return visitor.visit(this);
    }
}
