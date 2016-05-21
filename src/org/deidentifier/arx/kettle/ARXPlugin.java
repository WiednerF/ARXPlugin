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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * The Whole Logic of Running of the ARXPlugin Step
 * @author Florian Wiedner
 * @category BaseStep
 * @version 1.0
 * @since 1.7
 *
 */
public class ARXPlugin extends BaseStep implements StepInterface {
	/**
	 * The Data of the Row with all the Data of the Object
	 */
	private ARXPluginData data;
	/**
	 * The Meta Data for the Configuration of the Object
	 */
	private ARXPluginMeta meta;
	/**
	 * The RowList of the Object
	 */
	private List<String[]> rowList = new LinkedList<String[]>();
	/**
	 * Length of One Row
	 */
	private int size;

	/**
	 * Generates the Plugin Step
	 * @param s The StepMeta
	 * @param stepDataInterface The Step Interface Data
	 * @param c c
	 * @param t TransMeta Data
	 * @param dis Trans from Previous Step
	 */
	public ARXPlugin(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);

	}

	/*
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#processRow(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (ARXPluginMeta) smi;
		data = (ARXPluginData) sdi;

		Object[] r = getRow(); // get row, blocks when needed!
		if (r == null) // no more input to be expected...
		{
			ARXPluginProcess arx = new ARXPluginProcess(this.rowList, getInputRowMeta().getFieldNames(), meta);
			try {
				this.rowList = arx.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < this.rowList.size(); i++) {
				putRow(this.data.outputRowMeta,this.rowList.get(i));
			}
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;
			this.size=getInputRowMeta().getFieldNames().length;
			rowList.add(getInputRowMeta().getFieldNames());
			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
		}
		String[] tempRow=new String[size];
		
		for (int i = 0; i < tempRow.length; i++) {
			try {
				tempRow[i] = getInputRowMeta().getString(r, i);
			} catch (Exception e) {
				logBasic("Error");
			}
		}
		rowList.add(tempRow);
		if (checkFeedback(getLinesRead()))
			logBasic("Linenr " + getLinesRead()); // Some basic logging every
													// 5000 rows.

		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#init(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (ARXPluginMeta) smi;
		data = (ARXPluginData) sdi;

		return super.init(smi, sdi);
	}

	/*
	 * (non-Javadoc)
	 * @see org.pentaho.di.trans.step.BaseStep#dispose(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
	 */
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (ARXPluginMeta) smi;
		data = (ARXPluginData) sdi;

		super.dispose(smi, sdi);
	}

	/**
	 * Generates the Object and runs it
	 */
	public void run() {
		logBasic("Starting to run...");
		try {
			while (processRow(meta, data) && !isStopped())
				;

		} catch (Exception e) {
			logError("Unexpected error : " + e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logBasic("Finished, processing " + getLinesRead() + " rows");
			markStop();
		}
	}
}
