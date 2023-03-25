package games.stendhal.server.core.rp.achievement.factory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
//import java.util.LinkedList;
import java.util.List;

import org.xml.sax.SAXException;

import games.stendhal.server.core.config.AchievementsXMLLoader;
import games.stendhal.server.core.rp.achievement.Achievement;
import games.stendhal.server.core.rp.achievement.Category;
//import games.stendhal.server.entity.npc.condition.QuestStateGreaterThanCondition;

public class KirdnehItemAchievementFactory extends AbstractAchievementFactory {

	@Override
	protected Category getCategory() {
		return Category.QUEST_KIRDNEH_ITEM;
	}

	@Override
	public Collection<Achievement> createAchievements() throws SAXException, URISyntaxException {
		//List<Achievement> achievements = new LinkedList<Achievement>();
		
		AchievementsXMLLoader loader = new AchievementsXMLLoader();
		
		List<Achievement> achievements = loader.load(new URI ("/data/conf/achievements/kirdnehitemachievements.xml"));
		
//		achievements.add(createAchievement("quest.special.weekly_item.0005", "Archaeologist", "Finish weekly item quest 5 times",
//				Achievement.HARD_BASE_SCORE, true, new QuestStateGreaterThanCondition("weekly_item", 2, 4)));
//		achievements.add(createAchievement("quest.special.weekly_item.0025", "Dedicated Archaeologist", "Finish weekly item quest 25 times",
//				Achievement.HARD_BASE_SCORE, true, new QuestStateGreaterThanCondition("weekly_item", 2, 24)));
//		achievements.add(createAchievement("quest.special.weekly_item.0050", "Senior Archaeologist", "Finish weekly item quest 50 times",
//				Achievement.HARD_BASE_SCORE, true, new QuestStateGreaterThanCondition("weekly_item", 2, 49)));
//		achievements.add(createAchievement("quest.special.weekly_item.0100", "Master Archaeologist", "Finish weekly item quest 100 times",
//				Achievement.HARD_BASE_SCORE, true, new QuestStateGreaterThanCondition("weekly_item", 2, 99)));
		return achievements;
	}

}
