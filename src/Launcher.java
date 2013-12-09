import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.migrateagent.MigrationAgent;
import org.janusproject.kernel.network.NetworkAdapter;
import org.janusproject.kernel.network.jxse.agent.JxtaJxseKernelAgent;
import org.janusproject.kernel.network.jxse.agent.JxtaJxseKernelAgentFactory;
import org.janusproject.kernel.util.sizediterator.SizedIterator;


public class Launcher  {

	
	public static void main(String[] args) {

		Kernels.setPreferredKernelFactory(new JxtaJxseKernelAgentFactory());
		Kernel kernel = Kernels.get(true, "test Platform janus network"); //$NON-NLS-1$
		
		Kernel kernel2 = Kernels.create(true, "test Platform janus network2"); //$NON-NLS-1$
		
		
		MigrationAgent agent = new MigrationAgent(kernel2.getAddress());
		kernel.launchLightAgent(agent,"agent1"); //$NON-NLS-1$
		
	}

	
	
	
	
	
	
	
	
}
