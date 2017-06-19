// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ErrorNotice.proto

package com.artisankid.elementwar.ewmessagemodel;

public final class ErrorNoticeOuterClass {
  private ErrorNoticeOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ErrorNoticeOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ErrorNotice)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    java.lang.String getMessageId();
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
     *错误码
     * </pre>
     *
     * <code>uint32 code = 4;</code>
     */
    int getCode();

    /**
     * <pre>
     *错误信息
     * </pre>
     *
     * <code>string message = 5;</code>
     */
    java.lang.String getMessage();
    /**
     * <pre>
     *错误信息
     * </pre>
     *
     * <code>string message = 5;</code>
     */
    com.google.protobuf.ByteString
        getMessageBytes();
  }
  /**
   * <pre>
   *错误通知
   * </pre>
   *
   * Protobuf type {@code ErrorNotice}
   */
  public  static final class ErrorNotice extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ErrorNotice)
      ErrorNoticeOrBuilder {
    // Use ErrorNotice.newBuilder() to construct.
    private ErrorNotice(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ErrorNotice() {
      messageId_ = "";
      sendTime_ = 0D;
      expiredTime_ = 0D;
      code_ = 0;
      message_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private ErrorNotice(
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
              java.lang.String s = input.readStringRequireUtf8();

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

              code_ = input.readUInt32();
              break;
            }
            case 42: {
              java.lang.String s = input.readStringRequireUtf8();

              message_ = s;
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
      return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.internal_static_ErrorNotice_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.internal_static_ErrorNotice_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.class, com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.Builder.class);
    }

    public static final int MESSAGE_ID_FIELD_NUMBER = 1;
    private volatile java.lang.Object messageId_;
    /**
     * <pre>
     *消息ID
     * </pre>
     *
     * <code>string message_id = 1;</code>
     */
    public java.lang.String getMessageId() {
      java.lang.Object ref = messageId_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
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
      java.lang.Object ref = messageId_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
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

    public static final int CODE_FIELD_NUMBER = 4;
    private int code_;
    /**
     * <pre>
     *错误码
     * </pre>
     *
     * <code>uint32 code = 4;</code>
     */
    public int getCode() {
      return code_;
    }

    public static final int MESSAGE_FIELD_NUMBER = 5;
    private volatile java.lang.Object message_;
    /**
     * <pre>
     *错误信息
     * </pre>
     *
     * <code>string message = 5;</code>
     */
    public java.lang.String getMessage() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        message_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *错误信息
     * </pre>
     *
     * <code>string message = 5;</code>
     */
    public com.google.protobuf.ByteString
        getMessageBytes() {
      java.lang.Object ref = message_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        message_ = b;
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
      if (code_ != 0) {
        output.writeUInt32(4, code_);
      }
      if (!getMessageBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, message_);
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
      if (code_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(4, code_);
      }
      if (!getMessageBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, message_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice)) {
        return super.equals(obj);
      }
      com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice other = (com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice) obj;

      boolean result = true;
      result = result && getMessageId()
          .equals(other.getMessageId());
      result = result && (
          java.lang.Double.doubleToLongBits(getSendTime())
          == java.lang.Double.doubleToLongBits(
              other.getSendTime()));
      result = result && (
          java.lang.Double.doubleToLongBits(getExpiredTime())
          == java.lang.Double.doubleToLongBits(
              other.getExpiredTime()));
      result = result && (getCode()
          == other.getCode());
      result = result && getMessage()
          .equals(other.getMessage());
      return result;
    }

    @java.lang.Override
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
          java.lang.Double.doubleToLongBits(getSendTime()));
      hash = (37 * hash) + EXPIRED_TIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          java.lang.Double.doubleToLongBits(getExpiredTime()));
      hash = (37 * hash) + CODE_FIELD_NUMBER;
      hash = (53 * hash) + getCode();
      hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
      hash = (53 * hash) + getMessage().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parseFrom(
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
    public static Builder newBuilder(com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     *错误通知
     * </pre>
     *
     * Protobuf type {@code ErrorNotice}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ErrorNotice)
        com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNoticeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.internal_static_ErrorNotice_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.internal_static_ErrorNotice_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.class, com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.Builder.class);
      }

      // Construct using com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
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

        code_ = 0;

        message_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.internal_static_ErrorNotice_descriptor;
      }

      public com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice getDefaultInstanceForType() {
        return com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.getDefaultInstance();
      }

      public com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice build() {
        com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice buildPartial() {
        com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice result = new com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice(this);
        result.messageId_ = messageId_;
        result.sendTime_ = sendTime_;
        result.expiredTime_ = expiredTime_;
        result.code_ = code_;
        result.message_ = message_;
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
        if (other instanceof com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice) {
          return mergeFrom((com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice other) {
        if (other == com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice.getDefaultInstance()) return this;
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
        if (other.getCode() != 0) {
          setCode(other.getCode());
        }
        if (!other.getMessage().isEmpty()) {
          message_ = other.message_;
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
        com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private java.lang.Object messageId_ = "";
      /**
       * <pre>
       *消息ID
       * </pre>
       *
       * <code>string message_id = 1;</code>
       */
      public java.lang.String getMessageId() {
        java.lang.Object ref = messageId_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          messageId_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
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
        java.lang.Object ref = messageId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
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
          java.lang.String value) {
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

      private int code_ ;
      /**
       * <pre>
       *错误码
       * </pre>
       *
       * <code>uint32 code = 4;</code>
       */
      public int getCode() {
        return code_;
      }
      /**
       * <pre>
       *错误码
       * </pre>
       *
       * <code>uint32 code = 4;</code>
       */
      public Builder setCode(int value) {
        
        code_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *错误码
       * </pre>
       *
       * <code>uint32 code = 4;</code>
       */
      public Builder clearCode() {
        
        code_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object message_ = "";
      /**
       * <pre>
       *错误信息
       * </pre>
       *
       * <code>string message = 5;</code>
       */
      public java.lang.String getMessage() {
        java.lang.Object ref = message_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          message_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *错误信息
       * </pre>
       *
       * <code>string message = 5;</code>
       */
      public com.google.protobuf.ByteString
          getMessageBytes() {
        java.lang.Object ref = message_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          message_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *错误信息
       * </pre>
       *
       * <code>string message = 5;</code>
       */
      public Builder setMessage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        message_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *错误信息
       * </pre>
       *
       * <code>string message = 5;</code>
       */
      public Builder clearMessage() {
        
        message_ = getDefaultInstance().getMessage();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *错误信息
       * </pre>
       *
       * <code>string message = 5;</code>
       */
      public Builder setMessageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        message_ = value;
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


      // @@protoc_insertion_point(builder_scope:ErrorNotice)
    }

    // @@protoc_insertion_point(class_scope:ErrorNotice)
    private static final com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice();
    }

    public static com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ErrorNotice>
        PARSER = new com.google.protobuf.AbstractParser<ErrorNotice>() {
      public ErrorNotice parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new ErrorNotice(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ErrorNotice> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<ErrorNotice> getParserForType() {
      return PARSER;
    }

    public com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass.ErrorNotice getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ErrorNotice_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ErrorNotice_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021ErrorNotice.proto\"i\n\013ErrorNotice\022\022\n\nme" +
      "ssage_id\030\001 \001(\t\022\021\n\tsend_time\030\002 \001(\001\022\024\n\014exp" +
      "ired_time\030\003 \001(\001\022\014\n\004code\030\004 \001(\r\022\017\n\007message" +
      "\030\005 \001(\tB*\n(com.artisankid.elementwar.ewme" +
      "ssagemodelb\006proto3"
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
    internal_static_ErrorNotice_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ErrorNotice_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ErrorNotice_descriptor,
        new java.lang.String[] { "MessageId", "SendTime", "ExpiredTime", "Code", "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
