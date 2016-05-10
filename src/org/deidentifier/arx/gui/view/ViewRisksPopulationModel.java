package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.criteria.PrivacyCriterion;
import org.deidentifier.arx.gui.resources.Resources;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

public class ViewRisksPopulationModel {

	/** View */
	private Composite root;
	 private ARXResult result;
	 private Data data;
	 /** View */
    private DynamicTable     table;
    /** View */
    private Text             textSampleFraction;
    /** View */
    private Text             textPopulationSize;
    /** View */
    private Button           buttonUse;
    private boolean input;
    private ARXPopulationModel population;
    

	public ViewRisksPopulationModel(final Composite parent, ARXResult result, DataHandle result2, Data data,
			ARXConfiguration config, ARXPopulationModel population, boolean input) {
		this.result=result;
		this.data=data;
		this.input=input;
		this.population=population;
		root = parent;
        root.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());
		try {
			this.build(this.root);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void build(Composite parent) throws InterruptedException {
		 buttonUse = new Button(parent, SWT.CHECK);
	        buttonUse.setText(Resources.getMessage("ViewRisksPopulationModel.3")); //$NON-NLS-1$
	        buttonUse.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());
	        
	        Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Resources.getMessage("ViewRisksPopulationModel.4")); //$NON-NLS-1$
	        lbl1.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).create());
	        
	        table = SWTUtil.createTableDynamic(root, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.READ_ONLY);
	        table.setLayoutData(new GridData(GridData.FILL_BOTH));
	        table.setHeaderVisible(false);
	        table.setLinesVisible(true);

	        DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
	        c.setWidth("100%"); //$NON-NLS-1$ //$NON-NLS-2$
	        c.setText(""); //$NON-NLS-1$
	        c.setResizable(false);
	        
	        for (Region region : Region.values()) {
	            final TableItem item = new TableItem(table, SWT.NONE);
	            item.setText(region.getName());
	        }
	        
	        Label lbl2 = new Label(parent, SWT.NONE);
	        lbl2.setText(Resources.getMessage("ViewRisksPopulationModel.5")); //$NON-NLS-1$
	        
	        textSampleFraction = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        textSampleFraction.setText("0"); //$NON-NLS-1$
	        textSampleFraction.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        textSampleFraction.setEditable(false);
	        
	        Label lbl3 = new Label(parent, SWT.NONE);
	        lbl3.setText(Resources.getMessage("ViewRisksPopulationModel.7")); //$NON-NLS-1$
	        
	        textPopulationSize = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        textPopulationSize.setText("0"); //$NON-NLS-1$
	        textPopulationSize.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        textPopulationSize.setEditable(false);
	        
	        table.addListener(SWT.Selection, new Listener() {
	            @Override
	            public void handleEvent(Event event) {
	                event.detail = SWT.NONE;
	                event.type = SWT.None;
	                event.doit = false;
	                try
	                {
	                    table.setRedraw(false);
	                    table.deselectAll();
	                } finally {
	                    table.setRedraw(true);
	                    table.getParent().setFocus();
	                }
	            }
	        });
	        root.setRedraw(false);
	        SWTUtil.enable(root);
	        
	        boolean mayUseOutput = isOutputPopulationModelAvailable();
	        boolean enabled = !this.input ? mayUseOutput : !mayUseOutput;
	        this.buttonUse.setSelection(enabled);
	        
	        if (!this.input && !isOutputPopulationModelAvailable()) {
	            reset();
	        } else {

	            ARXPopulationModel popmodel = population;
	            if (!this.input && isOutputPopulationModelAvailable()) {
	                popmodel = getOutputPopulationModel();
	            }
	            
	            table.deselectAll();
	            TableItem selected = null;
	            for (TableItem item : table.getItems()) {
	                if (item.getText().equals(popmodel.getRegion().getName())) {
	                    item.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
	                    selected = item;
	                } else {
	                    item.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	                }
	            }
	            if (selected != null) {
	                table.showItem(selected);
	            }
	            table.getParent().setFocus();
	            DataHandle handle = data.getHandle();
	            long population = (long)popmodel.getPopulationSize();
	            double fraction = (double)handle.getNumRows() / (double)population;
	            textSampleFraction.setText(SWTUtil.getPrettyString(fraction));
	            textSampleFraction.setToolTipText(String.valueOf(fraction));
	            textSampleFraction.setEnabled(true);
	            textPopulationSize.setText(SWTUtil.getPrettyString(population));
	            textPopulationSize.setToolTipText(String.valueOf(population));
	            textPopulationSize.setEnabled(true);
	        }
	        root.setRedraw(true);
		

		root.layout();
	}
	 /**
     * Is an output model available
     * @return
     */
    private boolean isOutputPopulationModelAvailable() {
      
        for (PrivacyCriterion c : this.result.getConfiguration().getCriteria()) {
            if (c.getPopulationModel() != null) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the output population model, if any. Null otherwise.
     * @return
     */
    public ARXPopulationModel getOutputPopulationModel() {
            for (PrivacyCriterion c : this.result.getConfiguration().getCriteria()) {
                if (c.getPopulationModel() != null) {
                    return c.getPopulationModel();
                }
            }
        return null;
    }
    
    public void reset() {
        table.select(0);
        table.showSelection();
        textSampleFraction.setText(""); //$NON-NLS-1$
        textPopulationSize.setText(""); //$NON-NLS-1$
        SWTUtil.disable(root);
    }
	

}
