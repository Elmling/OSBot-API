import java.util.List;
import java.util.Random;

import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.Skills;
import org.osbot.rs07.api.Tabs;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.api.Tabs;



public class Fighter 
{
	private Script script;
	private String[] enemy = {""};
	private boolean attackEnemies = true;
	private boolean isFighterPaused;
	private int hoverTimeout;
	private long lastHovertime;
	private String lastHoverName;
	private long lastStatCheckTime = System.currentTimeMillis();
	private long statCheckTimeout = 60000;
	
	/**
	 * 
	 * @param s
	 */
	
	public Fighter(Script s)
	{
		this.script = s;
		if(this.script.equals(""))
			return;
		this.hoverTimeout = 10000;
		this.lastHovertime = System.currentTimeMillis();
	}
	
	/**
	 * Pauses the fighter
	 */
	
	public void pauseFighter()
	{
		this.isFighterPaused = true;
	}
	
	/**
	 * Unpauses the fighter
	 */
	
	public void unpauseFighter()
	{
		this.isFighterPaused = false;
	}
	
	/**
	 * Returns true if the fighter is paused
	 * @return
	 */
	
	public boolean isFighterPaused()
	{
		return this.isFighterPaused;
	}
	
	/**
	 * Attempts to find an enemy provided by
	 * setEnemies() method
	 * @return
	 */
	
