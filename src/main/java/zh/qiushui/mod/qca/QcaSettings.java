package zh.qiushui.mod.qca;

import carpet.api.settings.Rule;
import net.minecraft.world.level.block.Block;
import zh.qiushui.mod.qca.util.rule.PlantTransformRecord;

import java.util.Arrays;
import java.util.Set;

import static carpet.api.settings.RuleCategory.EXPERIMENTAL;
import static carpet.api.settings.RuleCategory.FEATURE;
import static carpet.api.settings.RuleCategory.SURVIVAL;

public class QcaSettings {
    public static final String QCA = "qca";
    public static final String PVP = "pvp";

    @Rule(
        categories = {QCA, FEATURE, EXPERIMENTAL}
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

    public static int matchTallPlant(Block plant) {
        Set<String> settings = Set.copyOf(Arrays.stream(tallPlantShearToSmall.split(",")).toList());

        if (
            PlantTransformRecord.SMALL_TALL_GRASSES.containsValue(plant)
            && (settings.contains("enable") || settings.contains("grasses"))
        ) {
            return 1;
        } else if (
            PlantTransformRecord.SMALL_TALL_DRIPLEAF.containsValue(plant)
            && (settings.contains("enable") || settings.contains("dripleaf"))
        ) {
            return 2;
        } else if (
            PlantTransformRecord.SMALL_TALL_FLOWERS.containsValue(plant)
            && (settings.contains("enable") || settings.contains("flowers"))
        ) {
            return 3;
        }

        return 0;
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

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean pvpDoNotDamageEquipment = false;

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean pvpDoNotDamageWeapon = false;

    @Rule(categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean boneMealDoubleSmallFlowers = false;
}
