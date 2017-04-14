// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MatchMessage.proto

package com.artisankid.elementwar.ewmodel;

public final class MatchMessageOuterClass {
  private MatchMessageOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MatchMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:MatchMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    String getMessageId();
    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    com.google.protobuf.ByteString
        getMessageIdBytes();

    /**
     * <pre>
     *发送时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
     * </pre>
     *
     * <code>double send_time = 2;</code>
     */
    double getSendTime();

    /**
     * <pre>
     *过期时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
     * </pre>
     *
     * <code>double expired_time = 3;</code>
     */
    double getExpiredTime();

    /**
     * <pre>
     *是否需要响应
     * </pre>
     *
     * <code>bool needResponse = 4;</code>
     */
    boolean getNeedResponse();

    /**
     * <pre>
     *发送者ID
     * </pre>
     *
     * <code>string sender_id = 5;</code>
     */
    String getSenderId();
    /**
     * <pre>
     *发送者ID
     * </pre>
     *
     * <code>string sender_id = 5;</code>
     */
    com.google.protobuf.ByteString
        getSenderIdBytes();
  }
  /**
   * <pre>
   *匹配消息
   * </pre>
   *
   * Protobuf type {@code MatchMessage}
   */
  public  static final class MatchMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:MatchMessage)
      MatchMessageOrBuilder {
    // Use MatchMessage.newBuilder() to construct.
    private MatchMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private MatchMessage() {
      messageId_ = "";
      sendTime_ = 0D;
      expiredTime_ = 0D;
      needResponse_ = false;
      senderId_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private MatchMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              String s = input.readStringRequireUtf8();

              messageId_ = s;
              break;
            }
            case 17: {

              sendTime_ = input.readDouble();
              break;
            }
            case 25: {

              expiredTime_ = input.readDouble();
              break;
            }
            case 32: {

              needResponse_ = input.readBool();
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              senderId_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return MatchMessageOuterClass.internal_static_MatchMessage_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return MatchMessageOuterClass.internal_static_MatchMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              MatchMessage.class, Builder.class);
    }

    public static final int MESSAGE_ID_FIELD_NUMBER = 1;
    private volatile Object messageId_;
    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    public String getMessageId() {
      Object ref = messageId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        messageId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    public com.google.protobuf.ByteString
        getMessageIdBytes() {
      Object ref = messageId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        messageId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int SEND_TIME_FIELD_NUMBER = 2;
    private double sendTime_;
    /**
     * <pre>
     *发送时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
     * </pre>
     *
     * <code>double send_time = 2;</code>
     */
    public double getSendTime() {
      return sendTime_;
    }

    public static final int EXPIRED_TIME_FIELD_NUMBER = 3;
    private double expiredTime_;
    /**
     * <pre>
     *过期时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
     * </pre>
     *
     * <code>double expired_time = 3;</code>
     */
    public double getExpiredTime() {
      return expiredTime_;
    }

    public static final int NEEDRESPONSE_FIELD_NUMBER = 4;
    private boolean needResponse_;
    /**
     * <pre>
     *是否需要响应
     * </pre>
     *
     * <code>bool needResponse = 4;</code>
     */
    public boolean getNeedResponse() {
      return needResponse_;
    }

    public static final int SENDER_ID_FIELD_NUMBER = 5;
    private volatile Object senderId_;
    /**
     * <pre>
     *发送者ID
     * </pre>
     *
     * <code>string sender_id = 5;</code>
     */
    public String getSenderId() {
      Object ref = senderId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        senderId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *发送者ID
     * </pre>
     *
     * <code>string sender_id = 5;</code>
     */
    public com.google.protobuf.ByteString
        getSenderIdBytes() {
      Object ref = senderId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        senderId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getMessageIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, messageId_);
      }
      if (sendTime_ != 0D) {
        output.writeDouble(2, sendTime_);
      }
      if (expiredTime_ != 0D) {
        output.writeDouble(3, expiredTime_);
      }
      if (needResponse_ != false) {
        output.writeBool(4, needResponse_);
      }
      if (!getSenderIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, senderId_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getMessageIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, messageId_);
      }
      if (sendTime_ != 0D) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(2, sendTime_);
      }
      if (expiredTime_ != 0D) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(3, expiredTime_);
      }
      if (needResponse_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(4, needResponse_);
      }
      if (!getSenderIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, senderId_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof MatchMessage)) {
        return super.equals(obj);
      }
      MatchMessage other = (MatchMessage) obj;

      boolean result = true;
      result = result && getMessageId()
          .equals(other.getMessageId());
      result = result && (
          Double.doubleToLongBits(getSendTime())
          == Double.doubleToLongBits(
              other.getSendTime()));
      result = result && (
          Double.doubleToLongBits(getExpiredTime())
          == Double.doubleToLongBits(
              other.getExpiredTime()));
      result = result && (getNeedResponse()
          == other.getNeedResponse());
      result = result && getSenderId()
          .equals(other.getSenderId());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + MESSAGE_ID_FIELD_NUMBER;
      hash = (53 * hash) + getMessageId().hashCode();
      hash = (37 * hash) + SEND_TIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          Double.doubleToLongBits(getSendTime()));
      hash = (37 * hash) + EXPIRED_TIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          Double.doubleToLongBits(getExpiredTime()));
      hash = (37 * hash) + NEEDRESPONSE_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getNeedResponse());
      hash = (37 * hash) + SENDER_ID_FIELD_NUMBER;
      hash = (53 * hash) + getSenderId().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static MatchMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MatchMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MatchMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static MatchMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static MatchMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MatchMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static MatchMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static MatchMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static MatchMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static MatchMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(MatchMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     *匹配消息
     * </pre>
     *
     * Protobuf type {@code MatchMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:MatchMessage)
        MatchMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return MatchMessageOuterClass.internal_static_MatchMessage_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return MatchMessageOuterClass.internal_static_MatchMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                MatchMessage.class, Builder.class);
      }

      // Construct using com.artisankid.elementwar.ewmodel.MatchMessageOuterClass.MatchMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        messageId_ = "";

        sendTime_ = 0D;

        expiredTime_ = 0D;

        needResponse_ = false;

        senderId_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MatchMessageOuterClass.internal_static_MatchMessage_descriptor;
      }

      public MatchMessage getDefaultInstanceForType() {
        return MatchMessage.getDefaultInstance();
      }

      public MatchMessage build() {
        MatchMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public MatchMessage buildPartial() {
        MatchMessage result = new MatchMessage(this);
        result.messageId_ = messageId_;
        result.sendTime_ = sendTime_;
        result.expiredTime_ = expiredTime_;
        result.needResponse_ = needResponse_;
        result.senderId_ = senderId_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MatchMessage) {
          return mergeFrom((MatchMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(MatchMessage other) {
        if (other == MatchMessage.getDefaultInstance()) return this;
        if (!other.getMessageId().isEmpty()) {
          messageId_ = other.messageId_;
          onChanged();
        }
        if (other.getSendTime() != 0D) {
          setSendTime(other.getSendTime());
        }
        if (other.getExpiredTime() != 0D) {
          setExpiredTime(other.getExpiredTime());
        }
        if (other.getNeedResponse() != false) {
          setNeedResponse(other.getNeedResponse());
        }
        if (!other.getSenderId().isEmpty()) {
          senderId_ = other.senderId_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        MatchMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (MatchMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object messageId_ = "";
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public String getMessageId() {
        Object ref = messageId_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          messageId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public com.google.protobuf.ByteString
          getMessageIdBytes() {
        Object ref = messageId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          messageId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public Builder setMessageId(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        messageId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public Builder clearMessageId() {
        
        messageId_ = getDefaultInstance().getMessageId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public Builder setMessageIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        messageId_ = value;
        onChanged();
        return this;
      }

      private double sendTime_ ;
      /**
       * <pre>
       *发送时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double send_time = 2;</code>
       */
      public double getSendTime() {
        return sendTime_;
      }
      /**
       * <pre>
       *发送时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double send_time = 2;</code>
       */
      public Builder setSendTime(double value) {
        
        sendTime_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double send_time = 2;</code>
       */
      public Builder clearSendTime() {
        
        sendTime_ = 0D;
        onChanged();
        return this;
      }

      private double expiredTime_ ;
      /**
       * <pre>
       *过期时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double expired_time = 3;</code>
       */
      public double getExpiredTime() {
        return expiredTime_;
      }
      /**
       * <pre>
       *过期时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double expired_time = 3;</code>
       */
      public Builder setExpiredTime(double value) {
        
        expiredTime_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *过期时间戳，因为时间戳处在1482934107这种量级，超过2^28，需要fixed32这种高效率编码类型
       * </pre>
       *
       * <code>double expired_time = 3;</code>
       */
      public Builder clearExpiredTime() {
        
        expiredTime_ = 0D;
        onChanged();
        return this;
      }

      private boolean needResponse_ ;
      /**
       * <pre>
       *是否需要响应
       * </pre>
       *
       * <code>bool needResponse = 4;</code>
       */
      public boolean getNeedResponse() {
        return needResponse_;
      }
      /**
       * <pre>
       *是否需要响应
       * </pre>
       *
       * <code>bool needResponse = 4;</code>
       */
      public Builder setNeedResponse(boolean value) {
        
        needResponse_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *是否需要响应
       * </pre>
       *
       * <code>bool needResponse = 4;</code>
       */
      public Builder clearNeedResponse() {
        
        needResponse_ = false;
        onChanged();
        return this;
      }

      private Object senderId_ = "";
      /**
       * <pre>
       *发送者ID
       * </pre>
       *
       * <code>string sender_id = 5;</code>
       */
      public String getSenderId() {
        Object ref = senderId_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          senderId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *发送者ID
       * </pre>
       *
       * <code>string sender_id = 5;</code>
       */
      public com.google.protobuf.ByteString
          getSenderIdBytes() {
        Object ref = senderId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          senderId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *发送者ID
       * </pre>
       *
       * <code>string sender_id = 5;</code>
       */
      public Builder setSenderId(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        senderId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送者ID
       * </pre>
       *
       * <code>string sender_id = 5;</code>
       */
      public Builder clearSenderId() {
        
        senderId_ = getDefaultInstance().getSenderId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发送者ID
       * </pre>
       *
       * <code>string sender_id = 5;</code>
       */
      public Builder setSenderIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        senderId_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:MatchMessage)
    }

    // @@protoc_insertion_point(class_scope:MatchMessage)
    private static final MatchMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new MatchMessage();
    }

    public static MatchMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<MatchMessage>
        PARSER = new com.google.protobuf.AbstractParser<MatchMessage>() {
      public MatchMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new MatchMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<MatchMessage> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<MatchMessage> getParserForType() {
      return PARSER;
    }

    public MatchMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MatchMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MatchMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\022MatchMessage.proto\"t\n\014MatchMessage\022\022\n\n" +
      "message_id\030\001 \001(\t\022\021\n\tsend_time\030\002 \001(\001\022\024\n\014e" +
      "xpired_time\030\003 \001(\001\022\024\n\014needResponse\030\004 \001(\010\022" +
      "\021\n\tsender_id\030\005 \001(\tB\030\n\026com.artisankid.ewm" +
      "odelb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_MatchMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_MatchMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MatchMessage_descriptor,
        new String[] { "MessageId", "SendTime", "ExpiredTime", "NeedResponse", "SenderId", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
