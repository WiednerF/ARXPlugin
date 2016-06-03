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
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This class layouts the risk analysis view.
 *
 * @author Fabian Prasser
 */
public class LayoutRisks {

	/** Constant */
	private static final int WEIGHT_TOP = 75;
	/** Constant */
	private static final int WEIGHT_BOTTOM = 25;
	/** Constant */
	private static final int WEIGHT_LEFT = 50;
	/** Constant */
	private static final int WEIGHT_RIGHT = 50;

	/** View */
	private Composite centerLeft;
	/** View */
	private Composite centerRight;
	/** View */
	private Composite bottomLeft;
	/** View */
	private Composite bottomRight;
	/** View */
	private SashForm centerSash;

	/** View */
	private LayoutTop layoutTopLeft;
	/** View */
	private LayoutTop layoutTopRight;
	/** View */
	private LayoutBottom layoutBottomLeft;
	/** View */
	private LayoutBottom layoutBottomRight;
	/**
	 * The Data Used with Config and Definitions
	 */
	private Data data;
	/**
	 * The Population Model is used
	 */
	private ARXPopulationModel population;
	/**
	 * The DataHandle for the Output
	 */
	private DataHandle output;
	/**
	 * The DataHandle for the Input
	 */
	private DataHandle input;

	/**
	 * Creates a new Instance and View
	 * 
	 * @param parent
	 *            The Parent Composite
	 * @param output
	 *            The Output
	 * @param data
	 *            The Data used
	 * @param population
	 *            The Population Model used
	 */
	public LayoutRisks(final Composite parent, DataHandle output, Data data, ARXPopulationModel population) {
		this.output = output;
		this.input = data.getHandle();
		this.data = data;
		this.population = population;
		this.build(parent);

	}

	/**
	 * The build of the View
	 * 
	 * @param parent
	 *            The Parent Composite
	 */
	private void build(final Composite parent) {
		// Create the SashForm with HORIZONTAL
		centerSash = new SashForm(parent, SWT.VERTICAL);
		centerSash.setLayoutData(SWTUtil.createFillGridData());

		// Create center composite
		SashForm center = new SashForm(centerSash, SWT.HORIZONTAL | SWT.SMOOTH);
		center.setLayoutData(SWTUtil.createFillGridData());

		// Create left composite
		centerLeft = new Composite(center, SWT.NONE);
		centerLeft.setLayoutData(SWTUtil.createFillGridData());
		centerLeft.setLayout(new FillLayout());

		// Create right composite
		centerRight = new Composite(center, SWT.NONE);
		centerRight.setLayoutData(SWTUtil.createFillGridData());
		centerRight.setLayout(new FillLayout());

		// Create views
		layoutTopLeft = new LayoutTop(centerLeft, input, population, this);
		layoutTopRight = new LayoutTop(centerRight, output, population);
		// Create bottom composite
		final Composite compositeBottom = new Composite(centerSash, SWT.NONE);
		compositeBottom.setLayout(new FillLayout());
		final SashForm bottomSash = new SashForm(compositeBottom, SWT.HORIZONTAL | SWT.SMOOTH);

		bottomLeft = new Composite(bottomSash, SWT.NONE);
		bottomLeft.setLayout(new FillLayout());

		bottomRight = new Composite(bottomSash, SWT.NONE);
		bottomRight.setLayout(new FillLayout());

		/** Create views **/
		layoutBottomLeft = new LayoutBottom(bottomLeft, input, data, population, this);
		layoutBottomRight = new LayoutBottom(bottomRight, output, data, population);
		// Sync folders
		layoutBottomLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				layoutBottomRight.setSelectionIndex(layoutBottomLeft.getSelectionIndex());

				if (layoutBottomLeft.getSelectionIndex() == 3) {
					layoutTopLeft.setSelectionIndex(2);
					layoutTopRight.setSelectionIndex(2);
				} else if (layoutBottomLeft.getSelectionIndex() == 0) {
					layoutTopLeft.setSelectionIndex(3);
					layoutTopRight.setSelectionIndex(3);
				}
			}
		});
		layoutBottomRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				layoutBottomLeft.setSelectionIndex(layoutBottomRight.getSelectionIndex());

				if (layoutBottomRight.getSelectionIndex() == 0) {
					layoutTopLeft.setSelectionIndex(3);
					layoutTopRight.setSelectionIndex(3);
				}
			}
		});

		layoutTopLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				layoutTopRight.setSelectionIndex(layoutTopLeft.getSelectionIndex());

				if (layoutTopLeft.getSelectionIndex() == 2) {
					layoutBottomLeft.setSelectionIndex(3);
				} else if (layoutTopLeft.getSelectionIndex() == 3) {
					layoutBottomLeft.setSelectionIndex(0);
					layoutBottomRight.setSelectionIndex(0);
				}
			}
		});
		layoutTopRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				layoutTopLeft.setSelectionIndex(layoutTopRight.getSelectionIndex());

				if (layoutTopRight.getSelectionIndex() == 2) {
					layoutBottomLeft.setSelectionIndex(3);
				} else if (layoutTopRight.getSelectionIndex() == 3) {
					layoutBottomLeft.setSelectionIndex(0);
					layoutBottomRight.setSelectionIndex(0);
				}
			}
		});

		// Set sash weights
		centerSash.setWeights(new int[] { WEIGHT_TOP, WEIGHT_BOTTOM });
		bottomSash.setWeights(new int[] { WEIGHT_LEFT, WEIGHT_RIGHT });
		center.setWeights(new int[] { WEIGHT_LEFT, WEIGHT_RIGHT });
	}

	/**
	 * Updating of the Risk Part at the Quasi Identifier
	 * 
	 * @param attributeRisk
	 *            The Uniqueness Model used
	 */
	public void updateQuasiIdentifier(final String attributeRisk) {
		this.layoutTopLeft.update(attributeRisk);
		this.layoutTopRight.update(attributeRisk);
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
		this.layoutTopRight.handleThresholdUpdateInMonitors(recordsAtRisk, highestRisk, successRat);
	}

}