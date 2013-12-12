package org.janusproject.kernel.migrateagent;
import java.util.logging.Level;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.network.jxse.agent.JxtaJxseKernelAgentFactory;


public class Launcher  {

	
	public static void main(String[] args) {

		LoggerUtil.setGlobalLevel(Level.SEVERE);
		
		Kernels.setPreferredKernelFactory(new JxtaJxseKernelAgentFactory());
		Kernel kernel = Kernels.create(false, "jxta demo migrate janus"); //$NON-NLS-1$
		
		Kernel kernel2 = Kernels.create(false, "jxta demo migrate janus"); //$NON-NLS-1$
		
		
		MigrationAgent agent = new MigrationAgent(kernel2.getAddress());
		kernel.launchLightAgent(agent,"agent1"); //$NON-NLS-1$
		
	}

	
	
	
	
	
	
	
	
}
