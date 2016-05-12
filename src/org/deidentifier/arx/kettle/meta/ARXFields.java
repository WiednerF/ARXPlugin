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
package org.deidentifier.arx.kettle.meta;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;

/**
 * This is the class for the FieldStorage.</br>
 * <h2>Content</h2>
 * We store here every Field and the values for this field and the creation of the
 * Database or XML Storage and the Default Values.
 * 
 * @author Florian Wiedner
 * @since 1.5
 * @category ARXPluginMeta
 *
 */
public class ARXFields {

	/**
	 * The Field Name from the Datasource
	 */
	private String name;
	/**
	 * The FieldType after the ARX Project
	 */
	private String type;
	/**
	 * Transformation Type Index from the ComboBox 
	 */
	private int transformation;
	/**
	 * The Minimum Number of Generalization from the Hierarchie, -1 if all
	 */
	private int minimumGen;
	/**
	 * The Maximum Step in the Hierarchie for Generalization, -1 for all
	 */
	private int maximumGen;//-1 for all
	/**
	 * The String of the Microaggreagtion function
	 */
	private String functionMicro;
	/**
	 * Using missing Data of Microaggregation (Ignore is true)
	 */
	private boolean missingDataMicro;
	/**
	 * The Path to the hierarchie Source
	 */
	private String hierarchie;
	/**
	 * The attributWeight in prozent for this field
	 */
	private double attributeWeight;
	/**
	 * Using Privacy L-Diversity or not
	 */
	private boolean lDiversityEnable;
	/**
	 * The L Value of the L-Diversity Privacy Criterion
	 */
	private int lDiversity;
	/**
	 * The Variant as Int from the ComboBox for the L-Diversity
	 */
	private int lDiversityVariant;
	/**
	 * The C-Value for the LC-Recursive-Diversity Privacy Criterion
	 */
	private double lDiversityC;
	/**
	 * If T-Closeness is Enabled or not
	 */
	private boolean tClosenessEnable;
	/**
	 * The T-Value of the T-Closeness Privacy
	 */
	private double tCloseness;
	/**
	 * The Measure Index of the ComboBox at T-Closeness Privacy
	 */
	private int tClosenessMeasure;
	/**
	 * D-Disclosure Privacy enabled or not
	 */
	private boolean dDisclosureEnable;
	/**
	 * Delta Value of the D-Disclosure Privacy
	 */
	private double dDisclosure;
	
	/**
	 * Generates a new ARXField with all default Values
	 * @param name The name of the Field
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 */
	public ARXFields(String name) {
		this.name=name;
		this.type="Quasi-identifying";
		this.transformation=0;
		this.minimumGen=-1;
		this.maximumGen=-1;
		this.functionMicro="Generalization";
		this.missingDataMicro=true;
		this.attributeWeight=0.5;
		this.hierarchie="";
		this.lDiversityEnable=false;
		this.lDiversity=2;
		this.lDiversityVariant=0;
		this.lDiversityC=0.001d;
		this.tClosenessEnable=false;
		this.tCloseness=0.001d;
		this.tClosenessMeasure=0;
		this.dDisclosureEnable=true;
		this.dDisclosure=1.0;
	}
	
