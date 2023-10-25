package org.example;

import OpenLRImpl.LineImpl;
import OpenLRImpl.MapDatabaseImpl;
import OpenLRImpl.NodeImpl;
import openlr.map.Line;
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
    System.out.println("Getting Number of nodes from database: " +
            mapDb.getNumberOfNodes());
    System.out.println("Getting Number of lines from database: " +
            mapDb.getNumberOfLines());
    //Iterator <Line> itr = mapDb.getAllLines();
    /*while (itr.hasNext()) {
      // Get the next element from the iterator
      Line lineItr = itr.next();

      // Now, 'node' contains the current element from the iteration
      // Do something with 'node'
      System.out.println("Getting Start node ID from Line "+ lineItr.getID()+ ": " + lineItr.getStartNode().getID());
    }*/
    Iterator<Node> itlrNodeClose = mapDb.findNodesCloseByCoordinate(51.21663319921368,14.117331000000002,1);
    Iterator <Line> itlrLineClose = mapDb.findLinesCloseByCoordinate(51.05496611566341, 13.727288054098223,100000000);
    System.out.println(itlrNodeClose);
    System.out.println(itlrLineClose);
    int s=0;
    while (itlrLineClose.hasNext()) {
      ++s;
      // Get the next element from the iterator
      Line lineItr = itlrLineClose.next();

      // Now, 'node' contains the current element from the iteration
      // Do something with 'node'
      System.out.println("Getting Start node ID from Line "+ lineItr.getID()+ ": " + lineItr.getStartNode().getID());
    }
    System.out.println("Number of lines from database "+ s);
  }
}