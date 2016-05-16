package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.LayoutCriteria;
import org.deidentifier.arx.kettle.define.LayoutTransformationModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogGeneralTab implements ARXPluginDialogInterface {
	
	final CTabFolder parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	

	  private CTabItem tabGeneral;
	  
	  private ARXPluginDialogInterface[] composites;
	  private String[] fieldNames;

	public ARXDialogGeneralTab(final CTabFolder parent,ARXPluginMeta meta, final PropsUI props, String[] fieldNames) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.fieldNames=fieldNames;
		composites=new ARXPluginDialogInterface[2];

		 tabGeneral = new CTabItem( parent, SWT.NONE );
	      tabGeneral.setText( Messages.getString( "ARXPluginDialog.general.title" ) );
	   		      
	      ScrolledComposite scroller=new ScrolledComposite(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	     
	      Composite tabGeneralComp = new Composite( scroller, SWT.NONE );
	      props.setLook( tabGeneralComp );
	     
	      tabGeneralComp.setLayout( SWTUtil.createGridLayout(1));
	      
			Composite compositeRight;
			Composite compositeCenterRight;
			Composite compositeBottomRight;
	        // Create right composite
	        compositeRight = new Composite(tabGeneralComp, SWT.NONE);
	        compositeRight.setLayoutData(SWTUtil.createFillGridData());
	        compositeRight.setLayout( SWTUtil.createGridLayout(1)); 

	        // Create center-right composite
	        compositeCenterRight = new Composite(compositeRight, SWT.NONE);
	        compositeCenterRight.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        compositeCenterRight.setLayout(SWTUtil.createGridLayout(1));
	        
	        // Create bottom-right composite
	        compositeBottomRight = new Composite(compositeRight, SWT.NONE);
	        compositeBottomRight.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        compositeBottomRight.setLayout(SWTUtil.createGridLayout(1));
	        
	        
	        
	        this.composites[0]=new LayoutTransformationModel(compositeBottomRight,this.meta,this.props,this.fieldNames);
	        this.composites[1]=new LayoutCriteria(compositeCenterRight,this.meta,this.props,this.fieldNames);
	     
	      FormData tabGeneralFileComp = new FormData();
	      tabGeneralFileComp.left = new FormAttachment( 0, 0 );
	      tabGeneralFileComp.top = new FormAttachment( 0, 0 );
	      tabGeneralFileComp.right = new FormAttachment( 100, 0 );
	      tabGeneralFileComp.bottom = new FormAttachment( 100, 0 );
	      tabGeneralComp.setLayoutData( tabGeneralFileComp );
	      tabGeneralComp.layout();
	      tabGeneral.setControl( scroller );
	      scroller.setContent(tabGeneralComp);
	      scroller.setExpandVertical(true);
	      scroller.setExpandHorizontal(true);
	      scroller.setMinSize(tabGeneralComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		{
			
		}
	}
	
	public void getData(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}
	}
	
	public void saveData(){
		//TODO Delete
	}

}
