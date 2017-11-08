// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NetworkPackets.proto

package generated;

/**
 * Protobuf enum {@code generated.PacketType}
 */
public enum PacketType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>LOGIN = 0;</code>
   */
  LOGIN(0),
  /**
   * <code>LOGIN_ACCEPTED = 1;</code>
   */
  LOGIN_ACCEPTED(1),
  /**
   * <code>LOGIN_FAILED = 2;</code>
   */
  LOGIN_FAILED(2),
  /**
   * <code>PLAYER_INFO = 3;</code>
   */
  PLAYER_INFO(3),
  /**
   * <code>CREATE_CHARACTER = 4;</code>
   */
  CREATE_CHARACTER(4),
  /**
   * <code>CHARACTER_SELECT = 5;</code>
   */
  CHARACTER_SELECT(5),
  /**
   * <code>LOAD_LEVEL = 6;</code>
   */
  LOAD_LEVEL(6),
  /**
   * <code>LEVEL_READY = 7;</code>
   */
  LEVEL_READY(7),
  /**
   * <code>SERVER_ERROR = 8;</code>
   */
  SERVER_ERROR(8),
  /**
   * <code>PING = 9;</code>
   */
  PING(9),
  /**
   * <code>PONG = 10;</code>
   */
  PONG(10),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>LOGIN = 0;</code>
   */
  public static final int LOGIN_VALUE = 0;
  /**
   * <code>LOGIN_ACCEPTED = 1;</code>
   */
  public static final int LOGIN_ACCEPTED_VALUE = 1;
  /**
   * <code>LOGIN_FAILED = 2;</code>
   */
  public static final int LOGIN_FAILED_VALUE = 2;
  /**
   * <code>PLAYER_INFO = 3;</code>
   */
  public static final int PLAYER_INFO_VALUE = 3;
  /**
   * <code>CREATE_CHARACTER = 4;</code>
   */
  public static final int CREATE_CHARACTER_VALUE = 4;
  /**
   * <code>CHARACTER_SELECT = 5;</code>
   */
  public static final int CHARACTER_SELECT_VALUE = 5;
  /**
   * <code>LOAD_LEVEL = 6;</code>
   */
  public static final int LOAD_LEVEL_VALUE = 6;
  /**
   * <code>LEVEL_READY = 7;</code>
   */
  public static final int LEVEL_READY_VALUE = 7;
  /**
   * <code>SERVER_ERROR = 8;</code>
   */
  public static final int SERVER_ERROR_VALUE = 8;
  /**
   * <code>PING = 9;</code>
   */
  public static final int PING_VALUE = 9;
  /**
   * <code>PONG = 10;</code>
   */
  public static final int PONG_VALUE = 10;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @Deprecated
  public static PacketType valueOf(int value) {
    return forNumber(value);
  }

  public static PacketType forNumber(int value) {
    switch (value) {
      case 0: return LOGIN;
      case 1: return LOGIN_ACCEPTED;
      case 2: return LOGIN_FAILED;
      case 3: return PLAYER_INFO;
      case 4: return CREATE_CHARACTER;
      case 5: return CHARACTER_SELECT;
      case 6: return LOAD_LEVEL;
      case 7: return LEVEL_READY;
      case 8: return SERVER_ERROR;
      case 9: return PING;
      case 10: return PONG;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<PacketType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      PacketType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<PacketType>() {
          public PacketType findValueByNumber(int number) {
            return PacketType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return NetworkPackets.getDescriptor().getEnumTypes().get(0);
  }

  private static final PacketType[] VALUES = values();

  public static PacketType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private PacketType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:generated.PacketType)
}

