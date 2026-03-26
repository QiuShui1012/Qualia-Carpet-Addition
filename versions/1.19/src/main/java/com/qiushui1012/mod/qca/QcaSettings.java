package com.qiushui1012.mod.qca;

import carpet.settings.Rule;
import net.minecraft.world.level.block.Block;
import com.qiushui1012.mod.qca.util.rule.PlantTransformRecord;

import java.util.Arrays;
import java.util.Set;

import static carpet.settings.RuleCategory.FEATURE;
import static carpet.settings.RuleCategory.SURVIVAL;

public class QcaSettings {
    public static final String QCA = "qca";
    public static final String PVP = "pvp";

    @Rule(
        category = {QCA, FEATURE},
        desc = "Changes the behaviour of the stems when top break.\n"
               + "Now, when the top dripleaf break, the top stem will become dripleaf and keep other stems."
    )
    public static boolean breakDripleafKeepStem = false;

    @Rule(
        category = {QCA, SURVIVAL, FEATURE},
        desc = "Allow players to shear off tall plants or craft them with shears to create their smaller \"variants.\"\n"
               + "Note that these \"variants\" are purely for in-game use and have no relation to real-life plants.",
        options = {
            "enable",
            "grasses,dripleaf", "grasses,flowers", "dripleaf,flowers",
            "grasses", "dripleaf", "flowers",
            "disable"
        },
        validate = QcaValidators.PlantTransform.class
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
        category = {QCA, SURVIVAL, FEATURE},
        desc = "Changes the repair cost level that can trigger the \"Too Expensive!\" tip in anvil.",
        validate = QcaValidators.TooExpensiveLevel.class
    )
    public static int tooExpensiveLevel = 40;

    public static int getTooExpensiveLevel() {
        return tooExpensiveLevel == -1 ? Integer.MAX_VALUE - 1 : tooExpensiveLevel;
    }

    @Rule(
        category = {QCA, SURVIVAL, FEATURE},
        desc = "Sets the multiplier in calculating next repair cost."
    )
    public static double repairCostMultiplier = 2.0D;

    @Rule(
        category = {QCA, FEATURE},
        desc = "Allow item entities pass through chains.\n"
               + "For other entities, there is no difference between the collision box and when the rule is closed."
    )
    public static boolean itemsCanPassThroughChains = false;

    @Rule(
        category = {QCA, PVP, SURVIVAL, FEATURE},
        desc = "Make equipments not damage during PVP."
    )
    public static boolean pvpDoNotDamageEquipment = false;

    @Rule(
        category = {QCA, PVP, SURVIVAL, FEATURE},
        desc = "Make weapon not damage during PVP."
    )
    public static boolean pvpDoNotDamageWeapon = false;

    @Rule(
        category = {QCA, SURVIVAL, FEATURE},
        desc = "Allow players to use bone meal to double the small flowers."
    )
    public static boolean fertilizableSmallFlowers = false;
}
