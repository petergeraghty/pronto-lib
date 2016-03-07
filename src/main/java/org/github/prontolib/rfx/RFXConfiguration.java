package org.github.prontolib.rfx;

public class RFXConfiguration {

    public static final String HEADER_ID = "X-Maestro-Extender-ID";
    public static final String HEADER_FIRMWARE_VERSION = "X-Maestro-Extender-Firmware";
    public static final String HEADER_MAC_ADDRESS = "X-Maestro-Extender-MAC";
    public static final String HEADER_RFX_TYPE = "X-Maestro-Extender-Type";
    public static final String HEADER_MODE = "X-Maestro-Extender-Mode";

    public static final String MODE_USE = "usemode";

    private int id;
    private String firmwareVersion;
    private String macAddress;
    private RFXType type;
    private boolean useMode;
    private String ipAddress;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public RFXType getType() {
        return type;
    }

    public void setType(RFXType type) {
        this.type = type;
    }

    public boolean isUseMode() {
        return useMode;
    }

    public void setUseMode(boolean useMode) {
        this.useMode = useMode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
