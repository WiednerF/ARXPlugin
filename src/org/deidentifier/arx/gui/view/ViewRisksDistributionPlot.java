package org.deidentifier.arx.gui.view;

import java.util.Arrays;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskEstimateBuilderInterruptible;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelSampleRiskDistribution;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.Range;
import org.swtchart.ILineSeries.PlotSymbolType;

import de.linearbits.swt.table.DynamicTable;

import org.swtchart.ISeries.SeriesType;


public class ViewRisksDistributionPlot {
	
	/** Minimal width of a category label. */
    private static final int MIN_CATEGORY_WIDTH = 10;
	
	final private ARXResult result;
	/** View */
    private Composite root;
    
    /** View */
    private Chart            chart;
    

    private Data data;
    private ARXConfiguration config;
    private ARXPopulationModel population;
    private boolean input;
    private DataHandle result2;
    
	public ViewRisksDistributionPlot(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input) {
		this.result2=result2;
		this.result=result;
		this.data=data;
		this.config=config;
		this.population=population;
		this.input=input;
		try{
		this.build(parent);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void build(Composite parent) throws InterruptedException{
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

        // Initialize axes
        ITitle yAxisTitle = yAxis.getTitle();
        yAxisTitle.setText(Resources.getMessage("ViewRisksClassDistributionPlot.0")); //$NON-NLS-1$
        xAxisTitle.setText(Resources.getMessage("ViewRisksClassDistributionPlot.1")); //$NON-NLS-1$
        chart.setEnabled(false);
        updateCategories();
     // Enable/disable
        final RiskEstimateBuilder builder;
        if(this.input!=true){
        	builder=this.data.getHandle().getRiskEstimator(population);//TODO Error Failure   
        }else{
        	builder=this.result2.getRiskEstimator(population);
        }
        if(builder!=null){
        	 double[] frequencies;
             double[] cumulative;
             double[] threshold;
             String[] labels;
             double   max     = 0d;
             // Perform work
             RiskModelSampleRiskDistribution model = builder.getSampleBasedRiskDistribution();

             // Create array
             frequencies = model.getFractionOfRecordsForRiskThresholds();
             cumulative = model.getFractionOfRecordsForCumulativeRiskThresholds();
             threshold = new double[frequencies.length];
             double enforced = model.getRiskThreshold();
             labels = new String[frequencies.length];
             max = 0d;
             for (int i = 0; i < frequencies.length; i++) {
                 frequencies[i] *= 100d;
                 cumulative[i] *= 100d;
                 if (enforced != 1d && enforced <= model.getAvailableRiskThresholds()[i]) {
                     threshold[i] = 100d;
                 }
                 max = Math.max(max, frequencies[i]);
                 max = Math.max(max, cumulative[i]);
                 labels[i] = String.valueOf(SWTUtil.getPrettyString(model.getAvailableRiskThresholds()[i] * 100d));
             }
             labels[0] = "<=" + SWTUtil.getPrettyString(1e-6); //$NON-NLS-1$
             
             // TODO: Ugly hack
             frequencies = insertToBack(frequencies, frequencies[frequencies.length-1]);
             cumulative = insertToBack(cumulative, cumulative[cumulative.length-1]);
             threshold = insertToBack(threshold, threshold[threshold.length-1]);
             labels = insertToBack(labels, "");
             
          // Update chart
             chart.setRedraw(false);

             ISeriesSet seriesSet = chart.getSeriesSet();

             ILineSeries series1 = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, Resources.getMessage("ViewRisksClassDistributionPlot.3")); //$NON-NLS-1$
             series1.getLabel().setVisible(false);
             series1.getLabel().setFont(chart.getFont());
             series1.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
             series1.setYSeries(cumulative);
             series1.setAntialias(SWT.ON);
             series1.setSymbolType(PlotSymbolType.NONE);
             series1.enableStep(true);
             series1.enableArea(true);
             
             ILineSeries series2 = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, Resources.getMessage("ViewRisksClassDistributionPlot.2")); //$NON-NLS-1$
             series2.getLabel().setVisible(false);
             series2.getLabel().setFont(chart.getFont());
             series2.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
             series2.setYSeries(frequencies);
             series2.setSymbolType(PlotSymbolType.NONE);
             series2.enableStep(true);
             series2.enableArea(true);

             ILineSeries series3 = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, Resources.getMessage("ViewRisksClassDistributionPlot.4")); //$NON-NLS-1$
             series3.getLabel().setVisible(false);
             series3.getLabel().setFont(chart.getFont());
             series3.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
             series3.setYSeries(threshold);
             series3.setAntialias(SWT.ON);
             series3.setSymbolColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
             series3.setSymbolType(PlotSymbolType.NONE);
             series3.enableStep(true);
             series3.enableArea(true);
             
             seriesSet.bringToFront(Resources.getMessage("ViewRisksClassDistributionPlot.2")); //$NON-NLS-1$
             seriesSet.bringToFront(Resources.getMessage("ViewRisksClassDistributionPlot.4")); //$NON-NLS-1$
             
             chart.getLegend().setVisible(true);
             chart.getLegend().setPosition(SWT.TOP);

             IAxisSet axisSetI = chart.getAxisSet();

             IAxis yAxisI = axisSetI.getYAxis(0);
             yAxisI.setRange(new Range(0d, max));

             IAxis xAxisI = axisSetI.getXAxis(0);
             xAxisI.setCategorySeries(labels);
             xAxisI.adjustRange();
             updateCategories();

             chart.updateLayout();
             chart.update();
             chart.setRedraw(true);
             
        }
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

    /**
     * Insert to front
     * @param array
     * @param value
     * @return
     */
    private double[] insertToBack(double[] array, double value) {
        double[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length-1] = value;
        return result;
    }

    /**
     * Insert to front
     * @param array
     * @param value
     * @return
     */
    private String[] insertToBack(String[] array, String value) {
        String[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length-1] = value;
        return result;
    }


}
