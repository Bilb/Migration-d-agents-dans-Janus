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
package org.janusproject.kernel.network.fakenetwork;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.configuration.JanusProperties;
import org.janusproject.kernel.crio.core.GroupAddress;
import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.crio.core.RoleAddress;
import org.janusproject.kernel.crio.organization.GroupCondition;
import org.janusproject.kernel.crio.organization.MembershipService;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.network.NetworkAdapter;
import org.janusproject.kernel.network.NetworkListener;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.util.sizediterator.SizedIterator;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NetworkFakeNetworkAdapter implements NetworkAdapter {
	
	/** List of all the network adapters on the fake network.
	 */
	static final Set<NetworkFakeNetworkAdapter> ALL_ADAPTERS = new TreeSet<NetworkFakeNetworkAdapter>(
			new Comparator<NetworkFakeNetworkAdapter>() {
				@SuppressWarnings("null")
				@Override
				public int compare(
						NetworkFakeNetworkAdapter o1,
						NetworkFakeNetworkAdapter o2) {
					if (o1==o2) return 0;
					if (o1==null && o2!=null) return -1;
					if (o1!=null && o2==null) return 1;
					assert(o1!=null && o2!=null);
					AgentAddress a1 = o1.getKernelAddress();
					AgentAddress a2 = o2.getKernelAddress();
					if (a1==a2) return 0;
					if (a1!=null) return a1.compareTo(a2);
					assert(a2!=null);
					return -a2.compareTo(a1);
				}
			});

	private AgentAddress localKernel = null;
	private NetworkListener localListener = null;
	
	
	/**
	 */
	public NetworkFakeNetworkAdapter() {
		//
	}
	
	private NetworkFakeKernelAgent getKernel() {
		return (NetworkFakeKernelAgent)this.localListener;
	}
		
	/** Replies the address of the kernel agent which is running
	 * the network support.
	 * 
	 * @return the address of the kernel agent.
	 */
	public AgentAddress getKernelAddress() {
		return this.localKernel;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setNetworkAdapterListener(NetworkListener listener) {
		this.localListener = listener;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setJanusProperties(JanusProperties properties) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void initializeNetwork(
			AgentAddress kernelAddress,
			JanusProperties properties) throws Exception {
		this.localKernel = kernelAddress;
		synchronized(NetworkFakeNetworkAdapter.class) {
			ALL_ADAPTERS.add(this);
		}
		this.localListener.networkLog("Network node launched"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public void shutdownNetwork() throws Exception {
		synchronized(NetworkFakeNetworkAdapter.class) {
			ALL_ADAPTERS.remove(this);
		}
		this.localListener.networkLog("Network node destroyed"); //$NON-NLS-1$
	}

	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<AgentAddress> getRemoteKernels() {
		return new KernelIterator(this.localKernel);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void informLocalRoleTaken(
			GroupAddress groupAddress,
			Class<? extends Role> role,
			AgentAddress agentAddress) {
		// Do nothing special
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void informLocalRoleReleased(GroupAddress groupAddress,
			Class<? extends Role> role, AgentAddress agentAddress) {
		// Do nothing special
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Status informLocalGroupCreated(GroupAddress ga,
			Collection<? extends GroupCondition> obtainConditions,
			Collection<? extends GroupCondition> leaveConditions,
			MembershipService membership) {
		this.localListener.networkLog("Notifying group creation"); //$NON-NLS-1$
		synchronized(NetworkFakeNetworkAdapter.class) {
			for(NetworkFakeNetworkAdapter adapter : ALL_ADAPTERS) {
				if (adapter!=this) {
					adapter.onDistantGroupCreated(ga, obtainConditions, leaveConditions, membership);
				}
			}
		}
		return null;
	}
	
	private void onDistantGroupCreated(GroupAddress ga,
			Collection<? extends GroupCondition> obtainConditions,
			Collection<? extends GroupCondition> leaveConditions,
			MembershipService membership) {
		this.localListener.networkLog("Group "+ga+" was created on distant kernel"); //$NON-NLS-1$ //$NON-NLS-2$
		this.localListener.distantGroupDiscovered(
				ga.getOrganization(),
				ga.getUUID(), 
				obtainConditions, leaveConditions,
				membership,
				false,
				ga.getName());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Status informLocalGroupRemoved(GroupAddress ga) {
		// Do nothing special
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void informLocalAgentAdded(AgentAddress agentAddress) {
		// Do nothing special
	}

	/** {@inheritDoc}
	 */
	@Override
	public void informLocalAgentRemoved(AgentAddress agentAddress) {
		// Do nothing special
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isRemoteAddress(
			GroupAddress groupAddress,
			AgentAddress address) {
		synchronized(NetworkFakeNetworkAdapter.class) {
			for(NetworkFakeNetworkAdapter adapter : ALL_ADAPTERS) {
				if (adapter!=this &&
					getKernel().isMemberOf(address, groupAddress)) {
					return true;
				}
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public RoleAddress getRemoteAddress(GroupAddress groupAddress) {
		synchronized(NetworkFakeNetworkAdapter.class) {
			for(NetworkFakeNetworkAdapter adapter : ALL_ADAPTERS) {
				if (adapter!=this) {
					RoleAddress a = getKernel().getRolePlayerIn(groupAddress);
					if (a!=null) return a;
				}
			}
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Address sendMessage(Message message) {
		try {
			this.localListener.networkLog("Send message on network: "+message); //$NON-NLS-1$
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			baos.close();
			Address receiver = null;
			synchronized(NetworkFakeNetworkAdapter.class) {
				for(NetworkFakeNetworkAdapter adapter : ALL_ADAPTERS) {
					if (adapter!=this) {
						receiver = adapter.onReceiveMessage(baos.toByteArray());
						if (receiver!=null) return receiver;
					}
				}
			}
		}
		catch (Throwable e) {
			this.localListener.networkError(e);
		}
		return null;
	}
	
	private Address onReceiveMessage(byte[] bytes) {
		try {
			this.localListener.networkLog("Receiving message from network"); //$NON-NLS-1$
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			bais.close();
			Message msg = (Message)o;
			Address adr = msg.getSender();
			if (adr instanceof RoleAddress) {
				RoleAddress ra = (RoleAddress)adr;
				return this.localListener.receiveOrganizationalDistantMessage(
						ra.getGroup(),
						((RoleAddress)msg.getReceiver()).getRole(),
						msg,
						false);
			}
			return this.localListener.receiveAgentAgentDistantMessage(msg, false);
		}
		catch (Throwable e) {
			this.localListener.networkError(e);
		}
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void broadcastMessage(Message message) {
		try {
			this.localListener.networkLog("Send message on network: "+message); //$NON-NLS-1$
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			baos.close();
			synchronized(NetworkFakeNetworkAdapter.class) {
				for(NetworkFakeNetworkAdapter adapter : ALL_ADAPTERS) {
					if (adapter!=this) {
						adapter.onBroadcastMessage(baos.toByteArray());
					}
				}
			}
		}
		catch (Throwable e) {
			this.localListener.networkError(e);
		}
	}
	
	private void onBroadcastMessage(byte[] bytes) {
		try {
			this.localListener.networkLog("Receiving message from network"); //$NON-NLS-1$
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			bais.close();
			Message msg = (Message)o;
			Address adr = msg.getSender();
			if (adr instanceof RoleAddress) {
				RoleAddress ra = (RoleAddress)adr;
				this.localListener.receiveOrganizationalDistantMessage(
						ra.getGroup(),
						((RoleAddress)msg.getReceiver()).getRole(),
						msg,
						true);
			}
			else {
				this.localListener.receiveAgentAgentDistantMessage(msg, true);
			}
		}
		catch (Throwable e) {
			this.localListener.networkError(e);
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class KernelIterator implements SizedIterator<AgentAddress> {
		
		private final AgentAddress localKernel;
		private final Iterator<NetworkFakeNetworkAdapter> adapters;
		private AgentAddress next;
		private int index = 0;
		
		public KernelIterator(AgentAddress a) {
			this.localKernel = a;
			synchronized(NetworkFakeNetworkAdapter.class) {
				this.adapters = ALL_ADAPTERS.iterator();
			}
			searchNext();
		}
		
		private void searchNext() {
			this.next = null;
			while (this.next==null && this.adapters.hasNext()) {
				NetworkFakeNetworkAdapter adapter = this.adapters.next();
				AgentAddress a = adapter.getKernelAddress();
				assert(a!=null);
				if (this.localKernel==null || !this.localKernel.equals(a)) {
					this.next = a;
				}
				++this.index;
			}
		}

		/** {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/** {@inheritDoc}
		 */
		@Override
		public AgentAddress next() {
			if (this.next==null) throw new NoSuchElementException();
			AgentAddress a = this.next;
			searchNext();
			return a;
		}

		/** {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/** {@inheritDoc}
		 */
		@Override
		public int totalSize() {
			return ALL_ADAPTERS.size();
		}

		/** {@inheritDoc}
		 */
		@Override
		public int rest() {
			return totalSize() - this.index;
		}
		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Network node for "+getKernelAddress(); //$NON-NLS-1$
	}

}
