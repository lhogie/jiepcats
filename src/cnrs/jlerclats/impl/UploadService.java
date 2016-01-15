package cnrs.jlerclats.impl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;

import toools.io.file.RegularFile;
import cnrs.jlerclats.Access;
import cnrs.jlerclats.CommandLineApplicationInput;
import cnrs.jlerclats.Service;
import cnrs.jlerclats.ServiceException;

public class UploadService extends Service
{
	public UploadService(Access sshConfig)
	{
		super(sshConfig, "cat >");
	}

	public void upload(RegularFile f) throws ServiceException, IOException
	{
		upload(f.getContent(), f.getName());
	}

	public void upload(File f) throws ServiceException, IOException
	{
		upload(Files.readAllBytes(f.toPath()), f.getName());
	}

	public String upload(byte[] bytes) throws ServiceException, IOException
	{
		String filename = Math.abs(bytes.hashCode()) + ".dat";
		upload(bytes, filename);
		return filename;
	}

	public void upload(byte[] bytes, String remoteFileName) throws ServiceException,
			IOException
	{
		CommandLineApplicationInput sshInput = new CommandLineApplicationInput();
		sshInput.stdin = bytes;
		sshInput.cmdLineParameters.add(remoteFileName);
		doit(sshInput);
	}

	public static void main(String[] args) throws ServiceException, IOException
	{
		Access sshConfig = new Access(
				InetAddress.getByName("musclotte"));
		new UploadService(sshConfig).upload(new RegularFile("/etc/hosts"));
	}
}
