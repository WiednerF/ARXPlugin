package org.deidentifier.arx.kettle.dialoge;

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.dialoge.resources.ComponentGSSlider;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.ui.core.PropsUI;

public class ARXDialogTransformationCoding implements ARXPluginDialogInterface {

final Composite parent;
	
	final ARXPluginMeta meta;
	
	final PropsUI props;
	
	final ModifyListener lsMod;	

    public ComponentGSSlider slider;

	
	public ARXDialogTransformationCoding(final Composite parent,ARXPluginMeta meta, final PropsUI props, ModifyListener lsMod) {
		this.parent=parent;
		this.meta=meta;
		this.props=props;
		this.lsMod = lsMod;
		this.build();
	}
	public void build(){
		this.slider = new ComponentGSSlider(parent);
        this.slider.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent arg0) {
                meta.setChanged(true);
            }
        });
	}
	public void getData() {
		this.slider.setSelection(this.meta.getGSFactor());

	}

	public void saveData() {
		this.meta.setGSFactor(this.slider.getSelection());
	}

}
