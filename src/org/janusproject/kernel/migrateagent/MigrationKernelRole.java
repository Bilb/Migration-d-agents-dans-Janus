package org.janusproject.kernel.migrateagent;

import org.janusproject.kernel.crio.core.Role;
import org.janusproject.kernel.message.Message;
import org.janusproject.kernel.status.Status;

public class MigrationKernelRole extends Role {

	@Override
	public Status live() {
		//checker file attente de message
		//TODO
		int nb=0;
		for (Message msg : getMailbox()) {
			System.out.println(getAddress() + " ----- " + nb + " : got a message");
		}
		
		return null;
	}


}
