package tp;

import java.io.IOException;
import java.nio.file.Path;

interface MyAction {
	
void perform(Path p) throws IOException;

}