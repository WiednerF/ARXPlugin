/*
 * Plugin for Kettle with ARX: Powerful Data Anonymization
 * Copyright 2016 Florian Wiedner and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deidentifier.arx.kettle.define;

import java.util.ArrayList;
import java.util.List;

import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.MicroAggregationFunctionDescription;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.define.common.ComponentMultiStack;
import org.deidentifier.arx.kettle.meta.ARXFields;
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
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;
import org.pentaho.di.ui.core.widget.TextVar;

/**
 * Add the FieldWise Attribute Transformation Look
 * @author Florian Wiedner
 * @category LayoutField
 * @since 1.7
 * @version 1.0
 *
 */
public class ViewAttributeTransformation implements LayoutCompositeInterface {
	
	/**
	 * Messages
	 */
	private static Class<?> PKG = ARXPluginMeta.class;

	/**
	 * The Meta from the other Project
	 */
	final ARXPluginMeta meta;
	
	/**
	 * The PropsUI from the Main Project Kettle
	 */
	final PropsUI props;
	
	/**
	 * The ModifyListener from the MainPart
	 */
	final ModifyListener lsMod;	
	
	/**
	 * The Parent Tab and Folder
	 */
	final LayoutField parentFieldTab;
	
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
   
    /**
     * Multi Stack
     */
    private ComponentMultiStack									stack;
    /**View**/
    private Button btnField;
    /**View**/
    private TextVar textHierarchie;
    /**View**/
    private TransMeta transMeta;
	
    /**View**/
	private CTabItem cTabTransformation;
	/**
	 * The Actual Field Name Attribut
	 */
	private String fieldName;

	/**
	 * Creates the new View with a Default Attribut Value
	 * @param parent The Parent Composite
	 * @param meta The Meta Information
	 * @param props The PropsUI from the Kettle Project
	 * @param lsMod The Modification Listener
	 * @param parentFieldTab The Parent FieldTab
	 * @param transMeta The TransMeta from this Step
	 * @param field The Starting default FieldName
	 */
	public ViewAttributeTransformation(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,LayoutField parentFieldTab,TransMeta transMeta,String field) {
		this.fieldName=field;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.transMeta=transMeta;
		this.parentFieldTab=parentFieldTab;
		
			 CTabFolder wTabFolder = new CTabFolder( parent,SWT.BORDER );
		      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
		      wTabFolder.setSimple( false );
		      wTabFolder.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		      
		      
		      cTabTransformation = new CTabItem( wTabFolder, SWT.NONE );
		      cTabTransformation.setText( Resources.getMessage("LayoutAttributeMetadata.0") );
		      wTabFolder.setSelection(0);
		      Composite cTabTransformationComp = new Composite( wTabFolder, SWT.NONE );
		      props.setLook(  cTabTransformationComp );
		      cTabTransformationComp.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		      cTabTransformationComp.setLayout(new FillLayout());
		      
		      
		      
		
		Composite root = new Composite(cTabTransformationComp, SWT.NULL);
        final GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 1;
        root.setLayout(groupInputGridLayout);
		this.build(root);
		cTabTransformationComp.layout();
		cTabTransformation.setControl( cTabTransformationComp );
	}
	
