package sessionj.runtime.transport;

public abstract class SJAcceptorThread extends Thread {
    protected final SJConnectionAcceptor ca;
    protected final SJAcceptorThreadGroup atg;
    protected boolean run = true;

    protected SJAcceptorThread(SJAcceptorThreadGroup atg, String s, SJConnectionAcceptor ca) {
        super(atg, s);
        this.ca = ca;
        this.atg = atg;
        setDaemon(true);
    }

    private boolean hasTransportName(String transportName) {
        return ca.getTransportName().equals(transportName);
    }

    public SJConnectionAcceptor getAcceptorFor(String transportName) {
        if (hasTransportName(transportName))
            return ca;
        else return null;
    }

    public void close()
    {		
        run = false;
        
        ca.close();
    
        if (ca.interruptToClose()) {
            interrupt();
            //throw new RuntimeException("..."); // Maybe this would be better, with an appropriate exception catcher?
        }
    }

	public SJTransport getTransport() {
		return ca.getTransport();
	}
}
