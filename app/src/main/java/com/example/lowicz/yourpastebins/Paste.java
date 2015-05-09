package com.example.lowicz.yourpastebins;

import java.io.Serializable;

/**
 * Created by s391382 on 2015-05-09.
 */
public class Paste implements Serializable {
    //dodaj ViewAnimator
    private String url;
    private String paste_text;
    private String pasteID;

    public String getPaste_text() {
        return paste_text;
    }

    public void setPaste_text(String paste_text) {
        this.paste_text = paste_text;
    }

    public String getUrl() {
        return url;
    }

    public String getPasteID() {
        return pasteID;
    }

    public void setPasteID(String pasteID) {
        this.pasteID = pasteID;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
