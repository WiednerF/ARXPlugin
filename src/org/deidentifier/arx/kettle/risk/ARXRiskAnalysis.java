package org.deidentifier.arx.kettle.risk;


import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.gui.view.LayoutRisks;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ARXRiskAnalysis extends Thread {
	private ARXResult result;
	  private Data data;
	    private ARXConfiguration config;
	    private ARXPopulationModel population;
	private Display display;
	private DataHandle result2;
	
	public ARXRiskAnalysis(ARXResult result,DataHandle result2, Data data, ARXConfiguration config,ARXPopulationModel population,Display display) {
		this.result=result;
		this.result2=result2;
		this.data=data;
		this.config=config;
		this.population=population;
		this.display=display;
	}
	
	public void run(){
        
        final Shell shell = new Shell(display.getActiveShell(),SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
        shell.setText("Risk Analytics from the Anonymication with the ARX Tool");
        shell.setImage(Resources.getImage("logo_64.png"));
        
	    shell.setLayout(SWTUtil.createGridLayout(2));
	    new LayoutRisks(shell,result2,data,population);
	      
	          
        shell.pack();
        shell.open();

        // Set up the event loop.
        while (!shell.isDisposed()) {
          if (!display.readAndDispatch()) {
            // If no more entries in event queue
            display.sleep();
          }
        }
        
	}
	


}
