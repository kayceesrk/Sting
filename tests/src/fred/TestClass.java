import java.io.Serializable;
import java.util.Date;


public class TestClass implements Serializable{

	private static final long serialVersionUID = -5826227360255696605L;
	private String payload;
	private Date now;

	public TestClass(String payload) {
		this.setPayload(payload);
		this.now = new Date();
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return payload;
	}
	
	public Date getNow() {
		return now;
	}

	public void setNow(Date now) {
		this.now = now;
	}

	public String toString(){
		return getPayload() + getNow();
	}

}
