import java.io.BufferedReader;
import java.io.InputStreamReader;

import sj.runtime.SJProtocol;
import sj.runtime.SJServerAddress;
import sj.runtime.net.SJParameters;
import sj.runtime.net.SJNewSocket;


public class FClient {
		
		public FClient(String server, int port) throws Exception
		{	
			String tS = "Loremipsumdolorsitamet,consectetueradipiscingelitnPraesentbiconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbbendumaliquamjustonNamadipiscingU\ntrhoncusluctusorcinNamlobortismollisligulanAliquamurnaodio,interdumet,tempusvitae,pretiumsitamet,auguenCrasduinDonecpellentesque,anteinpretiumpretium,eliterosconvallisorci,sedpellentesqueligulanisifringillanuncnIntristiquedapibusnislnIntegerquismetusegettortorvenenatisimperdietnProininterdumrutrumnislnEtiamleoarcu,auctorquis,dapibusnon,sodalesvel,urnanMaecenasaenimquispedebibendumtristiquenAeneanesttortor,posuerein,iaculissed,interdumnon,semnProinpedenAeneanvulputategravidaodionCrasmalesuadaaccumsansapiennDuisquismagnaacdolorfringillafermentumnCrasipsumest,ornarepharetra,auctorac,auctornec,auguenSuspendissefermentumipsumegettortornDuisinorcinNullamametussedodiotinciduntplaceratnCrasnonjuston\nVestibulucursusnInvenenatisenimvitaeliberonInaliberoamauriscursussollicitudinnAliquamvnNOTDONEtheresmorehere";
			String tSs = tS+"1\n"+tS+"2\n"+tS+"3\n"+tS+"4\n"+tS+"5\n"+tS+"6\n"+tS+"7\n"+tS+"8\n";
			String tSss = tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs;
			String mts = tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + "DONEDONE";
			
			
			SJProtocol p = new SJProtocol("H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
				       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
				       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
				       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
				       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
				       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
				       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
				       "U1KKuBq6qSsqq+AekRcXhSAgAA");
			
			SJServerAddress c = SJServerAddress.create(p, server, port);
			SJNewSocket s = SJNewSocket.create(c);
//			try(s)
//			{	

				s.request();
				System.out.println("Connected...");
				System.out.println(s.receiveInt());
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input = "";
				while(input.equals("")){
					System.out.print("Anything to continue:");
					input = br.readLine();
				}
				s.sendInt(56789);
				
				System.out.println(s.receive());
				s.send(mts);
				System.out.println("Done!");
//			}
//			finally
//			{
//				
//			}		
		}
		
		public static void main(String[] args) throws Exception
		{
			new FClient(args[0], Integer.parseInt(args[1]));
		}	
}
