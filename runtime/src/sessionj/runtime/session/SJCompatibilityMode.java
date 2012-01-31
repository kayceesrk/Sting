package sessionj.runtime.session;

public enum SJCompatibilityMode
{
	SJ, 
	CUSTOM, 
	SECURE // This means use the SRP-authenticating transport (HTTPS) for secure delegation. 
	// FIXME: "bounded-buffers" should be a mode? 
}
