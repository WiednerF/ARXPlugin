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
import org.deidentifier.arx.gui.view.LayoutRisks;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.risk.common.ComponentRiskMonitor;
import org.deidentifier.arx.kettle.risk.common.ComponentRiskThresholds;
import org.deidentifier.arx.kettle.risk.common.ComponentTitledSeparator;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleSummary;
import org.deidentifier.arx.risk.RiskModelSampleSummary.JournalistRisk;
import org.deidentifier.arx.risk.RiskModelSampleSummary.MarketerRisk;
import org.deidentifier.arx.risk.RiskModelSampleSummary.ProsecutorRisk;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Generates the RiskMonitor
 * @author Florian Wiedner
 * @version 1.0
 * @since 1.7
 *
 */
public class ViewRisksReidentificationRisks {
	/** View */
	private Composite root;
	/**
	 * The ARX Population Model used here
	 */
	private ARXPopulationModel population;

	/** View */
	private static final String MESSAGE_CAPTION1 = Resources.getMessage("ViewRisksReIdentification.0"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_CAPTION2 = Resources.getMessage("ViewRisksReIdentification.1"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_CAPTION3 = Resources.getMessage("ViewRisksReIdentification.2"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_LABEL1 = Resources.getMessage("ViewRisksReIdentification.3"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_LABEL2 = Resources.getMessage("ViewRisksReIdentification.4"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_LABEL3 = Resources.getMessage("ViewRisksReIdentification.5"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_SHORT1 = Resources.getMessage("ViewRisksReIdentification.6"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_SHORT2 = Resources.getMessage("ViewRisksReIdentification.7"); //$NON-NLS-1$
	/** View */
	private static final String MESSAGE_SHORT3 = Resources.getMessage("ViewRisksReIdentification.8"); //$NON-NLS-1$

	/** View */
	private ComponentRiskMonitor prosecutor1;
	/** View */
	private ComponentRiskMonitor prosecutor2;
	/** View */
	private ComponentRiskMonitor prosecutor3;
	/** View */
	private ComponentRiskMonitor journalist1;
	/** View */
	private ComponentRiskMonitor journalist2;
	/** View */
	private ComponentRiskMonitor journalist3;
	/** View */
	private ComponentRiskMonitor marketer1;
	/**
	 * View
	 */
	private ComponentRiskThresholds riskThresholds;

	/**
	 * The DataHandle for In and Output
	 */
	private DataHandle result;
	/**
	 * The Layout Risk Parent 
	 */
	private LayoutRisks risk;

	/**
	 * The Constructor for the Output
	 * @param parent The Parent Composite
	 * @param result The DataHandle
	 * @param population The Population
	 */
	public ViewRisksReidentificationRisks(final Composite parent, DataHandle result, ARXPopulationModel population) {
		this.risk = null;
		this.result = result;
		this.population = population;
		this.riskThresholds = null;
		this.build(parent);
	}

	/**
	 * The Constructor for the Input
	 * @param parent The Parent Composite
	 * @param result The DataHandle
	 * @param population The Population Model
	 * @param risk The Risk Layout Parent
	 */
	public ViewRisksReidentificationRisks(final Composite parent, DataHandle result, final ARXPopulationModel population,
			final LayoutRisks risk) {
		this.risk = risk;
		this.result = result;
		this.population = population;
		this.riskThresholds = null;
		this.build(parent);
	}

	/**
	 * The Building Private
	 * @param parent The Parent Composite
	 */
	private void build(final Composite parent) {
		GridLayout layout = SWTUtil.createGridLayoutWithEqualWidth(3);
		layout.marginHeight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		layout.verticalSpacing = 0;
		layout.makeColumnsEqualWidth = true;

		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(layout);
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		// Prepare
		GridData separatordata = SWTUtil.createFillHorizontallyGridData(true, 3);
		separatordata.verticalIndent = 0;

		// Prosecutor
		ComponentTitledSeparator separator = new ComponentTitledSeparator(root, SWT.NONE);
		separator.setLayoutData(separatordata);
		separator.setText(MESSAGE_CAPTION1);
		separator.setImage(Resources.getImage("prosecutor.png")); //$NON-NLS-1$

		prosecutor1 = new ComponentRiskMonitor(root, MESSAGE_LABEL1, MESSAGE_SHORT1);
		prosecutor2 = new ComponentRiskMonitor(root, MESSAGE_LABEL2, MESSAGE_SHORT2);
		prosecutor3 = new ComponentRiskMonitor(root, MESSAGE_LABEL3, MESSAGE_SHORT3);
		prosecutor1.setLayoutData(SWTUtil.createFillGridData());
		prosecutor2.setLayoutData(SWTUtil.createFillGridData());
		prosecutor3.setLayoutData(SWTUtil.createFillGridData());

		// Journalist
		separator = new ComponentTitledSeparator(root, SWT.NONE);
		separator.setLayoutData(separatordata);
		separator.setText(MESSAGE_CAPTION2);
		separator.setImage(Resources.getImage("journalist.png")); //$NON-NLS-1$

		journalist1 = new ComponentRiskMonitor(root, MESSAGE_LABEL1, MESSAGE_SHORT1);
		journalist2 = new ComponentRiskMonitor(root, MESSAGE_LABEL2, MESSAGE_SHORT2);
		journalist3 = new ComponentRiskMonitor(root, MESSAGE_LABEL3, MESSAGE_SHORT3);
		journalist1.setLayoutData(SWTUtil.createFillGridData());
		journalist2.setLayoutData(SWTUtil.createFillGridData());
		journalist3.setLayoutData(SWTUtil.createFillGridData());

		// Marketer
		separator = new ComponentTitledSeparator(root, SWT.NONE);
		separator.setLayoutData(separatordata);
		separator.setText(MESSAGE_CAPTION3);
		separator.setImage(Resources.getImage("marketer.png")); //$NON-NLS-1$

		marketer1 = new ComponentRiskMonitor(root, MESSAGE_LABEL3, MESSAGE_SHORT3);
		marketer1.setLayoutData(SWTUtil.createFillGridData());

		if (this.risk != null) {
			GridData data = SWTUtil.createFillGridData();
			data.heightHint = 30;
			data.horizontalSpan = 2;
			riskThresholds = new ComponentRiskThresholds(root);
			riskThresholds.setLayoutData(data);
			riskThresholds.setThresholdHighestRisk(0.2d);
			riskThresholds.setThresholdRecordsAtRisk(0.05d);
			riskThresholds.setThresholdSuccessRate(0.05d);
			riskThresholds.addSelectionListenerThresholdHighestRisk(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					handleThresholdUpdateInMonitors();

				}
			});

			riskThresholds.addSelectionListenerThresholdRecordsAtRisk(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					handleThresholdUpdateInMonitors();

				}
			});
			riskThresholds.addSelectionListenerThresholdSuccessRate(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					handleThresholdUpdateInMonitors();

				}
			});
		}
		// Enable/disable
		final RiskEstimateBuilder builder;
		builder = result.getRiskEstimator(population);
		if (builder != null) {
			ProsecutorRisk prosecutor;
			JournalistRisk journalist;
			MarketerRisk marketer;

			// Perform work
			RiskModelSampleSummary summary = builder.getSampleBasedRiskSummary(0.20d);
			prosecutor = summary.getProsecutorRisk();
			journalist = summary.getJournalistRisk();
			marketer = summary.getMarketerRisk();

			// Update views
			prosecutor1.setRisk(prosecutor.getRecordsAtRisk());
			prosecutor1.setThreshold(0.05d);
			prosecutor2.setRisk(prosecutor.getHighestRisk());
			prosecutor2.setThreshold(0.2d);
			prosecutor3.setRisk(prosecutor.getSuccessRate());
			prosecutor3.setThreshold(0.05d);

			// Update views
			journalist1.setRisk(journalist.getRecordsAtRisk());
			journalist1.setThreshold(0.05d);
			journalist2.setRisk(journalist.getHighestRisk());
			journalist2.setThreshold(0.2d);
			journalist3.setRisk(journalist.getSuccessRate());
			journalist3.setThreshold(0.05d);

			// Update views
			marketer1.setRisk(marketer.getSuccessRate());
			marketer1.setThreshold(0.05d);

			// Layout
			root.layout();
		}

	}

	/**
	 * Handles updates of risk thresholds
	 */
	private void handleThresholdUpdateInMonitors() {
		prosecutor1.setThreshold(riskThresholds.getThresholdRecordsAtRisk());
		prosecutor2.setThreshold(riskThresholds.getThresholdHighestRisk());
		prosecutor3.setThreshold(riskThresholds.getThresholdSuccessRate());
		journalist1.setThreshold(riskThresholds.getThresholdRecordsAtRisk());
		journalist2.setThreshold(riskThresholds.getThresholdHighestRisk());
		journalist3.setThreshold(riskThresholds.getThresholdSuccessRate());
		marketer1.setThreshold(riskThresholds.getThresholdSuccessRate());
		this.risk.handleThresholdUpdateInMonitors(riskThresholds.getThresholdRecordsAtRisk(),
				riskThresholds.getThresholdHighestRisk(), riskThresholds.getThresholdSuccessRate());
	}

	/**
	 * The Update of the Monitors
	 * @param recordsAtRisk The Records at Risk
	 * @param highestRisk The Highest Risk Threshold
	 * @param successRat The Success Rat Threshold
	 */
	public void handleThresholdUpdateInMonitors(double recordsAtRisk, double highestRisk, double successRat) {
		prosecutor1.setThreshold(recordsAtRisk);
		prosecutor2.setThreshold(highestRisk);
		prosecutor3.setThreshold(successRat);
		journalist1.setThreshold(recordsAtRisk);
		journalist2.setThreshold(highestRisk);
		journalist3.setThreshold(successRat);
		marketer1.setThreshold(successRat);
	}

}
