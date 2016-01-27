import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class Banking 
{
	
	private Script script;
	
	/**
	 * Creates a new Banking Class
	 * @param s - script (this)
	 */

	public Banking(Script s)
	{
		this.script = s;
	}
	
	/**
	 * Attempts to open up the bank
	 * @return success
	 */
	
	public boolean openClosestBank()
	{
		if(this.script.bank.isOpen())
			this.script.bank.close();
		
		RS2Object bankBooth = this.script.objects.closest("Bank booth");
		if(bankBooth != null)
		if(bankBooth.getPosition().distance(this.script.myPosition()) < 10)
		{
			if(bankBooth.getPosition().distance(this.script.players.myPosition()) > 13)
			{
				this.script.walking.webWalk(bankBooth.getPosition());
			}
			
			if(!bankBooth.isVisible())
			{
				this.script.camera.toEntity(bankBooth);
			}
			bankBooth.interact("Bank");
			try {
				MethodProvider.sleep(MethodProvider.random(500,1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(this.script.myPlayer().isMoving())
			{
				try {
					MethodProvider.sleep(MethodProvider.random(100,500));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			try {
				MethodProvider.sleep(MethodProvider.random(600,800));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.script.bank.isOpen())
				return true;
			else
				return false;			
		}
		else
		{
			NPC banker = this.script.npcs.closest("Banker");
			if(banker != null)
			{
				if(banker.getPosition().distance(this.script.players.myPosition()) > 13)
				{
					this.script.walking.webWalk(banker.getPosition());
				}
				
				if(!banker.isOnScreen())
				{
					this.script.camera.toEntity(banker);
				}
				if(banker.hasAction("Use Bow string"))
				{
					banker.interact("Use Bow string");
				}
				banker.interact("Bank");
				try {
					MethodProvider.sleep(MethodProvider.random(500,1000));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(this.script.myPlayer().isMoving())
				{
					try {
						MethodProvider.sleep(MethodProvider.random(100,500));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
				try {
					MethodProvider.sleep(MethodProvider.random(600,800));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.script.bank.isOpen())
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	/**
	 * Utilizes the webWalk class to walk to an area
	 * 
	 * @param bank - An area returned by the getClosestBank() method
	 * @return True if we arrived in the bank, otherwise false
	 */
	public boolean runToBank(Area bank)
	{
		this.script.walking.webWalk(bank.getRandomPosition());
		if(bank.contains(this.script.myPlayer()))
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Gets the closest bank from your position and returns that Area object<br>
	 * @return Area
	 */
	
	public Area getClosestBank()
	{
		Area closestBank = null;
		Area[] banks = 
		{
			Banks.AL_KHARID,
			Banks.DRAYNOR,
			Banks.EDGEVILLE,
			Banks.FALADOR_EAST,
			Banks.FALADOR_WEST,
			Banks.VARROCK_EAST,
			Banks.VARROCK_WEST,
			Banks.CAMELOT,
			Banks.CATHERBY,
			Banks.ARDOUGNE_NORTH,
			Banks.ARDOUGNE_SOUTH,
			Banks.CANIFIS,
			Banks.CASTLE_WARS,
			Banks.DUEL_ARENA,
			Banks.GNOME_STRONGHOLD,
		};
		int l = banks.length;
		if(this.script.myPlayer() != null)
		{
			Position p = this.script.myPosition();
			for(int i = 0; i < l; i ++)
			{
				Area currBank = banks[i];
				if(closestBank == null)
					closestBank = currBank;
				else
				if(currBank.getRandomPosition().distance(p) < closestBank.getRandomPosition().distance(p))
					closestBank = currBank;
			}
		}
		return closestBank;
	}
	
	public boolean depositAllFish(List<String> list)
	{
		if(list.isEmpty())
			return true;
		int count = list.size();
		for(int i = 0; i < count; i ++)
		{
			String item = list.get(i);
			if(this.script.inventory.contains("Raw " + item))
			{
				this.script.bank.deposit("Raw " + item, MethodProvider.random(28,60));
				continue;
			}
			else
			if(this.script.inventory.contains("Raw " + item + "s"))
			{
				this.script.bank.deposit("Raw " + item + "s", MethodProvider.random(28,60));
				continue;	
			}
		}
		return true;
	}
}
