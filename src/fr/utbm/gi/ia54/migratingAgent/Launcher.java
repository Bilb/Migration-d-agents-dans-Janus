package fr.utbm.gi.ia54.migratingAgent;

import java.util.logging.Level;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.network.fakenetwork.NetworkFakeKernelAgentFactory;


public class Launcher  {

	private static final String APPLICATION_NAME = "ia54 migration demo"; //$NON-NLS-1$
	
	public static void main(String[] args) {
		LoggerUtil.setGlobalLevel(Level.FINE);
		Kernels.setPreferredKernelFactory(new NetworkFakeKernelAgentFactory());

		Kernel kernel = Kernels.create(false, APPLICATION_NAME);
		Kernels.create(false, APPLICATION_NAME);
		
		
		MigrationAgent agent = new MigrationAgent();
		kernel.launchLightAgent(agent,"agent1"); //$NON-NLS-1$
		
	}

	
	
	
	
	
	
	
	
}
