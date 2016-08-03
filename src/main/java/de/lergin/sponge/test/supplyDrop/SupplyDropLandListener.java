package de.lergin.sponge.test.supplyDrop;

import de.lergin.sponge.test.supplyDrop.dataManipulator.SupplyDropKeys;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.ExpireEntityEvent;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SupplyDropLandListener {
    //@Exclude(DestructEntityEvent.Death.class)

    @Listener
    public void onFallingBlockDestruct(ExpireEntityEvent event){//, @First @Has(SupplyDropData.class) Entity entity) {
        //Entity entity = event.getTargetEntity();

        System.out.println("TEST");

        //test if it is a supplyDrop
      /*  if(!entity.getType().equals(EntityTypes.FALLING_BLOCK) || !entity.get(SupplyDropKeys.SUPPLY_DROP_INVENTORY).isPresent())
            return;

        Location<World> location = entity.getLocation();



        if(location.getBlockType().equals(BlockTypes.CHEST)){
            //todo add the items to the chest inventory
            TileEntityCarrier chest = (TileEntityCarrier) event.getTargetEntity().getLocation().getTileEntity().get();

            System.out.println(chest.getInventory().capacity());

        } else {
            //drop the items form the inventory because the chest will drop also
            Inventory inventory = entity.get(SupplyDropKeys.SUPPLY_DROP_INVENTORY).get();

            inventory.slots().forEach((inv) -> {
                if (inv.peek().isPresent()) {
                    Optional<Entity> optional = location.getExtent().createEntity(EntityTypes.ITEM, location.getPosition());

                    if (optional.isPresent()) {
                        Item item = (Item) optional.get();
                        item.offer(Keys.REPRESENTED_ITEM, inv.peek().get().createSnapshot());
                        location.getExtent().spawnEntity(item, Cause.source(SpawnCause.builder().type(SpawnTypes.CUSTOM).build()).build());
                    }
                }
            });
        }*/
    }
}
