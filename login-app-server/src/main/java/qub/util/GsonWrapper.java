package qub.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides a central location for GSON Creation.
 */
public class GsonWrapper {

    /**
     * Gets a configured GSON object.
     * @return
     */
    public static Gson getGson() {

        return new GsonBuilder().setExclusionStrategies(new GsonExcludeMarkedFields()).create();

    }

}
