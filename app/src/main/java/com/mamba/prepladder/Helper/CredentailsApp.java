package com.mamba.prepladder.Helper;

public class CredentailsApp {
    String data;
    String id;
    String iv;
    String rendom;

    public CredentailsApp(String str, String str2, String str3, String str4) {
        this.rendom = str;
        this.iv = str2;
        this.data = str3;
        this.id = str4;
    }

    public String getData() {
        return this.data;
    }

    public String getId() {
        return this.id;
    }

    public String getIv() {
        return this.iv;
    }

    public String getRendom() {
        return this.rendom;
    }

    public void setData(String str) {
        this.data = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setIv(String str) {
        this.iv = str;
    }

    public void setRendom(String str) {
        this.rendom = str;
    }
}
