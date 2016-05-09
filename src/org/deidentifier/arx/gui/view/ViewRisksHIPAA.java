package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.ARXSolverConfiguration;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.risk.HIPAAIdentifierMatch;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskEstimateBuilderInterruptible;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

public class ViewRisksHIPAA {
	
	final private ARXResult result;
	/** View */
    private Composite root;
    
    /** View */
    private DynamicTable table;
    private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private DataHandle result2;

	public ViewRisksHIPAA(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population) {
		this.result=result;
		this.result2=result2;
		this.data=data;
		this.config=config;
		this.population=population;
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
        
        table = SWTUtil.createTableDynamic(root, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
        
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setMenu(new org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable(table).getMenu());
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
        
        DataHandle handle=data.getHandle();
        RiskEstimateBuilder builder= handle.getRiskEstimator(this.population);
        HIPAAIdentifierMatch[] matches = builder.getHIPAAIdentifiers();
        // For all identifiers
        for (HIPAAIdentifierMatch item : matches) {
            createItem(item);
        }
       
        table.redraw();
        table.layout();
        parent.layout();
        //TODO find Error of the Values (No Values there)
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
