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

import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.ui.core.PropsUI;

/**
 * Contains the Layout for the Complete General Tab
 * @author Florian Wiedner
 * @category ARXPluginDialog
 * @version 1.0
 * @since 1.7
 *
 */
public class LayoutGeneral implements LayoutCompositeInterface {
	
	/**
	 * The Meta Data for the Project Step
	 */
	private final ARXPluginMeta meta;

	/**
	 * The PropsUI from the Kettle Project
	 */
	private final PropsUI props;

	/**
	 * The Composite Container for the Parts of this Tab
	 */
	private LayoutCompositeInterface[] composites;
	/**
	 * The FieldNames from the Previous Step
	 */
	private String[] fieldNames;

	/**
	 * Creates the Complete General Tab
	 * @param parent The Parent Folder
	 * @param meta The Meta Include for this Project
	 * @param props The PropsUI from the Kettle Project
	 * @param fieldNames The FieldNames from the Previous Step
	 */
	public LayoutGeneral(final CTabFolder parent, ARXPluginMeta meta, final PropsUI props, String[] fieldNames) {
		this.meta = meta;
		this.props = props;
		this.fieldNames = fieldNames;
		composites = new LayoutCompositeInterface[2];

		CTabItem tabGeneral = new CTabItem(parent, SWT.NONE);
		tabGeneral.setText(Resources.getMessage("General.2"));
		ScrolledComposite scroller = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		this.build(scroller);
		tabGeneral.setControl(scroller);
		{

		}
	}

	/**
	 * Builds the Inner Composite of the Tab and all the Content
	 * @param parent The Parent Scroller from the TabItem
	 */
	private void build(ScrolledComposite parent) {
		Composite tabGeneralComp = new Composite(parent, SWT.NONE);
		props.setLook(tabGeneralComp);

		tabGeneralComp.setLayout(SWTUtil.createGridLayout(1));
		Composite compositeTop;
		Composite compositeBottom;

		// Create center-right composite
		compositeTop = new Composite(tabGeneralComp, SWT.NONE);
		compositeTop.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		compositeTop.setLayout(SWTUtil.createGridLayout(1));

		// Create bottom-right composite
		compositeBottom = new Composite(tabGeneralComp, SWT.NONE);
		compositeBottom.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		compositeBottom.setLayout(SWTUtil.createGridLayout(1));

		this.composites[0] = new LayoutTransformationModel(compositeBottom, this.meta, this.props, this.fieldNames);
		this.composites[1] = new LayoutCriteria(compositeTop, this.meta, this.props, this.fieldNames);

		FormData tabGeneralCompData = new FormData();
		tabGeneralCompData.left = new FormAttachment(0, 0);
		tabGeneralCompData.top = new FormAttachment(0, 0);
		tabGeneralCompData.right = new FormAttachment(100, 0);
		tabGeneralCompData.bottom = new FormAttachment(100, 0);
		tabGeneralComp.setLayoutData(tabGeneralCompData);
		tabGeneralComp.layout();
		parent.setContent(tabGeneralComp);
		parent.setExpandVertical(true);
		parent.setExpandHorizontal(true);
		parent.setMinSize(tabGeneralComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/*
	 * (non-Javadoc)
	 * @see org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface#getData()
	 */
	public void getData() {
		for (LayoutCompositeInterface composite : this.composites) {
			composite.getData();
		}
	}

}
