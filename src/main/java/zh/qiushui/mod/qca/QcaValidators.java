package zh.qiushui.mod.qca;

import com.google.common.collect.ImmutableSet;
import dev.anvilcraft.rg.api.RGValidator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@ParametersAreNonnullByDefault
public class QcaValidators {
    public static class PlantTransform implements RGValidator<String> {
        private static final Set<String> OPTIONS = Set.of("enable", "grasses", "dripleaf", "flowers", "disable");

        @Override
        public boolean validate(String newValue, @NotNull String userInput) {
            String[] options = newValue.trim().split(",");
            return OPTIONS.containsAll(Arrays.stream(options).toList());
        }

        @Override
        public String reason() {
            return "Can be limited to different plant types only, enable/disable for all types/no types, or some of types.";
        }
    }

    public static class TooExpensiveLevel extends RGValidator.IntegerValidator {
        @Override
        public Map.@NotNull Entry<Integer, Integer> getRange() {
            return Map.entry(-1, Integer.MAX_VALUE);
        }

        @Override
        public Map.Entry<Boolean, Boolean> containsRange() {
            return Map.entry(true, false);
        }

        @Override
        public String reason() {
            return "This value represents the maximum repair cost level at which an item is considered too expensive to repair in an anvil. Must be -1 or larger";
        }
    }

    public static class LimitationSources implements RGValidator<String> {
        private static final Set<String> OPTIONS = Set.of("disabled", "itemFrame", "customName");

        @Override
        public boolean validate(String newValue, @NotNull String userInput) {
            String[] options = newValue.trim().split(",");
            return OPTIONS.containsAll(Arrays.stream(options).toList());
        }

        @Override
        public String reason() {
            return "Can be limited to limitation sources only. disabled for disable limitation.";
        }
    }

    public static class BeaconIncreaseInteractionRangeMode extends RGValidator.StringInSetValidator {
        private static final Set<String> MODES = ImmutableSet.of(
            "add", "multiplyBase", "multiplyTotal",
            "addWithoutLevel", "multiplyBaseWithoutLevel", "multiplyTotalWithoutLevel",
            "false"
        );

        @Override
        public Set<String> getSet() {
            return MODES;
        }

        @Override
        public String reason() {
            return "This value controls the mode of how to increase the value.\n\"addition\", \"multiplyBase\" and \"multiplyTotal\" are their literal meanings. \"WithoutLevel\" means it will ignore the beacon's level on calculate.";
        }
    }

    public static class BeaconIncreaseInteractionRangeValue implements RGValidator<Double> {
        @Override
        public boolean validate(Double newValue, String userInput) {
            return QcaServerRules.beaconIncreaseIsEnabled() && newValue > 0;
        }

        @Override
        public String reason() {
            return "This value represents the addend or multiplier will be used in calculate increase value. Rule beaconIncreaseInteractionRange must be enabled. Value must be positive.";
        }
    }
}
