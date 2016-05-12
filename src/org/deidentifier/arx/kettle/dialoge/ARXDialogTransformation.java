package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.ViewTransformationSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
	
	public ARXDialogTransformationCoding coding;
	public ARXDialogTransformationAttributWeight attributeWeight;
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
	      this.composites[1]=new ARXDialogTransformationUtility(this,cTabUtilityComp, meta, props, lsMod);
	      cTabUtilityComp.layout();
	      cTabUtility.setControl( cTabUtilityComp );
	      
	      this.cTabAttributeWeights = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabAttributeWeights.setText( Messages.getString( "ARXPluginDialog.transformation.attribut.title" ) );
	      
	      Composite cTabAttributeWeightsComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabAttributeWeightsComp );
	      cTabAttributeWeightsComp.setLayout(new FillLayout());
	      cTabAttributeWeightsComp.setEnabled(true);
	      this.composites[2]=new ARXDialogTransformationAttributWeight(cTabAttributeWeightsComp,meta, props, lsMod,fieldNames);
	      this.attributeWeight=(ARXDialogTransformationAttributWeight) this.composites[2];
	      cTabAttributeWeightsComp.layout();
	      cTabAttributeWeights.setControl( cTabAttributeWeightsComp );
	      
	      this.cTabCodingModel = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabCodingModel.setText( Messages.getString( "ARXPluginDialog.transformation.coding.title" ) );
	      
	      Composite cTabCodingModelComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabCodingModelComp );
	      cTabCodingModelComp.setLayout(new FillLayout());
	      this.composites[3]=new ARXDialogTransformationCoding(cTabCodingModelComp, meta, props, lsMod);
	      this.coding=(ARXDialogTransformationCoding)this.composites[3];
	      cTabCodingModelComp.layout();
	      cTabCodingModel.setControl( cTabCodingModelComp );
	      
	      wTabFolder.setSelection(0);
	      wTabFolder.setLayoutData( ARXDialogGeneralTab.createFillHorizontallyGridData() );
	      
	      
	      
	}

	public void getData() {
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}

	}

	public void saveData() {
		for(ARXPluginDialogInterface composite:this.composites){
			composite.saveData();
		}
	}

}
