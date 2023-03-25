package games.stendhal.server.core.rp.achievement.factory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.xml.sax.SAXException;

import games.stendhal.server.core.config.AchievementsXMLLoader;
import games.stendhal.server.core.rp.achievement.Achievement;
import games.stendhal.server.core.rp.achievement.Category;
import games.stendhal.server.entity.npc.condition.QuestStateGreaterThanCondition;

/**
 * factory for item related achievements.
 *
 * @author madmetzger
 */
public class AdosItemQuestAchievementsFactory extends AbstractAchievementFactory {

	@Override
	protected Category getCategory() {
		return Category.QUEST_ADOS_ITEMS;
	}

	@Override

	public Collection<Achievement> createAchievements() throws SAXException, URISyntaxException {
//		List<Achievement> achievements = new LinkedList<Achievement>();
		
		// Create the loader to load XML
		AchievementsXMLLoader loader = new AchievementsXMLLoader();
		
		// Load the Achievements
		List<Achievement> achievements = loader.load(new URI("/data/conf/achievements/adositemquestachievements.xml"));
		

		//daily item quest achievements
//		achievements.add(createAchievement("quest.special.daily_item.0010", "Ados' Supporter", "Finish daily item quest 10 times",
//												Achievement.EASY_BASE_SCORE, true, new QuestStateGreaterThanCondition("daily_item", 2, 9)));
		achievements.add(createAchievement("quest.special.daily_item.0050", "Ados' Provider", "Finish daily item quest 50 times",
												Achievement.EASY_BASE_SCORE, true, new QuestStateGreaterThanCondition("daily_item", 2, 49)));
		achievements.add(createAchievement("quest.special.daily_item.0100", "Ados' Supplier", "Finish daily item quest 100 times",
												Achievement.MEDIUM_BASE_SCORE, true, new QuestStateGreaterThanCondition("daily_item", 2, 99)));
		achievements.add(createAchievement("quest.special.daily_item.0250", "Ados' Stockpiler", "Finish daily item quest 250 times",
												Achievement.MEDIUM_BASE_SCORE, true, new QuestStateGreaterThanCondition("daily_item", 2, 249)));
		achievements.add(createAchievement("quest.special.daily_item.0500", "Ados' Hoarder", "Finish daily item quest 500 times",
												Achievement.HARD_BASE_SCORE, true, new QuestStateGreaterThanCondition("daily_item", 2, 499)));
		return achievements;
	}

}
