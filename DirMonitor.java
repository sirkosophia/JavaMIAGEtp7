package tp;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class DirMonitor {
	private Path p;
	private long res = 0;
	private Path pp;
	//PrefixFilter1 filter;
	
	public DirMonitor(Path p) throws Exception {
		String s = p.normalize().toString();
		File f = new File(s);
		if (f.exists() && f.isDirectory()) {
			this.p = p;
		}
		else {
			throw new IOException();
		}
	}
	
	/*
	public DirMonitor(Path p, PrefixFilter1 filter) throws Exception {
		this.filter = filter;
		String s = p.normalize().toString();
		File f = new File(s);
		if (f.exists() && f.isDirectory()) {
			this.p = p;
		}
		else {
			throw new IOException();
		}
	}
	*/
	
	public void afficheFichiers () throws IOException {
		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
			long n;

			@Override
			public boolean accept(Path entry) throws IOException {
				return (Files.size(entry) > n);
			}
			
		};
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(p, filter)) {
            Iterator<Path> iterator = ds.iterator();
            while (iterator.hasNext()) {
                Path p = iterator.next();
                System.out.println(p);
            }
        }
	}
	
	public void afficheFichiers2 (long n) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
            Iterator<Path> iterator = ds.iterator();
            while (iterator.hasNext()) {
                Path p = iterator.next();
                String s = p.normalize().toString();
                MyAction action = new MyAction() {
					@Override
					public void perform(Path p) throws IOException {
						if (Files.size(p) > n) {
							System.out.println(p);
						}
					}
                };
                applyAction(s, action);
            }
        }
	}
	
	public long sizeOfFiles2(long n) throws IOException {
		res = 0;
		String s = p.normalize().toString();
		MyAction action = new MyAction() {
			@Override
			public void perform(Path pp) throws IOException {
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(pp)) {
		            Iterator<Path> iterator = ds.iterator();
		            while (iterator.hasNext()) {
		                Path p = iterator.next();
		                if (Files.size(p) > n) {
			                String s = p.normalize().toString();
			        		File f = new File(s);
			                if (f.isFile()) {
			                	long bytes = Files.size(p);
			                	res += bytes;
			                }
		                }
		            }
		        }
			}
			
		};
		applyAction(s, action);
		return res;
	
	}
	
	public long sizeOfFiles() throws IOException {
		long res = 0;
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
            Iterator<Path> iterator = ds.iterator();
            while (iterator.hasNext()) {
                Path p = iterator.next();
                String s = p.normalize().toString();
        		File f = new File(s);
                if (f.isFile()) {
                	long bytes = Files.size(p);
                	res += bytes;
                }
            }
        }
		return res;
	
	}
	
	public Path mostRecent2(long n) throws ParseException, IOException {
		pp = null;
		String s = p.normalize().toString();
		MyAction action = new MyAction() {
			@Override
			public void perform(Path p) throws IOException {
				SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
		            Iterator<Path> iterator = ds.iterator();
		            	pp = iterator.next();
			            BasicFileAttributes attr = Files.readAttributes(pp, BasicFileAttributes.class);
			   			String s1 = sdf1.format(attr.lastModifiedTime().toMillis());
			   			Date date1 = sdf1.parse(s1);
			            while (iterator.hasNext()) {
			                Path ppp = iterator.next();
			                if (Files.size(ppp) > n) {
			                
			                try {
				       			attr = Files.readAttributes(ppp, BasicFileAttributes.class);
				       			String s2 = sdf2.format(attr.lastModifiedTime().toMillis());
				       			Date date2 = sdf2.parse(s2);
				       			int result = date1.compareTo(date2);
						        if (result < 0) {
						            date1 = date2;
						            pp = ppp;
						        }
			               }
			                catch(IOException e) {}
			                }
			            
			        }
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		};
		applyAction(s, action);
		if (Files.size(p) <= n) {
			pp = null;
		}
		System.out.println(pp);
		return pp;
	}
	
	public Path mostRecent() throws ParseException, IOException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Path pp;
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(p)) {
            Iterator<Path> iterator = ds.iterator();
            pp = iterator.next();
            BasicFileAttributes attr = Files.readAttributes(pp, BasicFileAttributes.class);
   			String s1 = sdf1.format(attr.lastModifiedTime().toMillis());
   			Date date1 = sdf1.parse(s1);
            while (iterator.hasNext()) {
                Path p = iterator.next();
                try {
	       			attr = Files.readAttributes(p, BasicFileAttributes.class);
	       			String s2 = sdf2.format(attr.lastModifiedTime().toMillis());
	       			Date date2 = sdf2.parse(s2);
	       			int result = date1.compareTo(date2);
			        if (result < 0) {
			            date1 = date2;
			            pp = p;
			        }
               }
                catch(IOException e) {}
            }
        }
		System.out.println(pp);
		return pp;
	}
	
	public static void applyAction(String prefix, MyAction action) throws IOException{
		Path path = Paths.get(prefix);
		action.perform(path);
	
	}

}

/*
class PrefixFilter1 implements DirectoryStream.Filter<Path>{
long n;
	
	public PrefixFilter1 (long n) {
		this.n = n;
	}

	@Override
	public boolean accept(Path entry) throws IOException {
		return (Files.size(entry) > n);
	}
	
}
*/
	
