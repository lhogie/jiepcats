package cnrs.jlerclats;

import java.util.List;

import toools.io.file.RegularFile;

public interface SSHListener
{

	void connectingUsing(Access sshConfig);

	void running(String cmdLine, List cmdLineParameters);

	void disconnected();

	void disconnecting();

	void uploading(String path);

	void uploaded(String path);

	void downloading(String name, RegularFile outfile);

	void downloaded(String name, RegularFile outfile);

	void removing(String name);

	void removed(String name);

}
