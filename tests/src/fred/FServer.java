import sj.runtime.SJProtocol;
import sj.runtime.net.SJParameters;
import sj.runtime.net.SJNewServerSocket;
import sj.runtime.net.SJNewSocket;

class FServer {
	
//	private protocol p
//	{
//		begin.!<int>.?(int)
//	}
	
	public FServer(int port) throws Exception
	{	
		
		SJNewServerSocket ss = null;
		try
		{	

//			for(Map.Entry<String, String> e : System.getenv().entrySet()){
//				System.out.println(e.getKey() + " : " + e.getValue());
//			}
			
			SJProtocol p = new SJProtocol("H4sIAAAAAAAAAIWQMUsDMRTH31UrFUFEKAhWnKSDctlcOlUQRAMWq/MR03Dm" +
				       "SJN4ieUKxVk/iYu7\no7ujX0EHF7+Bg7mkVSnCZQjk5f3/v/d/j59QNznsmS" +
				       "y2Y82Mv9Vlxqg1sWHGhGr/+IClXJ67R0Ih\nnKgGUOTQrpTOq2oY6vSKi4GF" +
				       "NjYZ8n3ojxr9qFFQdzAsaZIzaa/hFmqO2tRKjFOh7JQdGN2dpwv8\n8bwdJt" +
				       "v8p+fUExL6td/aeu2+Ly9AhKGhleGWK2lhA89E6MZygXrTn06hdeEWFVemPW" +
				       "OU8RGbD+0w\nK0PXRlL/ZaH5SwpRfdCiDBh51G4lqs/koIJT2jVmplo729bc" +
				       "VnqCUHakxIDlCX14e5kcntzfeZ9F\nSYZu0HWckRFBgsgU9W3OZeqWYWGtrM" +
				       "ZlNQ5VXVJW9TfbYV6VUgIAAA==");
			
			System.out.println("Loading Server Socket");
			ss = SJNewServerSocket.create(p, port);
			
			while(true)
			{
				SJNewSocket s = null;
				
//				try (s)
//				{
					s = ss.accept();
					
					s.sendInt(1234);
					
					System.out.println(s.receiveInt());
					
					s.send("asdf");
					
					System.out.println(s.receive());
//				}
//				finally
//				{
//					
//				}
			}
			
		} 
		finally 
		{
			if (ss != null) ss.close();
		}		
	}
	
	public static void main(String[] args) throws Exception 
	{
		new FServer(Integer.parseInt(args[0]));
	}
}
