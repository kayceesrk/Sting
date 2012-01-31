import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;


public class TestFileWriter {
	public static final byte READ = 1;
	public static final byte DONE = 2;
	
	public TestFileWriter() {
		
		String testString = "Loremipsumdolorsitamet,consectetueradipiscingelitnPraesentbiconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbconsectetueradipiscingelitnPraesentbbendumaliquamjustonNamadipiscingU\ntrhoncusluctusorcinNamlobortismollisligulanAliquamurnaodio,interdumet,tempusvitae,pretiumsitamet,auguenCrasduinDonecpellentesque,anteinpretiumpretium,eliterosconvallisorci,sedpellentesqueligulanisifringillanuncnIntristiquedapibusnislnIntegerquismetusegettortorvenenatisimperdietnProininterdumrutrumnislnEtiamleoarcu,auctorquis,dapibusnon,sodalesvel,urnanMaecenasaenimquispedebibendumtristiquenAeneanesttortor,posuerein,iaculissed,interdumnon,semnProinpedenAeneanvulputategravidaodionCrasmalesuadaaccumsansapiennDuisquismagnaacdolorfringillafermentumnCrasipsumest,ornarepharetra,auctorac,auctornec,auguenSuspendissefermentumipsumegettortornDuisinorcinNullamametussedodiotinciduntplaceratnCrasnonjuston\nVestibulucursusnInvenenatisenimvitaeliberonInaliberoamauriscursussollicitudinnAliquamvnNOTDONEtheresmorehere";

		
		File cts  = new File("/Users/fred/cts");
		File ctssignals  = new File("/Users/fred/ctssignals");
		
		try {
		
				FileOutputStream fos = new FileOutputStream(cts); //for writing to the file.
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				FileOutputStream sos = new FileOutputStream(ctssignals);

				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String in = "";

				
				while(!in.equals("quit")){
					System.out.print("Send message: ");
					in = br.readLine();
					
					if(in.equals("go")){
						byte[] stringBytes = toByteArray(testString);
						System.err.println("Object is " + stringBytes.length + " bytes");
						
						
						
						oos.write(stringBytes);

						sos.write(bytesFromInt(stringBytes.length));
					}
				}
		
		} catch (FileNotFoundException ioe) {
			ioe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} /*catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}*/

	}
	
	private byte[] toByteArray(Object o) throws IOException {
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

	private int bytesToInt(byte[] b) {
		return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | (b[3]&0xff);
	}
	
	private byte[] bytesFromInt(int i) {
		return new byte[] { (byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i };
	}
	
	public static void main(String[] args){
		new TestFileWriter();
	}
	
	
}
