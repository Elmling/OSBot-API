import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class Fisher 
{
	private String fishingMode;
	private Script script;
	public List<String> Fish = new ArrayList<String>();
	
	public Fisher(Script s)
	{
		Fish.add("Shrimp");
		Fish.add("Sardine");
		Fish.add("Karambwanji");
		Fish.add("Herring");
		Fish.add("Anchovies");
		Fish.add("Mackerel");
		Fish.add("Oyster");
		Fish.add("Caskets");
		Fish.add("Trout");
		Fish.add("Cod");
		Fish.add("Pike");
		Fish.add("Smily eel");
		Fish.add("Salmon");
		Fish.add("Giant frogspawn");
		Fish.add("Tuna");
		Fish.add("Rainbow fish");
		Fish.add("Cave eel");
		Fish.add("Lobster");
		Fish.add("Bass");
		Fish.add("Swordfish");
		Fish.add("Lava eel");
		Fish.add("Monkfish");
		Fish.add("Karambwan");
		Fish.add("Shark");
		Fish.add("Sear turtle");
		Fish.add("Manta ray");
		Fish.add("Anglerfish");
		Fish.add("Dark crab");
		Fish.add("Sacred eel");
		
		
		this.script = s;
	}
	
	/**
	 * Runs the logic of the Fisher
	 * @return String<br><hr>
	 * -> Error<br>
	 * 	- an error has occured, you should terminate<br>
	 * -> Animating<br>
	 *  - When your player is in an animation<br>
	 * -> New Fishing spot<br>
	 *  - When we attempt to click a new fishing spot<br>
	 * -> No Fishing Spot<br>
	 *  - When no fishing spot is available
	 */
	
	public String run()
	{
		NPC fishingSpot = getClosestFishingSpot();
		if(fishingSpot == null)
		{
			this.script.log("No fishing spots around!");
			return "No Fishing Spots";
		}
		else
		{
			if(this.script.myPlayer().isAnimating())
			{
				return "Animating";
			}
			if(fishingSpot.getPosition().distance(this.script.myPosition()) > 10)
			{
				this.script.walking.webWalk(fishingSpot.getPosition());
				if(!fishingSpot.isVisible())
					this.script.camera.toEntity(fishingSpot);
				if(fishingSpot.isVisible())
				{
					fishingSpot.interact(getFishingMode());
					if(MethodProvider.random(0,20) < 5)
					{
						this.script.mouse.moveRandomly();
					}
				}
				return "New Fishing Spot";
			}
			else
			{
				if(!fishingSpot.isVisible())
					this.script.camera.toEntity(fishingSpot);
				if(fishingSpot.isVisible())
				{
					fishingSpot.interact(getFishingMode());
					if(MethodProvider.random(0,20) < 5)
					{
						this.script.mouse.moveRandomly();
					}
				}
			}
		}
		return "Error";
	}
	
	private NPC getClosestFishingSpot()
	{
		NPC fishingSpot = this.script.npcs.closest("Fishing spot");
		if(fishingSpot != null)
		{
			if(fishingSpot.hasAction(getFishingMode()))
			{
				return fishingSpot;
			}
		}
		return null;
	}
	
	/**
	 * Sets the fishing mode<br>
	 * <br>
	 * Example:<br>
	 * 		setFishingMode("Bait");<br>
	 * 		or<br>
	 * 		setFishingMode("Net");<br>
	 * @param str
	 */
	
	public void setFishingMode(String str)
	{
		this.fishingMode = str;
	}
	
	public String getFishingMode()
	{
		return this.fishingMode;
	}
	
	
}
