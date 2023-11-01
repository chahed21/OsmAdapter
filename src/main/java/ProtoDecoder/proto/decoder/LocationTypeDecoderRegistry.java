package ProtoDecoder.proto.decoder;

import openlr.LocationType;

public class LocationTypeDecoderRegistry {
    private final LineDecoder lineDecoder;

    public static LocationTypeDecoderRegistry create() {
        LocationReferencePointDecoder locationReferencePointDecoder = new LocationReferencePointDecoder();
        LineDecoder lineDecoder = new LineDecoder(locationReferencePointDecoder);

        return new LocationTypeDecoderRegistry(
                lineDecoder
                );
    }

    LocationTypeDecoderRegistry(LineDecoder lineDecoder) {
        this.lineDecoder = lineDecoder;

    }

    public LocationReferenceDecoder getDecoder(LocationType locationType) {
        switch (locationType) {
            case LINE_LOCATION:
                return lineDecoder;
            case UNKNOWN:
                return null;
            default:
                throw new IllegalStateException();
        }
    }
}
