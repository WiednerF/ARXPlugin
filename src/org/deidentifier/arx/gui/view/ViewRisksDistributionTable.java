package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleRiskDistribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.swtchart.Chart;
import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;


public class ViewRisksDistributionTable {
	
	/** View */
    private Composite root;
    private Data data;
    private ARXPopulationModel population;
    private boolean input;
    private DynamicTable table;
    private DataHandle result2;
    
	public ViewRisksDistributionTable(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input) {
		this.result2=result2;
		this.data=data;
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
        this.root.setLayout(new FillLayout());
        this.root.setLayoutData(SWTUtil.createFillGridDataBoth());
        
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

       
     // Enable/disable
        final RiskEstimateBuilder builder;
        if(this.input!=true){
        	builder=this.data.getHandle().getRiskEstimator(population);
        }else{
        	builder=this.result2.getRiskEstimator(population);
        }
        if(builder!=null){

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
                labels[i] = String.valueOf(SWTUtil.getPrettyString(model.getAvailableRiskThresholds()[i] * 100d));
            }
            labels[0] = "<=" + SWTUtil.getPrettyString(1e-6); //$NON-NLS-1$


         // Create entries
            for (int i = labels.length-1; i >=0 ; i--) {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, labels[i]);
                item.setData("1", frequencies[i]);
                item.setData("2", cumulative[i]);
            }

            root.layout();
        }
	}
	
	
	

}
