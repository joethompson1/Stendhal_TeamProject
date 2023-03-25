package games.stendhal.server.maps.deniran;

import games.stendhal.server.core.config.ZoneConfigurator;
//import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.core.pathfinder.Node;
//import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.entity.npc.SpeakerNPC;

//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
import java.util.Map;


public class LonJathamNPC implements ZoneConfigurator {
	
	/**
	 * Configure a zone
	 * 
	 * @param zone The zone to be configured
	 * @param attributes Configuration attributes
	 * 
	 */
	
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Lon Jatham") {
			@Override
			protected void createPath() {
				// NPC does not move
				setPath(null);
			}
			
			@Override
			protected void createDialog() {
				addGreeting("Hello folks!");
				addGoodbye();
			}
			
		};
		
		
		// This determines what the npc will look like
		npc.setEntityClass("wisemannpc");
		// set a description for when a player does 'Look'
		npc.setDescription("You see Mr Lon Jatam, he looks a bit busy at the moment but perhaps you can help him out");
		// Set the initial position to be the first node on the path you defined above
		npc.setPosition(16, 8);
		npc.initHP(100);
		zone.add(npc);
		
	}
}