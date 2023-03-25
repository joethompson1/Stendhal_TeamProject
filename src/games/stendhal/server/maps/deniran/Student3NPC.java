package games.stendhal.server.maps.deniran;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Map;

public class Student3NPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("James") {
			@Override
			protected void createPath() {
				// NPC does not move
				setPath(null);
			}
			
			@Override
			protected void createDialog() {
				addGreeting("Hiya!");
				addGoodbye("Farewell!");
			}
			
		};
		
	
	// This determines what the npc will look like
	npc.setEntityClass("boynpc");
	// set a description for when a player does 'Look'
	npc.setDescription("You see a casual, anxious looking man.");
	// Set the initial position to be the first node on the path you defined above
	npc.setPosition(4, 21);
	npc.initHP(100);
	zone.add(npc);
	}
}