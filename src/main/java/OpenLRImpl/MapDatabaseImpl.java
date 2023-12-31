package OpenLRImpl;

import DataBase.DatasourceConfig;
import GeometryFunctions.GeometryFunctions;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.jooq.Condition;
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

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.sources.Tables.METADATA;
import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

/**
 * Implementation of the TomTom OpenLR MapDatabase interface.
 *
 * @author Emily Kast
 */

public class MapDatabaseImpl implements MapDatabase {
  GeometryFactory geometryFactory = new GeometryFactory();
  public static DSLContext ctx;
  public static Connection con;

  public MapDatabaseImpl() {
    try {
      this.con = DatasourceConfig.getConnection();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    this.ctx = DSL.using(con, SQLDialect.POSTGRES);
  }
  @Override
  public boolean hasTurnRestrictions() {
    return false;
  }

  @Override
  public Line getLine(long id) {
    return ctx
        .select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE, KANTEN.FRC,
                KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME, KANTEN.ONEWAY)
        .from(KANTEN)
        .where(KANTEN.LINE_ID.eq(id))
        .fetchOne()
        .map(record -> {
          LineImpl line = record.into(LineImpl.class);
          setLineGeometry(line);
          line.setMdb(this);
          return line;
        });
  }

  /**
   * Sets line geometry for each line in the list using WKT representation.

   */

  public static void setLineGeometry(LineImpl line) {

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
      return field("ST_AsText(geom)");
    else {
      return field("ST_AsText(ST_Reverse(geom))");
    }
  }
  @Override
  public Node getNode(long id) {
    return ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
        .from(KNOTEN)
        .where(KNOTEN.NODE_ID.eq(id))
        .fetchOne()
        .map(record -> {
          NodeImpl node = record.into(NodeImpl.class);
          setNodeGeometry(node); // Apply the transformation function
          node.setMdb(this); // Apply another transformation function if needed
          return node;
        });
  }
  @Override
  public boolean hasTurnRestrictionOnPath(List<? extends Line> path) {
    return false;
  }
  @Override
  public Iterator<Line>
  findLinesCloseByCoordinate(double longitude, double latitude, int distance) {
    double distanceDeg = GeometryFunctions.distToDeg(latitude, distance);
    Condition condition = DSL.condition(
        "ST_DWithin({0}::geometry, ST_SetSRID(ST_MakePoint({1}, {2}), 4326), {3})",
        DSL.field(name("geom")), DSL.val(longitude), DSL.val(latitude),
        DSL.val(distanceDeg));
    List<Line> closeByLines =
        ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE,
                   KANTEN.FRC, KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME,
                   KANTEN.ONEWAY)
            .from(KANTEN)
            .where(condition)
            .fetch()
            .map(record -> {
              LineImpl line = record.into(LineImpl.class);
              setLineGeometry(line); // Apply the transformation function
              line.setMdb(
                  this); // Apply another transformation function if needed
              return line;
            });

    return closeByLines.iterator();
  }
  @Override
  public Iterator<Node>
  findNodesCloseByCoordinate(double longitude, double latitude, int distance) {
    double distanceDeg = GeometryFunctions.distToDeg(latitude, distance);
    Condition condition = DSL.condition(
        "ST_DWithin({0}::geometry, ST_SetSRID(ST_MakePoint({1}, {2}), 4326), {3})",
        DSL.field(name("geom")), DSL.val(longitude), DSL.val(latitude),
        DSL.val(distanceDeg));
    List<Node> closeByNodes =
        ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
            .from(KNOTEN)
            .where(condition)
            .fetch()
            .map(record -> {
              NodeImpl node = record.into(NodeImpl.class);
              setNodeGeometry(node); // Apply the transformation function
              node.setMdb(
                  this); // Apply another transformation function if needed
              return node;
            });

    return closeByNodes.iterator();
  }
  @Override
  public Iterator<Line> getAllLines() {
    List<Line> allLines =
        ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE,
                   KANTEN.FRC, KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME,
                   KANTEN.ONEWAY)
            .from(KANTEN)
            .fetch()
            .map(record -> {
              LineImpl line = record.into(LineImpl.class);
              setLineGeometry(line);
              line.setMdb(this);
              return line;
            });
    return allLines.iterator();
  }

  @Override
  public Iterator<Node> getAllNodes() {

    List<Node> allNodes =
        ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
            .from(KNOTEN)
            .fetch()
            .map(record -> {
              NodeImpl node = record.into(NodeImpl.class);
              setNodeGeometry(node); // Apply the transformation function
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
  @Override
  public Rectangle2D.Double getMapBoundingBox() {

    double x = ctx.select(METADATA.LEFT_LAT).from(METADATA).fetchOne().value1();
    double y = ctx.select(METADATA.LEFT_LON).from(METADATA).fetchOne().value1();
    double width =
        ctx.select(METADATA.BBOX_WIDTH).from(METADATA).fetchOne().value1();
    double height =
        ctx.select(METADATA.BBOX_HEIGHT).from(METADATA).fetchOne().value1();
    return new Rectangle2D.Double(x, y, width, height);
  }

  public void close() throws Exception {
    if (ctx != null)
      ctx.close();
    if (con != null)
      con.close();
  }
  @Override
  public int getNumberOfNodes() {
    return ctx.selectCount().from(KNOTEN).fetchOne(0, int.class);
  }
  @Override
  public int getNumberOfLines() {
    return ctx.selectCount().from(KANTEN).fetchOne(0, int.class);
  }
}
