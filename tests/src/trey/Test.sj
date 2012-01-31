
import sj.runtime.*;
import sj.runtime.net.*;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;

public class Test{


	 public void notify(int result) throws HeadlessException{
		 
		 if(result == 3)
			 
			 JOptionPane.showMessageDialog(null, "YOU WON!!!", "Server Result", JOptionPane.INFORMATION_MESSAGE);
		 else
			 JOptionPane.showMessageDialog(null, "YOU LOST!!!", "Server Result", JOptionPane.INFORMATION_MESSAGE);
	 }
}