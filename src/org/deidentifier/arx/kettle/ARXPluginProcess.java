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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import org.deidentifier.arx.AttributeType.MicroAggregationFunctionDescription;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataGeneralizationScheme;
import org.deidentifier.arx.DataGeneralizationScheme.GeneralizationDegree;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.criteria.AverageReidentificationRisk;
import org.deidentifier.arx.criteria.DDisclosurePrivacy;
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.EDDifferentialPrivacy;
import org.deidentifier.arx.criteria.EntropyLDiversity;
import org.deidentifier.arx.criteria.EqualDistanceTCloseness;
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.KMap;
import org.deidentifier.arx.criteria.KMap.CellSizeEstimator;
import org.deidentifier.arx.criteria.PopulationUniqueness;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.criteria.SampleUniqueness;
import org.deidentifier.arx.kettle.meta.ARXFields;
import org.deidentifier.arx.kettle.risk.ARXRiskAnalysis;
import org.deidentifier.arx.metric.Metric;
import org.deidentifier.arx.metric.Metric.AggregateFunction;
import org.deidentifier.arx.metric.MetricDescription;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.eclipse.swt.widgets.Display;

/**
 * The Class for the Main Part of the Step, the Anonymazation of the Data
 * @author Florian Wiedner
 * @category BaseStep
 * @version 1.0
 * @since 1.7
 *
 */
public class ARXPluginProcess {

	/**
	 * The RowList with all the Data
	 */
	private List<String[]> rowList;
	/**
	 * The FieldName List
	 */
	private String[] fieldNames;
	private ARXPluginMeta meta;

	/** Static settings. */
	private static final List<MetricDescription> METRICS = Metric.list();

	/** Static settings. */
	private static final String[] LABELS = getLabels(METRICS);

	/**
	 * Generates this Class with all the Needed Informations
	 * @param rowList The RowListData
	 * @param fieldNames The FieldNames list of this Step
	 * @param meta The Meta Data for Informations
	 */
	public ARXPluginProcess(List<String[]> rowList, String[] fieldNames, ARXPluginMeta meta) {
		this.rowList = rowList;
		this.fieldNames = fieldNames;
		this.meta = meta;
	}

