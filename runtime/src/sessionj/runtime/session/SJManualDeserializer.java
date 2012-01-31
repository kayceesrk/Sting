package sessionj.runtime.session;

/**
 */
public class SJManualDeserializer implements SJDeserializer {
    public OngoingRead newOngoingRead() {
        return new OngoingReadImpl();
    }
}
