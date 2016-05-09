package org.deidentifier.arx.kettle.dialoge;

import java.util.ArrayList;
import java.util.List;

import org.deidentifier.arx.DataGeneralizationScheme.GeneralizationDegree;
import org.deidentifier.arx.criteria.KMap;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.Messages;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.ui.core.PropsUI;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

public class ARXDialogCriteriaPrivacy implements ARXPluginDialogInterface {

final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	
	
	/** View */
    private final Composite  root;
    
    private Button buttonKAnonymity;
    private Button buttonDifferential;
    private Button buttonKMap;
    private Button buttonReidentification;
    private Button buttonSampleUniqueness;
    private Button buttonPopulationUniqueness;
    
    private Text labelKAnonymity,labelDifferentialDelta,labelDifferentialEpsilon;
    
    private Knob<Integer> knobK;
    private Knob<Double> knobDifferentialEpsilon,knobDifferentialDelta;
    
    /** View */
    private Combo                 comboDifferentialGeneralization;
    
    /** View */
    private Text          labelKMap;
                          
    /** View */
    private Knob<Integer> knobKMap;
                          
    /** View */
    private Combo         cmbModelKMap;
                          
    /** View */
    private Knob<Double>  knobSignificanceLevelKMap;
                          
    /** View */
    private Text          labelSignificanceLevelKMap;
    
    
    private Text			labelReidentification;
    private Knob<Double> 	knobReidentification;
    
    private Text			labelSampleUniqueness;
    private Knob<Double> 	knobSampleUniqueness;
    private Text			labelPopulationUniqueness;
    private Knob<Double> 	knobPopulationUniqueness;
    private Combo 			comboPopulationUniqueness;

    
    /** Color profile */
    private final KnobColorProfile defaultColorProfile;
    /** Color profile */
    private final KnobColorProfile focusedColorProfile;
		
	public ARXDialogCriteriaPrivacy(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		
		this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());
		
