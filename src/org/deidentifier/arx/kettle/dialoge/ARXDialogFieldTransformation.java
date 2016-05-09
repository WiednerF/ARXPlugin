package org.deidentifier.arx.kettle.dialoge;

import java.util.ArrayList;
import java.util.List;

import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.MicroAggregationFunctionDescription;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.attribut.ARXFields;
import org.deidentifier.arx.kettle.dialoge.resources.ComponentMultiStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.widget.TextVar;

public class ARXDialogFieldTransformation implements ARXPluginDialogInterface {
	
	private static Class<?> PKG = ARXPluginMeta.class;
	
	final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	
	final ARXDialogFieldTab parentFieldTab;
	
	 /** Widget */
    private  Combo                                            cmbType;
    /** Widget */
    private  Combo                                            cmbMode;
    /** Widget */
    private  Combo                                            cmbFunction;
    /** Widget */
    private  Button                                           btnMissing;
    /** Widget. */
    private  Combo                                            cmbMin;
    /** Widget. */
    private  Combo                                            cmbMax;
    /** View */
    private final Composite                                        root;
    
    private ComponentMultiStack									stack;
    private Button btnField;
    private TextVar textHierarchie;
    private TransMeta transMeta;
    
    private CTabFolder wTabFolder;
	
	private CTabItem cTabTransformation;


	public ARXDialogFieldTransformation(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,ARXDialogFieldTab parentFieldTab,TransMeta transMeta) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.transMeta=transMeta;
		this.parentFieldTab=parentFieldTab;
		
