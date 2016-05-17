package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.define.LayoutGeneral;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

public class LayoutTopRight {
	
	final private Composite parent;
	final private ARXResult result;
	private CTabFolder folder;
	private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private DataHandle result2;
	public ViewRisksReidentificationRisks reidentification;
	public ViewRisksQuasiIdentifierTable quasiIdentifier; 

	public LayoutTopRight(final Composite parent,ARXResult result,DataHandle result2, Data data, ARXConfiguration config,ARXPopulationModel population) {
		this.result=result;
		this.result2=result2;
		this.data=data;
		this.config=config;
		this.population=population;
		this.parent=parent;
		folder = new CTabFolder( this.parent,SWT.BORDER );
		folder.setSimple( false );
		this.build(folder);      
	      //END TABS
	     folder.setSelection(0);
		
	}
	
	public void build(CTabFolder parent){
		this.buildClassSizes(parent);
		this.buildClassSizesTable(parent);
		this.buildQuasiIdentifiers(parent);
		this.buildReidentificationRisks(parent);
	}
	
	private void buildReidentificationRisks(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.32") );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      this.reidentification=new ViewRisksReidentificationRisks(tabFieldComp,result,result2,data,config,population,true,null);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildQuasiIdentifiers(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.15") );
   		      
      ScrolledComposite scroller=new ScrolledComposite(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
     
      Composite tabFieldComp = new Composite( scroller, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      this.quasiIdentifier= new ViewRisksQuasiIdentifierTable(tabFieldComp,result,result2,data,config,population,true);
      
      tabFieldComp.layout();
      tabField.setControl( scroller );
      scroller.setContent(tabFieldComp);
      scroller.setExpandVertical(true);
      scroller.setExpandHorizontal(true);
      scroller.setMinSize(tabFieldComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void buildClassSizesTable(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.0") );   
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksDistributionTable(tabFieldComp,result,result2,data,config,population,true);
      
      tabFieldComp.layout();
      tabField.setControl( tabFieldComp );
	}
	
	private void buildClassSizes(CTabFolder parent){
		CTabItem tabField = new CTabItem( parent, SWT.NONE );
	 	tabField.setText( Resources.getMessage("RiskAnalysis.4") );
   		      
     
      Composite tabFieldComp = new Composite( parent, SWT.NONE );
     
      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
      
      new ViewRisksDistributionPlot(tabFieldComp,result,result2,data,config,population,true);
      
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