		  // Create group
        root = new Composite(parent, SWT.NONE);
        root.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());
		
		this.build(this.root);
	}
	
	private void build(final Composite parent){
		this.kanonymity(parent);
       this.differential(parent);
       this.KMap(parent);
        this.Reidentification(parent);
        this.SampleUniqueness(parent);
        this.PopulationUniqueness(parent);
	}
	
	protected Composite PopulationUniqueness(Composite parent){
		  Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness")); //$NON-NLS-1$
	        buttonPopulationUniqueness = new Button(parent, SWT.CHECK);
	        buttonPopulationUniqueness.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
	        buttonPopulationUniqueness.setSelection(false);
	        buttonPopulationUniqueness.setEnabled(true);
	        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
	        d82.horizontalSpan = 1;
	        buttonPopulationUniqueness.setLayoutData(d82);
	        buttonPopulationUniqueness.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
	    	if(buttonPopulationUniqueness.getSelection()){
	    		knobPopulationUniqueness.setEnabled(true);
	    		labelPopulationUniqueness.setEnabled(true);
	    		comboPopulationUniqueness.setEnabled(true);
	    	}else{
	    		knobPopulationUniqueness.setEnabled(false);
	    		labelPopulationUniqueness.setEnabled(false);
	    		comboPopulationUniqueness.setEnabled(false);
	    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
	    			knobPopulationUniqueness.setEnabled(true);
	        		labelPopulationUniqueness.setEnabled(true);
		    		comboPopulationUniqueness.setEnabled(true);
		    		buttonPopulationUniqueness.setSelection(true);
	    		}
	    	}
	    	meta.setChanged(true);
	    }
	});
		
		
		
		// Create input group
    final Composite group = new Composite(parent, SWT.NONE);
    group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
    final GridLayout groupInputGridLayout = new GridLayout();
    groupInputGridLayout.numColumns = 5;
    group.setLayout(groupInputGridLayout);
    
    // Create threshold slider
    final Label zLabel = new Label(group, SWT.NONE);
    zLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.1")); //$NON-NLS-1$

    this.labelPopulationUniqueness= new Text(group, SWT.BORDER | SWT.LEFT);
    GridData data = ARXDialogGeneralTab.createFillGridData();
    labelPopulationUniqueness.setLayoutData(data);
    labelPopulationUniqueness.setEditable(false);
    knobPopulationUniqueness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
    knobPopulationUniqueness.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
    knobPopulationUniqueness.setDefaultColorProfile(defaultColorProfile);
    knobPopulationUniqueness.setFocusedColorProfile(focusedColorProfile);
    knobPopulationUniqueness.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent arg0) {
      	  labelPopulationUniqueness.setText(knobPopulationUniqueness.getValue()+"");
        	meta.setChanged(true);
        }
    });
    
    Label lblModel = new Label(group, SWT.NONE);
    lblModel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.2")); //$NON-NLS-1$
    
    comboPopulationUniqueness = new Combo(group, SWT.READ_ONLY);
    comboPopulationUniqueness.setItems(new String[]{Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.3.1"), //$NON-NLS-1$
  		  Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.3.2"), //$NON-NLS-1$
  		  Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.3.3"), //$NON-NLS-1$
  		  Messages.getString("ARXPluginDialog.criteria.privacy.populationuniqueness.3.4")}); //$NON-NLS-1$
    comboPopulationUniqueness.select(0);
    comboPopulationUniqueness.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent arg0) {
        	meta.setChanged(true);
        }
    });
    
    return group;
	}
	
	
	protected Composite SampleUniqueness(Composite parent){
		  Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.sampleuniqueness")); //$NON-NLS-1$
	        buttonSampleUniqueness = new Button(parent, SWT.CHECK);
	        buttonSampleUniqueness.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
	        buttonSampleUniqueness.setSelection(false);
	        buttonSampleUniqueness.setEnabled(true);
	        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
	        d82.horizontalSpan = 1;
	        buttonSampleUniqueness.setLayoutData(d82);
	        buttonSampleUniqueness.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
	    	if(buttonSampleUniqueness.getSelection()){
	    		knobSampleUniqueness.setEnabled(true);
	    		labelSampleUniqueness.setEnabled(true);
	    	}else{
	    		knobSampleUniqueness.setEnabled(false);
	    		labelSampleUniqueness.setEnabled(false);
	    	  
	    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
	    			knobSampleUniqueness.setEnabled(true);
	        		labelSampleUniqueness.setEnabled(true);
	        		buttonSampleUniqueness.setSelection(true);
	    		}
	    	}
	    	meta.setChanged(true);
	    }
	});
		
		
		
		// Create input group
      final Composite group = new Composite(parent, SWT.NONE);
      group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
      final GridLayout groupInputGridLayout = new GridLayout();
      groupInputGridLayout.numColumns = 3;
      group.setLayout(groupInputGridLayout);
      
      // Create threshold slider
      final Label zLabel = new Label(group, SWT.NONE);
      zLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.sampleuniqueness.1")); //$NON-NLS-1$

      this.labelSampleUniqueness= new Text(group, SWT.BORDER | SWT.LEFT);
      GridData data = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
      labelSampleUniqueness.setLayoutData(data);
      labelSampleUniqueness.setEditable(false);
      knobSampleUniqueness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
      knobSampleUniqueness.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
      knobSampleUniqueness.setDefaultColorProfile(defaultColorProfile);
      knobSampleUniqueness.setFocusedColorProfile(focusedColorProfile);
      knobSampleUniqueness.addSelectionListener(new SelectionAdapter() {
          @Override
          public void widgetSelected(final SelectionEvent arg0) {
        	  labelSampleUniqueness.setText(knobSampleUniqueness.getValue()+"");
          	meta.setChanged(true);
          }
      });
      
      return group;
	}
	
	protected Composite Reidentification(Composite parent){
		  Label lbl1 = new Label(parent, SWT.NONE);
	        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.reidentification")); //$NON-NLS-1$
	        buttonReidentification = new Button(parent, SWT.CHECK);
	        buttonReidentification.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
	        buttonReidentification.setSelection(false);
	        buttonReidentification.setEnabled(true);
	        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
	        d82.horizontalSpan = 1;
	        buttonReidentification.setLayoutData(d82);
	        buttonReidentification.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(final SelectionEvent arg0) {
	    	if(buttonReidentification.getSelection()){
	    		knobReidentification.setEnabled(true);
	    		labelReidentification.setEnabled(true);
	    	}else{
	    		knobReidentification.setEnabled(false);
	    		labelReidentification.setEnabled(false);
	    	  
	    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
	    			knobReidentification.setEnabled(true);
	        		labelReidentification.setEnabled(true);
	        		buttonReidentification.setSelection(true);
	    		}
	    	}
	    	meta.setChanged(true);
	    }
	});
		
		
		
		// Create input group
        final Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
        final GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 3;
        group.setLayout(groupInputGridLayout);
        
        // Create threshold slider
        final Label zLabel = new Label(group, SWT.NONE);
        zLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.reidentification.1")); //$NON-NLS-1$

        this.labelReidentification= new Text(group, SWT.BORDER | SWT.LEFT);
        GridData data = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelReidentification.setLayoutData(data);
        labelReidentification.setEditable(false);
        knobReidentification = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
        knobReidentification.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobReidentification.setDefaultColorProfile(defaultColorProfile);
        knobReidentification.setFocusedColorProfile(focusedColorProfile);
        knobReidentification.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelReidentification.setText(knobReidentification.getValue()+"");
            	meta.setChanged(true);
            }
        });
        return group;
	}
	
	protected Composite KMap(Composite parent){
		Label lbl1 = new Label(parent, SWT.NONE);
        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kmap")); //$NON-NLS-1$
        buttonKMap = new Button(parent, SWT.CHECK);
        buttonKMap.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
        buttonKMap.setSelection(false);
        buttonKMap.setEnabled(true);
        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d82.horizontalSpan = 1;
        buttonKMap.setLayoutData(d82);
        buttonKMap.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(final SelectionEvent arg0) {
    	if(buttonKMap.getSelection()){
    		knobKMap.setEnabled(true);
    		labelKMap.setEnabled(true);
    	    cmbModelKMap.setEnabled(true);
    	    if(cmbModelKMap.getSelectionIndex()!=0){
    	    	knobSignificanceLevelKMap.setEnabled(true);
    	    	labelSignificanceLevelKMap.setEnabled(true);
    	    }else{
    	    	knobSignificanceLevelKMap.setEnabled(false);
    	    	labelSignificanceLevelKMap.setEnabled(false);
    	    }
    	}else{
    		knobKMap.setEnabled(false);
    		labelKMap.setEnabled(false);
    	    cmbModelKMap.setEnabled(false);
    	    knobSignificanceLevelKMap.setEnabled(false);
    	    labelSignificanceLevelKMap.setEnabled(false);
    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
    			knobKMap.setEnabled(true);
        		labelKMap.setEnabled(true);
        	    cmbModelKMap.setEnabled(true);
        	    if(cmbModelKMap.getSelectionIndex()!=0){
        	    	knobSignificanceLevelKMap.setEnabled(true);
        	    	labelSignificanceLevelKMap.setEnabled(true);
        	    }else{
        	    	knobSignificanceLevelKMap.setEnabled(false);
        	    	labelSignificanceLevelKMap.setEnabled(false);
        	    }
        	    buttonPopulationUniqueness.setSelection(true);
    		}
    	}
    	meta.setChanged(true);
    }
});
		
		
		// Create input group
        Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
        GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 10;
        group.setLayout(groupInputGridLayout);
        
        
        // Create k slider
        Label kLabel = new Label(group, SWT.NONE);
        kLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kmap.1")); //$NON-NLS-1$
        
        
        this.labelKMap= new Text(group, SWT.BORDER | SWT.LEFT);
        GridData data = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelKMap.setLayoutData(data);
        labelKMap.setEditable(false);
        knobKMap = new Knob<Integer>(group, SWT.NULL, new KnobRange.Integer(2, 1000));
        knobKMap.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobKMap.setDefaultColorProfile(defaultColorProfile);
        knobKMap.setFocusedColorProfile(focusedColorProfile);
        knobKMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelKMap.setText(knobKMap.getValue()+"");
            	meta.setChanged(true);
            }
        });
        
        Label lblModel = new Label(group, SWT.NONE);
        lblModel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kmap.2")); //$NON-NLS-1$
        
        cmbModelKMap = new Combo(group, SWT.READ_ONLY);
        cmbModelKMap.setItems(new String[] { //$NON-NLS-1$
        		Messages.getString("ARXPluginDialog.criteria.privacy.kmap.2.2"), //$NON-NLS-1$
        		Messages.getString("ARXPluginDialog.criteria.privacy.kmap.2.3") }); //$NON-NLS-1$
        cmbModelKMap.select(0);
        cmbModelKMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                switch (cmbModelKMap.getSelectionIndex()) {
                case 0:
                	knobSignificanceLevelKMap.setEnabled(false);
                    labelSignificanceLevelKMap.setEnabled(false);
                    break;
                case 1:
                    knobSignificanceLevelKMap.setEnabled(true);
                    labelSignificanceLevelKMap.setEnabled(true);
                    break;
                case 2:
                    knobSignificanceLevelKMap.setEnabled(true);
                    labelSignificanceLevelKMap.setEnabled(true);
                    break;
                }
                meta.setChanged(true);
            }
        });
        
        Label sigLabel = new Label(group, SWT.NONE);
        sigLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kmap.3")); //$NON-NLS-1$
        
        this.labelSignificanceLevelKMap = new Text(group, SWT.BORDER | SWT.LEFT);
        GridData dataDelta = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelSignificanceLevelKMap.setLayoutData(dataDelta);
        labelSignificanceLevelKMap.setEditable(false);
        knobSignificanceLevelKMap = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
        knobSignificanceLevelKMap.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobSignificanceLevelKMap.setDefaultColorProfile(defaultColorProfile);
        knobSignificanceLevelKMap.setFocusedColorProfile(focusedColorProfile);
        knobSignificanceLevelKMap.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelSignificanceLevelKMap.setText(knobSignificanceLevelKMap.getValue()+"");
            	meta.setChanged(true);
            }
        }); 
        return group;
	}
	
	
    protected Composite differential(Composite parent) {
    	 Label lbl1 = new Label(parent, SWT.NONE);
         lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.differential")); //$NON-NLS-1$
         buttonDifferential = new Button(parent, SWT.CHECK);
         buttonDifferential.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
         buttonDifferential.setSelection(false);
         buttonDifferential.setEnabled(true);
         final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
         d82.horizontalSpan = 1;
         buttonDifferential.setLayoutData(d82);
         buttonDifferential.addSelectionListener(new SelectionAdapter() {
     @Override
     public void widgetSelected(final SelectionEvent arg0) {
     	if(buttonDifferential.getSelection()){
     		labelDifferentialEpsilon.setEnabled(true);
     		labelDifferentialDelta.setEnabled(true);
     		knobDifferentialEpsilon.setEnabled(true);
     		knobDifferentialDelta.setEnabled(true);
     		comboDifferentialGeneralization.setEnabled(true);
     	}else{
     		labelDifferentialEpsilon.setEnabled(false);
     		labelDifferentialDelta.setEnabled(false);
     		knobDifferentialEpsilon.setEnabled(false);
     		knobDifferentialDelta.setEnabled(false);
     		comboDifferentialGeneralization.setEnabled(false);
    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
     			labelDifferentialEpsilon.setEnabled(true);
         		labelDifferentialDelta.setEnabled(true);
         		knobDifferentialEpsilon.setEnabled(true);
         		knobDifferentialDelta.setEnabled(true);
         		comboDifferentialGeneralization.setEnabled(true);
     			buttonDifferential.setSelection(true);
     		}
     	}
     	meta.setChanged(true);
     }
 });

    	
    	
        // Create input group
        final Composite group = new Composite(parent, SWT.NONE);
        group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
        final GridLayout groupInputGridLayout = new GridLayout();
        groupInputGridLayout.numColumns = 10;
        group.setLayout(groupInputGridLayout);
        
       
        // Create epsilon slider
        final Label zLabel = new Label(group, SWT.NONE);
        zLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.differential.1")); //$NON-NLS-1$

        this.labelDifferentialEpsilon  = new Text(group, SWT.BORDER | SWT.LEFT);
        GridData data = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelDifferentialEpsilon.setLayoutData(data);
        labelDifferentialEpsilon.setEditable(false);
        knobDifferentialEpsilon = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.01d, 2d));
        knobDifferentialEpsilon.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobDifferentialEpsilon.setDefaultColorProfile(defaultColorProfile);
        knobDifferentialEpsilon.setFocusedColorProfile(focusedColorProfile);
        knobDifferentialEpsilon.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelDifferentialEpsilon.setText(knobDifferentialEpsilon.getValue()+"");
            	meta.setChanged(true);
            }
        });

        // Create delta slider
        final Label lLabel = new Label(group, SWT.NONE);
        lLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.differential.2")); //$NON-NLS-1$
        
        this.labelDifferentialDelta = new Text(group, SWT.BORDER | SWT.LEFT);
        GridData dataDelta = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelDifferentialDelta.setLayoutData(dataDelta);
        labelDifferentialDelta.setEditable(false);
        knobDifferentialDelta = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.00000000001d, 0.00001d));
        knobDifferentialDelta.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobDifferentialDelta.setDefaultColorProfile(defaultColorProfile);
        knobDifferentialDelta.setFocusedColorProfile(focusedColorProfile);
        knobDifferentialDelta.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelDifferentialDelta.setText(knobDifferentialDelta.getValue()+"");
            	meta.setChanged(true);
            }
        }); 
      
        // Create criterion combo
        final Label cLabel = new Label(group, SWT.PUSH);
        cLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.differential.3")); //$NON-NLS-1$

        comboDifferentialGeneralization = new Combo(group, SWT.READ_ONLY);
        GridData d31 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d31.verticalAlignment = SWT.CENTER;
        d31.horizontalSpan = 1;
        comboDifferentialGeneralization.setLayoutData(d31);
        comboDifferentialGeneralization.setItems(getGeneralizationDegrees());
        comboDifferentialGeneralization.select(0);

        comboDifferentialGeneralization.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });

        return group;
    }
    
    /**
     * Returns a set of all generalization degrees
     * @return
     */
    private String[] getGeneralizationDegrees() {
        List<String> result = new ArrayList<String>();
        for (GeneralizationDegree degree : GeneralizationDegree.values()) {
            String label = degree.toString().replace("_", "-").toLowerCase();
            label = label.substring(0,1).toUpperCase() + label.substring(1);
            result.add(label);
        }
        return result.toArray(new String[result.size()]);
    }
	
	 /**
     * Build
     * @param parent
     * @return
     */
    protected Composite kanonymity(Composite parent) {
		Label lbl1 = new Label(parent, SWT.NONE);
        lbl1.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kanonymity")); //$NON-NLS-1$
        buttonKAnonymity = new Button(parent, SWT.CHECK);
        buttonKAnonymity.setText(Messages.getString("ARXPluginDialog.criteria.privacy.enabled")); //$NON-NLS-1$
        buttonKAnonymity.setSelection(false);
        buttonKAnonymity.setEnabled(true);
        final GridData d82 = ARXDialogGeneralTab.createFillHorizontallyGridData();
        d82.horizontalSpan = 1;
        buttonKAnonymity.setLayoutData(d82);
        buttonKAnonymity.addSelectionListener(new SelectionAdapter() {
    @Override
    public void widgetSelected(final SelectionEvent arg0) {
    	if(buttonKAnonymity.getSelection()){
    		labelKAnonymity.setEnabled(true);
    		knobK.setEnabled(true);
    	}else{
    		labelKAnonymity.setEnabled(false);
    		knobK.setEnabled(false);
    		if(!buttonPopulationUniqueness.getSelection()&&!buttonSampleUniqueness.getSelection()&&!buttonReidentification.getSelection()&&!buttonKMap.getSelection()&&!buttonKAnonymity.getSelection()&&!buttonDifferential.getSelection()){
    			labelKAnonymity.setEnabled(true);
        		knobK.setEnabled(true);
        		buttonKAnonymity.setSelection(true);
    		}
    	}
    	meta.setChanged(true);
    }
});
    	
    	
    	Composite group = new Composite(parent, SWT.NONE);
         group.setLayoutData(ARXDialogGeneralTab.createFillHorizontallyGridData());
         GridLayout groupInputGridLayout = new GridLayout();
         groupInputGridLayout.numColumns = 5;
         group.setLayout(groupInputGridLayout);
    	//Kaononymity
    	
        // Create k slider
        Label kLabel = new Label(group, SWT.NONE);
        kLabel.setText(Messages.getString("ARXPluginDialog.criteria.privacy.kanonymity.1")); //$NON-NLS-1$

        this.labelKAnonymity  = new Text(group, SWT.BORDER | SWT.LEFT);
        GridData data = ARXDialogGeneralTab.createFillHorizontallyGridData(false);
        labelKAnonymity.setLayoutData(data);
        labelKAnonymity.setEditable(false);
        knobK = new Knob<Integer>(group, SWT.NULL, new KnobRange.Integer(0, 1000));
        knobK.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
        knobK.setDefaultColorProfile(defaultColorProfile);
        knobK.setFocusedColorProfile(focusedColorProfile);
        knobK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent arg0) {
            	labelKAnonymity.setText(knobK.getValue()+"");
            	meta.setChanged(true);
            }
        });

        return group;
    }

	public void getData() {
		this.buttonKAnonymity.setSelection(this.meta.isKanonymityEnabled());
		if(buttonKAnonymity.getSelection()){
    		labelKAnonymity.setEnabled(true);
    		knobK.setEnabled(true);
    	}else{
    		labelKAnonymity.setEnabled(false);
    		knobK.setEnabled(false);
    	}
		this.knobK.setValue(Integer.valueOf(this.meta.getKanonymity()));
		this.labelKAnonymity.setText(this.meta.getKanonymity()+"");
		this.buttonDifferential.setSelection(this.meta.isDifferentialEnabled());
		if(buttonDifferential.getSelection()){
    		labelDifferentialEpsilon.setEnabled(true);
    		labelDifferentialDelta.setEnabled(true);
    		knobDifferentialEpsilon.setEnabled(true);
    		knobDifferentialDelta.setEnabled(true);
    		comboDifferentialGeneralization.setEnabled(true);
    	}else{
    		labelDifferentialEpsilon.setEnabled(false);
    		labelDifferentialDelta.setEnabled(false);
    		knobDifferentialEpsilon.setEnabled(false);
    		knobDifferentialDelta.setEnabled(false);
    		comboDifferentialGeneralization.setEnabled(false);
    	}
		this.labelDifferentialDelta.setText(this.meta.getDifferentialDelta()+"");
		this.labelDifferentialEpsilon.setText(this.meta.getDifferentialEpsilon()+"");
		this.knobDifferentialDelta.setValue(this.meta.getDifferentialDelta());
		this.knobDifferentialEpsilon.setValue(this.meta.getDifferentialEpsilon());
		this.comboDifferentialGeneralization.select(this.meta.getDifferentialGeneralizationIndex());
		this.labelKMap.setText(meta.getKmap()+"");
		this.knobKMap.setValue(this.meta.getKmap());
		this.labelSignificanceLevelKMap.setText(this.meta.getKmapSignificanceLevel()+"");
		this.knobSignificanceLevelKMap.setValue(this.meta.getKmapSignificanceLevel());
		this.buttonKMap.setSelection(this.meta.isKmapEnabled());
		if(buttonKMap.getSelection()){
    		knobKMap.setEnabled(true);
    		labelKMap.setEnabled(true);
    	    cmbModelKMap.setEnabled(true);
    	    if(cmbModelKMap.getSelectionIndex()!=0){
    	    	knobSignificanceLevelKMap.setEnabled(true);
    	    	labelSignificanceLevelKMap.setEnabled(true);
    	    }else{
    	    	knobSignificanceLevelKMap.setEnabled(false);
    	    	labelSignificanceLevelKMap.setEnabled(false);
    	    }
    	}else{
    		knobKMap.setEnabled(false);
    		labelKMap.setEnabled(false);
    	    cmbModelKMap.setEnabled(false);
    	    knobSignificanceLevelKMap.setEnabled(false);
    	    labelSignificanceLevelKMap.setEnabled(false);
    	}
		this.buttonSampleUniqueness.setSelection(this.meta.isSampleUniquenessEnabled());
		this.labelSampleUniqueness.setText(this.meta.getSampleUniqueness()+"");
		this.knobSampleUniqueness.setValue(this.meta.getSampleUniqueness());
		if(this.buttonSampleUniqueness.getSelection()){
			labelSampleUniqueness.setEnabled(true);
			knobSampleUniqueness.setEnabled(true);
		}else{
			labelSampleUniqueness.setEnabled(false);
			knobSampleUniqueness.setEnabled(false);
		}
		this.buttonReidentification.setSelection(this.meta.isReidentificationRiskEnabled());
		this.labelReidentification.setText(this.meta.getReidentificationRisk()+"");
		this.knobReidentification.setValue(this.meta.getReidentificationRisk());
		if(this.buttonReidentification.getSelection()){
			labelReidentification.setEnabled(true);
			knobReidentification.setEnabled(true);
		}else{
			labelReidentification.setEnabled(false);
			knobReidentification.setEnabled(false);
		}
		this.buttonPopulationUniqueness.setSelection(this.meta.isPopulationUniquenessEnabled());
		this.labelPopulationUniqueness.setText(this.meta.getPopulationUniqueness()+"");
		this.knobPopulationUniqueness.setValue(this.meta.getPopulationUniqueness());
		this.comboPopulationUniqueness.select(this.meta.getPopulationUniquenessModel());
		if(this.buttonPopulationUniqueness.getSelection()){
			labelPopulationUniqueness.setEnabled(true);
			knobPopulationUniqueness.setEnabled(true);
			this.comboPopulationUniqueness.setEnabled(true);
		}else{
			labelPopulationUniqueness.setEnabled(false);
			knobPopulationUniqueness.setEnabled(false);
			this.comboPopulationUniqueness.setEnabled(false);

		}
		
	}

	public void saveData() {
		if(this.buttonKAnonymity.getSelection()){
			this.meta.setKanonymity(this.knobK.getValue().intValue());
		}
		this.meta.setKanonymityEnabled(this.buttonKAnonymity.getSelection());
		this.meta.setDifferentialEnabled(this.buttonDifferential.getSelection());
		if(this.buttonDifferential.getSelection()){
			this.meta.setDifferentialGeneralizationIndex(this.comboDifferentialGeneralization.getSelectionIndex());
			this.meta.setDifferentialDelta(this.knobDifferentialDelta.getValue());
			this.meta.setDifferentialEpsilon(this.knobDifferentialEpsilon.getValue());
		}
		this.meta.setKmapEnabled(this.buttonKMap.getSelection());
		if(this.buttonKMap.getSelection()){
			this.meta.setKmap(this.knobKMap.getValue());
			this.meta.setKmapEstimatorIndex(this.cmbModelKMap.getSelectionIndex());
			this.meta.setKmapSignificanceLevel(this.knobSignificanceLevelKMap.getValue());
		}
		this.meta.setReidentificationRiskEnabled(this.buttonReidentification.getSelection());
		if(this.buttonReidentification.getSelection()){
			this.meta.setReidentificationRisk(this.knobReidentification.getValue());
		}
		this.meta.setSampleUniquenessEnabled(this.buttonSampleUniqueness.getSelection());
		if(this.buttonSampleUniqueness.getSelection()){
			this.meta.setSampleUniqueness(this.knobSampleUniqueness.getValue());
		}
		this.meta.setPopulationUniquenessEnabled(this.buttonPopulationUniqueness.getSelection());
		if(this.buttonPopulationUniqueness.getSelection()){
			this.meta.setPopulationUniqueness(this.knobPopulationUniqueness.getValue());
			this.meta.setPopulationUniquenessModel(this.comboPopulationUniqueness.getSelectionIndex());
		}
	}

}
