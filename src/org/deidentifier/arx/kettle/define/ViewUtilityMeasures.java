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
package org.deidentifier.arx.kettle.define;

import java.util.List;

import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.dialoge.ARXDialogTransformation;
import org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface;
import org.deidentifier.arx.metric.Metric;
import org.deidentifier.arx.metric.Metric.AggregateFunction;
import org.deidentifier.arx.metric.MetricDescription;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Represents the ViewModel for the Utility Measure Tab and Saving of the Data
 * @author Florian Wiedner
 * @category ARXDialogTransformation
 * @since 1.7
 */
public class ViewUtilityMeasures implements ARXPluginDialogInterface {


    /** Static settings. */
    private static final int                     LABEL_HEIGHT = 20;

    /** Static settings. */
    private static final List<MetricDescription> METRICS      = Metric.list();
    
    /** Static settings. */
    private static final String[]                LABELS       = getLabels(METRICS);
    
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
    
    
    /** View. */
    private Combo                 comboMetric;
    
    /** View. */
    private Button                monotonicVariant;

    /** View. */
    private Button                utilityBasedMicroaggregation;
    
    /** View. */
    private Combo                 comboAggregate;
    
    /**
     * The Meta Information for getting and saving of Data
     */
	private final ARXPluginMeta meta;
	
	/**
	 * The Transformation Overview Class for Changing AttributWeight and CodingModel
	 */
	private final ARXDialogTransformation trans;
	
	/**
	 * Creates a new Tab and add it to the Composite parent
	 * @param trans The Transformation View (Parent)
	 * @param parent The Parent Composite
	 * @param meta The Meta Informations for the Project
	 */
	public ViewUtilityMeasures(final ARXDialogTransformation trans,final Composite parent,final ARXPluginMeta meta) {
		this.meta=meta;
		this.trans=trans;
		this.build(parent);
		this.getData();
	}
	
	/**
	 * Builds the new Tab with the Data and Enable Saving
	 * @param parent The Parent Composite for adding to the Overall view
	 * @author Florian Wiedner
	 * @since 1.7
	 * @category ViewUtilityMeasure
	 */
	private void build(final Composite parent){
		final Composite mBase = new Composite(parent, SWT.NONE);
        mBase.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());

        // Create metric combo
        final Label mLabel = new Label(mBase, SWT.PUSH);
        mLabel.setText(Resources.getMessage("CriterionDefinitionView.32")); //$NON-NLS-1$
        GridData d2 = new GridData();
        d2.heightHint = LABEL_HEIGHT;
        d2.minimumHeight = LABEL_HEIGHT;
        d2.grabExcessVerticalSpace = true;
        d2.verticalAlignment = GridData.CENTER;
        mLabel.setLayoutData(d2);

        comboMetric = new Combo(mBase, SWT.READ_ONLY);
        GridData d30 = SWTUtil.createFillHorizontallyGridData();
        d30.horizontalSpan = 3;
        d30.verticalAlignment = GridData.CENTER;
        d30.grabExcessVerticalSpace = true;
        comboMetric.setLayoutData(d30);
        comboMetric.setItems(LABELS);
        comboMetric.select(0);
        comboMetric.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
        		meta.setMetric(comboMetric.getItem(comboMetric.getSelectionIndex()));
                meta.setChanged(true);
                getData();
            }
        });

        // Create monotonicity button
        final Label mLabel2 = new Label(mBase, SWT.PUSH);
        mLabel2.setText(Resources.getMessage("CriterionDefinitionView.67")); //$NON-NLS-1$
        GridData d22 = new GridData();
        d22.heightHint = LABEL_HEIGHT;
        d22.minimumHeight = LABEL_HEIGHT;
        d22.grabExcessVerticalSpace = true;
        d22.verticalAlignment = GridData.CENTER;
        mLabel2.setLayoutData(d22);

        monotonicVariant = new Button(mBase, SWT.CHECK);
        monotonicVariant.setText(Resources.getMessage("CriterionDefinitionView.68")); //$NON-NLS-1$
        monotonicVariant.setSelection(false);
        monotonicVariant.setEnabled(true);
        monotonicVariant.setLayoutData(GridDataFactory.swtDefaults().span(3, 1).grab(false, true).align(GridData.BEGINNING, GridData.CENTER).create());
        monotonicVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	MetricDescription description = METRICS.get(comboMetric.getSelectionIndex());
        		if (description.isMonotonicVariantSupported()) {
        			meta.setMonotonicVariant(monotonicVariant.getSelection());
        		}
                meta.setChanged(true);
            }
        });

        // Create microaggreation button
        final Label mLabel4 = new Label(mBase, SWT.PUSH);
        mLabel4.setText(Resources.getMessage("CriterionDefinitionView.90")); //$NON-NLS-1$
        GridData d24 = new GridData();
        d24.heightHint = LABEL_HEIGHT;
        d24.minimumHeight = LABEL_HEIGHT;
        d24.grabExcessVerticalSpace = true;
        d24.verticalAlignment = GridData.CENTER;
        mLabel4.setLayoutData(d24);

        utilityBasedMicroaggregation = new Button(mBase, SWT.CHECK);
        utilityBasedMicroaggregation.setText(Resources.getMessage("CriterionDefinitionView.91")); //$NON-NLS-1$
        utilityBasedMicroaggregation.setSelection(false);
        utilityBasedMicroaggregation.setEnabled(true);
        utilityBasedMicroaggregation.setLayoutData(GridDataFactory.swtDefaults().span(3, 1).grab(false, true).align(GridData.BEGINNING, GridData.CENTER).create());
        utilityBasedMicroaggregation.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
        		meta.setMicroaggregation(utilityBasedMicroaggregation.getSelection());
                meta.setChanged(true);
            }
        });

        // Create monotonicity button
        final Label mLabel3 = new Label(mBase, SWT.PUSH);
        mLabel3.setText(Resources.getMessage("CriterionDefinitionView.72")); //$NON-NLS-1$
        GridData d23 = new GridData();
        d23.heightHint = LABEL_HEIGHT;
        d23.minimumHeight = LABEL_HEIGHT;
        d23.grabExcessVerticalSpace = true;
        d23.verticalAlignment = GridData.CENTER;
        mLabel3.setLayoutData(d23);

        comboAggregate = new Combo(mBase, SWT.READ_ONLY);
        GridData d31 = SWTUtil.createFillHorizontallyGridData();
        d31.horizontalSpan = 3;
        d31.grabExcessVerticalSpace = true;
        d31.verticalAlignment = GridData.CENTER;
        comboAggregate.setLayoutData(d31);
        comboAggregate.setEnabled(false);
        comboAggregate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	if(comboAggregate.getItemCount()!=1){
        			meta.setAggregation(comboAggregate.getItem(comboAggregate.getSelectionIndex()));
        		}
               meta.setChanged(true);
            }
        });
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
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
		//TODO Delete
	}
	
	/**
	 * This Methode updates the view with the new Metric Model
	 * @author Florian Wiedner
	 * @since 1.7
	 * @category ViewUtilityMeasure
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
            comboAggregate.add(Resources.getMessage("ViewMetric.0")); //$NON-NLS-1$
            comboAggregate.select(0);
            comboAggregate.setEnabled(false);
        }else{
        	comboAggregate.setEnabled(true);
        }
        //Updating Metric for AttributeWeight and CodingModel
        this.trans.changeMetric(description);
    }

}
