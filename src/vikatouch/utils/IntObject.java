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
	
	public final boolean equals(Object var1) {
		if (var1 instanceof IntObject) {
			return this.value == ((IntObject) var1).value;
		} else if (var1 instanceof Integer) {
			return this.value == ((Integer) var1).intValue();
		} else {
			return false;
		}
	}
	
	public int value;

}
