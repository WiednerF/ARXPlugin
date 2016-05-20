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
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.PropsUI;

/**
 * Creates the Layout and the View of the complete Attribute Part and the Saving Work
 * @author Florian Wiedner
 * @category Dialog, Design, Layout
 * @since 1.7
 * @version 1.0
 */
public class LayoutField implements LayoutCompositeInterface {
	/**
	 * The Meta Data for the Complete Project
	 */
	private final ARXPluginMeta meta;
	/**
	 * The PropsUI from the Kettle Project
	 */
	private final PropsUI props;
	/**
	 * The FieldName List from the Previous Step
	 */
	private String[] fieldNames;
	/**
	 * The TransMeta for this Step
	 */
	private TransMeta transMeta;
	/**
	 * The Privacy Tab for the Attributes
	 */
	private ViewCriteriaListField privacy;
	/**
	 * The Transformation Settings for the Attributes
	 */
	private ViewAttributeTransformation transformation;
	/**
	 * The Actual Attribute/Field
	 */
	private String field;

	/**
	 * Creates the new View of the FieldTab
	 * @param parent The Parent Folder
	 * @param meta The Meta for Saving
	 * @param props The PropsUI
	 * @param fieldNames The FieldNames
	 * @param transMeta The TransMeta for this Step
	 */
	public LayoutField(final CTabFolder parent, ARXPluginMeta meta, final PropsUI props,
			String[] fieldNames, TransMeta transMeta) {
		this.meta = meta;
		this.props = props;
		this.fieldNames = fieldNames;
		this.transMeta = transMeta;
		this.field=fieldNames[0];
		
		/**
		 * TAB FieldWise ***************
		 */
		CTabItem tabField = new CTabItem(parent, SWT.NONE);
		tabField.setText(Resources.getMessage("Attribut.2"));

		ScrolledComposite scroller = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		Composite tabFieldComp = new Composite(scroller, SWT.NONE);
		props.setLook(tabFieldComp);

		tabFieldComp.setLayout(SWTUtil.createGridLayout(1));
		
		this.build(tabFieldComp);
		
		FormData tabFieldFileComp = new FormData();
		tabFieldFileComp.left = new FormAttachment(0, 0);
		tabFieldFileComp.top = new FormAttachment(0, 0);
		tabFieldFileComp.right = new FormAttachment(100, 0);
		tabFieldFileComp.bottom = new FormAttachment(100, 0);
		tabFieldComp.setLayoutData(tabFieldFileComp);
		tabFieldComp.layout();
		tabField.setControl(scroller);
		scroller.setContent(tabFieldComp);
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setMinSize(tabFieldComp.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	/**
	 * Builds the new View
	 * @param parent The Parent Composite
	 */
	private void build(Composite parent) {
		// Create top composite
		Composite compositeTop = new Composite(parent, SWT.NONE);
		compositeTop.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		compositeTop.setLayout(SWTUtil.createGridLayout(1));

		// Create center composite
		Composite compositeCenter = new Composite(parent, SWT.NONE);
		compositeCenter.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		compositeCenter.setLayout(SWTUtil.createGridLayout(1));

		// Create bottom composite
		Composite compositeBottom = new Composite(parent, SWT.NONE);
		compositeBottom.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		compositeBottom.setLayout(SWTUtil.createGridLayout(1));

		this.fieldList(compositeTop);
		this.transformation = new ViewAttributeTransformation(compositeCenter, meta, props, this, transMeta,
				this.field);
		this.privacy = new ViewCriteriaListField(compositeBottom, meta, props,
				this.field);
		this.transformation.setFieldName(this.field);
		this.privacy.setFieldName(this.field);
	}

	/**
	 * Creates the Attribute List
	 * @param parent The parent Composite
	 */
	private void fieldList(Composite parent) {
		// Create input group
		final Composite group = new Composite(parent, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		for (int i=0; i<this.fieldNames.length; i++) {
			final int x = i;
			Button button = new Button (group, SWT.RADIO);
			button.setText (this.fieldNames[i]);
			if (i == 0) button.setSelection (true);
			button.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(final SelectionEvent arg0) {
					field=fieldNames[x];
					privacy.setFieldName(field);
					transformation.setFieldName(field);
					meta.setChanged(true);
				}
			});
			
		}
	}

	/*
	 * 
	 */
	public void getData() {
		this.privacy.setFieldName(this.field);
		this.transformation.setFieldName(this.field);
	}

	/**
	 * Enables or Disables the Sensitive Attribut Privacy Criteria
	 * @param enabled Enable or Disable
	 */
	public void setEnabled(boolean enabled) {
		this.privacy.enable(enabled);
	}
}
