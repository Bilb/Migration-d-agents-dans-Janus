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
package org.janusproject.groovyengine;

import javax.script.ScriptEngineManager;

import org.janusproject.scriptedagent.ScriptedAgent;

import junit.framework.TestCase;

/**
 * @author $Author: sgalland$
 */
public class IndependentEngineTest extends TestCase {

	private ScriptEngineManager scriptManager;
	private GroovyExecutionContext context1;
	private GroovyExecutionContext context2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.scriptManager = ScriptedAgent.getSharedScriptEngineManager();
		this.context1 = new GroovyExecutionContext(this.scriptManager);
		this.context2 = new GroovyExecutionContext(this.scriptManager);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		this.context1 = this.context2 = null;
		this.scriptManager = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testAgentSeperationConcern() {
		assertTrue(this.context1.isAgentSeparationCompliant());
		assertTrue(this.context2.isAgentSeparationCompliant());
	}
	
	/**
	 * @throws Exception
	 */
	public void testGlobalIndependancy() throws Exception {
		this.context1.runCommand("a = 18"); //$NON-NLS-1$
		this.context2.runCommand("a = 34"); //$NON-NLS-1$
		
		assertEquals(18, this.context1.getGlobalValue("a")); //$NON-NLS-1$
		assertEquals(34, this.context2.getGlobalValue("a")); //$NON-NLS-1$
		
		this.context2.runCommand("a = 45"); //$NON-NLS-1$

		assertEquals(18, this.context1.getGlobalValue("a")); //$NON-NLS-1$
		assertEquals(45, this.context2.getGlobalValue("a")); //$NON-NLS-1$
	}
		
}
