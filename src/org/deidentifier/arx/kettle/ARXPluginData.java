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


import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 *This class is a Container for the Data and the RowMeta output of this step
 * @author Florian Wiedner
 * @since 1.7
 * @category StepDataInterface
 * @version 1.0
 */
public class ARXPluginData extends BaseStepData implements StepDataInterface
{
	/**
	 * The Row Meta Interface Output
	 */
	public RowMetaInterface outputRowMeta;

	/**
	 * Generates the Data and the BaseStepData
	 * @author Florian Wiedner
	 * @since 1.7
	 */
    public ARXPluginData()
	{
		super();
	}
}
