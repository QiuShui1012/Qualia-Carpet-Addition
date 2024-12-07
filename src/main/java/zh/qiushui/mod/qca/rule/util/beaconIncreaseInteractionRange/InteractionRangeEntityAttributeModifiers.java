package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;

public class InteractionRangeEntityAttributeModifiers {
    private static EntityAttributeModifier previousBeaconBlockRangeModifier = null;
    private static EntityAttributeModifier previousBeaconEntityRangeModifier = null;

    public static EntityAttributeModifier getPreviousBeaconBlockRangeModifier(int beaconLevel) {
        return previousBeaconBlockRangeModifier != null
               ? previousBeaconBlockRangeModifier
               : getBeaconBlockRangeModifier(beaconLevel);
    }
    public static EntityAttributeModifier getBeaconBlockRangeModifier(int beaconLevel) {
        previousBeaconBlockRangeModifier = new EntityAttributeModifier(
                QcaExtension.id("beacon_effected_block_range"),
                QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
                QcaSettings.beaconIncreaseModeIsAdd()
                ? EntityAttributeModifier.Operation.ADD_VALUE
                : QcaSettings.beaconIncreaseModeIsBase()
                  ? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                  : EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        return previousBeaconBlockRangeModifier;
    }

    public static EntityAttributeModifier getPreviousBeaconEntityRangeModifier(int beaconLevel) {
        return previousBeaconEntityRangeModifier != null
               ? previousBeaconEntityRangeModifier
               : getBeaconEntityRangeModifier(beaconLevel);
    }
    public static EntityAttributeModifier getBeaconEntityRangeModifier(int beaconLevel) {
        previousBeaconEntityRangeModifier = new EntityAttributeModifier(
                QcaExtension.id("beacon_effected_entity_range"),
                QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
                QcaSettings.beaconIncreaseModeIsAdd()
                ? EntityAttributeModifier.Operation.ADD_VALUE
                : QcaSettings.beaconIncreaseModeIsBase()
                  ? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                  : EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        return previousBeaconEntityRangeModifier;
    }
}
