package org.example;

import OpenLRImpl.MapDatabaseImpl;

import java.sql.SQLException;

public class OsmAdapter {

    public static void main(String[] args) throws SQLException {
        System.out.println("OsmAdapter");
        MapDatabaseImpl MapDb = new MapDatabaseImpl();
        double  node = MapDb.getNode(5).getLatitudeDeg();
        System.out.println(node);

    }
}