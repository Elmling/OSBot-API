import java.util.ArrayList;
import java.util.List;


public class PriceDatabase 
{
	private List<String> list = new ArrayList<String>();

	public PriceDatabase() 
	{
		
	}
	
	/**
	 * Stores the price of an object.
	 * @param name
	 * @param price
	 */
	
	public void store(String name, int price)
	{
		String val = String.valueOf(price);
		list.add(name);
		list.add(val);
		
	}
	
	/**
	 * Returns the price of an object
	 * Will return -1 on failure.
	 * @param name
	 * @return
	 */
	
	public int getPrice(String name)
	{
		if(list.contains(name))
		{
			int val = list.indexOf(name);
			int finalVal = Integer.parseInt(list.get(val + 1));
			return finalVal;
		}
		return -1;
	}
	
	/**
	 * Will return true if the PriceDatabase has
	 * Stored a value for the name provided.
	 * @param name
	 * @return
	 */
	
	public boolean hasValue(String name)
	{
		if(list.contains(name))
			return true;
		else
			return false;
	}
	
	/**
	 * Clears the Database, getting rid of all stored data
	 */
	public void flush()
	{
		list.clear();
	}
	
}
