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
package org.janusproject.kernel.bench.agent.message;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.agent.Kernels;
import org.janusproject.kernel.bench.api.AgentNumberBenchRun;
import org.janusproject.kernel.bench.api.BenchUtil;
import org.janusproject.kernel.bench.api.CsvBench;
import org.janusproject.kernel.logger.LoggerUtil;
import org.janusproject.kernel.message.StringMessage;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.util.sizediterator.SizedIterator;

/** Run the bench on the message API.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.5
 */
public class BroadcastMessageSendingBench extends CsvBench<AgentNumberBenchRun> {

	private Emitter emitter;
	
	/**
	 * @param directory
	 * @throws IOException
	 */
	public BroadcastMessageSendingBench(File directory) throws IOException {
		super(directory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<AgentNumberBenchRun> setUpGroupWithCSV(String benchFunctionName) throws Exception {
		writeHeader("Name", "Agents", "Tests", "Run (ns)", "Operation Duration (ns)", "Standard Deviation", "OS Load Average"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		return BenchUtil.makeAllHeavyAgentIntervals(
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
		
		AtomicInteger nbLaunchedAgents = new AtomicInteger(0);
		
		for(int i=0; i<run.getNumberOfAgents()-1; ++i) {
			kernel.submitLightAgent(new IddleAgent(nbLaunchedAgents, true));
		}
		
		this.emitter = new Emitter(nbLaunchedAgents);
		kernel.submitLightAgent(this.emitter);
		
		kernel.launchDifferedExecutionAgents();

		while (nbLaunchedAgents.get()<run.getNumberOfAgents()) {
			Thread.yield();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDownUnitaryBench(AgentNumberBenchRun run) throws Exception {
		Kernels.killAll();
		this.emitter = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDownMultiRunBench(int nbRuns, AgentNumberBenchRun run)
			throws Exception {
		writeRecord(
				run.getName(),
				run.getNumberOfAgents(), 
				getNumberOfTests(),
				run.getRunDuration(),
				run.getTestAverageDuration(),
				run.getTestStandardDeviation(),
				getSystemLoadAverage());
		super.tearDownMultiRunBench(nbRuns, run);
	}
	
	/**
	 */
	public void benchBroadcastMessage() {
		this.emitter.broadcast();
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class Emitter extends Agent {
		
		private static final long serialVersionUID = 8804935270327139156L;

		private AtomicInteger flag;
		
		/**
		 * @param flag
		 */
		public Emitter(AtomicInteger flag) {
			this.flag = flag;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Status live() {
			if (this.flag==null) {
				getMailbox().clear();
			}
			else {
				this.flag.incrementAndGet();
				this.flag = null;
			}
			return null;
		}
		
		/**
		 */
		public void broadcast() {
			broadcastMessage(new StringMessage("")); //$NON-NLS-1$
		}

	}

}