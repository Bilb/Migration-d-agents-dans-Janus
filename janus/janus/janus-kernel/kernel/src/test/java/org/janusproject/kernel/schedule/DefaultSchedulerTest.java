/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2010-2011 Janus Core Developers
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
package org.janusproject.kernel.schedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.janusproject.kernel.schedule.ActivationStage;
import org.janusproject.kernel.util.directaccess.SafeIterator;
import org.janusproject.kernel.logger.LoggerUtil;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultSchedulerTest extends TestCase {

	private ActivatorStub a1;
	private ActivatorStub a2;
	private DefaultScheduler<ActivatorStub> activator;
	private List<ActivatorStub> stubs;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		LoggerUtil.setGlobalLevel(Level.OFF);
		this.activator = new DefaultScheduler<ActivatorStub>(ActivatorStub.class);
		this.a1 = new ActivatorStub(true);
		this.a2 = new ActivatorStub(false);
		this.stubs = Arrays.asList(this.a1, this.a2);
		this.activator.addAllActivators(this.stubs);
		this.activator.sync();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.activator = null;
		this.a1 = null;
		this.a2 = null;
		this.stubs = null;
		super.tearDown();
	}

	private static void assertSequence(Iterator<?> iterator, Object... objects) {
		assertNotNull(iterator);
		int i=0; 
		while(i<objects.length && iterator.hasNext()) {
			Object obj = iterator.next();
			assertEquals(objects[i], obj);
			++i;
		}
		assertTrue(i==objects.length);		
	}

	private static void assertEquals(Collection<?> c1, SafeIterator<?> c2) {
		if (c1==c2) return;
		if (c1!=null && c2!=null) {
			try {
				ArrayList<Object> obj = new ArrayList<Object>(c1);
				try {
					boolean failure = false;
					Object o1;
					while (c2.hasNext() && !failure) {
						o1 = c2.next();
						failure = !obj.remove(o1);
					}
					if (!failure && obj.isEmpty()) return;
				}
				finally {
					c2.release();
				}
			}
			catch(Throwable _) {
				//
			}
		}
		fail("collections are not equal. Expected: " //$NON-NLS-1$
				+((c1==null)?null:c1.toString())
				+"; Actual: " //$NON-NLS-1$
				+((c2==null)?null:c2.toString()));
	}

	private static void assertEmpty(SafeIterator<?> c1) {
		if (c1!=null) {
			if (!c1.hasNext()) return;
			fail("collections is not empty"); //$NON-NLS-1$
		}
		else {
			fail("collection is null"); //$NON-NLS-1$
		}
	}

	/**
	 */
	public void testCanActivateClass() {
		assertTrue(this.activator.canActivate(ActivatorStub.class));
		assertFalse(this.activator.canActivate(DefaultActivator.class));
	}

	/**
	 */
	public void testGetExecutionPolicyActivationStageCollection() {
		assertSequence(
				this.activator.getExecutionPolicy(ActivationStage.INITIALIZATION, this.stubs),
				this.a1, this.a2);
		assertSequence(
				this.activator.getExecutionPolicy(ActivationStage.LIVE, this.stubs),
				this.a1, this.a2);
		assertSequence(
				this.activator.getExecutionPolicy(ActivationStage.DESTRUCTION, this.stubs),
				this.a1, this.a2);
	}

	/**
	 */
	public void testInitObjectArray() {
		assertFalse(this.a1.isInit);
		assertFalse(this.a2.isInit);

		assertNull(this.activator.getInitParameters());
		Object o1 = new Object();
		Object o2 = new Object();
		assertNotNull(this.activator.activate(o1, o2));
		Object[] params = this.activator.getInitParameters();
		assertNotNull(params);
		assertEquals(2, params.length);
		assertSame(o1, params[0]);
		assertSame(o2, params[1]);
		
		assertTrue(this.a1.isInit);
		assertTrue(this.a2.isInit);
	}

	/**
	 */
	public void testBehaviour() {
		assertFalse(this.a1.isRun);
		assertFalse(this.a2.isRun);

		assertNotNull(this.activator.live());
		
		assertTrue(this.a1.isRun);
		assertTrue(this.a2.isRun);
	}

	/**
	 */
	public void testDestroy() {
		assertFalse(this.a1.isDestroy);
		assertFalse(this.a2.isDestroy);

		assertNotNull(this.activator.end());
		
		assertTrue(this.a1.isDestroy);
		assertTrue(this.a2.isDestroy);
	}
		
	/**
	 */
	public void testGetInitParameters() {
		assertNull(this.activator.getInitParameters());
		Object o1 = new Object();
		Object o2 = new Object();
		this.activator.activate(o1, o2);
		Object[] params = this.activator.getInitParameters();
		assertNotNull(params);
		assertEquals(2, params.length);
		assertSame(o1, params[0]);
		assertSame(o2, params[1]);
	}
	
	/** 
	 */
	public void testAddActivator() {
		ActivatorStub a3 = new ActivatorStub(true);
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.addActivator(a3);
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.sync();
		assertEquals(Arrays.asList(this.a1, this.a2, a3),
				this.activator.iterator());
	}

	/** 
	 */
	public void testAddAllActivators() {
		ActivatorStub a3 = new ActivatorStub(true);
		ActivatorStub a4 = new ActivatorStub(false);
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.addAllActivators(Arrays.asList(a3,a4));
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.sync();
		assertEquals(Arrays.asList(this.a1, this.a2, a3, a4),
				this.activator.iterator());
	}

	/** 
	 */
	public void testGetAllActivators() {
		assertEquals(this.stubs, this.activator.iterator());
	}

	/** 
	 */
	public void testRemoveActivator() {
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.removeActivator(this.a2);
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.sync();
		assertEquals(
				Collections.singleton(this.a1),
				this.activator.iterator());
		this.activator.removeActivator(this.a1);
		assertEquals(
				Collections.singleton(this.a1),
				this.activator.iterator());
		this.activator.sync();
		assertEquals(
				Collections.emptyList(),
				this.activator.iterator());
	}

	/** 
	 */
	public void testRemoveAllActivators() {
		this.activator.removeAllActivators();
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.sync();
		assertEmpty(this.activator.iterator());
	}

	/** 
	 */
	public void testRemoveAllActivatorsCollection() {
		ActivatorStub r3 = new ActivatorStub(true);
		this.activator.removeAllActivators(Arrays.asList(this.a1, r3));
		assertEquals(this.stubs, this.activator.iterator());
		this.activator.sync();
		assertEquals(
				Collections.singleton(this.a2),
				this.activator.iterator());
	}
	
	/**
	 */
	public void testHasActivable() {
		assertTrue(this.activator.hasActivable());

		this.activator.removeAllActivators();
		assertTrue(this.activator.hasActivable());
		this.activator.sync();
		assertFalse(this.activator.hasActivable());
		
		this.activator.addActivator(this.a1);
		assertFalse(this.activator.hasActivable()); // because the element is inside the pending list.
		this.activator.sync();
		assertTrue(this.activator.hasActivable()); // because the element was move from the pending list to the main list
		
		this.activator.removeAllActivators();
		assertTrue(this.activator.hasActivable()); // because the main list is not yet empty
		this.activator.sync();
		assertFalse(this.activator.hasActivable());
		
		this.activator.addActivator(this.a2);
		assertFalse(this.activator.hasActivable()); // because the element is inside the pending list.
		this.activator.sync();
		assertFalse(this.activator.hasActivable()); // because the element was move from the pending list to the main list
		
		this.activator.removeAllActivators();
		assertFalse(this.activator.hasActivable()); // because the main list is not yet empty
		this.activator.sync();
		assertFalse(this.activator.hasActivable());
	}

	/**
	 */
	public void testIsUsed() {
		assertTrue(this.activator.isUsed());

		this.activator.removeAllActivators();
		assertTrue(this.activator.isUsed());
		
		this.activator.addActivator(this.a1);
		assertTrue(this.activator.isUsed());
		
		this.activator.removeAllActivators();
		assertTrue(this.activator.isUsed());
		
		this.activator.activate();
		assertTrue(this.activator.isUsed());
		
		this.activator.addActivator(this.a2);
		assertTrue(this.activator.isUsed());
		
		this.activator.removeAllActivators();
		assertTrue(this.activator.isUsed());
	}

	/**
	 */
	public void testSize() {
		assertEquals(2, this.activator.size());
		this.activator.removeAllActivators();
		assertEquals(2, this.activator.size());
		this.activator.sync();
		assertEquals(0, this.activator.size());
	}

}
