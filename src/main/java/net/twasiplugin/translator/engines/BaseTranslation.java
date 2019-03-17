package net.twasiplugin.translator.engines;

import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class BaseTranslation {

    protected URI uri;
    protected String source;
    protected String target;
    protected String query;

    protected String result = null;
    protected JsonElement rawResult = null;

    protected ResultState state;

    public BaseTranslation(String target, String source, String query) {
        this.target = target.toLowerCase();
        this.source = source.toLowerCase();
        this.query = query;
        try {
            this.uri = getUri();
            translate();
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            this.state = ResultState.INVALID_INPUT;
        }
    }

    protected abstract void translate();

    public abstract URI getUri() throws UnsupportedEncodingException, URISyntaxException;

    public String getTarget() {
        return target;
    }

    public String getSource() {
        return source;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    public ResultState getState() {
        return state;
    }

    public JsonElement getRawResult() {
        return rawResult;
    }

    public enum ResultState {
        SUCCESS, ERROR, IDENTICAL_TEXT, INVALID_INPUT, SAME_LANGUAGE
    }
}
