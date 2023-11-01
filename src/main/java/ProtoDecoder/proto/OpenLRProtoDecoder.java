package ProtoDecoder.proto;

import ProtoDecoder.proto.decoder.LocationReferenceDecoder;
import ProtoDecoder.proto.decoder.LocationTypeDecoderRegistry;
import joynext.protobuf.SnapshotOuterClass;
import joynext.protobuf.SnapshotOuterClass.OpenLR;
import openlr.LocationReference;
import openlr.LocationType;
import openlr.PhysicalDecoder;
import openlr.PhysicalFormatException;
import openlr.rawLocRef.RawLocationReference;

public class OpenLRProtoDecoder implements PhysicalDecoder {
    private final LocationTypeDecoderRegistry locationTypeDecoderRegistry = LocationTypeDecoderRegistry.create();

    @Override
    public Class<?> getDataClass() {
        return SnapshotOuterClass.OpenLR.class;
    }

    @Override
    public RawLocationReference decodeData(LocationReference data) throws PhysicalFormatException {
        LocationType locationType = data.getLocationType();
        LocationReferenceDecoder locationReferenceDecoder = locationTypeDecoderRegistry.getDecoder(locationType);

        if (locationReferenceDecoder == null) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.UNSUPPORTED_LOCATION_TYPE);
        }

        Object locationReferenceData = data.getLocationReferenceData();

        if (!(locationReferenceData instanceof OpenLR)) {
            throw new OpenLRProtoException(OpenLRProtoStatusCode.INVALID_DATA_TYPE);
        }

        return locationReferenceDecoder.decode(data.getID(), (OpenLR) locationReferenceData);
    }

    @Override
    public String getDataFormatIdentifier() {
        return OpenLRProtoConstants.DATA_FORMAT_IDENTIFIER;
    }
}
