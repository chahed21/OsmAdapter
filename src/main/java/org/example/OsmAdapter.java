package org.example;

import Loader.RoutableOSMMapLoader;
import OpenLRImpl.MapDatabaseImpl;

import java.sql.SQLException;

public class OsmAdapter {

    public static void main(String[] args) throws SQLException {
        System.out.println("OsmAdapter");
        RoutableOSMMapLoader OsmLoader = new RoutableOSMMapLoader();
        /*System.out.println(OsmLoader.numberOfLines());
        //System.out.println(osm.getAllLinesList());
        System.out.println(OsmLoader.numberOfNodes());*/
        MapDatabaseImpl MapDb = new MapDatabaseImpl(OsmLoader);
        System.out.println(MapDb.getNumberOfLines());
        System.out.println(MapDb.getNumberOfNodes());

        //System.out.println(osm.getAllNodesList());
        /*try {
            GenerationTool.generate(
                    Files.readString(Path.of("jooq-config.xml"))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/


    }
}