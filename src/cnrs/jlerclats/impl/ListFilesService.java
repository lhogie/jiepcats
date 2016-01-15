package cnrs.jlerclats.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import toools.text.TextUtilities;
import cnrs.jlerclats.Access;
import cnrs.jlerclats.CommandLineApplicationInput;
import cnrs.jlerclats.CommandLineApplicationOutput;
import cnrs.jlerclats.Service;
import cnrs.jlerclats.ServiceException;

public class ListFilesService extends Service
{
	public ListFilesService(Access sshConfig)
	{
		super(sshConfig, "ls");
	}

	public List<String> ls(String... pattern) throws ServiceException, IOException
	{
		CommandLineApplicationInput r = new CommandLineApplicationInput();

		for (String p : pattern)
		{
			r.cmdLineParameters.add(p);
		}

		CommandLineApplicationOutput out = doit(r);
		return TextUtilities.splitInLines(new String(out.stdout));
	}

	public static void main(String[] args) throws ServiceException, IOException
	{
		Access sshConfig = new Access(InetAddress.getByName("musclotte"));

		System.out.println(new ListFilesService(sshConfig).ls());
	}
}
