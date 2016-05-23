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
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleRiskDistribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

/**
 * Creates the View of the Distribution of Risk Table
 * @author Florian Wiedner
 * @category Risk
 * @since 1.7
 * @version 1.0
 *
 */
public class ViewRisksDistributionTable {

	/** View */
	private Composite root;
	/**
	 * The ARXPopulationModel used in the mean part of Generation of the Results
	 */
	private ARXPopulationModel population;
	/**View**/	
	private DynamicTable table;
	/**
	 * The DataHandle from the Input or the Output for Generation of the Plot
	 */
	private DataHandle result;

	/**
	 * Generates the new View of the Distribution of Risk Table
	 * @param parent The Parent Composite for Adding
	 * @param result The result of the Data (Input or Output)
	 * @param population The Population Model
	 */
	public ViewRisksDistributionTable(final Composite parent, DataHandle result, ARXPopulationModel population) {
		this.result = result;
		this.population = population;
		this.build(parent);
	}

	/**
	 * Generates the View and builds it.
	 * @param parent The Parent Composite
	 */
	private void build(Composite parent){
		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(new FillLayout());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		// The Table
		table = SWTUtil.createTableDynamic(root, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setMenu(new ClipboardHandlerTable(table).getMenu());

		DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
		c.setWidth("33%", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.1")); //$NON-NLS-1$
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("33%", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.2")); //$NON-NLS-1$
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("33%", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.3")); //$NON-NLS-1$
		for (final TableColumn col : table.getColumns()) {
			col.pack();
		}
		SWTUtil.createGenericTooltip(table);

		Thread getData = new Thread(new Runnable() {

			@Override
			public void run() {
				final RiskEstimateBuilder builder;
				builder = result.getRiskEstimator(population);

				if (builder != null) {

					double[] frequencies;
					double[] cumulative;
					String[] labels;
					// Perform work
					// Perform work
					RiskModelSampleRiskDistribution model = builder.getSampleBasedRiskDistribution();

					// Create array
					frequencies = model.getFractionOfRecordsForRiskThresholds();
					cumulative = model.getFractionOfRecordsForCumulativeRiskThresholds();
					labels = new String[frequencies.length];
					for (int i = 0; i < frequencies.length; i++) {
						labels[i] = String
								.valueOf(SWTUtil.getPrettyString(model.getAvailableRiskThresholds()[i] * 100d));
					}
					labels[0] = "<=" + SWTUtil.getPrettyString(1e-6); //$NON-NLS-1$

					// Create entries
					for (int i = labels.length - 1; i >= 0; i--) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(0, labels[i]);
						item.setData("1", frequencies[i]);
						item.setData("2", cumulative[i]);
					}

					root.layout();
				}
			}
		});
		getData.run();
	}
}
