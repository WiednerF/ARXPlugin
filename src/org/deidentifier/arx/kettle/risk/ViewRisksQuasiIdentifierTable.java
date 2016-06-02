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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.risk.common.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelAttributes;
import org.deidentifier.arx.risk.RiskModelAttributes.QuasiIdentifierRisk;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

/**
 * This Class contains the QuasiIdentifier Table for the ARX Risk Analysis
 * @author Florian Wiedner
 * @version 1.0
 * @since 1.7
 * @category risk
 *
 */
public class ViewRisksQuasiIdentifierTable {

	/** View */
	private Composite root;
	/*
	 * The ARX Population Model for the Population of a Region
	 */
	private final ARXPopulationModel population;
	/**View**/
	private DynamicTable table;
	/**
	 * The DataHandle for In - Or Output
	 */
	private final DataHandle result;

	/**
	 * Creates the View of the new QuasiIdentifierTable
	 * @param parent The Parent Composite
	 * @param result The Result for In- Or Output
	 * @param population The PopulationModel Used
	 */
	public ViewRisksQuasiIdentifierTable(final Composite parent, DataHandle result,
			 ARXPopulationModel population) {
		this.result = result;
		this.population = population;
		this.build(parent);
	}

	/**
	 * Creates the View
	 * @param parent The Parent Composite
	 */
	private void build(Composite parent) {
		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(new FillLayout());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		table = SWTUtil.createTableDynamic(root, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setMenu(new ClipboardHandlerTable(table).getMenu());

		DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
		c.setWidth("70%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.19")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.20")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.21")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.22")); //$NON-NLS-1$
		c.setResizable(true);
		for (final TableColumn col : table.getColumns()) {
			col.pack();
		}
		SWTUtil.createGenericTooltip(table);
		update("SAMPLE_UNIQUENESS");
	
	}
	
	/**
	 * Updates this View for the new Attribute Risks
	 * @param attributeRisk The Attribute Risk from Outside
	 */
	public void update(final String attributeRisk){
		
					final RiskEstimateBuilder builder;
					builder = result.getRiskEstimator(population);
					if (builder != null) {

						RiskModelAttributes      risks;
						switch (attributeRisk) {
			            case "SAMPLE_UNIQUENESS":
			                risks = builder.getSampleBasedAttributeRisks();
			                break;
			            case "POPULATION_UNIQUENESS_PITMAN":
			                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.PITMAN);
			                break;
			            case "POPULATION_UNIQUENESS_ZAYATZ":
			                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.ZAYATZ);
			                break;
			            case "POPULATION_UNIQUENESS_SNB":
			                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.SNB);
			                break;
			            case "POPULATION_UNIQUENESS_DANKAR":
			                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.DANKAR);
			                break;
			            default:
			                throw new RuntimeException("Invalid risk model"); //$NON-NLS-1$
			            }
						for (final TableItem i : table.getItems()) {
				            i.dispose();
				        }

		                // For all sizes
		                for (QuasiIdentifierRisk item : risks.getAttributeRisks()) {
		                    createItem(item);
		                }

		                for (final TableColumn col : table.getColumns()) {
		                    col.pack();
		                }

		                table.layout();
		                table.redraw();
						root.layout();
					}				
	}
	
	 /**
     * Creates a table item
     * @param risks
     */
    private void createItem(QuasiIdentifierRisk risks) {
        final TableItem item = new TableItem(table, SWT.NONE);
        List<String> list = new ArrayList<String>();
        list.addAll(risks.getIdentifier());
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1){
                builder.append(", "); //$NON-NLS-1$
            }
        }
        item.setText(0, builder.toString());
        item.setData("1", risks.getFractionOfUniqueTuples());
        item.setData("2", risks.getHighestReidentificationRisk());
        item.setData("3", risks.getAverageReidentificationRisk());
    }

}
