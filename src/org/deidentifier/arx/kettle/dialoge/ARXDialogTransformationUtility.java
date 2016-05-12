package org.deidentifier.arx.kettle.dialoge;

import java.util.List;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.metric.Metric;
import org.deidentifier.arx.metric.Metric.AggregateFunction;
import org.deidentifier.arx.metric.MetricDescription;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogTransformationUtility implements ARXPluginDialogInterface {


    /** Static settings. */
    private static final int                     LABEL_HEIGHT = 20;

    /** Static settings. */
    private static final List<MetricDescription> METRICS      = Metric.list();
    
    /** Static settings. */
    private static final String[]                LABELS       = getLabels(METRICS);
    
    
    /** View. */
    private Combo                 comboMetric;
    
    /** View. */
    private Composite             root;
    
    /** View. */
    private Button                monotonicVariant;

    /** View. */
    private Button                utilityBasedMicroaggregation;
    
    /** View. */
    private Combo                 comboAggregate;


    /**
     * Returns a list of names of all available metrics.
     *
     * @param metrics
     * @return
     */
    private static String[] getLabels(List<MetricDescription> metrics) {
        String[] labels = new String[metrics.size()];
        for (int i = 0; i < metrics.size(); i++) {
            labels[i] = metrics.get(i).getName();
        }
        return labels;
    }
	
    final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;
	final ARXDialogTransformation trans;
	
	public ARXDialogTransformationUtility(ARXDialogTransformation trans,final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.trans=trans;
		this.build();
	}
	
	public void build(){
		final Composite mBase = new Composite(parent, SWT.NONE);
        mBase.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());

        // Create metric combo
        final Label mLabel = new Label(mBase, SWT.PUSH);
        mLabel.setText(Messages.getString("ARXPluginDialog.transformation.utility.measure")); //$NON-NLS-1$
        GridData d2 = new GridData();
        d2.heightHint = LABEL_HEIGHT;
        d2.minimumHeight = LABEL_HEIGHT;
        d2.grabExcessVerticalSpace = true;
        d2.verticalAlignment = GridData.CENTER;
        mLabel.setLayoutData(d2);

        comboMetric = new Combo(mBase, SWT.READ_ONLY);
        GridData d30 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d30.horizontalSpan = 3;
        d30.verticalAlignment = GridData.CENTER;
        d30.grabExcessVerticalSpace = true;
        comboMetric.setLayoutData(d30);
        comboMetric.setItems(LABELS);
        comboMetric.select(0);
        comboMetric.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	updateControls();
                meta.setChanged(true);
            }
        });

        // Create monotonicity button
        final Label mLabel2 = new Label(mBase, SWT.PUSH);
        mLabel2.setText(Messages.getString("ARXPluginDialog.transformation.utility.monotonicity")); //$NON-NLS-1$
        GridData d22 = new GridData();
        d22.heightHint = LABEL_HEIGHT;
        d22.minimumHeight = LABEL_HEIGHT;
        d22.grabExcessVerticalSpace = true;
        d22.verticalAlignment = GridData.CENTER;
        mLabel2.setLayoutData(d22);

        monotonicVariant = new Button(mBase, SWT.CHECK);
        monotonicVariant.setText(Messages.getString("ARXPluginDialog.transformation.utility.monotonicity.2")); //$NON-NLS-1$
        monotonicVariant.setSelection(false);
        monotonicVariant.setEnabled(true);
        monotonicVariant.setLayoutData(GridDataFactory.swtDefaults().span(3, 1).grab(false, true).align(GridData.BEGINNING, GridData.CENTER).create());
        monotonicVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });

        // Create microaggreation button
        final Label mLabel4 = new Label(mBase, SWT.PUSH);
        mLabel4.setText(Messages.getString("ARXPluginDialog.transformation.utility.microaggregation")); //$NON-NLS-1$
        GridData d24 = new GridData();
        d24.heightHint = LABEL_HEIGHT;
        d24.minimumHeight = LABEL_HEIGHT;
        d24.grabExcessVerticalSpace = true;
        d24.verticalAlignment = GridData.CENTER;
        mLabel4.setLayoutData(d24);

        utilityBasedMicroaggregation = new Button(mBase, SWT.CHECK);
        utilityBasedMicroaggregation.setText(Messages.getString("ARXPluginDialog.transformation.utility.microaggregation.2")); //$NON-NLS-1$
        utilityBasedMicroaggregation.setSelection(false);
        utilityBasedMicroaggregation.setEnabled(true);
        utilityBasedMicroaggregation.setLayoutData(GridDataFactory.swtDefaults().span(3, 1).grab(false, true).align(GridData.BEGINNING, GridData.CENTER).create());
        utilityBasedMicroaggregation.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });

        // Create monotonicity button
        final Label mLabel3 = new Label(mBase, SWT.PUSH);
        mLabel3.setText(Messages.getString("ARXPluginDialog.transformation.utility.aggregate")); //$NON-NLS-1$
        GridData d23 = new GridData();
        d23.heightHint = LABEL_HEIGHT;
        d23.minimumHeight = LABEL_HEIGHT;
        d23.grabExcessVerticalSpace = true;
        d23.verticalAlignment = GridData.CENTER;
        mLabel3.setLayoutData(d23);

        comboAggregate = new Combo(mBase, SWT.READ_ONLY);
        GridData d31 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d31.horizontalSpan = 3;
        d31.grabExcessVerticalSpace = true;
        d31.verticalAlignment = GridData.CENTER;
        comboAggregate.setLayoutData(d31);
        comboAggregate.setEnabled(false);
        comboAggregate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
               meta.setChanged(true);
            }
        });
	}

	public void getData() {
		if(meta.getMetric()!=null){
			this.comboMetric.select(this.comboMetric.indexOf(meta.getMetric()));
		}
		this.monotonicVariant.setSelection(this.meta.isMonotonicVariant());
		this.utilityBasedMicroaggregation.setSelection(this.meta.isMicroaggregation());
		this.updateControls();
		if(comboAggregate.getItemCount()!=1&&meta.getAggregation()!=null&&this.comboAggregate.indexOf(meta.getAggregation())!=-1){
			this.comboAggregate.select(this.comboAggregate.indexOf(meta.getAggregation()));
		}
	}

	public void saveData() {
		MetricDescription description = METRICS.get(this.comboMetric.getSelectionIndex());
		this.meta.setMetric(this.comboMetric.getItem(this.comboMetric.getSelectionIndex()));
		if (description.isMonotonicVariantSupported()) {
			this.meta.setMonotonicVariant(this.monotonicVariant.getSelection());
		}
		this.meta.setMicroaggregation(this.utilityBasedMicroaggregation.getSelection());
		if(comboAggregate.getItemCount()!=1){
			this.meta.setAggregation(this.comboAggregate.getItem(this.comboAggregate.getSelectionIndex()));
		}
	}
	
	/**
     * This method updates the view
     */
    private void updateControls(){
        MetricDescription description = METRICS.get(this.comboMetric.getSelectionIndex());
        // Monotonicity
        if (!description.isMonotonicVariantSupported()) {
            this.monotonicVariant.setSelection(false);
            this.monotonicVariant.setEnabled(false);
        } else {
            this.monotonicVariant.setEnabled(true);
            this.monotonicVariant.setSelection(meta.isMonotonicVariant());
        }

        // Aggregate function
        comboAggregate.removeAll();
        int index = 0;
        int selected = -1;
        for (AggregateFunction function : description.getSupportedAggregateFunctions()) {
            comboAggregate.add(function.toString());
            if(meta.getAggregation()!=null){
            	if (function.toString().equals(meta.getAggregation())) {
                	selected = index;
            	}
            }
            index++;
        }
        if (selected != -1) {
            comboAggregate.select(selected);
        } 

        if (comboAggregate.getItemCount() == 0) {
            comboAggregate.add(Messages.getString("ARXPluginDialog.transformation.utility.none")); //$NON-NLS-1$
            comboAggregate.select(0);
            comboAggregate.setEnabled(false);
        }else{
        	comboAggregate.setEnabled(true);
        }
        if(description.isConfigurableCodingModelSupported()){
        	this.trans.coding.slider.setEnabled(true);
        }else{
        	this.trans.coding.slider.setEnabled(false);
        }
        
        if(description.isAttributeWeightsSupported()){
        	this.trans.attributeWeight.setEnabled(true);
        }else{
        	this.trans.attributeWeight.setEnabled(false);
        }
    }

}
