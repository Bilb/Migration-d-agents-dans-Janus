package org.janusproject.kernel.message;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;


/**
 * Class used to send an Agent to another agent.
 * @author Audric Ackermann
 *
 */
public class MigrationRequestMessage extends ObjectMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1445666706724835919L;
	
	
	/**
	 * the agent address associated with the agent currently 
	 * stored on this message
	 */
	private AgentAddress agentMigratingAddress = null;
	
	
	/**
	 * Creates a MigrationRequestMessage containing the specified agent
	 * @param agent the agent to send
	 */
	public MigrationRequestMessage(Agent agent) {
		super(agent);
		this.agentMigratingAddress = agent.getAddress();
	}

	
	/**
	 * @return the agent address associated with the agent currently 
	 * stored on this message
	 */
	public AgentAddress getAgentMigratingAddress() {
		return this.agentMigratingAddress;
	}
	
}