			 wTabFolder = new CTabFolder( parent,SWT.BORDER );
		      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
		      wTabFolder.setSimple( false );
		      wTabFolder.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
		      
		      
		      cTabTransformation = new CTabItem( wTabFolder, SWT.NONE );
		      cTabTransformation.setText( Messages.getString( "ARXPluginDialog.field.transformation.title" ) );
		      wTabFolder.setSelection(0);
		      Composite cTabTransformationComp = new Composite( this.wTabFolder, SWT.NONE );
		      props.setLook(  cTabTransformationComp );
		      cTabTransformationComp.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
		      cTabTransformationComp.setLayout(new FillLayout());
		      
		      
		      
		
		root = new Composite(cTabTransformationComp, SWT.NULL);
        final GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 1;
        root.setLayout(groupInputGridLayout);
        this.transMeta=transMeta;
		this.build(this.root);
		cTabTransformationComp.layout();
		cTabTransformation.setControl( cTabTransformationComp );

	}
	
	
	private void build(final Composite parent){
		 // Group
        
        
        // Group
        final Composite innerGroup = new Composite(parent, SWT.NULL);
        innerGroup.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
        final GridLayout typeInputGridLayout = new GridLayout();
        typeInputGridLayout.numColumns = 4;
        innerGroup.setLayout(typeInputGridLayout);
        
        // Combo for attribute type
        final Label kLabel = new Label(innerGroup, SWT.PUSH);
        kLabel.setText(Messages.getString("ARXPluginDialog.field.transformation.type")); //$NON-NLS-1$
        cmbType = new Combo(innerGroup, SWT.READ_ONLY);
        cmbType.setLayoutData(ARXDialogFieldTab.createFillGridData());
        cmbType.setItems(new String[]{"Insensitive","Sensitive","Quasi-identifying","Identifying"});
        cmbType.select(0);
        cmbType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
                updateFromAttributeType();
            }
        });
        
        // Add combo for mode
        final Label fLabel2 = new Label(innerGroup, SWT.PUSH);
        fLabel2.setText(Messages.getString("ARXPluginDialog.field.transformation.mode")); //$NON-NLS-1$
        cmbMode = new Combo(innerGroup, SWT.READ_ONLY);
        cmbMode.setLayoutData(ARXDialogFieldTab.createFillGridData());
        cmbMode.setItems(new String[]{Messages.getString("ARXPluginDialog.field.transformation.mode.1"),
        		Messages.getString("ARXPluginDialog.field.transformation.mode.2")});
        cmbMode.select(0);
        cmbMode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
            	if(cmbMode.getSelectionIndex()==0){
            		stack.setLayer(0);
            	}else{
            		stack.setLayer(1);
            	}
            }
        });
        
        // Create multistack
        stack = new ComponentMultiStack(innerGroup);
        
        // First column
        Composite first = stack.create(ARXDialogFieldTab.createGridData());
        
        Composite compositeLabelMin = new Composite(first, SWT.NONE);
        
        GridLayout compositeLabelMinLayout = new GridLayout();
        compositeLabelMinLayout.numColumns = 1;
        compositeLabelMinLayout.marginLeft = 0;
        compositeLabelMinLayout.marginRight = 0;
        compositeLabelMinLayout.marginWidth = 0;
        compositeLabelMin.setLayout(compositeLabelMinLayout);
        Label labelMin = new Label(compositeLabelMin, SWT.PUSH);
        labelMin.setText(Messages.getString("ARXPluginDialog.field.transformation.min")); //$NON-NLS-1$
        Composite compositelabelFunction = new Composite(first, SWT.NONE);
        GridLayout compositelabelFunctionLayout = new GridLayout();
        compositelabelFunctionLayout.numColumns = 1;
        compositelabelFunctionLayout.marginLeft = 0;
        compositelabelFunctionLayout.marginRight = 0;
        compositelabelFunctionLayout.marginWidth = 0;
        compositelabelFunction.setLayout(compositelabelFunctionLayout);
        final Label labelFunction = new Label(compositelabelFunction, SWT.PUSH);
        labelFunction.setText(Messages.getString("ARXPluginDialog.field.transformation.function")); //$NON-NLS-1$
        
        // Second column
        Composite second = stack.create(ARXDialogFieldTab.createFillHorizontallyGridData());
        this.cmbMin = new Combo(second, SWT.READ_ONLY);
        this.cmbMin.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
        this.cmbMin.setItems(new String[]{"All","0","1","2","3","4","5"});
        this.cmbMin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });
        this.cmbFunction = new Combo(second, SWT.READ_ONLY);
        this.cmbFunction.setLayoutData(ARXDialogFieldTab.createFillGridData());
        List<String> functions = new ArrayList<String>();
        for (MicroAggregationFunctionDescription function : AttributeType.listMicroAggregationFunctions()) {
        	functions.add(function.getLabel());
        }
        this.cmbFunction.setItems(functions.toArray(new String[functions.size()]));
        this.cmbFunction.select(0);
        this.cmbFunction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });
        //TODO Ask at the Meeting: How can i get the correct functions
        
        // Third column
        Composite third = stack.create(ARXDialogFieldTab.createGridData());
        Composite compositelabelMax = new Composite(third, SWT.NONE);
        GridLayout compositelabelMaxLayout = new GridLayout();
        compositelabelMaxLayout.numColumns = 1;
        compositelabelMaxLayout.marginLeft = 0;
        compositelabelMaxLayout.marginRight = 0;
        compositelabelMaxLayout.marginWidth = 0;
        compositelabelMax.setLayout(compositelabelMaxLayout);
        Label labelMax = new Label(compositelabelMax, SWT.PUSH);
        labelMax.setText(Messages.getString("ARXPluginDialog.field.transformation.max")); //$NON-NLS-1$
        Composite compositelabelMissing = new Composite(third, SWT.NONE);
        GridLayout compositelabelMissingLayout = new GridLayout();
        compositelabelMissingLayout.numColumns = 1;
        compositelabelMissingLayout.marginLeft = 0;
        compositelabelMissingLayout.marginRight = 0;
        compositelabelMissingLayout.marginWidth = 0;
        compositelabelMissing.setLayout(compositelabelMissingLayout);
        Label labelMissing = new Label(compositelabelMissing, SWT.PUSH);
        labelMissing.setText(Messages.getString("ARXPluginDialog.field.transformation.missing")); //$NON-NLS-1$
        
        // Fourth column
        Composite fourth = stack.create(ARXDialogFieldTab.createFillHorizontallyGridData());
        this.cmbMax = new Combo(fourth, SWT.READ_ONLY);
        this.cmbMax.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
        this.cmbMax.setItems(new String[]{"0","1","2","3","4","5","All"});
        this.cmbMax.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });
        btnMissing = new Button(fourth, SWT.CHECK);
        GridData btnMissingData = ARXDialogFieldTab.createFillGridData();
        btnMissingData.horizontalSpan = 2;
        btnMissing.setLayoutData(btnMissingData);
        btnMissing.setText(Messages.getString("ARXPluginDialog.field.transformation.missing.1")); //$NON-NLS-1$
        btnMissing.setSelection(true);
        btnMissing.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });
        // Collect info about children in stack
        stack.pack();
        stack.setLayer(0);
        
        Label tempLabel = new Label( parent, SWT.LEFT );
	    tempLabel.setText(Messages.getString("ARXPluginDialog.fields.transformation.hierarchie") );
	    props.setLook( tempLabel );
	    tempLabel.setLayoutData(new GridData());
	 
	    

	    this.textHierarchie = new TextVar(transMeta, parent, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
	    props.setLook( textHierarchie );
	    this.textHierarchie.addModifyListener( lsMod );
	    GridData btnHierarchieData = ARXDialogFieldTab.createFillGridData();
	    btnHierarchieData.horizontalSpan = 2;
        textHierarchie.setLayoutData(btnHierarchieData);
	    
	    btnField= new Button( parent, SWT.PUSH | SWT.CENTER );
	    props.setLook( btnField );
	    this.btnField.setText( BaseMessages.getString( PKG, "System.Button.Browse" ) );
	   
	    this.btnField.setLayoutData( new GridData() );
	    this.btnField.addSelectionListener( new SelectionAdapter() {
	    	public void widgetSelected( SelectionEvent e ) {
	          FileDialog dialog = new FileDialog( parent.getShell(), SWT.SAVE );
	          dialog.setFilterExtensions( new String[] { "*.csv" } );
	          if ( textHierarchie.getText() != null ) {
	            dialog.setFileName( transMeta.environmentSubstitute( textHierarchie.getText() ) );
	          }
	          dialog.setFilterNames(new String[]{
	            BaseMessages.getString( PKG, "System.FileType.CSVFiles" )});
	          if ( dialog.open() != null ) {
	            
	              textHierarchie.setText( dialog.getFilterPath()
	                + System.getProperty( "file.separator" ) + dialog.getFileName() );
	          }
	          meta.setChanged(true);
	        }
	      }
	   
	    		);
        
	}

	public void getData() {
		ARXFields field=this.meta.getField(this.parentFieldTab.field);
		this.cmbType.select(this.cmbType.indexOf(field.getType()));
		this.cmbMode.select(field.getTransformation());
		stack.setLayer(field.getTransformation());
		this.cmbMin.select((field.getMinimumGen()!=-1?field.getMinimumGen():0));
		this.cmbFunction.select(this.cmbFunction.indexOf(field.getFunctionMicro()));
		this.btnMissing.setSelection(field.isMissingDataMicro());
		this.cmbMax.select((field.getMaximumGen()!=-1?field.getMaximumGen():this.cmbMax.getItemCount()-1));
		if(field.getHierarchie()!=null){
			this.textHierarchie.setText(field.getHierarchie());
		}else{
			this.textHierarchie.setText("");
		}
		this.updateFromAttributeType();
	}

	public void saveData() {
		ARXFields field=this.meta.getField(this.parentFieldTab.field);
		field.setType(this.cmbType.getItem(this.cmbType.getSelectionIndex()));
		field.setTransformation(this.cmbMode.getSelectionIndex());
		field.setFunctionMicro(this.cmbFunction.getItem(this.cmbFunction.getSelectionIndex()));
		field.setMinimumGen((this.cmbMin.getSelectionIndex()!=0?this.cmbMin.getSelectionIndex():-1));
		field.setMissingDataMicro(this.btnMissing.getSelection());
		field.setMaximumGen((this.cmbMax.getSelectionIndex()!=(this.cmbMax.getItemCount()-1)?this.cmbMax.getSelectionIndex():-1));
		field.setHierarchie(this.textHierarchie.getText());
		this.meta.setField(field);
	}
	
	public void updateFromAttributeType(){
		if(this.cmbType.getItem(this.cmbType.getSelectionIndex()).equals("Sensitive")){
			this.parentFieldTab.privacy.enable(true);
		}else{
			this.parentFieldTab.privacy.enable(false);
		}
		if(this.cmbType.getItem(this.cmbType.getSelectionIndex()).equals("Quasi-identifying")){
			this.textHierarchie.setEnabled(true);
			this.btnField.setEnabled(true);
		}else{
			this.textHierarchie.setEnabled(false);
			this.btnField.setEnabled(false);
		}

	}

}
