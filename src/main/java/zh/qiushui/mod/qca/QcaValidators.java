package zh.qiushui.mod.qca;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
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
            return newValue < 0 ? -1 : newValue;
        }

        @Override
        public String description() {
            return "This value represents the maximum repair cost level at which an item is considered too expensive to repair in an anvil. Value lesser than 0 will disable the Too Expensive.";
        }
    }
}