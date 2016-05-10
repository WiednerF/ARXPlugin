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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.kettle.attribut.ARXFields;
import org.deidentifier.arx.kettle.attribut.RegionStore;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/**
 * The class contains the configuration of the step for both, the Processing
 * and the Dialog. In this class also the loading from the configurationstorage
 * is written.</br>
 * You have the option to store either in a XML File or in a database, what 
 * you more like to do, you can do. 
 * @author Florian Wiedner
 * @category Storage
 * @version 1.0.0
 * @since 10.05.2016
 *
 */
/**
 * @author Florian
 *
 */
public class ARXPluginMeta extends BaseStepMeta implements StepMetaInterface {
	/**
	 * int kanonymity The K Value of the K-Anonymity Privacy Criterion
	 */
	private int kanonymity;
	/**
	 * @var boolean kanonymityEnabled Says if the K-Anonymity is enabled for
	 *      here or not
	 */
	private boolean kanonymityEnabled;// The Field for Kanonymity
	/**
	 * @var boolean differntialEnabled Says if the d,e-Differential Privacy is
	 *      enabled
	 */
	private boolean differentialEnabled;
	/**
	 * @var double differentialDelte The Delta Value of the Differential Privacy
	 *      Criterion
	 */
	private double differentialDelta;
	/**
	 * @var double differentialEpsilon The Epsilon Value of the Differential
	 *      Privacy Criterion
	 */
	private double differentialEpsilon;
	/**
	 * @var int differentialGeneralizationIndex The Index of the Value in the
	 *      Combobox of the Generalization for the<br>
	 *      Differential Privacy Criterion
	 */
	private int differentialGeneralizationIndex;
	/**
	 * @var boolean kmapEnabled if the K-Map is enabled as Privacy Criterion
	 */
	private boolean kmapEnabled;
	/**
	 * @var int kmap The K Value of the K-Map Privacy Criterion
	 */
	private int kmap;
	/**
	 * @var int kmapEstimatorIndex The Index of the Combobox for the Estimator
	 *      of the K-Map PrivacyCriterion
	 */
	private int kmapEstimatorIndex;
	/**
	 * @var double kmapSignifanceLevel The Signifance Level Value for the K-Map
	 */
	private double kmapSignificanceLevel;
	/**
	 * @var boolean reidentificationRiskEnabled If the Average Reidentification
	 *      Risk Privacy Criterion is enabled
	 */
	private boolean reidentificationRiskEnabled;
	/**
	 * @var double reidentificationRisk The Average Reidentification Risk
	 *      Privacy Criterion Threshold
	 */
	private double reidentificationRisk;
	/**
	 * @var boolean sampleUniquenessEnabled If the Sample-Uniqueness Privacy
	 *      Criterion is enabled
	 */
	private boolean sampleUniquenessEnabled;
	/**
	 * @var double sampleUniqueness The Threshold for the Sample Uniqueness
	 *      Privacy
	 */
	private double sampleUniqueness;
	/**
	 * @var boolean populationUniquenessEnabled If the Population-Uniqueness
	 *      Privacy Criterion is enabled
	 */
	private boolean populationUniquenessEnabled;
	/**
	 * @var double populationUniqueness The Threshold for the
	 *      PopulationUniqueness Privacy
	 */
	private double populationUniqueness;
	/**
	 * @var int populationUniquenessModel The Model Index of the Combobox for
	 *      the Population- Uniqueness Privacy Criterion
	 */
	private int populationUniquenessModel;
	/**
	 * @var double maxOutliers The Maximum Outliers in Prozent
	 */
	private double maxOutliers;
	/**
	 * @var boolean practicalMonotonicity If the Practical Monotonicity is set
	 *      or not
	 */
	private boolean practicalMonotonicity;
	/**
	 * @var boolean precomputed If some things are precomputed or not
	 */
	private boolean precomputed;
	/**
	 * @var double precomputedThreshold The Threshold for the precomputing in
	 *      prozent
	 */
	private double precomputedThreshold;
	/**
	 * @var boolean monotonicVariant If the Monotonic Variant is used or not
	 */
	private boolean monotonicVariant;
	/***
	 * @var String metric The Metric which is used for the computing. It is a
	 *      String Representation of the Metric stored
	 */
	private String metric;
	/***
	 * @var boolean microaggregation If we use Aggregation or Microaggregation
	 */
	private boolean microaggregation;
	/**
	 * @var String aggregation The Aggregation Function which is used as String
	 */
	private String aggregation;
	/**
	 * @var double GSFactor The Factor for Generalization vs. Suppression
	 */
	private double GSFactor;
	/**
	 * @var String region The String representation of the Region used as
	 *      Population
	 */
	private String region;
	/**
	 * @var HashMap<String,RegionStore> regions The Store of all the Regions and
	 *      their Values
	 */
	private HashMap<String, RegionStore> regions;
	/**
	 * @var HashMap<String,ARXFields> fields The Storage for all Fieldrelated
	 *      Stuff
	 */
	private HashMap<String, ARXFields> fields;
	/**
	 * @var boolean showRiskAnalysis If sow the riskAnalysis at the End or not
	 */
	private boolean showRiskAnalysis;

	/**
	 * Generates the new Meta and initializes the HashMaps
	 */
	public ARXPluginMeta() {
		super(); // allocate BaseStepInfo
		regions = new HashMap<String, RegionStore>();
		fields = new HashMap<String, ARXFields>();
	}

