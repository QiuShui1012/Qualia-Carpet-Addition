package zh.qiushui.mod.qca.util.rule;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class PlantTransformRecord {
    public static final BiMap<Block, Block> SMALL_TALL_GRASSES = PlantTransformRecord.buildGrassTransformMap();
    public static final BiMap<Block, Block> SMALL_TALL_DRIPLEAF = PlantTransformRecord.buildDripleafTransformMap();
    public static final BiMap<Block, Block> SMALL_TALL_FLOWERS = PlantTransformRecord.buildFlowerTransformMap();
    public static final BiMap<Block, Block> SMALL_TALL_PLANTS = PlantTransformRecord.buildAllPlantsTransformMap();

    private static BiMap<Block, Block> buildGrassTransformMap() {
        BiMap<Block, Block> map = HashBiMap.create();

        map.put(Blocks.SHORT_GRASS, Blocks.TALL_GRASS);
        map.put(Blocks.FERN, Blocks.LARGE_FERN);

        return Maps.unmodifiableBiMap(map);
    }

    private static BiMap<Block, Block> buildDripleafTransformMap() {
        BiMap<Block, Block> map = HashBiMap.create();

        map.put(Blocks.SMALL_DRIPLEAF, Blocks.BIG_DRIPLEAF);

        return Maps.unmodifiableBiMap(map);
    }

    private static BiMap<Block, Block> buildFlowerTransformMap() {
        BiMap<Block, Block> map = HashBiMap.create();
        
        map.put(Blocks.DANDELION, Blocks.SUNFLOWER);
        map.put(Blocks.POPPY, Blocks.ROSE_BUSH);
        map.put(Blocks.ALLIUM, Blocks.LILAC);
        map.put(Blocks.PINK_TULIP, Blocks.PEONY);

        return Maps.unmodifiableBiMap(map);
    }

    private static BiMap<Block, Block> buildAllPlantsTransformMap() {
        BiMap<Block, Block> map = HashBiMap.create();
        
        map.putAll(PlantTransformRecord.buildGrassTransformMap());
        map.putAll(PlantTransformRecord.buildDripleafTransformMap());
        map.putAll(PlantTransformRecord.buildFlowerTransformMap());
        
        return Maps.unmodifiableBiMap(map);
    }
}
