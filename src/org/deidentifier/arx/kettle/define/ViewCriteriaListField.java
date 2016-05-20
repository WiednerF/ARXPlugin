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
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.meta.ARXFields;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Props;
import org.pentaho.di.ui.core.PropsUI;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

/**
 * This Class Contains the View of the Privacy Criterion of the Field Wise Attributes
 * @author Florian Wiedner
 * @since 1.7
 * @category LayoutField
 * @version 1.0
 *
 */
public class ViewCriteriaListField implements LayoutCompositeInterface {
	
	/**
	 * The Meta Information for this Project
	 */
	private final ARXPluginMeta meta;
	
	/** View */
    private final Composite                                        root;
    /** Color profile */
    private final KnobColorProfile defaultColorProfile;
    /** Color profile */
    private final KnobColorProfile focusedColorProfile;
    
    /** View */
    private Button btnDDisclosure,btnTCloseness,btnLDiversity;
    /** View */
    private Text labelDDisclosure,labelTCloseness,labelLDiversity,labelLDiversityC;
    /** View */
    private Knob<Double> knobDDisclosure,knobTCloseness,knobLDiversityC;
    /** View */
    private Combo comboTCloseness,comboLDiversityVariant;
    /** View */
    private Knob<Integer> knobLDiversity;
    /**
     * The Actual Fieldname for the View
     */
    private String fieldName;
    
	/** Images **/
	private final Image symbolL = Resources.getImage("symbol_l.png"); //$NON-NLS-1$
	/** Images **/
	private final Image symbolT = Resources.getImage("symbol_t.png"); //$NON-NLS-1$
	/** Images **/
	private final Image symbolD = Resources.getImage("symbol_d.png"); //$NON-NLS-1$
	
	/**
	 * Creates the View for the FieldPrivacy
	 * @param parent The Parent Composite
	 * @param meta The Meta for this Project
	 * @param props The PropsUI from the KettleProject
	 * @param fieldName The Starting FieldName
	 */
	public ViewCriteriaListField(final Composite parent,ARXPluginMeta meta, final PropsUI props,String fieldName) {
		this.meta=meta;
		this.fieldName=fieldName;
		
		this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());
		
		 CTabFolder wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      wTabFolder.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	      
	      
