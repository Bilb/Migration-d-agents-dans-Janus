import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.network.jxse.agent.JxtaJxseKernelAgentFactory;


public class Launcher {

	public static void main(String[] args) {

		Kernels.setPreferredKernelFactory(new JxtaJxseKernelAgentFactory());
		Kernel kernel = Kernels.get(true, "test Platofrm janus network"); //$NON-NLS-1$
		
		
		MigrationAgent agent = new MigrationAgent();
		kernel.launchLightAgent(agent,"agent1"); //$NON-NLS-1$
	}
}
