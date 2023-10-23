package org.example;

import OpenLRImpl.LineImpl;
import OpenLRImpl.MapDatabaseImpl;
import OpenLRImpl.NodeImpl;
import org.locationtech.jts.geom.Point;

import java.sql.SQLException;

public class OsmAdapter {

  public static void main(String[] args) throws SQLException {
    System.out.println("OsmAdapter");
    MapDatabaseImpl mapDb = new MapDatabaseImpl();
    NodeImpl node = (NodeImpl)mapDb.getNode(5);
    Point pointGeometry = node.getPointGeometry();
    System.out.println("Getting node from Database :" + pointGeometry);
    LineImpl line = (LineImpl)mapDb.getLine(4972);
    System.out.println("Getting Line Geometry from Database :" +
                       line.getLineGeometry());
    System.out.println("Getting Start node LatitudeDeg from Line 4972: " +
            line.getStartNode());
  }
}