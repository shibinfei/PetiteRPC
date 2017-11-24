package io.hahahahaha.petiterpc.registry;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.common.base.Objects;

/**
 * 注册服务抽象.
 * @author shibinfei
 */
public class Registration {

	private String host;
	
	private int port;
	
	private String interfaceName;
	
	public Registration(String host, int port, String interfaceName) {
		super();
		this.host = host;
		this.port = port;
		this.interfaceName = interfaceName;
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

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public String toString() {
		return "Registration [host=" + host + ", port=" + port + ", serviceName=" + interfaceName + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(host, port, interfaceName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Registration)) {
			return false;
		}
		
		Registration other = (Registration)obj;
		return new EqualsBuilder().append(host, other.host)
				.append(port, other.host).append(interfaceName, other.interfaceName).isEquals();
	}
	
}