	/**
	 * Generating a new ARXFields from XML Type of Storage
	 * @param parent The XML Node of the field
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 */
	public ARXFields(Node parent){
		this.name=XMLHandler.getTagValue(parent,"name");
		this.type=XMLHandler.getTagValue(parent,"type");
		this.functionMicro=XMLHandler.getTagValue(parent,"functionMicro");
		this.hierarchie=XMLHandler.getTagValue(parent,"hierarchie");
		this.attributeWeight=Double.parseDouble(XMLHandler.getTagValue(parent,"attributeWeight"));
		this.lDiversityC=Double.parseDouble(XMLHandler.getTagValue(parent,"lDiversityC"));
		this.tCloseness=Double.parseDouble(XMLHandler.getTagValue(parent,"tCloseness"));
		this.transformation=Integer.parseInt(XMLHandler.getTagValue(parent,"transformation"));
		this.minimumGen=Integer.parseInt(XMLHandler.getTagValue(parent,"minimumGen"));
		this.maximumGen=Integer.parseInt(XMLHandler.getTagValue(parent,"maximumGen"));
		this.lDiversity=Integer.parseInt(XMLHandler.getTagValue(parent,"lDiversity"));
		this.lDiversityVariant=Integer.parseInt(XMLHandler.getTagValue(parent,"lDiversityVariant"));
		this.tClosenessMeasure=Integer.parseInt(XMLHandler.getTagValue(parent,"tClosenessMeasure"));
		this.dDisclosure=Double.parseDouble(XMLHandler.getTagValue(parent,"dDisclosure"));
		this.missingDataMicro=Boolean.parseBoolean(XMLHandler.getTagValue(parent,"missingDataMicro"));
		this.lDiversityEnable=Boolean.parseBoolean(XMLHandler.getTagValue(parent,"lDiversityEnable"));
		this.tClosenessEnable=Boolean.parseBoolean(XMLHandler.getTagValue(parent,"tClosenessEnable"));
		this.dDisclosureEnable=Boolean.parseBoolean(XMLHandler.getTagValue(parent,"dDisclosureEnable"));
	}
	
