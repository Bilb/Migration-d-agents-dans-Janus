/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2013 Janus Core Developers
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
package org.janusproject.kernel.network.fakenetwork;

import java.util.EventListener;

import org.janusproject.kernel.agent.AgentActivator;
import org.janusproject.kernel.agent.KernelAgent;
import org.janusproject.kernel.agent.KernelAgentFactory;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NetworkFakeKernelAgentFactory implements KernelAgentFactory {

	/**
	 */
	public NetworkFakeKernelAgentFactory() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KernelAgent newInstance(Boolean commitSuicide, AgentActivator activator, EventListener startUpListener, String applicationName) throws Exception {
		return new NetworkFakeKernelAgent(
				activator, commitSuicide, startUpListener, 
				applicationName,
				new NetworkFakeNetworkAdapter());
	}

}
