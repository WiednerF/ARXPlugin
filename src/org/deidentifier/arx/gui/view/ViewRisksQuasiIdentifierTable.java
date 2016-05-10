package org.deidentifier.arx.gui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.gui.resources.Resources;
import org.deidentifier.arx.kettle.dialoge.resources.ClipboardHandlerTable;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelAttributes;
import org.deidentifier.arx.risk.RiskModelAttributes.QuasiIdentifierRisk;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import de.linearbits.swt.table.DynamicTable;
import de.linearbits.swt.table.DynamicTableColumn;

public class ViewRisksQuasiIdentifierTable {

	/** View */
	private Composite root;
	private Data data;
	private ARXPopulationModel population;
	private boolean input;
	private DynamicTable table;
	private DataHandle result2;

	public ViewRisksQuasiIdentifierTable(final Composite parent, ARXResult result, DataHandle result2, Data data,
			ARXConfiguration config, ARXPopulationModel population, boolean input) {
		this.result2 = result2;
		this.data = data;
		this.population = population;
		this.input = input;
		try {
			this.build(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void build(Composite parent) throws InterruptedException {
		this.root = new Composite(parent, SWT.NONE);
		this.root.setLayout(new FillLayout());
		this.root.setLayoutData(SWTUtil.createFillGridDataBoth());

		table = SWTUtil.createTableDynamic(root, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setMenu(new ClipboardHandlerTable(table).getMenu());

		DynamicTableColumn c = new DynamicTableColumn(table, SWT.LEFT);
		c.setWidth("70%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.19")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.20")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.21")); //$NON-NLS-1$
		c.setResizable(true);
		c = new DynamicTableColumn(table, SWT.LEFT);
		SWTUtil.createColumnWithBarCharts(table, c);
		c.setWidth("10%"); //$NON-NLS-1$ //$NON-NLS-2$
		c.setText(Resources.getMessage("RiskAnalysis.22")); //$NON-NLS-1$
		c.setResizable(true);
		for (final TableColumn col : table.getColumns()) {
			col.pack();
		}
		SWTUtil.createGenericTooltip(table);
			this.update("SAMPLE_UNIQUENESS");
			root.layout();
	}
	
	//TODO Button Change of Things
	public void update(String attributeRisk){
		// Enable/disable
				final RiskEstimateBuilder builder;
				if (this.input != true) {
					builder = this.data.getHandle().getRiskEstimator(population);
				} else {
					builder = this.result2.getRiskEstimator(population);
				}
				if (builder != null) {

					RiskModelAttributes      risks;
					switch (attributeRisk) {
		            case "SAMPLE_UNIQUENESS":
		                risks = builder.getSampleBasedAttributeRisks();
		                break;
		            case "POPULATION_UNIQUENESS_PITMAN":
		                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.PITMAN);
		                break;
		            case "POPULATION_UNIQUENESS_ZAYATZ":
		                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.ZAYATZ);
		                break;
		            case "POPULATION_UNIQUENESS_SNB":
		                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.SNB);
		                break;
		            case "POPULATION_UNIQUENESS_DANKAR":
		                risks = builder.getPopulationBasedAttributeRisks(PopulationUniquenessModel.DANKAR);
		                break;
		            default:
		                throw new RuntimeException("Invalid risk model"); //$NON-NLS-1$
		            }

					  // Update chart
	                for (final TableItem i : table.getItems()) {
	                    i.dispose();
	                }

	                // For all sizes
	                for (QuasiIdentifierRisk item : risks.getAttributeRisks()) {
	                    createItem(item);
	                }

	                for (final TableColumn col : table.getColumns()) {
	                    col.pack();
	                }

	                table.layout();
	                table.redraw();
					root.layout();
				}
	}
	 /**
     * Creates a table item
     * @param risks
     */
    private void createItem(QuasiIdentifierRisk risks) {
        final TableItem item = new TableItem(table, SWT.NONE);
        List<String> list = new ArrayList<String>();
        list.addAll(risks.getIdentifier());
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1){
                builder.append(", "); //$NON-NLS-1$
            }
        }
        item.setText(0, builder.toString());
        item.setData("1", risks.getFractionOfUniqueTuples());
        item.setData("2", risks.getHighestReidentificationRisk());
        item.setData("3", risks.getAverageReidentificationRisk());
    }

}
