package spcore.api.models;

import net.minecraft.util.math.BlockPos;

public class TreeTransaction {
    public final String BlockId;
    public final int X;
    public final int Y;
    public final int Z;
    public TreeTransaction(String blockId, BlockPos pos){
        BlockId = blockId;
        X = pos.getX();
        Y = pos.getY();
        Z = pos.getZ();
    }

    public String toMessage(){
        return BlockId + "$" + X + "+" + Y + "+" + Z;
    }
}
