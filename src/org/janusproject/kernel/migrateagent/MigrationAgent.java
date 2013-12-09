package org.janusproject.kernel.migrateagent;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;


public class MigrationAgent extends Agent{

	//TODO
	private int test = 182;

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

		if(this.count< 10) {
			System.out.println("" + this.count + "  "+ getAddress() + "" +" Je suis sur " + getKernelContext().getKernelAgent());  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			
			if(this.count == 9) {
				//AgentAddress destination = getKernelContext().getKernelAgent(); //same kernel
				
				System.out.println(""+this.count+ " demande de migration de " + getKernelContext().getKernelAgent() + " à " + this.toMigrate); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				migrate(this.toMigrate);
			}
		}
		
		if(this.count%3000 == 0) {
			System.out.println("current kernel  " + getKernelContext().getKernelAgent()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		
		this.count++;

		/*SizedIterator<AgentAddress> it = getKernelContext().getKernel().getRemoteKernels();
		AgentAddress address;
		System.out.println("size:" + it.rest());
		while(it.hasNext()) {
			address = it.next();
			System.out.println("address :" +address.getUUID());
		}*/


		return super.live();
	}


	
	public int getTest() {
		return test;
	}
}
