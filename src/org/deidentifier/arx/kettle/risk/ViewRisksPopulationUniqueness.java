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
package org.deidentifier.arx.kettle.risk;

import java.util.Arrays;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.common.SWTUtil;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.Range;
import org.swtchart.ILineSeries.PlotSymbolType;

import org.swtchart.ISeries.SeriesType;

/**
 * Generates the Different Plots for the Population Uniqueness Modells
 * @author Florian Wiedner
 * @category Risk
 * @since 1.7
 * @version 1.0
 *
 */
public class ViewRisksPopulationUniqueness {
	
	 /** Minimal width of a category label. */
    private static final int           MIN_CATEGORY_WIDTH = 10;

    /** Labels for the plot. */
    private static final double[]      POINTS             = getPoints();

    /** Labels for the plot. */
    private static final String[]      LABELS             = getLabels(POINTS);
	
    /**
     * The DataHandle for In- and Output
     */
    private DataHandle result;

	/** View */
    private Composite root;
    
    /** View */
    private Chart            chart;
    
    /**
     * The PopulationModel of the ARX at the Output
     */
    private ARXPopulationModel population;
    /**
     * If use all or only one Population Uniqueness Models
     */
    private boolean all;
    
    /**
     * Creates the PopulationUniqueness Risk Plot
     * @param parent The Parent Composite
     * @param result The Result Data Handle
     * @param population The PopulationModel
     * @param all If use all Population Uniqueness Models or only one
     */
	public ViewRisksPopulationUniqueness(final Composite parent,DataHandle result,ARXPopulationModel population,boolean all) {
		this.result=result;
		this.population=population;
		this.all=all;
		this.build(parent);
	}
	
	/**
	 * Creates, builds the View of the PopulationUniqueness Model
	 * @param parent The Parent Composite
	 */
	private void build(Composite parent){
		this.root = new Composite(parent, SWT.NONE);
        this.root.setLayout(new FillLayout());
        this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

             chart = new Chart(root, SWT.NONE);
        chart.setOrientation(SWT.HORIZONTAL);
        
        // Show/Hide axis
        chart.addControlListener(new ControlAdapter(){
            @Override
            public void controlResized(ControlEvent arg0) {
                updateCategories();
            }
        });
        
        // Update font
        FontData[] fd = chart.getFont().getFontData();
        fd[0].setHeight(8);
        final Font font = new Font(chart.getDisplay(), fd[0]);
        chart.setFont(font);
        chart.addDisposeListener(new DisposeListener(){
            public void widgetDisposed(DisposeEvent arg0) {
                if (font != null && !font.isDisposed()) {
                    font.dispose();
                }
            } 
        });
        
        // Update title
        ITitle graphTitle = chart.getTitle();
        graphTitle.setText(""); //$NON-NLS-1$
        graphTitle.setFont(chart.getFont());
        
        // Set colors
        chart.setBackground(root.getBackground());
        chart.setForeground(root.getForeground());
        
        // OSX workaround
        if (System.getProperty("os.name").toLowerCase().contains("mac")){ //$NON-NLS-1$ //$NON-NLS-2$
            int r = chart.getBackground().getRed()-13;
            int g = chart.getBackground().getGreen()-13;
            int b = chart.getBackground().getBlue()-13;
            r = r>0 ? r : 0;
            r = g>0 ? g : 0;
            r = b>0 ? b : 0;
            final Color background = new Color(chart.getDisplay(), r, g, b);
            chart.setBackground(background);
            chart.addDisposeListener(new DisposeListener(){
                public void widgetDisposed(DisposeEvent arg0) {
                    if (background != null && !background.isDisposed()) {
                        background.dispose();
                    }
                } 
            });
        }

        // Initialize axes
        IAxisSet axisSet = chart.getAxisSet();
        IAxis yAxis = axisSet.getYAxis(0);
        IAxis xAxis = axisSet.getXAxis(0);
        ITitle xAxisTitle = xAxis.getTitle();
        xAxisTitle.setText(""); //$NON-NLS-1$
        xAxis.getTitle().setFont(chart.getFont());
        yAxis.getTitle().setFont(chart.getFont());
        xAxis.getTick().setFont(chart.getFont());
        yAxis.getTick().setFont(chart.getFont());
        xAxis.getTick().setForeground(chart.getForeground());
        yAxis.getTick().setForeground(chart.getForeground());
        xAxis.getTitle().setForeground(chart.getForeground());
        yAxis.getTitle().setForeground(chart.getForeground());

        // Initialize y-axis
        ITitle yAxisTitle = yAxis.getTitle();
        yAxisTitle.setText(Resources.getMessage("ViewRisksPlotUniquenessEstimates.0")); //$NON-NLS-1$
        xAxisTitle.setText(Resources.getMessage("ViewRisksPlotUniquenessEstimates.1")); //$NON-NLS-1$
        chart.setEnabled(false);
        updateCategories();
     // Enable/disable
        Thread getData=new Thread(new Runnable(){

			@Override
			public void run() {
	        	 double[] dataPitman;
	             double[] dataZayatz;
	             double[] dataSNB;
	             double[] dataDankar;
	             int idx;
	            
	             dataDankar = new double[POINTS.length];
	             dataPitman = new double[POINTS.length];
	             dataZayatz = new double[POINTS.length];
	              dataSNB = new double[POINTS.length];
	             RiskEstimateBuilder Basebuilder=result.getRiskEstimator(population);
	             int sampleSize=result.getNumRows();
	             RiskEstimateBuilder builder=Basebuilder;
	             for (idx = 0; idx < POINTS.length; idx++) {
	            	 RiskModelHistogram histogram = builder.getEquivalenceClassModel();
	                 ARXPopulationModel population = ARXPopulationModel.create(sampleSize, POINTS[idx]);
	                 builder = result.getRiskEstimator(population, histogram);
	                 
	                 if (idx == 0 && builder.getSampleBasedUniquenessRisk().getFractionOfUniqueTuples() == 0.0d) {
	                     Arrays.fill(dataDankar, 0.0d);
	                     Arrays.fill(dataPitman, 0.0d);
	                     Arrays.fill(dataZayatz, 0.0d);
	                     Arrays.fill(dataSNB, 0.0d);
	                     break;
	                 }
	                 RiskModelPopulationUniqueness populationBasedModel = builder.getPopulationBasedUniquenessRisk();
	                 dataDankar[idx] = populationBasedModel.getFractionOfUniqueTuplesDankar();
	                 dataPitman[idx] = populationBasedModel.getFractionOfUniqueTuplesPitman();
	                 dataZayatz[idx] = populationBasedModel.getFractionOfUniqueTuplesZayatz();
	                 dataSNB[idx] = populationBasedModel.getFractionOfUniqueTuplesSNB();
	             }
	             
	             makePercentage(dataDankar);
	             makePercentage(dataPitman);
	             makePercentage(dataZayatz);
	             makePercentage(dataSNB);
	             
	             ISeriesSet seriesSet = chart.getSeriesSet();
	             createSeries(seriesSet, dataPitman, "Pitman", PlotSymbolType.CIRCLE, new Color(Display.getCurrent(),0,0,0)); //$NON-NLS-1$
	             if(all){
	             createSeries(seriesSet, dataZayatz, "Zayatz", PlotSymbolType.CROSS, new Color(Display.getCurrent(),0,0,255)); //$NON-NLS-1$
	             createSeries(seriesSet, dataSNB, "SNB", PlotSymbolType.DIAMOND, new Color(Display.getCurrent(),0,255,0)); //$NON-NLS-1$
	             createSeries(seriesSet, dataDankar, "Dankar", PlotSymbolType.SQUARE, new Color(Display.getCurrent(),255,0,0)); //$NON-NLS-1$
	             chart.getLegend().setVisible(true);
	             }
	             
	             seriesSet.bringToFront("Pitman"); //$NON-NLS-1$
	             if(all){
	             seriesSet.bringToFront("Zayatz"); //$NON-NLS-1$
	             seriesSet.bringToFront("SNB"); //$NON-NLS-1$

	             seriesSet.bringToFront("Dankar"); //$NON-NLS-1$
	             }
	             
	             IAxisSet axisSetI = chart.getAxisSet();

	             IAxis yAxisI = axisSetI.getYAxis(0);
	             yAxisI.setRange(new Range(0d, 100d));

	             IAxis xAxisI = axisSetI.getXAxis(0);
	             xAxisI.setRange(new Range(0d, LABELS.length));
	             xAxisI.setCategorySeries(LABELS);

	             chart.updateLayout();
	             chart.update();
	             updateCategories();
	             chart.layout();
	             chart.setRedraw(true);
	             chart.redraw();			
			}
        	
        });
        
        getData.run();
        

	}
	
