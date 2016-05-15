package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.ViewPopulationModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.core.Props;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogCriteria implements ARXPluginDialogInterface {
	
	final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	private ARXPluginDialogInterface[] composites;
	
	private CTabFolder wTabFolder;
	
	private CTabItem cTabPrivacy,cTabPopulation;

	public ARXDialogCriteria(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		composites=new ARXPluginDialogInterface[2];
		
		this.build();
	}
	
	private void build(){
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
        group.setLayout(new FillLayout());
        
		 wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      
	      
	      cTabPrivacy = new CTabItem( wTabFolder, SWT.NONE );
	      cTabPrivacy.setText( Messages.getString( "ARXPluginDialog.criteria.privacy.title" ) );
	      
	      Composite cTabPrivacyComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabPrivacyComp );
	      cTabPrivacyComp.setLayout(new FillLayout());
	      this.composites[0]=new ARXDialogCriteriaPrivacy(cTabPrivacyComp,meta,props,lsMod);
	      cTabPrivacyComp.layout();
	      cTabPrivacy.setControl( cTabPrivacyComp );
	      
	      this.cTabPopulation = new CTabItem( wTabFolder, SWT.NONE );
	      this.cTabPopulation.setText( Messages.getString( "ARXPluginDialog.criteria.population.title" ) );
	      
	      Composite cTabPopulationComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabPopulationComp );
	      cTabPopulationComp.setLayout(new FillLayout());
	      this.composites[1]=new ViewPopulationModel(cTabPopulationComp,meta);
	      cTabPopulationComp.layout();
	      cTabPopulation.setControl( cTabPopulationComp );

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
