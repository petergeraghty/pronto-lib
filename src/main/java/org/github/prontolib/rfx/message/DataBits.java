package org.github.prontolib.rfx.message;

public enum DataBits {

    FIVE(0),
    SIX(64),
    SEVEN(128),
    EIGHT(192);

    private final int flag;

    private DataBits(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

}
