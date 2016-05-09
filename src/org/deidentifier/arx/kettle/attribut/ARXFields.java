package org.deidentifier.arx.kettle.attribut;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.xml.XMLHandler;
import org.w3c.dom.Node;

public class ARXFields {

	private String name;
	private String type;
	private int transformation;
	private int minimumGen;//-1 for all
	private int maximumGen;//-1 for all
	private String functionMicro;
	private boolean missingDataMicro;
	private String hierarchie;
	private double attributeWeight;
	private boolean lDiversityEnable;
	private int lDiversity;
	private int lDiversityVariant;
	private double lDiversityC;
	private boolean tClosenessEnable;
	private double tCloseness;
	private int tClosenessMeasure;
	private boolean dDisclosureEnable;
	private double dDisclosure;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTransformation() {
		return transformation;
	}

	public void setTransformation(int transformation) {
		this.transformation = transformation;
	}

	public int getMinimumGen() {
		return minimumGen;
	}

	public void setMinimumGen(int minimumGen) {
		this.minimumGen = minimumGen;
	}

	public int getMaximumGen() {
		return maximumGen;
	}

	public void setMaximumGen(int maximumGen) {
		this.maximumGen = maximumGen;
	}

	public String getFunctionMicro() {
		return functionMicro;
	}

	public void setFunctionMicro(String functionMicro) {
		this.functionMicro = functionMicro;
	}

	public boolean isMissingDataMicro() {
		return missingDataMicro;
	}

	public void setMissingDataMicro(boolean missingDataMicro) {
		this.missingDataMicro = missingDataMicro;
	}

	public String getHierarchie() {
		return hierarchie;
	}

	public void setHierarchie(String hierarchie) {
		this.hierarchie = hierarchie;
	}

	public double getAttributeWeight() {
		return attributeWeight;
	}

	public void setAttributeWeight(double attributeWeight) {
		this.attributeWeight = attributeWeight;
	}

	public boolean islDiversityEnable() {
		return lDiversityEnable;
	}

	public void setlDiversityEnable(boolean lDiversityEnable) {
		this.lDiversityEnable = lDiversityEnable;
	}

	public int getlDiversity() {
		return lDiversity;
	}

	public void setlDiversity(int lDiversity) {
		this.lDiversity = lDiversity;
	}

	public int getlDiversityVariant() {
		return lDiversityVariant;
	}

	public void setlDiversityVariant(int lDiversityVariant) {
		this.lDiversityVariant = lDiversityVariant;
	}

	public double getlDiversityC() {
		return lDiversityC;
	}

	public void setlDiversityC(double lDiversityC) {
		this.lDiversityC = lDiversityC;
	}

	public boolean istClosenessEnable() {
		return tClosenessEnable;
	}

	public void settClosenessEnable(boolean tClosenessEnable) {
		this.tClosenessEnable = tClosenessEnable;
	}

	public double gettCloseness() {
		return tCloseness;
	}

	public void settCloseness(double tCloseness) {
		this.tCloseness = tCloseness;
	}

	public int gettClosenessMeasure() {
		return tClosenessMeasure;
	}

	public void settClosenessMeasure(int tClosenessMeasure) {
		this.tClosenessMeasure = tClosenessMeasure;
	}

	public boolean isdDisclosureEnable() {
		return dDisclosureEnable;
	}

	public void setdDisclosureEnable(boolean dDisclosureEnable) {
		this.dDisclosureEnable = dDisclosureEnable;
	}

	public double getdDisclosure() {
		return dDisclosure;
	}

	public void setdDisclosure(double dDisclosure) {
		this.dDisclosure = dDisclosure;
	}

}
