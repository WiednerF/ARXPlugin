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

/*
 * Created on 02-jun-2003
 *
 */
/***
 * The Class for the MetaData and Saving of all Values
 * @author Florian Wiedner
 *
 */
public class ARXPluginMeta extends BaseStepMeta implements StepMetaInterface
{
	private int kanonymity;//The Field for Kanonymity
	private boolean kanonymityEnabled;//The Field for Kanonymity
	private boolean differentialEnabled;
	private double differentialDelta;
	private double differentialEpsilon;
	private int differentialGeneralizationIndex;
	private boolean kmapEnabled;
	private int kmap;
	private int kmapEstimatorIndex;
	private double kmapSignificanceLevel;
	private boolean reidentificationRiskEnabled;
	private double reidentificationRisk;
	private boolean sampleUniquenessEnabled;
	private double sampleUniqueness;
	private boolean populationUniquenessEnabled;
	private double populationUniqueness;
	private int populationUniquenessModel;
	
	
	private double maxOutliers;
	private boolean practicalMonotonicity;
	private boolean precomputed;
	private double precomputedThreshold;
	private boolean monotonicVariant;
	private String metric;
	private boolean microaggregation;
	private String aggregation;
	private double GSFactor;
	private String region;
	private HashMap<String,RegionStore> regions;
	private HashMap<String,ARXFields> fields;
	private boolean showRiskAnalysis;
	
	
	
	
	
	

	public ARXPluginMeta()
	{
		super(); // allocate BaseStepInfo
		regions = new HashMap<String,RegionStore>();
		fields = new HashMap<String,ARXFields>(); 
	}
	
