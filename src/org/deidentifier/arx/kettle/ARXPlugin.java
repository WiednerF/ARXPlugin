package org.deidentifier.arx.kettle;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;





/*
 * Created on 2-jun-2003
 *
 */

public class ARXPlugin extends BaseStep implements StepInterface
{
    private ARXPluginData data;
	private ARXPluginMeta meta;
	private List<String[]> rowList = new ArrayList<String[]>();
	
	
	public ARXPlugin(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis)
	{
		super(s,stepDataInterface,c,t,dis);
	
	}
	
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException
	{
	    meta = (ARXPluginMeta)smi;
	    data = (ARXPluginData)sdi;
	    
		Object[] r=getRow();    // get row, blocks when needed!
		if (r==null)  // no more input to be expected...
		{
			ARXPluginProcess arx=new ARXPluginProcess(this.rowList,getInputRowMeta().getFieldNames(),meta);
			try {
				this.rowList=arx.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(int i=0;i<this.rowList.size();i++){
				processRowOut(this.rowList.get(i));
			}
			setOutputDone();
			return false;
		}
        
        if (first)
        {
            first = false;
            rowList.add(getInputRowMeta().getFieldNames());
            data.outputRowMeta = (RowMetaInterface)getInputRowMeta().clone();
            meta.getFields(data.outputRowMeta, getStepname(), null, null, this);            
        }
       
        
       String[] temp = new String[data.outputRowMeta.size()];
       for(int i=0;i<temp.length;i++){
    	   try{
    		   temp[i]= getInputRowMeta().getString(r, i); 
    	   }catch(Exception e){
    		   logBasic("Error");
    	   }
       }
       rowList.add(temp);		if (checkFeedback(linesRead)) logBasic("Linenr "+linesRead);  // Some basic logging every 5000 rows.
		
		return true;
	}
	
	public void processRowOut(Object[] data) throws KettleStepException{
		Object[] r = new Object[data.length];
		for(int i=0;i<data.length;i++){
			r = RowDataUtil.addValueData(r, i, data[i]);
		}        
	       
		
			putRow(this.data.outputRowMeta, r);     // copy row to possible alternate rowset(s).
	}
		
	public boolean init(StepMetaInterface smi, StepDataInterface sdi)
	{
	    meta = (ARXPluginMeta)smi;
	    data = (ARXPluginData)sdi;
	    
	    return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi)
	{
	    meta = (ARXPluginMeta)smi;
	    data = (ARXPluginData)sdi;

	    super.dispose(smi, sdi);
	}
	
	//
	// Run is were the action happens!
	public void run()
	{
		logBasic("Starting to run...");
		try
		{
			while (processRow(meta, data) && !isStopped());
			
		}
		catch(Exception e)
		{
			logError("Unexpected error : "+e.toString());
            logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		}
		finally
		{
		    dispose(meta, data);
			logBasic("Finished, processing "+linesRead+" rows");
			markStop();
		}
	}
}
