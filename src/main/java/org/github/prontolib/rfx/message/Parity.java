package org.github.prontolib.rfx.message;

public enum Parity {

    NONE(0),
    EVEN(8),
    ODD(16),
    MARK(24),
    SPACE(32);

    private final int flag;

    private Parity(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

}