	/***
	 * Generates the XML Output for the Storage of this Step Configuration</br>
	 * <b>Only for internal Use from the Pentaho Project</b>*
	 * 
	 * @author Florian Wiedner
	 * @category Storage
	 * @return String The String of the XML Representation
	 * @throws KettleException
	 */
	public String getXML() throws KettleException {
		StringBuilder retval = new StringBuilder(800);
		// Appends all normal values
		retval.append("    ").append(XMLHandler.addTagValue("kanonymity", this.kanonymity + ""));
		retval.append("    ")
				.append(XMLHandler.addTagValue("kanonymityEnabled", (this.kanonymityEnabled ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("maxoutliers", this.maxOutliers + ""));
		retval.append("    ").append(
				XMLHandler.addTagValue("practicalMonotonicity", (this.practicalMonotonicity ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("precomputed", (this.precomputed ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("precomputedThreshold", this.precomputedThreshold));
		retval.append("    ")
				.append(XMLHandler.addTagValue("monotonicVariant", (this.monotonicVariant ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("metric", this.metric));
		retval.append("    ")
				.append(XMLHandler.addTagValue("microaggregation", (this.microaggregation ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("aggregation", this.aggregation));
		retval.append("    ").append(XMLHandler.addTagValue("GSFactor", this.GSFactor));
		retval.append("    ")
				.append(XMLHandler.addTagValue("differentialEnabled", (this.differentialEnabled ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("differentialDelta", this.differentialDelta));
		retval.append("    ").append(XMLHandler.addTagValue("differentialEpsilon", this.differentialEpsilon));
		retval.append("    ").append(
				XMLHandler.addTagValue("differentialGeneralizationIndex", this.differentialGeneralizationIndex));
		retval.append("    ").append(XMLHandler.addTagValue("kmapEnabled", (this.kmapEnabled ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("kmap", this.kmap));
		retval.append("    ").append(XMLHandler.addTagValue("kmapEstimatorIndex", this.kmapEstimatorIndex));
		retval.append("    ").append(XMLHandler.addTagValue("kmapSignificanceLevel", this.kmapSignificanceLevel));
		retval.append("    ").append(XMLHandler.addTagValue("region", this.region));
		retval.append("    ").append(XMLHandler.addTagValue("reidentificationRisk", this.reidentificationRisk));
		retval.append("    ").append(XMLHandler.addTagValue("reidentificationRiskEnabled",
				(this.reidentificationRiskEnabled ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("sampleUniqueness", this.sampleUniqueness));
		retval.append("    ").append(
				XMLHandler.addTagValue("sampleUniquenessEnabled", (this.sampleUniquenessEnabled ? "true" : "false")));
		retval.append("    ").append(XMLHandler.addTagValue("populationUniqueness", this.populationUniqueness));
		retval.append("    ").append(XMLHandler.addTagValue("populationUniquenessEnabled",
				(this.populationUniquenessEnabled ? "true" : "false")));
		retval.append("    ")
				.append(XMLHandler.addTagValue("populationUniquenessModel", this.populationUniquenessModel));
		retval.append("    ")
				.append(XMLHandler.addTagValue("showRiskAnalysis", (this.showRiskAnalysis ? "true" : "false")));

		// Appends the Regions
		Set<String> tempRegions = regions.keySet();
		retval.append("    <regions>" + Const.CR);
		for (String temp : tempRegions) {
			RegionStore regions = this.regions.get(temp);
			retval.append(regions.getXML());
		}
		retval.append("      </regions>" + Const.CR);

		// Appends the fields
		Set<String> tempFields = this.fields.keySet();
		retval.append("    <fields>" + Const.CR);
		for (String temp : tempFields) {
			ARXFields field = this.fields.get(temp);
			retval.append(field.getXML());
		}
		retval.append("      </fields>" + Const.CR);

		return retval.toString();
	}

	/***
	 * This Method overwrittes the fields and makes all field outputs as String
	 * <br>
	 * <i>The Params are defined from the parent class, but not all
	 * used</i></br>
	 * <b>Only for internal Use from the Pentaho Project</b> {@inheritDoc}
	 * 
	 * @author Florian Wiedner
	 * @category Storage
	 * @since 1.5
	 * @param r
	 *            RowMetaInterface The RowMetaInterface generated for this step
	 * @param origin
	 *            String The origin (Not used)
	 * @param info
	 *            RowMetaInterface[] The MetaInterfaces of the previous Steps
	 * @param nextStep
	 *            StepMeta (Not used)
	 * @param space
	 *            VariableSpace (not used)
	 * @see org.pentaho.di.trans.step.BaseStepMeta#getFields()
	 */
	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep,
			VariableSpace space) {
		String[] fieldNames = r.getFieldNames();
		for (int i = 0; i < fieldNames.length; i++) {
			ValueMetaInterface temp = r.getValueMeta(i);
			temp.getLength();
			ValueMeta temp2 = new ValueMeta(fieldNames[i], ValueMeta.getType("String"), temp.getLength(),
					temp.getPrecision());
			try {
				r.removeValueMeta(fieldNames[i]);
				r.addValueMeta(i, temp2);
			} catch (KettleValueException e) {
				logError("Unexpected error : " + e.toString());
				logError(Const.getStackTracker(e));
			}

		}
	}

	/**
	 * (No Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	/**
	 * This Method get the Variables out of the XML Storage Format and get them
	 * back into the Method <i>The Params are defined from the parent class, but
	 * not all used</i></br>
	 * <b>Only for internal Use from the Pentaho Project</b> {@inheritDoc}
	 * 
	 * @author Florian Wiedner
	 * @category Storage
	 * @since 1.5
	 * @param stepnode
	 *            The XML Node for this step
	 * @param databases
	 *            (Not used)
	 * @param counters
	 *            (Not used)
	 * @see org.pentaho.di.trans.step.BaseStepMeta#loadXML()
	 * @throws KettleXMLException
	 */
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleXMLException {
		this.setDefault();// Sets the Defaults to all Variables
		try {
			// Gets the Normal Parameters
			if (XMLHandler.getTagValue(stepnode, "kanonymity") != null) {
				this.kanonymity = Integer.parseInt(XMLHandler.getTagValue(stepnode, "kanonymity"));
			}
			if (XMLHandler.getTagValue(stepnode, "kanonymityEnabled") != null) {
				this.kanonymityEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "kanonymityEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "maxoutliers") != null) {
				this.maxOutliers = Double.parseDouble(XMLHandler.getTagValue(stepnode, "maxoutliers"));
			}
			if (XMLHandler.getTagValue(stepnode, "practicalMonotonicity") != null) {
				this.practicalMonotonicity = Boolean
						.parseBoolean(XMLHandler.getTagValue(stepnode, "practicalMonotonicity"));
			}
			if (XMLHandler.getTagValue(stepnode, "precomputed") != null) {
				this.precomputed = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "precomputed"));
			}
			if (XMLHandler.getTagValue(stepnode, "precomputedThreshold") != null) {
				this.precomputedThreshold = Double
						.parseDouble(XMLHandler.getTagValue(stepnode, "precomputedThreshold"));
			}
			if (XMLHandler.getTagValue(stepnode, "monotonicVariant") != null) {
				this.monotonicVariant = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "monotonicVariant"));
			}
			if (XMLHandler.getTagValue(stepnode, "metric") != null) {
				this.metric = XMLHandler.getTagValue(stepnode, "metric");
			}
			if (XMLHandler.getTagValue(stepnode, "aggregation") != null) {
				this.aggregation = XMLHandler.getTagValue(stepnode, "aggregation");
			}
			if (XMLHandler.getTagValue(stepnode, "microaggregation") != null) {
				this.microaggregation = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "microaggregation"));
			}
			if (XMLHandler.getTagValue(stepnode, "GSFactor") != null) {
				this.GSFactor = Double.parseDouble(XMLHandler.getTagValue(stepnode, "GSFactor"));
			}
			if (XMLHandler.getTagValue(stepnode, "region") != null) {
				this.region = XMLHandler.getTagValue(stepnode, "region");
			}
			if (XMLHandler.getTagValue(stepnode, "differentialEpsilon") != null) {
				this.differentialEpsilon = Double.parseDouble(XMLHandler.getTagValue(stepnode, "differentialEpsilon"));
			}
			if (XMLHandler.getTagValue(stepnode, "differentialDelta") != null) {
				this.differentialDelta = Double.parseDouble(XMLHandler.getTagValue(stepnode, "differentialDelta"));
			}
			if (XMLHandler.getTagValue(stepnode, "differentialEnabled") != null) {
				this.differentialEnabled = Boolean
						.parseBoolean(XMLHandler.getTagValue(stepnode, "differentialEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "differentialGeneralizationIndex") != null) {
				this.differentialGeneralizationIndex = Integer
						.parseInt(XMLHandler.getTagValue(stepnode, "differentialGeneralizationIndex"));
			}
			if (XMLHandler.getTagValue(stepnode, "kmapEnabled") != null) {
				this.kmapEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "kmapEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "kmap") != null) {
				this.kmap = Integer.parseInt(XMLHandler.getTagValue(stepnode, "kmap"));
			}
			if (XMLHandler.getTagValue(stepnode, "kmapEstimatorIndex") != null) {
				this.kmapEstimatorIndex = Integer.parseInt(XMLHandler.getTagValue(stepnode, "kmapEstimatorIndex"));
			}
			if (XMLHandler.getTagValue(stepnode, "kmapSignificanceLevel") != null) {
				this.kmapSignificanceLevel = Double
						.parseDouble(XMLHandler.getTagValue(stepnode, "kmapSignificanceLevel"));
			}
			if (XMLHandler.getTagValue(stepnode, "reidentificationRisk") != null) {
				this.reidentificationRisk = Double
						.parseDouble(XMLHandler.getTagValue(stepnode, "reidentificationRisk"));
			}
			if (XMLHandler.getTagValue(stepnode, "reidentificationRiskEnabled") != null) {
				this.reidentificationRiskEnabled = Boolean
						.parseBoolean(XMLHandler.getTagValue(stepnode, "reidentificationRiskEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "sampleUniqueness") != null) {
				this.sampleUniqueness = Double.parseDouble(XMLHandler.getTagValue(stepnode, "sampleUniqueness"));
			}
			if (XMLHandler.getTagValue(stepnode, "sampleUniquenessEnabled") != null) {
				this.sampleUniquenessEnabled = Boolean
						.parseBoolean(XMLHandler.getTagValue(stepnode, "sampleUniquenessEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "populationUniqueness") != null) {
				this.populationUniqueness = Double
						.parseDouble(XMLHandler.getTagValue(stepnode, "populationUniqueness"));
			}
			if (XMLHandler.getTagValue(stepnode, "populationUniquenessEnabled") != null) {
				this.populationUniquenessEnabled = Boolean
						.parseBoolean(XMLHandler.getTagValue(stepnode, "populationUniquenessEnabled"));
			}
			if (XMLHandler.getTagValue(stepnode, "populationUniquenessModel") != null) {
				this.populationUniquenessModel = Integer
						.parseInt(XMLHandler.getTagValue(stepnode, "populationUniquenessModel"));
			}
			if (XMLHandler.getTagValue(stepnode, "showRiskAnalysis") != null) {
				this.showRiskAnalysis = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "showRiskAnalysis"));
			}

			// Get the Parameter for the Regions
			Node regionNode = XMLHandler.getSubNode(stepnode, "regions");
			if (regionNode != null) {
				int nrRegions = XMLHandler.countNodes(regionNode, "region");

				for (int i = 0; i < nrRegions; i++) {
					Node tempRegion = XMLHandler.getSubNodeByNr(regionNode, "region", i);
					RegionStore temp = new RegionStore(tempRegion);
					this.regions.put(temp.getName(), temp);
				}
			}

			// Gets the Values for the Fields
			Node fieldsNode = XMLHandler.getSubNode(stepnode, "fields");
			if (fieldsNode != null) {
				int nrFields = XMLHandler.countNodes(fieldsNode, "field");

				for (int i = 0; i < nrFields; i++) {
					Node tempField = XMLHandler.getSubNodeByNr(fieldsNode, "field", i);
					ARXFields temp = new ARXFields(tempField);
					this.fields.put(temp.getName(), temp);
				}
			}
		} catch (Exception e) {
			throw new KettleXMLException("Unable to load step info from XML", e);
		}
	}

	/**
	 * Sets the Default Values to all Parameters, which have a Default
	 * Value</br>
	 * The Values are cloned from the ARX GUI Project</br>
	 * <b>Only for internal Use from the Pentaho Project and this class</b>
	 * {@inheritDoc}
	 * 
	 * @author Florian Wiedner
	 * @since 1.5
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 */
	public void setDefault() {
		this.kanonymity = 5;
		this.maxOutliers = 0d;
		this.practicalMonotonicity = false;
		this.precomputed = false;
		this.precomputedThreshold = 1.0;
		this.metric = "Loss";
		this.monotonicVariant = false;
		this.microaggregation = false;
		this.aggregation = "Geometric Mean";
		this.GSFactor = 0.5;
		this.region = "Europe";
		this.kanonymityEnabled = true;
		this.differentialEnabled = false;
		this.differentialDelta = 1.1e-6d;
		this.differentialEpsilon = 2;
		this.differentialGeneralizationIndex = 4;
		this.kmapEnabled = false;
		this.kmap = 5;
		this.kmapEstimatorIndex = 0;
		this.kmapSignificanceLevel = 0.01d;
		this.reidentificationRiskEnabled = false;
		this.reidentificationRisk = 0.01d;
		this.sampleUniqueness = 0.1d;
		this.sampleUniquenessEnabled = false;
		this.populationUniqueness = 0.1d;
		this.populationUniquenessEnabled = false;
		this.populationUniquenessModel = 0;
		this.showRiskAnalysis = true;
	}

	/**
	 * This functions reads the Repositor Database Storage Version</br>
	 * <i>The Params are defined from the parent class, but not all
	 * used</i></br>
	 * <b>Only for internal Use from the Pentaho Project</b> {@inheritDoc}
	 * 
	 * @author Florian Wiedner
	 * @category Storage
	 * @since 1.5
	 * @param rep
	 *            The repository Object for the Database
	 * @param id_step
	 *            The Object ID for the Step, for getting the correct
	 *            Informations
	 * @param databases
	 *            (not used)
	 * @param counters
	 *            (Not used)
	 * @throws KettleException
	 * @see org.pentaho.di.trans.step.StepMetaInterface#readRep(org.pentaho.di.repository.Repository,
	 *      org.pentaho.di.repository.ObjectId, java.util.List, java.util.Map)
	 */
	public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		this.setDefault();// Sets default
		try {
			// Store all Variables into
			this.kanonymity = Integer.parseInt(rep.getStepAttributeString(id_step, "kanonymity"));
			this.kanonymityEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "kanonymityEnabled"));
			this.maxOutliers = Double.parseDouble(rep.getStepAttributeString(id_step, "maxoutliers"));
			this.practicalMonotonicity = Boolean
					.parseBoolean(rep.getStepAttributeString(id_step, "practicalMonotonicity"));
			this.precomputed = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "precomputed"));
			this.precomputedThreshold = Double.parseDouble(rep.getStepAttributeString(id_step, "precomputedThreshold"));
			this.monotonicVariant = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "monotonicVariant"));
			this.microaggregation = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "microaggregation"));
			this.metric = rep.getStepAttributeString(id_step, "metric");
			this.aggregation = rep.getStepAttributeString(id_step, "aggregation");
			this.region = rep.getStepAttributeString(id_step, "region");
			this.GSFactor = Double.parseDouble(rep.getStepAttributeString(id_step, "GSFactor"));
			this.differentialEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "differentialEnabled"));
			this.differentialDelta = Double.parseDouble(rep.getStepAttributeString(id_step, "differentialDelta"));
			this.differentialEpsilon = Double.parseDouble(rep.getStepAttributeString(id_step, "differentialEpsilon"));
			this.differentialGeneralizationIndex = Integer
					.parseInt(rep.getStepAttributeString(id_step, "differentialGeneralizationIndex"));
			this.kmapEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "kmapEnabled"));
			this.kmap = Integer.parseInt(rep.getStepAttributeString(id_step, "kmap"));
			this.kmapEstimatorIndex = Integer.parseInt(rep.getStepAttributeString(id_step, "kmapEstimatorIndex"));
			this.kmapSignificanceLevel = Double
					.parseDouble(rep.getStepAttributeString(id_step, "kmapSignificanceLevel"));
			this.sampleUniqueness = Double.parseDouble(rep.getStepAttributeString(id_step, "sampleUniqueness"));
			this.sampleUniquenessEnabled = Boolean
					.parseBoolean(rep.getStepAttributeString(id_step, "sampleUniquenessEnabled"));
			this.populationUniqueness = Double.parseDouble(rep.getStepAttributeString(id_step, "populationUniqueness"));
			this.populationUniquenessEnabled = Boolean
					.parseBoolean(rep.getStepAttributeString(id_step, "populationUniquenessEnabled"));
			this.reidentificationRisk = Double.parseDouble(rep.getStepAttributeString(id_step, "reidentificationRisk"));
			this.reidentificationRiskEnabled = Boolean
					.parseBoolean(rep.getStepAttributeString(id_step, "reidentificationRiskEnabled"));
			this.populationUniquenessModel = Integer
					.parseInt(rep.getStepAttributeString(id_step, "populationUniquenessModel"));
			this.showRiskAnalysis = Boolean.parseBoolean(rep.getStepAttributeString(id_step, "showRiskAnalysis"));

			// Stores the infos for the region
			int nrRegions = rep.countNrStepAttributes(id_step, "regionsName");

			for (int i = 0; i < nrRegions; i++) {
				RegionStore temp = new RegionStore("", 0.0, 0l);
				temp.setName(rep.getStepAttributeString(id_step, i, "regionsName"));
				temp.setSampling(Double.parseDouble(rep.getStepAttributeString(id_step, i, "regionsSampling")));
				temp.setPopulation(Long.parseLong(rep.getStepAttributeString(id_step, i, "regionsPopulation")));
				this.regions.put(temp.getName(), temp);
			}

			// Stores the Infos for the field
			int nrFields = rep.countNrStepAttributes(id_step, "fieldsName");

			for (int i = 0; i < nrFields; i++) {
				ARXFields temp = new ARXFields(rep.getStepAttributeString(id_step, i, "fieldsName"));
				temp.setType(rep.getStepAttributeString(id_step, i, "fieldsType"));
				temp.setHierarchie(rep.getStepAttributeString(id_step, i, "fieldsHierarchie"));
				temp.setFunctionMicro(rep.getStepAttributeString(id_step, i, "fieldsFunctionMicro"));
				temp.setAttributeWeight(
						Double.parseDouble(rep.getStepAttributeString(id_step, i, "fieldsAttributeWeight")));
				temp.setlDiversityC(Double.parseDouble(rep.getStepAttributeString(id_step, i, "fieldsLDiversityC")));
				temp.settCloseness(Double.parseDouble(rep.getStepAttributeString(id_step, i, "fieldsTCloseness")));
				temp.setMissingDataMicro(
						Boolean.parseBoolean(rep.getStepAttributeString(id_step, i, "fieldsMissingDataMicro")));
				temp.setlDiversityEnable(
						Boolean.parseBoolean(rep.getStepAttributeString(id_step, i, "fieldsLDiversityEnable")));
				temp.settClosenessEnable(
						Boolean.parseBoolean(rep.getStepAttributeString(id_step, i, "fieldsTClosenessEnable")));
				temp.setdDisclosureEnable(
						Boolean.parseBoolean(rep.getStepAttributeString(id_step, i, "fieldsDDisclosureEnable")));
				temp.setTransformation(
						Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsTransformation")));
				temp.setMinimumGen(Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsMinimumGen")));
				temp.setMaximumGen(Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsMaximumGen")));
				temp.setlDiversity(Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsLDiversity")));
				temp.setlDiversityVariant(
						Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsLDiversityVariant")));
				temp.settClosenessMeasure(
						Integer.parseInt(rep.getStepAttributeString(id_step, i, "fieldsTClosenessMeasure")));
				temp.setdDisclosure(Double.parseDouble(rep.getStepAttributeString(id_step, i, "fieldsDDisclosure")));

				this.fields.put(temp.getName(), temp);
			}

		} catch (Exception e) {
			throw new KettleException("Unexpected error reading step information from the repository", e);
		}

	}

	/**
	 * This Method saves our Configuration in the Database way of Storage</br>
	 * <b>Only for internal Use from the Pentaho Project</b> {@inheritDoc}
	 * 
	 * @author Florian Wiedner
	 * @category Storage
	 * @since 1.5
	 * @throws KettleException
	 * @param rep
	 *            The Repository in the Database
	 * @param id_transformation
	 *            The Object ID of our Transformation
	 * @param id_step
	 *            The Object ID for our Step in the Transformation
	 * @see org.pentaho.di.trans.step.StepMetaInterface#saveRep(org.pentaho.di.repository.Repository,
	 *      org.pentaho.di.repository.ObjectId,
	 *      org.pentaho.di.repository.ObjectId)
	 */
	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException {
		try {
			// All Normal Parameters
			rep.saveStepAttribute(id_transformation, id_step, "kanonymity", this.kanonymity);
			rep.saveStepAttribute(id_transformation, id_step, "kanonymityEnabled",
					(this.kanonymityEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "maxoutliers", this.maxOutliers);
			rep.saveStepAttribute(id_transformation, id_step, "practicalMonotonicity",
					(this.practicalMonotonicity ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "precomputed", (this.precomputed ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "monotonicVariant",
					(this.monotonicVariant ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "microaggregation",
					(this.microaggregation ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "metric", this.metric);
			rep.saveStepAttribute(id_transformation, id_step, "aggregation", this.aggregation);
			rep.saveStepAttribute(id_transformation, id_step, "region", this.region);
			rep.saveStepAttribute(id_transformation, id_step, "precomputedThreshold", this.precomputedThreshold);
			rep.saveStepAttribute(id_transformation, id_step, "GSFactor", this.GSFactor);
			rep.saveStepAttribute(id_transformation, id_step, "differentialEnabled",
					(this.differentialEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "differentialDelta", this.differentialDelta);
			rep.saveStepAttribute(id_transformation, id_step, "differentialEpsilon", this.differentialEpsilon);
			rep.saveStepAttribute(id_transformation, id_step, "differentialGeneralizationIndex",
					this.differentialGeneralizationIndex);
			rep.saveStepAttribute(id_transformation, id_step, "kmapEnabled", (this.kmapEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "kmap", this.kmap);
			rep.saveStepAttribute(id_transformation, id_step, "kmapEstimatorIndex", this.kmapEstimatorIndex);
			rep.saveStepAttribute(id_transformation, id_step, "kmapSignificanceLevel", this.kmapSignificanceLevel);
			rep.saveStepAttribute(id_transformation, id_step, "reidentificationRiskEnabled",
					(this.reidentificationRiskEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "reidentificationRisk", this.reidentificationRisk);
			rep.saveStepAttribute(id_transformation, id_step, "sampleUniquenessEnabled",
					(this.sampleUniquenessEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "sampleUniqueness", this.sampleUniqueness);
			rep.saveStepAttribute(id_transformation, id_step, "populationUniquenessEnabled",
					(this.populationUniquenessEnabled ? "true" : "false"));
			rep.saveStepAttribute(id_transformation, id_step, "populationUniqueness", this.populationUniqueness);
			rep.saveStepAttribute(id_transformation, id_step, "populationUniquenessModel",
					this.populationUniquenessModel);
			rep.saveStepAttribute(id_transformation, id_step, "showRiskAnalysis",
					(this.showRiskAnalysis ? "true" : "false"));

			// Get our Regions
			Set<String> tempRegions = regions.keySet();
			int i = 0;
			for (String regionNames : tempRegions) {
				if (this.regions.get(regionNames) != null) {
					RegionStore temp = this.regions.get(regionNames);
					rep.saveStepAttribute(id_transformation, id_step, i, "regionsName", temp.getName());
					rep.saveStepAttribute(id_transformation, id_step, i, "regionsSampling", temp.getSampling());
					rep.saveStepAttribute(id_transformation, id_step, i, "regionsPopulation", temp.getPopulation());
					i++;
				}
			}

			// Get the fields
			Set<String> tempFields = fields.keySet();
			int x = 0;
			for (String fieldsNames : tempFields) {
				if (this.fields.get(fieldsNames) != null) {
					ARXFields temp = this.fields.get(fieldsNames);
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsName", temp.getName());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsTransformation",
							temp.getTransformation());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsMinimumGen", temp.getMinimumGen());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsMaximumGen", temp.getMaximumGen());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsFunctionMicro",
							temp.getFunctionMicro());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsHierarchie", temp.getHierarchie());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsAttributeWeight",
							temp.getAttributeWeight());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsLDiversity", temp.getlDiversity());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsLDiversityVariant",
							temp.getlDiversityVariant());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsLDiversityC", temp.getlDiversityC());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsTCloseness", temp.gettCloseness());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsTClosenessMeasure",
							temp.gettClosenessMeasure());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsDDisclosure", temp.getdDisclosure());
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsMissingDataMicro",
							(temp.isMissingDataMicro() ? "true" : "false"));
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsLDiversityEnable",
							(temp.islDiversityEnable() ? "true" : "false"));
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsTClosenessEnable",
							(temp.istClosenessEnable() ? "true" : "false"));
					rep.saveStepAttribute(id_transformation, id_step, x, "fieldsDDisclosureEnable",
							(temp.isdDisclosureEnable() ? "true" : "false"));

					i++;
				}
			}
		} catch (Exception e) {
			throw new KettleException("Unable to save step information to the repository for id_step=" + id_step, e);
		}
	}

	/**
	 * Precomputed Selection is enabled or not
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @return Boolean
	 */
	public boolean isPrecomputed() {
		return precomputed;
	}

	/**
	 * Setting of the Precomputed Enabling
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @param precomputed The Precomputing Enabling for the General Settings
	 */
	public void setPrecomputed(boolean precomputed) {
		this.precomputed = precomputed;
	}

	/**
	 * 
	 * @since 1.1
	 * @author Florian Wiedner
	 * @category Parameters
	 * @return The Precomputing Threshold from the General Configuration for the Anonymazation
	 */
	public double getPrecomputedThreshold() {
		return precomputedThreshold;
	}

	/**
	 * 
	 * @since 1.1
	 * @author Florian Wiedner
	 * @category Parameters
	 * @param precomputedThreshold The Precomputing Threshold for the Anonymazation
	 */
	public void setPrecomputedThreshold(double precomputedThreshold) {
		this.precomputedThreshold = precomputedThreshold;
	}

	/**
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.StepMetaInterface#check(java.util.List, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.row.RowMetaInterface, java.lang.String[], java.lang.String[], org.pentaho.di.core.row.RowMetaInterface)
	 */
	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev,
			String input[], String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!",
					stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					"Step is connected to previous one, receiving " + prev.size() + " fields", stepMeta);
			remarks.add(cr);
		}

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}
	}

