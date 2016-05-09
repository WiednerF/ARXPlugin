package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.resources.ComponentRiskMonitor;
import org.deidentifier.arx.kettle.dialoge.resources.ComponentRiskThresholds;
import org.deidentifier.arx.kettle.dialoge.resources.ComponentTitledSeparator;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleSummary;
import org.deidentifier.arx.risk.RiskModelSampleSummary.JournalistRisk;
import org.deidentifier.arx.risk.RiskModelSampleSummary.MarketerRisk;
import org.deidentifier.arx.risk.RiskModelSampleSummary.ProsecutorRisk;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class ViewRisksReidentificationRisks {
	final private ARXResult result;
	/** View */
    private Composite root;
    

    private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private boolean input;
    
    /** View */
    private static final String  MESSAGE_CAPTION1 = Resources.getMessage("ViewRisksReIdentification.0"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_CAPTION2 = Resources.getMessage("ViewRisksReIdentification.1"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_CAPTION3 = Resources.getMessage("ViewRisksReIdentification.2"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_LABEL1   = Resources.getMessage("ViewRisksReIdentification.3"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_LABEL2   = Resources.getMessage("ViewRisksReIdentification.4"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_LABEL3   = Resources.getMessage("ViewRisksReIdentification.5"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_SHORT1   = Resources.getMessage("ViewRisksReIdentification.6"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_SHORT2   = Resources.getMessage("ViewRisksReIdentification.7"); //$NON-NLS-1$
    /** View */
    private static final String  MESSAGE_SHORT3   = Resources.getMessage("ViewRisksReIdentification.8"); //$NON-NLS-1$

    /** View */
    private ComponentRiskMonitor    prosecutor1;
    /** View */
    private ComponentRiskMonitor    prosecutor2;
    /** View */
    private ComponentRiskMonitor    prosecutor3;
    /** View */
    private ComponentRiskMonitor    journalist1;
    /** View */
    private ComponentRiskMonitor    journalist2;
    /** View */
    private ComponentRiskMonitor    journalist3;
    /** View */
    private ComponentRiskMonitor    marketer1;
    private ComponentRiskThresholds riskThresholds;
    
    private DataHandle result2;
    private LayoutRisks risk;
    
	public ViewRisksReidentificationRisks(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input,LayoutRisks risk) {
		this.result=result;
		this.risk=risk;
		this.result2=result2;
		this.data=data;
		this.config=config;
		this.population=population;
		this.input=input;
		this.riskThresholds=null;
		try{
		this.build(parent);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void build(Composite parent) throws InterruptedException{
		 GridLayout layout = SWTUtil.createGridLayoutWithEqualWidth(3);
	        layout.marginHeight = 0;
	        layout.marginTop = 0;
	        layout.marginBottom = 0;
	        layout.verticalSpacing = 0;
	        layout.makeColumnsEqualWidth = true;
	        
	        this.root = new Composite(parent, SWT.NONE);
	        this.root.setLayout(layout);
        this.root.setLayoutData(SWTUtil.createFillGridDataBoth());
        
        // Prepare
        GridData separatordata = SWTUtil.createFillHorizontallyGridData(true, 3);
        separatordata.verticalIndent = 0;

        // Prosecutor
        ComponentTitledSeparator separator = new ComponentTitledSeparator(root, SWT.NONE);
        separator.setLayoutData(separatordata);
        separator.setText(MESSAGE_CAPTION1);
        separator.setImage(Resources.getImage("prosecutor.png")); //$NON-NLS-1$
        
        prosecutor1 = new ComponentRiskMonitor(root,  MESSAGE_LABEL1, MESSAGE_SHORT1);
        prosecutor2 = new ComponentRiskMonitor(root,  MESSAGE_LABEL2, MESSAGE_SHORT2);
        prosecutor3 = new ComponentRiskMonitor(root,  MESSAGE_LABEL3, MESSAGE_SHORT3);        
        prosecutor1.setLayoutData(SWTUtil.createFillGridData());
        prosecutor2.setLayoutData(SWTUtil.createFillGridData());
        prosecutor3.setLayoutData(SWTUtil.createFillGridData());
        
        // Journalist
        separator = new ComponentTitledSeparator(root, SWT.NONE);
        separator.setLayoutData(separatordata);
        separator.setText(MESSAGE_CAPTION2);
        separator.setImage(Resources.getImage("journalist.png")); //$NON-NLS-1$
        
        journalist1 = new ComponentRiskMonitor(root, MESSAGE_LABEL1, MESSAGE_SHORT1);
        journalist2 = new ComponentRiskMonitor(root, MESSAGE_LABEL2, MESSAGE_SHORT2);
        journalist3 = new ComponentRiskMonitor(root, MESSAGE_LABEL3, MESSAGE_SHORT3);
        journalist1.setLayoutData(SWTUtil.createFillGridData());
        journalist2.setLayoutData(SWTUtil.createFillGridData());
        journalist3.setLayoutData(SWTUtil.createFillGridData());

        // Marketer
        separator = new ComponentTitledSeparator(root, SWT.NONE);
        separator.setLayoutData(separatordata);
        separator.setText(MESSAGE_CAPTION3);
        separator.setImage(Resources.getImage("marketer.png")); //$NON-NLS-1$
        
        marketer1 = new ComponentRiskMonitor(root, MESSAGE_LABEL3, MESSAGE_SHORT3);
        marketer1.setLayoutData(SWTUtil.createFillGridData());
        
        if(!this.input){
        	GridData data = SWTUtil.createFillGridData();
            data.heightHint = 30;
            data.horizontalSpan = 2;
            riskThresholds = new ComponentRiskThresholds(root);
            riskThresholds.setLayoutData(data);
            riskThresholds.setThresholdHighestRisk(0.2d);
            riskThresholds.setThresholdRecordsAtRisk(0.05d);
            riskThresholds.setThresholdSuccessRate(0.05d);
            riskThresholds.addSelectionListenerThresholdHighestRisk(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                        handleThresholdUpdateInMonitors();
                    
                }
            });
            
            riskThresholds.addSelectionListenerThresholdRecordsAtRisk(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                        handleThresholdUpdateInMonitors();
                    
                }
            });
            riskThresholds.addSelectionListenerThresholdSuccessRate(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent arg0) {
                        handleThresholdUpdateInMonitors();
                    
                }
            });
        	//TODO Risk Thresholds for the Output
        }
       
     // Enable/disable
        final RiskEstimateBuilder builder;
        if(this.input!=true){
        	builder=this.data.getHandle().getRiskEstimator(population);
        }else{
        	builder=this.result2.getRiskEstimator(population);
        }
        if(builder!=null){
        	 ProsecutorRisk prosecutor;
             JournalistRisk journalist;
             MarketerRisk   marketer;
             
          // Perform work
             RiskModelSampleSummary summary = builder.getSampleBasedRiskSummary(0.20d);//TODO As Value from the Threshold
             prosecutor = summary.getProsecutorRisk();
             journalist = summary.getJournalistRisk();
             marketer = summary.getMarketerRisk();
             
             // Update views
             prosecutor1.setRisk(prosecutor.getRecordsAtRisk());
             prosecutor1.setThreshold(0.05d);//TODO From RiskThreshold
             prosecutor2.setRisk(prosecutor.getHighestRisk());
             prosecutor2.setThreshold(0.2d);//TODO From RiskThreshold
             prosecutor3.setRisk(prosecutor.getSuccessRate());
             prosecutor3.setThreshold(0.05d);//TODO From RiskThreshold

             // Update views
             journalist1.setRisk(journalist.getRecordsAtRisk());
             journalist1.setThreshold(0.05d);//TODO From RiskThreshold
             journalist2.setRisk(journalist.getHighestRisk());
             journalist2.setThreshold(0.2d);//TODO From RiskThreshold
             journalist3.setRisk(journalist.getSuccessRate());
             journalist3.setThreshold(0.05d);//TODO From RiskThreshold
             
             // Update views
             marketer1.setRisk(marketer.getSuccessRate());
             marketer1.setThreshold(0.05d);//TODO From RiskThreshold

             // Layout
             root.layout();
        	
          
        }}
        
        /**
         * Handles updates of risk thresholds
         */
        private void handleThresholdUpdateInMonitors() {
            prosecutor1.setThreshold(riskThresholds.getThresholdRecordsAtRisk());
            prosecutor2.setThreshold(riskThresholds.getThresholdHighestRisk());
            prosecutor3.setThreshold(riskThresholds.getThresholdSuccessRate());
            journalist1.setThreshold(riskThresholds.getThresholdRecordsAtRisk());
            journalist2.setThreshold(riskThresholds.getThresholdHighestRisk());
            journalist3.setThreshold(riskThresholds.getThresholdSuccessRate());
            marketer1.setThreshold(riskThresholds.getThresholdSuccessRate());
            this.risk.layoutTopRight.reidentification.handleThresholdUpdateInMonitors(riskThresholds.getThresholdRecordsAtRisk(), riskThresholds.getThresholdHighestRisk(), riskThresholds.getThresholdSuccessRate());
        }
	
        /**
         * Handles updates of risk thresholds
         */
        private void handleThresholdUpdateInMonitors(double recordsAtRisk,double highestRisk,double successRat) {
            prosecutor1.setThreshold(recordsAtRisk);
            prosecutor2.setThreshold(highestRisk);
            prosecutor3.setThreshold(successRat);
            journalist1.setThreshold(recordsAtRisk);
            journalist2.setThreshold(highestRisk);
            journalist3.setThreshold(successRat);
            marketer1.setThreshold(successRat);
        }
	
	
	

}
