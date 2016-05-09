package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogTransformationGeneral implements ARXPluginDialogInterface {
	/** Static settings. */
    private static final int      LABEL_WIDTH  = 50;
    
    /** Static settings. */
    private static final int      LABEL_HEIGHT = 20;
	
	final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	
	 /** View */
    private Scale                 sliderOutliers;
    
    /** View. */
    private Label                 labelOutliers;
    
    /** View. */
    private Button                buttonPracticalMonotonicity;
    
    /** View. */
    private Composite             root;
    
    /** View. */
    private Button                precomputedVariant;
    
    /** View. */
    private Scale                 precomputationThreshold;
    
    /** View. */
    private Label                 labelThreshold;
    
	
	
	public ARXDialogTransformationGeneral(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.build();
	}
	
	public void build(){
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayout(ARXDialogGeneralTab.createGridLayout(4, false));

        // Create outliers slider
        final Label sLabel = new Label(group, SWT.PUSH);
        sLabel.setText(Messages.getString("ARXPluginDialog.transformation.general.supression")); //$NON-NLS-1$

        Composite outliersBase = new Composite(group, SWT.NONE);
        GridData baseData = ARXDialogGeneralTab.createFillHorizontallyGridData();
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
        sliderOutliers.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
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
            }
        });

        // Build approximate button
        final Label m2Label = new Label(group, SWT.PUSH);
        m2Label.setText(Messages.getString("ARXPluginDialog.transformation.general.approximate")); //$NON-NLS-1$
        d2 = new GridData();
        d2.heightHint = LABEL_HEIGHT;
        d2.minimumHeight = LABEL_HEIGHT;
        m2Label.setLayoutData(d2);

        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d82.horizontalSpan = 3;
        buttonPracticalMonotonicity = new Button(group, SWT.CHECK);
        buttonPracticalMonotonicity.setText(Messages.getString("ARXPluginDialog.transformation.general.approximate.2")); //$NON-NLS-1$
        buttonPracticalMonotonicity.setSelection(false);
        buttonPracticalMonotonicity.setEnabled(true);
        buttonPracticalMonotonicity.setLayoutData(d82);
        

        // Create slider for precomputation threshold
        final Label sLabel2 = new Label(group, SWT.PUSH);
        sLabel2.setText(Messages.getString("ARXPluginDialog.transformation.general.precomputation")); //$NON-NLS-1$

        precomputedVariant = new Button(group, SWT.CHECK);
        precomputedVariant.setText(Messages.getString("ARXPluginDialog.transformation.general.enable")); //$NON-NLS-1$
        precomputedVariant.setSelection(false);
        precomputedVariant.setEnabled(true);
        precomputedVariant.setLayoutData(GridDataFactory.swtDefaults().span(1, 1).create());
        precomputedVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
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
        precomputationThreshold.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
        precomputationThreshold.setMaximum(100);
        precomputationThreshold.setMinimum(0);
        precomputationThreshold.setSelection(0);
        precomputationThreshold.setEnabled(false);
        precomputationThreshold.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
            	labelThreshold.setText(precomputationThreshold.getSelection()+"%");
            }
        });
	}

	public void getData() {
		System.out.println((int) (meta.getMaxOutliers()*100));
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

	public void saveData() {
		this.meta.setMaxOutliers(this.sliderOutliers.getSelection()/100.0);
		this.meta.setPracticalMonotonicity(this.buttonPracticalMonotonicity.getSelection());
		this.meta.setPrecomputed(this.precomputedVariant.getSelection());
		this.meta.setPrecomputedThreshold(this.precomputationThreshold.getSelection()/100.0);
	}

}
