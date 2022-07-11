package com.vnpt.retrofit;

import java.io.Serializable;

public class CompanyInfo implements Serializable {

    private Integer ID;
    private String MST;
    private String NAME;
    private String URL;
    private String PORTAL;
    private String USER;
    private String PASS;
    private Integer STATUS;
    private Integer TYPE;

    public CompanyInfo(Integer ID, String MST, String NAME, String URL, String PORTAL, String USER, String PASS, Integer STATUS, Integer TYPE) {
        this.ID = ID;
        this.MST = MST;
        this.NAME = NAME;
        this.URL = URL;
        this.PORTAL = PORTAL;
        this.USER = USER;
        this.PASS = PASS;
        this.STATUS = STATUS;
        this.TYPE = TYPE;
    }

    public CompanyInfo() {
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getMST() {
        return MST;
    }

    public void setMST(String MST) {
        this.MST = MST;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPORTAL() {
        return PORTAL;
    }

    public void setPORTAL(String PORTAL) {
        this.PORTAL = PORTAL;
    }

    public String getUSER() {
        return USER;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public Integer getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(Integer STATUS) {
        this.STATUS = STATUS;
    }
}
