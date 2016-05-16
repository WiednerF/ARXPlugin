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

import org.deidentifier.arx.DataGeneralizationScheme.GeneralizationDegree;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.SWTUtil;
import org.deidentifier.arx.kettle.ARXPluginMeta;
import org.deidentifier.arx.kettle.dialoge.ARXPluginDialogInterface;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

/**
 * Creates the View for the Not Field Dependend Privacy Criteria
 * @author Florian Wiedner
 * @version 1.0
 * @category LayoutCriteria
 * @since 1.7
 *
 */
public class ViewCriteriaList implements ARXPluginDialogInterface {
	/**
	 * The Meta Data for the Whole Project
	 */
	private final ARXPluginMeta meta;

	/** View */
	private final Composite root;

	/** View */
	private Button buttonKAnonymity,buttonDifferential,buttonKMap,buttonReidentification,buttonSampleUniqueness,buttonPopulationUniqueness;
	/** View */
	private Text labelKAnonymity, labelDifferentialDelta, labelDifferentialEpsilon,labelKMap,labelSignificanceLevelKMap,labelReidentification,
				labelSampleUniqueness,labelPopulationUniqueness;
	/** View */
	private Knob<Integer> knobK,knobKMap;
	private Knob<Double> knobDifferentialEpsilon, knobDifferentialDelta,knobSignificanceLevelKMap,knobReidentification,
			knobSampleUniqueness,knobPopulationUniqueness;
	/** View */
	private Combo comboDifferentialGeneralization,cmbModelKMap,comboPopulationUniqueness;

	/** Color profile */
	private final KnobColorProfile defaultColorProfile;
	/** Color profile */
	private final KnobColorProfile focusedColorProfile;
	/** Images **/
	private final Image symbolK = Resources.getImage("symbol_k.png"); //$NON-NLS-1$
	/** Images **/
	private final Image symbolR = Resources.getImage("symbol_r.png"); //$NON-NLS-1$
	/** Images **/
	private final Image symbolDP = Resources.getImage("symbol_dp.png"); //$NON-NLS-1$
	/**
	 * The FieldNames from the Previous Step
	 */
	private String[] fieldNames;
	/**
	 * If you have Sensitive Data or not
	 */
	private boolean sensitive=false;

	/**
	 * Creates the New Privacy Criteria View
	 * @param parent The Composite Parent
	 * @param meta The Meta Data Information
	 */
	public ViewCriteriaList(final Composite parent, ARXPluginMeta meta,String[] fieldNames) {
		this.meta = meta;
		this.fieldNames=fieldNames;

		this.defaultColorProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
		this.focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile(parent.getDisplay());

		// Create group
		root = new Composite(parent, SWT.NONE);
		root.setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());

