package de.lergin.sponge.test.supplyDrop;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SupplyDropCommand implements CommandExecutor {
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {

        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) commandSource;

        Carrier carrier = player;

        System.out.println(carrier.getInventory());

        Location<World> loc = player.getLocation();


        //Carrier carrier1 = (Carrier) loc.getTileEntity().get();

        //System.out.println(carrier1.getInventory());



        if(loc.getBlockY() > 200){
            player.sendMessage(Text.of("You are to hight for spawning a drop"));
            return CommandResult.empty();
        }

        Location<World> spawnLocation =
                loc.add(0, 150 - loc.getY(), 0);


        SupplyDrop supplyDrop = new SupplyDrop(player.getInventory());
        supplyDrop.spawn(spawnLocation);

        return CommandResult.success();
    }
}
