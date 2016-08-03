package de.lergin.sponge.test.deathCanon;

import com.flowpowered.math.vector.Vector3d;
import de.lergin.sponge.test.TestPlugin;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

/**
 * allows the generation of the heads of skins of players in the world
 */
public class BlockSkinHead {
    final private BlockState humanSkinColorBlockState;
    final private int scale;
    final private BlockType blockType;

    /**
     * @param blockType the BlockType of the material the skin should be generated from (must support Keys.DYE_COLOR)
     * @param scale the scale of the skin, with 1 every pixel gets represented by 1*1 blocks, 2 -> 2*2, 3 -> 3*3 ...
     * @param humanSkinColorBlockState the blockState that should be used for human skin colored pixels, null to deactivate the special blocks for human skin colored pixels
     */
    public BlockSkinHead(BlockType blockType, int scale, BlockState humanSkinColorBlockState) {
        this.blockType = blockType;
        this.scale = scale;
        this.humanSkinColorBlockState = humanSkinColorBlockState;
    }

    /**
     * @param blockType the BlockType of the material the skin should be generated from (must support Keys.DYE_COLOR)
     * @param scale the scale of the skin, with 1 every pixel gets represented by 1*1 blocks, 2 -> 2*2, 3 -> 3*3 ...
     */
    public BlockSkinHead(BlockType blockType, int scale) {
        this(blockType, scale, null);
    }

    /**
     * @param blockType the BlockType of the material the skin should be generated from (must support Keys.DYE_COLOR)
     */
    public BlockSkinHead(BlockType blockType) {
        this(blockType, 1, null);
    }

    /**
     * gets the BlockState that will be used for human skin colored pixels, null if is deactivated
     * @return the BlockState for human skin colored pixels
     */
    public BlockState getHumanSkinColorBlockState() {
        return humanSkinColorBlockState;
    }

