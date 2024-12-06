package zh.qiushui.mod.qca;

import carpet.api.settings.Rule;
import carpet.api.settings.Validators;
import net.minecraft.block.Block;
import zh.qiushui.mod.qca.rule.util.PlantTransformUtil;

import java.util.Arrays;
import java.util.Set;

import static carpet.api.settings.RuleCategory.*;

public class QcaSettings {
    public static final String QCA = "qca";

    @Rule(
            categories = {QCA, COMMAND, EXPERIMENTAL},
            options = {"ops", "0", "1", "2", "3", "4", "true", "false"},
            validators = Validators.CommandLevel.class
    )
    public static String commandTpPos = "ops";
    @Rule(
            categories = {QCA, COMMAND, EXPERIMENTAL},
            options = {"ops", "0", "1", "2", "3", "4", "true", "false"},
            validators = Validators.CommandLevel.class
    )
    public static String commandTpPlayer = "ops";

    @Rule(
            categories = {QCA, FEATURE, EXPERIMENTAL}
    )
    public static boolean breakDripleafKeepStem = false;

    @Rule(
            categories = {QCA, FEATURE, SURVIVAL, EXPERIMENTAL},
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
            categories = {QCA, FEATURE, EXPERIMENTAL},
            validators = QcaValidators.TooExpensiveLevel.class
    )
    public static int tooExpensiveLevel = 39;

    public static int getTooExpensiveLevel() {
        return tooExpensiveLevel == -1 ? Integer.MAX_VALUE - 1 : tooExpensiveLevel;
    }

    @Rule(
            categories = {QCA, FEATURE, EXPERIMENTAL}
    )
    public static double repairCostMultiplier = 2.0D;

    @Rule(
            categories = {QCA, FEATURE, EXPERIMENTAL}
    )
    public static boolean itemsCanPassThroughChains = false;

    @Rule(
            categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL}
    )
    public static boolean easierHopperRestriction = false;

    @Rule(
            categories = {QCA, SURVIVAL, FEATURE, EXPERIMENTAL}
    )
    public static boolean crafterRecipeCanRestrict = false;
}
