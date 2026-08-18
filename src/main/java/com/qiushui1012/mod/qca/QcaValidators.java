package com.qiushui1012.mod.qca;

import dev.anvilcraft.rg.api.RGValidator;

import java.util.Map;

public class QcaValidators {
    public static class TooExpensiveLevel extends RGValidator.IntegerValidator {
        @Override
        public Map.Entry<Integer, Integer> getRange() {
            return Map.entry(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        @Override
        public String reason() {
            return "This value represents the maximum repair cost level at which an item is considered too expensive to repair in an anvil.";
        }
    }
}
