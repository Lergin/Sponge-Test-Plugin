package de.lergin.sponge.test.supplyDrop;

import com.flowpowered.math.vector.Vector3d;
import de.lergin.sponge.test.supplyDrop.dataManipulator.SupplyDropData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SupplyDrop {
    private final Inventory inventory;
    private Map<Vector3d, BlockState> parachuteBlocks = new HashMap<>();

    public SupplyDrop(Inventory inventory) {
        parachuteBlocks.put(Vector3d.ZERO.add( 0.0, 1.0,  0.0), BlockTypes.FENCE.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add( 0.0, 2.0,  0.0), BlockTypes.FENCE.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add( 0.0, 3.0,  0.0), BlockTypes.WOOL.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add( 1.0, 2.0,  0.0), BlockTypes.WOOL.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add(-1.0, 2.0,  0.0), BlockTypes.WOOL.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add( 0.0, 2.0,  1.0), BlockTypes.WOOL.getDefaultState());
        parachuteBlocks.put(Vector3d.ZERO.add( 0.0, 2.0, -1.0), BlockTypes.WOOL.getDefaultState());

        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void spawn(Location<World> location){
        Extent spawnExtent = location.getExtent();
        Optional<Entity> optional = spawnExtent.createEntity(EntityTypes.FALLING_BLOCK, location.getPosition());

        if(optional.isPresent()) {
            FallingBlock fallingChest = (FallingBlock) optional.get();

            BlockState chest = BlockState.builder().blockType(BlockTypes.CHEST).build();

            fallingChest.offer(Keys.FALLING_BLOCK_STATE, chest);
            fallingChest.offer(Keys.CAN_PLACE_AS_BLOCK, false); //must be false so it will be placed (seems strange...)
            fallingChest.offer(Keys.CAN_DROP_AS_ITEM, true); //and here it needs to be true to be true...
            fallingChest.offer(Keys.FALLING_BLOCK_CAN_HURT_ENTITIES, true); //this also must be true so the entitys can get damaged
            fallingChest.offer(Keys.FALL_DAMAGE_PER_BLOCK, 0.08); //this is just so that it only hurts
            fallingChest.offer(Keys.FALL_TIME, 1); //needs to be 1 to not despawn immediately
            fallingChest.offer(new SupplyDropData(inventory));

            parachuteBlocks.forEach((vector3d, blockState) -> {
                Optional<Entity> optional1 =
                        spawnExtent.createEntity(EntityTypes.FALLING_BLOCK, location.getPosition().add(vector3d));

                if(optional1.isPresent()){
                    FallingBlock fallingBlock = (FallingBlock) optional1.get();
                    fallingBlock.offer(Keys.FALLING_BLOCK_STATE, blockState);
                    fallingBlock.offer(Keys.CAN_PLACE_AS_BLOCK, true); //must be true so it won't be placed...
                    fallingBlock.offer(Keys.CAN_DROP_AS_ITEM, true); //and here it needs to be false to be false...
                    fallingBlock.offer(Keys.FALLING_BLOCK_CAN_HURT_ENTITIES, false);
                    fallingBlock.offer(Keys.FALL_TIME, 1); //needs to be 1 to not despawn immediately

                    spawnExtent.spawnEntity(
                            fallingBlock,
                            Cause.source(SpawnCause.builder().type(SpawnTypes.CUSTOM).build()).build()
                    );
                }
            });

            spawnExtent.spawnEntity(
                    fallingChest,
                    Cause.source(SpawnCause.builder().type(SpawnTypes.CUSTOM).build()).build()
            );
        }
    }
}