	  /**
    * Creates a series
     * @param seriesSet
     * @param data
     * @param label
     * @param symbol
     * @param color
     */
    private void createSeries(ISeriesSet seriesSet, double[] data, String label, PlotSymbolType symbol, Color color) {
        ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, label); //$NON-NLS-1$
        series.setAntialias(SWT.ON);
        series.getLabel().setVisible(false);
        series.getLabel().setFont(chart.getFont());
        series.setYSeries(data);
        series.setSymbolType(symbol);
        series.setSymbolColor(color);
        series.setLineColor(color);
        series.setXAxisId(0);
        series.setYAxisId(0);
    }
    
    /**
     * Makes the chart show category labels or not.
     */
    private void updateCategories(){
        if (chart != null){
            IAxisSet axisSet = chart.getAxisSet();
            if (axisSet != null) {
                IAxis xAxis = axisSet.getXAxis(0);
                if (xAxis != null) {
                    String[] series = xAxis.getCategorySeries();
                    if (series != null) {
                        boolean enoughSpace = chart.getPlotArea().getSize().x / series.length >= MIN_CATEGORY_WIDTH;
                        xAxis.enableCategory(enoughSpace);
                        xAxis.getTick().setVisible(enoughSpace);
                    }
                }
            }
        }
    }
    /** Convert to percentage
    * @param data
    */
   private void makePercentage(double[] data) {
       for (int i = 0; i < data.length; i++) {
           data[i] = data[i] * 100d;
       }
   }
   /**
    * Creates a set of labels
    * @param points
    * @return
    */
   private static String[] getLabels(double[] points) {
       String[] result = new String[points.length];
       for (int i = 0; i < points.length; i++) {
           result[i] = SWTUtil.getPrettyString(points[i]);
       }
       return result;
   }
   
   /**
    * Creates an array of points
    * @return
    */
   private static double[] getPoints() {
       return new double[]{0.0000001d, 0.000001d, 0.00001d, 
                           0.0001d, 0.001d, 0.01d, 0.1d, 
                           0.2d, 0.3d, 0.4d, 0.5d, 0.6d, 
                           0.7d, 0.8d, 0.9d};
   }
	

}
