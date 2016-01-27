import java.util.List;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class AntiBan 
{
	private Script script;
	private int timeout;
	private long lastExamineTime = System.currentTimeMillis();
	private long lastMoveTime = System.currentTimeMillis();
	private long lastSkillCheckTime;
	private Skill skillTraining;

	public AntiBan(Script s)
	{
		this.script = s;
		this.timeout = 5000;
	}
	
	/**
	 * Used to check current Exp.
	 * @param s
	 */
	public void setSkillTraining(Skill s)
	{
		this.skillTraining = s;
	}
	
	/**
	 * Performs a random anti-ban from the AntiBan class.
	 */
	public void doRandomAntiBan()
	{
		int ran = MethodProvider.random(0,2);
		if(ran == 0)
			examineRandomObject();
		else
		if(ran == 1)
			moveMouseRandomly();
		else
		if(ran == 2 && this.skillTraining != null)
			checkSkill(this.skillTraining);
	}
	
	/**
	 * Attempts to right click or examine a random object/npc/player
	 */
	
	public void examineRandomObject()
	{
		long time = System.currentTimeMillis();
		if(time - this.lastExamineTime < this.timeout)
			return;
		int ran = MethodProvider.random(0,2);
		if(ran == 0)
		{
			List<RS2Object> o = this.script.objects.getAll();
			for(int i = 0; i < o.size(); i++)
			{
				RS2Object obj = o.get(i);
				if(obj != null)
				{
					if(obj.isVisible())
					{
						ran = MethodProvider.random(0,10);
						if(ran <= 1)
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.interact("Examine");
							break;
						}
						else
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.hover();
							try 
							{
								MethodProvider.sleep(MethodProvider.random(100,500));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.click(true);
							try 
							{
								MethodProvider.sleep(MethodProvider.random(300,1000));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.moveRandomly();
							break;
						}
					}
				}
			}
		}
		else
		if(ran == 1)
		{
			List<NPC> o = this.script.npcs.getAll();
			for(int i = 0; i < o.size(); i++)
			{
				NPC obj = o.get(i);
				if(obj != null)
				{
					if(obj.isVisible())
					{
						ran = MethodProvider.random(0,10);
						if(ran <= 1)
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.interact("Examine");
							break;
						}
						else
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.hover();
							try 
							{
								MethodProvider.sleep(MethodProvider.random(100,500));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.click(true);
							try 
							{
								MethodProvider.sleep(MethodProvider.random(300,1000));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.moveRandomly();
							break;
						}
					}
				}
			}			
		}
		else
		if(ran == 2)
		{
			List<Player> o = this.script.players.getAll();
			for(int i = 0; i < o.size(); i++)
			{
				Player obj = o.get(i);
				if(obj != null)
				{
					if(obj == this.script.myPlayer())
						continue;
					
					if(obj.isVisible())
					{
						ran = MethodProvider.random(0,10);
						if(ran <= 1)
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.interact("Examine");
							break;
						}
						else
						{
							ran = MethodProvider.random(0,10);
							if(ran <= 2)
								this.script.camera.toEntity(obj);
							obj.hover();
							try 
							{
								MethodProvider.sleep(MethodProvider.random(100,500));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.click(true);
							try 
							{
								MethodProvider.sleep(MethodProvider.random(300,1000));
							} 
							catch (InterruptedException e) 
							{
								e.printStackTrace();
							}
							this.script.mouse.moveRandomly();
							break;
						}
					}
				}
			}
		}
		this.lastExamineTime = time;
	}
	
	/**
	 * This will move the mouse randomly
	 */
	public void moveMouseRandomly()
	{
		long time = System.currentTimeMillis();
		if(time - this.lastMoveTime < this.timeout)
			return;
		int ran = MethodProvider.random(0,2);
		if(ran == 0)
			this.script.mouse.moveRandomly();
		else
		if(ran == 1)
			this.script.mouse.moveSlightly();
		else
		if(ran == 2)
			this.script.mouse.moveOutsideScreen();
		this.lastMoveTime = time;
	}
	
	/**
	 * Hovers over the skill provided
	 * @param s Skill
	 */
	public void checkSkill(Skill s)
	{
		long time = System.currentTimeMillis();
		if(time - this.lastSkillCheckTime < this.timeout)
			return;		
		try 
		{
			this.script.skills.hoverSkill(s);
			this.lastSkillCheckTime = time;
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Timeout before each anti-ban method will execute again
	 * @param timeout
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
}