	/**
	 * The run of the Main Part of the Step
	 * @return The ResultOutpur
	 * @throws IOException
	 */
	public List<String[]> run() throws IOException {
		//Creates Data and the Config
		Data data = Data.create(this.rowList);

		ARXAnonymizer anonymizer = new ARXAnonymizer();
		ARXConfiguration config = ARXConfiguration.create();
		config.setMaxOutliers(meta.getMaxOutliers());
		config.setPracticalMonotonicity(meta.isPracticalMonotonicity());
		config.setUtilityBasedMicroaggregation(meta.isMicroaggregation());
		int index = -1;
		int result1 = -1;
		for (String temp : LABELS) {
			index++;
			if (temp.equals(meta.getMetric())) {
				result1 = index;
				break;
			}
		}
		config.setMetric(METRICS.get(result1).createInstance(config.getMetric().getConfiguration()));
		config.getMetric().getConfiguration().setPrecomputed(meta.isPrecomputed());
		config.getMetric().getConfiguration().setPrecomputationThreshold(meta.getPrecomputedThreshold());
		if (config.getMetric().getDescription().isMonotonicVariantSupported()) {
			config.getMetric().getConfiguration().setMonotonic(meta.isMonotonicVariant());
		}
		if (!config.getMetric().getDescription().getSupportedAggregateFunctions().isEmpty()) {
			for (AggregateFunction function : config.getMetric().getDescription().getSupportedAggregateFunctions()) {
				if (function.toString().equals(meta.getAggregation())) {
					config.getMetric().getConfiguration().setAggregateFunction(function);
				}
			}
		}
		if (config.getMetric().getDescription().isConfigurableCodingModelSupported()) {
			config.setSuppressionLimit(this.meta.getGSFactor());
			config.getMetric().getConfiguration().setGsFactor(this.meta.getGSFactor());
		}
		if (this.meta.isKanonymityEnabled()) {
			config.addCriterion(new KAnonymity(this.meta.getKanonymity()));
		}
		if (this.meta.isDifferentialEnabled()) {
			config.addCriterion(new EDDifferentialPrivacy(meta.getDifferentialEpsilon(), meta.getDifferentialDelta(),
					DataGeneralizationScheme
							.create(GeneralizationDegree.values()[this.meta.getDifferentialGeneralizationIndex()])));
		}
		if (this.meta.isReidentificationRiskEnabled()) {
			config.addCriterion(new AverageReidentificationRisk(this.meta.getReidentificationRisk()));
		}
		Region temp = null;
		for (Region temp3 : Region.values()) {
			if (temp3.getName().equals(this.meta.getRegion())) {
				temp = temp3;
				break;
			}
		}

		ARXPopulationModel populationModel = ARXPopulationModel.create(temp);
		if (this.meta.getRegion() != null && this.meta.getRegions(this.meta.getRegion()) != null) {
			if (temp.getPopulationSize() != this.meta.getRegions(this.meta.getRegion()).getPopulation()) {
				populationModel = ARXPopulationModel
						.create(this.meta.getRegions(this.meta.getRegion()).getPopulation());
			}
		}

		if (this.meta.isKmapEnabled()) {
			config.addCriterion(new KMap(this.meta.getKmap(), this.meta.getKmapSignificanceLevel(), populationModel,
					(this.meta.getKmapEstimatorIndex() == 0 ? CellSizeEstimator.POISSON
							: CellSizeEstimator.ZERO_TRUNCATED_POISSON)));
		}

		if (this.meta.isSampleUniquenessEnabled()) {
			config.addCriterion(new SampleUniqueness(this.meta.getSampleUniqueness()));
		}
		if (this.meta.isPopulationUniquenessEnabled()) {
			config.addCriterion(new PopulationUniqueness(this.meta.getPopulationUniqueness(),
					this.getPopulationUniquessModel(this.meta.getPopulationUniquenessModel()), populationModel));
		}

		for (int i = 0; i < this.fieldNames.length; i++) {
			ARXFields field = this.meta.getField(fieldNames[i]);

			if (field.getType().equals("Insensitive")) {
				data.getDefinition().setAttributeType(field.getName(), AttributeType.INSENSITIVE_ATTRIBUTE);
			} else if (field.getType().equals("Sensitive")) {
				data.getDefinition().setAttributeType(field.getName(), AttributeType.SENSITIVE_ATTRIBUTE);
				if (field.islDiversityEnable()) {
					switch (field.getlDiversityVariant()) {
					case 0:
						config.addCriterion(new DistinctLDiversity(field.getName(), field.getlDiversity()));
						break;
					case 1:
						config.addCriterion(new EntropyLDiversity(field.getName(), field.getlDiversity()));
						break;
					default:
						config.addCriterion(new RecursiveCLDiversity(field.getName(), field.getlDiversityC(),
								field.getlDiversity()));
						break;
					}
				}
				if (field.isdDisclosureEnable()) {
					config.addCriterion(new DDisclosurePrivacy(field.getName(), field.getdDisclosure()));
				}
				if (field.istClosenessEnable()) {
					if (field.gettClosenessMeasure() == 0) {
						config.addCriterion(new EqualDistanceTCloseness(field.getName(), field.gettCloseness()));
					} else if (field.getHierarchie() != "") {
						config.addCriterion(new HierarchicalDistanceTCloseness(field.getName(), field.gettCloseness(),
								Hierarchy.create(field.getHierarchie(), Charset.defaultCharset(), ';')));
					}
				}
			} else if (field.getType().equals("Quasi-identifying")) {
				data.getDefinition().setAttributeType(field.getName(), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
				if (config.getMetric().getDescription().isAttributeWeightsSupported()) {
					config.setAttributeWeight(field.getName(), field.getAttributeWeight());
				}
				if (field.getHierarchie() != "" && field.getHierarchie() != null) {
					File file = new File(field.getHierarchie());
					if (file.exists() && file.isFile()) {
						data.getDefinition().setAttributeType(field.getName(),
								Hierarchy.create(field.getHierarchie(), Charset.defaultCharset(), ';'));
					}
				}
			} else if (field.getType().equals("Identifying")) {
				data.getDefinition().setAttributeType(field.getName(), AttributeType.IDENTIFYING_ATTRIBUTE);
			}

			if (field.getTransformation() == 0) {
				if (field.getMaximumGen() != -1) {
					data.getDefinition().setMaximumGeneralization(field.getName(), field.getMaximumGen());
				}
				if (field.getMinimumGen() != -1) {
					data.getDefinition().setMinimumGeneralization(field.getName(), field.getMinimumGen());
				}

			} else {
				MicroAggregationFunction function = getMicroAggregationFunction(field.getFunctionMicro())
						.createInstance(field.isMissingDataMicro());
				data.getDefinition().setMicroAggregationFunction(field.getName(), function);
			}

		}

		//Anonymize
		ARXResult result = anonymizer.anonymize(data, config);
		if (result == null || result.getOutput(false) == null) {
			return new ArrayList<String[]>();
		} else {

			DataHandle output = result.getOutput(false);

			Iterator<String[]> transformed = output.iterator();
			boolean first = true;
			this.rowList = new ArrayList<String[]>();
			while (transformed.hasNext()) {
				if (first) {
					first = false;
					transformed.next();
				} else {
					this.rowList.add(transformed.next());
				}
			}
			if (meta.isShowRiskAnalysis()) {

				Thread risk = new ARXRiskAnalysis( output, data, populationModel, Display.getDefault());
				Display.getDefault().asyncExec(risk);
			}

			return this.rowList;
		}

	}

	/**
	 * Gets the PopulationUniqueness Model out of the Index
	 * @param x The Index
	 * @return The Population Uniqueness Model
	 */
	private PopulationUniquenessModel getPopulationUniquessModel(int x) {
		switch (x) {
		case 0:
			return PopulationUniquenessModel.DANKAR;
		case 1:
			return PopulationUniquenessModel.PITMAN;
		case 2:
			return PopulationUniquenessModel.ZAYATZ;
		default:
			return PopulationUniquenessModel.SNB;
		}
	}

	/**
	 * Returns the microaggregation function for the given label
	 * 
	 * @param function
	 * @return
	 */
	private MicroAggregationFunctionDescription getMicroAggregationFunction(String label) {
		for (MicroAggregationFunctionDescription function : AttributeType.listMicroAggregationFunctions()) {
			if (function.getLabel().equals(label)) {
				return function;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of names of all available metrics.
	 *
	 * @param metrics
	 * @return
	 */
	private static String[] getLabels(List<MetricDescription> metrics) {
		String[] labels = new String[metrics.size()];
		for (int i = 0; i < metrics.size(); i++) {
			labels[i] = metrics.get(i).getName();
		}
		return labels;
	}


}
