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
import org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.Props;
import org.pentaho.di.ui.core.PropsUI;

/**
 * Creates the Whole view For the Criteria like Population and Privacy
 * @author Florian Wiedner
 * @version 1.0
 * @since 1.7
 * @category ARXDialogGeneralTab
 *
 */
public class LayoutCriteria implements ARXPluginDialogInterface {
	/**
	 * The Meta for the Whole Project for Saving
	 */
	private final ARXPluginMeta meta;
	
	/**
	 * The PropsUI from the Kettle Project
	 */
	private final PropsUI props;
	
	/**
	 * The Composite Interfaces for Saving and getting of Data
	 */
	private ARXPluginDialogInterface[] composites;
	
	/**
	 * The Tab Folder 
	 */
	private CTabFolder wTabFolder;
	
	/**
	 * The TabItems for this Folder
	 */
	private CTabItem cTabPrivacy,cTabPopulation;
	/**
	 * The FieldNames of the Previous Step
	 */
	private String[] fieldNames;

	/**
	 * Creates the View and Sets up this Object
	 * @param parent The Parent Composite
	 * @param meta The Meta Data of the Project
	 * @param props The PropsUI from the Kettle Project
	 */
	public LayoutCriteria(final Composite parent,final ARXPluginMeta meta, final PropsUI props,String[] fieldNames) {
		this.meta=meta;
		this.fieldNames=fieldNames;
		this.props=props;
		composites=new ARXPluginDialogInterface[2];
		this.build(parent);
	}
	
	/**
	 * Builds this View
	 * @param parent The Parent Composite
	 */
	private void build(final Composite parent){
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        group.setLayout(new FillLayout());
        
		 wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      wTabFolder.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent arg0) {
					composites[wTabFolder.getSelectionIndex()].getData();
				} 
		      });
	      
	      
	      cTabPrivacy = new CTabItem( wTabFolder, SWT.NONE );
	      cTabPrivacy.setText( Resources.getMessage("CriterionSelectionDialog.4") );
	      
	      Composite cTabPrivacyComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabPrivacyComp );
	      cTabPrivacyComp.setLayout(new FillLayout());
	      this.composites[0]=new ViewCriteriaList(cTabPrivacyComp,meta,fieldNames);
	      cTabPrivacyComp.layout();
	      cTabPrivacy.setControl( cTabPrivacyComp );
	      
	      this.cTabPopulation = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabPopulation.setText( Resources.getMessage("CriterionSelectionDialog.5") );
	      
	      Composite cTabPopulationComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabPopulationComp );
	      cTabPopulationComp.setLayout(new FillLayout());
	      this.composites[1]=new ViewPopulationModel(cTabPopulationComp,meta);
	      cTabPopulationComp.layout();
	      cTabPopulation.setControl( cTabPopulationComp );

	      wTabFolder.setSelection(0);
	      wTabFolder.setLayoutData( SWTUtil.createFillHorizontallyGridData() );
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}
	}

	public void saveData() {
		//TODO Delete
	}

}
