package cnrs.jlerclats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import toools.io.Utilities;
import toools.text.TextUtilities;
import toools.thread.Threads;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Service
{
	private final String cmdLine;
	private final Access sshConfig;
	private SSHListener specificListener = null;
	private int nbCalls;
	public static SSHListener globalListener = new SilentSSHListener();
//	public static SSHListener globalListener = new DefaultSSHListener();

	public Access getSSHConfig()
	{
		return sshConfig;
	}

	public SSHListener getListener()
	{
		if (specificListener == null)
		{
			return globalListener;
		}
		else
		{
			return specificListener;
		}
	}

	public void setSpecificListener(SSHListener l)
	{
		this.specificListener = l;
	}

	public SSHListener getSpecificListener()
	{
		return specificListener;
	}

	public Service(Access sshConfig, String cmdLine)
	{
		this.cmdLine = cmdLine;
		this.sshConfig = sshConfig;
	}

	public String getName()
	{
		return cmdLine;
	}

	public int getNumberOfCalls()
	{
		return nbCalls;
	}

	protected synchronized CommandLineApplicationOutput doit(
			CommandLineApplicationInput input) throws ServiceException, IOException
	{
		++nbCalls;

		if (input == null)
			throw new IllegalArgumentException("null input");

		try
		{
			getListener().connectingUsing(sshConfig);
			Session session = Service.createSSHSession(sshConfig);

			getListener().running(cmdLine, input.cmdLineParameters);
			CommandLineApplicationOutput res = runShellCommand(session, cmdLine, input);

			getListener().disconnecting();
			session.disconnect();
			getListener().disconnected();
			return res;
		}
		catch (Throwable e)
		{
			throw new ServiceException(e);
		}
	}

	private static Session createSSHSession(Access sshConfig) throws JSchException
	{
		JSch jsch = new JSch();
		jsch.addIdentity(sshConfig.getPrivateKeyFile().getPath());
		jsch.setKnownHosts(sshConfig.getKnownHostFile().getPath());
		Session session = jsch.getSession(sshConfig.getUserName(), sshConfig.getServer()
				.getHostName(), 22);

		// bypass .ssh/known_hosts file
		Properties props = new Properties();
		props.put("StrictHostKeyChecking", "no");
		session.setConfig(props);

		session.connect();
		return session;
	}

	private static CommandLineApplicationOutput runShellCommand(Session session,
			String name, CommandLineApplicationInput input) throws JSchException,
			IOException, ServiceException
	{
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(name + " "
				+ TextUtilities.concatene(input.cmdLineParameters, " "));
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();
		InputStream err = channel.getErrStream();
		channel.connect();

		if (input.stdin != null)
		{
			out.write(input.stdin);
			out.close();
		}

		CommandLineApplicationOutput output = new CommandLineApplicationOutput();
		output.stdout = Utilities.readUntilEOF(in);
		output.stderr = new String(Utilities.readUntilEOF(err));

		while ( ! channel.isClosed())
		{
			Threads.sleepMs(100);
		}

		output.returnCode = channel.getExitStatus();

		if (output.returnCode != 0)
		{
			System.err.println("Error: " + output);
			throw new ServiceException("remote command has returned a non-zero value");
		}

		channel.disconnect();
		return output;
	}
}
