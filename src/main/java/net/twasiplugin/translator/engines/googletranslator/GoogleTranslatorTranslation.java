package net.twasiplugin.translator.engines.googletranslator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.twasiplugin.translator.engines.BaseTranslation;
import org.apache.http.client.fluent.Request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class GoogleTranslatorTranslation extends BaseTranslation {

    public GoogleTranslatorTranslation(String target, String source, String query) {
        super(target, source, query);
    }

    public GoogleTranslatorTranslation(String target, String query) {
        this(target, "auto", query);
    }

    @Override
    protected void translate() {
        try {
            JsonArray obj = (rawResult = new JsonParser().parse(Request.Get(this.uri).execute().returnContent().asString())).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonArray();
            this.result = obj.get(0).getAsString();
            this.state = ResultState.SUCCESS;
            if (this.query.equalsIgnoreCase(this.result)) this.state = ResultState.IDENTICAL_TEXT;
            if (this.getRecognizedSource().equalsIgnoreCase(this.target)) this.state = ResultState.SAME_LANGUAGE;
        } catch (Exception e) {
            this.state = ResultState.ERROR;
        }
    }

    @Override
    public URI getUri() throws UnsupportedEncodingException, URISyntaxException {
        String bd = "https://translate.googleapis.com/translate_a/single" +
                "?client=gtx" + "&dt=t" + // Settings
                "&sl=" + this.source + // Set source language
                "&tl=" + this.target + // Set target language
                "&q=" + URLEncoder.encode(this.query, "UTF-8");
        return new URI(bd);
    }

    public String getRecognizedSource() {
        try {
            return ((JsonArray) this.rawResult).get(2).getAsString();
        } catch (Exception e) {
            return null;
        }
    }

}
