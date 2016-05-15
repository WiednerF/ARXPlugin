package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.define.LayoutTransformationModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogGeneralTab implements ARXPluginDialogInterface {
	
	final CTabFolder parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;
	  private CTabItem tabGeneral;
	  
	  private ARXPluginDialogInterface[] composites;
	  private String[] fieldNames;

	public ARXDialogGeneralTab(final CTabFolder parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,String[] fieldNames) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.fieldNames=fieldNames;
		composites=new ARXPluginDialogInterface[2];

		 tabGeneral = new CTabItem( parent, SWT.NONE );
	      tabGeneral.setText( Messages.getString( "ARXPluginDialog.general.title" ) );
	   		      
	      ScrolledComposite scroller=new ScrolledComposite(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	     
	      Composite tabGeneralComp = new Composite( scroller, SWT.NONE );
	      props.setLook( tabGeneralComp );
	     
	      tabGeneralComp.setLayout( ARXDialogGeneralTab.createGridLayout(1));
	      
			Composite compositeRight;
			Composite compositeCenterRight;
			Composite compositeBottomRight;
	        // Create right composite
	        compositeRight = new Composite(tabGeneralComp, SWT.NONE);
	        compositeRight.setLayoutData(ARXDialogGeneralTab.createFillGridData());
	        compositeRight.setLayout( ARXDialogGeneralTab.createGridLayout(1)); 

	        // Create center-right composite
	        compositeCenterRight = new Composite(compositeRight, SWT.NONE);
	        compositeCenterRight.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
	        compositeCenterRight.setLayout(ARXDialogGeneralTab.createGridLayout(1));
	        
	        // Create bottom-right composite
	        compositeBottomRight = new Composite(compositeRight, SWT.NONE);
	        compositeBottomRight.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
	        compositeBottomRight.setLayout(ARXDialogGeneralTab.createGridLayout(1));
	        
	        
	        
	        this.composites[0]=new LayoutTransformationModel(compositeBottomRight,this.meta,this.props,this.fieldNames);
	        this.composites[1]=new ARXDialogCriteria(compositeCenterRight,this.meta,this.props,this.lsMod);
	     
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
