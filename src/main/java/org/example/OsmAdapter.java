package org.example;

import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.impl.LocationReferenceBinaryImpl;
import openlr.rawLocRef.RawLocationReference;

import java.sql.SQLException;
import java.util.Base64;

public class OsmAdapter {

  public static void main(String[] args) throws SQLException, PhysicalFormatException {
    //base64 openlr binary string  to decode
    String openlr = "CwmQ9SVWJS2qBAD9/14tCQ==";
    //Initialize the binary decoder and decode the binary string
    OpenLRBinaryDecoder binaryDecoder = new OpenLRBinaryDecoder();
    ByteArray byteArray = new ByteArray(Base64.getDecoder().decode(openlr));
    System.out.println(byteArray);
    LocationReferenceBinaryImpl locationReferenceBinary = new LocationReferenceBinaryImpl("Test location", byteArray);
    System.out.println("locationReferenceBinary : 0"+locationReferenceBinary);
    RawLocationReference rawLocationReference = binaryDecoder.decodeData(locationReferenceBinary);
    System.out.println("rawLocationReference :"+rawLocationReference);

}
}
