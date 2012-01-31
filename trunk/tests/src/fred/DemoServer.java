import sj.runtime.*;
import sj.runtime.net.*;

class DemoServer {
	
	public DemoServer(int port) throws Exception
	{	
		SJAbstractServerSocket ss = null;
		
		try{
			
			SJProtocol p = new SJProtocol("H4sIAAAAAAAAAIWQMUsDMRTH31UrFUFEKAhWnKSDctlcOlUQRAMWq/MR03Dm" +
				       "SJN4ieUKxVk/iYu7\no7ujX0EHF7+Bg7mkVSnCZQjk5f3/v/d/j59QNznsmS" +
				       "y2Y82Mv9Vlxqg1sWHGhGr/+IClXJ67R0Ih\nnKgGUOTQrpTOq2oY6vSKi4GF" +
				       "NjYZ8n3ojxr9qFFQdzAsaZIzaa/hFmqO2tRKjFOh7JQdGN2dpwv8\n8bwdJt" +
				       "v8p+fUExL6td/aeu2+Ly9AhKGhleGWK2lhA89E6MZygXrTn06hdeEWFVemPW" +
				       "OU8RGbD+0w\nK0PXRlL/ZaH5SwpRfdCiDBh51G4lqs/koIJT2jVmplo729bc" +
				       "VnqCUHakxIDlCX14e5kcntzfeZ9F\nSYZu0HWckRFBgsgU9W3OZeqWYWGtrM" +
				       "ZlNQ5VXVJW9TfbYV6VUgIAAA==");
			
			System.out.println("Loading Server Socket");
			ss = SJNewServerSocket.create(p, port, SJParameters.test());
			
			while(true){
				SJAbstractSocket s = ss.accept();
				
				String received = (String) s.receive();
				
				while(!received.equalsIgnoreCase("quit")){
					System.out.println("Recieved: " + received);
					s.send("Message received");
					received = (String) s.receive();
				}
				
				System.out.println("Client disconnecting");
				s.send("Quit acknowledged");
				
				s.close();
				
			}
			
		}finally{
			if (ss != null) ss.close();
		}		
	}
	
	public static void main(String[] args) throws Exception 
	{
		new DemoServer(Integer.parseInt(args[0]));
	}	
}

