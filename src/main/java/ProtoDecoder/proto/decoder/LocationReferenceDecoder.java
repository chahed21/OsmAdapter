package ProtoDecoder.proto.decoder;

import joynext.protobuf.SnapshotOuterClass.OpenLR;
import openlr.rawLocRef.RawLocationReference;
import ProtoDecoder.proto.OpenLRProtoException;
public interface LocationReferenceDecoder {
    RawLocationReference decode(String id, OpenLR data) throws OpenLRProtoException;
}
