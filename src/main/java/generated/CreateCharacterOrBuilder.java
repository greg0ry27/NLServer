// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NetworkPackets.proto

package generated;

public interface CreateCharacterOrBuilder extends
    // @@protoc_insertion_point(interface_extends:generated.CreateCharacter)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string name = 1;</code>
   */
  String getName();
  /**
   * <code>string name = 1;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>.generated.CharacterType type = 2;</code>
   */
  int getTypeValue();
  /**
   * <code>.generated.CharacterType type = 2;</code>
   */
  CharacterType getType();
}
