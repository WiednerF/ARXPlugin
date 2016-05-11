package org.deidentifier.arx.kettle.dialoge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.meta.ARXFields;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.ui.core.PropsUI;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

public class ARXDialogTransformationAttributWeight implements ARXPluginDialogInterface {
	 /** Constant */
    private static final int        MIN_SPACE  = 60;

    /** Constant */
    private static final int        MIN_KNOB   = 30;

    final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;
	Boolean enabled = false;
	private Composite composite;
	private String[] fields;
	private ArrayList<Knob<Double>> knobs;
	private ArrayList<Text> labels;
	 /** Color profile */
    private final KnobColorProfile defaultColorProfile;
    /** Color profile */
    private final KnobColorProfile focusedColorProfile;
    private String[] fieldNames;
	
	public ARXDialogTransformationAttributWeight(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod,String[] fieldNames) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.knobs=new ArrayList<Knob<Double>>();
		this.labels=new ArrayList<Text>();
		this.fieldNames=fieldNames;
	
        // Color profiles
        this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());
        
		this.build(parent);
	}

	public void getData() {
		this.setEnabled(this.enabled);
	}

	public void saveData() {
		if(this.knobs.size()!=0){
			for(int i=0;i<this.knobs.size();i++){
				 ARXFields field=meta.getField(fieldNames[i]);
				 field.setAttributeWeight(this.knobs.get(i).getValue());
				 meta.setField(field);
			}
		}

	}
	
	public void setEnabled(boolean enabled){
		this.enabled=enabled;
		if(this.enabled==false){
			for(int i=0;i<this.fieldNames.length;i++){
				this.knobs.get(i).setEnabled(false);
				this.labels.get(i).setEnabled(false);
			}
		}else{
			this.fields=this.meta.getQuasiIdentifierFields(this.fieldNames);
			for(int i=0;i<this.fieldNames.length;i++){
				this.knobs.get(i).setEnabled(false);
				this.labels.get(i).setEnabled(false);
			}
			for(String field:this.fields){
				for(int i=0;i<this.fieldNames.length;i++){
					if(field.equals(this.fieldNames[i])){
						this.knobs.get(i).setEnabled(true);
						this.labels.get(i).setEnabled(true);
					}
				}
			}
		}
		
	}
	
	private void build(Composite parent){
			
			 // Create layout
			this.composite = new Composite(parent, SWT.NONE);
			this.composite.setLayout(GridLayoutFactory.swtDefaults().numColumns(fieldNames.length).margins(0, 0).equalWidth(true).create());
	        	 // Create composites
	            List<Composite> composites = new ArrayList<Composite>();
	            for(int i=0; i<this.fieldNames.length; i++){
	                Composite c = new Composite(this.composite, SWT.NONE);
	                c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.CENTER).create());
	                c.setLayout(GridLayoutFactory.swtDefaults().numColumns(1).margins(2, 0).create());
	                composites.add(c);
	            }
	            
	            // Create labels
	            for(int i=0; i<this.fieldNames.length; i++){
	                Label label = new Label(composites.get(i), SWT.CENTER);
	                label.setText(this.fieldNames[i]);
	                label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	            }
	            
	            // Create knob widgets
	            knobs = new ArrayList<Knob<Double>>();
	            for(int i=0; i<this.fieldNames.length; i++){
	                Knob<Double> knob = new Knob<Double>(composites.get(i), SWT.NULL, new KnobRange.Double(0d, 1d));
	                knob.setLayoutData(GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(MIN_KNOB, MIN_KNOB).create());
	                knob.setDefaultColorProfile(defaultColorProfile);
	                knob.setFocusedColorProfile(focusedColorProfile);
	                knobs.add(knob);
	            }

	            // Create labels
	            labels = new ArrayList<Text>();
	            for(int i=0; i<this.fieldNames.length; i++){
	                
	                final Text label = new Text(composites.get(i), SWT.CENTER);
	                label.setText("0.0"); //$NON-NLS-1$
	                label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	                label.setEditable(false);
	                label.setEnabled(false);
	                final Knob<Double> knob = knobs.get(i);
	                knob.addSelectionListener(new SelectionAdapter(){
	                    public void widgetSelected(SelectionEvent arg0) {
	                        double value = knob.getValue();
	                        label.setText(value+"");
	                        label.setToolTipText(String.valueOf(value));
	                        meta.setChanged(true);
	                    }
	                });
	                labels.add(label);
	            }
	            
	            // Set values
	            for(int i=0; i<this.fieldNames.length; i++){
	            	ARXFields field=meta.getField(fieldNames[i]);
	                    knobs.get(i).setValue(field.getAttributeWeight());
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
	

}
