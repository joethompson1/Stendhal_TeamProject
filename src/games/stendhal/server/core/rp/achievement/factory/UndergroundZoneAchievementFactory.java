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
//import games.stendhal.server.entity.npc.condition.PlayerVisitedZonesInRegionCondition;
/**
 * Factory for underground zone achievements
 *
 * @author madmetzger
 */
public class UndergroundZoneAchievementFactory extends AbstractAchievementFactory {

	@Override
	protected Category getCategory() {
		return Category.UNDERGROUND_ZONE;
	}

	@Override
	public Collection<Achievement> createAchievements() throws SAXException, URISyntaxException {
		//Collection<Achievement> list = new LinkedList<Achievement>();
		//All below ground achievements
		
		// Create the loader to load XML
		AchievementsXMLLoader loader = new AchievementsXMLLoader();
		
		// Load the Achievements
		List<Achievement> list = loader.load(new URI("/data/conf/achievements/undergroundzoneachievements.xml"));
		
		
		
		
//		list.add(createAchievement("zone.underground.semos", "Canary", "Visit all underground zones in the Semos region",
//									Achievement.MEDIUM_BASE_SCORE, true,
//									new PlayerVisitedZonesInRegionCondition("semos", Boolean.TRUE, Boolean.FALSE)));
//		list.add(createAchievement("zone.underground.nalwor", "Fear not drows nor hell", "Visit all underground zones in the Nalwor region",
//									Achievement.MEDIUM_BASE_SCORE, true,
//									new PlayerVisitedZonesInRegionCondition("nalwor", Boolean.TRUE, Boolean.FALSE)));
//		list.add(createAchievement("zone.underground.athor", "Labyrinth Solver", "Visit all underground zones in the Athor region",
//									Achievement.MEDIUM_BASE_SCORE, true,
//									new PlayerVisitedZonesInRegionCondition("athor", Boolean.TRUE, Boolean.FALSE)));
//		list.add(createAchievement("zone.underground.amazon", "Human Mole", "Visit all underground zones in the Amazon region",
//									Achievement.MEDIUM_BASE_SCORE, true,
//									new PlayerVisitedZonesInRegionCondition("amazon", Boolean.TRUE, Boolean.FALSE)));
//		list.add(createAchievement("zone.underground.ados", "Deep Dweller", "Visit all underground zones in the Ados region",
//									Achievement.MEDIUM_BASE_SCORE, true,
//									new PlayerVisitedZonesInRegionCondition("ados", Boolean.TRUE, Boolean.FALSE)));
		return list;
	}

}
