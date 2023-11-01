// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: OpenLR.proto

package joynext.protobuf;

public interface OpenLROrBuilder extends
    // @@protoc_insertion_point(interface_extends:OpenLR)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * mandatory, Represents a linear location reference as defined
   * </pre>
   *
   * <code>.OpenLR.LinearLocationReference linearLocationReference = 1;</code>
   */
  boolean hasLinearLocationReference();
  /**
   * <pre>
   * mandatory, Represents a linear location reference as defined
   * </pre>
   *
   * <code>.OpenLR.LinearLocationReference linearLocationReference = 1;</code>
   */
  joynext.protobuf.OpenLR.LinearLocationReference getLinearLocationReference();
  /**
   * <pre>
   * mandatory, Represents a linear location reference as defined
   * </pre>
   *
   * <code>.OpenLR.LinearLocationReference linearLocationReference = 1;</code>
   */
  joynext.protobuf.OpenLR.LinearLocationReferenceOrBuilder getLinearLocationReferenceOrBuilder();

  /**
   * <pre>
   * in OpenLR location reference (TPEG2-OLR_1.0/002)
   * </pre>
   *
   * <code>.OpenLR.AreaLocationReference areaLocationReference = 2;</code>
   */
  boolean hasAreaLocationReference();
  /**
   * <pre>
   * in OpenLR location reference (TPEG2-OLR_1.0/002)
   * </pre>
   *
   * <code>.OpenLR.AreaLocationReference areaLocationReference = 2;</code>
   */
  joynext.protobuf.OpenLR.AreaLocationReference getAreaLocationReference();
  /**
   * <pre>
   * in OpenLR location reference (TPEG2-OLR_1.0/002)
   * </pre>
   *
   * <code>.OpenLR.AreaLocationReference areaLocationReference = 2;</code>
   */
  joynext.protobuf.OpenLR.AreaLocationReferenceOrBuilder getAreaLocationReferenceOrBuilder();

  public joynext.protobuf.OpenLR.LocationReferenceCase getLocationReferenceCase();
}
