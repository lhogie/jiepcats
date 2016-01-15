package cnrs.jlerclats;

import java.util.List;

import toools.io.file.RegularFile;
import toools.text.TextUtilities;

public class DefaultSSHListener implements SSHListener
{
	public String prefix = "# ";
	
	@Override
	public void connectingUsing(Access sshConfig)
	{
		System.out.println(prefix + "connecting using " + sshConfig);
	}

	@Override
	public void running(String cmdLine, List cmdLineParameters)
	{
		System.out.println(prefix + "running: " + cmdLine + " "
				+ TextUtilities.concatene(cmdLineParameters, " "));

	}

	@Override
	public void disconnected()
	{
		System.out.println(prefix + "disconnected");

	}

	@Override
	public void disconnecting()
	{
		System.out.println(prefix + "disconnecting...");
	}

	@Override
	public void uploading(String path)
	{
		System.out.println(prefix + "uploading " + path);
	}

	@Override
	public void uploaded(String path)
	{
		System.out.println(prefix + "uploaded complete! " + path);
	}

	@Override
	public void downloading(String name, RegularFile outfile)
	{
		System.out.println(prefix + "downloading " + name + " to " + outfile.getPath());
	}

	@Override
	public void downloaded(String name, RegularFile outfile)
	{
		System.out.println(prefix + "file " + name + " downloaded to " + outfile.getPath());
	}

	@Override
	public void removing(String name)
	{
		System.out.println(prefix + "removing " + name);

	}

	@Override
	public void removed(String name)
	{
		System.out.println(prefix + name + " removed");
	}

}
