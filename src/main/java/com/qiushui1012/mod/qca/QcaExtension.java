package com.qiushui1012.mod.qca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import net.fabricmc.api.ModInitializer;

//#if MC >= 11502
import com.qiushui1012.mod.qca.util.TranslationsUtil;
import java.util.Map;
//#endif

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

    //#if MC >= 11502
    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return TranslationsUtil.getTranslations(lang);
    }
    //#endif
}