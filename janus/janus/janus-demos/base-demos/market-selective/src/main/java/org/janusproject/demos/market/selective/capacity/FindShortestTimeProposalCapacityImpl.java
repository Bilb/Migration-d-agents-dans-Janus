/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2004-2010 Janus Core Developers
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
package org.janusproject.demos.market.selective.capacity;

import org.janusproject.kernel.crio.capacity.CapacityContext;
import org.janusproject.kernel.crio.capacity.CapacityImplementation;
import org.janusproject.kernel.crio.capacity.CapacityImplementationType;

/**
 * 
 * Atomic implementation of the capacity <code>FindShortestTimeProposalCapacity</code>
 * 
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FindShortestTimeProposalCapacityImpl
extends CapacityImplementation
implements FindShortestTimeProposalCapacity
{
	/**
	 */
	public FindShortestTimeProposalCapacityImpl() {
		super(CapacityImplementationType.DIRECT_ACTOMIC);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(CapacityContext call) throws Exception {
		Proposal p, best=null;
		for(Object o : call.getInputValues()) {
			p = (Proposal)o;
			if (p!=null) {
				if (best==null || p.getDuration() < best.getDuration()) {
					best = p;
				}
			}
		}
		
		if (best!=null)
			call.setOutputValues(best);
	}	
}