	public String getXML() throws KettleException
	{
		StringBuilder retval = new StringBuilder( 800 );
	    retval.append( "    " ).append( XMLHandler.addTagValue( "kanonymity", this.kanonymity+"" ) );
	    retval.append( "    " ).append( XMLHandler.addTagValue( "kanonymityEnabled", (this.kanonymityEnabled?"true":"false") ) );
	    retval.append( "    " ).append( XMLHandler.addTagValue( "maxoutliers", this.maxOutliers+"" ) ); 
	    retval.append( "    " ).append(XMLHandler.addTagValue( "practicalMonotonicity", (this.practicalMonotonicity?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "precomputed", (this.precomputed?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "precomputedThreshold", this.precomputedThreshold));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "monotonicVariant", (this.monotonicVariant?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "metric", this.metric));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "microaggregation", (this.microaggregation?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "aggregation", this.aggregation));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "GSFactor", this.GSFactor));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "differentialEnabled", (this.differentialEnabled?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "differentialDelta", this.differentialDelta));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "differentialEpsilon", this.differentialEpsilon));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "differentialGeneralizationIndex", this.differentialGeneralizationIndex));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "kmapEnabled", (this.kmapEnabled?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "kmap", this.kmap));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "kmapEstimatorIndex", this.kmapEstimatorIndex));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "kmapSignificanceLevel", this.kmapSignificanceLevel));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "region", this.region));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "reidentificationRisk", this.reidentificationRisk));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "reidentificationRiskEnabled", (this.reidentificationRiskEnabled?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "sampleUniqueness", this.sampleUniqueness));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "sampleUniquenessEnabled", (this.sampleUniquenessEnabled?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "populationUniqueness", this.populationUniqueness));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "populationUniquenessEnabled", (this.populationUniquenessEnabled?"true":"false")));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "populationUniquenessModel", this.populationUniquenessModel));
	    retval.append( "    " ).append(XMLHandler.addTagValue( "showRiskAnalysis", (this.showRiskAnalysis?"true":"false")));
	    
	    Set<String> tempRegions=regions.keySet();
	    retval.append( "    <regions>" + Const.CR );
	    for (String temp : tempRegions) {
	      RegionStore regions = this.regions.get(temp);
	      retval.append( regions.getXML() );
	    }
	    retval.append( "      </regions>" + Const.CR );
	    
	    
	    Set<String> tempFields=this.fields.keySet();
	    retval.append( "    <fields>" + Const.CR );
	    for (String temp : tempFields) {
	      ARXFields regions = this.fields.get(temp);
	      retval.append( regions.getXML() );
	    }
	    retval.append( "      </fields>" + Const.CR );
	    
	    return retval.toString();
	}

	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	{
		//DELETE After Starting of the Use of Data.Types
		String[] fieldNames = r.getFieldNames();
		for(int i=0;i<fieldNames.length;i++){
			ValueMetaInterface temp = r.getValueMeta(i);
			temp.getLength();
			ValueMeta temp2 = new ValueMeta(fieldNames[i],ValueMeta.getType("String"),temp.getLength(),temp.getPrecision());
			try {
				r.removeValueMeta(fieldNames[i]);
				r.addValueMeta(i, temp2);
			} catch (KettleValueException e) {
				logError("Unexpected error : "+e.toString());
	            logError(Const.getStackTracker(e));
			}
			
		}
	}

	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String,Counter> counters)
		throws KettleXMLException
	{
		this.setDefault();
		try {
				if(XMLHandler.getTagValue(stepnode, "kanonymity")!=null){
					this.kanonymity = Integer.parseInt(XMLHandler.getTagValue( stepnode, "kanonymity" ));
				}
				if(XMLHandler.getTagValue(stepnode, "kanonymityEnabled")!=null){
					this.kanonymityEnabled = Boolean.parseBoolean(XMLHandler.getTagValue( stepnode, "kanonymityEnabled" ));
				}
				if(XMLHandler.getTagValue(stepnode, "maxoutliers")!=null){
					this.maxOutliers = Double.parseDouble(XMLHandler.getTagValue(stepnode, "maxoutliers"));
				}
				if(XMLHandler.getTagValue(stepnode, "practicalMonotonicity")!=null){
					this.practicalMonotonicity = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "practicalMonotonicity"));
				}
				if(XMLHandler.getTagValue(stepnode, "precomputed")!=null){
					this.precomputed = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "precomputed"));
				}
				if(XMLHandler.getTagValue(stepnode, "precomputedThreshold")!=null){
					this.precomputedThreshold = Double.parseDouble(XMLHandler.getTagValue(stepnode, "precomputedThreshold"));
				}
				if(XMLHandler.getTagValue(stepnode, "monotonicVariant")!=null){
					this.monotonicVariant = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "monotonicVariant"));
				}
				if(XMLHandler.getTagValue(stepnode, "metric")!=null){
					this.metric = XMLHandler.getTagValue(stepnode, "metric");
				}
				if(XMLHandler.getTagValue(stepnode, "aggregation")!=null){
					this.aggregation = XMLHandler.getTagValue(stepnode, "aggregation");
				}
				if(XMLHandler.getTagValue(stepnode, "microaggregation")!=null){
					this.microaggregation = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "microaggregation"));
				}
				if(XMLHandler.getTagValue(stepnode, "GSFactor")!=null){
					this.GSFactor = Double.parseDouble(XMLHandler.getTagValue(stepnode, "GSFactor"));
				}
				if(XMLHandler.getTagValue(stepnode, "region")!=null){
					this.region = XMLHandler.getTagValue(stepnode, "region");
				}
				if(XMLHandler.getTagValue(stepnode, "differentialEpsilon")!=null){
					this.differentialEpsilon = Double.parseDouble(XMLHandler.getTagValue(stepnode, "differentialEpsilon"));
				}
				if(XMLHandler.getTagValue(stepnode, "differentialDelta")!=null){
					this.differentialDelta = Double.parseDouble(XMLHandler.getTagValue(stepnode, "differentialDelta"));
				}
				if(XMLHandler.getTagValue(stepnode, "differentialEnabled")!=null){
					this.differentialEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "differentialEnabled"));
				}
				if(XMLHandler.getTagValue(stepnode, "differentialGeneralizationIndex")!=null){
					this.differentialGeneralizationIndex = Integer.parseInt(XMLHandler.getTagValue( stepnode, "differentialGeneralizationIndex" ));
				}
				if(XMLHandler.getTagValue(stepnode, "kmapEnabled")!=null){
					this.kmapEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "kmapEnabled"));
				}
				if(XMLHandler.getTagValue(stepnode, "kmap")!=null){
					this.kmap = Integer.parseInt(XMLHandler.getTagValue( stepnode, "kmap" ));
				}
				if(XMLHandler.getTagValue(stepnode, "kmapEstimatorIndex")!=null){
					this.kmapEstimatorIndex = Integer.parseInt(XMLHandler.getTagValue( stepnode, "kmapEstimatorIndex" ));
				}
				if(XMLHandler.getTagValue(stepnode, "kmapSignificanceLevel")!=null){
					this.kmapSignificanceLevel = Double.parseDouble(XMLHandler.getTagValue(stepnode, "kmapSignificanceLevel"));
				}
				if(XMLHandler.getTagValue(stepnode, "reidentificationRisk")!=null){
					this.reidentificationRisk = Double.parseDouble(XMLHandler.getTagValue(stepnode, "reidentificationRisk"));
				}
				if(XMLHandler.getTagValue(stepnode, "reidentificationRiskEnabled")!=null){
					this.reidentificationRiskEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "reidentificationRiskEnabled"));
				}
				if(XMLHandler.getTagValue(stepnode, "sampleUniqueness")!=null){
					this.sampleUniqueness = Double.parseDouble(XMLHandler.getTagValue(stepnode, "sampleUniqueness"));
				}
				if(XMLHandler.getTagValue(stepnode, "sampleUniquenessEnabled")!=null){
					this.sampleUniquenessEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "sampleUniquenessEnabled"));
				}
				if(XMLHandler.getTagValue(stepnode, "populationUniqueness")!=null){
					this.populationUniqueness = Double.parseDouble(XMLHandler.getTagValue(stepnode, "populationUniqueness"));
				}
				if(XMLHandler.getTagValue(stepnode, "populationUniquenessEnabled")!=null){
					this.populationUniquenessEnabled = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "populationUniquenessEnabled"));
				}
				if(XMLHandler.getTagValue(stepnode, "populationUniquenessModel")!=null){
					this.populationUniquenessModel = Integer.parseInt(XMLHandler.getTagValue( stepnode, "populationUniquenessModel" ));
				}
				if(XMLHandler.getTagValue(stepnode, "showRiskAnalysis")!=null){
					this.showRiskAnalysis = Boolean.parseBoolean(XMLHandler.getTagValue(stepnode, "showRiskAnalysis"));
				}

				Node regionNode = XMLHandler.getSubNode(stepnode, "regions");
				if(regionNode!=null){
					int nrRegions = XMLHandler.countNodes(regionNode, "region");

					for(int i=0;i<nrRegions;i++){
						Node tempRegion=XMLHandler.getSubNodeByNr(regionNode, "region", i);
						RegionStore temp=new RegionStore(tempRegion);
						this.regions.put(temp.getName(), temp);
					}
				}		
				
				Node fieldsNode = XMLHandler.getSubNode(stepnode, "fields");
				if(fieldsNode!=null){
					int nrFields = XMLHandler.countNodes(fieldsNode, "field");

					for(int i=0;i<nrFields;i++){
						Node tempField=XMLHandler.getSubNodeByNr(fieldsNode, "field", i);
						ARXFields temp=new ARXFields(tempField);
						this.fields.put(temp.getName(), temp);
					}
				}			
			} catch ( Exception e ) {
		      throw new KettleXMLException( "Unable to load step info from XML", e );
		    }
	}

	/**
	 * Sets the Default Values
	 */
	public void setDefault()
	{
		this.kanonymity=5;//Default for kanonymity
		this.maxOutliers=0d;
		this.practicalMonotonicity=false;
		this.precomputed=false;
		this.precomputedThreshold=1.0;
		this.metric = "Loss";
		this.monotonicVariant=false;
		this.microaggregation=false;
		this.aggregation="Geometric Mean";
		this.GSFactor = 0.5;
		this.region="Europe";
		this.kanonymityEnabled=true;
		this.differentialEnabled = false;
		this.differentialDelta= 1.1e-6d;
		this.differentialEpsilon=2;
		this.differentialGeneralizationIndex=4;
		this.kmapEnabled=false;
		this.kmap=5;
		this.kmapEstimatorIndex=0;
		this.kmapSignificanceLevel=0.01d;
		this.reidentificationRiskEnabled=false;
		this.reidentificationRisk=0.01d;
		this.sampleUniqueness=0.1d;
		this.sampleUniquenessEnabled=false;
		this.populationUniqueness=0.1d;
		this.populationUniquenessEnabled=false;
		this.populationUniquenessModel=0;
		this.showRiskAnalysis=true;
	}

	public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String,Counter> counters) throws KettleException
	{
		this.setDefault();
		 try {
			 this.kanonymity = Integer.parseInt(rep.getStepAttributeString( id_step, "kanonymity" ));
			 this.kanonymityEnabled = Boolean.parseBoolean(rep.getStepAttributeString( id_step, "kanonymityEnabled" ));
			 this.maxOutliers = Double.parseDouble(rep.getStepAttributeString(id_step, "maxoutliers"));
			 this.practicalMonotonicity = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "practicalMonotonicity"));
			 this.precomputed = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "precomputed"));
			 this.precomputedThreshold = Double.parseDouble(rep.getStepAttributeString(id_step, "precomputedThreshold"));
			 this.monotonicVariant = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "monotonicVariant"));
			 this.microaggregation = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "microaggregation"));
			 this.metric = rep.getStepAttributeString(id_step,  "metric");
			 this.aggregation = rep.getStepAttributeString(id_step,  "aggregation");
			 this.region = rep.getStepAttributeString(id_step,  "region");
			 this.GSFactor = Double.parseDouble(rep.getStepAttributeString(id_step,  "GSFactor"));
			 this.differentialEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "differentialEnabled"));
			 this.differentialDelta = Double.parseDouble(rep.getStepAttributeString(id_step,  "differentialDelta"));
			 this.differentialEpsilon = Double.parseDouble(rep.getStepAttributeString(id_step,  "differentialEpsilon"));
			 this.differentialGeneralizationIndex = Integer.parseInt(rep.getStepAttributeString( id_step, "differentialGeneralizationIndex" ));
			 this.kmapEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "kmapEnabled"));
			 this.kmap = Integer.parseInt(rep.getStepAttributeString( id_step, "kmap" ));
			 this.kmapEstimatorIndex = Integer.parseInt(rep.getStepAttributeString( id_step, "kmapEstimatorIndex" ));
			 this.kmapSignificanceLevel = Double.parseDouble(rep.getStepAttributeString(id_step,  "kmapSignificanceLevel"));
			 this.sampleUniqueness = Double.parseDouble(rep.getStepAttributeString(id_step,  "sampleUniqueness"));
			 this.sampleUniquenessEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "sampleUniquenessEnabled"));
			 this.populationUniqueness = Double.parseDouble(rep.getStepAttributeString(id_step,  "populationUniqueness"));
			 this.populationUniquenessEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "populationUniquenessEnabled"));
			 this.reidentificationRisk = Double.parseDouble(rep.getStepAttributeString(id_step,  "reidentificationRisk"));
			 this.reidentificationRiskEnabled = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "reidentificationRiskEnabled"));
			 this.populationUniquenessModel = Integer.parseInt(rep.getStepAttributeString( id_step, "populationUniquenessModel" ));
			 this.showRiskAnalysis = Boolean.parseBoolean(rep.getStepAttributeString(id_step,  "showRiskAnalysis"));

			 int nrRegions = rep.countNrStepAttributes( id_step, "regionsName" );
			 
				for(int i=0;i<nrRegions;i++){
					RegionStore temp=new RegionStore("",0.0,0l);
					temp.setName(rep.getStepAttributeString(id_step,i, "regionsName"));
					temp.setSampling(Double.parseDouble(rep.getStepAttributeString(id_step,i, "regionsSampling")));
					temp.setPopulation(Long.parseLong(rep.getStepAttributeString(id_step,i, "regionsPopulation")));
					this.regions.put(temp.getName(), temp);
				}
				
				int nrFields = rep.countNrStepAttributes( id_step, "fieldsName" );
				 
				for(int i=0;i<nrFields;i++){
					ARXFields temp=new ARXFields(rep.getStepAttributeString(id_step,i, "fieldsName"));
					temp.setType(rep.getStepAttributeString(id_step,i, "fieldsType"));
					temp.setHierarchie(rep.getStepAttributeString(id_step,i, "fieldsHierarchie"));
					temp.setFunctionMicro(rep.getStepAttributeString(id_step,i, "fieldsFunctionMicro"));
					temp.setAttributeWeight(Double.parseDouble(rep.getStepAttributeString(id_step,i, "fieldsAttributeWeight")));
					temp.setlDiversityC(Double.parseDouble(rep.getStepAttributeString(id_step,i, "fieldsLDiversityC")));
					temp.settCloseness(Double.parseDouble(rep.getStepAttributeString(id_step,i, "fieldsTCloseness")));
					temp.setMissingDataMicro(Boolean.parseBoolean(rep.getStepAttributeString(id_step,i, "fieldsMissingDataMicro")));
					temp.setlDiversityEnable(Boolean.parseBoolean(rep.getStepAttributeString(id_step,i, "fieldsLDiversityEnable")));
					temp.settClosenessEnable(Boolean.parseBoolean(rep.getStepAttributeString(id_step,i, "fieldsTClosenessEnable")));
					temp.setdDisclosureEnable(Boolean.parseBoolean(rep.getStepAttributeString(id_step,i, "fieldsDDisclosureEnable")));
					temp.setTransformation(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsTransformation")));
					temp.setMinimumGen(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsMinimumGen")));
					temp.setMaximumGen(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsMaximumGen")));
					temp.setlDiversity(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsLDiversity")));
					temp.setlDiversityVariant(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsLDiversityVariant")));
					temp.settClosenessMeasure(Integer.parseInt(rep.getStepAttributeString(id_step,i, "fieldsTClosenessMeasure")));
					temp.setdDisclosure(Double.parseDouble(rep.getStepAttributeString(id_step,i, "fieldsDDisclosure")));

					this.fields.put(temp.getName(), temp);
				}
				
		 
		 } catch ( Exception e ) {
		      throw new KettleException( "Unexpected error reading step information from the repository", e );
		    }
		 
	}
	
	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException
	{
		 try {
		      rep.saveStepAttribute( id_transformation, id_step, "kanonymity", this.kanonymity );
		      rep.saveStepAttribute( id_transformation, id_step, "kanonymityEnabled", (this.kanonymityEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "maxoutliers", this.maxOutliers );
		      rep.saveStepAttribute( id_transformation, id_step, "practicalMonotonicity", (this.practicalMonotonicity?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "precomputed", (this.precomputed?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "monotonicVariant", (this.monotonicVariant?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "microaggregation", (this.microaggregation?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "metric", this.metric );
		      rep.saveStepAttribute( id_transformation, id_step, "aggregation", this.aggregation );
		      rep.saveStepAttribute( id_transformation, id_step, "region", this.region );
		      rep.saveStepAttribute( id_transformation, id_step, "precomputedThreshold", this.precomputedThreshold );
		      rep.saveStepAttribute( id_transformation, id_step, "GSFactor", this.GSFactor );
		      rep.saveStepAttribute( id_transformation, id_step, "differentialEnabled", (this.differentialEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "differentialDelta", this.differentialDelta );
		      rep.saveStepAttribute( id_transformation, id_step, "differentialEpsilon", this.differentialEpsilon );
		      rep.saveStepAttribute( id_transformation, id_step, "differentialGeneralizationIndex", this.differentialGeneralizationIndex );
		      rep.saveStepAttribute( id_transformation, id_step, "kmapEnabled", (this.kmapEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "kmap", this.kmap );
		      rep.saveStepAttribute( id_transformation, id_step, "kmapEstimatorIndex", this.kmapEstimatorIndex );
		      rep.saveStepAttribute( id_transformation, id_step, "kmapSignificanceLevel", this.kmapSignificanceLevel );
		      rep.saveStepAttribute( id_transformation, id_step, "reidentificationRiskEnabled", (this.reidentificationRiskEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "reidentificationRisk", this.reidentificationRisk );
		      rep.saveStepAttribute( id_transformation, id_step, "sampleUniquenessEnabled", (this.sampleUniquenessEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "sampleUniqueness", this.sampleUniqueness );
		      rep.saveStepAttribute( id_transformation, id_step, "populationUniquenessEnabled", (this.populationUniquenessEnabled?"true":"false") );
		      rep.saveStepAttribute( id_transformation, id_step, "populationUniqueness", this.populationUniqueness );
		      rep.saveStepAttribute( id_transformation, id_step, "populationUniquenessModel", this.populationUniquenessModel );
		      rep.saveStepAttribute( id_transformation, id_step, "showRiskAnalysis", (this.showRiskAnalysis?"true":"false") );

		      Set<String> tempRegions=regions.keySet();
			    int i=0;
			    for(String regionNames:tempRegions){
			    	if(this.regions.get(regionNames)!=null){
			    		RegionStore temp=this.regions.get(regionNames);
			    		rep.saveStepAttribute( id_transformation, id_step,i, "regionsName", temp.getName() );
			    		rep.saveStepAttribute(id_transformation, id_step, i,"regionsSampling", temp.getSampling());
			    		rep.saveStepAttribute(id_transformation, id_step, i,"regionsPopulation", temp.getPopulation());
			    		i++;
			    	}
			    }
			    
			    Set<String> tempFields=fields.keySet();
			    int x=0;
			    for(String fieldsNames:tempFields){
			    	if(this.fields.get(fieldsNames)!=null){
			    		ARXFields temp=this.fields.get(fieldsNames);
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsName", temp.getName() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsTransformation", temp.getTransformation() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsMinimumGen", temp.getMinimumGen() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsMaximumGen", temp.getMaximumGen() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsFunctionMicro", temp.getFunctionMicro() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsHierarchie", temp.getHierarchie() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsAttributeWeight", temp.getAttributeWeight() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsLDiversity", temp.getlDiversity() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsLDiversityVariant", temp.getlDiversityVariant() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsLDiversityC", temp.getlDiversityC() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsTCloseness", temp.gettCloseness() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsTClosenessMeasure", temp.gettClosenessMeasure() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsDDisclosure", temp.getdDisclosure() );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsMissingDataMicro",(temp.isMissingDataMicro()?"true":"false") );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsLDiversityEnable",(temp.islDiversityEnable()?"true":"false") );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsTClosenessEnable",(temp.istClosenessEnable()?"true":"false") );
			    		rep.saveStepAttribute( id_transformation, id_step,x, "fieldsDDisclosureEnable",(temp.isdDisclosureEnable()?"true":"false") );
			    		
			    		i++;
			    	}
			    }
		 } catch ( Exception e ) {
		      throw new KettleException( "Unable to save step information to the repository for id_step=" + id_step, e );
		    }
	}

	public boolean isPrecomputed() {
		return precomputed;
	}

	public void setPrecomputed(boolean precomputed) {
		this.precomputed = precomputed;
	}

	public double getPrecomputedThreshold() {
		return precomputedThreshold;
	}

	public void setPrecomputedThreshold(double precomputedThreshold) {
		this.precomputedThreshold = precomputedThreshold;
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is connected to previous one, receiving "+prev.size()+" fields", stepMeta);
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}
	}
	
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name)
	{
		return new ARXPluginDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp)
	{
		return new ARXPlugin(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData()
	{
		return new ARXPluginData();
	}

	public int getKanonymity() {
		return kanonymity;
	}

	public void setKanonymity(int kanonymity) {
		this.kanonymity = kanonymity;
	}

	public double getMaxOutliers() {
		return maxOutliers;
	}

	public void setMaxOutliers(double maxOutliers) {
		this.maxOutliers = maxOutliers;
	}
	

	public boolean isPracticalMonotonicity() {
		return practicalMonotonicity;
	}

	public void setPracticalMonotonicity(boolean practicalMonotonicity) {
		this.practicalMonotonicity = practicalMonotonicity;
	}
	
	public boolean isMonotonicVariant() {
		return monotonicVariant;
	}

	public void setMonotonicVariant(boolean monotonicVariant) {
		this.monotonicVariant = monotonicVariant;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public boolean isMicroaggregation() {
		return microaggregation;
	}

	public void setMicroaggregation(boolean microaggregation) {
		this.microaggregation = microaggregation;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	public double getGSFactor() {
		return GSFactor;
	}

	public void setGSFactor(double gSFactor) {
		GSFactor = gSFactor;
	}
	
	public RegionStore getRegions(String region){
		if(this.regions.containsKey(region)){
			return this.regions.get(region);
		}else{
			Region regionName=null;
			 for (Region regionOptimal : Region.values()) {
		           if(this.region.equals(regionOptimal.getName())){
		        	   regionName=regionOptimal;
		           }
		        }
			//TODO Get Sampling Size
			return new RegionStore("region",(0.1d),(regionName!=null?regionName.getPopulationSize():0));
		}
	}
	
	public void setRegion(String name,double sampling,long population){
		if(this.regions.containsKey(name)){
			RegionStore temp=this.regions.get(name);
			temp.setName(name);
			temp.setSampling(sampling);
			temp.setPopulation(population);
		}else{
			this.regions.put(name, new RegionStore(name,sampling,population));
		}
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isKanonymityEnabled() {
		return kanonymityEnabled;
	}

	public void setKanonymityEnabled(boolean kanonymityEnabled) {
		this.kanonymityEnabled = kanonymityEnabled;
	}

	public boolean isDifferentialEnabled() {
		return differentialEnabled;
	}

	public void setDifferentialEnabled(boolean differentialEnabled) {
		this.differentialEnabled = differentialEnabled;
	}

	public double getDifferentialDelta() {
		return differentialDelta;
	}

	public void setDifferentialDelta(double differentialDelta) {
		this.differentialDelta = differentialDelta;
	}

	public double getDifferentialEpsilon() {
		return differentialEpsilon;
	}

	public void setDifferentialEpsilon(double differentialEpsilon) {
		this.differentialEpsilon = differentialEpsilon;
	}

	public int getDifferentialGeneralizationIndex() {
		return differentialGeneralizationIndex;
	}

	public void setDifferentialGeneralizationIndex(int differentialGeneralizationIndex) {
		this.differentialGeneralizationIndex = differentialGeneralizationIndex;
	}

	public boolean isKmapEnabled() {
		return kmapEnabled;
	}

	public void setKmapEnabled(boolean kmapEnabled) {
		this.kmapEnabled = kmapEnabled;
	}

	public int getKmap() {
		return kmap;
	}

	public void setKmap(int kmap) {
		this.kmap = kmap;
	}

	public int getKmapEstimatorIndex() {
		return kmapEstimatorIndex;
	}

	public void setKmapEstimatorIndex(int kmapEstimatorIndex) {
		this.kmapEstimatorIndex = kmapEstimatorIndex;
	}

	public double getKmapSignificanceLevel() {
		return kmapSignificanceLevel;
	}

	public void setKmapSignificanceLevel(double kmapSignificanceLevel) {
		this.kmapSignificanceLevel = kmapSignificanceLevel;
	}

	public boolean isReidentificationRiskEnabled() {
		return reidentificationRiskEnabled;
	}

	public void setReidentificationRiskEnabled(boolean reidentificationRiskEnabled) {
		this.reidentificationRiskEnabled = reidentificationRiskEnabled;
	}

	public double getReidentificationRisk() {
		return reidentificationRisk;
	}

	public void setReidentificationRisk(double reidentificationRisk) {
		this.reidentificationRisk = reidentificationRisk;
	}

	public boolean isSampleUniquenessEnabled() {
		return sampleUniquenessEnabled;
	}

	public void setSampleUniquenessEnabled(boolean sampleUniquenessEnabled) {
		this.sampleUniquenessEnabled = sampleUniquenessEnabled;
	}

	public double getSampleUniqueness() {
		return sampleUniqueness;
	}

	public void setSampleUniqueness(double sampleUniqueness) {
		this.sampleUniqueness = sampleUniqueness;
	}

	public boolean isPopulationUniquenessEnabled() {
		return populationUniquenessEnabled;
	}

	public void setPopulationUniquenessEnabled(boolean populationUniquenessEnabled) {
		this.populationUniquenessEnabled = populationUniquenessEnabled;
	}

	public double getPopulationUniqueness() {
		return populationUniqueness;
	}

	public void setPopulationUniqueness(double populationUniqueness) {
		this.populationUniqueness = populationUniqueness;
	}

	public int getPopulationUniquenessModel() {
		return populationUniquenessModel;
	}

	public void setPopulationUniquenessModel(int populationUniquenessModel) {
		this.populationUniquenessModel = populationUniquenessModel;
	}
	
	public ARXFields getField(String name){
		if(this.fields.get(name)!=null){
			return this.fields.get(name);
		}
		ARXFields temp=new ARXFields(name);
		this.fields.put(name, temp);
		return temp;
	}
	
	public void setField(ARXFields field){
		this.fields.put(field.getName(), field);
	}

	public String[] getQuasiIdentifierFields(String[] fieldNames){
		if(fieldNames!=null){
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<fieldNames.length;i++){
			ARXFields temp=this.fields.get(fieldNames[i]);
			if(temp!=null&&temp.getType().equals("Quasi-identifying")){
				list.add(temp.getName());
			}
		}
		return list.toArray(new String[list.size()]);
		}
		return new String[0];
	}
	public boolean isShowRiskAnalysis() {
		return showRiskAnalysis;
	}

	public void setShowRiskAnalysis(boolean showRiskAnalysis) {
		this.showRiskAnalysis = showRiskAnalysis;
	}
}
