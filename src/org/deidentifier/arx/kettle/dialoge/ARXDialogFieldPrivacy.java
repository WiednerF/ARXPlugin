package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.deidentifier.arx.kettle.meta.ARXFields;
import org.eclipse.jface.layout.GridDataFactory;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Props;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

public class ARXDialogFieldPrivacy implements ARXPluginDialogInterface {

	
private static Class<?> PKG = ARXPluginMeta.class;
	
	final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	
	final ARXDialogFieldTab parentFieldTab;
	private TransMeta transMeta;
    
    private CTabFolder wTabFolder;
	
	private CTabItem cTabPrivacy;
	
	/** View */
    private final Composite                                        root;
    /** Color profile */
    private final KnobColorProfile defaultColorProfile;
    /** Color profile */
    private final KnobColorProfile focusedColorProfile;
    
    private Button btnDDisclosure;
    private Text labelDDisclosure;
    private Knob<Double> knobDDisclosure;
    
    private Button btnTCloseness;
    private Text labelTCloseness;
    private Knob<Double> knobTCloseness;
    private Combo comboTCloseness;
    
    private Button btnLDiversity;
    private Combo comboLDiversityVariant;
    private Knob<Integer> knobLDiversity;
    private Text labelLDiversity;
    private Knob<Double> knobLDiversityC;
    private Text labelLDiversityC;
	
	public ARXDialogFieldPrivacy(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,ARXDialogFieldTab parentFieldTab,TransMeta transMeta) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.transMeta=transMeta;
		this.parentFieldTab=parentFieldTab;
		
