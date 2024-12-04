package zh.qiushui.mod.qca.rule.util;

import com.google.common.collect.ImmutableListMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class PlantTransformUtil {
    public static final ImmutableListMultimap<Block, Block> SMALL_TALL_GRASSES = ImmutableListMultimap.of(
            Blocks.SHORT_GRASS    , Blocks.TALL_GRASS,
            Blocks.FERN           , Blocks.LARGE_FERN
    );
    public static final ImmutableListMultimap<Block, Block> SMALL_TALL_DRIPLEAF = ImmutableListMultimap.of(
            Blocks.SMALL_DRIPLEAF , Blocks.BIG_DRIPLEAF
    );
    public static final ImmutableListMultimap<Block, Block> SMALL_TALL_FLOWERS = ImmutableListMultimap.of(
            Blocks.DANDELION      , Blocks.SUNFLOWER,
            Blocks.POPPY          , Blocks.ROSE_BUSH,
            Blocks.ALLIUM         , Blocks.LILAC,
            Blocks.PINK_TULIP     , Blocks.PEONY
    );
    public static final ImmutableListMultimap<Block, Block> SMALL_TALL_PLANTS = ImmutableListMultimap
            .<Block, Block>builder()
            .putAll(SMALL_TALL_GRASSES)
            .putAll(SMALL_TALL_DRIPLEAF)
            .putAll(SMALL_TALL_FLOWERS)
            .build();
}
