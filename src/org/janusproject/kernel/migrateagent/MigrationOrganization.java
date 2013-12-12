package org.janusproject.kernel.migrateagent;
import org.janusproject.kernel.crio.core.CRIOContext;
import org.janusproject.kernel.crio.core.Organization;


public class MigrationOrganization extends Organization {

	public MigrationOrganization(CRIOContext crioContext) {
		super(crioContext);
		addRole(MigrationKernelRole.class);
		
		/*TODO add capaity check ? */
	}
	
	

}
