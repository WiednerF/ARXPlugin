package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class ViewRisksQuasiIdentifier {

	/** View */
	private Composite root;
	private String[] index = new String[] { "SAMPLE_UNIQUENESS", "POPULATION_UNIQUENESS_PITMAN",
			"POPULATION_UNIQUENESS_ZAYATZ", "POPULATION_UNIQUENESS_SNB", "POPULATION_UNIQUENESS_SNB",
			"POPULATION_UNIQUENESS_DANKAR" };
	private String[] values = new String[] { "Sample Uniqueness", "Population Uniqueness PITMAN",
			"Population Uniqueness ZAYATZ", "Population Uniqueness SNB", "Population Uniqueness SNB",
			"Population Uniqueness DANKAR" };
	/** View */
	private Table table;
	private LayoutRisks riskParent;
	private String actual;

	public ViewRisksQuasiIdentifier(final Composite parent, ARXResult result, DataHandle result2, Data data,
			ARXConfiguration config, ARXPopulationModel population, boolean input, LayoutRisks riskParent) {
		this.riskParent = riskParent;
		this.actual = "Sample Uniqueness";
		try {
			this.build(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void build(Composite parent) throws InterruptedException {
		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(new FillLayout());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		// Create table
		table = SWTUtil.createTable(parent, SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				for (TableItem item : table.getItems()) {
					if (item.getChecked() && item.getText().equals(actual)) {
						item.setChecked(false);
					} else if (item.getChecked()) {
						actual = item.getText();
					}
				}
				fireEvent();
			}
		});
		for (int i = 0; i < this.values.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(this.values[i]);
			item.setChecked((i == 0 ? true : false));
		}

		root.layout();
	}

	private void fireEvent() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String selection = "";
				int i = 0;
				for (String temp : values) {
					if (actual.equals(temp)) {
						selection = index[i];
						break;
					}
					i++;
				}
				if (!selection.equals("")) {
					riskParent.layoutTopLeft.quasiIdentifier.update(selection);
					riskParent.layoutTopRight.quasiIdentifier.update(selection);
				}

			}

		});
		thread.start();
	}

}
