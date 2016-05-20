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

import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.define.common.ComponentGSSlider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * The Class contains the View for the GUI of the Coding Model in the Transformation
 * @author Florian Wiedner
 * @category LayoutTransformationModel
 * @since 1.7
 *
 */
public class ViewCodingModel implements LayoutCompositeInterface {

	/**
	 * The Meta Information for Saving and getting Saved Data
	 */
	private final ARXPluginMeta meta;
	

	/**
	 * The Slider for this Special Case
	 */
    private ComponentGSSlider slider;

	/**
	 * The CodingView Model used for the Coding Model of the Transformation Settings
	 * @param parent The Parent Composite
	 * @param meta The Meta Information
	 */
	public ViewCodingModel(final Composite parent,final ARXPluginMeta meta) {
		this.meta=meta;
		this.build(parent);
	}
	
	/**
	 * The building for the final build of the Coding Model
	 * @param parent The Parent Composite
	 */
	public void build(final Composite parent){
		this.slider = new ComponentGSSlider(parent);
        this.slider.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent arg0) {
            	meta.setGSFactor(slider.getSelection());
                meta.setChanged(true);
            }
        });
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		this.slider.setSelection(this.meta.getGSFactor());
	}

}
