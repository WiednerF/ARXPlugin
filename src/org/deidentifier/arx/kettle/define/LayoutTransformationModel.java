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
import org.deidentifier.arx.metric.MetricDescription;
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
 * Contains the Tabs and View for the Transformation Model.
 * <h3>Tabs</h3>
 * <ul><li>Tranformation Settings</li><li>Utility Measure</li><li>Attribut Weight</li><li>Coding Model</li></ul>
 * <h3>Visibility</h3>
 * The Visibility of the Tabs is defined after the Support through the Metrics from the Utility Measure Tab.
 * @author Florian Wiedner
 * @since 1.7
 * @version 1.0
 *
 */
public class LayoutTransformationModel implements ARXPluginDialogInterface {
	/**
	 * The Meat Information for Saving and catching of the Data
	 */
	final ARXPluginMeta meta;
	
	/**
	 * UI Settings from the Parent (Kettle) Project
	 */
	final PropsUI props;
	
	/**
	 * The Tab Composites which are Used here and which we need for further action
	 */
	private ARXPluginDialogInterface[] composites;
	
	/**
	 * The Coding Model Tab
	 */
	public ViewCodingModel coding;
	/**
	 * The Attribut Weight Tab
	 */
	public ViewAttributeWeights attributeWeight;
	/**
	 * The Tab Folder Parent
	 */
	private CTabFolder wTabFolder;
	
	/**
	 * The Different Tab Items for the 4 Tabs
	 */
	private CTabItem cTabTransformationSettings,cTabUtility,cTabAttributeWeights,cTabCodingModel;
	/**
	 * The fieldName list for the Attribut Weight
	 */
	private String[] fieldNames;
	
	/**
	 * Generates a new view for the Transformation Settings. Needs a GridLayout from the Parent
	 * @param parent The Parent Composite
	 * @param meta The Meta Information
	 * @param props The PropsUI
	 * @param fieldNames The Fieldnames for the AttributWeight Settings
	 */
	public LayoutTransformationModel(final Composite parent,ARXPluginMeta meta, final PropsUI props,String[] fieldNames) {
		this.meta=meta;
		this.props=props;
		this.fieldNames=fieldNames;
		composites=new ARXPluginDialogInterface[4];
		this.build(parent);    
	}
	
	/**
	 * Building the View
	 * @param parent The Parent Composite for this View
	 */
	private void build(final Composite parent){
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(SWTUtil.createFillGridData());
        group.setLayout(new FillLayout());
        
		 wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      wTabFolder.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent arg0) {
				composites[wTabFolder.getSelectionIndex()].getData();
			} 
	      });
	      
	      
	      
	      cTabTransformationSettings = new CTabItem( wTabFolder, SWT.NONE );
	      cTabTransformationSettings.setText( Resources.getMessage("CriterionDefinitionView.61") );
	     
	      Composite cTabTransformationSettingsComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabTransformationSettingsComp );
	      cTabTransformationSettingsComp.setLayout(new FillLayout());
	      this.composites[0]=new ViewTransformationSettings(cTabTransformationSettingsComp, meta);
	      cTabTransformationSettingsComp.layout();
	      cTabTransformationSettings.setControl( cTabTransformationSettingsComp );
	      
	      this.cTabUtility = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabUtility.setText( Resources.getMessage("CriterionDefinitionView.66") );
	      
	      Composite cTabUtilityComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabUtilityComp );
	      cTabUtilityComp.setLayout(new FillLayout());
	      this.composites[1]=new ViewUtilityMeasures(this,cTabUtilityComp, meta);
	      cTabUtilityComp.layout();
	      cTabUtility.setControl( cTabUtilityComp );
      
	      wTabFolder.setSelection(0);
	      wTabFolder.setLayoutData( SWTUtil.createFillHorizontallyGridData() );
	      
	      
	}
	
	/**
	 * Changes the Metric for our two optional Tabs
	 * @param description The Metric Description from the ARX Project
	 */
	public void changeMetric(MetricDescription description){
		
		  if(description.isConfigurableCodingModelSupported()){
			  this.showSettingsCodingModel();
	        }else{
	        	this.hideSettingsCodingModel();
	        }
	        
	        if(description.isAttributeWeightsSupported()){
	        	this.showSettingsAttributeWeights();
	        }else{
	        	this.hideSettingsAttributeWeights();
	        }
	}
	
	 /**
     * Hides the settings for the attribute weights.
     */
    private void hideSettingsAttributeWeights(){

        if (this.cTabAttributeWeights != null) {
            this.cTabAttributeWeights.dispose();
            this.cTabAttributeWeights = null;
            this.attributeWeight=null;
        }
    }

    /**
     * Hides the settings for the coding model.
     */
    private void hideSettingsCodingModel(){
        if (this.cTabCodingModel != null) {
            this.cTabCodingModel.dispose();
            this.cTabCodingModel = null;
            this.cTabCodingModel=null;
        }
    }

    /**
     * Shows the settings for the attribute weights.
     */
    private void showSettingsAttributeWeights(){
        if (this.cTabAttributeWeights != null&&!this.attributeWeight.quasiIdentifierChanged()) return;
        if(this.cTabAttributeWeights !=null) this.hideSettingsAttributeWeights();
          this.cTabAttributeWeights = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabAttributeWeights.setText( Resources.getMessage("CriterionDefinitionView.63") );
	      
	      Composite cTabAttributeWeightsComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabAttributeWeightsComp );
	      cTabAttributeWeightsComp.setLayout(new FillLayout());
	      cTabAttributeWeightsComp.setEnabled(true);
	      this.attributeWeight=new ViewAttributeWeights(cTabAttributeWeightsComp,meta,fieldNames);
	      this.composites[2]=this.attributeWeight;
	      cTabAttributeWeightsComp.layout();
	      cTabAttributeWeights.setControl( cTabAttributeWeightsComp );
    }

    /**
     * Shows the settings for the coding model.
     */
    private void showSettingsCodingModel(){
        if (this.cTabCodingModel != null) return;
        this.cTabCodingModel = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabCodingModel.setText( Resources.getMessage("CriterionDefinitionView.65") );
	      
	      Composite cTabCodingModelComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabCodingModelComp );
	      cTabCodingModelComp.setLayout(new FillLayout());
	      this.coding=new ViewCodingModel(cTabCodingModelComp, meta);
	      this.composites[3]=this.coding;
	      cTabCodingModelComp.layout();
	      cTabCodingModel.setControl( cTabCodingModelComp );
    }

    /*
     * (non-Javadoc)
     * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
     */
	public void getData() {
		for(ARXPluginDialogInterface composite:this.composites){
			if(composite!=null) composite.getData();
		}
		if(this.attributeWeight!=null&&this.attributeWeight.quasiIdentifierChanged()){
			this.hideSettingsAttributeWeights();
			this.showSettingsAttributeWeights();
		}

	}

	public void saveData() {
			//TODO Delete
	}

}