	/**
	 * 
	 * @since 1.5
	 * @category ARX
	 * @param shell The used shell as Parent
	 * @param meta The Meta Step
	 * @param transMeta The TransMetastep for the Previous Step
	 * @param name The StepName
	 * @return The StepDialogInterface Implementation for this Plugin
	 */
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new ARXPluginDialog(shell, meta, transMeta, name);
	}

	/**
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.StepMetaInterface#getStep(org.pentaho.di.trans.step.StepMeta, org.pentaho.di.trans.step.StepDataInterface, int, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.Trans)
	 */
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new ARXPlugin(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	/**
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.StepMetaInterface#getStepData()
	 */
	public StepDataInterface getStepData() {
		return new ARXPluginData();
	}

	/**
	 * 
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @return The K Value of the K-Anonymity Privacy Criterion
	  * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a> 
	 */
	public int getKanonymity() {
		return kanonymity;
	}

	/**
	 * 
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @param kanonymity The K Value for the K-Anonymity Privacy Criterion
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 */
	public void setKanonymity(int kanonymity) {
		this.kanonymity = kanonymity;
	}

	/**
	 * 
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Maximum Outliers allowed in prozents
	 */
	public double getMaxOutliers() {
		return maxOutliers;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param maxOutliers The Maximum Outliers allowed in prozent
	 */
	public void setMaxOutliers(double maxOutliers) {
		this.maxOutliers = maxOutliers;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Use Practical Monotonicity or not
	 */
	public boolean isPracticalMonotonicity() {
		return practicalMonotonicity;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param practicalMonotonicity Setting the Practical Monotonicity or set it to false
	 */
	public void setPracticalMonotonicity(boolean practicalMonotonicity) {
		this.practicalMonotonicity = practicalMonotonicity;
	}
	
	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using of Monotonic Variant for the Anonymization
	 */
	public boolean isMonotonicVariant() {
		return monotonicVariant;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param monotonicVariant Using of the Monotonic Variant for the Anonymization
	 */
	public void setMonotonicVariant(boolean monotonicVariant) {
		this.monotonicVariant = monotonicVariant;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Metric String Representation
	 */
	public String getMetric() {
		return metric;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param metric Setting the String Metric Representation from the ARX Project
	 */
	public void setMetric(String metric) {
		this.metric = metric;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Use Microaggregation in the Anonymization
	 */
	public boolean isMicroaggregation() {
		return microaggregation;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param microaggregation Use Microaggregation in our Anonymization
	 */
	public void setMicroaggregation(boolean microaggregation) {
		this.microaggregation = microaggregation;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The String Representation of the Used Aggregation Function
	 */
	public String getAggregation() {
		return aggregation;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param aggregation The String representation of one Aggregation Function
	 */
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Generalzation-Suppression Factor
	 */
	public double getGSFactor() {
		return GSFactor;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param gSFactor The Generalization-Suppression Factor
	 */
	public void setGSFactor(double gSFactor) {
		GSFactor = gSFactor;
	}

	/**
	 * 
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param region The String Name of the Region we search for
	 * @return The Stored RegionStore or a new RegionStore with default Values for this Region
	 */
	public RegionStore getRegions(String region) {
		if (this.regions.containsKey(region)) {
			return this.regions.get(region);
		} else {
			Region regionName = null;
			for (Region regionOptimal : Region.values()) {
				if (this.region.equals(regionOptimal.getName())) {
					regionName = regionOptimal;
					break;
				}
			}
			return new RegionStore("region", (0.1d), (regionName != null ? regionName.getPopulationSize() : 0));
		}
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param name The Nam of the Region
	 * @param sampling The Sampling Fraction for this region
	 * @param population The Population in this region
	 */
	public void setRegion(String name, double sampling, long population) {
		if (this.regions.containsKey(name)) {
			RegionStore temp = this.regions.get(name);
			temp.setSampling(sampling);
			temp.setPopulation(population);
		} else {
			this.regions.put(name, new RegionStore(name, sampling, population));
		}
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Region Name which is selected for the Population in the Anonymization
	 */
	public String getRegion() {
		return region;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param region The Region Name which you want to select for the Population Model
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using of K-Anonymity Privacy Criterion
	 */
	public boolean isKanonymityEnabled() {
		return kanonymityEnabled;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param kanonymityEnabled Enable K-Anonymity Privacy
	 */
	public void setKanonymityEnabled(boolean kanonymityEnabled) {
		this.kanonymityEnabled = kanonymityEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using of the Differntial Privacy Criterion
	 */
	public boolean isDifferentialEnabled() {
		return differentialEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param differentialEnabled Enabling od Risabling of the Differential Privacy Criterion
	 */
	public void setDifferentialEnabled(boolean differentialEnabled) {
		this.differentialEnabled = differentialEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Delta Value of the Differntial Privacy Criterion
	 */
	public double getDifferentialDelta() {
		return differentialDelta;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param differentialDelta Setting the Delta Value of the Differential Privacy Criterion
	 */
	public void setDifferentialDelta(double differentialDelta) {
		this.differentialDelta = differentialDelta;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Epsilon Value of the Differential Privacy
	 */
	public double getDifferentialEpsilon() {
		return differentialEpsilon;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param differentialEpsilon Setting the Epsilon Value of the Differential Privacy
	 */
	public void setDifferentialEpsilon(double differentialEpsilon) {
		this.differentialEpsilon = differentialEpsilon;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Index of the ComboBox for the Generalization of the Differential Privacy
	 */
	public int getDifferentialGeneralizationIndex() {
		return differentialGeneralizationIndex;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param differentialGeneralizationIndex The Index of the ComboBox for the Generalization of the Differential Privacy
	 */
	public void setDifferentialGeneralizationIndex(int differentialGeneralizationIndex) {
		this.differentialGeneralizationIndex = differentialGeneralizationIndex;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a> 
	 * @return Using K-Map Privacy Criterion
	 */
	public boolean isKmapEnabled() {
		return kmapEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param kmapEnabled Enable or disable the K-Map Privacy
	 */
	public void setKmapEnabled(boolean kmapEnabled) {
		this.kmapEnabled = kmapEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The K Value of the K-Map Privacy Criterion
	 */
	public int getKmap() {
		return kmap;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param kmap The K Value of the K-Map Privacy Criterion
	 */
	public void setKmap(int kmap) {
		this.kmap = kmap;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Index of the ComboBox for the Estimator in the K-Map
	 */
	public int getKmapEstimatorIndex() {
		return kmapEstimatorIndex;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param kmapEstimatorIndex The Index of the ComboBox for the Estimator of the K-Map
	 */
	public void setKmapEstimatorIndex(int kmapEstimatorIndex) {
		this.kmapEstimatorIndex = kmapEstimatorIndex;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a> 
	 * @return The Signifance Level for the K-Map Privacy
	 */
	public double getKmapSignificanceLevel() {
		return kmapSignificanceLevel;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param kmapSignificanceLevel Set the Significance Level of K-Map Privacy Criterion
	 */
	public void setKmapSignificanceLevel(double kmapSignificanceLevel) {
		this.kmapSignificanceLevel = kmapSignificanceLevel;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using of the Average Reidentification Risk Privacy Criterion
	 */
	public boolean isReidentificationRiskEnabled() {
		return reidentificationRiskEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param reidentificationRiskEnabled Enable or Disable the Average Reidentification Risk Privacy Criterion
	 */
	public void setReidentificationRiskEnabled(boolean reidentificationRiskEnabled) {
		this.reidentificationRiskEnabled = reidentificationRiskEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Average Reidentification Risk Threshold
	 */
	public double getReidentificationRisk() {
		return reidentificationRisk;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param reidentificationRisk The Average Reidentification Risk Threshold
	 */
	public void setReidentificationRisk(double reidentificationRisk) {
		this.reidentificationRisk = reidentificationRisk;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using the Sample Uniqueness Privacy Criterion
	 */
	public boolean isSampleUniquenessEnabled() {
		return sampleUniquenessEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param sampleUniquenessEnabled Enable or Disable the Sample-Uniqueness Privacy Criterion
	 */
	public void setSampleUniquenessEnabled(boolean sampleUniquenessEnabled) {
		this.sampleUniquenessEnabled = sampleUniquenessEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Sample-Uniqueness Privacy Threshold
	 */
	public double getSampleUniqueness() {
		return sampleUniqueness;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param sampleUniqueness The Sample-Uniqueness Privacy Threshold
	 */
	public void setSampleUniqueness(double sampleUniqueness) {
		this.sampleUniqueness = sampleUniqueness;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using the Population-Uniqueness Privacy Criterion
	 */
	public boolean isPopulationUniquenessEnabled() {
		return populationUniquenessEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param populationUniquenessEnabled Enable or Disable the Population-Uniqueness Privacy Criterion
	 */
	public void setPopulationUniquenessEnabled(boolean populationUniquenessEnabled) {
		this.populationUniquenessEnabled = populationUniquenessEnabled;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return  The Population Uniqueness Privacy Criterion Threshold
	 */
	public double getPopulationUniqueness() {
		return populationUniqueness;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param populationUniqueness The Population-Uniqueness Privacy Criterion Threshold
	 */
	public void setPopulationUniqueness(double populationUniqueness) {
		this.populationUniqueness = populationUniqueness;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Index of the ComboBox list of Models for the Population-Uniqueness Privacy
	 */
	public int getPopulationUniquenessModel() {
		return populationUniquenessModel;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param populationUniquenessModel The Index of the ComboBox list of Models for the Population-Uniqueness Privacy
	 */
	public void setPopulationUniquenessModel(int populationUniquenessModel) {
		this.populationUniquenessModel = populationUniquenessModel;
	}

	/**
	 * This method returns the stored field with the name or creates a new
	 * Object with the default values for fields
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param name The Fieldname for the HashIndex
	 * @return The ARXField Object for this field name
	 */
	public ARXFields getField(String name) {
		ARXFields temp=this.fields.get(name);
		if (temp == null) {
			temp = new ARXFields(name);
			this.fields.put(name, temp);
		}
		return temp;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param field The field Object with the actual parameters
	 */
	public void setField(ARXFields field) {
		this.fields.put(field.getName(), field);
	}

	/**
	 * The QuasiIdentifierFields out of the actual list of fieldNames
	 * @param fieldNames The Array of all FieldNames which are actual in use
	 * @return The FieldName list of all the fields which have the Type Quasi-Identifiers
	 */
	public String[] getQuasiIdentifierFields(String[] fieldNames) {
		if (fieldNames != null) {
			ArrayList<String> list = new ArrayList<String>();
			for (String field:fieldNames) {
				ARXFields temp = this.fields.get(field);
				if (temp != null && temp.getType().equals("Quasi-identifying")) {
					list.add(field);
				}else if(temp==null){
					list.add(field);
				}
			}
			return list.toArray(new String[list.size()]);
		}
		return new String[0];
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Showing the Risk Analysis at the End of this step or not
	 */
	public boolean isShowRiskAnalysis() {
		return showRiskAnalysis;
	}

	/**
	  * @author Florian Wiedner
	 * @since 1.1
	 * @category Parameters
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param showRiskAnalysis Showing the Risk Analysis at the End of this step or not
	 */
	public void setShowRiskAnalysis(boolean showRiskAnalysis) {
		this.showRiskAnalysis = showRiskAnalysis;
	}
}
