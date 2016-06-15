package ch.heigvd.bomberman.common.communication.responses;

import ch.heigvd.bomberman.common.game.Arena.Arena;

import java.util.List;
import java.util.UUID;

/**
 * Created by matthieu.villard on 27.05.2016.
 */
public class ArenasResponse extends Response<List<Arena>> {

    private List<Arena> arenas;

    public ArenasResponse(UUID uuid, List<Arena> arenas) {
        super(uuid);
        this.arenas = arenas;
    }

    public List<Arena> getArenas(){
        return arenas;
    }

    @Override
    public List<Arena> accept(ResponseVisitor visitor) {
        return visitor.visit(this);
    }
}
