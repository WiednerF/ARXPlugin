package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
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
	  	public String field;
	  	private TransMeta transMeta;
		public ARXDialogFieldPrivacy privacy;


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
	        this.composites[0]=new ARXDialogFieldTransformation(compositeCenterRight,meta,props,lsMod,this,transMeta);
	        this.privacy=new ARXDialogFieldPrivacy(compositeBottomRight,meta,props,lsMod,this,transMeta);
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
    		composites[i].getData();//TODO Use field in getData
    	}
      	meta.setChanged(true);
      }
  });
  
  return group;
	}

	public void getData(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.getData();
		}
	}
	
	public void saveData(){
		for(ARXPluginDialogInterface composite:this.composites){
			composite.saveData();
		}
	}
	
    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createGridData() {
        final GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = false;
        data.grabExcessVerticalSpace = false;
        return data;
    }
    
    /**
     * Creates a grid layout.
     *
     * @param columns
     * @return
     */
    public static GridLayout createGridLayout(int columns) {
        final GridLayout layout = new GridLayout();
        layout.numColumns = columns;
        layout.marginBottom = 0;
        layout.marginHeight = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        layout.marginWidth = 0;
        return layout;
    }

    /**
     * Creates a grid layout.
     *
     * @param columns
     * @param compact
     * @return
     */
    public static GridLayout createGridLayout(int columns, boolean compact) {
        if (compact) return createGridLayout(columns);
        final GridLayout layout = new GridLayout();
        layout.numColumns = columns;
        return layout;
    }
    
    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillGridData() {
        return createFillGridData(1);
    }

    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillGridData(int span) {
        final GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalIndent=0;
        data.verticalIndent=0;
        data.horizontalSpan = span;
        return data;
    }

    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillHorizontallyGridData() {
        return createFillHorizontallyGridData(true);
    }

    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillHorizontallyGridData(boolean fill) {
        return createFillHorizontallyGridData(fill, 1);
    }
    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillHorizontallyGridData(boolean fill, int span) {
        final GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = fill ? SWT.FILL : SWT.CENTER;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = false;
        data.horizontalSpan = span;
        data.horizontalIndent=0;
        data.verticalIndent=0;
        return data;
    }

    /**
     * Creates grid data.
     *
     * @return
     */
    public static GridData createFillVerticallyGridData() {
        final GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = false;
        data.grabExcessVerticalSpace = true;
        data.horizontalIndent=0;
        data.verticalIndent=0;
        return data;
    }


}
