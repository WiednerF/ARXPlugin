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

import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

/**
 * This Class contains the Normal Tranformation Settings
 * @author Florian Wiedner
 * @since 1.7
 *
 */
public class ViewTransformationSettings implements LayoutCompositeInterface {
	/** Static settings. */
    private static final int      LABEL_WIDTH  = 50;
    
    /** Static settings. */
    private static final int      LABEL_HEIGHT = 20;
    
	private final ARXPluginMeta meta;
	
	 /** View */
    private Scale                 sliderOutliers;
    
    /** View. */
    private Label                 labelOutliers;
    
    /** View. */
    private Button                buttonPracticalMonotonicity;
    
    /** View. */
    private Button                precomputedVariant;
    
    /** View. */
    private Scale                 precomputationThreshold;
    
    /** View. */
    private Label                 labelThreshold;
    
	
	/**
	 * Generates the Setting GUI for the Transformation Settings with Loading of the Current Data for this
	 * @author Florian Wiedner
	 * @param parent The parent Composite with a Fill Layout
	 * @param meta The ARXMeta for the Data
	 */
	public ViewTransformationSettings(final Composite parent,ARXPluginMeta meta) {
		this.meta=meta;
		this.build(parent);
		this.getData();
	}
	
	/**
	 * This Function generates the Complete GUI for this Step
	 * @author Florian Wiedner
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param parent The Composite parent in which this GUI should be loaded
	 */
	private void build(final Composite parent){
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayout(SWTUtil.createGridLayout(4, false));

        // Create outliers slider
        final Label sLabel = new Label(group, SWT.PUSH);
        sLabel.setText(Resources.getMessage("CriterionDefinitionView.11")); //$NON-NLS-1$

        Composite outliersBase = new Composite(group, SWT.NONE);
        GridData baseData = SWTUtil.createFillHorizontallyGridData();
        baseData.horizontalSpan = 3;
        outliersBase.setLayoutData(baseData);
        outliersBase.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
        
        labelOutliers = new Label(outliersBase, SWT.BORDER | SWT.CENTER);
        GridData d2 = new GridData();
        d2.minimumWidth = LABEL_WIDTH;
        d2.widthHint = LABEL_WIDTH;
        d2.heightHint = LABEL_HEIGHT;
        labelOutliers.setLayoutData(d2);
        labelOutliers.setText("0%"); //$NON-NLS-1$

        sliderOutliers = new Scale(outliersBase, SWT.HORIZONTAL);
        sliderOutliers.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        sliderOutliers.setMaximum(100);
        sliderOutliers.setMinimum(0);
        sliderOutliers.setSelection(0);
        sliderOutliers.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                labelOutliers.setText(sliderOutliers.getSelection()+"%");
                buttonPracticalMonotonicity.setEnabled(sliderOutliers.getSelection()!=0);
                if(buttonPracticalMonotonicity.getEnabled()!=true){
                	buttonPracticalMonotonicity.setSelection(false);
                }
                meta.setChanged(true);
                meta.setMaxOutliers(sliderOutliers.getSelection()/100.0);
            }
        });

        // Build approximate button
        final Label m2Label = new Label(group, SWT.PUSH);
        m2Label.setText(Resources.getMessage("CriterionDefinitionView.31")); //$NON-NLS-1$
        d2 = new GridData();
        d2.heightHint = LABEL_HEIGHT;
        d2.minimumHeight = LABEL_HEIGHT;
        m2Label.setLayoutData(d2);

        final GridData d82 = SWTUtil.createFillHorizontallyGridData();
        d82.horizontalSpan = 3;
        buttonPracticalMonotonicity = new Button(group, SWT.CHECK);
        buttonPracticalMonotonicity.setText(Resources.getMessage("CriterionDefinitionView.53")); //$NON-NLS-1$
        buttonPracticalMonotonicity.setSelection(false);
        buttonPracticalMonotonicity.setEnabled(true);
        buttonPracticalMonotonicity.setLayoutData(d82);
        buttonPracticalMonotonicity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                buttonPracticalMonotonicity.setEnabled(sliderOutliers.getSelection()!=0);
                if(buttonPracticalMonotonicity.getEnabled()!=true){
                	buttonPracticalMonotonicity.setSelection(false);
                }else{
                	meta.setPracticalMonotonicity(buttonPracticalMonotonicity.getSelection());
                }
                meta.setChanged(true);
            }
        });

        
     // Create slider for precomputation threshold
        final Label labelPreComputation = new Label(group, SWT.PUSH);
        labelPreComputation.setText(Resources.getMessage("CriterionDefinitionView.71")); //$NON-NLS-1$

        precomputedVariant = new Button(group, SWT.CHECK);
        precomputedVariant.setText(Resources.getMessage("CriterionDefinitionView.70")); //$NON-NLS-1$
        precomputedVariant.setSelection(false);
        precomputedVariant.setEnabled(true);
        precomputedVariant.setLayoutData(GridDataFactory.swtDefaults().span(1, 1).create());
        precomputedVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
            	meta.setPrecomputed(precomputedVariant.getSelection());
                if (precomputedVariant.getSelection()) {
                    precomputationThreshold.setEnabled(true);
                } else {
                    precomputationThreshold.setEnabled(false);
                }
            }
        });
        
        labelThreshold = new Label(group, SWT.BORDER | SWT.CENTER);
        GridData d24 = new GridData();
        d24.minimumWidth = LABEL_WIDTH;
        d24.widthHint = LABEL_WIDTH;
        d24.heightHint = LABEL_HEIGHT;
        labelThreshold.setLayoutData(d24);
        labelThreshold.setText("0%"); //$NON-NLS-1$

        precomputationThreshold = new Scale(group, SWT.HORIZONTAL);
        precomputationThreshold.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        precomputationThreshold.setMaximum(100);
        precomputationThreshold.setMinimum(0);
        precomputationThreshold.setSelection(0);
        precomputationThreshold.setEnabled(false);
        precomputationThreshold.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
            	meta.setPrecomputedThreshold(precomputationThreshold.getSelection()/100.0);
            	labelThreshold.setText(precomputationThreshold.getSelection()+"%");
            }
        });
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		this.sliderOutliers.setSelection((int) (meta.getMaxOutliers()*100));
		this.labelOutliers.setText(((int) (meta.getMaxOutliers()*100)+"%"));
		if(meta.getMaxOutliers()!=0.0){
			this.buttonPracticalMonotonicity.setSelection(meta.isPracticalMonotonicity());
		}else{
			this.buttonPracticalMonotonicity.setEnabled(false);
			this.buttonPracticalMonotonicity.setSelection(false);
		}
		this.precomputedVariant.setSelection(meta.isPrecomputed());
		this.precomputationThreshold.setSelection((int) (meta.getPrecomputedThreshold()*100));
		this.labelThreshold.setText(((int) (meta.getPrecomputedThreshold()*100)+"%"));
		if(!meta.isPrecomputed()){
			this.precomputationThreshold.setEnabled(false);
		}else{
			this.precomputationThreshold.setEnabled(true);
		}
	}

}
