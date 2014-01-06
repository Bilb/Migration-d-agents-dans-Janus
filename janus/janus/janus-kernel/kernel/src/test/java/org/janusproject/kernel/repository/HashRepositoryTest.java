/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2010-2012 Janus Core Developers
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
package org.janusproject.kernel.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.repository.RepositoryChangeEvent.ChangeType;

import junit.framework.TestCase;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HashRepositoryTest extends TestCase {

	private HashRepository<Object,Object> repository;
	private RepositoryChangeListenerStub listener;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		LoggerUtil.setGlobalLevel(Level.OFF);
		this.repository = new HashRepository<Object,Object>();
		this.listener = new RepositoryChangeListenerStub();
		this.repository.addRepositoryChangeListener(this.listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.repository.removeRepositoryChangeListener(this.listener);
		this.listener = null;
		this.repository = null;
		super.tearDown();
	}

	/**
	 */
	public void testSize() {
		assertEquals(0, this.repository.size());

		Object r = new Object();
		
		this.repository.add(new Object(), new Object());
		assertEquals(1, this.repository.size());
		
		this.repository.add(r, new Object());
		assertEquals(2, this.repository.size());
		
		this.repository.add(new Object(), new Object());
		assertEquals(3, this.repository.size());

		this.repository.remove(r);
		assertEquals(2, this.repository.size());

		this.repository.clear();
		assertEquals(0, this.repository.size());
	}

	/**
	 */
	public void testAddObjectObject() {
		Object r, v;
		
		r = new Object();
		v = new Object();
		this.repository.add(r, v);
		assertTrue(this.repository.contains(r));
		this.listener.assertEquals(
				this.repository,
				ChangeType.ADD,
				r,
				null,
				v);
		this.listener.assertNull();
		
		r = new Object();
		v = new Object();
		this.repository.add(r, v);
		assertTrue(this.repository.contains(r));
		this.listener.assertEquals(
				this.repository,
				ChangeType.ADD,
				r,
				null,
				v);
		this.listener.assertNull();

		Object v2 = new Object();
		this.repository.add(r, v2);
		assertTrue(this.repository.contains(r));
		this.listener.assertEquals(
				this.repository,
				ChangeType.UPDATE,
				r,
				v,
				v2);
		this.listener.assertNull();
	}

	/**
	 */
	public void testClear() {
		assertEquals(0, this.repository.size());

		Object r1 = new Object();
		Object v1 = new Object();
		Object r2 = new Object();
		Object v2 = new Object();
		Object r3 = new Object();
		Object v3 = new Object();
		
		this.repository.add(r1, v1);
		this.repository.add(r2, v2);
		this.repository.add(r3, v3);
		this.listener.reset();
		assertEquals(3, this.repository.size());

		this.repository.clear();
		assertEquals(0, this.repository.size());
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				r1, v1, null);
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				r2, v2, null);
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				r3, v3, null);
		this.listener.assertNull();
	}

	/**
	 */
	public void testGetEntryIterator() {
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		
		this.repository.add(k1, new Object());
		this.repository.add(k2, new Object());
		this.repository.add(k3, new Object());
		
		Iterator<Entry<Object,Object>> iterator = this.repository.getEntryIterator();
		assertNotNull(iterator);
		
		ArrayList<Object> l = new ArrayList<Object>();
		while (iterator.hasNext()) {
			l.add(iterator.next().getKey());
		}
		
		assertEquals(3, l.size());
		assertTrue(l.remove(k1));
		assertTrue(l.remove(k2));
		assertTrue(l.remove(k3));
		assertTrue(l.isEmpty());
	}

	/**
	 */
	public void testRemoveObject() {
		Object k1 = new Object();
		Object v1 = new Object();
		Object k2 = new Object();
		Object v2 = new Object();
		Object k3 = new Object();
		Object v3 = new Object();
		Object k4 = new Object();
		
		this.repository.add(k1, v1);
		this.repository.add(k2, v2);
		this.repository.add(k3, v3);
		this.listener.reset();
		
		assertTrue(this.repository.contains(k1));
		assertTrue(this.repository.contains(k2));
		assertTrue(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		
		assertSame(v2, this.repository.remove(k2));

		assertTrue(this.repository.contains(k1));
		assertFalse(this.repository.contains(k2));
		assertTrue(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				k2, v2, null);
		this.listener.assertNull();

		assertSame(v3, this.repository.remove(k3));

		assertTrue(this.repository.contains(k1));
		assertFalse(this.repository.contains(k2));
		assertFalse(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				k3, v3, null);
		this.listener.assertNull();

		assertNull(this.repository.remove(k4));

		assertTrue(this.repository.contains(k1));
		assertFalse(this.repository.contains(k2));
		assertFalse(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		this.listener.assertNull();

		assertSame(v1, this.repository.remove(k1));

		assertFalse(this.repository.contains(k1));
		assertFalse(this.repository.contains(k2));
		assertFalse(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		this.listener.assertEquals(
				this.repository,
				ChangeType.REMOVE,
				k1, v1, null);
		this.listener.assertNull();

		assertNull(this.repository.remove(k1));

		assertFalse(this.repository.contains(k1));
		assertFalse(this.repository.contains(k2));
		assertFalse(this.repository.contains(k3));
		assertFalse(this.repository.contains(k4));
		this.listener.assertNull();
	}

	/**
	 */
	public void testGet() {
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		Object k4 = new Object();
		Object v1 = new Object();
		Object v2 = new Object();
		Object v3 = new Object();
		
		this.repository.add(k1, v1);
		this.repository.add(k2, v2);
		this.repository.add(k3, v3);
		
		assertSame(v1, this.repository.get(k1));
		assertSame(v2, this.repository.get(k2));
		assertSame(v3, this.repository.get(k3));
		assertNull(this.repository.get(k4));

		assertNull(this.repository.get(v1));
		assertNull(this.repository.get(v2));
		assertNull(this.repository.get(v3));
	}
	
	/**
	 */
	public void testIdentifiers() {
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		
		this.repository.add(k1, new Object());
		this.repository.add(k2, new Object());
		this.repository.add(k3, new Object());
		
		Collection<Object> identifiers = this.repository.identifiers();
		assertNotNull(identifiers);
		
		ArrayList<Object> l = new ArrayList<Object>(identifiers);
		
		assertEquals(3, l.size());
		assertTrue(l.remove(k1));
		assertTrue(l.remove(k2));
		assertTrue(l.remove(k3));
		assertTrue(l.isEmpty());
	}

	/**
	 */
	public void testValues() {
		Object k1 = new Object();
		Object k2 = new Object();
		Object k3 = new Object();
		Object v1 = new Object();
		Object v2 = new Object();
		Object v3 = new Object();
		
		this.repository.add(k1, v1);
		this.repository.add(k2, v2);
		this.repository.add(k3, v3);
		
		Collection<Object> values = this.repository.values();
		assertNotNull(values);
		
		ArrayList<Object> l = new ArrayList<Object>(values);
		
		assertEquals(3, l.size());
		assertTrue(l.remove(v1));
		assertTrue(l.remove(v2));
		assertTrue(l.remove(v3));
		assertTrue(l.isEmpty());
	}

}
