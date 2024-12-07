package zh.qiushui.mod.qca.rule.util.beaconIncreaseInteractionRange;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import zh.qiushui.mod.qca.QcaExtension;
import zh.qiushui.mod.qca.QcaSettings;

public class InteractionRangeEntityAttributeModifiers {
    public static final Identifier BEACON_BLOCK_RANGE_MODIFIER_IDENTIFIER = QcaExtension.id("beacon_effected_block_range");
    public static final Identifier BEACON_ENTITY_RANGE_MODIFIER_IDENTIFIER = QcaExtension.id("beacon_effected_entity_range");

    public static EntityAttributeModifier getBeaconBlockRangeModifier(int beaconLevel) {
        return new EntityAttributeModifier(
                BEACON_BLOCK_RANGE_MODIFIER_IDENTIFIER,
                QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
                QcaSettings.beaconIncreaseModeIsAdd()
                ? EntityAttributeModifier.Operation.ADD_VALUE
                : QcaSettings.beaconIncreaseModeIsBase()
                  ? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                  : EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    public static EntityAttributeModifier getBeaconEntityRangeModifier(int beaconLevel) {
        return new EntityAttributeModifier(
                BEACON_ENTITY_RANGE_MODIFIER_IDENTIFIER,
                QcaSettings.getBeaconIncreaseInteractionRangeValue(beaconLevel),
                QcaSettings.beaconIncreaseModeIsAdd()
                ? EntityAttributeModifier.Operation.ADD_VALUE
                : QcaSettings.beaconIncreaseModeIsBase()
                  ? EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                  : EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
