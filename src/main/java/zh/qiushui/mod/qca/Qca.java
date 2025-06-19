package zh.qiushui.mod.qca;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.BeaconUtil;

@Mod(Qca.MOD_ID)
public class Qca {
    public static final String MOD_ID = "qca";
    public static final String MOD_NAME = "Qualia Carpet Addition";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public Qca(@SuppressWarnings("unused") IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(Qca::onServerTick);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void onServerTick(ServerTickEvent.Post event) {
        BeaconUtil.tick();
    }
}