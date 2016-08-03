package de.lergin.sponge.test.deathCanon;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class DeathCanonCommand implements CommandExecutor {
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {

        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) commandSource;

        DeathCanon canon = new DeathCanon();

        canon.shoot(player, Sponge.getServer().getOnlinePlayers());

        new BlockSkinHead(BlockTypes.STAINED_GLASS, 2).genereateSkinHead(UUID.fromString("ebdf264a-abda-4570-8f61-f2d7a2bb4758"), player.getLocation(), Vector3d.from(-1,0,0), Vector3d.UP);

        return CommandResult.success();
    }
}
