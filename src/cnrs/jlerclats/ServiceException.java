package cnrs.jlerclats;


public class ServiceException extends Exception
{

	public ServiceException(Throwable e)
	{
		super(e);
	}

	public ServiceException(String e)
	{
		super(e);
	}

}
