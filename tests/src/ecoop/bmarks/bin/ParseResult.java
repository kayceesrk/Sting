import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ParseResult {


	public static final int SIZE = 6000;
	public static final int REPS = 110;

	public static void main(String args[]) {
  

		if (args.length < 2) {
			System.out.println("Usage: java Parse Results <result file> <repetitions>");
		}
  		String inFile = args[0];
		int reps = Integer.parseInt(args[1]);

		String head[] = new String[SIZE];
		long num[][] = new long[SIZE][reps];

		double avg[] = new double[SIZE];
		long min[] = new long[SIZE];
		long max[] = new long[SIZE];

		int count = -1, r = 0;
  

		File file = new File(inFile);
    	FileInputStream fis = null;
    	BufferedInputStream bis = null;
    	DataInputStream dis = null;

    	try {
      		fis = new FileInputStream(file);

      		// Here BufferedInputStream is added for fast reading.
      		bis = new BufferedInputStream(fis);
      		dis = new DataInputStream(bis);

      		// dis.available() returns 0 if the file does not have more lines.
      		while (dis.available() != 0) {
        		String line = dis.readLine();

				if (line.charAt(0) == 'B') {
					int ind = line.indexOf(",", 19);
					String s1 = line.substring(19,ind);

					ind += 9;
      				int ind2 = line.indexOf(",", ind);

					String s2 = line.substring(ind, ind2);

					ind2 += 10;

					ind = line.indexOf(",",ind2);

					String s3 = line.substring(ind2, ind);

					ind += 16;
					String s4 = line.substring(ind);

					head[++count] = "v:" + s1 + "c:" + s2 + "ms:" + s3 + "sl:" + s4;

      				if (count != 0)
						avg[count - 1] = avg[count - 1] / reps;
      				r = 0;
      				avg[count] = max[count] = min[count] = 0;
    			}
    			else if (line.charAt(0) == '[') {

					String s1 = line.substring(32,line.indexOf(" ", 32));
     
					num[count][r] = Long.parseLong(s1);

					if (num[count][r] > max[count])
						max[count] = num[count][r];
     
					if (r == 0 || num[count][r] < min[count])
						min[count] = num[count][r];
      
					avg[count] += num[count][r];
					r++;
    			}
				
      		}

      		// dispose all the resources after using them.
      		fis.close();
      		bis.close();
      		dis.close();

    	} catch (FileNotFoundException e) {
      		e.printStackTrace();
    	} catch (IOException e) {
     		 e.printStackTrace();
    	}


		int i, j;
		for (i = 0; i < count; i++) {
			System.out.print(head[i] + "\t");
		}
 
		System.out.println();
  
		for (i = 0; i < reps; i++) {
			for (j = 0; j < count; j++)
				System.out.print(num[j][i] + "\t");
			System.out.println();
		}
  
		System.out.println();

		for (j = 0; j < count; j++)
			System.out.print(avg[j] + "\t");

		System.out.println();

  		for (j = 0; j < count; j++)
			System.out.print(max[j] + "\t");

		System.out.println();


		for (j = 0; j < count; j++)
			System.out.print(min[j] + "\t");

		System.out.println();
	}

}