    public int getScale() {
        return scale;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    /**
     * generates the head of the skin of the player to the given location
     * @param player the uuid of the player
     * @param loc the position of the lower left position of the skin
     */
    public void genereateSkinHead(UUID player, Location<World> loc, Vector3d direction, Vector3d rotation) {
        BufferedImage skinhead;

        try {
            skinhead = ImageIO.read(new URL("https://crafatar.com/avatars/" + player + "?size=8&overlay"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final int scale = getScale();
        Vector3d v1 = direction.cross(rotation);
        Vector3d v2 = v1.cross(direction);

        // no value higher than 1 -> otherwise the blocks are too far away from each other
        v2 = Vector3d.from(
                v2.getX() > 0 ? 1 : v2.getX() < 0 ? -1 : 0,
                v2.getY() > 0 ? 1 : v2.getY() < 0 ? -1 : 0,
                v2.getZ() > 0 ? 1 : v2.getZ() < 0 ? -1 : 0
        );
        v1 = Vector3d.from(
                v1.getX() > 0 ? 1 : v1.getX() < 0 ? -1 : 0,
                v1.getY() > 0 ? 1 : v1.getY() < 0 ? -1 : 0,
                v1.getZ() > 0 ? 1 : v1.getZ() < 0 ? -1 : 0
        );

        // no distance higher than 1 -> otherwise the blocks are too far away from each other
        v2 = Vector3d.from(
                v2.getX() - v1.getX() > 1 ? 0 : v2.getX(),
                v2.getY() - v1.getY() > 1 ? 0 : v2.getY(),
                v2.getZ() - v1.getZ() > 1 ? 0 : v2.getZ()
        );
        v1 = Vector3d.from(
                v1.getX() - v2.getX() > 1 ? 0 : v1.getX(),
                v1.getY() - v2.getY() > 1 ? 0 : v1.getY(),
                v1.getZ() - v2.getZ() > 1 ? 0 : v1.getZ()
        );

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //and for the scale (scale 2 generates a chunk of 2*2 blocks per pixel, scale 3 -> 3*3 ...)
                for (int k = 0; k < scale; k++) {
                    for (int l = 0; l < scale; l++) {

                        //calculate the offset from the starting position for the next block
                        final int x = (8 * scale) - (i * scale) - k;
                        final int y = (8 * scale) - (j * scale) - l;

                        loc.add(
                                v1.mul(x).add(v2.mul(y))
                        ).setBlock(
                                getMaterialFromColor(
                                        Color.ofRgb(skinhead.getRGB(i, j)),
                                        getBlockType(),
                                        getHumanSkinColorBlockState()
                                ),
                                Cause.builder().owner(
                                        Sponge.getPluginManager().fromInstance(TestPlugin.instance()).get()
                                ).build()
                        );
                    }
                }
            }
        }
    }


    /**
     * generates the distance between the two colors with an algorithm based on http://www.compuphase.com/cmetric.htm
     * @param c1
     * @param c2
     * @return the distance between the colors
     */
    private static double colorDistance(Color c1, Color c2) {
        final double meanR = (c1.getRed() + c2.getRed()) / 2;
        final int r = c1.getRed() - c2.getRed();
        final int g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        final double weightR = 2 + meanR / 256;
        final double weightG = 4.0;
        final double weightB = 2 + (255 - meanR) / 256;
        return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
    }

    /**
     * decides which material should be used for which color
     * @param color the color that material should have
     * @param blockType the BlockType of the material (must support Keys.DYE_COLOR)
     * @param humanSkillColorBlockState the blockState that should be used for pixels that are similar to the color of the human skin, to deactivate pass null
     * @return the BlockState of the block with the closest color
     */
    private static BlockState getMaterialFromColor(Color color, BlockType blockType, BlockState humanSkillColorBlockState) {
        if(humanSkillColorBlockState != null && isHumanSkin(color)){
            return humanSkillColorBlockState;
        }

        return BlockState.builder().blockType(blockType).add(Keys.DYE_COLOR, getClosestDyeColor(color)).build();
    }

    /**
     * decides which material should be used for which color
     * @param color the color that material should have
     * @param blockType the BlockType of the material (must support Keys.DYE_COLOR)
     * @return the BlockState of the block with the closest color
     */
    private static BlockState getMaterialFromColor(Color color, BlockType blockType) {
        return getMaterialFromColor(color, blockType, null);
    }

    /**
     * generates the closest DyeColor to the give color
     * @param color the color that should be matched
     * @return the closest DyeColor
     */
    private static DyeColor getClosestDyeColor(Color color){
        DyeColor closestDyeColor = DyeColors.BLACK;

        double d = colorDistance(color, DyeColors.BLACK.getColor());

        for (DyeColor dyeColor : Sponge.getRegistry().getAllOf(CatalogTypes.DYE_COLOR)) {
            double currentdist = colorDistance(color, dyeColor.getColor());

            if (currentdist < d) {
                closestDyeColor = dyeColor;
                d = currentdist;
            }
        }

        return closestDyeColor;
    }

    /**
     * decides if the color is similar to the color of a human skin
     * @param color color
     * @return true if similar to a human skin otherwise false
     */
    private static boolean isHumanSkin(Color color) {
        float[] hsb = new float[3];
        java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        float h = hsb[0];
        float s = hsb[1];
        float b = hsb[2];

        if (h > 0.068 && h < 0.1194444 && s > 0.19 && s < 0.6 && b > 0.7) {
            return true;
        } else if (h > 0.041 && h < 0.09 && s > 0.3 && s < 0.6 && b > 0.84) {
            return true;
        } else if (h > 0.110 && h < 0.1389 && s < 0.6 && s > 0.3 && b > 0.74 && b < 0.91) {
            return true;
        } else if (h < 0.09722 && h > 0.0333333 && s > 0.21 && s < 0.41 && b > 0.95) {
            return true;
        }

        return h > 0.03 && h < 0.086 && s > 0.3 && s < 0.7 && b > 0.6;
    }
}
