package org.example;

import OpenLRImpl.MapDatabaseImpl;
import ProtoDecoder.proto.decoder.LineDecoder;
import ProtoDecoder.proto.decoder.LocationReferencePointDecoder;
import joynext.protobuf.SnapshotOuterClass;
import openlr.PhysicalFormatException;
import openlr.decoder.OpenLRDecoder;
import openlr.decoder.OpenLRDecoderParameter;
import openlr.location.Location;
import openlr.map.MapDatabase;
import openlr.properties.OpenLRPropertiesReader;
import openlr.rawLocRef.RawLocationReference;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class OsmAdapter {
  private static final Logger logger = LoggerFactory.getLogger(OsmAdapter.class);
  public static void main(String[] args) throws SQLException, PhysicalFormatException, FileNotFoundException {
    try {

      // Read data from the binary file
      BasicConfigurator.configure();
      Scanner sc= new Scanner(System.in);    //System.in is a standard input stream
      System.out.print("Enter Screenshot number- ");
      int id= sc.nextInt();
      FileInputStream input = new FileInputStream("src/resources/deutschland.pbf"); // Replace "data.pbf" with your actual file name
      SnapshotOuterClass.Snapshot snapshotMessage = SnapshotOuterClass.Snapshot.parseFrom(input);
      LineDecoder exampleLineDecoder = new LineDecoder(new LocationReferencePointDecoder());
      SnapshotOuterClass.OpenLR messageOpenLR = null;
      for (SnapshotOuterClass.Snapshot.Message message: snapshotMessage.getMessageList()){
        if (id == message.getMessageManagement().getId()){
          messageOpenLR = message.getLocation().getOpenLR();
          System.out.println(messageOpenLR);
          break;
        }
      }

      RawLocationReference exampleRawLineLocRef = exampleLineDecoder.decode("1",messageOpenLR);
      System.out.println("rawLocationReference from protobuf:"+exampleRawLineLocRef);
      // Initialize database
      MapDatabase mapDatabase = new MapDatabaseImpl();
      Instant start = Instant.now();
      FileConfiguration decoderConfig = OpenLRPropertiesReader.loadPropertiesFromFile(new File("src/resources/OpenLR-Decoder-Properties.xml"));
      OpenLRDecoderParameter params = new OpenLRDecoderParameter.Builder().with(mapDatabase).with(decoderConfig).buildParameter();
      //Initialize the decoderL
      OpenLRDecoder decoder = new openlr.decoder.OpenLRDecoder();
      Location location = decoder.decodeRaw(params, exampleRawLineLocRef);
      Instant end = Instant.now();
      long duration = Duration.between(start, end).toMillis();
      logger.info("Decoding ended. Duration: " + duration+" ms ");
      System.out.println("From protobuf decoder :"+location.toString());
      System.out.println("message ID : "+id);
      ((MapDatabaseImpl) mapDatabase).close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
