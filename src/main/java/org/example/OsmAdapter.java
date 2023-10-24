package org.example;

import OpenLRImpl.LineImpl;
import OpenLRImpl.MapDatabaseImpl;
import OpenLRImpl.NodeImpl;
import openlr.map.Node;
import org.locationtech.jts.geom.Point;

import java.sql.SQLException;
import java.util.Iterator;

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
    System.out.println("Getting Start node ID from Line 4972: " +
            line.getStartNode().getID());
    Iterator <Node> itr = mapDb.getAllNodes();
    while (itr.hasNext()) {
      // Get the next element from the iterator
      Node nodeItr = itr.next();

      // Now, 'node' contains the current element from the iteration
      // Do something with 'node'
      System.out.println(nodeItr.hashCode());
    }
  }
}