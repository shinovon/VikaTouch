package vikatouch.utils;

public class IntObject
{
	
	public IntObject(int value)
	{
		this.value = value;
	}
	
	public final int hashCode()
	{
		return value;
	}
	
	public int value;

}
