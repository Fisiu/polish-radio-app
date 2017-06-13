package pl.fidano.apps.polishradio.models;

import java.io.Serializable;

public class Radio implements Serializable {
    private String mLogoUrl;
    private String mName;
    private String mUrl;
    private String mStreamUrl;

    public Radio(String mLogoUrl, String mName, String mUrl, String mStreamUrl) {
        this.mLogoUrl = mLogoUrl;
        this.mName = mName;
        this.mUrl = mUrl;
        this.mStreamUrl = mStreamUrl;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public void setLogoUrl(String mLogoUrl) {
        this.mLogoUrl = mLogoUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String mStreamUrl) {
        this.mStreamUrl = mStreamUrl;
    }
}
