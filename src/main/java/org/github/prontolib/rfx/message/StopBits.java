package org.github.prontolib.rfx.message;

public enum StopBits {

    ONE(0),
    ONE_AND_A_HALF(2),
    TWO(4);

    private final int flag;

    private StopBits(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

}
