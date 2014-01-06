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

/** Exception when using a probe.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProbeException extends Exception {

	private static final long serialVersionUID = -2582901114568728622L;

	/**
	 * @param probeName is the name of the probe.
	 */
	public ProbeException(String probeName) {
		super(probeName);
	}
	
	/**
	 * @param probeName is the name of the probe.
	 * @param e
	 */
	public ProbeException(String probeName, Throwable e) {
		super(probeName, e);
	}

	/**
	 */
	public ProbeException() {
		super();
	}

}