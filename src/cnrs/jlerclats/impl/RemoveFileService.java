package cnrs.jlerclats.impl;

import java.io.IOException;
import java.net.InetAddress;

import cnrs.jlerclats.Access;
import cnrs.jlerclats.CommandLineApplicationInput;
import cnrs.jlerclats.CommandLineApplicationOutput;
import cnrs.jlerclats.Service;
import cnrs.jlerclats.ServiceException;

public class RemoveFileService extends Service
{
	public RemoveFileService(Access sshConfig)
	{
		super(sshConfig, "rm -f ");
	}

	public void remove(String filename) throws ServiceException, IOException
	{
		CommandLineApplicationInput r = new CommandLineApplicationInput();
		r.cmdLineParameters.add(filename);
		CommandLineApplicationOutput out = doit(r);
	}

	public static void main(String[] args) throws ServiceException, IOException
	{
		Access sshConfig = new Access(InetAddress.getByName("musclotte"));
		new RemoveFileService(sshConfig).remove("a.txt");
	}
}
