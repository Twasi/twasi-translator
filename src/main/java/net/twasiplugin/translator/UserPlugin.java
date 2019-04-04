package net.twasiplugin.translator;

import com.google.gson.Gson;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasiplugin.translator.engines.BaseTranslation;
import net.twasiplugin.translator.engines.googletranslator.GoogleTranslatorTranslation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserPlugin extends TwasiUserPlugin {

    private List<String> allowedLanguages;
    private List<TwasiPluginCommand> cmds;

    public UserPlugin() {
        this.allowedLanguages = Arrays.asList("de", "en", "fr", "sp", "ch");
        String languages = new Gson().toJson(allowedLanguages).replace("\\[", "").replace("\\]", "").replaceAll(",", ", ");
        cmds = Collections.singletonList(new TwasiPluginCommand(this) {
            @Override
            public void process(TwasiCustomCommandEvent e) {
                if (e.getArgs().size() == 0) {
                    e.reply(getTranslation("twasi.translator.helptext", languages));
                    return;
                }
                String targetLanguage = getTranslation("twasi.translator.googletranslator.languagecode");
                String query = e.getArgsAsOne();
                if (allowedLanguages.stream().anyMatch(e.getArgs().get(0)::equalsIgnoreCase)) {
                    if (e.getArgs().size() == 1) {
                        e.reply(getTranslation("twasi.translator.helptext", languages));
                        return;
                    } else {
                        targetLanguage = e.getArgs().get(0);
                        query = query.replaceFirst(targetLanguage + " ", "");
                    }
                }
                BaseTranslation translation = new GoogleTranslatorTranslation(targetLanguage, query);
                switch (translation.getState()) {
                    case IDENTICAL_TEXT:
                        e.reply(getTranslation("twasi.translator.warning.indenticaltext", e.getSender().getDisplayName()));
                        break;
                    case SAME_LANGUAGE:
                        e.reply(getTranslation("twasi.translator.warning.samelanguage", e.getSender().getDisplayName()));
                        break;
                    case SUCCESS:
                        e.reply(getTranslation("twasi.translator.success", translation.getResult()));
                        break;
                    case ERROR:
                        e.reply(getTranslation("twasi.translator.error"));
                        break;
                    case INVALID_INPUT:
                        e.reply(getTranslation("twasi.translator.warning.invalidinput", e.getSender().getDisplayName()));
                        break;
                }
            }

            @Override
            public String getCommandName() {
                return "translate";
            }

            @Override
            public boolean allowsListing() {
                return false;
            }

            @Override
            public boolean allowsTimer() {
                return false;
            }
        });
    }

    @Override
    public List<TwasiPluginCommand> getCommands() {
        return cmds;
    }
}
