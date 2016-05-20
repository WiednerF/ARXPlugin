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
package org.deidentifier.arx.kettle;

import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.define.LayoutCompositeInterface;
import org.deidentifier.arx.kettle.define.LayoutField;
import org.deidentifier.arx.kettle.define.LayoutGeneral;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;

/**
 * Implements the Complete Definition Dialog
 * 
 * @author Florian Wiedner
 * @category Dialog, Defintion
 * @version 1.0
 * @since 1.7
 *
 */
public class ARXPluginDialog extends org.pentaho.di.ui.trans.step.BaseStepDialog
		implements org.pentaho.di.trans.step.StepDialogInterface {
	/**
	 * for i18n purposes, needed by Translator2!!
	 */
	private static Class<?> PKG = ARXPluginMeta.class; // for i18n purposes,
														// needed by
														// Translator2!!

	/**
	 * The Input Structure
	 */
	private ARXPluginMeta input;// The InputStructure

	/**
	 * The FormLayout for this Step
	 */
	private FormLayout formLayout;
	/**
	 * The Folder for the Main Part of the Definiton
	 */
	private CTabFolder wTabFolder;// The Common Tab plugin
	/**
	 * The FieldNames from the Previous Step
	 */
	private String[] fieldNames;
	/** VIEW **/
	private Button fdbShowAnalysis;
	/**
	 * The Children Composites for this Dialog
	 */
	private LayoutCompositeInterface[] composites;

	/**
	 * Creates the new Dialog, Creates all Data and Save it
	 * @param parent The Parent Shell
	 * @param in The Input Meta Object
	 * @param transMeta The TransMeta Data
	 * @param sname The StepName
	 */
	public ARXPluginDialog(org.eclipse.swt.widgets.Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		input = (ARXPluginMeta) in;
		composites = new LayoutCompositeInterface[2];
	}

	/*
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.StepDialogInterface#open()
	 */
	public String open() {
		Shell parent = getParent();// Get the Shell for Working
		Display display = parent.getDisplay();// Get the Working Display Set

		try {
			RowMetaInterface r;
			r = transMeta.getPrevStepFields(stepname);
			fieldNames = r.getFieldNames();

		} catch (KettleStepException e2) {
			Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.setSize(100, 100);
			dialog.setText("Error! Could not Load the Method!");
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			this.cancel();
		}

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);

		props.setLook(shell);
		setShellImage(shell, input);

		ModifyListener lsMod = new ModifyListener() {// Listen for Changes
			public void modifyText(ModifyEvent e) {
				input.setChanged();
			}
		};
		changed = input.hasChanged();// If something changed, for Updating
										// purpose
		formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Resources.getMessage("ARXPluginDialog.Title"));
		int middle = props.getMiddlePct();// The Middle Point
		int margin = Const.MARGIN;// The Normal Margin

		Label wlShowAnalysis = new Label(shell, SWT.RIGHT);
		wlShowAnalysis.setText(Resources.getMessage("ARXPluginDialog.show"));
		props.setLook(wlShowAnalysis);
		FormData fdlShowAnalysis = new FormData();
		fdlShowAnalysis.left = new FormAttachment(0, 0);
		fdlShowAnalysis.top = new FormAttachment(0, margin);
		fdlShowAnalysis.right = new FormAttachment(middle, -margin);
		wlShowAnalysis.setLayoutData(fdlShowAnalysis);
		this.fdbShowAnalysis = new Button(shell, SWT.CHECK);
		this.fdbShowAnalysis.setSelection(true);
		FormData fdbShowAnalysisData = new FormData();
		fdbShowAnalysisData.left = new FormAttachment(middle, 0);
		fdbShowAnalysisData.top = new FormAttachment(0, margin);
		fdbShowAnalysisData.right = new FormAttachment(100, 0);
		this.fdbShowAnalysis.setLayoutData(fdbShowAnalysisData);
		this.fdbShowAnalysis.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				input.setShowRiskAnalysis(fdbShowAnalysis.getSelection());
				input.setChanged(true);
			}

		});
		// ADD StepName
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.top = new FormAttachment(wlShowAnalysis, margin);
		fdlStepname.right = new FormAttachment(middle, -margin);
		wlStepname.setLayoutData(fdlStepname);
		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(wlShowAnalysis, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);
		// END STEPNAME

		// TABS
		wTabFolder = new CTabFolder(shell, SWT.BORDER);
		props.setLook(wTabFolder, Props.WIDGET_STYLE_TAB);
		wTabFolder.setSimple(false);
		wTabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				composites[wTabFolder.getSelectionIndex()].getData();
			}
		});

		this.composites[0] = new LayoutGeneral(wTabFolder, input, props, fieldNames);
		this.composites[1] = new LayoutField(wTabFolder, input, props, fieldNames, transMeta);
		// END TABS
		wTabFolder.setSelection(0);
		FormData fdTabFolder = new FormData();
		fdTabFolder.left = new FormAttachment(0, 0);
		fdTabFolder.top = new FormAttachment(wStepname, margin);
		fdTabFolder.right = new FormAttachment(100, 0);
		fdTabFolder.bottom = new FormAttachment(100, -50);
		wTabFolder.setLayoutData(fdTabFolder);
		// BUTTON
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));

		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

		setButtonPositions(new Button[] { wOK, wCancel }, margin, wTabFolder);

		lsOK = new Listener() {
			public void handleEvent(Event e) {
				ok();
			}
		};
		wOK.addListener(SWT.Selection, lsOK);
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		wCancel.addListener(SWT.Selection, lsCancel);
		// SHELL CLOSE HANDLE
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});

		setSize();
		getData();
		// OPEN AND CONTROL
		input.setChanged(changed);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.stepname;
	}

	/**
	 * Reacts when you press OK
	 */
	private void ok() {
		if (Const.isEmpty(wStepname.getText())) {
			return;
		}

		this.stepname = wStepname.getText(); // return value

		dispose();
	}

	/**
	 * Reacts when you press Cancel
	 */
	private void cancel() {
		stepname = null;

		input.setChanged(backupChanged);

		dispose();
	}

	/**
	 * Writes the Data to the Plugin Dialog
	 */
	private void getData() {
		this.fdbShowAnalysis.setSelection(this.input.isShowRiskAnalysis());
	}

}
