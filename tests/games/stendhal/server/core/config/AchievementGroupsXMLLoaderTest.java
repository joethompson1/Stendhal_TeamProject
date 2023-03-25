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
package games.stendhal.server.core.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.rp.achievement.Achievement;
import games.stendhal.server.core.rp.achievement.AchievementNotifier;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.server.game.db.DatabaseFactory;

public class AchievementGroupsXMLLoaderTest {

	@Before
	public void setUp() {
		// Create mock world and DB
		MockStendlRPWorld.get();
		new DatabaseFactory().initializeDatabase();
		
		// Create achievement notifier
			// maybe
	}

	@After
	public void tearDown() {
		MockStendlRPWorld.reset();
	}

	@Test
	public void testLoad() throws Exception {
		
		// Initialise achievements in the game
		AchievementNotifier notifier = SingletonRepository.getAchievementNotifier();
		notifier.initialize(); 
		// If you want AchievementGroupsXMLLoader, need this to call abstract factories (already does) 
		// as well as the AchievementGroupsXMLLoader - IF DOING THIS, remove AchievementsXML loader from factories
		
		
		// Create loader to produce a list of Achievements using XML
//		AchievementGroupsXMLLoader loader = new AchievementGroupsXMLLoader(new URI("testachievements.xml"));
//		List<Achievement> list = loader.load();
//		Achievement ach = list.get(0);
				
		
		// Check achievements actually added to the world 
		// Retrieve the list of achievements
		ImmutableList<Achievement> achievements = notifier.getAchievements();
		Achievement achieve = achievements.get(0);

		System.out.println(achievements.toString());

		assertThat(achieve.getIdentifier(), is("xp.level.597"));
		assertThat(achieve.getTitle(), is("Stendhal High Master"));
		assertThat(achieve.getDescription(), is("Reach level 597"));
		assertThat(achieve.getBaseScore(), is(Achievement.HARD_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(1);

		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.300"));
		assertThat(achieve.getTitle(), is("Experienced Adventurer"));
		assertThat(achieve.getDescription(), is("Reach level 300"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));	
		
		achieve = achievements.get(2);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.100"));
		assertThat(achieve.getTitle(), is("Apprentice"));
		assertThat(achieve.getDescription(), is("Reach level 100"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));	
		
		achieve = achievements.get(3);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.500"));
		assertThat(achieve.getTitle(), is("Stendhal Master"));
		assertThat(achieve.getDescription(), is("Reach level 500"));
		assertThat(achieve.getBaseScore(), is(Achievement.HARD_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(4);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.050"));
		assertThat(achieve.getTitle(), is("Novice"));
		assertThat(achieve.getDescription(), is("Reach level 50"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));	
		
		achieve = achievements.get(5);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.010"));
		assertThat(achieve.getTitle(), is("Greenhorn"));
		assertThat(achieve.getDescription(), is("Reach level 10"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));

		achieve = achievements.get(6);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.200"));
		assertThat(achieve.getTitle(), is("Adventurer"));
		assertThat(achieve.getDescription(), is("Reach level 200"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(7);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("xp.level.400"));
		assertThat(achieve.getTitle(), is("Master Adventurer"));
		assertThat(achieve.getDescription(), is("Reach level 400"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(8);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.special.all"));
		assertThat(achieve.getTitle(), is("Legend"));
		assertThat(achieve.getDescription(), is("Kill all creatures solo"));
		assertThat(achieve.getBaseScore(), is(Achievement.HARD_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(9);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.deer"));
		assertThat(achieve.getTitle(), is("Deer Hunter"));
		assertThat(achieve.getDescription(), is("Kill 25 deer"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(10);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.rats"));
		assertThat(achieve.getTitle(), is("Rat Hunter"));
		assertThat(achieve.getDescription(), is("Kill 15 rats"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(11);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.boars"));
		assertThat(achieve.getTitle(), is("Boar Hunter"));
		assertThat(achieve.getDescription(), is("Kill 20 boar"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(12);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.foxes"));
		assertThat(achieve.getTitle(), is("Fox Hunter"));
		assertThat(achieve.getDescription(), is("Kill 20 foxes"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		//		", "Safari", "Kill 30 tigers, 30 lions and 50 elephants", Achievement.EASY_BASE_SCORE, true,

		achieve = achievements.get(13);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.safari"));
		assertThat(achieve.getTitle(), is("Safari"));
		assertThat(achieve.getDescription(), is("Kill 30 tigers, 30 lions and 50 elephants"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(14);
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("fight.general.ents"));
		assertThat(achieve.getTitle(), is("Wood Cutter"));
		assertThat(achieve.getDescription(), is("Kill 10 ents, 10 entwifes and 10 old ents"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(20);
		//quest.count.80", "Quest Junkie","Complete at least 80 quests"
		System.out.println(achievements.toString());
		
		assertThat(achieve.getIdentifier(), is("quest.count.80"));
		assertThat(achieve.getTitle(), is("Quest Junkie"));
		assertThat(achieve.getDescription(), is("Complete at least 80 quests"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(30);
		System.out.println(achievements.toString());
		assertThat(achieve.getIdentifier(), is("zone.outside.athor"));
		assertThat(achieve.getTitle(), is("Tourist"));
		assertThat(achieve.getDescription(), is("Visit all outside zones in the Athor region"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(40);
		System.out.println(achievements.toString());

		
		assertThat(achieve.getIdentifier(), is("zone.interior.semos"));
		assertThat(achieve.getTitle(), is("Home maker"));
		assertThat(achieve.getDescription(), is("Visit all interior zones in the Semos region"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(50);
		System.out.println(achievements.toString());

		assertThat(achieve.getIdentifier(), is("item.set.xeno"));
		assertThat(achieve.getTitle(), is("A Bit Xenophobic?"));
		assertThat(achieve.getDescription(), is("Loot a complete xeno equipment set"));
		assertThat(achieve.getBaseScore(), is(Achievement.HARD_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(60);
		System.out.println(achievements.toString());

		assertThat(achieve.getIdentifier(), is("friend.karma.250"));
		assertThat(achieve.getTitle(), is("Good Samaritan"));
		assertThat(achieve.getDescription(), is("Earn a very good karma"));
		assertThat(achieve.getBaseScore(), is(Achievement.MEDIUM_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(70);
		System.out.println(achievements.toString());

		assertThat(achieve.getIdentifier(), is("quest.special.daily_item.0010"));
		assertThat(achieve.getTitle(), is("Ados' Supporter"));
		assertThat(achieve.getDescription(), is("Finish daily item quest 10 times"));
		assertThat(achieve.getBaseScore(), is(Achievement.EASY_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
		
		achieve = achievements.get(80);
		System.out.println(achievements.toString());

		assertThat(achieve.getIdentifier(), is("quest.special.weekly_item.0025"));
		assertThat(achieve.getTitle(), is("Dedicated Archaeologist"));
		assertThat(achieve.getDescription(), is("Finish weekly item quest 25 times"));
		assertThat(achieve.getBaseScore(), is(Achievement.HARD_BASE_SCORE));
		assertThat(achieve.isActive(), is(true));
	}

}
