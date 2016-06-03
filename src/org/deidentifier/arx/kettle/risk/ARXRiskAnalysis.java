/*
 * Plugin for Kettle with ARX: Powerful Data Anonymization
 * Copyright 2016 Florian Wiedner and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deidentifier.arx.kettle.risk;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Creates the Window for the ARX Risk Analysis
 * @author Florian Wiedner
 * @since 1.7
 * @version 1.0
 *
 */
public class ARXRiskAnalysis extends Thread {
	/**
	 * The Data from the Input
	 */
	private Data data;
	/**
	 * The Population Model used at the moment
	 */
	private ARXPopulationModel population;
	/**
	 * The Current Display
	 */
	private Display display;
	/**
	 * The DataHandle for the Output
	 */
	private DataHandle output;

	/**
	 * Opens the ARX Risk Analysis Windows
	 * @param output The Output DataHandle
	 * @param data The Input Data and Configuration
	 * @param population The Population Model used
	 * @param display The Current Display
	 */
	public ARXRiskAnalysis(DataHandle output, Data data, ARXPopulationModel population, Display display) {
		this.output = output;
		this.data = data;
		this.population = population;
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		final Shell shell = new Shell(display.getActiveShell(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
		shell.setText("Risk Analytics from the Anonymication with the ARX Tool");
		shell.setImage(Resources.getImage("logo_64.png"));

		shell.setLayout(SWTUtil.createGridLayout(2));
		new LayoutRisks(shell, output, data, population);

		shell.pack();
		shell.open();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
		shell.dispose();
	}

}
