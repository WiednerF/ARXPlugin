package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.ViewUtilityMeasures;
import org.deidentifier.arx.kettle.define.ViewAttributeWeights;
import org.deidentifier.arx.kettle.define.ViewCodingModel;
import org.deidentifier.arx.kettle.define.ViewTransformationSettings;
import org.deidentifier.arx.metric.MetricDescription;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.Props;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogTransformation implements ARXPluginDialogInterface {
	
	final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	private ARXPluginDialogInterface[] composites;
	
	public ViewCodingModel coding;
	public ViewAttributeWeights attributeWeight;
	private CTabFolder wTabFolder;
	
	private CTabItem cTabGeneral,cTabUtility,cTabAttributeWeights,cTabCodingModel;
	private String[] fieldNames;
	
	public ARXDialogTransformation(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,String[] fieldNames) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.fieldNames=fieldNames;
		composites=new ARXPluginDialogInterface[4];
		
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogGeneralTab.createFillGridData());
        group.setLayout(new FillLayout());
        
		 wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      wTabFolder.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent arg0) {
				composites[wTabFolder.getSelectionIndex()].getData();
			} 
	      });
	      
	      
	      
	      cTabGeneral = new CTabItem( wTabFolder, SWT.NONE );
	      cTabGeneral.setText( Messages.getString( "ARXPluginDialog.transformation.general.title" ) );
	     
	      Composite cTabGeneralComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabGeneralComp );
	      cTabGeneralComp.setLayout(new FillLayout());
	      this.composites[0]=new ViewTransformationSettings(cTabGeneralComp, meta);
	      cTabGeneralComp.layout();
	      cTabGeneral.setControl( cTabGeneralComp );
	      
	      this.cTabUtility = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabUtility.setText( Messages.getString( "ARXPluginDialog.transformation.utility.title" ) );
	      
	      Composite cTabUtilityComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabUtilityComp );
	      cTabUtilityComp.setLayout(new FillLayout());
	      this.composites[1]=new ViewUtilityMeasures(this,cTabUtilityComp, meta);
	      cTabUtilityComp.layout();
	      cTabUtility.setControl( cTabUtilityComp );
      
	      wTabFolder.setSelection(0);
	      wTabFolder.setLayoutData( ARXDialogGeneralTab.createFillHorizontallyGridData() );
	      
	      
	      
	}
	
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
		this.parent.redraw();
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
	      this.cTabAttributeWeights.setText( Messages.getString( "ARXPluginDialog.transformation.attribut.title" ) );
	      
	      Composite cTabAttributeWeightsComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabAttributeWeightsComp );
	      cTabAttributeWeightsComp.setLayout(new FillLayout());
	      cTabAttributeWeightsComp.setEnabled(true);
	      this.composites[2]=new ViewAttributeWeights(cTabAttributeWeightsComp,meta,fieldNames);
	      this.attributeWeight=(ViewAttributeWeights) this.composites[2];
	      cTabAttributeWeightsComp.layout();
	      cTabAttributeWeights.setControl( cTabAttributeWeightsComp );
    }

    /**
     * Shows the settings for the coding model.
     */
    private void showSettingsCodingModel(){
        if (this.cTabCodingModel != null) return;
        this.cTabCodingModel = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabCodingModel.setText( Messages.getString( "ARXPluginDialog.transformation.coding.title" ) );
	      
	      Composite cTabCodingModelComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabCodingModelComp );
	      cTabCodingModelComp.setLayout(new FillLayout());
	      this.composites[3]=new ViewCodingModel(cTabCodingModelComp, meta);
	      this.coding=(ViewCodingModel)this.composites[3];
	      cTabCodingModelComp.layout();
	      cTabCodingModel.setControl( cTabCodingModelComp );
    }

	public void getData() {
		for(ARXPluginDialogInterface composite:this.composites){
			if(composite!=null) composite.getData();
		}
		if(this.attributeWeight.quasiIdentifierChanged()){
			this.hideSettingsAttributeWeights();
			this.showSettingsAttributeWeights();
		}

	}

	public void saveData() {
		for(ARXPluginDialogInterface composite:this.composites){
			if(composite!=null) composite.saveData();
		}
	}

}
