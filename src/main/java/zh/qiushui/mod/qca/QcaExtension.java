package zh.qiushui.mod.qca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.qiushui.mod.qca.command.commandTpPos.TpPosCommand;
import zh.qiushui.mod.qca.util.TranslationsUtil;

import java.util.Map;

public class QcaExtension implements CarpetExtension, ModInitializer {
	public static final String MOD_ID = "qca";
	public static final String MOD_NAME = "Qualian Carpet Addition";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		CarpetServer.manageExtension(this);
	}

	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(QcaSettings.class);
	}

	@Override
	public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext) {
		TpPosCommand.register(dispatcher);
	}

	@Override
	public String version() {
		return MOD_ID;
	}

	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return TranslationsUtil.getTranslations(lang);
	}
}