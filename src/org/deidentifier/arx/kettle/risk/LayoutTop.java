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
import org.deidentifier.arx.gui.view.LayoutRisks;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

/**
 * Creates the Upper View for the Risk Result Analysis
 * 
 * @author Florian Wiedner
 * @since 1.7
 * @version 1.0
 *
 */
public class LayoutTop {

	/** View **/
	private CTabFolder folder;
	/** View **/
	public ViewRisksReidentificationRisks reidentification;
	/** View **/
	public ViewRisksQuasiIdentifierTable quasiIdentifier;

	/**
	 * Creates the Upper Result View for the Output
	 * 
	 * @param parent
	 *            The Parent Composite
	 * @param result
	 *            The Result DataHandle
	 * @param population
	 *            The Population Model used
	 */
	public LayoutTop(final Composite parent, DataHandle result, ARXPopulationModel population) {
		folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		this.build(folder, null, result, population);
		// END TABS
		folder.setSelection(0);

	}

	/**
	 * Creates the Upper Result View for the Output
	 * 
	 * @param parent
	 *            The Parent Composite
	 * @param result
	 *            The Result DataHandle
	 * @param population
	 *            The Population Model used
	 * @param risk The Parent Layout for Updating the Reidentification Risk
	 */
	public LayoutTop(final Composite parent, DataHandle result, ARXPopulationModel population, LayoutRisks risk) {
		folder = new CTabFolder(parent, SWT.BORDER);
		folder.setSimple(false);
		this.build(folder, risk, result, population);
		// END TABS
		folder.setSelection(0);

	}

	/**
	 * Builds the View
	 * @param folder The Folder Parent
	 * @param risk The Risk Parent Layout,null for Output
	 * @param result The Result DataHandle for In or Output
	 * @param population The Population Model used
	 */
	private void build(final CTabFolder folder, LayoutRisks risk, DataHandle result, ARXPopulationModel population) {
		new ViewRisksDistributionPlot(LayoutRisks.createItem(folder, "RiskAnalysis.4"), result, population);
		new ViewRisksDistributionTable(LayoutRisks.createItem(folder, "RiskAnalysis.0"), result, population);
		this.quasiIdentifier = new ViewRisksQuasiIdentifierTable(LayoutRisks.createItem(folder, "RiskAnalysis.15"),
				result, population);
		if (risk != null) {
			this.reidentification = new ViewRisksReidentificationRisks(
					LayoutRisks.createItem(folder, "RiskAnalysis.32"), result, population, risk);
			new ViewRisksHIPAA(LayoutRisks.createItem(folder, "RiskAnalysis.26"), result, population);
		} else {
			this.reidentification = new ViewRisksReidentificationRisks(
					LayoutRisks.createItem(folder, "RiskAnalysis.32"), result, population);
		}
	}

	/**
	 * 
	 * @param index
	 *            Set the Selection to this Index
	 */
	public void setSelectionIndex(int index) {
		this.folder.setSelection(index);
	}

	/**
	 * 
	 * @param listener
	 *            Add this as a SelectionListener to this Folder
	 */
	public void addSelectionListener(SelectionListener listener) {
		this.folder.addSelectionListener(listener);
	}

	/**
	 * 
	 * @return The Actual Selected Part
	 */
	public int getSelectionIndex() {
		return this.folder.getSelectionIndex();
	}

	/**
	 * Updating of the Risk Part at the Quasi Identifier
	 * 
	 * @param attributeRisk
	 *            The Uniqueness Model used
	 */
	public void update(final String attributeRisk) {
		this.quasiIdentifier.update(attributeRisk);
	}

	/**
	 * The Update of the Monitors
	 * 
	 * @param recordsAtRisk
	 *            The Records at Risk
	 * @param highestRisk
	 *            The Highest Risk Threshold
	 * @param successRat
	 *            The Success Rat Threshold
	 */
	public void handleThresholdUpdateInMonitors(double recordsAtRisk, double highestRisk, double successRat) {
		this.reidentification.handleThresholdUpdateInMonitors(recordsAtRisk, highestRisk, successRat);
	}

}