		this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());
		
		 wTabFolder = new CTabFolder( parent,SWT.BORDER );
	      props.setLook( wTabFolder, Props.WIDGET_STYLE_TAB );
	      wTabFolder.setSimple( false );
	      wTabFolder.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	      
	      
	      cTabPrivacy = new CTabItem( wTabFolder, SWT.NONE );
	      cTabPrivacy.setText( Messages.getString( "ARXPluginDialog.field.privacy.title" ) );
	      wTabFolder.setSelection(0);
	      Composite cTabPrivacyComp = new Composite( this.wTabFolder, SWT.NONE );
	      props.setLook(  cTabPrivacyComp );
	      cTabPrivacyComp.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
	      cTabPrivacyComp.setLayout(new FillLayout());
	      
	      
	      
	
			root = new Composite(cTabPrivacyComp, SWT.NULL);
		    final GridLayout groupInputGridLayout = new GridLayout();
		    groupInputGridLayout.numColumns = 3;
		    root.setLayout(groupInputGridLayout);
		    this.transMeta=transMeta;
			this.build(this.root);
			cTabPrivacyComp.layout();
			cTabPrivacy.setControl( cTabPrivacyComp );
	}
	
	public void build(Composite parent){
		this.dDisclosurePrivacy(parent);
		this.tCloseness(parent);
		this.LDiversity(parent);
	}
	
	
	protected Composite LDiversity(Composite parent){
		Label lbl1 = new Label(parent, SWT.NONE);
        lbl1.setText(Messages.getString("ARXPluginDialog.field.privacy.ldiversity")); //$NON-NLS-1$
        btnLDiversity = new Button(parent, SWT.CHECK);
        btnLDiversity.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
        btnLDiversity.setSelection(false);
        btnLDiversity.setEnabled(true);
        final GridData d82 = SWTUtil.createFillHorizontallyGridData();
        d82.horizontalSpan = 1;
        btnLDiversity.setLayoutData(d82);
        btnLDiversity.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(final SelectionEvent arg0) {
    	if(btnLDiversity.getSelection()){
    		knobLDiversity.setEnabled(true);
    		labelLDiversity.setEnabled(true);
    		comboLDiversityVariant.setEnabled(true);
    		if (comboLDiversityVariant.getSelectionIndex() == 2) {
                labelLDiversityC.setEnabled(true);
    			knobLDiversityC.setEnabled(true);
            } else {
                labelLDiversityC.setEnabled(false);
                knobLDiversityC.setEnabled(false);
            }
    	}else{
    		knobLDiversity.setEnabled(false);
    		labelLDiversity.setEnabled(false);
    		comboLDiversityVariant.setEnabled(false);
    		knobLDiversityC.setEnabled(false);
    		labelLDiversityC.setEnabled(false);
    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
    			knobLDiversity.setEnabled(true);
        		labelLDiversity.setEnabled(true);
        		comboLDiversityVariant.setEnabled(true);
        		btnLDiversity.setSelection(true);
        		if (comboLDiversityVariant.getSelectionIndex() == 2) {
                    labelLDiversityC.setEnabled(true);
        			knobLDiversityC.setEnabled(true);
                } else {
                    labelLDiversityC.setEnabled(false);
                    knobLDiversityC.setEnabled(false);
                }
    		}
    	}
    	meta.setChanged(true);
    }
});
		
		
		
		// Create input group
        final Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogFieldTab.createFillHorizontallyGridData());
        final GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 8;
        group.setLayout(groupInputGridLayout);

        // Create l slider
        final Label lLabel = new Label(group, SWT.NONE);
        lLabel.setText(Messages.getString("ARXPluginDialog.field.privacy.ldiversity.1")); //$NON-NLS-1$

        labelLDiversity = createLabel(group);
        knobLDiversity = createKnobInteger(group, 2, 1000);
        knobLDiversity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelLDiversity.setText(knobLDiversity.getValue()+"");
            	meta.setChanged(true);
            }
        });

        // Create criterion combo
        final Label cLabel = new Label(group, SWT.PUSH);
        cLabel.setText(Messages.getString("ARXPluginDialog.field.privacy.ldiversity.2")); //$NON-NLS-1$

        comboLDiversityVariant = new Combo(group, SWT.READ_ONLY);
        GridData d31 = ARXDialogFieldTab.createFillHorizontallyGridData();
        d31.verticalAlignment = SWT.CENTER;
        d31.horizontalSpan = 1;
        comboLDiversityVariant.setLayoutData(d31);
        comboLDiversityVariant.setItems(new String[]{"Distinct-L-diversity","Entropy-L-diversity","Recursive-(c,l)-diversity"});
        comboLDiversityVariant.select(0);

        // Create c slider
        final Label zLabel = new Label(group, SWT.NONE);
        zLabel.setText(Messages.getString("ARXPluginDialog.field.privacy.ldiversity.3")); //$NON-NLS-1$

        labelLDiversityC = createLabel(group);
        knobLDiversityC = createKnobDouble(group, 0.00001d, 1000d);
        knobLDiversityC.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                labelLDiversityC.setText(knobLDiversityC.getValue()+"");
                meta.setChanged(true);
            }
        });

        comboLDiversityVariant.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	if (comboLDiversityVariant.getSelectionIndex() == 2) {
                    labelLDiversityC.setEnabled(true);
        			knobLDiversityC.setEnabled(true);
                } else {
                    labelLDiversityC.setEnabled(false);
                    knobLDiversityC.setEnabled(false);
                }
                meta.setChanged(true);
            }
            
        });

        return group;
	}
	
	protected Composite dDisclosurePrivacy(Composite parent){
		  Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.field.privacy.ddisclosureprivacy")); //$NON-NLS-1$
	        btnDDisclosure = new Button(parent, SWT.CHECK);
	        btnDDisclosure.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
	        btnDDisclosure.setSelection(false);
	        btnDDisclosure.setEnabled(true);
	        final GridData d82 = SWTUtil.createFillHorizontallyGridData();
	        d82.horizontalSpan = 1;
	        btnDDisclosure.setLayoutData(d82);
	        btnDDisclosure.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
	    	if(btnDDisclosure.getSelection()){
	    		knobDDisclosure.setEnabled(true);
	    		labelDDisclosure.setEnabled(true);
	    	}else{
	    		knobDDisclosure.setEnabled(false);
	    		labelDDisclosure.setEnabled(false);
	    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
	    			knobDDisclosure.setEnabled(true);
	    			labelDDisclosure.setEnabled(true);
	    			btnDDisclosure.setSelection(true);
	    		}
	    	}
	    	meta.setChanged(true);
	    }
	});
	    	// Create input group
	        final Composite group = new Composite(parent, SWT.NONE);
	        group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        final GridLayout groupInputGridLayout = new GridLayout();
	        groupInputGridLayout.numColumns = 3;
	        group.setLayout(groupInputGridLayout);
	        
	        // Create threshold slider
	        final Label zLabel = new Label(group, SWT.NONE);
	        zLabel.setText(Messages.getString("ARXPluginDialog.field.privacy.ddisclosureprivacy.1")); //$NON-NLS-1$

	        this.labelDDisclosure= new Text(group, SWT.BORDER | SWT.LEFT);
	        GridData data = SWTUtil.createFillHorizontallyGridData(false);
	        labelDDisclosure.setLayoutData(data);
	        labelDDisclosure.setEditable(false);
	        knobDDisclosure = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.000001d, 10d));
	        knobDDisclosure.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
	        knobDDisclosure.setDefaultColorProfile(defaultColorProfile);
	        knobDDisclosure.setFocusedColorProfile(focusedColorProfile);
	        knobDDisclosure.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	labelDDisclosure.setText(knobDDisclosure.getValue()+"");
	            	meta.setChanged(true);
	            }
	        });
	        return group;
	}
	
	protected Composite tCloseness(Composite parent){
		  Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.field.privacy.tcloseness")); //$NON-NLS-1$
	        this.btnTCloseness = new Button(parent, SWT.CHECK);
	        btnTCloseness.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
	        btnTCloseness.setSelection(false);
	        btnTCloseness.setEnabled(true);
	        final GridData d82 = SWTUtil.createFillHorizontallyGridData();
	        d82.horizontalSpan = 1;
	        btnTCloseness.setLayoutData(d82);
	        btnTCloseness.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
	    	if(btnTCloseness.getSelection()){
	    		knobTCloseness.setEnabled(true);
	    		labelTCloseness.setEnabled(true);
	    		comboTCloseness.setEnabled(true);
	    	}else{
	    		knobTCloseness.setEnabled(false);
	    		labelTCloseness.setEnabled(false);
	    		comboTCloseness.setEnabled(false);
	    		if(!btnLDiversity.getSelection()&&!btnTCloseness.getSelection()&&!btnDDisclosure.getSelection()){
	    			knobTCloseness.setEnabled(true);
	    			labelTCloseness.setEnabled(true);
	    			btnTCloseness.setSelection(true);
		    		comboTCloseness.setEnabled(true);

	    		}
	    	}
	    	meta.setChanged(true);
	    }
	});
	    	// Create input group
	        final Composite group = new Composite(parent, SWT.NONE);
	        group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
	        final GridLayout groupInputGridLayout = new GridLayout();
	        groupInputGridLayout.numColumns = 5;
	        group.setLayout(groupInputGridLayout);
	       
	        
	        Label lblModel = new Label(group, SWT.NONE);
	        lblModel.setText(Messages.getString("ARXPluginDialog.field.privacy.tcloseness.1")); //$NON-NLS-1$
	        
	        this.comboTCloseness = new Combo(group, SWT.READ_ONLY);
	        comboTCloseness.setItems(new String[]{Messages.getString("ARXPluginDialog.field.privacy.tcloseness.1.1"), //$NON-NLS-1$
	      		  Messages.getString("ARXPluginDialog.field.privacy.tcloseness.1.2")}); //$NON-NLS-1$
	        comboTCloseness.select(0);
	        comboTCloseness.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	meta.setChanged(true);
	            }
	        });
	        // Create threshold slider
	        final Label zLabel = new Label(group, SWT.NONE);
	        zLabel.setText(Messages.getString("ARXPluginDialog.field.privacy.tcloseness.2")); //$NON-NLS-1$

	        this.labelTCloseness= new Text(group, SWT.BORDER | SWT.LEFT);
	        GridData data = SWTUtil.createFillHorizontallyGridData(false);
	        labelTCloseness.setLayoutData(data);
	        labelTCloseness.setEditable(false);
	        this.knobTCloseness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.000001d, 10d));
	        knobTCloseness.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
	        knobTCloseness.setDefaultColorProfile(defaultColorProfile);
	        knobTCloseness.setFocusedColorProfile(focusedColorProfile);
	        knobTCloseness.addSelectionListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(final SelectionEvent arg0) {
	            	labelTCloseness.setText(knobTCloseness.getValue()+"");
	            	meta.setChanged(true);
	            }
	        });
	        return group;
	}
	public void getData() {
		ARXFields field=this.meta.getField(this.parentFieldTab.field);
		this.btnDDisclosure.setSelection(field.isdDisclosureEnable());
		this.knobDDisclosure.setValue(field.getdDisclosure());
		this.labelDDisclosure.setText(field.getdDisclosure()+"");
		this.btnTCloseness.setSelection(field.istClosenessEnable());
		this.comboTCloseness.select(field.gettClosenessMeasure());
		this.labelTCloseness.setText(field.gettCloseness()+"");
		this.knobTCloseness.setValue(field.gettCloseness());
		this.btnLDiversity.setSelection(field.islDiversityEnable());
		this.knobLDiversity.setValue(field.getlDiversity());
		this.labelLDiversity.setText(field.getlDiversity()+"");
		this.knobLDiversityC.setValue(field.getlDiversityC());
		this.labelLDiversityC.setText(field.getlDiversityC()+"");
		this.comboLDiversityVariant.select(field.getlDiversityVariant());
		enable(field.getType().equals("Sensitive"));

	}

	public void saveData() {
		ARXFields field=this.meta.getField(this.parentFieldTab.field);
		field.setdDisclosureEnable(this.btnDDisclosure.getSelection());
		if(field.isdDisclosureEnable()){
			field.setdDisclosure(this.knobDDisclosure.getValue());
		}
		field.settClosenessEnable(this.btnTCloseness.getSelection());
		if(field.istClosenessEnable()){
			field.settCloseness(this.knobTCloseness.getValue());
			field.settClosenessMeasure(this.comboTCloseness.getSelectionIndex());
		}
		field.setlDiversityEnable(this.btnLDiversity.getSelection());
		if(field.islDiversityEnable()){
			field.setlDiversity(this.knobLDiversity.getValue());
			field.setlDiversityC(this.knobLDiversityC.getValue());
			field.setlDiversityVariant(this.comboLDiversityVariant.getSelectionIndex());
		}
		this.meta.setField(field);


	}
	
	public void enable(boolean enable){
		if(enable){
			btnDDisclosure.setEnabled(true);
			btnTCloseness.setEnabled(true);
    		this.btnLDiversity.setEnabled(true);
			updateEnable();
		}else{
			btnDDisclosure.setEnabled(false);
			knobDDisclosure.setEnabled(false);
    		labelDDisclosure.setEnabled(false);
    		knobTCloseness.setEnabled(false);
    		labelTCloseness.setEnabled(false);
    		comboTCloseness.setEnabled(false);
    		btnTCloseness.setEnabled(false);
    		knobLDiversity.setEnabled(false);
    		labelLDiversity.setEnabled(false);
    		comboLDiversityVariant.setEnabled(false);
    		knobLDiversityC.setEnabled(false);
    		labelLDiversityC.setEnabled(false);
    		this.btnLDiversity.setEnabled(false);
		}
	}
	
	public void updateEnable(){
		//For Overall Update
		if(btnDDisclosure.getSelection()){
    		knobDDisclosure.setEnabled(true);
    		labelDDisclosure.setEnabled(true);
    	}else{
    		knobDDisclosure.setEnabled(false);
    		labelDDisclosure.setEnabled(false);
    	}
		if(btnTCloseness.getSelection()){
    		knobTCloseness.setEnabled(true);
    		labelTCloseness.setEnabled(true);
    		comboTCloseness.setEnabled(true);
    	}else{
    		knobTCloseness.setEnabled(false);
    		labelTCloseness.setEnabled(false);
    		comboTCloseness.setEnabled(false);
    	}
		if(btnLDiversity.getSelection()){
    		knobLDiversity.setEnabled(true);
    		labelLDiversity.setEnabled(true);
    		comboLDiversityVariant.setEnabled(true);
    		if (comboLDiversityVariant.getSelectionIndex() == 2) {
                labelLDiversityC.setEnabled(true);
    			knobLDiversityC.setEnabled(true);
            } else {
                labelLDiversityC.setEnabled(false);
                knobLDiversityC.setEnabled(false);
            }
    	}else{
    		knobLDiversity.setEnabled(false);
    		labelLDiversity.setEnabled(false);
    		comboLDiversityVariant.setEnabled(false);
    		knobLDiversityC.setEnabled(false);
    		labelLDiversityC.setEnabled(false);
    	}
	}
	
	 /**
     * Creates a label
     * @return
     */
    protected Text createLabel(Composite parent) {

        final Text label = new Text(parent, SWT.BORDER | SWT.LEFT);
        GridData data = ARXDialogFieldTab.createFillHorizontallyGridData(false);
        label.setLayoutData(data);
        label.setEditable(false);
        return label;
    }
    
    /**
     * Creates a double knob
     * @param parent
     * @param min
     * @param max
     * @return
     */
    protected Knob<Double> createKnobDouble(Composite parent, double min, double max) {
        Knob<Double> knob = new Knob<Double>(parent, SWT.NULL, new KnobRange.Double(min, max));
        knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        return knob;
    }
    
    /**
     * Creates a double knob
     * @param parent
     * @param min
     * @param max
     * @return
     */
    protected Knob<Integer> createKnobInteger(Composite parent, int min, int max) {
        Knob<Integer> knob = new Knob<Integer>(parent, SWT.NULL, new KnobRange.Integer(min, max));
        knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        return knob;
    }
    
    //TODO Error NullPointer after Enabling of LDiveristy

}
