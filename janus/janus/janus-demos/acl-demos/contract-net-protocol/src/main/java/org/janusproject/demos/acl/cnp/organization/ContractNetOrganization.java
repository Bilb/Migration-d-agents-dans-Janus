/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2012 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.janusproject.demos.acl.cnp.organization;

import org.janusproject.demos.acl.cnp.role.ContractNetBroker;
import org.janusproject.demos.acl.cnp.role.ContractNetRequester;
import org.janusproject.kernel.crio.core.CRIOContext;
import org.janusproject.kernel.crio.core.Organization;


/** Defines the organization that is supporting the Contract-Net-Protocol.
*
* 
* @author $Author: madeline$
* @author $Author: kleroy$
* @author $Author: ptalagrand$
* @author $Author: ngaud$
* @version $FullVersion$
* @mavengroupid $Groupid$
* @mavenartifactid $ArtifactId$
*/
public class ContractNetOrganization extends Organization {

	/**
	 * @param crioContext
	 */
	public ContractNetOrganization(CRIOContext crioContext) {
		super(crioContext);
		addRole(ContractNetRequester.class);
		addRole(ContractNetBroker.class);
		addRole(ContractNetBroker.class);
	}

}
