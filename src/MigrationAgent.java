import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.status.Status;


public class MigrationAgent extends Agent{

	private boolean migrateAsked = false;

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
		if(this.migrateAsked== false) {
			AgentAddress destination = getKernelContext().getKernelAgent(); //same kernel
			
			migrate(destination);
			this.migrateAsked = true;
		}
		return super.live();
	}


}
