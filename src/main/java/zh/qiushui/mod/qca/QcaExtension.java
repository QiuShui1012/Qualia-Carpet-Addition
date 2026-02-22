package zh.qiushui.mod.qca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;
import zh.qiushui.mod.qca.util.TranslationsUtil;

import java.util.Map;

public class QcaExtension implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "qca";

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(QcaSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return TranslationsUtil.getTranslations(lang);
    }
}