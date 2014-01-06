/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2004-2011 Janus Core Developers
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
package org.janusproject.kernel.probe;

/** A watchable object is able to create and destroy a probe instance.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.5
 */
public interface WatchableObject {

	/** Create a probe instance.
	 * 
	 * @param probeType is the type of the probe.
	 * @return the probe instance.
	 * @throws ProbeCreationException
	 */
	public <T extends Probe> T createProbe(Class<T> probeType) throws ProbeCreationException;

	/** Release a probe instance.
	 * 
	 * @param probe is the probe to release.
	 */
	public void releaseProbe(Probe probe);

}