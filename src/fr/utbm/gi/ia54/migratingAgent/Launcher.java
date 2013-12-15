package fr.utbm.gi.ia54.migratingAgent;

import java.util.Set;
import java.util.logging.Level;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.migrateagent.MigrationAgent;


public class Launcher  {

	private static final String APPLICATION_NAME = "kernel"; //$NON-NLS-1$
	
	public static void main(String[] args) {

		LoggerUtil.setGlobalLevel(Level.INFO);
		
		Kernels.setPreferredKernelFactory(new NetworkFakeKernelAgentFactory());
		Kernel kernel = Kernels.create(false, APPLICATION_NAME);
		
		Kernel kernel2 = Kernels.create(false, APPLICATION_NAME);
		
		Set<AgentAddress> kernels = Kernels.getKernels();
		
		for (AgentAddress agentAddress : kernels) {
			System.out.println("kernel : " + agentAddress); //$NON-NLS-1$
		}
		
		
		MigrationAgent agent = new MigrationAgent(kernel2.getAddress());
		kernel.launchLightAgent(agent,"agent1"); //$NON-NLS-1$
		
	}

	
	
	
	
	
	
	
	
}
