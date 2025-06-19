package zh.qiushui.mod.qca.api.parse;

import net.minecraft.world.item.Item;
import zh.qiushui.mod.qca.api.section.AllSection;
import zh.qiushui.mod.qca.api.section.AnySection;
import zh.qiushui.mod.qca.api.section.ItemSection;
import zh.qiushui.mod.qca.api.section.NotSection;
import zh.qiushui.mod.qca.api.section.Section;
import zh.qiushui.mod.qca.api.section.TagSection;
import zh.qiushui.mod.qca.util.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ItemPredicateParser {
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9_.-]+:[a-z0-9/._-]+$", Pattern.CASE_INSENSITIVE);

    public static Optional<Section> parseItemPredicate(String idRaw) {
        List<Token> tokens = tokenize(idRaw);
        if (tokens.isEmpty()) return Optional.empty();
        if (!hasValidIdOrTag(tokens)) return Optional.empty();

        Parser parser = new Parser(tokens);
        Optional<Section> ast = parser.parse();
        if (parser.currentToken().type() != TokenType.EOF) return Optional.empty();
        return ast;
    }

    private static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        int length = input.length();

        while (index < length) {
            char c = input.charAt(index);
            switch (c) {
                case ' ', '\t' -> index++;
                case '!' -> {
                    tokens.add(new Token(TokenType.NOT, "!", index));
                    index++;
                }
                case '&' -> {
                    tokens.add(new Token(TokenType.AND, "&", index));
                    index++;
                }
                case '|' -> {
                    tokens.add(new Token(TokenType.OR, "|", index));
                    index++;
                }
                case '(' -> {
                    tokens.add(new Token(TokenType.LEFT_PAREN, "(", index));
                    index++;
                }
                case ')' -> {
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", index));
                    index++;
                }
                default -> {
                    int start = index;
                    while (index < length) {
                        char ch = input.charAt(index);
                        if (ch == '!' || ch == '&' || ch == '|' || ch == '(' || ch == ')' || Character.isWhitespace(ch)) break;
                        index++;
                    }
                    String value = input.substring(start, index);

                    if (value.startsWith("#")) {
                        tokens.add(new Token(TokenType.TAG, value, start));
                    } else {
                        tokens.add(new Token(TokenType.ID, value, start));
                    }
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", input.length()));
        return tokens;
    }

    private static boolean hasValidIdOrTag(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.type() == TokenType.ID && isValidId(token.value())) {
                return true;
            }
            if (token.type() == TokenType.TAG && isValidId(token.value().substring(1))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidId(String str) {
        return ID_PATTERN.matcher(str).matches();
    }

    private static class Parser {
        private final List<Token> tokens;
        private int current = 0;

        Parser(List<Token> tokens) {
            this.tokens = tokens;
        }

        Token currentToken() {
            return this.tokens.get(this.current);
        }

        void advance() {
            if (!isAtEnd()) this.current++;
        }

        boolean isAtEnd() {
            return this.current >= this.tokens.size() || this.currentToken().type() == TokenType.EOF;
        }

        Optional<Section> parse() {
            return this.expression();
        }

        private Optional<Section> expression() {
            return this.any();
        }

        private Optional<Section> any() {
            List<Section> sections = new ArrayList<>();

            do {
                this.all().ifPresent(sections::add);
            } while (this.match(TokenType.OR));

            if (sections.isEmpty()) return Optional.empty();
            if (sections.size() == 1) return Optional.of(sections.getFirst());
            return Optional.of(new AnySection(sections));
        }

        private Optional<Section> all() {
            List<Section> sections = new ArrayList<>();

            do {
                this.not().ifPresent(sections::add);
            } while (this.match(TokenType.AND));

            if (sections.isEmpty()) return Optional.empty();
            if (sections.size() == 1) return Optional.of(sections.getFirst());
            return Optional.of(new AllSection(sections));
        }

        private Optional<Section> not() {
            if (this.match(TokenType.NOT)) return this.not().map(NotSection::new);
            return this.primary();
        }

        private Optional<Section> primary() {
            if (this.match(TokenType.LEFT_PAREN)) {
                Optional<Section> expr = this.expression();
                if (!this.match(TokenType.RIGHT_PAREN)) return Optional.empty();
                return expr;
            }

            if (this.match(TokenType.ID)) {
                return ParseUtil.parseItem(this.tokens.get(this.current - 1).value())
                    .map(Item::getDefaultInstance)
                    .map(ItemSection::new);
            }
            if (this.match(TokenType.TAG)) {
                return ParseUtil.parseItemTag(this.tokens.get(this.current - 1).value().substring(1))
                    .map(TagSection::new);
            }

            return Optional.empty();
        }

        private boolean match(TokenType type) {
            if (this.check(type)) {
                this.advance();
                return true;
            }
            return false;
        }

        private boolean check(TokenType type) {
            if (this.isAtEnd()) return false;
            return this.currentToken().type() == type;
        }
    }
}