	public boolean attackEnemies()
	{
		if(!isFighterPaused())
		{
			if(this.script.myPlayer().isUnderAttack())
			{
				if(this.script.myPlayer().getCurrentHealth() < 20)
				{
					Item[] inventoryList = this.script.inventory.getItems();
					for(int j = 0; j < inventoryList.length; j++)
					{
						Item inventoryItem = inventoryList[j];
						if(inventoryItem != null)
						{
							this.script.log("Inventory item : " + inventoryItem.getName());
							if(inventoryItem.hasAction("Eat"))
							{
								inventoryItem.interact("Eat");
								try {
									MethodProvider.sleep(1500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
				return false;
			}
			String[] enemies = getEnemies();
			if(enemies.length > 0)
			{
				for(int i = 0; i < enemies.length; i++)
				{
					String currEnemy = enemies[i];
					this.script.log("[Fighter] - Curr enemy check: " + currEnemy);
					if(!currEnemy.equals(""))
					{
						NPC enemy = getClosest(currEnemy);
						
						if(enemy == null)
						{
							return false;
						}
						
						if(enemy.isOnScreen())
						{
							RS2Object doorCheck = this.script.objects.closest("Door","Longhall Door");
							if(doorCheck != null)
							{
								if(doorCheck.getPosition().distance(this.script.myPlayer().getPosition()) <= 5)
								{
									if(doorCheck.hasAction("Open") && !doorCheck.hasAction("Close"))
									{
										doorCheck.interact("Open");
										try {
											MethodProvider.sleep(800);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										return false;
									}
								}
							}
							this.script.walking.webWalk(enemy.getPosition());
							enemy.interact("Attack");
							this.script.log("[Fighter] - Attacking " + currEnemy);
							return true;
						}
						else
						{
							this.script.camera.toEntity(enemy);
							this.script.walking.webWalk(enemy.getPosition());
							if(enemy.exists() && enemy.isOnScreen())
							{
								enemy.interact("Attack");
								try {
									MethodProvider.sleep(random(1000,1500));
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Gets the closest NPC by name
	 * @param name
	 * @return NPC object
	 */
	
	public NPC getClosest(String name)
	{
		List<NPC> npcList = this.script.npcs.getAll();
		NPC finalNPC = null;
		NPC hold = null;
		Position myPos = this.script.myPosition();
		for(int i = 0; i < npcList.size(); i++)
		{
			NPC npcItem = npcList.get(i);
			if(npcItem == null)continue;
			if(!npcItem.getName().equals(name))continue;
			if(!npcItem.isAttackable())
				continue;
			if(hold == null)
				hold = npcItem;
			else if(myPos.distance(hold.getPosition()) > myPos.distance(npcItem.getPosition()))
			{
				hold = npcItem;
			}
		}
		finalNPC = hold;
		return finalNPC;
	}
	
	/**
	 * Random function
	 * @param a
	 * @param b
	 * @return
	 */
	
	public int random(int a, int b)
	{
		Random random = new Random();
	    long range = (long)b - (long)a + 1;
	    long fraction = (long)(range * random.nextDouble());
	    int randomNumber =  (int)(fraction + a);  
	    return randomNumber;
	}
	
	/**
	 * Sets the fighter to attack these enemies
	 * Each enemy is further prioritized than the first
	 * @param enemy
	 * @return
	 */
	
	public String[] setEnemies(String[] enemy)
	{
		this.enemy = enemy;
		return enemy;
	}
	
	/**
	 * Get current enemies the fighter will look for
	 * @return
	 */

	public String[] getEnemies() 
	{
		return this.enemy;
	} 
	
	public int setHoverTimeout(int timeout)
	{
		return (this.hoverTimeout = timeout);
	}
	
	/**
	 * Checks a random Combat stat
	 */
	
	public void checkTrainingStat()
	{
		long time = System.currentTimeMillis();
		if(time - lastStatCheckTime > statCheckTimeout)
		{
			this.script.tabs.open(Tab.SKILLS);
			Skill s = Skill.ATTACK;
			int rand = random(0,4);
			if(rand == 0)
				s = Skill.ATTACK;
			else
			if(rand == 1)
				s = Skill.DEFENCE;
			if(rand == 2)
				s = Skill.HITPOINTS;
			else
			if(rand == 3)
				s = Skill.STRENGTH;
			
			try {
				MethodProvider.sleep(random(100,1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				this.script.skills.hoverSkill(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				MethodProvider.sleep(random(1000,2500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			lastStatCheckTime = time;
			
			this.script.tabs.open(Tab.INVENTORY);
			try {
				MethodProvider.sleep(random(200,600));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			this.script.mouse.moveRandomly();
		}
	}
	
	/**
	 * Attempts to hover over a random NPC
	 */
	
	public void hoverOverRandomNPC()
	{
		long time = System.currentTimeMillis();
		if(time - this.lastHovertime > hoverTimeout)
		{
			List<NPC> npcGroup = this.script.npcs.getAll();
			if(!npcGroup.isEmpty())
			{
				NPC random = npcGroup.get(random(0,npcGroup.size() -1));
				if(random != null)
				{
					this.script.log("Random NPC name = " + random.getName());
					if(random.getPosition().distance(this.script.myPosition()) < random(5,10))
					{
						return;
					}
					
					if(!random.isOnScreen())
						this.script.camera.toEntity(random);
					
					if(random.isOnScreen())
					{
						this.lastHovertime = time;
						this.lastHoverName = random.getName();
						random.hover();
					}
				}
			}		
		}
	}
	
	/**
	 * Attempts to hover over a random Object
	 */
	
	public void hoverOverRandomObject()
	{
		long time = System.currentTimeMillis();
		if(time - this.lastHovertime > hoverTimeout)
		{
			List<RS2Object> objectGroup = this.script.objects.getAll();
			if(!objectGroup.isEmpty())
			{
				RS2Object random = objectGroup.get(random(0,objectGroup.size() -1));
				if(random != null)
				{
					if(random.getPosition().distance(this.script.myPosition()) < random(5,10))
					{
						return;
					}
					
					this.script.log("Random Object name = " + random.getName());
					if(!random.isVisible())
						this.script.camera.toEntity(random);
					
					if(random.isVisible())
					{
						this.lastHovertime = time;
						this.lastHoverName = random.getName();
						random.hover();
					}
				}
			}		
		}
	}
}
