package zh.qiushui.mod.qca;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import com.google.common.collect.ImmutableSet;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

public class QcaValidators {
    public static class PlantTransform extends Validator<String> {
        private static final Set<String> OPTIONS = Set.of("enable", "grasses", "dripleaf", "flowers", "disable");

        @Override
        public String validate(@Nullable CommandSourceStack source, CarpetRule<String> carpetRule, String newValue, String userInput) {
            String[] options = newValue.trim().split(",");
            return !OPTIONS.containsAll(Arrays.stream(options).toList()) ? null : newValue;
        }

        @Override
        public String description() {
            return "Can be limited to different plant types only, enable/disable for all types/no types, or some of types.";
        }
    }

    public static class TooExpensiveLevel extends Validator<Integer> {
        @Override
        public Integer validate(@Nullable CommandSourceStack source, CarpetRule<Integer> carpetRule, Integer newValue, String userInput) {
            return newValue < -1 ? 39 : newValue;
        }

        @Override
        public String description() {
            return "This value represents the maximum repair cost level at which an item is considered too expensive to repair in an anvil. Must be -1 or larger";
        }
    }

    public static class LimitationSources extends Validator<String> {
        private static final Set<String> OPTIONS = Set.of("disabled", "itemFrame", "customName");

        @Override
        public String validate(@Nullable CommandSourceStack source, CarpetRule<String> carpetRule, String newValue, String userInput) {
            String[] options = newValue.trim().split(",");
            return !OPTIONS.containsAll(Arrays.stream(options).toList()) ? null : newValue;
        }

        @Override
        public String description() {
            return "Can be limited to limitation sources only. disabled for disable limitation.";
        }
    }

    public static class BeaconIncreaseInteractionRangeMode extends Validator<String> {
        private static final Set<String> MODES = ImmutableSet.of(
            "add", "multiplyBase", "multiplyTotal",
            "addWithoutLevel", "multiplyBaseWithoutLevel", "multiplyTotalWithoutLevel",
            "false"
        );

        @Override
        public String validate(@Nullable CommandSourceStack source, CarpetRule<String> carpetRule, String newValue, String userInput) {
            return !MODES.contains(newValue) ? null : newValue;
        }

        @Override
        public String description() {
            return "This value controls the mode of how to increase the value.\n\"addition\", \"multiplyBase\" and \"multiplyTotal\" are their literal meanings. \"WithoutLevel\" means it will ignore the beacon's level on calculate.";
        }
    }

    public static class BeaconIncreaseInteractionRangeValue extends Validator<Double> {
        @Override
        public Double validate(@Nullable CommandSourceStack source, CarpetRule<Double> carpetRule, Double newValue, String userInput) {
            return (QcaSettings.beaconIncreaseIsEnabled() && newValue < 0) ? 0 : newValue;
        }

        @Override
        public String description() {
            return "This value represents the addend or multiplier will be used in calculate increase value. Rule beaconIncreaseInteractionRange must be enabled. Value must be positive.";
        }
    }
}