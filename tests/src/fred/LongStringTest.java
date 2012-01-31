import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;


public class LongStringTest {

	static final byte READ = 1;
	
	public LongStringTest(){
		try {
			File bytes = new File("/Users/fred/bytes");
			
			FileOutputStream fos = new FileOutputStream(bytes);
//			BufferedOutputStream bos = new BufferedOutputStream(fos, 8);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			
			String tS = "Loremipsumdolorsitamet,consectetueradipiscingelitnPraesentbiconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbbendumaliquamjustonNamadipiscingU\ntrhoncusluctusorcinNamlobortismollisligulanAliquamurnaodio,interdumet,tempusvitae,pretiumsitamet,auguenCrasduinDonecpellentesque,anteinpretiumpretium,eliterosconvallisorci,sedpellentesqueligulanisifringillanuncnIntristiquedapibusnislnIntegerquismetusegettortorvenenatisimperdietnProininterdumrutrumnislnEtiamleoarcu,auctorquis,dapibusnon,sodalesvel,urnanMaecenasaenimquispedebibendumtristiquenAeneanesttortor,posuerein,iaculissed,interdumnon,semnProinpedenAeneanvulputategravidaodionCrasmalesuadaaccumsansapiennDuisquismagnaacdolorfringillafermentumnCrasipsumest,ornarepharetra,auctorac,auctornec,auguenSuspendissefermentumipsumegettortornDuisinorcinNullamametussedodiotinciduntplaceratnCrasnonjuston\nVestibulucursusnInvenenatisenimvitaeliberonInaliberoamauriscursussollicitudinnAliquamvnNOTDONEtheresmorehere";
			String tSs = tS+"1\n"+tS+"2\n"+tS+"3\n"+tS+"4\n"+tS+"5\n"+tS+"6\n"+tS+"7\n"+tS+"8\n";
			String tSss = tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs + tSs;
			String mts = tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + tSss + "DONEDONE";
			
			byte[] stringBytes = toByteArray(mts);			

			new Reader(bytes).start();
			
			System.out.println(stringBytes.length);
			
			fos.write(stringBytes);
			fos.flush();
			

			
		} catch (OptionalDataException ode){
			System.out.println(ode.eof + " : " + ode.length);
		}	catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}	
	
	public byte[] toByteArray(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}
	
	public Object fromByteArray(final byte[] bytes) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(bis);
		return in.readObject();     
	}
	
	public static void main(String[] args){
		new LongStringTest();
	}
	
	private class Reader extends Thread{
		
		private File bytes;

		public Reader(File bytes){
			this.bytes = bytes;
		}
		
		public void run(){
			try {
//				FileInputStream fis = new FileInputStream(bytes);				
//				BufferedInputStream bis = new BufferedInputStream(fis, 4096);
//				ObjectInputStream ois = new ObjectInputStream(bis);
//				
//				while(fis.available() < 1){}
//				
//				byte[] bs = (byte[]) ois.readObject();
//				Object o = fromByteArray(bs);
//
//				System.out.println("String: " + (String) o);
//				System.out.println(bs.length);
//				System.out.println(ois.markSupported());
				
				FileInputStream fis = new FileInputStream(bytes);				
				BufferedInputStream bis = new BufferedInputStream(fis);
				//ObjectInputStream ois = new ObjectInputStream(bis);
				
				while(bis.available() < 1){}
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//while(bis.available() < 1){}
				int c;
				while((c = bis.read()) != -1){
					baos.write(c);
				}

				Object o = fromByteArray(baos.toByteArray());
				
				System.out.println("Read: " + o);
				System.out.println(bis.markSupported());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
