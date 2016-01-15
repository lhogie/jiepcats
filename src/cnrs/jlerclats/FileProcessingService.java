package cnrs.jlerclats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import toools.io.file.Directory;
import toools.io.file.RegularFile;
import cnrs.jlerclats.impl.DownloadService;
import cnrs.jlerclats.impl.RemoveFileService;
import cnrs.jlerclats.impl.UploadService;

public abstract class FileProcessingService extends Service
{
	private Directory resultFilesDirectory = Directory.getCurrentDirectory();

	public FileProcessingService(Access sshConfig, String cmd)
	{
		super(sshConfig, cmd);
	}

	public Directory getTargetDirectory()
	{
		return resultFilesDirectory;
	}

	public void setTargetDirectory(Directory targetDirectory)
	{
		this.resultFilesDirectory = targetDirectory;
	}

	/**
	 * A service deals with files. Files cannot be transferred through the
	 * command-line. So they must be uploaded first. Result files must be
	 * downloaded after the SSH command has completed after what they can be
	 * deleted from the server.
	 */
	@Override
	protected synchronized CommandLineApplicationOutput doit(
			CommandLineApplicationInput input) throws ServiceException,
			IOException
	{
		// upload input files to the server so the SSH command will find them at
		// the root directory of the local user
		List<RegularFile> uploadedFiles = uploadFilesIfAny(input.cmdLineParameters);

		// run the shell command on the server
		CommandLineApplicationOutput output = super.doit(input);

		// if some input files were uploaded for remote processing
		if ( ! uploadedFiles.isEmpty())
		{
			// delete them from the server
			CommandLineApplicationInput rmInput = new CommandLineApplicationInput();
			rmInput.cmdLineParameters.addAll(uploadedFiles);
			new RemoveFileService(getSSHConfig()).doit(rmInput);
		}

		if ( ! resultFilesDirectory.exists())
			resultFilesDirectory.mkdirs();

		return output;
	}

	/**
	 * Returns a set of the names of the files that have been uploaded. This
	 * name will allow the remote program to find the files
	 */
	private List<RegularFile> uploadFilesIfAny(List parms) throws ServiceException,
			IOException
	{
		List<RegularFile> uploadedFileNames = new ArrayList<>();

		for (int i = 0; i < parms.size(); ++i)
		{
			if (parms.get(i) instanceof RegularFile)
			{
				RegularFile f = (RegularFile) parms.get(i);
				getListener().uploading(f.getPath());
				new UploadService(getSSHConfig()).upload(f);
				getListener().uploaded(f.getPath());
				uploadedFileNames.add(f);
				parms.set(i, f.getName());
			}
			else if (parms.get(i) instanceof File)
			{
				File f = (File) parms.get(i);
				getListener().uploaded(f.getAbsolutePath());
				new UploadService(getSSHConfig()).upload(f);
				getListener().uploaded(f.getAbsolutePath());
				parms.set(i, f.getName());
				uploadedFileNames.add(new RegularFile(f.getAbsolutePath()));
			}
		}

		return uploadedFileNames;
	}

	protected RegularFile retrieveFile(String name) throws ServiceException, IOException
	{
		RegularFile outfile = getTargetDirectory().getChildRegularFile(name);
		getListener().downloading(name, outfile);
		new DownloadService(getSSHConfig()).download(name, outfile);
		getListener().downloaded(name, outfile);
		getListener().removing(name);
		new RemoveFileService(getSSHConfig()).remove(name);
		getListener().removed(name);
		return outfile;
	}
}