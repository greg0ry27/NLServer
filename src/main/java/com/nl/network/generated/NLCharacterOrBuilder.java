// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pockets.proto

package com.nl.network.generated;

public interface NLCharacterOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.nl.network.generated.NLCharacter)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 id = 1;</code>
   */
  long getId();

  /**
   * <code>string name = 2;</code>
   */
  java.lang.String getName();
  /**
   * <code>string name = 2;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>int32 level = 3;</code>
   */
  int getLevel();

  /**
   * <code>.com.nl.network.generated.PlayerCharacterType type = 4;</code>
   */
  int getTypeValue();
  /**
   * <code>.com.nl.network.generated.PlayerCharacterType type = 4;</code>
   */
  com.nl.network.generated.CharacterType getType();
}