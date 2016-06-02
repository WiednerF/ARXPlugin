package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.define.LayoutGeneral;
import org.deidentifier.arx.kettle.risk.ViewRisksPopulationUniqueness;
import org.deidentifier.arx.kettle.risk.ViewRisksQuasiIdentifier;
import org.deidentifier.arx.kettle.risk.ViewRisksReidentificationRisksTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

public class LayoutBottomLeft {
	
	final private Composite parent;
	final private ARXResult result;
	private CTabFolder folder;
	private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private DataHandle result2;
    private LayoutRisks parentLayout;

	public LayoutBottomLeft(final Composite parent,ARXResult result,DataHandle result2, Data data, ARXConfiguration config,ARXPopulationModel population,LayoutRisks parentLayout) {
		this.data=data;
		this.result2=data.getHandle();
		this.config=config;
		this.population=population;
		this.parent=parent;
		this.result=result;
		this.parentLayout=parentLayout;
		folder = new CTabFolder( this.parent,SWT.BORDER );
		folder.setSimple( false );
		this.build(folder);      
	      //END TABS
	     folder.setSelection(0);
		
	}
	
	public void build(CTabFolder parent){
		this.buildReidentification(parent);
		this.buildPopulationUniquess(parent);
		this.buildPopulationUniquessAll(parent);
		this.buildQuasiIdentifiers(parent);
	}
	
	private void buildQuasiIdentifiers(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.23")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksQuasiIdentifier(tabFieldComp,this.parentLayout);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildPopulationUniquessAll(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.12")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksPopulationUniqueness(tabFieldComp,result2,population,true);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildPopulationUniquess(CTabFolder parent){
	 	CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.13")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksPopulationUniqueness(tabFieldComp,result2,population,false);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildReidentification(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.5")  );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksReidentificationRisksTable(tabFieldComp,result2,data,population);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	public void setSelectionIndex(int index){
		this.folder.setSelection(index);
	}
	
	public void addSelectionListener(SelectionListener listener){
		this.folder.addSelectionListener(listener);
	}
	
	public int getSelectionIndex(){
		return this.folder.getSelectionIndex();
	}

}
