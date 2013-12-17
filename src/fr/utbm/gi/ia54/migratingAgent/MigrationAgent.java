package fr.utbm.gi.ia54.migratingAgent;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.status.Status;


public class MigrationAgent extends Agent{

	private long count = 0;




	/**
	 * 
	 */
	private static final long serialVersionUID = -7116894316743309806L;

	
	@Override
	public Status activate(Object... parameters) {
		return super.activate(parameters);
	}

	@Override
	public Status live() {

		if(this.count%60000 == 0) {
			System.out.println(this + " ---- Je suis sur:  " + getKernelContext().getKernelAgent()); //$NON-NLS-1$
		}
		if(!hasMigrated()) {
			if(this.count == 100000) {
				AgentAddress kernelDestination = Kernels.getOtherRandomKernel(getKernelContext().getKernelAgent());
				System.out.println("Demande de migration de " + getKernelContext().getKernelAgent() + " à " + kernelDestination); //$NON-NLS-1$ //$NON-NLS-2$
				migrate(kernelDestination);
			}
		}
		if(this.count == 400000) {
			System.out.println("Migration agent : suicide");  //$NON-NLS-1$
			killMe();
		}

		this.count++;
		return super.live();
	}



}
