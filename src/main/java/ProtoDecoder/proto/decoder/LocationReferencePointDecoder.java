package ProtoDecoder.proto.decoder;

import openlr.LocationReferencePoint;
import openlr.map.FormOfWay;
import openlr.map.FunctionalRoadClass;
import ProtoDecoder.proto.OpenLRProtoException;
import ProtoDecoder.proto.OpenLRProtoStatusCode;
import ProtoDecoder.impl.LocationReferencePointProtoImpl;
import joynext.protobuf.SnapshotOuterClass.OpenLR;

public class LocationReferencePointDecoder {
    LocationReferencePoint decode(OpenLR.LinearLocationReference.FirstLocationReferencePoint data, int sequenceNumber) throws OpenLRProtoException {
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

        if (!data.hasPathProperties()) {
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
        OpenLR.LinearLocationReference.PathProperties pathAttributes = data.getPathProperties();
        int distanceToNext = pathAttributes.getDistanceToNextLRP();
        FunctionalRoadClass lfrc = decode(pathAttributes.getLowestFRCToNextLRP());

        return new LocationReferencePointProtoImpl(
                sequenceNumber,
                longitudeDeg,
                latitudeDeg,
                bearing,
                frc,
                fow,
                distanceToNext,
                lfrc);
    }LocationReferencePoint decode(OpenLR.LinearLocationReference.IntermediateLocationReferencePoint data, int sequenceNumber) throws OpenLRProtoException {
        if (!data.hasRelativeCoordinate()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.GeoCoordinate coordinates = data.getRelativeCoordinate();
        double longitudeDeg = coordinates.getLongitude();
        double latitudeDeg = coordinates.getLatitude();

        if (!data.hasLineProperties()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.LinearLocationReference.LineProperties lineAttributes = data.getLineProperties();
        int bearing = lineAttributes.getBearing();
        FunctionalRoadClass frc = decode(lineAttributes.getFunctionalRoadClass());
        FormOfWay fow = decode(lineAttributes.getFormOfWay());

        if (!data.hasPathProperties()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
        OpenLR.LinearLocationReference.PathProperties pathAttributes = data.getPathProperties();
        int distanceToNext = pathAttributes.getDistanceToNextLRP();
        FunctionalRoadClass lfrc = decode(pathAttributes.getLowestFRCToNextLRP());

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
    LocationReferencePoint decode(OpenLR.LinearLocationReference.LastLocationReferencePoint data, int sequenceNumber) throws OpenLRProtoException {
        if (!data.hasRelativeCoordinate()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.GeoCoordinate coordinates = data.getRelativeCoordinate();
        double longitudeDeg = coordinates.getLongitude();
        double latitudeDeg = coordinates.getLatitude();

        if (!data.hasLineProperties()) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }

        OpenLR.LinearLocationReference.LineProperties lineAttributes = data.getLineProperties();
        int bearing = lineAttributes.getBearing();
        FunctionalRoadClass frc = decode(lineAttributes.getFunctionalRoadClass());
        FormOfWay fow = decode(lineAttributes.getFormOfWay());

        return new LocationReferencePointProtoImpl(
                sequenceNumber,
                longitudeDeg,
                latitudeDeg,
                bearing,
                frc,
                fow);
    }


    private FunctionalRoadClass decode(OpenLR.LinearLocationReference.FunctionalRoadClass frc) throws OpenLRProtoException {
        switch (frc) {
            case FUNCTIONAL_ROAD_CLASS_0:
                return FunctionalRoadClass.FRC_0;
            case FUNCTIONAL_ROAD_CLASS_1:
                return FunctionalRoadClass.FRC_1;
            case FUNCTIONAL_ROAD_CLASS_2:
                return FunctionalRoadClass.FRC_2;
            case FUNCTIONAL_ROAD_CLASS_3:
                return FunctionalRoadClass.FRC_3;
            case FUNCTIONAL_ROAD_CLASS_4:
                return FunctionalRoadClass.FRC_4;
            case FUNCTIONAL_ROAD_CLASS_5:
                return FunctionalRoadClass.FRC_5;
            case FUNCTIONAL_ROAD_CLASS_6:
                return FunctionalRoadClass.FRC_6;
            case FUNCTIONAL_ROAD_CLASS_7:
                return FunctionalRoadClass.FRC_7;
            default:
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
    }

    private FormOfWay decode(OpenLR.LinearLocationReference.LineProperties.FormOfWay fow) throws OpenLRProtoException {
        switch (fow) {
            case FORM_OF_WAY_UNDEFINED:
                return FormOfWay.UNDEFINED;
            case FORM_OF_WAY_MOTORWAY:
                return FormOfWay.MOTORWAY;
            case FORM_OF_WAY_MULTIPLE_CARRIAGEWAY:
                return FormOfWay.MULTIPLE_CARRIAGEWAY;
            case FORM_OF_WAY_SINGLE_CARRIAGEWAY:
                return FormOfWay.SINGLE_CARRIAGEWAY;
            case FORM_OF_WAY_ROUNDABOUT:
                return FormOfWay.ROUNDABOUT;
            case FORM_OF_WAY_TRAFFIC_SQUARE:
                return FormOfWay.TRAFFIC_SQUARE;
            case FORM_OF_WAY_SLIPROAD:
                return FormOfWay.SLIPROAD;
            case FORM_OF_WAY_OTHER:
                return FormOfWay.OTHER;
            default:
                throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_LOCATION_REFERENCE);
        }
    }
}
