package com.qiushui1012.mod.qca;

import carpet.api.settings.Rule;
import net.minecraft.world.level.block.Block;
import com.qiushui1012.mod.qca.util.rule.PlantTransformRecord;

import java.util.Arrays;
import java.util.Set;

import static carpet.api.settings.RuleCategory.FEATURE;
import static carpet.api.settings.RuleCategory.SURVIVAL;

public class QcaSettings {
    public static final String QCA = "qca";
    public static final String PVP = "pvp";

    @Rule(
        categories = {QCA, FEATURE}
    )
    public static boolean breakDripleafKeepStem = false;

    @Rule(
        categories = {QCA, SURVIVAL, FEATURE},
        options = {
            "enable",
            "grasses,dripleaf", "grasses,flowers", "dripleaf,flowers",
            "grasses", "dripleaf", "flowers",
            "disable"
        },
        validators = QcaValidators.PlantTransform.class
    )
    public static String tallPlantShearToSmall = "disable";

    public static final int NONE = 0;
    public static final int GRASSES = 1;
    public static final int DRIPLEAF = 2;
    public static final int FLOWERS = 3;

    public static int matchTallPlant(Block plant) {
        Set<String> settings = Set.copyOf(Arrays.stream(tallPlantShearToSmall.split(",")).toList());

        if (
            PlantTransformRecord.SMALL_TALL_GRASSES.containsValue(plant)
            && (settings.contains("enable") || settings.contains("grasses"))
        ) {
            return QcaSettings.GRASSES;
        } else if (
            PlantTransformRecord.SMALL_TALL_DRIPLEAF.containsValue(plant)
            && (settings.contains("enable") || settings.contains("dripleaf"))
        ) {
            return QcaSettings.DRIPLEAF;
        } else if (
            PlantTransformRecord.SMALL_TALL_FLOWERS.containsValue(plant)
            && (settings.contains("enable") || settings.contains("flowers"))
        ) {
            return QcaSettings.FLOWERS;
        }

        return QcaSettings.NONE;
    }

    @Rule(
        categories = {QCA, SURVIVAL, FEATURE},
        validators = QcaValidators.TooExpensiveLevel.class
    )
    public static int tooExpensiveLevel = 40;

    public static int getTooExpensiveLevel() {
        return tooExpensiveLevel == -1 ? Integer.MAX_VALUE - 1 : tooExpensiveLevel;
    }

    @Rule(
        categories = {QCA, SURVIVAL, FEATURE}
    )
    public static double repairCostMultiplier = 2.0D;

    @Rule(
        categories = {QCA, FEATURE}
    )
    public static boolean itemsCanPassThroughChains = false;

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE})
    public static boolean pvpDoNotDamageEquipment = false;

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE})
    public static boolean pvpDoNotDamageWeapon = false;

    @Rule(categories = {QCA, SURVIVAL, FEATURE})
    public static boolean fertilizableSmallFlowers = false;
}
