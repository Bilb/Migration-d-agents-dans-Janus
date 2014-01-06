/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2012 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.janusproject.demos.bdi.market.simple.message;

import org.janusproject.demos.bdi.market.simple.travel.TravelDestination;
import org.janusproject.demos.bdi.market.simple.travel.TravelSelectionCritera;
import org.janusproject.kernel.message.ObjectMessage;


/**
 * Message that contains a travel query.
 * 
 * @author $Author: mbrigaud$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public class TravelRequestMessage extends ObjectMessage {

	private static final long serialVersionUID = 812286688821571799L;
	
	private final TravelSelectionCritera critera;
	
	/**
	 * @param destination
	 * @param critera
	 */
	public TravelRequestMessage(TravelDestination destination, TravelSelectionCritera critera) {
		super(destination);
		this.critera = critera;
	}
	
	/** Replies the travel destination.
	 * 
	 * @return the travel destination.
	 */
	public TravelDestination getDestination() {
		return (TravelDestination)getContent();
	}
	
	/** Replies the expected selection critera.
	 * 
	 * @return the expected selection critera.
	 */
	public TravelSelectionCritera getCritera() {
		return this.critera;
	}
		
}
