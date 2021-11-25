package tp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class main {

	public static void main(String[] args) throws Exception {
		String curDir = System.getProperty("user.dir");
		Path path = Paths.get(curDir);
		DirMonitor d = new DirMonitor(path);
		
		d.afficheFichiers();
		System.out.println(d.sizeOfFiles());
		d.mostRecent();

	}
	
	public void applyAction(String prefix, MyAction action) throws IOException{
		
	}

}
