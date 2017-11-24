package io.hahahahaha.petiterpc.common;

import java.io.Serializable;

/**
 * @author shibinfei
 *
 */
public interface Codable extends Serializable {

    Type getType();
    
    enum Type {
        REQUEST, RESPONSE;
        
        public static Type of(int ordinal) {
            return values()[ordinal];
        }
        
    }
    
}
