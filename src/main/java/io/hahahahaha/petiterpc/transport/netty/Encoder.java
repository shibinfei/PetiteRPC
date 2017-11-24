package io.hahahahaha.petiterpc.transport.netty;

import io.hahahahaha.petiterpc.common.Codable;
import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.transport.CodecProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * magic -> byte: 类型 -> int 长度 -> 内容 
 * @author shibinfei
 *
 */
public class Encoder extends MessageToByteEncoder<Codable> {

    private Serializer serializer;
    
    public Encoder(Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Codable msg, ByteBuf out) throws Exception {
        out.writeShort(CodecProtocol.MAGIC);
        out.writeByte(msg.getType().ordinal());
        byte[] bytes = serializer.serialize(msg);
        int length = bytes.length;
        out.writeInt(length).writeBytes(bytes);
    }
    
}
