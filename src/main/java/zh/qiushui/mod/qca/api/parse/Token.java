package zh.qiushui.mod.qca.api.parse;

import org.jetbrains.annotations.NotNull;

public record Token(TokenType type, String value, int position) {
    @Override
    public @NotNull String toString() {
        return type + "('" + value + "')";
    }
}
