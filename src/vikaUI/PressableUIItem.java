package vikaUI;

public abstract interface PressableUIItem extends UIItem
{
	public static final int KEY_OK = -5;
	public static final int KEY_FUNC = -6;
	public static final int KEY_DELETE = 8;
	
	public void tap(int x, int y);
	
	public void keyPressed(int key);
	
	public boolean isSelected();
	
	public void setSelected(boolean selected);
}
