package org.janusproject.kernel.migrateagent;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.status.Status;


public class MigrationAgent extends Agent{

	public AgentAddress toMigrate;

	public int test = 5;

	public MigrationAgent() {
		
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
		this.toMigrate = Kernels.getOtherRandomKernel(getKernelContext().getKernelAgent());

		if(this.count%30000 == 0) {
			System.out.println(this + " ---- Je suis sur:  " + getKernelContext().getKernelAgent()); //$NON-NLS-1$ 
		}
		if(!hasMigrated()) {
			if(this.count == 2) {
				System.out.println("Demande de migration de " + getKernelContext().getKernelAgent() + " à " + this.toMigrate);//$NON-NLS-1$ //$NON-NLS-2$
				migrate(this.toMigrate);
			}
		}


		this.count++;

		return super.live();
	}



}
