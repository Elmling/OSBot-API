import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class WoodCutter 
{
	private Script script;
	private List<String> treeList;
	private RS2Object currentTreeChopping = null;
	private RS2Object lastTreeObj = null;
	private int lastTreeObjCount = 0;
	private List<String> treeMemoryFail = new ArrayList<String>();
	
	/**
	 * Creates a new WoodCutter Class<br>
	 * <br>
	 * Example: WoodCutter WoodCutter = new WoodCutter(this);<br>
	 * @param s script (this)<br>
	 */

	public WoodCutter(Script s)
	{
		this.script = s;
		List<String> treeList = new ArrayList<String>();
		this.treeList = treeList;
	}
	
	/**
	 * This function adds a tree by string name to the tree list<br>
	 * <br>
	 * Example: <br>
	 * 		TreeListAdd("Oak");<br>
	 * 		TreeListAdd("Maple");<br>
	 * 		TreeListAdd("Tree");<br>
	 * @param tree<br>
	 * @return	Success value<br>
	 */
	
	public boolean TreeListAdd(String tree)
	{
		if(!tree.equals(""))
		{
			this.treeList.add(tree);
			return true;
		}
		return false;
	}
	
	
	private int chopClosestTreeCallCount = 0;
	
	/**
	 * This function attempts to chop the closest tree available.<br>
	 * <br>
	 * -1<br>
	 * 	- Returns -1 if the player object is in an animation<br>
	 * 0<br>
	 * 	- Returns 0 on failed attempt<br>
	 * 1<br>
	 * 	- Returns 1 on success<br>
	 * @return integer value
	 */
	
	
	public int ChopClosestTrees()
	{
		if(this.script.myPlayer().isAnimating())
			return -1;
		if(this.treeList.isEmpty())
			return 0;
		if(!this.script.inventory.contains("Bronze axe","Iron axe","Steel axe","Black axe","Mithril axe","Adamant axe","Rune axe","Dragon axe"))
		{
			if(!this.script.equipment.contains("Bronze axe","Iron axe","Steel axe","Black axe","Mithril axe","Adamant axe","Rune axe","Dragon axe"))
			{
				return 0;
			}
		}
		setChopClosestTreeCallCount(getChopClosestTreeCallCount() + 1);
		if(getChopClosestTreeCallCount() >= 400)
		{
			this.script.log("<WoodCutter> Clearing Failed Tree Memory");
			treeMemoryFail.clear();
			setChopClosestTreeCallCount(0);
		}
		int treeListCount = this.treeList.size();
		for(int i = 0; i < treeListCount; i++)
		{
			String tree = this.treeList.get(i);
			RS2Object treeObj = getClosestTree(tree);
			if(treeObj != null)
			{
				if(this.script.myPlayer().isAnimating() || this.script.myPlayer().isMoving())
				{
					lastTreeObjCount = 0;
				}
				else
				{
					if(lastTreeObj == null)
						lastTreeObj = treeObj;
					else
					{
						if(lastTreeObj.equals(treeObj))
							lastTreeObjCount ++;
						
						if(lastTreeObjCount > 4)
						{
							treeMemoryFail.add(treeObj.getPosition().toString());
							lastTreeObjCount = 0;
							this.script.log("Ignoring tree at position " + treeObj.getPosition());
							continue;
						}
					}
				}
				
				boolean tmf = treeMemoryFail.contains(treeObj.getPosition().toString());
				
				if(tmf)
				{
					this.script.log("Tree on memory detected");
					continue;
				}
			
				//functionality 
				//-------------
				if(!treeObj.isVisible())
				{
					this.script.walking.webWalk(treeObj.getPosition());
				}
				
				if(this.currentTreeChopping != treeObj)
					treeObj.interact("Chop down");
				else if(MethodProvider.random(0,10) < 3)
				{
					treeObj.interact("Chop down");
				}
				this.setCurrentTreeChopping(treeObj);
				lastTreeObj  = treeObj;
				//=============
				return 1;
			}
		}
		return 0;
	}
	
	private String inventoryMode = "Bank";
	
	/**
	 * Sets what we are going to do when the inventory is full<br>
	 * <br>
	 * case 1: Bank<br>
	 * 		Find the closest bank and bank the inventory<br><br>
	 * case 2: Drop<br>
	 * 		Drop all logs from the inventory<br>
	 * @param mode (String)<br>
	 * @return True<br>
	 */
	
	public boolean setInventoryMode(String mode)
	{
		this.inventoryMode = mode;
		return true;
	}
	
	/**
	 * Checks the inventory based on inventoryMode set by setInventoryMode(String)<br>
	 * <br>
	 * @return<br>
	 * True - Inventory is full<br>
	 * False - Inventory is not full
	 */
	
	public boolean checkInventory()
	{
		if(this.script.inventory.isFull())
		{
		     return true;
		}
		return false;
	}
	
	/**
	 * Gets the closest Tree by the name of the Tree<br>
	 * <br>
	 * Example: <br>
	 * 		getClosestTree("Tree");<br>
	 * 		getClosestTree("Oak");<br>
	 * @param s (String)<br>
	 * @return Tree object (RS2Object)
	 */

	public RS2Object getClosestTree(String s)
	{
		List<RS2Object> trees = this.script.objects.getAll();
		if(trees.isEmpty())
			return null;
		RS2Object finalTree = null;
		int c = trees.size();
		for (int i = 0; i < c; i++)
		{
			RS2Object tree = trees.get(i);
			if(tree != null)
			{
				if(!tree.hasAction("Chop down"))
					continue;
				if(!treeList.contains(tree.getName()))
				{
					continue;
				}
				if(treeMemoryFail.contains(tree.getPosition().toString()))
				{
					continue;
				}	
				
				if(finalTree == null)
					finalTree = tree;
				else
				if(finalTree.getPosition().distance(this.script.myPosition()) > tree.getPosition().distance(this.script.myPosition()))
				{
					finalTree = tree;
				}
			}
		}
		//this.script.log("Tree at postion " + finalTree.getPosition() + " pulled Distance: " +finalTree.getPosition().distance(this.script.myPosition()));
		return finalTree;
	}
	
	public RS2Object getCurrentTreeChopping() {
		return currentTreeChopping;
	}

	public void setCurrentTreeChopping(RS2Object currentTreeChopping) {
		this.currentTreeChopping = currentTreeChopping;
	}

	public String getInventoryMode() {
		return inventoryMode;
	}

	public int getChopClosestTreeCallCount() {
		return chopClosestTreeCallCount;
	}

	public void setChopClosestTreeCallCount(int chopClosestTreeCallCount) {
		this.chopClosestTreeCallCount = chopClosestTreeCallCount;
	}
		
	

}
