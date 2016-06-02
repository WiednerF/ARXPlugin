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

import org.deidentifier.arx.gui.view.LayoutRisks;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Changing of the Uniqueness Model for the QuasiIdentifierTable and Updating the Table
 * Control Table for this.
 * @author Florian Wiedner
 * @category Risk
 * @version 1.0
 * @since 1.7
 *
 */
public class ViewRisksQuasiIdentifier {

	/** View */
	private Composite root;
	/**View**/
	private final String[] index = new String[] { "SAMPLE_UNIQUENESS", "POPULATION_UNIQUENESS_PITMAN",
			"POPULATION_UNIQUENESS_ZAYATZ", "POPULATION_UNIQUENESS_SNB", "POPULATION_UNIQUENESS_SNB",
			"POPULATION_UNIQUENESS_DANKAR" };
	/**View**/
	private final String[] values = new String[] { "Sample Uniqueness", "Population Uniqueness PITMAN",
			"Population Uniqueness ZAYATZ", "Population Uniqueness SNB", "Population Uniqueness SNB",
			"Population Uniqueness DANKAR" };
	/** View */
	private Table table;
	/**
	 * The LayoutRisk Parent for the Updating of the Table
	 */
	private final LayoutRisks riskParent;
	/**
	 * The Actual UNIQUENESS Model used
	 */
	private String actual;

	/**
	 * The Creating of the View for this part of the Control
	 * @param parent The Parent Composite
	 * @param riskParent The RiskParent Layout for Updating
	 */
	public ViewRisksQuasiIdentifier(final Composite parent, LayoutRisks riskParent) {
		this.riskParent = riskParent;
		this.actual = "Sample Uniqueness";
		this.build(parent);
	}

	/**
	 * The View Creating
	 * @param parent The Parent Composite
	 */
	private void build(Composite parent) {
		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(new FillLayout());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		// Create table
		table = SWTUtil.createTable(parent, SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				TableItem itemSelected = (TableItem) arg0.item;
				if (!actual.equals(itemSelected.getText())) {
					for (TableItem item : table.getItems()) {
						if (!item.equals(itemSelected) && item.getChecked()) {
							item.setChecked(false);
						}
					}

					actual = itemSelected.getText();
					root.layout();
					table.redraw();
					fireEvent();
				}
			}
		});
		for (int i = 0; i < this.values.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(this.values[i]);
			item.setChecked((i == 0 ? true : false));
		}

		root.layout();
	}

	/**
	 * Updates the QuasiIdentifierTable after Change of the Uniqueness Model
	 */
	private void fireEvent() {
		String selection = "";
		int i = 0;
		for (String temp : values) {
			System.out.println(actual);
			if (actual.equals(temp)) {
				selection = index[i];
				break;
			}
			i++;
		}
		if (!selection.equals("")) {
			riskParent.updateQuasiIdentifier(selection);
		}

	}

}
