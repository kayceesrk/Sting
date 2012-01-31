package aplas.bmarks2.micro;

import java.io.*;

public class ServerMessage implements Serializable {	
	private boolean isKill;

	private byte[] payload;

	private int size;
	private int number;

	public ServerMessage(int size, int number, boolean isKill) {
		this.size = size;    
		this.payload = new byte[size];
		this.number = number;
		this.isKill = isKill;
	}

	public byte[] getPayload() {
		return payload;
	}


	public boolean isKill() {
		return isKill;
	}

	public String toString() {
		return "ServerMessage[size=" + size + ",isKill=" + isKill + "]";
	}

	public int getNum() {
		return number;
	}
}