	      CTabItem cTabPrivacy = new CTabItem( wTabFolder, SWT.NONE );
	      cTabPrivacy.setText( Resources.getMessage("CriterionSelectionDialog.4") );
	      wTabFolder.setSelection(0);
	      Composite cTabPrivacyComp = new Composite( wTabFolder, SWT.NONE );
	      props.setLook(  cTabPrivacyComp );
	      cTabPrivacyComp.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	      cTabPrivacyComp.setLayout(new FillLayout());
	      
	      
	      
	
			root = new Composite(cTabPrivacyComp, SWT.NULL);
		    final GridLayout groupInputGridLayout = new GridLayout();
		    groupInputGridLayout.numColumns = 4;
		    root.setLayout(groupInputGridLayout);
			this.build(this.root);
			cTabPrivacyComp.layout();
			cTabPrivacy.setControl( cTabPrivacyComp );
	}
	
	/**
	 * Builds this View
	 * @param parent The Parent Composite
	 */
	public void build(Composite parent){
		this.dDisclosurePrivacy(parent);
		this.tCloseness(parent);
		this.LDiversity(parent);
	}
	
	
	/**
	 * Builds the View for LDiversity Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void LDiversity(Composite parent){
		this.createLabel(Resources.getMessage("Model.1d"), parent, this.symbolL);
		btnLDiversity=this.createButton(parent, new SelectionAdapter() {
		    @Override
		    public void widgetSelected(final SelectionEvent arg0) {
		    	if(btnLDiversity.getSelection()){
		    		knobLDiversity.setEnabled(true);
		    		labelLDiversity.setEnabled(true);
		    		comboLDiversityVariant.setEnabled(true);
		    		if (comboLDiversityVariant.getSelectionIndex() == 2) {
		                labelLDiversityC.setEnabled(true);
		    			knobLDiversityC.setEnabled(true);
		            } else {
		                labelLDiversityC.setEnabled(false);
		                knobLDiversityC.setEnabled(false);
		            }
		    	}else{
		    		knobLDiversity.setEnabled(false);
		    		labelLDiversity.setEnabled(false);
		    		comboLDiversityVariant.setEnabled(false);
		    		knobLDiversityC.setEnabled(false);
		    		labelLDiversityC.setEnabled(false);
		    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
		    			knobLDiversity.setEnabled(true);
		        		labelLDiversity.setEnabled(true);
		        		comboLDiversityVariant.setEnabled(true);
		        		btnLDiversity.setSelection(true);
		        		if (comboLDiversityVariant.getSelectionIndex() == 2) {
		                    labelLDiversityC.setEnabled(true);
		        			knobLDiversityC.setEnabled(true);
		                } else {
		                    labelLDiversityC.setEnabled(false);
		                    knobLDiversityC.setEnabled(false);
		                }
		    		}
		    	}
		    	ARXFields field=meta.getField(fieldName);
				field.setlDiversityEnable(btnLDiversity.getSelection());
				meta.setField(field);
		    	meta.setChanged(true);
		    }
		});
      
		// Create input group
        final Composite group=this.createGroup(8, parent);

        // Create l slider
        final Label lLabel = new Label(group, SWT.NONE);
        lLabel.setText(Resources.getMessage("CriterionDefinitionView.27")); //$NON-NLS-1$

        labelLDiversity = createLabel(group);
        knobLDiversity = createKnobInteger(group, 2, 1000);
        knobLDiversity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelLDiversity.setText(knobLDiversity.getValue()+"");
            	ARXFields field=meta.getField(fieldName);
            	field.setlDiversity(knobLDiversity.getValue());	
            	meta.setField(field);
            	meta.setChanged(true);
            }
        });

        // Create criterion combo
        final Label cLabel = new Label(group, SWT.PUSH);
        cLabel.setText(Resources.getMessage("CriterionDefinitionView.33")); //$NON-NLS-1$

        comboLDiversityVariant = new Combo(group, SWT.READ_ONLY);
        GridData d31 = SWTUtil.createFillHorizontallyGridData();
        d31.verticalAlignment = SWT.CENTER;
        d31.horizontalSpan = 1;
        comboLDiversityVariant.setLayoutData(d31);
        comboLDiversityVariant.setItems(new String[]{Resources.getMessage("CriterionDefinitionView.6"), //$NON-NLS-1$ 
                Resources.getMessage("CriterionDefinitionView.7"), //$NON-NLS-1$ 
                Resources.getMessage("CriterionDefinitionView.8")});
        comboLDiversityVariant.select(0);

        // Create c slider
        final Label zLabel = new Label(group, SWT.NONE);
        zLabel.setText(Resources.getMessage("CriterionDefinitionView.34")); //$NON-NLS-1$

        labelLDiversityC = createLabel(group);
        knobLDiversityC = createKnobDouble(group, 0.00001d, 1000d);
        knobLDiversityC.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                labelLDiversityC.setText(knobLDiversityC.getValue()+"");
                ARXFields field=meta.getField(fieldName);
                field.setlDiversityC(knobLDiversityC.getValue());
                meta.setField(field);
                meta.setChanged(true);
            }
        });

        comboLDiversityVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	if (comboLDiversityVariant.getSelectionIndex() == 2) {
                    labelLDiversityC.setEnabled(true);
        			knobLDiversityC.setEnabled(true);
                } else {
                    labelLDiversityC.setEnabled(false);
                    knobLDiversityC.setEnabled(false);
                }
            	ARXFields field=meta.getField(fieldName);
            	field.setlDiversityVariant(comboLDiversityVariant.getSelectionIndex());
            	meta.setField(field);
                meta.setChanged(true);
            }
            
        });
	}
	
	/**
	 * Builds the View for dDisclosure Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void dDisclosurePrivacy(Composite parent){
			this.createLabel('\u03B4' + Resources.getMessage("Model.31"), parent, this.symbolD);
			btnDDisclosure = this.createButton(parent, new SelectionAdapter() {
			    @Override
			    public void widgetSelected(final SelectionEvent arg0) {
			    	if(btnDDisclosure.getSelection()){
			    		knobDDisclosure.setEnabled(true);
			    		labelDDisclosure.setEnabled(true);
			    	}else{
			    		knobDDisclosure.setEnabled(false);
			    		labelDDisclosure.setEnabled(false);
			    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
			    			knobDDisclosure.setEnabled(true);
			    			labelDDisclosure.setEnabled(true);
			    			btnDDisclosure.setSelection(true);
			    		}
			    	}
			    	ARXFields field=meta.getField(fieldName);
					field.setdDisclosureEnable(btnDDisclosure.getSelection());
					meta.setField(field);
			    	meta.setChanged(true);
			    }
			});
	    	// Create input group
	        final Composite group =this.createGroup(3, parent);
	        
	        // Create threshold slider
	        final Label zLabel = new Label(group, SWT.NONE);
	        zLabel.setText(Resources.getMessage("CriterionDefinitionView.100")); //$NON-NLS-1$

	        this.labelDDisclosure= new Text(group, SWT.BORDER | SWT.LEFT);
	        GridData data = SWTUtil.createFillHorizontallyGridData(false);
	        labelDDisclosure.setLayoutData(data);
	        labelDDisclosure.setEditable(false);
	        knobDDisclosure = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.000001d, 10d));
	        knobDDisclosure.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
	        knobDDisclosure.setDefaultColorProfile(defaultColorProfile);
	        knobDDisclosure.setFocusedColorProfile(focusedColorProfile);
	        knobDDisclosure.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	labelDDisclosure.setText(knobDDisclosure.getValue()+"");
	            	ARXFields field=meta.getField(fieldName);
	            	field.setdDisclosure(knobDDisclosure.getValue());
	            	meta.setField(field);
	            	meta.setChanged(true);
	            }
	        });
	}
	
	/**
	 * Builds the View for tCloseness Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void tCloseness(Composite parent){
		this.createLabel(Resources.getMessage("Model.1b"), parent, this.symbolT);
		this.btnTCloseness=this.createButton(parent, new SelectionAdapter() {
		    @Override
		    public void widgetSelected(final SelectionEvent arg0) {
		    	if(btnTCloseness.getSelection()){
		    		knobTCloseness.setEnabled(true);
		    		labelTCloseness.setEnabled(true);
		    		comboTCloseness.setEnabled(true);
		    	}else{
		    		knobTCloseness.setEnabled(false);
		    		labelTCloseness.setEnabled(false);
		    		comboTCloseness.setEnabled(false);
		    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
		    			knobTCloseness.setEnabled(true);
		    			labelTCloseness.setEnabled(true);
		    			btnTCloseness.setSelection(true);
			    		comboTCloseness.setEnabled(true);	
		    		}
		    	}
		    	ARXFields field=meta.getField(fieldName);
				field.settClosenessEnable(btnTCloseness.getSelection());
				meta.setField(field);
		    	meta.setChanged(true);
		    }
		});
	        final Composite group = this.createGroup(5, parent);
	       
	        
	        Label lblModel = new Label(group, SWT.NONE);
	        lblModel.setText(Resources.getMessage("CriterionDefinitionView.42")); //$NON-NLS-1$
	        
	        this.comboTCloseness = new Combo(group, SWT.READ_ONLY);
	        comboTCloseness.setItems(new String[]{Resources.getMessage("CriterionDefinitionView.9"), //$NON-NLS-1$
                    Resources.getMessage("CriterionDefinitionView.10")}); //$NON-NLS-1$
	        comboTCloseness.select(0);
	        comboTCloseness.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	ARXFields field=meta.getField(fieldName);
	    			field.settClosenessMeasure(comboTCloseness.getSelectionIndex());
	            	meta.setField(field);
	            	meta.setChanged(true);
	            }
	        });
	        // Create threshold slider
	        final Label zLabel = new Label(group, SWT.NONE);
	        zLabel.setText(Resources.getMessage("CriterionDefinitionView.43")); //$NON-NLS-1$

	        this.labelTCloseness= new Text(group, SWT.BORDER | SWT.LEFT);
	        GridData data = SWTUtil.createFillHorizontallyGridData(false);
	        labelTCloseness.setLayoutData(data);
	        labelTCloseness.setEditable(false);
	        this.knobTCloseness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.000001d, 10d));
	        knobTCloseness.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
	        knobTCloseness.setDefaultColorProfile(defaultColorProfile);
	        knobTCloseness.setFocusedColorProfile(focusedColorProfile);
	        knobTCloseness.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	labelTCloseness.setText(knobTCloseness.getValue()+"");
	            	ARXFields field=meta.getField(fieldName);
	            	field.settCloseness(knobTCloseness.getValue());
	            	meta.setField(field);
	            	meta.setChanged(true);
	            }
	        });
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		ARXFields field=this.meta.getField(this.fieldName);
		this.btnDDisclosure.setSelection(field.isdDisclosureEnable());
		this.knobDDisclosure.setValue(field.getdDisclosure());
		this.labelDDisclosure.setText(field.getdDisclosure()+"");
		this.btnTCloseness.setSelection(field.istClosenessEnable());
		this.comboTCloseness.select(field.gettClosenessMeasure());
		this.labelTCloseness.setText(field.gettCloseness()+"");
		this.knobTCloseness.setValue(field.gettCloseness());
		this.btnLDiversity.setSelection(field.islDiversityEnable());
		this.knobLDiversity.setValue(field.getlDiversity());
		this.labelLDiversity.setText(field.getlDiversity()+"");
		this.knobLDiversityC.setValue(field.getlDiversityC());
		this.labelLDiversityC.setText(field.getlDiversityC()+"");
		this.comboLDiversityVariant.select(field.getlDiversityVariant());
	}
	
	/**
	 * Enable or Disable the Privacy Criterion for the Sensitive Attributes
	 * @param enable To Enable or Disable
	 */
	public void enable(boolean enable){
		if(enable){
			btnDDisclosure.setEnabled(true);
			btnTCloseness.setEnabled(true);
    		this.btnLDiversity.setEnabled(true);
			updateEnable();
		}else{
			btnDDisclosure.setEnabled(false);
			knobDDisclosure.setEnabled(false);
    		labelDDisclosure.setEnabled(false);
    		knobTCloseness.setEnabled(false);
    		labelTCloseness.setEnabled(false);
    		comboTCloseness.setEnabled(false);
    		btnTCloseness.setEnabled(false);
    		knobLDiversity.setEnabled(false);
    		labelLDiversity.setEnabled(false);
    		comboLDiversityVariant.setEnabled(false);
    		knobLDiversityC.setEnabled(false);
    		labelLDiversityC.setEnabled(false);
    		this.btnLDiversity.setEnabled(false);
		}
	}
	
	/**
	 * Updates the View, if it is enabled
	 */
	private void updateEnable(){
		//For Overall Update
		if(btnDDisclosure.getSelection()){
    		knobDDisclosure.setEnabled(true);
    		labelDDisclosure.setEnabled(true);
    	}else{
    		knobDDisclosure.setEnabled(false);
    		labelDDisclosure.setEnabled(false);
    	}
		if(btnTCloseness.getSelection()){
    		knobTCloseness.setEnabled(true);
    		labelTCloseness.setEnabled(true);
    		comboTCloseness.setEnabled(true);
    	}else{
    		knobTCloseness.setEnabled(false);
    		labelTCloseness.setEnabled(false);
    		comboTCloseness.setEnabled(false);
    	}
		if(btnLDiversity.getSelection()){
    		knobLDiversity.setEnabled(true);
    		labelLDiversity.setEnabled(true);
    		comboLDiversityVariant.setEnabled(true);
    		if (comboLDiversityVariant.getSelectionIndex() == 2) {
                labelLDiversityC.setEnabled(true);
    			knobLDiversityC.setEnabled(true);
            } else {
                labelLDiversityC.setEnabled(false);
                knobLDiversityC.setEnabled(false);
            }
    	}else{
    		knobLDiversity.setEnabled(false);
    		labelLDiversity.setEnabled(false);
    		comboLDiversityVariant.setEnabled(false);
    		knobLDiversityC.setEnabled(false);
    		labelLDiversityC.setEnabled(false);
    	}
	}
	
	 /**
     * Creates a label
     * @return
     */
    protected Text createLabel(Composite parent) {

        final Text label = new Text(parent, SWT.BORDER | SWT.LEFT);
        GridData data = SWTUtil.createFillHorizontallyGridData(false);
        label.setLayoutData(data);
        label.setEditable(false);
        return label;
    }
    
    /**
     * Creates a double knob
     * @param parent
     * @param min
     * @param max
     * @return
     */
    protected Knob<Double> createKnobDouble(Composite parent, double min, double max) {
        Knob<Double> knob = new Knob<Double>(parent, SWT.NULL, new KnobRange.Double(min, max));
        knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        return knob;
    }
    
    /**
     * Creates a double knob
     * @param parent
     * @param min
     * @param max
     * @return
     */
    protected Knob<Integer> createKnobInteger(Composite parent, int min, int max) {
        Knob<Integer> knob = new Knob<Integer>(parent, SWT.NULL, new KnobRange.Integer(min, max));
        knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        return knob;
    }

    /**
     * 
     * @return The FieldName for Performance Reasons
     */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * @param fieldName The New FieldName for Updating
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		this.getData();
	}
	
	/**
	 * Creates the Label in the Beginning
	 * 
	 * @param message
	 *            The Message Text of the Label
	 * @param parent
	 *            The Parent Composite
	 */
	private void createLabel(final String message, final Composite parent, final Image image) {
		Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setImage(image);
		Label lbl1 = new Label(parent, SWT.NONE);
		lbl1.setText(message); // $NON-NLS-1$

	}

	/**
	 * Creates the Check Button for the Different Fields
	 * 
	 * @param parent
	 *            The Parent Composite
	 * @param listener
	 *            The SelectionListener for this Button
	 * @return
	 */
	private Button createButton(final Composite parent, final SelectionAdapter listener) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(Resources.getMessage("CriterionDefinitionView.56")); //$NON-NLS-1$
		button.setSelection(false);
		button.setEnabled(true);
		final GridData d82 = SWTUtil.createFillHorizontallyGridData();
		d82.horizontalSpan = 1;
		button.setLayoutData(d82);
		button.addSelectionListener(listener);
		return button;
	}
	
	/**
	 * Creates the Composite for the Inner Group
	 * @param numColum The Number of Columns in this Composite
	 * @param parent The Parent Composite for this Composite
	 * @return The Group Composite
	 */
	private Composite createGroup(int numColum,final Composite parent){
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		GridLayout groupInputGridLayout = new GridLayout();
		groupInputGridLayout.numColumns = numColum;
		group.setLayout(groupInputGridLayout);
		return group;
	}

}
