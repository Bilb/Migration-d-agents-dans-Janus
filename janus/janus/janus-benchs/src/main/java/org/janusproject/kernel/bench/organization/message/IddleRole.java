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
package org.janusproject.kernel.bench.organization.message;

import org.janusproject.kernel.bench.api.BenchUtil;
import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.status.Status;

/** Iddle role for benchs.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.5
 */
public class IddleRole extends Role {

	/**
	 */
	public IddleRole() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status activate(Object... params) {
		boolean sendingBenchmark = ((Boolean)params[0]).booleanValue();
		if (sendingBenchmark)
			setMailbox(BenchUtil.createMailboxForSendingBenchs());
		else 
			setMailbox(BenchUtil.createMailboxForReadingBenchs());
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status live() {
		return null;
	}
	
}