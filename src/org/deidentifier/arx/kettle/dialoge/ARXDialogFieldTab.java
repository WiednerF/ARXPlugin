package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.ViewAttributeTransformation;
import org.deidentifier.arx.kettle.define.ViewCriteriaListField;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogFieldTab implements ARXPluginDialogInterface {
	
		final CTabFolder parent;
	
		final ARXPluginMeta meta;
	
		final PropsUI props;
	
		final ModifyListener lsMod;
		private CTabItem tabField;
	  
	  	private ARXPluginDialogInterface[] composites;
	  	
	  	private String[] fieldNames;
	  	private Combo comboField;
	  	private String field;
	  	private TransMeta transMeta;
		public ViewCriteriaListField privacy;


	public ARXDialogFieldTab(final CTabFolder parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,String[] fieldNames,TransMeta transMeta) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.fieldNames=fieldNames;
		this.field=fieldNames[0];
		this.transMeta=transMeta;
		composites=new ARXPluginDialogInterface[2];
		this.build();
		
	}
	
	private void build(){
		 /**
	       * TAB FieldWise ***************
	       */
		 	tabField = new CTabItem( parent, SWT.NONE );
		 	tabField.setText( Messages.getString( "ARXPluginDialog.field.title" ) );
	   		      
	      ScrolledComposite scroller=new ScrolledComposite(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	     
	      Composite tabFieldComp = new Composite( scroller, SWT.NONE );
	      props.setLook( tabFieldComp );
	     
	      tabFieldComp.setLayout( SWTUtil.createGridLayout(1));
	      
			Composite compositeRight;
			Composite compositeCenterRight;
			Composite compositeBottomRight;
			Composite compositeTopRight;
	        // Create right composite
	        compositeRight = new Composite(tabFieldComp, SWT.NONE);
	        compositeRight.setLayoutData(SWTUtil.createFillGridData());
	        compositeRight.setLayout( SWTUtil.createGridLayout(1)); 

	     // Create center-right composite
	        compositeTopRight = new Composite(compositeRight, SWT.NONE);
	        compositeTopRight.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        compositeTopRight.setLayout(SWTUtil.createGridLayout(1));
	        
	        // Create center-right composite
	        compositeCenterRight = new Composite(compositeRight, SWT.NONE);
	        compositeCenterRight.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        compositeCenterRight.setLayout(SWTUtil.createGridLayout(1));
	        
	        // Create bottom-right composite
	        compositeBottomRight = new Composite(compositeRight, SWT.NONE);
	        compositeBottomRight.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        compositeBottomRight.setLayout(SWTUtil.createGridLayout(1));
	        
	        this.fieldList(compositeTopRight);
	        this.composites[0]=new ViewAttributeTransformation(compositeCenterRight,meta,props,lsMod,this,transMeta,this.comboField.getItem(this.comboField.getSelectionIndex()));
	        this.privacy=new ViewCriteriaListField(compositeBottomRight,meta,props,this.comboField.getItem(this.comboField.getSelectionIndex()));
	        this.composites[1]=this.privacy;
	     
	      FormData tabGeneralFileComp = new FormData();
	      tabGeneralFileComp.left = new FormAttachment( 0, 0 );
	      tabGeneralFileComp.top = new FormAttachment( 0, 0 );
	      tabGeneralFileComp.right = new FormAttachment( 100, 0 );
	      tabGeneralFileComp.bottom = new FormAttachment( 100, 0 );
	      tabFieldComp.setLayoutData( tabGeneralFileComp );
	      tabFieldComp.layout();
	      tabField.setControl( scroller );
	      scroller.setContent(tabFieldComp);
	      scroller.setExpandVertical(true);
	      scroller.setExpandHorizontal(true);
	      scroller.setMinSize(tabFieldComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	
	
	protected Composite fieldList(Composite parent){
		// Create input group
  final Composite group = new Composite(parent, SWT.NONE);
  group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
  final GridLayout groupInputGridLayout = new GridLayout();
  groupInputGridLayout.numColumns = 2;
  group.setLayout(groupInputGridLayout);
		Label lbl1 = new Label(group, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.field.field.title")); //$NON-NLS-1$
	        
  comboField = new Combo(group, SWT.READ_ONLY);
  comboField.setItems(this.fieldNames); //$NON-NLS-1$
  comboField.select(0);
  comboField.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent arg0) {
    	saveData();
    	field=comboField.getItem(comboField.getSelectionIndex());
    	for(int i=0;i<composites.length;i++){
    		privacy.setFieldName(comboField.getItem(comboField.getSelectionIndex()));
    		composites[i].getData();
    	}
      	meta.setChanged(true);
      }
  });
  
  return group;
	}

	public void getData(){
		this.privacy.setFieldName(this.comboField.getItem(this.comboField.getSelectionIndex()));
		((ViewAttributeTransformation)this.composites[0]).setFieldName(this.comboField.getItem(this.comboField.getSelectionIndex()));
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}
	}
	
	public void saveData(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.saveData();
		}
	}

	public ViewCriteriaListField getPrivacy() {
		return privacy;
	}

	public void setPrivacy(ViewCriteriaListField privacy) {
		this.privacy = privacy;
	}
	
}
