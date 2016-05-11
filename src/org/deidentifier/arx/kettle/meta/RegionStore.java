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

import org.w3c.dom.Node;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;

/**
 * This class contains the Storageformat for one Region.</br>
 * Every region has one of this file. This file also handles the Creating of the
 * Storages for the whole Project with Regions.
 * 
 * @author Florian Wiedner
 * @since 1.5
 * @category ARXPluginMeta
 *
 */
public class RegionStore {

	/**
	 * The name of the Region
	 */
	private String name;
	/**
	 * The Sampling out of the Project for thi Region
	 */
	private double sampling;
	/**
	 * The Population Size for the Complete Region
	 */
	private long population;

	/**
	 * Creates a complete new RegionStore out of the Data Provided. This is for
	 * the Case, when you do not have any Stored Values until now.
	 * 
	 * @param name
	 *            The Name of the Region
	 * @param sampling
	 *            The Sampling Size out of the Population
	 * @param population
	 *            The PopulationSize of this Region
	 * @author Florian Wiedner
	 * @category RegionStore
	 * @since 1.1
	 */
	public RegionStore(String name, double sampling, long population) {
		this.name = name;
		this.sampling = sampling;
		this.population = population;
	}

	/**
	 * This Constructor creates a RegionStore out of the XML Storage Format
	 * 
	 * @param tempRegion
	 *            The Node of the XML Storage
	 * @author Florian Wiedner
	 * @category RegionStore
	 * @since 1.1
	 */
	public RegionStore(Node tempRegion) {
		this.name = XMLHandler.getTagValue(tempRegion, "name");
		this.sampling = Double.parseDouble(XMLHandler.getTagValue(tempRegion, "sampling"));
		this.population = Long.parseLong(XMLHandler.getTagValue(tempRegion, "population"));
	}

	/**
	 * Creates a new RegionStore out of the Database Storage
	 * 
	 * @param rep
	 *            The Database Repository
	 * @param id_step
	 *            The id of the actual Step
	 * @param count
	 *            The number for the Region out of the Region Array
	 * @throws KettleException
	 * @author Florian Wiedner
	 * @category RegionStore
	 * @since 1.1
	 */
	public RegionStore(Repository rep, ObjectId id_step, int count) throws KettleException {
		this.setName(rep.getStepAttributeString(id_step, count, "regionsName"));
		this.setSampling(Double.parseDouble(rep.getStepAttributeString(id_step, count, "regionsSampling")));
		this.setPopulation(Long.parseLong(rep.getStepAttributeString(id_step, count, "regionsPopulation")));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + ": " + sampling + "-" + population;
	}

	/***
	 * Generates the XML Output for the Storage of this Region</br>
	 * 
	 * @author Florian Wiedner
	 * @category RegionStore
	 * @return String The String of the XML Representation
	 */
	public String getXML() {
		String retval = "";

		retval += "      <region>" + Const.CR;
		retval += "        " + XMLHandler.addTagValue("name", getName());
		retval += "        " + XMLHandler.addTagValue("sampling", getSampling());
		retval += "        " + XMLHandler.addTagValue("population", getPopulation());
		retval += "        </region>" + Const.CR;

		return retval;
	}

	/**
	 * This Method saves our RegionStore Configuration in the Database way of
	 * Storage</br>
	 * 
	 * @author Florian Wiedner
	 * @category RegionStorage
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
		rep.saveStepAttribute(id_transformation, id_step, count, "regionsName", this.getName());
		rep.saveStepAttribute(id_transformation, id_step, count, "regionsSampling", this.getSampling());
		rep.saveStepAttribute(id_transformation, id_step, count, "regionsPopulation", this.getPopulation());
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @return The Name of the Region
	 */
	public String getName() {
		return name;
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @param name
	 *            Set a new Name to the Region
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @return The Sampling Fraction for this Region
	 */
	public double getSampling() {
		return sampling;
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @param sampling
	 *            The SamplingFraction for this Region
	 */
	public void setSampling(double sampling) {
		this.sampling = sampling;
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @return The Population Size of this Region
	 */
	public long getPopulation() {
		return population;
	}

	/**
	 * @author Florian Wiedner
	 * @category RegionStorage
	 * @since 1.5
	 * @param population
	 *            The PopulationSize of this Region
	 */
	public void setPopulation(long population) {
		this.population = population;
	}
}
