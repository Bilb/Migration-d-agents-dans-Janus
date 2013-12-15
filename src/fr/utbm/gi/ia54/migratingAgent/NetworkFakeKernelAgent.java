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
package fr.utbm.gi.ia54.migratingAgent;

import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.janusproject.kernel.address.Address;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.AgentActivator;
import org.janusproject.kernel.agent.KernelAgent;
import org.janusproject.kernel.configuration.JanusProperties;
import org.janusproject.kernel.configuration.JanusProperty;
import org.janusproject.kernel.crio.core.GroupAddress;
import org.janusproject.kernel.crio.core.Organization;
import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.crio.core.RoleAddress;
import org.janusproject.kernel.crio.organization.Group;
import org.janusproject.kernel.crio.organization.GroupCondition;
import org.janusproject.kernel.crio.organization.MembershipService;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.network.NetworkListener;
import org.janusproject.kernel.repository.RepositoryChangeEvent;
import org.janusproject.kernel.repository.RepositoryChangeEvent.ChangeType;
import org.janusproject.kernel.repository.RepositoryChangeListener;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.util.throwable.Throwables;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class NetworkFakeKernelAgent extends KernelAgent implements RepositoryChangeListener, NetworkListener {

	private static final long serialVersionUID = -7039552210031205196L;

	/**
	 * Delay between two tries to remove the no-more-used groups (in minutes).
	 */
	public static final long EMPTY_GROUP_CLEANING_DELAY = 10;
	
	private final NetworkFakeNetworkAdapter adapter;

	/**
	 * Create a kernel agent with the default settings.
	 * 
	 * @param activator
	 *            is the agent activator to use.
	 * @param commitSuicide
	 *            indicates if this agent is able to commit suicide or not
	 * @param startUpListener
	 *            is a listener on kernel events which may be added at startup.
	 * @param applicationName
	 *            is the name of the application supported by this kernel.
	 * @param networkAdapter
	 *            is the adapter used by this kernel to be connected through a network.
	 */
	NetworkFakeKernelAgent(AgentActivator activator, Boolean commitSuicide, EventListener startUpListener, String applicationName, NetworkFakeNetworkAdapter networkAdapter) {
		super(activator, commitSuicide, null, startUpListener, networkAdapter, applicationName);
		this.adapter = networkAdapter;
		this.adapter.setNetworkAdapterListener(this);

		JanusProperties prop = getKernelContext().getProperties();

		prop.setProperty(JanusProperty.GROUP_PERSISTENCE, true);
		this.adapter.setJanusProperties(prop);

		try {
			this.adapter.initializeNetwork(getKernelContext().getKernelAgent(), getKernelContext().getProperties());
		}
		catch (AssertionError ae) {
			throw ae;
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
		getGroupRepository().addRepositoryChangeListener(this);
		getAgentRepository().addRepositoryChangeListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status end() {
		getGroupRepository().removeRepositoryChangeListener(this);
		return super.end();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void repositoryChanged(RepositoryChangeEvent evt) {
		Logger logger = getLogger();
		if (evt.getType() == ChangeType.ADD) {
			if (evt.getChangedObject() instanceof GroupAddress) {
				try {
					GroupAddress group = (GroupAddress) evt.getChangedObject();					
					Group groupDescription = getGroupObject(group);
					this.adapter.informLocalGroupCreated(group,groupDescription.getObtainConditions(),groupDescription.getLeaveConditions(),groupDescription.getMembership());
				}
				catch (AssertionError ae) {
					throw ae;
				}
				catch (Exception e) {
					logger.fine(Throwables.toString(e));
				}
			}
			else if (evt.getChangedObject() instanceof AgentAddress) {
				AgentAddress adr = (AgentAddress) evt.getChangedObject();					
				this.adapter.informLocalAgentAdded(adr);
			}
		}
		else if (evt.getType() == ChangeType.REMOVE) {
			if (evt.getChangedObject() instanceof GroupAddress) {
				try {
					GroupAddress group = (GroupAddress) evt.getChangedObject();					
					this.adapter.informLocalGroupRemoved(group);
				}
				catch (AssertionError ae) {
					throw ae;
				}
				catch (Exception e) {
					logger.fine(Throwables.toString(e));
				}
			}
			else if (evt.getChangedObject() instanceof AgentAddress) {
				AgentAddress adr = (AgentAddress) evt.getChangedObject();					
				this.adapter.informLocalAgentRemoved(adr);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.janusproject.kernel.network.api.NetworkAdapterListener# distantGroupDiscovered(java.lang.Class, java.util.UUID)
	 */
	@Override
	public void distantGroupDiscovered(Class<? extends Organization> organization, UUID id, Collection<? extends GroupCondition> obtainConditions, Collection<? extends GroupCondition> leaveConditions, MembershipService membership, boolean persistent, String groupName) {
		getOrCreateGroup(id, organization, obtainConditions, leaveConditions, membership, true, persistent, groupName);
	}

	/** {@inheritDoc}
	 */
	@Override
	public RoleAddress receiveOrganizationalDistantMessage(GroupAddress group, Class<? extends Role> receiverRole, Message message, boolean isBroadcast) {
		if (isBroadcast) {
			forwardBroadcastMessage(message);
			return null;
		}
		Address a = forwardMessage(message);
		assert(a instanceof RoleAddress);
		return (RoleAddress)a;
	}

	/** {@inheritDoc}
	 */
	@Override
	public AgentAddress receiveAgentAgentDistantMessage(Message message, boolean isBroadcast) {
		if (isBroadcast) {
			forwardBroadcastMessage(message);
			return null;
		}
		Address a = forwardMessage(message);
		assert(a instanceof AgentAddress);
		return (AgentAddress)a;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void networkError(Throwable e) {
		if (fireUncatchedException(e)) {
			getLogger().log(Level.SEVERE, e.toString(), e);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void networkLog(String message) {
		getLogger().log(Level.INFO, message);
	}
	
	RoleAddress getRolePlayerIn(GroupAddress adr) {
		Iterator<RoleAddress> iterator = getRoleAddressesInGroup(adr);
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	
}