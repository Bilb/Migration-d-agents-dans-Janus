/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2011 Janus Core Developers
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
package org.janusproject.demos.jaak.pacman;

import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JScrollPane;

import org.janusproject.demos.jaak.pacman.channel.Player;
import org.janusproject.demos.jaak.pacman.ui.PacManPanel;
import org.janusproject.jaak.environment.model.JaakEnvironment;
import org.janusproject.jaak.kernel.JaakKernelController;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.logger.LoggerUtil;

/** Applet for the Ant Colony Demo.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Applet extends JApplet {

	private static final long serialVersionUID = 6426840892840218129L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		super.init();
		LoggerUtil.setLoggingEnable(false);
		
		JaakEnvironment environment = PacManGame.createEnvironment();
		JaakKernelController controller = PacManGame.initializeKernel(environment);
		Player player = PacManGame.createEntitiesWithInteractivePlayer();
		PacManPanel panel = PacManGame.createPanel(controller.getKernelAddress(), player);

		JScrollPane scroll = new JScrollPane(panel);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		Kernels.killAll();
		super.destroy();
	}

}