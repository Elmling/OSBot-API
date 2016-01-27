import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class Fletcher 
{
	private Script script;
	private String fletching;
	private String logsName;
	public final String FLETCHBOWSTRING = "Bow string";
	public final String FLETCHARROWSHAFT = "Arrow Shaft";
	public final String FLETCHLONGBOW = "Long Bow";
	public final String FLETCHSHORTBOW = "Short Bow";
	public final String FLETCHCROSSBOWSTOCK = "Crossbow Stock";

	public Fletcher(Script s)
	{
		if(s != null)
			this.script = s;
		else
			return;
	}
	
	/**
	 * Sets what the Fletcher will fletch.<br>
	 * <br>
	 * Example:<br>
	 * 		Fletcher.setFletching("Willow long bow","Bow string");<br>
	 * 		or:<br>
	 * 		Fletcher.setFletching("Logs","Arrow shaft");<br>
	 * 		or:<br>
	 * 		Fletcher.setFletching("Oak logs","Short bow");<br>
	 * @param type
	 * @param logs
	 * @return
	 */
	
	public boolean setFletching(String logs,String type)
	{
		this.fletching = type;
		this.logsName = logs;
		return true;
	}
	
	public String getFletching()
	{
		return this.logsName;
	}
	
	public List<String> getAllTypes()
	{
		List<String> types = new ArrayList();
		types.add("Shortbow (u)");
		types.add("Longbow (u)");
		types.add("Oak shortbow (u)");
		types.add("Oak longbow (u)");
		types.add("Ogre composite bow (u)");
		types.add("Willow shortbow (u)");
		types.add("Willow longbow (u)");
		types.add("Maple shortbow (u)");
		types.add("Maple longbow (u)");
		types.add("Yew shortbow (u)");
		types.add("Yew longbow (u)");
		types.add("Magic shortbow (u)");
		types.add("Magic longbow (u)");
		return types;
	}
	
	/**
	 * Attempts to fletch.<br>
	 * <br>
	 * Returns the following string values:<br><br>
	 * Null (when setFletching has not been called)<br>
	 * Missing Bow string (when no Bow string in inventory)<br>
	 * Missing Knife (when no Knife in inventory)<br>
	 * Missing Logs (when no Log-type in inventory)<br>
	 * Animating (when the player is performing an animation)<br>
	 * Success (when we successfully fletched an item)<br>
	 * @return String value
	 */
	
	public String performFletching()
	{
		String logs = this.logsName;
		String fletching = this.fletching;
		boolean bowString = this.fletching.equals("Bow string");
		
		if(logs.equals(""))
			return "Null";
		
		if(fletching.equals(""))
			return "Null";
		
		if(bowString)
			logs =  this.logsName + " (u)";
		
		this.script.log("Fletching: [" + fletching + "] on [" + logs + "]");
		
		if(!this.script.inventory.contains("Bow string") && bowString)
		{
			return "Missing Bow string";
		}
		if(bowString)
			if(!this.script.inventory.contains(logs))
			{
				return "Missing Unstrung bow";
			}
		
		if(!this.script.inventory.contains("Knife") && !bowString)
		{
			return "Missing Knife";
		}
		
		if(!this.script.inventory.contains(logs) && !bowString)
		{
			return "Missing Logs";
		}
		
		if(this.script.players.myPlayer().isAnimating())
		{
			return "Animating";
		}
		else
		{
			try {
				MethodProvider.sleep(MethodProvider.random(1000,2000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(this.script.players.myPlayer().isAnimating())
			{
				return "Animating";
			}
		}
		
		if(this.script.inventory.contains(logs))
		{
			//305,2 305,3 305,4 305,5
			//304,2 304,3 304,4
			if(bowString)
			{
				this.script.log("Using " + this.logsName);
				this.script.inventory.interact("use", this.logsName + " (u)");
			}
			else
			{
				this.script.log("Using " + logs);
				this.script.inventory.interact("use", logs);
			}
			
			if(bowString)
				this.script.inventory.interact("use", "Bow string");
			else
				this.script.inventory.interact("use", "Knife");
			
			try 
			{
				MethodProvider.sleep(MethodProvider.random(900,1500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(bowString)
			{	
				if(!this.script.inventory.contains("Bow string"))
					return "Missing Bow string";
				int ran = MethodProvider.random(400,1000);
				try {
					MethodProvider.sleep(ran);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RS2Widget fletch = this.script.widgets.get(309,2);
				if(fletch != null && fletch.isVisible())
				{
					fletch.interact("Make all");
					try {
						MethodProvider.sleep(MethodProvider.random(1000,2000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "Success";
				}
				return "Error";
			}
			else
			if(this.fletching.equals("Arrow Shaft"))
			{
				if(this.logsName.equals("Logs"))
				{
					RS2Widget fletch = this.script.widgets.get(305, 2);
					if(fletch != null)
					{
						fletch.interact("Make X");
						try 
						{
							MethodProvider.sleep(MethodProvider.random(1000,1500));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						int ran = MethodProvider.random(30,100);
						RS2Widget fletchFinal = this.script.widgets.get(162,33);
						if(fletchFinal != null)
						{
							if(fletchFinal.isVisible())
							{
								this.script.keyboard.typeString(String.valueOf(ran), true);
								try 
								{
									MethodProvider.sleep(MethodProvider.random(500,1000));
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}								
							}
						}
					}
				}
			}
			else
			if(this.fletching.equals("Short Bow"))
			{
				RS2Widget fletch = null;
				
				if(this.logsName.equals("Logs"))
					fletch = this.script.widgets.get(305, 3);
				else
					fletch = this.script.widgets.get(304, 2);
				
				if(fletch != null)
				{
					fletch.interact("Make X");
					try 
					{
						MethodProvider.sleep(MethodProvider.random(1000,1500));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					int ran = MethodProvider.random(30,100);
					RS2Widget fletchFinal = this.script.widgets.get(162,33);
					if(fletchFinal != null)
					{
						if(fletchFinal.isVisible())
						{
							this.script.keyboard.typeString(String.valueOf(ran), true);
							try 
							{
								MethodProvider.sleep(MethodProvider.random(500,1000));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}								
						}
					}
				}				
			}
			else
			if(this.fletching.equals("Long Bow"))
			{
				RS2Widget fletch = null;
				
				if(this.logsName.equals("Logs"))
					fletch = this.script.widgets.get(305, 4);
				else
					fletch = this.script.widgets.get(304, 3);
				
				if(fletch != null)
				{
					fletch.interact("Make X");
					try 
					{
						MethodProvider.sleep(MethodProvider.random(1000,1500));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					int ran = MethodProvider.random(30,100);
					RS2Widget fletchFinal = this.script.widgets.get(162,33);
					if(fletchFinal != null)
					{
						if(fletchFinal.isVisible())
						{
							this.script.keyboard.typeString(String.valueOf(ran), true);
							try 
							{
								MethodProvider.sleep(MethodProvider.random(500,1000));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}								
						}
					}
				}	
			}
			else
			if(this.fletching.equals("Crossbow Stock"))
			{
				
			}
			
			return "Success";
		}
		
		return "Success";
	}

	public boolean withdrawUnstrungBow() {
		boolean bowString = this.fletching.equals("Bow string");
		String logs = this.logsName;
		if(bowString)
		{
			this.script.bank.withdraw(logs + " (u)",14);
			try {
				MethodProvider.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.script.inventory.contains(logs + " (u)"))
				return true;
			else
				return false;
		}
		return false;	
	}
}
