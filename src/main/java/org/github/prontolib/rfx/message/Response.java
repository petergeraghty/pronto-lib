package org.github.prontolib.rfx.message;

import static com.igormaznitsa.jbbp.io.JBBPOut.BeginBin;

import java.io.IOException;

import com.igormaznitsa.jbbp.JBBPParser;
import com.igormaznitsa.jbbp.io.JBBPOut;
import com.igormaznitsa.jbbp.model.JBBPFieldStruct;
import com.igormaznitsa.jbbp.model.JBBPFieldUByte;
import com.igormaznitsa.jbbp.model.JBBPFieldUShort;

public class Response {

    // 8 bits
    private int unknown1;

    // 16 bits
    private int packetId;

    // 8 bits
    private int type;

    // 16 bits
    private int unknown3;

    // 16 bits
    private int unknown4;

    // 16 bits
    private int unknown5;

    // 16 bits
    private int unknown6;

    // 16 bits
    private int unknown7;

    public static Response deserialise(byte[] data) throws IOException {
        final JBBPParser responseParser = JBBPParser
                .prepare("ubyte unknown1;" + "ushort packetId; " + "ubyte type; " + "ushort unknown3; "
                        + "ushort unknown4; " + "ushort unknown5; " + "ushort unknown6; " + "ushort unknown7; ");

        final JBBPFieldStruct result = responseParser.parse(data);
        Response response = new Response();
        response.setPacketId(result.findFieldForNameAndType("packetId", JBBPFieldUShort.class).getAsInt());
        response.setType(result.findFieldForNameAndType("type", JBBPFieldUByte.class).getAsInt());
        return response;
    }

    public byte[] serialise() throws IOException {
        JBBPOut jbbpOut = BeginBin().Byte(unknown1).Short(packetId).Byte(type).Short(unknown3).Short(unknown4)
                .Short(unknown5).Short(unknown6).Short(unknown7);

        return jbbpOut.End().toByteArray();
    }

    public int getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(int unknown3) {
        this.unknown3 = unknown3;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public void setUnknown4(int unknown4) {
        this.unknown4 = unknown4;
    }

    public int getUnknown5() {
        return unknown5;
    }

    public void setUnknown5(int unknown5) {
        this.unknown5 = unknown5;
    }

    public int getUnknown6() {
        return unknown6;
    }

    public void setUnknown6(int unknown6) {
        this.unknown6 = unknown6;
    }

    public int getUnknown7() {
        return unknown7;
    }

    public void setUnknown7(int unknown7) {
        this.unknown7 = unknown7;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

}