	/**
	 * Creates a new ARXField from Database type of Storage
	 * @param rep The Database Repository
	 * @param id_step The Id of this step
	 * @param count The number of this Field
	 * @throws KettleException
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 */
	public ARXFields(Repository rep, ObjectId id_step, int count) throws KettleException {
		this.setName(rep.getStepAttributeString(id_step, count, "fieldsName"));
		this.setType(rep.getStepAttributeString(id_step, count, "fieldsType"));
		this.setHierarchie(rep.getStepAttributeString(id_step, count, "fieldsHierarchie"));
		this.setFunctionMicro(rep.getStepAttributeString(id_step, count, "fieldsFunctionMicro"));
		this.setAttributeWeight(
				Double.parseDouble(rep.getStepAttributeString(id_step, count, "fieldsAttributeWeight")));
		this.setlDiversityC(Double.parseDouble(rep.getStepAttributeString(id_step, count, "fieldsLDiversityC")));
		this.settCloseness(Double.parseDouble(rep.getStepAttributeString(id_step, count, "fieldsTCloseness")));
		this.setMissingDataMicro(
				Boolean.parseBoolean(rep.getStepAttributeString(id_step, count, "fieldsMissingDataMicro")));
		this.setlDiversityEnable(
				Boolean.parseBoolean(rep.getStepAttributeString(id_step, count, "fieldsLDiversityEnable")));
		this.settClosenessEnable(
				Boolean.parseBoolean(rep.getStepAttributeString(id_step, count, "fieldsTClosenessEnable")));
		this.setdDisclosureEnable(
				Boolean.parseBoolean(rep.getStepAttributeString(id_step, count, "fieldsDDisclosureEnable")));
		this.setTransformation(
				Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsTransformation")));
		this.setMinimumGen(Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsMinimumGen")));
		this.setMaximumGen(Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsMaximumGen")));
		this.setlDiversity(Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsLDiversity")));
		this.setlDiversityVariant(
				Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsLDiversityVariant")));
		this.settClosenessMeasure(
				Integer.parseInt(rep.getStepAttributeString(id_step, count, "fieldsTClosenessMeasure")));
		this.setdDisclosure(Double.parseDouble(rep.getStepAttributeString(id_step, count, "fieldsDDisclosure")));

	}
	
	/***
	 * Generates the XML Output for the Storage of this Field</br>
	 * 
	 * @author Florian Wiedner
	 * @category ARXFields
	 * @return String The String of the XML Representation
	 */
	public String getXML(){
		 String retval = "";

		    retval += "      <field>" + Const.CR;
		    retval += "        " + XMLHandler.addTagValue( "name", getName() );
		    retval += "        " + XMLHandler.addTagValue( "type", type );
		    retval += "        " + XMLHandler.addTagValue( "transformation", transformation );
		    retval += "        " + XMLHandler.addTagValue( "minimumGen", minimumGen );
		    retval += "        " + XMLHandler.addTagValue( "maximumGen", maximumGen );
		    retval += "        " + XMLHandler.addTagValue( "functionMicro", functionMicro );
		    retval += "        " + XMLHandler.addTagValue( "hierarchie", hierarchie );
		    retval += "        " + XMLHandler.addTagValue( "attributeWeight", attributeWeight );
		    retval += "        " + XMLHandler.addTagValue( "lDiversity", lDiversity );
		    retval += "        " + XMLHandler.addTagValue( "lDiversityVariant", lDiversityVariant );
		    retval += "        " + XMLHandler.addTagValue( "lDiversityC", lDiversityC );
		    retval += "        " + XMLHandler.addTagValue( "tCloseness", tCloseness );
		    retval += "        " + XMLHandler.addTagValue( "tClosenessMeasure", tClosenessMeasure );
		    retval += "        " + XMLHandler.addTagValue( "dDisclosure", dDisclosure );
		    retval += "        " + XMLHandler.addTagValue( "missingDataMicro", (missingDataMicro?"true":"false") );
		    retval += "        " + XMLHandler.addTagValue( "lDiversityEnable", (lDiversityEnable?"true":"false") );
		    retval += "        " + XMLHandler.addTagValue( "tClosenessEnable", (tClosenessEnable?"true":"false") );
		    retval += "        " + XMLHandler.addTagValue( "dDisclosureEnable", (dDisclosureEnable?"true":"false") );
		    retval += "        </field>" + Const.CR;

		    return retval;
	}
	
	/**
	 * This Method saves our ARXFields Configuration in the Database way of
	 * Storage</br>
	 * 
	 * @author Florian Wiedner
	 * @category ARXFields
	 * @since 1.5
	 * @throws KettleException
	 * @param rep
	 *            The Repository in the Database
	 * @param id_transformation
	 *            The Object ID of our Transformation
	 * @param id_step
	 *            The Object ID for our Step in the Transformation
	 * @param count
	 *            The actual Place of this Storage
	 */
	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step, int count)
			throws KettleException {
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsName", this.getName());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsTransformation",
				this.getTransformation());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsMinimumGen", this.getMinimumGen());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsMaximumGen", this.getMaximumGen());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsFunctionMicro",
				this.getFunctionMicro());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsHierarchie", this.getHierarchie());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsAttributeWeight",
				this.getAttributeWeight());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsLDiversity", this.getlDiversity());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsLDiversityVariant",
				this.getlDiversityVariant());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsLDiversityC", this.getlDiversityC());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsTCloseness", this.gettCloseness());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsTClosenessMeasure",
				this.gettClosenessMeasure());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsDDisclosure", this.getdDisclosure());
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsMissingDataMicro",
				(this.isMissingDataMicro() ? "true" : "false"));
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsLDiversityEnable",
				(this.islDiversityEnable() ? "true" : "false"));
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsTClosenessEnable",
				(this.istClosenessEnable() ? "true" : "false"));
		rep.saveStepAttribute(id_transformation, id_step, count, "fieldsDDisclosureEnable",
				(this.isdDisclosureEnable() ? "true" : "false"));
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @return The Name of the Field like it comes from other steps
	 */
	public String getName() {
		return name;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @param name The Name of the field
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Type of the Field as String after ARX Project
	 
	 */
	public String getType() {
		return type;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param type The Type of the Field as String after the ARX Project
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Transformation ComboBox Index from the GUI
	 */
	public int getTransformation() {
		return transformation;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param transformation Setting the index of the ComboBox of the Transformation of this Project
	 */
	public void setTransformation(int transformation) {
		this.transformation = transformation;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Minimum Generalization of the Hierarchie, -1 for all
	 */
	public int getMinimumGen() {
		return minimumGen;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param minimumGen The Minimum Generalization of the Hierarchie, -1 for all
	 */
	public void setMinimumGen(int minimumGen) {
		this.minimumGen = minimumGen;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Maximum Generalization of the Hierarchie, -1 for all
	 */
	public int getMaximumGen() {
		return maximumGen;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param maximumGen The Maximum Generalization of the Hierarchie, -1 for all
	 */
	public void setMaximumGen(int maximumGen) {
		this.maximumGen = maximumGen;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Used Microaggregation Function for this Field
	 */
	public String getFunctionMicro() {
		return functionMicro;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param functionMicro Then Microaggregation Function for this field
	 */
	public void setFunctionMicro(String functionMicro) {
		this.functionMicro = functionMicro;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Is Missing Data Ingnored for the Microaggregation
	 */
	public boolean isMissingDataMicro() {
		return missingDataMicro;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param missingDataMicro Ignoring Missing Data at the Microaggregation
	 */
	public void setMissingDataMicro(boolean missingDataMicro) {
		this.missingDataMicro = missingDataMicro;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Path to the Hierarchie CSV File
	 */
	public String getHierarchie() {
		return hierarchie;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param hierarchie The Path to the Hierarchie CSV File
	 */
	public void setHierarchie(String hierarchie) {
		this.hierarchie = hierarchie;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a> 
	 * @return The Attribut Weight of this Field
	 */
	public double getAttributeWeight() {
		return attributeWeight;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param attributeWeight The Attribut Weight of this field
	 */
	public void setAttributeWeight(double attributeWeight) {
		this.attributeWeight = attributeWeight;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using L-Diversity Privacy
	 */
	public boolean islDiversityEnable() {
		return lDiversityEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param lDiversityEnable Anabling or Disabling of L-Diversity Privacy
	 */
	public void setlDiversityEnable(boolean lDiversityEnable) {
		this.lDiversityEnable = lDiversityEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The L-Value of the L-Diversity Privacy
	 */
	public int getlDiversity() {
		return lDiversity;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The L-Value of the L-Diversity Privacy
	 */
	public void setlDiversity(int lDiversity) {
		this.lDiversity = lDiversity;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Index of the ComboBox for the Variant of the L-Diversity
	 */
	public int getlDiversityVariant() {
		return lDiversityVariant;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param lDiversityVariant The Index of the ComboBox for the Variant of the L-Diversity
	 */
	public void setlDiversityVariant(int lDiversityVariant) {
		this.lDiversityVariant = lDiversityVariant;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The C-Value for one Variant of L-Diversity Privacy
	 */
	public double getlDiversityC() {
		return lDiversityC;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param lDiversityC The C-Value for one Variant of L-Diversity Privacy
	 */
	public void setlDiversityC(double lDiversityC) {
		this.lDiversityC = lDiversityC;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using T-Closeness Privacy Criterion
	 */
	public boolean istClosenessEnable() {
		return tClosenessEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param tClosenessEnable Disable or enable T-Closeness Privacy Criterion
	 */
	public void settClosenessEnable(boolean tClosenessEnable) {
		this.tClosenessEnable = tClosenessEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a> 
	 * @return The T-Value of the T-Closeness Privacy
	 */
	public double gettCloseness() {
		return tCloseness;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param tCloseness The T-Value of the T-Closeness Privacy
	 */
	public void settCloseness(double tCloseness) {
		this.tCloseness = tCloseness;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Measure Index of the ComboBox at T-Closeness for this Field
	 */
	public int gettClosenessMeasure() {
		return tClosenessMeasure;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param tClosenessMeasure The T-Closeness Measure Index of the ComboBox
	 */
	public void settClosenessMeasure(int tClosenessMeasure) {
		this.tClosenessMeasure = tClosenessMeasure;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return Using T-Disclosure Privacy Criterion
	 */
	public boolean isdDisclosureEnable() {
		return dDisclosureEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param dDisclosureEnable Enable or Disable D-Disclosure Privacy Criterion
	 */
	public void setdDisclosureEnable(boolean dDisclosureEnable) {
		this.dDisclosureEnable = dDisclosureEnable;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @return The Delta Value of the D-Disclosure Privacy
	 */
	public double getdDisclosure() {
		return dDisclosure;
	}

	/**
	 * @author Florian Wiedner
	 * @since 1.1
	 * @category ARXFields
	 * @see <a href="http://arx.deidentifier.org">ARX Deidentifier Project
	 *      Website</a>
	 * @param dDisclosure The Delta Value of the D-Disclosure Privacy
	 */
	public void setdDisclosure(double dDisclosure) {
		this.dDisclosure = dDisclosure;
	}

}