	/**
	 * The Building of this Tab
	 * @param parent The Parent Composite
	 */
	private void build(final Composite parent){
        // Group
        final Composite innerGroup = new Composite(parent, SWT.NULL);
        innerGroup.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        final GridLayout typeInputGridLayout = new GridLayout();
        typeInputGridLayout.numColumns = 4;
        innerGroup.setLayout(typeInputGridLayout);
        
        // Combo for attribute type
        final Label kLabel = new Label(innerGroup, SWT.PUSH);
        kLabel.setText(Resources.getMessage("AttributeDefinitionView.7")); //$NON-NLS-1$
        cmbType = new Combo(innerGroup, SWT.READ_ONLY);
        cmbType.setLayoutData(SWTUtil.createFillGridData());
        cmbType.setItems(new String[]{Resources.getMessage("AttributeDefinitionView.0"), //$NON-NLS-1$
                Resources.getMessage("AttributeDefinitionView.1"), //$NON-NLS-1$
                Resources.getMessage("AttributeDefinitionView.2"), //$NON-NLS-1$
                Resources.getMessage("AttributeDefinitionView.3")});
        cmbType.select(0);
        cmbType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
                ARXFields field= meta.getField(fieldName);
                field.setType(cmbType.getItem(cmbType.getSelectionIndex()));
                meta.setField(field);
                updateFromAttributeType();
            }
        });
        
        // Add combo for mode
        final Label fLabel2 = new Label(innerGroup, SWT.PUSH);
        fLabel2.setText(Resources.getMessage("ViewMicoaggregation.4")); //$NON-NLS-1$
        cmbMode = new Combo(innerGroup, SWT.READ_ONLY);
        cmbMode.setLayoutData(SWTUtil.createFillGridData());
        cmbMode.setItems(new String[]{Resources.getMessage("ViewMicoaggregation.5"),
                Resources.getMessage("ViewMicoaggregation.6")});
        cmbMode.select(0);
        cmbMode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	meta.setChanged(true);
            	ARXFields field= meta.getField(fieldName);
        		field.setTransformation(cmbMode.getSelectionIndex());
                meta.setField(field);
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
        Composite first = stack.create(SWTUtil.createGridData());
        
        Composite compositeLabelMin = new Composite(first, SWT.NONE);
        
        GridLayout compositeLabelMinLayout = new GridLayout();
        compositeLabelMinLayout.numColumns = 1;
        compositeLabelMinLayout.marginLeft = 0;
        compositeLabelMinLayout.marginRight = 0;
        compositeLabelMinLayout.marginWidth = 0;
        compositeLabelMin.setLayout(compositeLabelMinLayout);
        Label labelMin = new Label(compositeLabelMin, SWT.PUSH);
        labelMin.setText(Resources.getMessage("HierarchyView.4")); //$NON-NLS-1$
        Composite compositelabelFunction = new Composite(first, SWT.NONE);
        GridLayout compositelabelFunctionLayout = new GridLayout();
        compositelabelFunctionLayout.numColumns = 1;
        compositelabelFunctionLayout.marginLeft = 0;
        compositelabelFunctionLayout.marginRight = 0;
        compositelabelFunctionLayout.marginWidth = 0;
        compositelabelFunction.setLayout(compositelabelFunctionLayout);
        final Label labelFunction = new Label(compositelabelFunction, SWT.PUSH);
        labelFunction.setText(Resources.getMessage("ViewMicoaggregation.0")); //$NON-NLS-1$
        
        // Second column
        Composite second = stack.create(SWTUtil.createFillHorizontallyGridData());
        this.cmbMin = new Combo(second, SWT.READ_ONLY);
        this.cmbMin.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        this.cmbMin.setItems(new String[]{"All","0","1","2","3","4","5"});
        this.cmbMin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	ARXFields field= meta.getField(fieldName);
        		field.setMinimumGen((cmbMin.getSelectionIndex()!=0?cmbMin.getSelectionIndex():-1));
                meta.setField(field);
                meta.setChanged(true);
            }
        });
        this.cmbFunction = new Combo(second, SWT.READ_ONLY);
        this.cmbFunction.setLayoutData(SWTUtil.createFillGridData());
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
                ARXFields field= meta.getField(fieldName);
            	field.setFunctionMicro(cmbFunction.getItem(cmbFunction.getSelectionIndex()));
                meta.setField(field);
            }
        });        
        // Third column
        Composite third = stack.create(SWTUtil.createGridData());
        Composite compositelabelMax = new Composite(third, SWT.NONE);
        GridLayout compositelabelMaxLayout = new GridLayout();
        compositelabelMaxLayout.numColumns = 1;
        compositelabelMaxLayout.marginLeft = 0;
        compositelabelMaxLayout.marginRight = 0;
        compositelabelMaxLayout.marginWidth = 0;
        compositelabelMax.setLayout(compositelabelMaxLayout);
        Label labelMax = new Label(compositelabelMax, SWT.PUSH);
        labelMax.setText(Resources.getMessage("HierarchyView.6")); //$NON-NLS-1$
        Composite compositelabelMissing = new Composite(third, SWT.NONE);
        GridLayout compositelabelMissingLayout = new GridLayout();
        compositelabelMissingLayout.numColumns = 1;
        compositelabelMissingLayout.marginLeft = 0;
        compositelabelMissingLayout.marginRight = 0;
        compositelabelMissingLayout.marginWidth = 0;
        compositelabelMissing.setLayout(compositelabelMissingLayout);
        Label labelMissing = new Label(compositelabelMissing, SWT.PUSH);
        labelMissing.setText(Resources.getMessage("ViewMicoaggregation.7")); //$NON-NLS-1$
        
        // Fourth column
        Composite fourth = stack.create(SWTUtil.createFillHorizontallyGridData());
        this.cmbMax = new Combo(fourth, SWT.READ_ONLY);
        this.cmbMax.setLayoutData(SWTUtil.createFillHorizontallyGridData());
        this.cmbMax.setItems(new String[]{"0","1","2","3","4","5","All"});
        this.cmbMax.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
                ARXFields field= meta.getField(fieldName);
        		field.setMaximumGen((cmbMax.getSelectionIndex()!=(cmbMax.getItemCount()-1)?cmbMax.getSelectionIndex():-1));
                meta.setField(field);
            }
        });
        btnMissing = new Button(fourth, SWT.CHECK);
        GridData btnMissingData = SWTUtil.createFillGridData();
        btnMissingData.horizontalSpan = 2;
        btnMissing.setLayoutData(btnMissingData);
        btnMissing.setText(Resources.getMessage("ViewMicoaggregation.2")); //$NON-NLS-1$
        btnMissing.setSelection(true);
        btnMissing.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
                ARXFields field= meta.getField(fieldName);
                field.setMissingDataMicro(btnMissing.getSelection());        		
                meta.setField(field);
            }
        });
        // Collect info about children in stack
        stack.pack();
        stack.setLayer(0);
        
        Label tempLabel = new Label( parent, SWT.LEFT );
	    tempLabel.setText(Resources.getMessage("MainMenu.15") );
	    props.setLook( tempLabel );
	    tempLabel.setLayoutData(new GridData());
	 
	    

	    this.textHierarchie = new TextVar(transMeta, parent, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
	    props.setLook( textHierarchie );
	    this.textHierarchie.addModifyListener( lsMod );
	    GridData btnHierarchieData = SWTUtil.createFillGridData();
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
	          ARXFields field= meta.getField(fieldName);
		  	  field.setHierarchie(textHierarchie.getText());
	          meta.setField(field);
	          meta.setChanged(true);
	        }
	      }
	   
	    		);
        
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		ARXFields field=this.meta.getField(this.fieldName);
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
	
	/*
	 * Updates all Information after Changing the FieldName in this Tab
	 */
	private void updateFromAttributeType(){
		if(this.cmbType.getItem(this.cmbType.getSelectionIndex()).equals("Sensitive")){
			this.parentFieldTab.setEnabled(true);
		}else{
			this.parentFieldTab.setEnabled(false);
		}
		if(this.cmbType.getItem(this.cmbType.getSelectionIndex()).equals("Quasi-identifying")){
			this.textHierarchie.setEnabled(true);
			this.btnField.setEnabled(true);
		}else{
			this.textHierarchie.setEnabled(false);
			this.btnField.setEnabled(false);
		}

	}
	
	   /**
     * 
     * @return The FieldName for Performance Reasons
     */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 
	 * @param fieldName The New FieldName for Updating
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		this.getData();
	}
	

}
