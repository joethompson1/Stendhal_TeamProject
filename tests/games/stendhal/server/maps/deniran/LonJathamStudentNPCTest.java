package games.stendhal.server.maps.deniran;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.quests.LonJathamRecruitingStudents;
import marauroa.common.Log4J;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import games.stendhal.server.maps.deniran.LonJathamNPC;
import games.stendhal.server.maps.deniran.Student1NPC;
import games.stendhal.server.maps.deniran.Student2NPC;
import games.stendhal.server.maps.deniran.Student3NPC;


public class LonJathamStudentNPCTest {

	private SpeakerNPC jacob = null;
	private SpeakerNPC michael = null;
	private SpeakerNPC james = null;
	private Player player = null;
	private Engine en = null;
	private LonJathamRecruitingStudents ljrs = null;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log4J.init();
		
		MockStendhalRPRuleProcessor.get();
		
		MockStendlRPWorld.reset();
		MockStendlRPWorld.get();

	}

	@Before
	public void setUp() {
		PlayerTestHelper.removeAllPlayers();
		StendhalRPZone zone = new StendhalRPZone("admin_test");
		new LonJathamNPC().configureZone(zone, null);
		new Student1NPC().configureZone(zone, null);
		new Student2NPC().configureZone(zone, null);
		new Student3NPC().configureZone(zone, null);
		jacob = SingletonRepository.getNPCList().get("Jacob");
		michael = SingletonRepository.getNPCList().get("Michael");
		james = SingletonRepository.getNPCList().get("James");
		
		ljrs = new LonJathamRecruitingStudents();
		ljrs.addToWorld();
	}

	/**
	 * Tests for hiandBye.
	 */
	
	// student 1
	@Test
	public void testHiandBye1() {
		en = jacob.getEngine();
		en.setCurrentState(ConversationStates.IDLE);
		player = PlayerTestHelper.createPlayer("player");
		
		en.step(player, "hi");
		assertThat(en.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(jacob), is("Hello"));

		en.step(player, "bye");
		assertThat(en.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(jacob), is("Bye!"));
	}
	
// student 2
	@Test
	public void testHiandBye2() {
		en = michael.getEngine();
		en.setCurrentState(ConversationStates.IDLE);
		player = PlayerTestHelper.createPlayer("player");
		
		en.step(player, "hi");
		assertThat(en.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(michael), is("Hey."));

		en.step(player, "bye");
		assertThat(en.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(michael), is("See ya."));
	}
	
	// student 3
	@Test
	public void testHiandBye3() {
		en = james.getEngine();
		en.setCurrentState(ConversationStates.IDLE);
		player = PlayerTestHelper.createPlayer("player");
		
		en.step(player, "hi");
		assertThat(en.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(james), is("Hiya!"));

		en.step(player, "bye");
		assertThat(en.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(james), is("Farewell!"));
	}
	
	// student 1 quest dialogue
	@Test
	public void lonJathamJacobTest() {

		en = jacob.getEngine();
		player = PlayerTestHelper.createPlayer("player");
		en.setCurrentState(ConversationStates.IDLE);


		player.setQuest("LonJathamRecruitingStudents", "start");
		Item item = ItemTestHelper.createItem("prospectus");
		player.getSlot("bag").add(item);
		
		en.step(player, "hi");
		assertEquals("Hello! I sure wish there was a technology #course available nearby",
				getReply(jacob));
		
		en.step(player, "course");
		assertEquals("Oh there is? Can you find out the answer to 'what type of AI will we study?'",
				getReply(jacob));

		en.step(player, "symbolic AI");
		assertEquals("Great! Sign me up!",
				getReply(jacob));
		
		assertFalse(player.isEquipped("prospectus"));
	}
	
	// student 2 quest dialogue
	@Test
	public void lonJathamMichaelTest() {

		en = michael.getEngine();
		player = PlayerTestHelper.createPlayer("player");
		en.setCurrentState(ConversationStates.IDLE);


		player.setQuest("LonJathamRecruitingStudents", "student1Signed");
		Item item = ItemTestHelper.createItem("prospectus");
		player.getSlot("bag").add(item);
		
		en.step(player, "hi");
		assertEquals("Hey, I'm bored, I wish I could learn #programming somewhere...",
				getReply(michael));
		
		en.step(player, "programming");
		assertEquals("I can? Fab can you find out 'Do we program in python?' and if not what we do learn!",
				getReply(michael));

		en.step(player, "java");
		assertEquals("Thats fine, Sign me up.",
				getReply(michael));
		
		assertFalse(player.isEquipped("prospectus"));
	}
	
	// student 3 quest dialogue
	@Test
	public void lonJathamJamesTest() {

		en = james.getEngine();
		player = PlayerTestHelper.createPlayer("player");
		en.setCurrentState(ConversationStates.IDLE);


		player.setQuest("LonJathamRecruitingStudents", "student2Signed");
		Item item = ItemTestHelper.createItem("prospectus");
		player.getSlot("bag").add(item);
		
		en.step(player, "hi");
		assertEquals("Hiya! I wonder where I could learn #computer things",
				getReply(james));
		
		en.step(player, "computer");
		assertEquals("Oh cool, I'm just a bit worried about one thing. Can you find out 'Are the hawaiian shirts optional?'",
				getReply(james));

		en.step(player, "yes");
		assertEquals("Awesome, sign me up!",
				getReply(james));
		
		assertFalse(player.isEquipped("prospectus"));
	}
	
	
}
