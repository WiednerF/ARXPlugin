package org.deidentifier.arx.gui.view;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskEstimateBuilderInterruptible;
import org.deidentifier.arx.risk.RiskModelHistogram;
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
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.Range;

import de.linearbits.swt.table.DynamicTable;

import org.swtchart.ISeries.SeriesType;


public class ViewRisksClassPlot {
	
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
    
	public ViewRisksClassPlot(final Composite parent,ARXResult result,DataHandle result2,Data data, ARXConfiguration config,ARXPopulationModel population,boolean input) {
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
        
        chart.addControlListener(new ControlAdapter(){
            @Override
            public void controlResized(ControlEvent arg0) {
                updateCategories();
            }
        });
        
     // Tool tip
        chart.getPlotArea().addListener(SWT.MouseMove, new Listener() {
  
            public void handleEvent(Event event) {
                IAxisSet axisSet = chart.getAxisSet();
                if (axisSet != null) {
                    IAxis xAxis = axisSet.getXAxis(0);
                    if (xAxis != null) {
                        String[] series = xAxis.getCategorySeries();
                        ISeries[] data = chart.getSeriesSet().getSeries();
                        if (data != null && data.length>0 && series != null) {
                            int x = (int) Math.round(xAxis.getDataCoordinate(event.x));
                            if (x >= 0 && x < series.length) {
                                chart.getPlotArea().setToolTipText("("+series[x]+", "+data[0].getYSeries()[x]+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                return;
                            }
                        }
                    }
                }
                chart.getPlotArea().setToolTipText(null);
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
        yAxisTitle.setText(Resources.getMessage("ViewRisksClassDistributionPlot.0")); //$NON-NLS-1$
        chart.setEnabled(true);
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
             String[] labels; 
             // Perform work
             RiskModelHistogram model = builder.getEquivalenceClassModel();
             int[] distribution = model.getHistogram();

             // Create array
             frequencies = new double[distribution.length/2];
             labels = new String[distribution.length/2];
             
             for (int i = 0; i < distribution.length; i+=2) {
                 frequencies[i/2] = (double) distribution[i+1] / model.getNumClasses() * 100d;
                 labels[i/2] = String.valueOf(distribution[i]);
             }
             
             chart.setRedraw(false);

             ISeriesSet seriesSet = chart.getSeriesSet();
             IBarSeries series = (IBarSeries) seriesSet.createSeries(SeriesType.BAR,
                                                                     "[%]"); //$NON-NLS-1$
             series.getLabel().setVisible(false);
             series.getLabel().setFont(chart.getFont());
             series.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
             series.setYSeries(frequencies);
             chart.getLegend().setVisible(false);

             IAxisSet axisSetI = chart.getAxisSet();

             IAxis yAxisI = axisSetI.getYAxis(0);
             yAxisI.setRange(new Range(0d, 1d));
             yAxisI.adjustRange();

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
	

}
