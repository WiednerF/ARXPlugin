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
import org.deidentifier.arx.kettle.risk.common.ClipboardHandlerTable;
import org.deidentifier.arx.risk.HIPAAIdentifierMatch;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

/**
 * The View of the Risks HIPAA
 * @author Florian Wiedner
 * @category Risk
 * @version 1.0
 * @since 1.7
 *
 */
public class ViewRisksHIPAA {
	
	/** View */
    private Composite root;
    
    /** View */
    private DynamicTable table;
    /**
     * The Data Handle
     */
    private Data data;
    /**
     * The Population Model
     */
    private ARXPopulationModel population;

    /**
     * The View of the HIPAA in a list Table
     * @param parent The Parent Composite
     * @param data The Data
     * @param population The Population Model
     */
	public ViewRisksHIPAA(final Composite parent,Data data,ARXPopulationModel population) {
		this.data=data;
		this.population=population;
		try{
		this.build(parent);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates the Table for HIPAA
	 * @param parent
	 */
	private void build(final Composite parent){
		this.root = new Composite(parent, SWT.NONE);
        this.root.setLayout(new FillLayout());
        this.root.setLayoutData(SWTUtil.createFillGridDataBoth());
        
        table = SWTUtil.createTableDynamic(root, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setMenu(new ClipboardHandlerTable(table).getMenu());
        table.setSize(SWT.MAX, SWT.MAX);
        
        
        DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
        c.setWidth("20%%"); //$NON-NLS-1$
        c.setText(Resources.getMessage("RiskAnalysis.27")); //$NON-NLS-1$
        c.setResizable(true);
        c = new DynamicTableColumn(table, SWT.LEFT);
        c.setWidth("20%"); //$NON-NLS-1$
        c.setText(Resources.getMessage("RiskAnalysis.28")); //$NON-NLS-1$
        c.setResizable(true);
        c = new DynamicTableColumn(table, SWT.LEFT);
        c.setWidth("20%%"); //$NON-NLS-1$
        c.setText(Resources.getMessage("RiskAnalysis.31")); //$NON-NLS-1$
        c.setResizable(true);
        c = new DynamicTableColumn(table, SWT.LEFT);
        c.setWidth("20%"); //$NON-NLS-1$
        c.setText(Resources.getMessage("RiskAnalysis.29")); //$NON-NLS-1$
        c.setResizable(true);
        c = new DynamicTableColumn(table, SWT.LEFT);
        SWTUtil.createColumnWithBarCharts(table, c);
        c.setWidth("20%"); //$NON-NLS-1$
        c.setText(Resources.getMessage("RiskAnalysis.30")); //$NON-NLS-1$
        c.setResizable(true);
        for (final TableColumn col : table.getColumns()) {
            col.pack();
        }
        table.computeSize(SWT.MAX,SWT.BOTTOM);
        
        
        SWTUtil.createGenericTooltip(table);
        
        Thread getData=new Thread(new Runnable(){

			@Override
			public void run() {
				DataHandle handle=data.getHandle();
		        RiskEstimateBuilder builder= handle.getRiskEstimator(population);
		        HIPAAIdentifierMatch[] matches = builder.getHIPAAIdentifiers();
		        // For all identifiers
		        for (HIPAAIdentifierMatch item : matches) {
		            createItem(item);
		        }
		       
		        table.redraw();
		        table.layout();
		        parent.layout();
		        //TODO NullPointer Exception
			}
        	
        });
        getData.run();
	}
	
	
	/**
     * Creates a table item
     * @param risks
     */
    private void createItem(HIPAAIdentifierMatch identifier) {
        final TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, identifier.getColumn());
        item.setText(1, format(identifier.getIdentifier().toString()));
        item.setText(2, identifier.getInstance());
        item.setText(3, format(identifier.getMatchType().toString()));
        if (identifier.getConfidence() != null) {
            item.setData("4", identifier.getConfidence());
        } else {
            item.setText(4, identifier.getValue());
        }
    }
    
    /**
     * Converts the given value
     * @param value
     * @return
     */
    private String format(String value) {
        value = value.toLowerCase().replace("_", " ");
        value = value.substring(0, 1).toUpperCase() + value.substring(1);
        return value;
    }

}
