package org.deidentifier.arx.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.deidentifier.arx.risk.RiskModelSampleRisks;
import org.deidentifier.arx.risk.RiskModelSampleUniqueness;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;


public class ViewRisksReidentificationRisksTable {
	final private ARXResult result;
	/** View */
    private Composite root;
    

    private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private boolean input;
    private DynamicTable table;
    private DataHandle result2;
    
	public ViewRisksReidentificationRisksTable(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input) {
		this.result=result;
		this.result2=result2;
		this.data=data;
		this.config=config;
		this.population=population;
		this.input=input;
		try{
		this.build(parent);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void build(Composite parent) throws InterruptedException{
        this.root = new Composite(parent, SWT.NONE);
        this.root.setLayout(SWTUtil.createGridLayout(1));
        this.root.setLayoutData(SWTUtil.createFillGridDataBoth());
        
        table = SWTUtil.createTableDynamic(root, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setMenu(new ClipboardHandlerTable(table).getMenu());
        table.setLayoutData(SWTUtil.createFillGridData());

        DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
        c.setWidth("50%", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
        c.setText(Resources.getMessage("RiskAnalysis.6")); //$NON-NLS-1$
        c = new DynamicTableColumn(table, SWT.LEFT);
        SWTUtil.createColumnWithBarCharts(table, c);
        c.setWidth("50%", "100px"); //$NON-NLS-1$ //$NON-NLS-2$
        c.setText(Resources.getMessage("RiskAnalysis.7")); //$NON-NLS-1$
        for (final TableColumn col : table.getColumns()) {
            col.pack();
        }
        SWTUtil.createGenericTooltip(table);

       
     // Enable/disable
        final RiskEstimateBuilder builder;
        if(this.input!=true){
        	builder=this.data.getHandle().getRiskEstimator(population);
        }else{
        	builder=this.result2.getRiskEstimator(population);
        }
        if(builder!=null){

        	double                     lowestRisk;
             double                     fractionOfTuplesAffectedByLowestRisk;
           double                     averageRisk;
           double                     highestRisk;
         double                     fractionOfTuplesAffectedByHighestRisk;
           double                     fractionOfUniqueTuples;
          double                     fractionOfUniqueTuplesDankar;
            PopulationUniquenessModel populationModel;
             

            // Perform work
            RiskModelSampleRisks samReidModel = builder.getSampleBasedReidentificationRisk();
            RiskModelSampleUniqueness samUniqueModel = builder.getSampleBasedUniquenessRisk();
            RiskModelPopulationUniqueness popUniqueModel = builder.getPopulationBasedUniquenessRisk();
            
            lowestRisk = samReidModel.getLowestRisk();
            fractionOfTuplesAffectedByLowestRisk = samReidModel.getFractionOfTuplesAffectedByLowestRisk();
            averageRisk = samReidModel.getAverageRisk();
            highestRisk = samReidModel.getHighestRisk();
            fractionOfTuplesAffectedByHighestRisk = samReidModel.getFractionOfTuplesAffectedByHighestRisk();
            fractionOfUniqueTuples = samUniqueModel.getFractionOfUniqueTuples();
            fractionOfUniqueTuplesDankar = popUniqueModel.getFractionOfUniqueTuplesDankar();
            populationModel = popUniqueModel.getPopulationUniquenessModel();
            
            table.setRedraw(false);
            for (final TableItem i : table.getItems()) {
                i.dispose();
            }
            
            createItem(Resources.getMessage("RiskAnalysis.14"), lowestRisk); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.17"), fractionOfTuplesAffectedByLowestRisk); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.8"), averageRisk); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.9"), highestRisk); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.10"), fractionOfTuplesAffectedByHighestRisk); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.11"), fractionOfUniqueTuples); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.12"), fractionOfUniqueTuplesDankar); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.18"), populationModel); //$NON-NLS-1$
            createItem(Resources.getMessage("RiskAnalysis.25"), getQuasiIdentifiers()); //$NON-NLS-1$

            table.setRedraw(true);
              root.layout();
        }
	}
	
	 /**
     * Creates a table item
     * @param label
     * @param value
     */
    private void createItem(String label, double value) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, label);
        item.setData("1", value);
    }
    
    /**
     * Creates a table item
     * @param label
     * @param value
     */
    private void createItem(String label, PopulationUniquenessModel value) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, label);
        item.setText(1, value == null ? "N/A" : value.toString()); //$NON-NLS-1$
    }
    
    /**
     * Creates a table item
     * @param label
     * @param value
     */
    private void createItem(String label, String value) {
        TableItem item = new TableItem(table, SWT.NONE);
        item.setText(0, label);
        item.setText(1, value);
    }
    
    /**
     * Returns a string containing all quasi-identifiers
     * @param context
     * @return the String of all QuasiIdentifying Attributes
     */
    protected String getQuasiIdentifiers() {
      
        List<String> list = new ArrayList<String>();
        list.addAll(data.getDefinition().getQuasiIdentifyingAttributes());
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1){
                builder.append(", "); //$NON-NLS-1$
            }
        }
        return builder.toString();
    }
	
	
	

}
