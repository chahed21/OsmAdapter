package OpenLRImpl;

import DataBase.DatasourceConfig;
import openlr.map.Line;
import openlr.map.Node;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.GeometryFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import static org.jooq.sources.tables.Knoten.KNOTEN;

/**
 * Implementation of the TomTom OpenLR MapDatabase interface.
 *
 * @author Emily Kast
 */

public class MapDatabaseImpl  {
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


    public boolean hasTurnRestrictions() {
        return false;
    }


    public Node getNode(long id) {

        NodeImpl node = ctx.select(KNOTEN.NODE_ID, KNOTEN.LAT, KNOTEN.LON)
                .from(KNOTEN)
                .where(KNOTEN.NODE_ID.eq(id))
                .fetchOneInto(NodeImpl.class);

        return node;
    }

    public Iterator<Line> getAllLines() {
        return null;
    }
}
