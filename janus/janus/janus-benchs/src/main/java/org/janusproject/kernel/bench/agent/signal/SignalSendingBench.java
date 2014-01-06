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
package org.janusproject.kernel.bench.agent.signal;

import java.io.File;
import java.io.IOException;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.agentsignal.Signal;
import org.janusproject.kernel.agentsignal.SignalListener;
import org.janusproject.kernel.bench.api.AgentNumberBenchRun;
import org.janusproject.kernel.bench.api.BenchUtil;
import org.janusproject.kernel.bench.api.CsvBench;
import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.util.sizediterator.SizedIterator;

/** Run the bench on the signal API.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.5
 */
public class SignalSendingBench extends CsvBench<AgentNumberBenchRun> {

	private Emitter emitter;
	
	/**
	 * @param directory
	 * @throws IOException
	 */
	public SignalSendingBench(File directory) throws IOException {
		super(directory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<AgentNumberBenchRun> setUpGroupWithCSV(String benchFunctionName) throws Exception {
		writeHeader("Name", "Listeners", "Tests", "Run (ns)", "Unit (ns)", "OS Load Average"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		return BenchUtil.makeAllLightAgentIntervals(
				AgentNumberBenchRun.class,
				benchFunctionName,
				2);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpUnitaryBench(AgentNumberBenchRun run) throws Exception {
		super.setUpUnitaryBench(run);
		Kernels.killAll();
		LoggerUtil.setLoggingEnable(false);
		Kernel kernel = Kernels.create();
		
		this.emitter = new Emitter();
		// Launch light because it takes more time to run a light agent than an heavy agent.
		kernel.launchLightAgent(this.emitter, run.getNumberOfAgents());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDownUnitaryBench(AgentNumberBenchRun run) throws Exception {
		Kernels.killAll();
		this.emitter = null;
		writeRecord(
				run.getName(),
				run.getNumberOfAgents(), 
				getNumberOfTests(),
				run.getRunDuration(),
				run.getTestAverageDuration(),
				getSystemLoadAverage());
	}
	
	/**
	 */
	public void benchFireSignal() {
		this.emitter.fire();
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class Emitter extends Agent {
		
		private static final long serialVersionUID = 8804935270327139156L;

		/**
		 */
		public Emitter() {
			//
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Status activate(Object... parameters) {
			int nb = ((Integer)parameters[0]).intValue();
			SignalListener listener = new IddleSignalListener();
			for(int i=0; i<nb; ++i) {
				addSignalListener(listener);
			}
			return super.activate(parameters);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Status live() {
			getMailbox().clear();
			return null;
		}
		
		/**
		 */
		public void fire() {
			fireSignal(new Signal("MYSIGNAL")); //$NON-NLS-1$
		}
		
	}

}