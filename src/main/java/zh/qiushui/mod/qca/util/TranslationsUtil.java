package zh.qiushui.mod.qca.util;

import carpet.utils.Translations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class TranslationsUtil {
    public static Map<String, String> getTranslations(String lang) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        try {
            return getTranslationsWithLang(gson, lang);
        } catch (NullPointerException ignored) {
            try {
                return getTranslationsWithLang(gson, "en_us");
            } catch (NullPointerException ignored1) {
                return null;
            }
        }
    }

    private static Map<String, String> getTranslationsWithLang(Gson gson, String lang) throws NullPointerException {
        return gson.fromJson(
                new InputStreamReader(
                        Objects.requireNonNull(
                                Translations.class
                                        .getClassLoader()
                                        .getResourceAsStream("assets/qca/lang/%s.json".formatted(lang))
                        ),
                        StandardCharsets.UTF_8
                ),
                TypeToken.getParameterized(Map.class, String.class, String.class).getType()
        );
    }
}
