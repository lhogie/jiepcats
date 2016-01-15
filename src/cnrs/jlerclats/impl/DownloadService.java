package cnrs.jlerclats.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import cnrs.jlerclats.Access;
import cnrs.jlerclats.CommandLineApplicationInput;
import cnrs.jlerclats.CommandLineApplicationOutput;
import cnrs.jlerclats.Service;
import cnrs.jlerclats.ServiceException;

public class DownloadService extends Service
{
	public DownloadService(Access sshConfig)
	{
		super(sshConfig, "cat ");
	}

	public byte[] download(String filename) throws ServiceException, IOException
	{
		CommandLineApplicationInput r = new CommandLineApplicationInput();
		r.cmdLineParameters.add(filename);
		CommandLineApplicationOutput out = doit(r);
		return out.stdout;
	}

	public void download(String filename, RegularFile outFile)
			throws ServiceException, IOException
	{
		outFile.setContent(download(filename));
	}

	public List<RegularFile> download(List<String> filenames, Directory targetDirectory)
			throws ServiceException, IOException
	{
		List<RegularFile> files = new ArrayList<RegularFile>();
		
		for (String filename : filenames)
		{
			RegularFile targetFile = targetDirectory.getChildRegularFile(filename);
			download(filename, targetFile);
			files.add(targetFile);
		}
		
		return files;
	}

	
	public static void main(String[] args) throws ServiceException, IOException
	{
		Access sshConfig = new Access(
				InetAddress.getByName("musclotte"));
		byte[] bytes = new DownloadService(sshConfig).download("a.txt");
		System.out.println(new String(bytes));
	}
}
