package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.ARXDialogGeneralTab;
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
		this.result2=result2;
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
		this.buildPopulation(parent);
		this.buildQuasiIdentifiers(parent);
	}
	
	private void buildQuasiIdentifiers(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.23")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
      
      new ViewRisksQuasiIdentifier(tabFieldComp,result,result2,data,config,population,false,this.parentLayout);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	
	private void buildPopulation(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.24")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
      
      new ViewRisksPopulationModel(tabFieldComp,result,result2,data,config,population,false);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildPopulationUniquessAll(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.12")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
      
      new ViewRisksPopulationUniqueness(tabFieldComp,result,result2,data,config,population,false,true);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildPopulationUniquess(CTabFolder parent){
	 	CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.13")   );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
      
      new ViewRisksPopulationUniqueness(tabFieldComp,result,result2,data,config,population,false,false);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildReidentification(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.5")  );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
      
      new ViewRisksReidentificationRisksTable(tabFieldComp,result,result2,data,config,population,false);
      
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
	
	public void update(){
		//TODO Update
	}

}
