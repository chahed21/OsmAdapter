package org.example;

import ProtoDecoder.proto.decoder.LineDecoder;
import ProtoDecoder.proto.decoder.LocationReferencePointDecoder;
import joynext.protobuf.OpenLR;
import openlr.PhysicalFormatException;
import openlr.rawLocRef.RawLocationReference;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class OsmAdapter {

  public static void main(String[] args) throws SQLException, PhysicalFormatException, FileNotFoundException {
    try {
      // Read data from the binary file
      /*FileInputStream input = new FileInputStream("src/resources/joynext.pbf"); // Replace "data.pbf" with your actual file name
      OpenLR openLRMessage = OpenLR.parseFrom(input);

      // Now you can work with the parsed OpenLR message
      // For example:
      OpenLR.LinearLocationReference linearLocationReference = openLRMessage.getLinearLocationReference();
      System.out.println("Longitude: " + linearLocationReference.getFirst().getAbsoluteCoordinate().getLongitude());
      System.out.println("Latitude: " + linearLocationReference.getFirst().getAbsoluteCoordinate().getLatitude());

      // Handle other fields as needed

      input.close();*/
      // Create an OpenLR message
      OpenLR.Builder openLRMessageBuilder = OpenLR.newBuilder();
      // Create a LinearLocationReference
      OpenLR.LinearLocationReference.Builder linearLocationBuilder = OpenLR.LinearLocationReference.newBuilder();

      // Set FirstLocationReferencePoint
      OpenLR.LinearLocationReference.FirstLocationReferencePoint.Builder firstLocationBuilder = OpenLR.LinearLocationReference.FirstLocationReferencePoint.newBuilder();
      OpenLR.GeoCoordinate.Builder geoCoordinateBuilder = OpenLR.GeoCoordinate.newBuilder();
      geoCoordinateBuilder.setLongitude(123456); // Set longitude according to your requirement
      geoCoordinateBuilder.setLatitude(789012);  // Set latitude according to your requirement
      firstLocationBuilder.setAbsoluteCoordinate(geoCoordinateBuilder.build());

      OpenLR.LinearLocationReference.LineProperties.Builder linePropertiesBuilder = OpenLR.LinearLocationReference.LineProperties.newBuilder();
      linePropertiesBuilder.setFunctionalRoadClass(OpenLR.LinearLocationReference.FunctionalRoadClass.FUNCTIONAL_ROAD_CLASS_1);
      linePropertiesBuilder.setFormOfWay(OpenLR.LinearLocationReference.LineProperties.FormOfWay.FORM_OF_WAY_MULTIPLE_CARRIAGEWAY);
      linePropertiesBuilder.setBearing(90); // Set bearing (angle from north) according to your requirement

      OpenLR.LinearLocationReference.PathProperties.Builder pathPropertiesBuilder = OpenLR.LinearLocationReference.PathProperties.newBuilder();
      pathPropertiesBuilder.setLowestFRCToNextLRP(OpenLR.LinearLocationReference.FunctionalRoadClass.FUNCTIONAL_ROAD_CLASS_2);
      pathPropertiesBuilder.setDistanceToNextLRP(1000); // Set distance in meters according to your requirement

      firstLocationBuilder.setLineProperties(linePropertiesBuilder.build());
      firstLocationBuilder.setPathProperties(pathPropertiesBuilder.build());
      linearLocationBuilder.setFirst(firstLocationBuilder.build());

      // Set LastLocationReferencePoint
      OpenLR.LinearLocationReference.LastLocationReferencePoint.Builder lastLocationBuilder = OpenLR.LinearLocationReference.LastLocationReferencePoint.newBuilder();
      geoCoordinateBuilder.setLongitude(223344); // Set longitude according to your requirement
      geoCoordinateBuilder.setLatitude(556677);  // Set latitude according to your requirement
      lastLocationBuilder.setRelativeCoordinate(geoCoordinateBuilder.build());

      linePropertiesBuilder.setFunctionalRoadClass(OpenLR.LinearLocationReference.FunctionalRoadClass.FUNCTIONAL_ROAD_CLASS_3);
      linePropertiesBuilder.setFormOfWay(OpenLR.LinearLocationReference.LineProperties.FormOfWay.FORM_OF_WAY_SINGLE_CARRIAGEWAY);
      linePropertiesBuilder.setBearing(180); // Set bearing (angle from north) according to your requirement

      lastLocationBuilder.setLineProperties(linePropertiesBuilder.build());
      linearLocationBuilder.setLast(lastLocationBuilder.build());
      // Build the LinearLocationReference
      OpenLR.LinearLocationReference linearLocationReference = linearLocationBuilder.build();
      openLRMessageBuilder.setLinearLocationReference(linearLocationBuilder.build());
      OpenLR openLRLocationReference = openLRMessageBuilder.build();
      LineDecoder exampleLineDecoder = new LineDecoder(new LocationReferencePointDecoder());
      RawLocationReference exampleRawLineLocRef = exampleLineDecoder.decode("0",openLRLocationReference);
      System.out.println(exampleRawLineLocRef.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
