package cnrs.jlerclats;

import java.util.List;

import toools.io.file.RegularFile;

public class SilentSSHListener implements SSHListener
{

	@Override
	public void connectingUsing(Access sshConfig)
	{
	}

	@Override
	public void running(String cmdLine, List cmdLineParameters)
	{
	}

	@Override
	public void disconnected()
	{
	}

	@Override
	public void disconnecting()
	{
	}

	@Override
	public void uploading(String path)
	{
	}

	@Override
	public void uploaded(String path)
	{
	}

	@Override
	public void downloading(String name, RegularFile outfile)
	{
	}

	@Override
	public void downloaded(String name, RegularFile outfile)
	{
	}

	@Override
	public void removing(String name)
	{
	}

	@Override
	public void removed(String name)
	{
	}

}
