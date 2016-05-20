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

import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.meta.RegionStore;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Contains the view for changing the Standard Population Model
 * @author Florian Wiedner
 * @version 1.0
 * @since 1.7
 * @category ViewCriteria
 *
 */
public class ViewPopulationModel implements LayoutCompositeInterface {
	/**
	 * The meta Field for the whole Project
	 */
	final ARXPluginMeta meta;
	/** View */
    private final Composite  root;
    /** View */
    private Combo            combo;
    /** View */
    private Text             text;
    /** View */
    private Text             text2;
	
    /**
     * Creates the new View for the Population Model Definition
     * @param parent The Parent Containing Composite
     * @param meta The Meta Class
     */
	public ViewPopulationModel(final Composite parent,ARXPluginMeta meta) {
		this.meta=meta;
		  // Create group
        root = new Composite(parent, SWT.NONE);
        root.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		this.build(this.root);
	}
	
	/**
	 * Builds the new View of the Population Model
	 * @param parent The Parent Composite for adding
	 */
	private void build(final Composite parent){
		 Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Resources.getMessage("ViewPopulationModel.3")); //$NON-NLS-1$
	        combo = new Combo(parent, SWT.SINGLE | SWT.READ_ONLY);
	        for (Region region : Region.values()) {
	            combo.add(region.getName());
	        }
	        combo.setEnabled(true);
	        combo.select(0);
	        combo.setLayoutData(SWTUtil.createFillHorizontallyGridData(true, 2));
	        combo.addSelectionListener(new SelectionAdapter(){
	            public void widgetSelected(SelectionEvent arg0) {
	                update();
	                }
	            
	        });
	        
	        // Sampling fraction
	        Label lbl2 = new Label(parent, SWT.NONE);
	        lbl2.setText(Resources.getMessage("ViewPopulationModel.4")); //$NON-NLS-1$
	        text = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        text.setText("0"); //$NON-NLS-1$
	        text.setToolTipText("0"); //$NON-NLS-1$
	        text.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        text.setEditable(false);
	        
	        // Button for updating
	        Button btn1 = new Button(parent, SWT.PUSH);
	        btn1.setText(Resources.getMessage("ViewPopulationModel.0")); //$NON-NLS-1$
	        btn1.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent arg0) {
	                String _value = showInputDialog(parent.getShell(), 
	                		Resources.getMessage("ViewPopulationModel.1"),  //$NON-NLS-1$
	                		Resources.getMessage("ViewPopulationModel.2"),  //$NON-NLS-1$
	                                                                text.getText(), 
	                                                                new IInputValidator(){
	                                                           
	                                                                    public String isValid(String arg0) {
	                                                                        double value = 0d;
	                                                                        try {
	                                                                            value = Double.valueOf(arg0);
	                                                                        } catch (Exception e) {
	                                                                            return Resources.getMessage("ViewPopulationModel.5"); //$NON-NLS-1$
	                                                                        }
	                                                                        if (value > 0d && value <= 1d) {
	                                                                            return null;
	                                                                        } else {
	                                                                            return Resources.getMessage("ViewPopulationModel.7"); //$NON-NLS-1$
	                                                                        }
	                                                                    }});
	                if (_value != null) {
	                    double value = Double.valueOf(_value);
	                    if(value==Double.parseDouble(text.getText())){
	                    	return;
	                    }
	                    text.setText(value+"");
	                    update();
	                    meta.setChanged(true);
	                   }
	            }
	        });
	        
	        Label lbl3 = new Label(parent, SWT.NONE);
	        lbl3.setText(Resources.getMessage("ViewPopulationModel.6")); //$NON-NLS-1$
	        
	        text2 = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        text2.setText("0"); //$NON-NLS-1$
	        text2.setToolTipText("0"); //$NON-NLS-1$
	        text2.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        text2.setEditable(false);

	        // Button for updating
	        Button btn2 = new Button(parent, SWT.PUSH);
	        btn2.setText(Resources.getMessage("ViewPopulationModel.8")); //$NON-NLS-1$
	        btn2.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent arg0) {
	                
	                String _value = showInputDialog(parent.getShell(), 
	                		 Resources.getMessage("ViewPopulationModel.9"),  //$NON-NLS-1$
                             Resources.getMessage("ViewPopulationModel.10") + 36000,  //$NON-NLS-1$
                             text2.getText(), 
	                                                                new IInputValidator(){
	                                                                    
	                                                                    public String isValid(String arg0) {
	                                                                        int value = 0;
	                                                                        try {
	                                                                            value = Integer.valueOf(arg0);
	                                                                        } catch (Exception e) {
	                                                                        	return Resources.getMessage("ViewPopulationModel.11"); //$NON-NLS-1$
	                                                                        }
	                                                                        if (value >= 3000) {
	                                                                            return null;
	                                                                        } else {
	                                                                        	 return Resources.getMessage("ViewPopulationModel.12"); //$NON-NLS-1$
	                                                                        }
	                                                                    }});
	                if (_value != null) {
	                    int value = Integer.valueOf(_value);
	                    text2.setText(value+"");
	                    update();
	                    meta.setChanged(true);
	                }
	            }
	        });
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		if(this.meta.getRegion()!=null){
			this.combo.select(this.combo.indexOf(this.meta.getRegion()));
			this.text.setText(this.meta.getRegions(this.meta.getRegion()).getSampling()+"");
			this.text2.setText(this.meta.getRegions(this.meta.getRegion()).getPopulation()+"");
		}
	}
	/**
	 * Updates the ComboBox and Saves the Data to the Meta Class
	 */
	private void update(){
		 meta.setChanged();
         Region selected = null;
         String sselected = combo.getItem(combo.getSelectionIndex());
         for (Region region : Region.values()) {
             if (region.getName().equals(sselected)) {
                 selected = region;
                 break;
             }
         }
         if (selected != null) {
         	meta.setRegion(meta.getRegion(), Double.parseDouble(text.getText()), Long.parseLong(text2.getText()));
         	meta.setRegion(selected.getName());
         	RegionStore temp=meta.getRegions(selected.getName());
         	text.setText(temp.getSampling()+"");
         	text2.setText(temp.getPopulation()+"");
         }
     }
	
	 /**
     * Shows an input dialog.
     *
     * @param shell The Shell for opening
     * @param header The Header Title
     * @param text The Text
     * @param initial The Initial Value
     * @param validator The Validator for the Input
     * @return The String which is the output of the Dialog
     */
    public String showInputDialog(final Shell shell, final String header, final String text, final String initial, final IInputValidator validator) {
        final InputDialog dlg = new InputDialog(shell, header, text, initial, validator);
        if (dlg.open() == Window.OK) {
            return dlg.getValue();
        } else {
            return null;
        }
    }

}
