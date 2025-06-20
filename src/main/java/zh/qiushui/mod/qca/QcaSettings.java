package zh.qiushui.mod.qca;

import carpet.api.settings.Rule;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.block.Block;
import zh.qiushui.mod.qca.rule.util.PlantTransformUtil;

import java.util.Arrays;
import java.util.Set;

import static carpet.api.settings.RuleCategory.EXPERIMENTAL;
import static carpet.api.settings.RuleCategory.FEATURE;
import static carpet.api.settings.RuleCategory.SURVIVAL;

public class QcaSettings {
    public static final String QCA = "qca";
    public static final String PVP = "pvp";
    public static final String DEBUG = "debug";

    @Rule(
        categories = {QCA, DEBUG}
    )
    public static boolean qcaDebugLog = false;

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
            PlantTransformUtil.SMALL_TALL_GRASSES.containsValue(plant)
            && (settings.contains("enable") || settings.contains("grasses"))
        ) {
            return 1;
        } else if (
            PlantTransformUtil.SMALL_TALL_DRIPLEAF.containsValue(plant)
            && (settings.contains("enable") || settings.contains("dripleaf"))
        ) {
            return 2;
        } else if (
            PlantTransformUtil.SMALL_TALL_FLOWERS.containsValue(plant)
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
    public static int tooExpensiveLevel = 39;

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

    @Rule(
        categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL},
        options = {
            "disabled",
            "itemFrame", "customName",
            "itemFrame,customName"
        },
        validators = QcaValidators.LimitationSources.class
    )
    public static String easyHopperLimitation = "disabled";

    @Rule(
        categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL},
        options = {
            "disabled",
            "itemFrame", "customName",
            "itemFrame,customName"
        },
        validators = QcaValidators.LimitationSources.class
    )
    public static String crafterLimitation = "disabled";

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canLimit(String setting) {
        return !setting.contains("disabled");
    }

    public static boolean canLimitByItemFrame(String setting) {
        return setting.contains("itemFrame");
    }

    public static boolean canLimitByCustomName(String setting) {
        return setting.contains("customName");
    }

    @Rule(
        categories = {QCA, FEATURE, EXPERIMENTAL},
        options = {
            "add", "multiplyBase", "multiplyTotal",
            "addWithoutLevel", "multiplyBaseWithoutLevel", "multiplyTotalWithoutLevel",
            "false"
        },
        validators = QcaValidators.BeaconIncreaseInteractionRangeMode.class
    )
    public static String beaconIncreaseInteractionRange = "false";

    public static boolean beaconIncreaseIsEnabled() {
        return !beaconIncreaseInteractionRange.equals("false");
    }

    public static AttributeModifier.Operation beaconIncreaseMode() {
        return QcaSettings.beaconIncreaseModeIsAdd()
               ? AttributeModifier.Operation.ADD_VALUE
               : QcaSettings.beaconIncreaseModeIsBase()
                 ? AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                 : AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
    }

    private static boolean beaconIncreaseModeIsAdd() {
        return beaconIncreaseInteractionRange.contains("add");
    }

    private static boolean beaconIncreaseModeIsBase() {
        return beaconIncreaseInteractionRange.contains("Base");
    }

    public static boolean beaconIncreaseModeIsWithoutLevel() {
        return beaconIncreaseInteractionRange.contains("WithoutLevel");
    }

    @Rule(
        categories = {QCA, FEATURE, EXPERIMENTAL},
        validators = QcaValidators.BeaconIncreaseInteractionRangeValue.class
    )
    public static double beaconIncreaseInteractionRangeValue = 0.3;

    public static double getBeaconIncreaseInteractionRangeValue(int level) {
        boolean isAdd = beaconIncreaseModeIsAdd();
        return beaconIncreaseModeIsWithoutLevel()
               ? beaconIncreaseInteractionRangeValue
               : isAdd
                 ? (level + beaconIncreaseInteractionRangeValue)
                 : (level * beaconIncreaseInteractionRangeValue);
    }

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean pvpDoNotDamageEquipment = false;

    @Rule(categories = {QCA, PVP, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean pvpDoNotDamageWeapon = false;

    @Rule(categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL})
    public static boolean boneMealDoubleSmallFlowers = false;
}
