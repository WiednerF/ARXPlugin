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
package org.deidentifier.arx.kettle.risk;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.view.LayoutRisks;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;

/**
 * Creates the Bottom View for the In and the Output Analysis
 * @author Florian Wiedner
 * @since 1.7
 * @version 1.0
 *
 */
public class LayoutBottom {
	
	/**View**/
	private final CTabFolder folder;
	/**
	 * The Data from the Input and the Configuration
	 */
	private final Data data;
	/**
	 * The Population Model Used
	 */
    private final ARXPopulationModel population;
    /**
     * The DataHandle from the Input or the Output
     */
    private final DataHandle result;
    /**
     * The Parent Risk Layout Model for Updating
     */
    private final LayoutRisks risk;

    /**
     * 
     * Creates the View for Output
	 * @param parent The Parent Composite
	 * @param result The Result DataHandle for In or Output
	 * @param data The Data with Configuration and Definition
	 * @param population The Population Model used
     */
	public LayoutBottom(final Composite parent,final DataHandle result,final Data data,final ARXPopulationModel population) {
		this(parent,result,data,population,null);
	}
	
	/**
	 * Creates the View for In or Output
	 * @param parent The Parent Composite
	 * @param result The Result DataHandle for In or Output
	 * @param data The Data with Configuration and Definition
	 * @param population The Population Model used
	 * @param risk The Risk Parent Layout for Update, null for Output
	 */
	public LayoutBottom(final Composite parent,final DataHandle result,final Data data,final ARXPopulationModel population,LayoutRisks risk) {
		this.data=data;
		this.population=population;
		this.result=result;
		this.risk=risk;
		folder = new CTabFolder( parent,SWT.BORDER );
		folder.setSimple( false );
		this.build(folder);      
	      //END TABS
	     folder.setSelection(0);
		
	}
	
	/**
	 * Builds the View
	 * @param folder The Folder as Parent of the Tabs of this View
	 */
	private void build(final CTabFolder folder){
		new ViewRisksReidentificationRisksTable(SWTUtil.createItem(folder, "RiskAnalysis.5"),result,data,population);
		new ViewRisksPopulationUniqueness(SWTUtil.createItem(folder, "RiskAnalysis.13"),result,population,false);
		new ViewRisksPopulationUniqueness(SWTUtil.createItem(folder, "RiskAnalysis.12"),result,population,true);
		if(risk!=null){
			 new ViewRisksQuasiIdentifier(SWTUtil.createItem(folder, "RiskAnalysis.23"),risk);
		}
	}
	
	/**
	 * 
	 * @param index
	 *            Set the Selection to this Index
	 */
	public void setSelectionIndex(int index) {
		this.folder.setSelection(index);
	}

	/**
	 * 
	 * @param listener
	 *            Add this as a SelectionListener to this Folder
	 */
	public void addSelectionListener(SelectionListener listener) {
		this.folder.addSelectionListener(listener);
	}

	/**
	 * 
	 * @return The Actual Selected Part
	 */
	public int getSelectionIndex() {
		return this.folder.getSelectionIndex();
	}

}
