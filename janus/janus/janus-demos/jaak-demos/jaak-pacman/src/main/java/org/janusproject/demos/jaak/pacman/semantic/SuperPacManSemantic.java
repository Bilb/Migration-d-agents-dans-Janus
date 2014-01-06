/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2011 Janus Core Developers
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
package org.janusproject.demos.jaak.pacman.semantic;

/** This class defines a pacman.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SuperPacManSemantic implements Cloneable {
	
	/**
	 * Semantic singleton.
	 */
	public static SuperPacManSemantic SEMANTIC = new SuperPacManSemantic();

	/**
	 */
	private SuperPacManSemantic() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SuperPacManSemantic clone() {
		try {
			return (SuperPacManSemantic)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof SuperPacManSemantic);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return SuperPacManSemantic.class.hashCode();
	}

}