package io.hahahahaha.petiterpc.common;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.common.base.Objects;

/**
 * @author shibinfei
 *
 */
public class Address {

    private String host;

    private int port;

    public Address(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Address)) {
            return false;
        }

        Address other = (Address) obj;
        return new EqualsBuilder().append(host, other.host).append(port, other.port).isEquals();
    }
}
