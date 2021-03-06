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
package org.janusproject.kernel.bench;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.arakhne.afc.vmutil.VMCommandLine;
import org.arakhne.afc.vmutil.locale.Locale;
import org.janusproject.kernel.bench.agent.message.BroadcastMessageSendingBench;
import org.janusproject.kernel.bench.agent.message.HeavyAgentBroadcastMessageSendingBench;
import org.janusproject.kernel.bench.agent.message.HeavyAgentMessageReceivingBench;
import org.janusproject.kernel.bench.agent.message.HeavyAgentMessageSendingBench;
import org.janusproject.kernel.bench.agent.message.LightAgentBroadcastMessageSendingBench;
import org.janusproject.kernel.bench.agent.message.LightAgentMessageReceivingBench;
import org.janusproject.kernel.bench.agent.message.LightAgentMessageSendingBench;
import org.janusproject.kernel.bench.agent.message.MessageReadingBench;
import org.janusproject.kernel.bench.agent.message.MessageSendingBench;
import org.janusproject.kernel.bench.agent.signal.HeavyAgentSignalSendingBench;
import org.janusproject.kernel.bench.agent.signal.LightAgentSignalSendingBench;
import org.janusproject.kernel.bench.agent.signal.SignalSendingBench;
import org.janusproject.kernel.bench.api.Bench;
import org.janusproject.kernel.bench.execution.HeavyAgentCreationBench;
import org.janusproject.kernel.bench.execution.HeavyAgentDestructionBench;
import org.janusproject.kernel.bench.execution.HeavyAgentExecutionBench;
import org.janusproject.kernel.bench.execution.HeavyAgentRoleExecutionBench;
import org.janusproject.kernel.bench.execution.LightAgentCreationBench;
import org.janusproject.kernel.bench.execution.LightAgentDestructionBench;
import org.janusproject.kernel.bench.execution.LightAgentExecutionBench;
import org.janusproject.kernel.bench.execution.LightAgentRoleExecutionBench;
import org.janusproject.kernel.bench.mailbox.ArrayListMailboxInsertionBench;
import org.janusproject.kernel.bench.mailbox.ArrayListMailboxReadingBench;
import org.janusproject.kernel.bench.mailbox.BlackHoleMailboxInsertionBench;
import org.janusproject.kernel.bench.mailbox.BlackHoleMailboxReadingBench;
import org.janusproject.kernel.bench.mailbox.LinkedListMailboxInsertionBench;
import org.janusproject.kernel.bench.mailbox.LinkedListMailboxReadingBench;
import org.janusproject.kernel.bench.mailbox.TreeSetMailboxInsertionBench;
import org.janusproject.kernel.bench.mailbox.TreeSetMailboxReadingBench;
import org.janusproject.kernel.bench.memory.HeavyAgentMemoryUsageBench;
import org.janusproject.kernel.bench.memory.LightAgentMemoryUsageBench;
import org.janusproject.kernel.bench.organization.message.CRIOBroadcastMessageSendingBench;
import org.janusproject.kernel.bench.organization.message.CRIOHeavyAgentBroadcastMessageSendingBench;
import org.janusproject.kernel.bench.organization.message.CRIOHeavyAgentMessageReceiving2Bench;
import org.janusproject.kernel.bench.organization.message.CRIOHeavyAgentMessageReceivingBench;
import org.janusproject.kernel.bench.organization.message.CRIOHeavyAgentMessageSendingBench;
import org.janusproject.kernel.bench.organization.message.CRIOLightAgentBroadcastMessageSendingBench;
import org.janusproject.kernel.bench.organization.message.CRIOLightAgentMessageReceiving2Bench;
import org.janusproject.kernel.bench.organization.message.CRIOLightAgentMessageReceivingBench;
import org.janusproject.kernel.bench.organization.message.CRIOLightAgentMessageSendingBench;
import org.janusproject.kernel.bench.organization.message.CRIOMessageReceiving2Bench;
import org.janusproject.kernel.bench.organization.message.CRIOMessageReceivingBench;
import org.janusproject.kernel.bench.organization.message.CRIOMessageSendingBench;
import org.janusproject.kernel.bench.organization.signal.CRIOHeavyAgentSignalSending2Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOHeavyAgentSignalSending3Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOHeavyAgentSignalSendingBench;
import org.janusproject.kernel.bench.organization.signal.CRIOLightAgentSignalSending2Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOLightAgentSignalSending3Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOLightAgentSignalSendingBench;
import org.janusproject.kernel.bench.organization.signal.CRIOSignalSending2Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOSignalSending3Bench;
import org.janusproject.kernel.bench.organization.signal.CRIOSignalSendingBench;

