package io.hahahahaha.petiterpc.transport.netty;

import java.util.List;

import com.google.common.base.Preconditions;

import io.hahahahaha.petiterpc.common.Codable;
import io.hahahahaha.petiterpc.common.Heartbeat;
import io.hahahahaha.petiterpc.common.Request;
import io.hahahahaha.petiterpc.common.Response;
import io.hahahahaha.petiterpc.serialization.Serializer;
import io.hahahahaha.petiterpc.transport.CodecProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * @author shibinfei
 * @see io.netty.handler.codec.ReplayingDecoder
 */
public class Decoder extends ByteToMessageDecoder {

    private static final int HEADER_LENGTH = 7;

    private State state = State.HEADER;

    private Codable.Type type;

    private int bodySize;

    private Serializer serializer;

    public Decoder(Serializer serializer) {
        super();
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (state == State.HEADER) {
            if (in.readableBytes() < HEADER_LENGTH) {
                return;
            }

            Preconditions.checkArgument(in.readShort() == CodecProtocol.MAGIC, "Illegal Message");
            
            this.type = Codable.Type.of((int) in.readByte());
            this.bodySize = in.readInt();

            state = State.BODY;
        } else if (state == State.BODY) {
            if (in.readableBytes() < bodySize) {
                return;
            }

            byte[] bodyBytes = new byte[bodySize];
            in.readBytes(bodyBytes);

            Codable result = null;
            if (type == Codable.Type.REQUEST) {
                result = serializer.deserialize(bodyBytes, Request.class);
            } else if (type == Codable.Type.RESPONSE) {
                result = serializer.deserialize(bodyBytes, Response.class);
            } else if (type == Codable.Type.HEARTBEAT) {
                result = serializer.deserialize(bodyBytes, Heartbeat.class);
            }

            out.add(result);
            state = State.HEADER;
        }

    }

    private enum State {
        HEADER, BODY;
    }

}
