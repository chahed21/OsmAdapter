package OpenLRImpl;

import DataBase.DatasourceConfig;
import GeometryFunctions.GeometryFunctions;
import openlr.map.Line;
import openlr.map.Node;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

/**
 * Implementation of the TomTom OpenLR MapDatabase interface.
 *
 * @author Emily Kast
 */

public class MapDatabaseImpl {
  GeometryFactory geometryFactory = new GeometryFactory();
  private DSLContext ctx;
  private Connection con;

  public MapDatabaseImpl() {
    try {
      this.con = DatasourceConfig.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    this.ctx = DSL.using(con, SQLDialect.POSTGRES);
  }

  public boolean hasTurnRestrictions() { return false; }

  public Line getLine(long id) {
    return ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE,
                               KANTEN.END_NODE, KANTEN.FRC, KANTEN.FOW,
                               KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
                        .from(KANTEN)
                        .where(KANTEN.LINE_ID.eq(id))
                        .fetchOne().map(record -> {
                        LineImpl line = record.into(LineImpl.class);
                        setLineGeometry(line);
                        line.setMdb(this);
                        return line;
            });
  }

  /**
   * Sets line geometry for each line in the list using WKT representation.
   * @param linesList list containing all lines in the road network
   */
  private void setLineGeometry(LineImpl line) {

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    WKTReader reader = new WKTReader(geometryFactory);
    String wktString;
    wktString = ctx.select(st_asText(line.isReversed()))
                    .from(KANTEN)
                    .where(KANTEN.LINE_ID.eq(line.getID()))
                    .fetchOne()
                    .value1()
                    .toString();
    try {
      line.setLineGeometry((LineString)reader.read(wktString));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private static Field<?> st_asText(boolean isReversed) {
    if (!isReversed)
      return DSL.field("ST_AsText(geom)");
    else {
      return DSL.field("ST_AsText(ST_Reverse(geom))");
    }
  }

  public Node getNode(long id) {
    return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
            .from(KNOTEN)
            .where(KNOTEN.NODE_ID.eq(id))
            .fetchOne()
            .map(record -> {
              NodeImpl node = record.into(NodeImpl.class);
              setNodeGeometry(node); // Apply the transformation function
              setConnectedLinesList(node);
              node.setMdb(this);// Apply another transformation function if needed
              return node;
            });
  }
  public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {
    return false;
  }
  private void setConnectedLinesList(NodeImpl node ) {
   List<Long> connectedLinesIDs = ctx.select().from(KANTEN)
              .where(KANTEN.START_NODE.eq(node.getID()))
              .or(KANTEN.END_NODE.eq(node.getID()))
              .fetch().getValues(KANTEN.LINE_ID);

    node.setConnectedLinesIDs(connectedLinesIDs);

  }

  Iterator<Node> findNodesCloseByCoordinate(double longitude, double latitude,
                                            int distance) {
    double distanceDeg = GeometryFunctions.distToDeg(latitude, distance);
    Point p = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    List<Node> closeByNodes = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                                  .from(KNOTEN)
                                  .fetchInto(NodeImpl.class);
    return null;
  }

  public Iterator<Line> getAllLines() { return null; }

  public Iterator<Node> getAllNodes() {

    List<Node> allNodes = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
            .from(KNOTEN)
            .fetch()
            .map(record -> {
              NodeImpl node = record.into(NodeImpl.class);
              setNodeGeometry(node); // Apply the transformation function
              setConnectedLinesList(node); // Apply another transformation function if needed
              return node;
            });
    return allNodes.iterator();
  }

  private void setNodeGeometry(NodeImpl node) {
    GeometryFactory factory = new GeometryFactory();
    Point point =
            factory.createPoint(new Coordinate(node.getLon(), node.getLat()));
    node.setPointGeometry(point);
  }

  public void close() throws Exception {
    if (ctx != null)
      ctx.close();
    if (con != null)
      con.close();
  }
}
