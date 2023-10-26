package ProtoDecoder.proto.decoder;

import openlr.LocationReferencePoint;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import ProtoDecoder.proto.OpenLRProtoException;
import ProtoDecoder.proto.OpenLRProtoStatusCode;
import ProtoDecoder.impl.LocationReferencePointProtoImpl;
import joynext.protobuf.OpenLR;

public class LocationReferencePointDecoder {
    LocationReferencePoint decode(OpenLR.LinearLocationReference.FirstLocationReferencePoint data, int sequenceNumber, boolean isLast) throws OpenLRProtoException {
        if (!data.hasAbsoluteCoordinate()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.GeoCoordinate coordinates = data.getAbsoluteCoordinate();
        double longitudeDeg = coordinates.getLongitude();
        double latitudeDeg = coordinates.getLatitude();

        if (!data.hasLineProperties()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.LinearLocationReference.LineProperties lineAttributes = data.getLineProperties();
        int bearing = lineAttributes.getBearing();
        FunctionalRoadClass frc = decode(lineAttributes.getFunctionalRoadClass());
        FormOfWay fow = decode(lineAttributes.getFormOfWay());
        } else {
            if (!data.hasPathAttributes()) {
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
            }

            PathAttributes pathAttributes = data.getPathAttributes();
            int distanceToNext = pathAttributes.getDistanceToNext();
            FunctionalRoadClass lfrc = decode(pathAttributes.getLowestFrcAlongPath());

            return new LocationReferencePointProtoImpl(
                    sequenceNumber,
                    longitudeDeg,
                    latitudeDeg,
                    bearing,
                    frc,
                    fow,
                    distanceToNext,
                    lfrc);
        }
    }

    private FunctionalRoadClass decode(FunctionalRoadClass frc) throws OpenLRProtoException {
        switch (frc) {
            case FRC_0:
                return FunctionalRoadClass.FRC_0;
            case FRC_1:
                return FunctionalRoadClass.FRC_1;
            case FRC_2:
                return FunctionalRoadClass.FRC_2;
            case FRC_3:
                return FunctionalRoadClass.FRC_3;
            case FRC_4:
                return FunctionalRoadClass.FRC_4;
            case FRC_5:
                return FunctionalRoadClass.FRC_5;
            case FRC_6:
                return FunctionalRoadClass.FRC_6;
            case FRC_7:
                return FunctionalRoadClass.FRC_7;
            default:
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
    }

    private FormOfWay decode(FormOfWay fow) throws OpenLRProtoException {
        switch (fow) {
            case UNDEFINED:
                return FormOfWay.UNDEFINED;
            case MOTORWAY:
                return FormOfWay.MOTORWAY;
            case MULTIPLE_CARRIAGEWAY:
                return FormOfWay.MULTIPLE_CARRIAGEWAY;
            case SINGLE_CARRIAGEWAY:
                return FormOfWay.SINGLE_CARRIAGEWAY;
            case ROUNDABOUT:
                return FormOfWay.ROUNDABOUT;
            case TRAFFIC_SQUARE:
                return FormOfWay.TRAFFIC_SQUARE;
            case SLIPROAD:
                return FormOfWay.SLIPROAD;
            case OTHER:
                return FormOfWay.OTHER;
            default:
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
    }
}
