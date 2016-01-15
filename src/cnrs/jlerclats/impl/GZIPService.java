package cnrs.jlerclats.impl;

import java.io.IOException;
import java.net.InetAddress;

import toools.io.file.RegularFile;
import cnrs.jlerclats.Access;
import cnrs.jlerclats.CommandLineApplicationInput;
import cnrs.jlerclats.CommandLineApplicationOutput;
import cnrs.jlerclats.FileProcessingService;
import cnrs.jlerclats.ServiceException;

public class GZIPService extends FileProcessingService
{
	public GZIPService(Access sshConfig)
	{
		super(sshConfig, "gzip");
	}

	public RegularFile gzip(RegularFile f) throws ServiceException, IOException
	{
		CommandLineApplicationInput in = new CommandLineApplicationInput();
		in.cmdLineParameters.add(f);
		CommandLineApplicationOutput out = doit(in);
		return retrieveFile(f.getName() + ".gz");
	}


	public static void main(String[] args) throws ServiceException, IOException
	{
		RegularFile f = new RegularFile("$HOME/job/i3s/joao/wav2mp3/HerMajesty.wav");

		Access sshConfig = new Access(InetAddress.getByName("musclotte"));
		RegularFile zipFile = new GZIPService(sshConfig).gzip(f);
		System.out.println(zipFile);
	}

}
