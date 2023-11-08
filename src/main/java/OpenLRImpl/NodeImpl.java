package OpenLRImpl;

import openlr.map.*;
import org.locationtech.jts.geom.Point;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.jooq.sources.Tables.KANTEN;

/**
 * Implementation of the TomTom OpenLR Node interface.
 *
 * @author Emily Kast
 */

public class NodeImpl implements Node {

    long node_id;
    double lat;
    double lon;
    Point pointGeometry;
    MapDatabaseImpl mdb;


    public NodeImpl(long node_id, double lat, double lon) {
        this.node_id = node_id;
        this.lat = lat;
        this.lon = lon;

    }

    public void setPointGeometry(Point pointGeometry) {
        this.pointGeometry = pointGeometry;
    }


    public void setMdb(MapDatabaseImpl mdb) {
        this.mdb = mdb;
    }

    public double getLat() {

        return lat;
    }

    public double getLon() {

        return lon;
    }

    public Point getPointGeometry() {

        return pointGeometry;
    }

    @Override
    public double getLatitudeDeg() {

        return lat;
    }

    @Override
    public double getLongitudeDeg() {

        return lon;
    }

    @Override
    public GeoCoordinates getGeoCoordinates() {
        GeoCoordinates coordinates = null;
        try {
            coordinates = new GeoCoordinatesImpl(lon, lat);
        } catch (InvalidMapDataException e) {
            e.printStackTrace();
        }
        return coordinates;
    }


    @Override
    public Iterator<Line> getConnectedLines() {
        List<Line> getConnectedLines =
                MapDatabaseImpl.ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE,
                                KANTEN.FRC, KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME,
                                KANTEN.ONEWAY)
                        .from(KANTEN).where(KANTEN.START_NODE.eq(getID()))
                        .or(KANTEN.END_NODE.eq(getID()))
                        .fetch()
                        .map(record -> {
                            LineImpl line = record.into(LineImpl.class);
                            MapDatabaseImpl.setLineGeometry(line);
                            line.setMdb(mdb);
                            return line;
                        });
        return getConnectedLines.iterator();
    }

    @Override
    public int getNumberConnectedLines() {
        return MapDatabaseImpl.ctx
                .selectCount()
                .from(KANTEN)
                .where(KANTEN.START_NODE.eq(getID()))
                .or(KANTEN.END_NODE.eq(getID()))
                .fetchOne(0, int.class);
    }

    @Override
    public Iterator<Line> getOutgoingLines() {
        List<Line> outgoingLines =
                MapDatabaseImpl.ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE,
                                KANTEN.FRC, KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME,
                                KANTEN.ONEWAY)
                        .from(KANTEN).where(KANTEN.START_NODE.eq(getID()))
                        .fetch()
                        .map(record -> {
                            LineImpl line = record.into(LineImpl.class);
                            MapDatabaseImpl.setLineGeometry(line);
                            line.setMdb(mdb);
                            return line;
                        });
        return outgoingLines.iterator();
    }

    @Override
    public Iterator<Line> getIncomingLines() {
        List<Line> incomingLines =
                MapDatabaseImpl.ctx.select(KANTEN.LINE_ID, KANTEN.START_NODE, KANTEN.END_NODE,
                                KANTEN.FRC, KANTEN.FOW, KANTEN.LENGTH_METER, KANTEN.NAME,
                                KANTEN.ONEWAY)
                        .from(KANTEN).where(KANTEN.END_NODE.eq(getID()))
                        .fetch()
                        .map(record -> {
                            LineImpl line = record.into(LineImpl.class);
                            MapDatabaseImpl.setLineGeometry(line);
                            line.setMdb(mdb);
                            return line;
                        });
        return incomingLines.iterator();
    }

    @Override
    public long getID() {
        return node_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return node_id == node.node_id &&
                Double.compare(node.lat, lat) == 0 &&
                Double.compare(node.lon, lon) == 0 &&
                pointGeometry.equals(node.pointGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node_id, lat, lon, pointGeometry);
    }
}
