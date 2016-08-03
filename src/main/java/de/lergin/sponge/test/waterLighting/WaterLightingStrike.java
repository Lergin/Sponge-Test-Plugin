package de.lergin.sponge.test.waterLighting;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.weather.Lightning;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WaterLightingStrike {
    private final Location<World> loc;
    private final double strength;
    private final double distance;
    private List<Vector3i> affectedLocs = new ArrayList<>();

    public WaterLightingStrike(Location<World> loc, double strength, double distance) {
        this.loc = loc;
        this.strength = strength;
        this.distance = distance;
    }

    /**
     * let this WaterLightningStrike out into the world and damage the players that are in the nearby water
     */
    public void strice(){
        Location<World> spawnLoc = loc;

        //find the highest block under the spawnLoc
        while(spawnLoc.getBlockType() == BlockTypes.AIR && spawnLoc.getY() > 0){
            spawnLoc = spawnLoc.add(0,-1,0);
        }

        //we only want to spawn lightning strikes in the water
        if(spawnLoc.getBlockType() != BlockTypes.WATER && spawnLoc.getBlockType() != BlockTypes.FLOWING_WATER){
            System.out.println("No Water");
            return;
        }

        spawnLighning(spawnLoc);
        damagePlayersInWater(spawnLoc);
    }

    /**
     * spawns a LightningStrike at the given location
     * @param spawnLoc the location the LightningStrike should target
     */
    private void spawnLighning(Location<World> spawnLoc){
        Extent extent = loc.getExtent();

        Optional<Entity> optional = extent.createEntity(EntityTypes.LIGHTNING, spawnLoc.getPosition().add(0,-1,0));

        if(optional.isPresent()) {
            Lightning lightning = (Lightning) optional.get();

            extent.spawnEntity(
                    lightning,
                    Cause.source(
                            EntitySpawnCause.builder().entity(lightning).type(SpawnTypes.PLUGIN).build()
                    ).build()
            );
        }
    }

    /**
     * damage all the players in the nearby water of the position
     * @param spawnLoc position from that on the water should damage
     */
    private void damagePlayersInWater(Location<World> spawnLoc){
        getAffectedEntitys(spawnLoc);
    }

    private void getAffectedEntitys(final Location<World> spawnLoc){
        affectedLocs = new ArrayList<>();
        getAdjacentWaterLocations(spawnLoc, spawnLoc.getPosition());

        List<Entity> entities = new ArrayList<>();
        final Vector3d spawnPos = spawnLoc.getPosition();

        //filter the entities by there distance to the spawnLoc ant if they are in a position of the list of the affectedLocations
        entities.addAll(spawnLoc.getExtent().getEntities().stream()
                        .filter(entity -> entity.getLocation().getPosition().distance(spawnPos) < distance)
                        .filter(entity -> affectedLocs.contains(entity.getLocation().getBlockPosition()))
                        .collect(Collectors.toList()));

        //damage all the entities that should be affected
        for (Entity entity : entities) {
            entity.damage(strength, DamageSource.builder().type(DamageTypes.CONTACT).bypassesArmor().build());
        }

        System.out.println(entities.size());
    }

    private void getAdjacentWaterLocations(final Location<World> loc, final Vector3d spawnPos) {
        final BlockType blockType = loc.getBlockType();

        //test if the blocks are water and if they aren't added to the list already
        if ((blockType == BlockTypes.WATER || blockType == BlockTypes.FLOWING_WATER) && !affectedLocs.contains(loc.getBlockPosition())) {
            affectedLocs.add(loc.getBlockPosition()); //add to the list of affected locations

            //test if we need to test the adjoining blocks -> if the distance between the current and the spawnPos is almost as high as the max distance we don't need to test the adjoining blocks
            if(loc.getPosition().distance(spawnPos) < distance - 1){
                getAdjacentWaterLocations(loc.add( 1.0, 0.0, 0.0), spawnPos); // east
                getAdjacentWaterLocations(loc.add(-1.0, 0.0, 0.0), spawnPos); // west
                getAdjacentWaterLocations(loc.add( 0.0, 1.0, 0.0), spawnPos); // top
                getAdjacentWaterLocations(loc.add( 0.0,-1.0, 0.0), spawnPos); // bottom
                getAdjacentWaterLocations(loc.add( 0.0, 0.0, 1.0), spawnPos); // south
                getAdjacentWaterLocations(loc.add( 0.0, 0.0,-1.0), spawnPos); // north
            }
        }
    }
}
