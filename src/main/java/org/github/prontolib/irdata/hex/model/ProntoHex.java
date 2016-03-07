package org.github.prontolib.irdata.hex.model;

import java.util.List;

public class ProntoHex {

    // 16 bits
    private int type;

    // 16 bits
    private int period;

    // 16 bits
    private int burstLength;

    // 16 bits
    private int repeatBurstLength;

    // burstLength * 32 bits
    private List<BurstPair> burst;

    // repeatBurstLength * 32 bits
    private List<BurstPair> repeatBurst;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getBurstLength() {
        return burstLength;
    }

    public void setBurstLength(int burstLength) {
        this.burstLength = burstLength;
    }

    public int getRepeatBurstLength() {
        return repeatBurstLength;
    }

    public void setRepeatBurstLength(int repeatBurstLength) {
        this.repeatBurstLength = repeatBurstLength;
    }

    public List<BurstPair> getBurst() {
        return burst;
    }

    public void setBurst(List<BurstPair> burst) {
        this.burst = burst;
    }

    public List<BurstPair> getRepeatBurst() {
        return repeatBurst;
    }

    public void setRepeatBurst(List<BurstPair> repeatBurst) {
        this.repeatBurst = repeatBurst;
    }

}
