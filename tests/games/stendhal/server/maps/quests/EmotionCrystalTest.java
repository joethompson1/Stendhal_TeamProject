package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.RPClass.ItemTestHelper;
import games.stendhal.server.maps.quests.EmotionCrystals;

public class EmotionCrystalTest
{
	private Player player = null;
	private AbstractQuest quest = null;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		QuestHelper.setUpBeforeClass();
}
	
	@Before
	public void setUp() {
		player = PlayerTestHelper.createPlayer("player");
		quest = new EmotionCrystals();
	}	
	
	/**
	 * Tests for getHistory.
	 */
	@Test
	public void testGetHistory() throws Exception{
		//Started the Emotion Crystal Quest
		player.setQuest(new EmotionCrystals().getSlotName(),"start");
		//Create 3 items: red,blue and yellow crystals
		final Item redcrystal = ItemTestHelper.createItem("red emotion crystal");
		final Item bluecrystal = ItemTestHelper.createItem("blue emotion crystal");
		final Item yellowcrystal = ItemTestHelper.createItem("yellow emotion crystal");
		//Add all 3 crystals in the inventory
		player.getSlot("bag").add(redcrystal);
		player.getSlot("bag").add(bluecrystal);
		player.getSlot("bag").add(yellowcrystal);
	    //create expectedlog which contains the loghistory
		List<String> expectedlog = quest.getHistory(player);
		//Drop 2 crystals
		player.drop(redcrystal);
		player.drop(bluecrystal);
		//create actuallog which contains the loghistory
		List<String> actuallog = quest.getHistory(player);
		//compare both the logs to check if they match which they should or else the tests fail
		assertEquals(expectedlog,actuallog);

	}

	
}