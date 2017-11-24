package io.hahahahaha.petiterpc.transport;

/**
 * @author shibinfei
 *
 */
public interface CodecProtocol {

    short MAGIC = (short) 0xffcc;

    int HEADER_LENGTH = 7;

}
