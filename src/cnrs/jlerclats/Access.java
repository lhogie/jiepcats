package cnrs.jlerclats;


import java.net.InetAddress;

import toools.io.file.RegularFile;




public class Access
{
	private String userName = System.getProperty("user.name");
	private RegularFile privateKeyFile = new RegularFile("$HOME/.ssh/id_rsa");
	private RegularFile knownHostFile = new RegularFile("$HOME/.ssh/known_hosts");
	private InetAddress server;

	public Access(InetAddress server)
	{
		this.server = server;
	}
	
	public InetAddress getServer()
	{
		return server;
	}

	@Override
	public String toString()
	{
		return userName + "@" + server.getHostName() + " (private key: " + privateKeyFile.getPath() + ")";
	}

	public void setServer(InetAddress server)
	{
		if (server == null)
			throw new IllegalArgumentException("null server is not allowed");

		this.server = server;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String newName)
	{
		if (newName == null)
			throw new IllegalArgumentException("null username is not allowed");

		if (newName.isEmpty())
			throw new IllegalArgumentException("empty username");

		this.userName = newName;
	}

	public RegularFile getPrivateKeyFile()
	{
		return privateKeyFile;
	}

	public void setPrivateKeyFile(RegularFile privateKeyFile)
	{
		if (privateKeyFile == null)
			throw new IllegalArgumentException("null privateKeyFile");

		if (!privateKeyFile.exists())
			throw new IllegalArgumentException("privateKeyFile does not exist");

		this.privateKeyFile = privateKeyFile;
	}

	public RegularFile getKnownHostFile()
	{
		return knownHostFile;
	}

	public void setKnownHostFile(RegularFile knownHostFile)
	{
		if (knownHostFile == null)
			throw new IllegalArgumentException("null knownHostFile");

		if (!knownHostFile.exists())
			throw new IllegalArgumentException("knownHostFile does not exist");

		this.knownHostFile = knownHostFile;
	}

}
