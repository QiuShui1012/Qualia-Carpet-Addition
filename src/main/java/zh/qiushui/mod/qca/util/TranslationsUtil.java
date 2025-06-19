package zh.qiushui.mod.qca.util;

import carpet.utils.Translations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class TranslationsUtil {
    public static Map<String, String> getTranslations(String lang) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return getTranslationsWithLang(gson, lang)
            .or(() -> getTranslationsWithLang(gson, "en_us"))
            .orElse(null);
    }

    private static Optional<Map<String, String>> getTranslationsWithLang(Gson gson, String lang) {
        return Optional.ofNullable(Translations.class.getClassLoader().getResourceAsStream("assets/qca/lang/" + lang + ".json"))
            .map(langStream -> gson.fromJson(
                new InputStreamReader(langStream, StandardCharsets.UTF_8),
                TypeToken.getParameterized(Map.class, String.class, String.class).getType()
            ));
    }
}