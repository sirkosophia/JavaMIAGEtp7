package tp;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrefixFilter implements DirectoryStream.Filter<Path>{
	long n;
	
	public PrefixFilter (long n) {
		this.n = n;
	}

	@Override
	public boolean accept(Path entry) throws IOException {
		return (Files.size(entry) > n);
	}
	


}
