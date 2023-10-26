package ProtoDecoder.proto.decoder;

import ProtoDecoder.impl.OffsetsProtoImpl;
import ProtoDecoder.proto.OpenLRProtoException;
import ProtoDecoder.proto.OpenLRProtoStatusCode;
import joynext.protobuf.OpenLR;
import joynext.protobuf.OpenLR.LinearLocationReference;
import openlr.LocationReferencePoint;
import openlr.Offsets;
import openlr.rawLocRef.RawLineLocRef;
import openlr.rawLocRef.RawLocationReference;

import java.util.ArrayList;
import java.util.List;

public class LineDecoder implements LocationReferenceDecoder {
    private final LocationReferencePointDecoder locationReferencePointDecoder;

    LineDecoder(LocationReferencePointDecoder locationReferencePointDecoder) {
        this.locationReferencePointDecoder = locationReferencePointDecoder;
    }

    @Override
    public RawLocationReference decode(String id, OpenLR data) throws OpenLRProtoException {
        if (!data.hasLinearLocationReference()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.MISSING_LOCATION_REFERENCE);
        }

        LinearLocationReference lineLocationReference = data.getLinearLocationReference();
        if (lineLocationReference.hasFirst() || lineLocationReference.hasLast() ){
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
        List<LocationReferencePoint> locationReferencePoints = new ArrayList<>();

        for (int sequenceNumber = 1; sequenceNumber <= lrpCount; sequenceNumber++) {
            LocationReferencePoint locationReferencePoint = locationReferencePointDecoder.decode(
                    LinearLocationReference.getLocationReferencePoints(sequenceNumber - 1),
                    sequenceNumber,
                    sequenceNumber == lrpCount);
            locationReferencePoints.add(locationReferencePoint);
        }

        Offsets offsets = new OffsetsProtoImpl(
                lineLocationReference.getPositiveOffset(),
                lineLocationReference.getNegativeOffset());

        return new RawLineLocRef(id, locationReferencePoints, offsets);
    }
}
