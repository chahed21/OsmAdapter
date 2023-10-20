package OpenLRImpl;

import DataBase.DatasourceConfig;
import GeometryFunctions.*;
import openlr.map.Line;
import openlr.map.MapDatabase;
import openlr.map.Node;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;

import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.sources.tables.Kanten.KANTEN;
import static org.jooq.sources.tables.Knoten.KNOTEN;

/**
 * Implementation of the TomTom OpenLR MapDatabase interface.
 *
 * @author Emily Kast
 */

public class MapDatabaseImpl implements MapDatabase {
    private final DSLContext ctx;
    private final Connection con;
    GeometryFactory geometryFactory = new GeometryFactory();

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
    public Node getNode(long id) {

        NodeImpl Node = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN).where(KNOTEN.NODE_ID.eq(id).fetchInto(NodeImpl.class);

        return matchingNode.get();
    }
}
