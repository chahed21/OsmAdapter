package ProtoDecoder.proto.decoder;

import ProtoDecoder.impl.OffsetsProtoImpl;
import ProtoDecoder.proto.OpenLRProtoException;
import ProtoDecoder.proto.OpenLRProtoStatusCode;
import joynext.protobuf.SnapshotOuterClass.OpenLR;
import joynext.protobuf.SnapshotOuterClass.OpenLR.LinearLocationReference;
import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;

import java.util.ArrayList;
import java.util.List;

public class LineDecoder implements LocationReferenceDecoder {
    private final LocationReferencePointDecoder locationReferencePointDecoder;

    public LineDecoder(LocationReferencePointDecoder locationReferencePointDecoder) {
        this.locationReferencePointDecoder = locationReferencePointDecoder;
    }

    @Override
    public RawLocationReference decode(String id, OpenLR data) throws OpenLRProtoException {
        if (!data.hasLinearLocationReference()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.MISSING_LOCATION_REFERENCE);
        }

        LinearLocationReference lineLocationReference = data.getLinearLocationReference();
        if (!(lineLocationReference.hasFirst() && lineLocationReference.hasLast()) ){
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        List<LocationReferencePoint> locationReferencePoints = new ArrayList<>();
        int sequenceNumber=1;
        LocationReferencePoint firstLocationReferencePoint = locationReferencePointDecoder.decode(lineLocationReference.getFirst(),sequenceNumber);
        locationReferencePoints.add(firstLocationReferencePoint);

        for (LinearLocationReference.IntermediateLocationReferencePoint intermediatePoint : lineLocationReference.getIntermediatesList()) {
            ++sequenceNumber;
            LocationReferencePoint intermediateLocationReferencePoint = locationReferencePointDecoder.decode(intermediatePoint,sequenceNumber);
            locationReferencePoints.add(intermediateLocationReferencePoint);
        }

        LocationReferencePoint lastLocationReferencePoint = locationReferencePointDecoder.decode(lineLocationReference.getLast(),sequenceNumber);
        ++sequenceNumber;
        locationReferencePoints.add(lastLocationReferencePoint);

        Offsets offsets = new OffsetsProtoImpl(
                0,
                0);

        return new RawLineLocRef(id, locationReferencePoints, offsets);
    }
}
