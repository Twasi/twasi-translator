package net.twasiplugin.translator;

import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TranslationPlugin extends TwasiPlugin {

    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return UserPlugin.class;
    }

}
