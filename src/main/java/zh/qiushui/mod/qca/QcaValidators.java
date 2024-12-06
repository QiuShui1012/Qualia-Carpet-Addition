package zh.qiushui.mod.qca;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

public class QcaValidators {
    public static class PlantTransform extends Validator<String> {
        private static final Set<String> OPTIONS = Set.of("enable", "grasses", "dripleaf", "flowers", "disable");

        @Override
        public String validate(
                @Nullable ServerCommandSource serverCommandSource, CarpetRule<String> carpetRule, String s, String s2
        ) {
            String[] options = s.trim().split(",");
            return OPTIONS.containsAll(Arrays.stream(options).toList()) ? s : null;
        }

        @Override
        public String description() {
            return "Can be limited to different plant types only, enable/disable for all types/no types, or some of types.";
        }
    }

    public static class TooExpensiveLevel extends Validator<Integer> {
        @Override
        public Integer validate(
                @Nullable ServerCommandSource serverCommandSource, CarpetRule<Integer> carpetRule, Integer newValue, String s
        ) {
            return newValue < -1 ? 39 : newValue;
        }

        @Override
        public String description() {
            return "This value represents the maximum repair cost level at which an item is considered too expensive to repair in an anvil. Must be -1 or larger";
        }
    }
}
