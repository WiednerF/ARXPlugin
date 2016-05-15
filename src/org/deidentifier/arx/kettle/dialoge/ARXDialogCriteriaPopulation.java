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
package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.meta.RegionStore;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.ui.core.PropsUI;
//TODO

public class ARXDialogCriteriaPopulation implements ARXPluginDialogInterface {

final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	
	/** View */
    private final Composite  root;
    /** View */
    private Combo            combo;
    /** View */
    private Text             text;
    /** View */
    private Text             text2;
	
	public ARXDialogCriteriaPopulation(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		  // Create group
        root = new Composite(parent, SWT.NONE);
        root.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		
		this.build(this.root);
	}
	
	private void build(final Composite parent){
		 Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.population.region")); //$NON-NLS-1$
	        combo = new Combo(parent, SWT.SINGLE | SWT.READ_ONLY);
	        for (Region region : Region.values()) {
	            combo.add(region.getName());
	        }
	        combo.setEnabled(true);
	        combo.select(0);
	        combo.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData(true, 2));
	        combo.addSelectionListener(new SelectionAdapter(){
	            public void widgetSelected(SelectionEvent arg0) {
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
	            
	        });
	        
	        // Sampling fraction
	        Label lbl2 = new Label(parent, SWT.NONE);
	        lbl2.setText(Messages.getString("ARXPluginDialog.criteria.population.sampling")); //$NON-NLS-1$
	        text = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        text.setText("0"); //$NON-NLS-1$
	        text.setToolTipText("0"); //$NON-NLS-1$
	        text.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
	        text.setEditable(false);
	        
	        // Button for updating
	        Button btn1 = new Button(parent, SWT.PUSH);
	        btn1.setText("..."); //$NON-NLS-1$
	        btn1.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent arg0) {
	                String _value = showInputDialog(parent.getShell(), 
	                                                                Messages.getString("ARXPluginDialog.criteria.population.sampling.1"),  //$NON-NLS-1$
	                                                                Messages.getString("ARXPluginDialog.criteria.population.sampling.2"),  //$NON-NLS-1$
	                                                                text.getText(), 
	                                                                new IInputValidator(){
	                                                           
	                                                                    public String isValid(String arg0) {
	                                                                        double value = 0d;
	                                                                        try {
	                                                                            value = Double.valueOf(arg0);
	                                                                        } catch (Exception e) {
	                                                                            return Messages.getString("ARXPluginDialog.criteria.population.sampling.3"); //$NON-NLS-1$
	                                                                        }
	                                                                        if (value > 0d && value <= 1d) {
	                                                                            return null;
	                                                                        } else {
	                                                                            return Messages.getString("ARXPluginDialog.criteria.population.sampling.4"); //$NON-NLS-1$
	                                                                        }
	                                                                    }});
	                if (_value != null) {
	                    double value = Double.valueOf(_value);
	                    if(value==Double.parseDouble(text.getText())){
	                    	return;
	                    }
	                    text.setText(value+"");
	                    meta.setChanged(true);
	                   }
	            }
	        });
	        
	        Label lbl3 = new Label(parent, SWT.NONE);
	        lbl3.setText(Messages.getString("ARXPluginDialog.criteria.population.population")); //$NON-NLS-1$
	        
	        text2 = new Text(parent, SWT.BORDER | SWT.SINGLE);
	        text2.setText("0"); //$NON-NLS-1$
	        text2.setToolTipText("0"); //$NON-NLS-1$
	        text2.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
	        text2.setEditable(false);

	        // Button for updating
	        Button btn2 = new Button(parent, SWT.PUSH);
	        btn2.setText("..."); //$NON-NLS-1$
	        btn2.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent arg0) {
	                
	                String _value = showInputDialog(parent.getShell(), 
	                												Messages.getString("ARXPluginDialog.criteria.population.population.1"),  //$NON-NLS-1$
	                												Messages.getString("ARXPluginDialog.criteria.population.population.2"),  //$NON-NLS-1$
	                                                                text2.getText(), 
	                                                                new IInputValidator(){
	                                                                    
	                                                                    public String isValid(String arg0) {
	                                                                        int value = 0;
	                                                                        try {
	                                                                            value = Integer.valueOf(arg0);
	                                                                        } catch (Exception e) {
	                                                                            return Messages.getString("ARXPluginDialog.criteria.population.population.3"); //$NON-NLS-1$
	                                                                        }
	                                                                        if (value >= 3000) {
	                                                                            return null;
	                                                                        } else {
	                                                                            return Messages.getString("ARXPluginDialog.criteria.population.population.1"); //$NON-NLS-1$
	                                                                        }
	                                                                    }});
	                if (_value != null) {
	                    int value = Integer.valueOf(_value);
	                    text2.setText(value+"");
	                   
	                    meta.setChanged(true);
	                }
	            }
	        });
	}

	public void getData() {
		if(this.meta.getRegion()!=null){
			this.combo.select(this.combo.indexOf(this.meta.getRegion()));
			this.text.setText(this.meta.getRegions(this.meta.getRegion()).getSampling()+"");
			this.text2.setText(this.meta.getRegions(this.meta.getRegion()).getPopulation()+"");
		}
	}

	public void saveData() {
		this.meta.setRegion(this.combo.getItem(this.combo.getSelectionIndex()));
		this.meta.setRegion(this.meta.getRegion(), Double.parseDouble(this.text.getText()), Long.parseLong(this.text2.getText()));
	}
	
	 /**
     * Shows an input dialog.
     *
     * @param shell
     * @param header
     * @param text
     * @param initial
     * @param validator
     * @return
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
