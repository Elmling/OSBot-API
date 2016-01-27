import java.io.IOException;
import java.util.List;

import org.osbot.rs07.api.GroundItems;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

public class ItemGrabber
{
	List<String> grabAlways;
	private int grabPriceHigherThan;
	private Script script;
	PriceDatabase priceDatabase = new PriceDatabase();
	GrandExchange GE = new GrandExchange();
	private List<String> ignore;
	private List<String> Stackable;
	
	
	public ItemGrabber(Script s)
	{
		this.script = s;
	}
	
	/**
	 * Grabs these items always<br>
	 * <br>
	 * Example:<br>
	 * 		List&lt;String&gt; items = new ArrayList&lt;String&gt;();<br>
	 * 		items.add("Coins");<br>
	 * 		items.add("Bones");<br>
	 *	
	 * @param name
	 */
	
	public void grabAlways(List<String> name)
	{
		grabAlways = name;
	}
	
	/**
	 * Checks if an item is on our always grab list
	 * @param name
	 * @return Boolean
	 */
	
	public boolean isGrabAlways(String name)
	{
		return grabAlways.contains(name);
	}
	
	/**
	 * Ignores these items<br>
	 * <br>
	 * Example:<br>
	 * 		List&lt;String&gt; itemsIgnore = new ArrayList&lt;String&gt;();<br>
	 * 		itemsIgnore.add("Bones");<br>
	 * 		itemsIgnore.add("Raw beef");<br>
	 * @param name
	 */
	
	public void ignore(List<String> name)
	{
		this.ignore = name;
	}
	
	
	/**
	 * Checks if an item is ignored
	 * @param name
	 * @return Boolean
	 */
	
	public boolean isIgnored(String name)
	{
		if(this.ignore == null)
			return false;
		if(this.ignore.isEmpty())
			return false;
		int count = this.ignore.size();
		for(int i = 0; i < count; i++)
		{
			String item = this.ignore.get(i);
			if(name.equals(item))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if an item is stackable, set by setIsStackable(String item)<br>
	 * <br>
	 * @param item
	 * @return Boolean
	 */
	
	public boolean isStackable(String item)
	{
		List<String> stackable = this.Stackable;
		if(stackable.contains(item))
			return true;
		return false;
	}
	
	/**
	 * Adds an item to our stackable list
	 * @param item
	 */
	
	public void setIsStackable(String item)
	{
		this.Stackable.add(item);
	}
	
	/**
	 * Sets the ItemGrabber to grab items above the price provided
	 * @param price
	 */
	
	public void grabIfPriceIsHigherThan(int price)
	{
		this.grabPriceHigherThan = price;
	}
	
	/**
	 * Scans the floor, performs logic, and will perform an action.<br>
	 * If an item is found it will attempt to pick it up. <br>
	 * <br>
	 * This should be called in a loop.
	 */
	public void scanFloor()
	{
		List<GroundItem> items = this.script.groundItems.getAll();
		if(!items.isEmpty())
		{
			Position playerPos = this.script.myPosition();
			for(int i = 0; i < items.size(); i++)
			{
				GroundItem item = items.get(i);
				if(this.script.inventory.isFull() && !isStackable(item.getName()))
					continue;
				if(item != null)
				{
					Position itemPos = item.getPosition();
					if(playerPos.distance(itemPos) > 5)
					{
						continue;
					}
					if(isIgnored(item.getName()))
					{
						continue;
					}
					if(isGrabAlways(item.getName()))
					{
						this.script.log("Always grab item " + item.getName());
						this.script.walking.webWalk(item.getPosition());
						item.interact("Take");
						try {
							MethodProvider.sleep(800 * item.getPosition().distance(this.script.myPosition()));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					}
					int price = -1;
					if(priceDatabase.hasValue(item.getName()))
					{
						price = priceDatabase.getPrice(item.getName());
					}
					else
					{
						try
						{
							price = GE.getOverallPrice(item.getId());
							priceDatabase.store(item.getName(), price);
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					if(price != -1)
						if(price >= this.grabPriceHigherThan)
						{
							this.script.log("Trying to pickup " + item.getName());
							item.interact("Take");
							try {
								MethodProvider.sleep(800 * item.getPosition().distance(this.script.myPosition()));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							continue;
						}
				}
			}
		}
	}

}
