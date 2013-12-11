package org.janusproject.kernel.migrateagent;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;


public class MigrationAgent extends Agent{

	private AgentAddress toMigrate;

	public MigrationAgent(AgentAddress toMigrate) {
		this.toMigrate = toMigrate;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -7116894316743309806L;

	int count = 0;
	@Override
	public Status activate(Object... parameters) {
		return super.activate(parameters);
	}

	@Override
	public Status live() {

		if(this.count%300 == 0) {
			System.out.println("Je suis sur:  " + getKernelContext().getKernelAgent()); //$NON-NLS-1$ 
		}

		if(this.count == 2) {
			System.out.println("Demande de migration de " + getKernelContext().getKernelAgent() + " à " + this.toMigrate);//$NON-NLS-1$ //$NON-NLS-2$ 
			migrate(this.toMigrate);
		}
		this.count++;

		return super.live();
	}



}
