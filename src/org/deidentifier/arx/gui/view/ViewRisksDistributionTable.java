package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskEstimateBuilderInterruptible;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.Range;

import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

import org.swtchart.ISeries.SeriesType;


public class ViewRisksDistributionTable {
	
	/** Minimal width of a category label. */
    private static final int MIN_CATEGORY_WIDTH = 10;
	
	final private ARXResult result;
	/** View */
    private Composite root;
    
    /** View */
    private Chart            chart;
    

    private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private boolean input;
    private DynamicTable table;
    private DataHandle result2;
    
	public ViewRisksDistributionTable(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input) {
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
        	builder=this.data.getHandle().getRiskEstimator(population);//TODO Error (Failure Ask)
        }else{
        	builder=this.result2.getRiskEstimator(population);
        }
        if(builder!=null){

              int[]                    distribution;
              double                   numClasses;
             // Perform work
              // Perform work
              RiskModelHistogram model = builder.getEquivalenceClassModel();
              distribution = model.getHistogram();
              numClasses = model.getNumClasses();

              for (int i = 0; i < distribution.length; i += 2) {
                  TableItem item = new TableItem(table, SWT.NONE);
                  item.setText(0, String.valueOf(distribution[i]));
                  item.setText(1, String.valueOf(distribution[i + 1]));
                  item.setData("2", (double) distribution[i + 1] / numClasses);
              }

              root.layout();
        }
	}
	
	
	

}
