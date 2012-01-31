package sessionj.runtime.transport;

public abstract class AbstractWithTransport {
	private final SJTransport transport;

	protected AbstractWithTransport(SJTransport transport) {
		this.transport = transport;
	}

	public String getTransportName()
	{
		return transport.getTransportName();
	}

	public SJTransport getTransport() {
		return transport;
	}
}
