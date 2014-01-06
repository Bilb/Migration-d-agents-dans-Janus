package org.janusproject.kernel.message;

import org.janusproject.kernel.address.AgentAddress;



/**
 * 
 * Message used to exchange informations of an agent's migration
 * k1 : kernel origin
 * k2 : kernel destination
 * a : agent
 * 
 * 1. k1 send a MigrationRequestMessage to k2 with agent a as content
 * 2. k2 receive the request, extract agent. 
 * 					If no error, send OK to k1
 * 					If error, send ERROR to k2
 * 3. 	k1 receive OK : delete agent from its agents and send an ACK
 * 		k1 receive ERROR : relaunch agent on k1 because of failure of migration
 * @author Audric Ackermann
 *
 */
public class MigrationStatusMessage extends ObjectMessage {
	
	/**
	 * Represents the possible state of a migrationStatusMessage
	 * OK : agent has been received
	 * ERROR : error while receiving or deserializing agent
	 * ACK : Acknowledge when agent has been successfully transfer
	 * @author Audric Ackermann
	 *
	 */
	public enum MigrationStatus {
		OK, ERROR, ACK
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1315269695830080845L;

	
	/**
	 * Represents the state of the migration of which we
	 * are talking about
	 */
	private final MigrationStatus status;
	
	/**
	 * 
	 * @param migratingAgentAddress the agent which is migrating.
	 * Used to track status of a migration of different agents in parrallel
	 * 
	 */
	public MigrationStatusMessage(AgentAddress migratingAgentAddress, MigrationStatus migrationStatus) {
		super(migratingAgentAddress);
		this.status = migrationStatus;
	}
	
	
	/**
	 * @return the agent of which this migration message is about
	 */
	public AgentAddress getAgentMigratingAddress() {
		return ((AgentAddress) getContent());
	}
	
	
	public MigrationStatus getStatus() {
		return this.status;
	}


}
