package zh.qiushui.mod.qca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thinkingstudio.bedsheet.loader.entrypoint.ModInitializer;
import zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange.BeaconUtil;
import zh.qiushui.mod.qca.util.TranslationsUtil;

import java.util.Map;

@Mod(QcaExtension.MOD_ID)
public class QcaExtension implements CarpetExtension {
    public static final String MOD_ID = "qca";
    public static final String MOD_NAME = "Qualia Carpet Addition";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("unused")
    public QcaExtension(IEventBus modEventBus) {
        CarpetServer.manageExtension(this);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public void onTick(MinecraftServer server) {
        BeaconUtil.tick();
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(QcaSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return TranslationsUtil.getTranslations(lang);
    }

    public static void debugLog(String message) {
        if (QcaSettings.qcaDebugLog) {
            QcaExtension.LOGGER.debug(message);
        }
    }

    public static void debugLog(String message, Object... args) {
        if (QcaSettings.qcaDebugLog) {
            QcaExtension.LOGGER.debug(message, args);
        }
    }
}