		this.build(this.root);
		this.getData();
	}

	/**
	 * Builds the View
	 * @param parent The Parent Composite for adding This with a 4 Column Layout
	 */
	private void build(final Composite parent) {
		this.kanonymity(parent);
		this.differential(parent);
		this.KMap(parent);
		this.Reidentification(parent);
		this.SampleUniqueness(parent);
		this.PopulationUniqueness(parent);
	}

	/**
	 * Creates the View for the Population Uniqueness Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void PopulationUniqueness(Composite parent) {
		this.createLabel(Resources.getMessage("Model.21"), parent, symbolR);
		buttonPopulationUniqueness=this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonPopulationUniqueness.getSelection()) {
					knobPopulationUniqueness.setEnabled(true);
					labelPopulationUniqueness.setEnabled(true);
					comboPopulationUniqueness.setEnabled(true);
				} else {
					knobPopulationUniqueness.setEnabled(false);
					labelPopulationUniqueness.setEnabled(false);
					comboPopulationUniqueness.setEnabled(false);
					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						knobPopulationUniqueness.setEnabled(true);
						labelPopulationUniqueness.setEnabled(true);
						comboPopulationUniqueness.setEnabled(true);
						buttonPopulationUniqueness.setSelection(true);
					}
				}
				meta.setPopulationUniquenessEnabled(buttonPopulationUniqueness.getSelection());
				meta.setChanged(true);
			}
		});
		
		// Create input group
		final Composite group = this.createGroup(5,parent);

		// Create threshold slider
		final Label zLabel = new Label(group, SWT.NONE);
		zLabel.setText(Resources.getMessage("CriterionDefinitionView.81")); //$NON-NLS-1$

		this.labelPopulationUniqueness = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillGridData();
		labelPopulationUniqueness.setLayoutData(data);
		labelPopulationUniqueness.setEditable(false);
		knobPopulationUniqueness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
		knobPopulationUniqueness.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobPopulationUniqueness.setDefaultColorProfile(defaultColorProfile);
		knobPopulationUniqueness.setFocusedColorProfile(focusedColorProfile);
		knobPopulationUniqueness.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelPopulationUniqueness.setText(knobPopulationUniqueness.getValue() + "");
				meta.setPopulationUniqueness(knobPopulationUniqueness.getValue());
				meta.setChanged(true);
			}
		});

		Label lblModel = new Label(group, SWT.NONE);
		lblModel.setText(Resources.getMessage("EditorCriterionRiskBased.0")); //$NON-NLS-1$

		comboPopulationUniqueness = new Combo(group, SWT.READ_ONLY);
		comboPopulationUniqueness.setItems(
				new String[]{Resources.getMessage("EditorCriterionRiskBased.1"), //$NON-NLS-1$
                        Resources.getMessage("EditorCriterionRiskBased.2"), //$NON-NLS-1$
                        Resources.getMessage("EditorCriterionRiskBased.3"), //$NON-NLS-1$
                        Resources.getMessage("EditorCriterionRiskBased.4")}); //$NON-NLS-1$
		comboPopulationUniqueness.select(0);
		comboPopulationUniqueness.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				meta.setPopulationUniquenessModel(comboPopulationUniqueness.getSelectionIndex());
				meta.setChanged(true);
			}
		});
	}

	/**
	 * Creates the View for the Sample Uniqueness Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void SampleUniqueness(Composite parent) {
		this.createLabel(Resources.getMessage("Model.6a"), parent, symbolR);
		buttonSampleUniqueness=this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonSampleUniqueness.getSelection()) {
					knobSampleUniqueness.setEnabled(true);
					labelSampleUniqueness.setEnabled(true);
				} else {
					knobSampleUniqueness.setEnabled(false);
					labelSampleUniqueness.setEnabled(false);

					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						knobSampleUniqueness.setEnabled(true);
						labelSampleUniqueness.setEnabled(true);
						buttonSampleUniqueness.setSelection(true);
					}
				}
				meta.setSampleUniquenessEnabled(buttonSampleUniqueness.getSelection());
				meta.setChanged(true);
			}
		});
		
		// Create input group
		final Composite group = this.createGroup(3,parent);

		// Create threshold slider
		final Label zLabel = new Label(group, SWT.NONE);
		zLabel.setText(Resources.getMessage("CriterionDefinitionView.81")); //$NON-NLS-1$

		this.labelSampleUniqueness = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillHorizontallyGridData(false);
		labelSampleUniqueness.setLayoutData(data);
		labelSampleUniqueness.setEditable(false);
		knobSampleUniqueness = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
		knobSampleUniqueness.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobSampleUniqueness.setDefaultColorProfile(defaultColorProfile);
		knobSampleUniqueness.setFocusedColorProfile(focusedColorProfile);
		knobSampleUniqueness.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelSampleUniqueness.setText(knobSampleUniqueness.getValue() + "");
				meta.setSampleUniqueness(knobSampleUniqueness.getValue());
				meta.setChanged(true);
			}
		});
	}

	/**
	 * Creates the View for the Average Reidentification Risk Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void Reidentification(Composite parent) {
		this.createLabel(Resources.getMessage("Model.1a"), parent, symbolR);
		buttonReidentification=this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonReidentification.getSelection()) {
					knobReidentification.setEnabled(true);
					labelReidentification.setEnabled(true);
				} else {
					knobReidentification.setEnabled(false);
					labelReidentification.setEnabled(false);

					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						knobReidentification.setEnabled(true);
						labelReidentification.setEnabled(true);
						buttonReidentification.setSelection(true);
					}
				}
				meta.setReidentificationRiskEnabled(buttonReidentification.getSelection());
				meta.setChanged(true);
			}
		});
		
		// Create input group
		final Composite group = this.createGroup(3,parent);

		// Create threshold slider
		final Label zLabel = new Label(group, SWT.NONE);
		zLabel.setText(Resources.getMessage("CriterionDefinitionView.81")); //$NON-NLS-1$

		this.labelReidentification = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillHorizontallyGridData(false);
		labelReidentification.setLayoutData(data);
		labelReidentification.setEditable(false);
		knobReidentification = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
		knobReidentification.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobReidentification.setDefaultColorProfile(defaultColorProfile);
		knobReidentification.setFocusedColorProfile(focusedColorProfile);
		knobReidentification.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelReidentification.setText(knobReidentification.getValue() + "");
				meta.setReidentificationRisk(knobReidentification.getValue());
				meta.setChanged(true);
			}
		});
	}

	/**
	 * Creates the View for the K-Map Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void KMap(Composite parent) {
		this.createLabel(Resources.getMessage("Model.32"), parent, symbolK);
		buttonKMap=this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonKMap.getSelection()) {
					knobKMap.setEnabled(true);
					labelKMap.setEnabled(true);
					cmbModelKMap.setEnabled(true);
					if (cmbModelKMap.getSelectionIndex() != 0) {
						knobSignificanceLevelKMap.setEnabled(true);
						labelSignificanceLevelKMap.setEnabled(true);
					} else {
						knobSignificanceLevelKMap.setEnabled(false);
						labelSignificanceLevelKMap.setEnabled(false);
					}
				} else {
					knobKMap.setEnabled(false);
					labelKMap.setEnabled(false);
					cmbModelKMap.setEnabled(false);
					knobSignificanceLevelKMap.setEnabled(false);
					labelSignificanceLevelKMap.setEnabled(false);
					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						knobKMap.setEnabled(true);
						labelKMap.setEnabled(true);
						cmbModelKMap.setEnabled(true);
						if (cmbModelKMap.getSelectionIndex() != 0) {
							knobSignificanceLevelKMap.setEnabled(true);
							labelSignificanceLevelKMap.setEnabled(true);
						} else {
							knobSignificanceLevelKMap.setEnabled(false);
							labelSignificanceLevelKMap.setEnabled(false);
						}
						buttonKMap.setSelection(true);
					}
				}
				meta.setKmapEnabled(buttonKMap.getSelection());
				meta.setChanged(true);
			}
		});
		// Create input group
		final Composite group = this.createGroup(10,parent);
		// Create k slider
		Label kLabel = new Label(group, SWT.NONE);
		kLabel.setText(Resources.getMessage("CriterionDefinitionView.22")); //$NON-NLS-1$

		this.labelKMap = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillHorizontallyGridData(false);
		labelKMap.setLayoutData(data);
		labelKMap.setEditable(false);
		knobKMap = new Knob<Integer>(group, SWT.NULL, new KnobRange.Integer(2, 1000));
		knobKMap.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobKMap.setDefaultColorProfile(defaultColorProfile);
		knobKMap.setFocusedColorProfile(focusedColorProfile);
		knobKMap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelKMap.setText(knobKMap.getValue() + "");
				meta.setKmap(knobKMap.getValue());
				meta.setChanged(true);
			}
		});

		Label lblModel = new Label(group, SWT.NONE);
		lblModel.setText(Resources.getMessage("EditorCriterionKMap.0")); //$NON-NLS-1$

		cmbModelKMap = new Combo(group, SWT.READ_ONLY);
		cmbModelKMap.setItems(new String[] { // $NON-NLS-1$
				Resources.getMessage("EditorCriterionKMap.2"), //$NON-NLS-1$
                Resources.getMessage("EditorCriterionKMap.3") }); //$NON-NLS-1$
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
				meta.setKmapEstimatorIndex(cmbModelKMap.getSelectionIndex());
				meta.setChanged(true);
			}
		});

		Label sigLabel = new Label(group, SWT.NONE);
		sigLabel.setText(Resources.getMessage("CriterionDefinitionView.101")); //$NON-NLS-1$

		this.labelSignificanceLevelKMap = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData dataDelta = SWTUtil.createFillHorizontallyGridData(false);
		labelSignificanceLevelKMap.setLayoutData(dataDelta);
		labelSignificanceLevelKMap.setEditable(false);
		knobSignificanceLevelKMap = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0d, 1d));
		knobSignificanceLevelKMap.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobSignificanceLevelKMap.setDefaultColorProfile(defaultColorProfile);
		knobSignificanceLevelKMap.setFocusedColorProfile(focusedColorProfile);
		knobSignificanceLevelKMap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelSignificanceLevelKMap.setText(knobSignificanceLevelKMap.getValue() + "");
				meta.setKmapSignificanceLevel(knobSignificanceLevelKMap.getValue());
				meta.setChanged(true);
			}
		});
	}

	/**
	 * Creates the View for the E,D-Differential Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void differential(Composite parent) {
		this.createLabel(Resources.getMessage("ModelCriterion.3"), parent, symbolDP);
		buttonDifferential=this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonDifferential.getSelection()) {
					labelDifferentialEpsilon.setEnabled(true);
					labelDifferentialDelta.setEnabled(true);
					knobDifferentialEpsilon.setEnabled(true);
					knobDifferentialDelta.setEnabled(true);
					comboDifferentialGeneralization.setEnabled(true);
				} else {
					labelDifferentialEpsilon.setEnabled(false);
					labelDifferentialDelta.setEnabled(false);
					knobDifferentialEpsilon.setEnabled(false);
					knobDifferentialDelta.setEnabled(false);
					comboDifferentialGeneralization.setEnabled(false);
					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						labelDifferentialEpsilon.setEnabled(true);
						labelDifferentialDelta.setEnabled(true);
						knobDifferentialEpsilon.setEnabled(true);
						knobDifferentialDelta.setEnabled(true);
						comboDifferentialGeneralization.setEnabled(true);
						buttonDifferential.setSelection(true);
					}
				}
				meta.setDifferentialEnabled(buttonDifferential.getSelection());
				meta.setChanged(true);
			}
		});
		// Create input group
		final Composite group = this.createGroup(10,parent);
		// Create epsilon slider
		final Label zLabel = new Label(group, SWT.NONE);
		zLabel.setText(Resources.getMessage("CriterionDefinitionView.92")); //$NON-NLS-1$

		this.labelDifferentialEpsilon = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillHorizontallyGridData(false);
		labelDifferentialEpsilon.setLayoutData(data);
		labelDifferentialEpsilon.setEditable(false);
		knobDifferentialEpsilon = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.01d, 2d));
		knobDifferentialEpsilon.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobDifferentialEpsilon.setDefaultColorProfile(defaultColorProfile);
		knobDifferentialEpsilon.setFocusedColorProfile(focusedColorProfile);
		knobDifferentialEpsilon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelDifferentialEpsilon.setText(knobDifferentialEpsilon.getValue() + "");
				meta.setDifferentialEpsilon(knobDifferentialEpsilon.getValue());
				meta.setChanged(true);
			}
		});

		// Create delta slider
		final Label lLabel = new Label(group, SWT.NONE);
		lLabel.setText(Resources.getMessage("CriterionDefinitionView.93")); //$NON-NLS-1$

		this.labelDifferentialDelta = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData dataDelta = SWTUtil.createFillHorizontallyGridData(false);
		labelDifferentialDelta.setLayoutData(dataDelta);
		labelDifferentialDelta.setEditable(false);
		knobDifferentialDelta = new Knob<Double>(group, SWT.NULL, new KnobRange.Double(0.00000000001d, 0.00001d));
		knobDifferentialDelta.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobDifferentialDelta.setDefaultColorProfile(defaultColorProfile);
		knobDifferentialDelta.setFocusedColorProfile(focusedColorProfile);
		knobDifferentialDelta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelDifferentialDelta.setText(knobDifferentialDelta.getValue() + "");
				meta.setDifferentialDelta(knobDifferentialDelta.getValue());
				meta.setChanged(true);
			}
		});

		// Create criterion combo
		final Label cLabel = new Label(group, SWT.PUSH);
		cLabel.setText(Resources.getMessage("CriterionDefinitionView.94")); //$NON-NLS-1$

		comboDifferentialGeneralization = new Combo(group, SWT.READ_ONLY);
		GridData d31 = SWTUtil.createFillHorizontallyGridData();
		d31.verticalAlignment = SWT.CENTER;
		d31.horizontalSpan = 1;
		comboDifferentialGeneralization.setLayoutData(d31);
		comboDifferentialGeneralization.setItems(getGeneralizationDegrees());
		comboDifferentialGeneralization.select(0);

		comboDifferentialGeneralization.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				meta.setDifferentialGeneralizationIndex(comboDifferentialGeneralization.getSelectionIndex());
				meta.setChanged(true);
			}
		});
	}

	/**
	 * Returns a set of all generalization degrees
	 * 
	 * @return The Generalization Degrees
	 */
	private String[] getGeneralizationDegrees() {
		List<String> result = new ArrayList<String>();
		for (GeneralizationDegree degree : GeneralizationDegree.values()) {
			String label = degree.toString().replace("_", "-").toLowerCase();
			label = label.substring(0, 1).toUpperCase() + label.substring(1);
			result.add(label);
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Creates the View for the K-Anonymity Privacy Criterion
	 * @param parent The Parent Composite
	 */
	protected void kanonymity(Composite parent) {
		this.createLabel(Resources.getMessage("ModelCriterion.0"), parent, symbolK);
		buttonKAnonymity = this.createButton(parent, new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				if (buttonKAnonymity.getSelection()) {
					labelKAnonymity.setEnabled(true);
					knobK.setEnabled(true);
				} else {
					labelKAnonymity.setEnabled(false);
					knobK.setEnabled(false);
					if (!buttonPopulationUniqueness.getSelection() && !buttonSampleUniqueness.getSelection()
							&& !buttonReidentification.getSelection() && !buttonKMap.getSelection()
							&& !buttonKAnonymity.getSelection() && !buttonDifferential.getSelection()&&!sensitive) {
						labelKAnonymity.setEnabled(true);
						knobK.setEnabled(true);
						buttonKAnonymity.setSelection(true);
					}
				}
				meta.setKanonymityEnabled(buttonKAnonymity.getSelection());
				meta.setChanged(true);
			}
		});
		final Composite group = this.createGroup(5,parent);
		// Kaononymity

		// Create k slider
		Label kLabel = new Label(group, SWT.NONE);
		kLabel.setText(Resources.getMessage("CriterionDefinitionView.22")); //$NON-NLS-1$

		this.labelKAnonymity = new Text(group, SWT.BORDER | SWT.LEFT);
		GridData data = SWTUtil.createFillHorizontallyGridData(false);
		labelKAnonymity.setLayoutData(data);
		labelKAnonymity.setEditable(false);
		knobK = new Knob<Integer>(group, SWT.NULL, new KnobRange.Integer(0, 1000));
		knobK.setLayoutData(
				GridDataFactory.swtDefaults().grab(false, false).align(SWT.CENTER, SWT.CENTER).hint(30, 30).create());
		knobK.setDefaultColorProfile(defaultColorProfile);
		knobK.setFocusedColorProfile(focusedColorProfile);
		knobK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent arg0) {
				labelKAnonymity.setText(knobK.getValue() + "");
				meta.setKanonymity(knobK.getValue().intValue());
				meta.setChanged(true);
			}
		});
	}

	
	 
	/**
	 * Get The Data for this Project 
	 */
	public void getData() {
		this.buttonKAnonymity.setSelection(this.meta.isKanonymityEnabled());
		if (buttonKAnonymity.getSelection()) {
			labelKAnonymity.setEnabled(true);
			knobK.setEnabled(true);
		} else {
			labelKAnonymity.setEnabled(false);
			knobK.setEnabled(false);
		}
		this.knobK.setValue(Integer.valueOf(this.meta.getKanonymity()));
		this.labelKAnonymity.setText(this.meta.getKanonymity() + "");
		this.buttonDifferential.setSelection(this.meta.isDifferentialEnabled());
		if (buttonDifferential.getSelection()) {
			labelDifferentialEpsilon.setEnabled(true);
			labelDifferentialDelta.setEnabled(true);
			knobDifferentialEpsilon.setEnabled(true);
			knobDifferentialDelta.setEnabled(true);
			comboDifferentialGeneralization.setEnabled(true);
		} else {
			labelDifferentialEpsilon.setEnabled(false);
			labelDifferentialDelta.setEnabled(false);
			knobDifferentialEpsilon.setEnabled(false);
			knobDifferentialDelta.setEnabled(false);
			comboDifferentialGeneralization.setEnabled(false);
		}
		this.labelDifferentialDelta.setText(this.meta.getDifferentialDelta() + "");
		this.labelDifferentialEpsilon.setText(this.meta.getDifferentialEpsilon() + "");
		this.knobDifferentialDelta.setValue(this.meta.getDifferentialDelta());
		this.knobDifferentialEpsilon.setValue(this.meta.getDifferentialEpsilon());
		this.comboDifferentialGeneralization.select(this.meta.getDifferentialGeneralizationIndex());
		this.labelKMap.setText(meta.getKmap() + "");
		this.knobKMap.setValue(this.meta.getKmap());
		this.labelSignificanceLevelKMap.setText(this.meta.getKmapSignificanceLevel() + "");
		this.knobSignificanceLevelKMap.setValue(this.meta.getKmapSignificanceLevel());
		this.buttonKMap.setSelection(this.meta.isKmapEnabled());
		if (buttonKMap.getSelection()) {
			knobKMap.setEnabled(true);
			labelKMap.setEnabled(true);
			cmbModelKMap.setEnabled(true);
			if (cmbModelKMap.getSelectionIndex() != 0) {
				knobSignificanceLevelKMap.setEnabled(true);
				labelSignificanceLevelKMap.setEnabled(true);
			} else {
				knobSignificanceLevelKMap.setEnabled(false);
				labelSignificanceLevelKMap.setEnabled(false);
			}
		} else {
			knobKMap.setEnabled(false);
			labelKMap.setEnabled(false);
			cmbModelKMap.setEnabled(false);
			knobSignificanceLevelKMap.setEnabled(false);
			labelSignificanceLevelKMap.setEnabled(false);
		}
		this.buttonSampleUniqueness.setSelection(this.meta.isSampleUniquenessEnabled());
		this.labelSampleUniqueness.setText(this.meta.getSampleUniqueness() + "");
		this.knobSampleUniqueness.setValue(this.meta.getSampleUniqueness());
		if (this.buttonSampleUniqueness.getSelection()) {
			labelSampleUniqueness.setEnabled(true);
			knobSampleUniqueness.setEnabled(true);
		} else {
			labelSampleUniqueness.setEnabled(false);
			knobSampleUniqueness.setEnabled(false);
		}
		this.buttonReidentification.setSelection(this.meta.isReidentificationRiskEnabled());
		this.labelReidentification.setText(this.meta.getReidentificationRisk() + "");
		this.knobReidentification.setValue(this.meta.getReidentificationRisk());
		if (this.buttonReidentification.getSelection()) {
			labelReidentification.setEnabled(true);
			knobReidentification.setEnabled(true);
		} else {
			labelReidentification.setEnabled(false);
			knobReidentification.setEnabled(false);
		}
		this.buttonPopulationUniqueness.setSelection(this.meta.isPopulationUniquenessEnabled());
		this.labelPopulationUniqueness.setText(this.meta.getPopulationUniqueness() + "");
		this.knobPopulationUniqueness.setValue(this.meta.getPopulationUniqueness());
		this.comboPopulationUniqueness.select(this.meta.getPopulationUniquenessModel());
		if (this.buttonPopulationUniqueness.getSelection()) {
			labelPopulationUniqueness.setEnabled(true);
			knobPopulationUniqueness.setEnabled(true);
			this.comboPopulationUniqueness.setEnabled(true);
		} else {
			labelPopulationUniqueness.setEnabled(false);
			knobPopulationUniqueness.setEnabled(false);
			this.comboPopulationUniqueness.setEnabled(false);

		}
		this.sensitive=this.meta.isContainingSensitive(fieldNames);
	}

	public void saveData() {
		//TODO Delete
	}

	/**
	 * Creates the Label in the Beginning
	 * 
	 * @param message
	 *            The Message Text of the Label
	 * @param parent
	 *            The Parent Composite
	 */
	private void createLabel(final String message, final Composite parent, final Image image) {
		Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setImage(image);
		Label lbl1 = new Label(parent, SWT.NONE);
		lbl1.setText(message); // $NON-NLS-1$

	}

	/**
	 * Creates the Check Button for the Different Fields
	 * 
	 * @param parent
	 *            The Parent Composite
	 * @param listener
	 *            The SelectionListener for this Button
	 * @return
	 */
	private Button createButton(final Composite parent, final SelectionAdapter listener) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(Resources.getMessage("CriterionDefinitionView.56")); //$NON-NLS-1$
		button.setSelection(false);
		button.setEnabled(true);
		final GridData d82 = SWTUtil.createFillHorizontallyGridData();
		d82.horizontalSpan = 1;
		button.setLayoutData(d82);
		button.addSelectionListener(listener);
		return button;
	}
	
	/**
	 * Creates the Composite for the Inner Group
	 * @param numColum The Number of Columns in this Composite
	 * @param parent The Parent Composite for this Composite
	 * @return The Group Composite
	 */
	private Composite createGroup(int numColum,final Composite parent){
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayoutData(SWTUtil.createFillHorizontallyGridData());
		GridLayout groupInputGridLayout = new GridLayout();
		groupInputGridLayout.numColumns = numColum;
		group.setLayout(groupInputGridLayout);
		return group;
	}

}
