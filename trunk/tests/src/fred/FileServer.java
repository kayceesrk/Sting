import sj.runtime.SJProtocol;
import sj.runtime.net.SJNewServerSocket;
import sj.runtime.net.SJNewSocket;
import sj.runtime.net.transport.SJOldFileTransport;
import sj.runtime.net.transport.SJServerTransport;
import sj.runtime.net.transport.SJServerTransportList;


public class FileServer {

	public FileServer(int port) throws Exception{
		
		String tS = "Loremipsumdolorsitamet,consectetueradipiscingelitnPraesentbiconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbbendumaliquamjustonNamadipiscingU\ntrhoncusluctusorcinNamlobortismollisligulanAliquamurnaodio,interdumet,tempusvitae,pretiumsitamet,auguenCrasduinDonecpellentesque,anteinpretiumpretium,eliterosconvallisorci,sedpellentesqueligulanisifringillanuncnIntristiquedapibusnislnIntegerquismetusegettortorvenenatisimperdietnProininterdumrutrumnislnEtiamleoarcu,auctorquis,dapibusnon,sodalesvel,urnanMaecenasaenimquispedebibendumtristiquenAeneanesttortor,posuerein,iaculissed,interdumnon,semnProinpedenAeneanvulputategravidaodionCrasmalesuadaaccumsansapiennDuisquismagnaacdolorfringillafermentumnCrasipsumest,ornarepharetra,auctorac,auctornec,auguenSuspendissefermentumipsumegettortornDuisinorcinNullamametussedodiotinciduntplaceratnCrasnonjuston\nVestibulucursusnInvenenatisenimvitaeliberonInaliberoamauriscursussollicitudinnAliquamvnNOTDONEtheresmorehere";
		String tSs = tS+"1\n"+tS+"2\n"+tS+"3\n"+tS+"4\n"+tS+"5\n"+tS+"6\n"+tS+"7\n"+tS+"8\n";
		String tSss = tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs;
		String mts = tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + "DONEDONE";
		
		String mmts = "";
		for(int i = 0; i < 5; i++){
			mmts += mts;
		}
		
		mmts += "DONEDONEDONE";
		
		SJNewServerSocket ss = null;
		try
		{	

			SJProtocol p = new SJProtocol("begin.!<int>.?(int)");
			SJServerTransport t = SJOldFileTransport.createServerTransport(port);
			SJServerTransportList ts = new SJServerTransportList(t);
			
			System.out.println("Loading Server Socket");
			ss = null; //SJNewServerSocket.create(p, ts);
			
			while(true)
			{
				SJNewSocket s = null;
				
//				try (s)
//				{
					s = ss.accept();
		
					s.sendInt(1234);
					
					System.out.println(s.receiveInt());
					
					s.send(mmts);
					
					System.out.println(s.receive());
					
					s.close();
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new FileServer(Integer.parseInt(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
