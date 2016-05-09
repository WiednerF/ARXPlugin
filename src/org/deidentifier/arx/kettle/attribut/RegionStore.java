package org.deidentifier.arx.kettle.attribut;

import org.w3c.dom.Node;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.xml.XMLHandler;

public class RegionStore {
	
	private String name;
	private double sampling;
	private long population;

	public RegionStore(String name,double sampling,long population) {
		this.name=name;
		this.sampling=sampling;
		this.population=population;
	}
	
	public RegionStore(Node tempRegion){
		this.name=XMLHandler.getTagValue(tempRegion,"name");
		this.sampling=Double.parseDouble(XMLHandler.getTagValue(tempRegion,"sampling"));
		this.population=Long.parseLong(XMLHandler.getTagValue(tempRegion, "population"));
	}
	
	public String toString(){
		return name+"_"+sampling+"_"+population;
	}
	
	
	  public String getXML() {
		    String retval = "";

		    retval += "      <region>" + Const.CR;
		    retval += "        " + XMLHandler.addTagValue( "name", getName() );
		    retval += "        " + XMLHandler.addTagValue( "sampling", getSampling() );
		    retval += "        " + XMLHandler.addTagValue( "population", getPopulation() );
		    retval += "        </region>" + Const.CR;

		    return retval;
}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSampling() {
		return sampling;
	}

	public void setSampling(double sampling) {
		this.sampling = sampling;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	
	
}
