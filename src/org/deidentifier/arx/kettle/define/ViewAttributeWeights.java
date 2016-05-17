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

import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface;
import org.deidentifier.arx.kettle.meta.ARXFields;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

/**
 * This Class Generates the View for the Attribute Weights and the Transformation
 * @author Florian Wiedner
 * @since 1.7
 * @category LayoutTransformationModel
 * @version 1.0
 *
 */
public class ViewAttributeWeights implements ARXPluginDialogInterface {

    /** Constant */
    private static final int        MIN_KNOB   = 30;

    /**
     * The Parent Composite for adding the new Composite
     */
    final Composite parent;
	
    /**
     * The ARX Meta Storage for Saving and getting Saved Values from the Storage Definition
     */
	final ARXPluginMeta meta;
	/**
	 * The Composite for adding the Attribute Weights
	 */
	private Composite composite;
	/**
	 * All Fields which are in Quasi-Identifier Mode
	 */
	private String[] fields;
	 /** Color profile */
    private final KnobColorProfile defaultColorProfile;
    /** Color profile */
    private final KnobColorProfile focusedColorProfile;
    /**
     * All Field Names which are coming as Input
     */
    private String[] fieldNames;
    
	/**
	 * Generates the new View of the Attribut Weight Tab in the GUI
	 * @author Florian Wiedner
	 * @since 1.7
	 * @param parent The Parent Composite
	 * @param meta The Meta Information for this Step
	 * @param fieldNames The FieldName List of all Fields which are coming from the previous Step
	 */
	public ViewAttributeWeights(final Composite parent,ARXPluginMeta meta, String[] fieldNames) {
		this.parent=parent;
		this.meta=meta;
		this.fieldNames=fieldNames;
	
        // Color profiles
        this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());
        this.getData();
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		if(this.composite==null){
			this.fields=this.meta.getQuasiIdentifierFields(this.fieldNames);
			this.build(parent);
		}
	}

	public void saveData() {
		//TODO Delete

	}
	
	/**
	 * This Method builds the View and add all Attribut Weights
	 * @author Florian Wiedner
	 * @category ViewAttributeWeights
	 * @since 1.7
	 * @param parent The Parent Composite for adding
	 */
	private void build(Composite parent){
			if(this.composite==null&&this.fields!=null){
			 // Create layout
			this.composite = new Composite(parent, SWT.NONE);
			this.composite.setLayout(GridLayoutFactory.swtDefaults().numColumns(fields.length).margins(0, 0).equalWidth(true).create());
	        	 // Create composites
	            for(int i=0; i<this.fields.length; i++){
	                Composite c = new Composite(this.composite, SWT.NONE);
	                c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.CENTER).create());
	                c.setLayout(GridLayoutFactory.swtDefaults().numColumns(1).margins(2, 0).create());
	                
	                //Create Label
	                Label label = new Label(c, SWT.CENTER);
	                label.setText(this.fields[i]);
	                label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	                //Create Knob
	                final Knob<Double> knob = new Knob<Double>(c, SWT.NULL, new KnobRange.Double(0d, 1d));
	                knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(MIN_KNOB, MIN_KNOB).create());
	                knob.setDefaultColorProfile(defaultColorProfile);
	                knob.setFocusedColorProfile(focusedColorProfile);
	                
	                //Create Text
	                final Text labelObject = new Text(c, SWT.CENTER);
	                labelObject.setText("0.0"); //$NON-NLS-1$
	                labelObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	                labelObject.setEditable(false);
	                labelObject.setEnabled(true);
	                final ARXFields fieldObject=meta.getField(fields[i]);
	                knob.setValue(fieldObject.getAttributeWeight());
	                labelObject.setText(knob.getValue()+"");
	                knob.addSelectionListener(new SelectionAdapter(){
	                    public void widgetSelected(SelectionEvent arg0) {
	                        double value = knob.getValue();
	                        labelObject.setText(value+"");
	                        labelObject.setToolTipText(String.valueOf(value));
	                        fieldObject.setAttributeWeight(value);
	                        meta.setChanged(true);
	                    }
	                });
	            }	            
			}
	}
	
	 /**
     * Creates a label
     * @return
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
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
     * @param parent The parent Composite
     * @param min The Minimum Value
     * @param max The Maximum Value
     * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
     * @return A new Knob for Doubles
     */
    protected Knob<Double> createKnobDouble(Composite parent, double min, double max) {
        Knob<Double> knob = new Knob<Double>(parent, SWT.NULL, new KnobRange.Double(min, max));
        knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        return knob;
    }
    
    /**
     * 
     * @author Florian Wiedner
     * @since 1.1
	 * @category Parameters
     * @return Changing of Quasi-Identifier Occured in between this and the last check
     */
	public boolean quasiIdentifierChanged(){
		String[] temp =this.meta.getQuasiIdentifierFields(this.fieldNames);
		if(temp.length!=fields.length) return true;
		boolean changed=false;
		for(int i=0;i<temp.length;i++){
			if(!temp[i].equals(fields[i])){
				changed=true;
			}
		}
		return changed;
	}

}
