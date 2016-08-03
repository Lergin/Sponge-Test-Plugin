package de.lergin.sponge.test.waterLightning;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class WaterLightningCommand implements CommandExecutor {
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {

        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) commandSource;

        Location<World> loc = player.getLocation();

        WaterLightningStrike lightning = new WaterLightningStrike(loc, 2.0, 10.0);

        lightning.strice();

        return CommandResult.success();
    }
}
