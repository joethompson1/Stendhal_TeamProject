/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests;

//import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.deniran.LonJathamNPC;
import games.stendhal.server.maps.deniran.Student1NPC;
import games.stendhal.server.maps.deniran.Student2NPC;
import games.stendhal.server.maps.deniran.Student3NPC;
import marauroa.common.Log4J;
import utilities.PlayerTestHelper;

public class LonJathamRecruitingStudentsTest {

	private SpeakerNPC lonJatham = null;

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
		lonJatham = SingletonRepository.getNPCList().get("Lon Jatham");
		
		ljrs = new LonJathamRecruitingStudents();
		ljrs.addToWorld();
	}

	/**
	 * Tests for quest.
	 */
	@Test
	public void testQuest() {
		//final Player player = PlayerTestHelper.createPlayer("player");
		player = PlayerTestHelper.createPlayer("player");
		en = lonJatham.getEngine();
		
		en.step(player, "hi");
		assertTrue(lonJatham.isTalking());
		assertEquals("Hello folks!", getReply(lonJatham));
		
		en.step(player, "quest");
		assertEquals("I only need another 3 students to fill my course, will you help me find the remaining 3",
				getReply(lonJatham));
		
		
		// accepts quest
		en.step(player, "yes");
		assertEquals("Thanks! I'll be right here, waiting, waiting for all those beautiful students.", 
				getReply(lonJatham));
		assertTrue(player.hasQuest("LonJathamRecruitingStudents"));
		en.step(player, "bye");
	}
	@Test	
	public void rejectQuestTest() {
		// rejects quest
		player = PlayerTestHelper.createPlayer("player");
		en = lonJatham.getEngine();
		
		en.step(player, "hi");
		assertTrue(lonJatham.isTalking());
		assertEquals("Hello folks!", getReply(lonJatham));
		
		en.step(player, "quest");
		assertEquals("I only need another 3 students to fill my course, will you help me find the remaining 3",
				getReply(lonJatham));
		
		en.step(player, "no");
		assertTrue(player.hasQuest("LonJathamRecruitingStudents"));
		assertEquals("rejected", player.getQuest("LonJathamRecruitingStudents"));
		en.step(player, "bye");
	} 
	
	@Test
	public void testQuestion1() {
		player = PlayerTestHelper.createPlayer("player");
		en = lonJatham.getEngine();
		
		player.setQuest("LonJathamRecruitingStudents", "start");
		
		en.step(player, "What type of AI will we study?");
		assertEquals("We will be learning symbolic AI. Now hurry off and find me those students",
				getReply(lonJatham));
		
		en.step(player, "bye");
	}
	
	@Test
	public void testQuestion2() {
		player = PlayerTestHelper.createPlayer("player");
		en = lonJatham.getEngine();
		player.setQuest("LonJathamRecruitingStudents", "start");
		// Question 2
		en.step(player, "Are the hawaiian shirts optional?");
		assertEquals("Of course they are optional. Its called FASHUN, look it up!",
				getReply(lonJatham));
		en.step(player, "bye");		
	}
	
	@Test
	public void testQuestion3() {
		player = PlayerTestHelper.createPlayer("player");
		en = lonJatham.getEngine();
		player.setQuest("LonJathamRecruitingStudents", "start");
		// Question 3
		en.step(player, "Do we program in python?");
		assertEquals("No! We will learn Java! The far more superior language, now hurry off and find me those students",
				getReply(lonJatham));
		en.step(player, "bye");
	}
	
	/**
	 *  Tests for getHistory
	 */
	@Test
	public void testgetHistory() {
		final Player player = PlayerTestHelper.createPlayer("player");
		assertTrue(ljrs.getHistory(player).isEmpty());
		player.setQuest("LonJathamRecruitingStudents", "");
		final List<String> history = new LinkedList<String>();
		history.add("I have talked to Lon Jatham.");
		assertEquals(history, ljrs.getHistory(player));

		player.setQuest("LonJathamRecruitingStudents", "rejected");
		history.add("I do not want to help Lon Jatham find 3 students");
		assertEquals(history, ljrs.getHistory(player));

		player.setQuest("LonJathamRecruitingStudents", "start");
		history.remove("I do not want to help Lon Jatham find 3 students");
		history.add("I promised to find 3 students for Lon Jatham");
		assertEquals(history, ljrs.getHistory(player));

		player.setQuest("LonJathamRecruitingStudents", "student1Signed");
		history.remove("I promised to find 3 students for Lon Jatham");
		history.add("Students found: 1");
		assertEquals(history, ljrs.getHistory(player));
		
		player.setQuest("LonJathamRecruitingStudents", "student2Signed");
		history.remove("Students found: 1");
		history.add("Students found: 2");
		assertEquals(history, ljrs.getHistory(player));
		
		player.setQuest("LonJathamRecruitingStudents", "student3Signed");
		history.remove("Students found: 2");
		history.add("Students found: 3");
		assertEquals(history, ljrs.getHistory(player));

	}
		
	
}
