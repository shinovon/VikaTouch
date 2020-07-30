package vikaUI;

public abstract interface PressableUIItem extends UIItem
{
	public static final int KEY_OK = 0;
	public static final int KEY_FUNC = 1;
	public static final int KEY_BACK = 2;
	
	public void tap(int x, int y);
	
	public void keyPressed(int key);
	
	public boolean isSelected();
	
	public void setSelected(boolean selected);
}