/** Run the benchs.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 0.5
 */
@SuppressWarnings("unchecked")
public class Benchs {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		VMCommandLine.saveVMParametersIfNotSet(BenchLauncher.class, args);
		run(
				//
				// Mailboxes
				//
				TreeSetMailboxInsertionBench.class,
				LinkedListMailboxInsertionBench.class,
				ArrayListMailboxInsertionBench.class,
				BlackHoleMailboxInsertionBench.class,
				TreeSetMailboxReadingBench.class,
				LinkedListMailboxReadingBench.class,
				ArrayListMailboxReadingBench.class,
				BlackHoleMailboxReadingBench.class,
				//
				// Agent Messages
				//
				MessageSendingBench.class,
				BroadcastMessageSendingBench.class,
				LightAgentMessageSendingBench.class,
				LightAgentBroadcastMessageSendingBench.class,
				HeavyAgentMessageSendingBench.class,
				HeavyAgentBroadcastMessageSendingBench.class,
				MessageReadingBench.class,
				LightAgentMessageReceivingBench.class,
				HeavyAgentMessageReceivingBench.class,
				//
				// CRIO Messages
				//
				CRIOMessageSendingBench.class,
				CRIOBroadcastMessageSendingBench.class,
				CRIOLightAgentMessageSendingBench.class,
				CRIOLightAgentBroadcastMessageSendingBench.class,
				CRIOHeavyAgentMessageSendingBench.class,
				CRIOHeavyAgentBroadcastMessageSendingBench.class,
				CRIOMessageReceivingBench.class,
				CRIOMessageReceiving2Bench.class,
				CRIOLightAgentMessageReceivingBench.class,
				CRIOLightAgentMessageReceiving2Bench.class,
				CRIOHeavyAgentMessageReceivingBench.class,
				CRIOHeavyAgentMessageReceiving2Bench.class,
				//
				// Agent Signals
				//
				SignalSendingBench.class,
				LightAgentSignalSendingBench.class,
				HeavyAgentSignalSendingBench.class,
				//
				// CRIO Signals
				//
				CRIOSignalSendingBench.class,
				CRIOSignalSending2Bench.class,
				CRIOSignalSending3Bench.class,
				CRIOLightAgentSignalSendingBench.class,
				CRIOLightAgentSignalSending2Bench.class,
				CRIOLightAgentSignalSending3Bench.class,
				CRIOHeavyAgentSignalSendingBench.class,
				CRIOHeavyAgentSignalSending2Bench.class,
				CRIOHeavyAgentSignalSending3Bench.class,
				//
				// Execution
				//
				HeavyAgentCreationBench.class,
				LightAgentCreationBench.class,
				LightAgentExecutionBench.class,
				HeavyAgentExecutionBench.class,
				LightAgentRoleExecutionBench.class,
				HeavyAgentRoleExecutionBench.class,
				HeavyAgentDestructionBench.class,
				LightAgentDestructionBench.class,
				//
				// Memory Usage
				//
				LightAgentMemoryUsageBench.class,
				HeavyAgentMemoryUsageBench.class
				);
		System.exit(0);
	}

	private static List<Class<? extends Bench<?>>> selectBenchs(Class<? extends Bench<?>>... benchs) {
		List<Class<? extends Bench<?>>> benchsToRun = new ArrayList<Class<? extends Bench<?>>>();
		SelectionGUI gui = new SelectionGUI(benchs);
		gui.setVisible(true);
		if (gui.isJei()) {
			benchsToRun = null;
		}
		else if (gui.isBenchmark()) {
			gui.getSelectionBenchs(benchsToRun);
		}
		gui.dispose();
		return benchsToRun;
	}

	private static void run(Class<? extends Bench<?>>... benchs) throws Exception {
		List<Class<? extends Bench<?>>> benchsToRun = selectBenchs(benchs);

		if (benchsToRun==null) {
			JanusExperienceIndex.main(null);
		}
		else if (!benchsToRun.isEmpty()) {
			File output = new File((File)null, "janusBenchmarks"); //$NON-NLS-1$
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd-HHmmss"); //$NON-NLS-1$
			output = new File(output, fmt.format(new Date()));
			System.out.println(output.getAbsolutePath());
			output.mkdirs();

			boolean isAssertEnabled = false;
			assert((isAssertEnabled = true)==true);
			ExecutorService serv = null;
			if (!isAssertEnabled)
				serv = Executors.newFixedThreadPool(2);
			
			float percentagePerBench = 100f / benchsToRun.size();
			float progression = 0f;
			
			for(Class<? extends Bench<?>> benchType : benchsToRun) {
				if (isAssertEnabled) {
					launchForDebug(output, benchType, progression, percentagePerBench);
				}
				else {
					launchInSubProcess(output, benchType, serv, progression, percentagePerBench);
				}
				progression += percentagePerBench;
			}

			System.out.println(output.getAbsolutePath());
		}
	}
	
	private static void launchForDebug(File output, Class<? extends Bench<?>> benchType, float progression, float percentage) throws Exception {
		BenchLauncher.main(
				new String[] {
						output.getAbsolutePath(),
						Float.toString(progression),
						Float.toString(percentage),
						benchType.getCanonicalName()
				});
	}
	
	private static void launchInSubProcess(File output, Class<? extends Bench<?>> benchType, ExecutorService service, float progression, float progressionWindow) throws Exception {
		Process process = null;
		try {
			String[] cmd = new String[] {
					VMCommandLine.getVMBinary(),
					"-Xmx"+BenchConstants.MAX_MEMORY+"m", //$NON-NLS-1$ //$NON-NLS-2$
					"-classpath", //$NON-NLS-1$
					System.getProperty("java.class.path"), //$NON-NLS-1$
					BenchLauncher.class.getCanonicalName(),
					output.getAbsolutePath(),
					Float.toString(progression),
					Float.toString(progressionWindow),
					benchType.getCanonicalName()
			};
			for(int i=0; i<cmd.length; ++i) {
				if (i>0) System.out.print(' ');
				System.out.print(cmd[i]);
			}
			System.out.print("\n"); //$NON-NLS-1$
			process = Runtime.getRuntime().exec(
					cmd,
					null,
					null);
			OutputRunner r1 = new OutputRunner(process.getInputStream(), System.out);
			OutputRunner r2 = new OutputRunner(process.getErrorStream(), System.err);
			service.submit(r1);
			service.submit(r2);
			process.waitFor();
			r1.stop();
			r2.stop();
			process = null;
		}
		catch(Throwable e) {
			System.err.println(Locale.getString(Benchs.class, "KILLING")); //$NON-NLS-1$
			e.printStackTrace();
			if (process!=null) {
				process.destroy();
			}
		}
	}
	
	private static class OutputRunner implements Runnable {
	
		private volatile boolean stop = false; 
		private final PrintStream os;
		private final InputStream is;
		
		public OutputRunner(InputStream is, PrintStream os) {
			this.is = is;
			this.os = os;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				String s;
				byte[] buffer = new byte[2048];
				Thread.yield();
				int nb = this.is.read(buffer);
				while (nb>0 && !this.stop) {
					s = new String(buffer, 0, nb);
					this.os.print(s);
					Thread.yield();
					nb = this.is.read(buffer);
				}
				this.is.close();
			}
			catch(IOException _) {
				//
			}
		}
		
		/** Stop the process.
		 */
		public void stop() {
			this.stop = true;
		}
		
	}
	

	/** Select the benchs.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 0.5
	 */
	private static class SelectionGUI extends JDialog implements ActionListener {

		private static final long serialVersionUID = 5475408361451706102L;

		private DefaultListModel model;
		private JList list;
		private boolean isBench = false;
		private boolean isJei = false;

		/**
		 * @param benchs
		 */
		public SelectionGUI(Class<? extends Bench<?>>... benchs) {
			super((Window)null, Locale.getString(Benchs.class, "SELECT_BENCHS")); //$NON-NLS-1$
			setPreferredSize(new Dimension(600, 600));
			setLayout(new BorderLayout());
			setModal(true);
			this.model = new DefaultListModel();
			this.list = new JList(this.model);
			add(BorderLayout.CENTER, new JScrollPane(this.list));
			this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.list.setCellRenderer(new SelectionRenderer());

			for(Class<? extends Bench<?>> benchType : benchs) {
				addInModel(benchType);
			}

			JPanel btPanel = new JPanel();
			add(BorderLayout.SOUTH, btPanel);
			
			JButton bt = new JButton(Locale.getString(Benchs.class, "LAUNCH_BENCHS")); //$NON-NLS-1$
			btPanel.add(bt);
			bt.setActionCommand("BENCH"); //$NON-NLS-1$
			bt.addActionListener(this);

			bt = new JButton(Locale.getString(Benchs.class, "COMPUTE_JEI")); //$NON-NLS-1$
			btPanel.add(bt);
			bt.setActionCommand("JEI"); //$NON-NLS-1$
			bt.addActionListener(this);

			pack();
		}

		/** Replies if the benchmarcks should be computed
		 * 
		 * @return <code>true</code> if the user decided to compute the benchmarks.
		 */
		public boolean isBenchmark() {
			return this.isBench;
		}

		/** Replies if the JEI should be computed
		 * 
		 * @return <code>true</code> if the user decided to compute the JEI.
		 */
		public boolean isJei() {
			return this.isJei;
		}

		/**
		 * @param list
		 * @return the number of added benchs.
		 */
		@SuppressWarnings("hiding")
		public int getSelectionBenchs(List<Class<? extends Bench<?>>> list) {
			int nb = 0;
			for(int i : this.list.getSelectedIndices()) {
				list.add((Class<? extends Bench<?>>)this.model.get(i));
				++nb;
			}
			return nb;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void dispose() {
			this.model.clear();
			this.model = null;
			this.list = null;
			super.dispose();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if ("JEI".equals(e.getActionCommand())) { //$NON-NLS-1$
				this.isJei = true;
				setVisible(false);
			}
			else if ("BENCH".equals(e.getActionCommand())) { //$NON-NLS-1$
				this.isBench = true;
				setVisible(false);
			}
		}

		private void addInModel(Class<? extends Bench<?>> data) {
			assert(data!=null);
			int f = 0;
			int l = this.model.size()-1;
			int c;
			Class<? extends Bench<?>> d;
			int cmpR;
			while (l>=f) {
				c = (f+l)/2;
				d = (Class<? extends Bench<?>>)this.model.get(c);
				cmpR = compare(data, d);
				if (cmpR==0) return;
				if (cmpR<0) {
					l = c-1;
				}
				else {
					f = c+1;
				}
			}
			this.model.add(f, data);
		}

		@SuppressWarnings("synthetic-access")
		public static int compare(Class<?> c1, Class<?> c2) {
			if (c1==c2) return 0;
			if (c1==null) return Integer.MAX_VALUE;
			if (c2==null) return Integer.MIN_VALUE;
			String n1 = toUIName(c1);
			String n2 = toUIName(c2);
			return n1.compareToIgnoreCase(n2);
		}

	}

	/** Select the benchs.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 0.5
	 */
	private static class SelectionRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -5166019436245423924L;

		/**
		 */
		public SelectionRenderer() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("synthetic-access")
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			label.setText(toUIName((Class<?>)value));
			return label;
		}

	}

	private static String toUIName(Class<?> type) {
		StringBuilder b = new StringBuilder();
		b.append(type.getPackage().getName().substring(type.getPackage().getName().lastIndexOf('.')+1));
		b.append(" | "); //$NON-NLS-1$
		b.append(type.getSimpleName());
		return b.toString();
	}

}