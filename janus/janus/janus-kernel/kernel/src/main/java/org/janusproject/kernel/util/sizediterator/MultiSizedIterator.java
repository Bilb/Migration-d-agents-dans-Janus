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
package org.janusproject.kernel.util.sizediterator;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A sized iterator that is based on the used of 
 * a collection of sized iterators.
 * 
 * @param <M> is the type of element.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiSizedIterator<M> implements SizedIterator<M> {

	private final LinkedList<SizedIterator<? extends M>> iterators = new LinkedList<SizedIterator<? extends M>>();
	private int total = 0;
	private int returned = 0;
	private SizedIterator<? extends M> iterator = null;
	private boolean update = false;
	private M next = null;
	
	/**
	 */
	public MultiSizedIterator() {
		//
	}
	
	/** Add an iterator in the list of iterators.
	 * 
	 * @param iterator
	 */
	public void addIterator(SizedIterator<? extends M> iterator) {
		if (this.iterators.add(iterator)) {
			this.total += iterator.totalSize();
			this.update = true;
		}
	}
	
	private void searchNext() {
		this.next = null;
		while ((this.iterator==null || !this.iterator.hasNext()) && (!this.iterators.isEmpty())) {
			this.iterator = this.iterators.removeFirst();
		}
		if (this.iterator!=null && this.iterator.hasNext()) {
			this.next = this.iterator.next();
		}
		this.update = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (this.next==null && this.update) searchNext();
		return this.next!=null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public M next() {
		if (this.next==null && this.update) searchNext();
		M v = this.next;
		if (v==null) throw new NoSuchElementException();
		++this.returned;
		searchNext();
		return v;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int totalSize() {
		return this.total;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rest() {
		return this.total - this.returned;
	}

}
