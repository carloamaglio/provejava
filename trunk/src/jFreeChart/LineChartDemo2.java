package jFreeChart;

/*
 * Scaricato da: http://www.koders.com/java/fid5E69A632A136F60327F09009803D1F0DD21E119B.aspx
 * 
 *  ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------
 * LineChartDemo2.java
 * -------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LineChartDemo2.java,v 1.1 2005/04/28 16:29:17 harrym_nu Exp $
 *
 * Changes
 * -------
 * 08-Apr-2002 : Version 1 (DG);
 * 10-Nov-2003 : Added axis offsets and changed colors (DG);
 *
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 * @author David Gilbert
 */
public class LineChartDemo2 extends JPanel {
	private static final long serialVersionUID = 1L;

	ChartPanel chartPanel;
	XYDataset dataset;
	JFreeChart chart;

	static class Marker extends ValueMarker {
		private static final long serialVersionUID = 1L;
		XYPlot plot;
		boolean visible;
		public Marker(XYPlot plot, double value) {
			super(value);
			this.plot = plot;
		}
		public boolean setVisible(boolean visible) {
			boolean rv = this.visible;
			if (visible != rv) {
				this.visible = visible;
				if (visible) {
					plot.addRangeMarker(this);
				} else {
					plot.removeRangeMarker(this);
				}
			}
			return rv;
		}
		public boolean toggleVisible() {
			return setVisible(!visible);
		}
	}
	Marker marker;

	public LineChartDemo2() {
		super();
		dataset = createDataset();
		chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);
		setLayout(new BorderLayout());
		add(chartPanel, BorderLayout.CENTER);

		JButton b = new JButton("ON/OFF MARKER");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				marker.toggleVisible();
			}
		});
		add(b, BorderLayout.SOUTH);

		marker = new Marker(chart.getXYPlot(), 5.2);
		marker.setPaint(Color.ORANGE);
		marker.setStroke(new BasicStroke(2));

		marker.setOutlinePaint(Color.BLUE);
		marker.setOutlineStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f));

		marker.setLabelOffset(new RectangleInsets(15, 5, 5, 5));
		marker.setLabelPaint(Color.BLACK);
		marker.setLabelFont(new Font("Aral", Font.BOLD, 14));
		marker.setLabel("5.0");
//		chart.getXYPlot().addDomainMarker(marker);
		marker.setVisible(true);

		XYTextAnnotation a = new XYTextAnnotation("CIAO", 3, 7);
		chart.getXYPlot().addAnnotation(a);
	}

	/**
	 * Creates a sample dataset.
	 * 
	 * @return a sample dataset.
	 */
	private XYDataset createDataset() {
		XYSeries series1 = new XYSeries("First", false);
		series1.add(1.0, 1.0);
		series1.add(5.5, 4.5);
		series1.add(3.0, 3.0);
		series1.add(4.0, 5.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);

		XYSeries series2 = new XYSeries("Second");
		series2.add(1.0, 5.0);
		series2.add(2.0, 7.0);
		series2.add(3.0, 6.0);
		series2.add(4.0, 8.0);
		series2.add(5.0, 4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);

		XYSeries series3 = new XYSeries("Third");
		series3.add(3.0, 4.0);
		series3.add(4.0, 3.0);
		series3.add(5.0, 2.0);
		series3.add(6.0, 3.0);
		series3.add(7.0, 6.0);
		series3.add(8.0, 3.0);
		series3.add(9.0, 4.0);
		series3.add(10.0, 3.0);

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);

		return dataset;
	}

	/**
	 * Creates a chart.
	 * 
	 * @param dataset  the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(XYDataset dataset) {
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart("Line Chart Demo 2", // chart title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		LegendTitle legend = (LegendTitle) chart.getLegend();
		System.out.println("legend is a " + legend.getClass());
		//        legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		//        plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.setDomainCrosshairLockedOnData(true);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairLockedOnData(true);
		plot.setRangeCrosshairVisible(true);
		XYItemRenderer ren = plot.getRenderer();
		System.out.println("renderer is a " + ren.getClass());
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		//        renderer.setPlotShapes(true);
		renderer.setShapesFilled(true);
		renderer.setItemLabelsVisible(true);
		ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
				TextAnchor.BOTTOM_CENTER, TextAnchor.CENTER, Math.PI / 4);
		renderer.setPositiveItemLabelPosition(p);

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE                                               *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
	// * to purchase from Object Refinery Limited:                                *
	// *                                                                          *
	// * http://www.object-refinery.com/jfreechart/guide.html                     *
	// *                                                                          *
	// * Sales are used to provide funding for the JFreeChart project - please    * 
	// * support us so that we can continue developing free software.             *
	// ****************************************************************************

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		LineChartDemo2 chart = new LineChartDemo2();
		AppFrame demo = new AppFrame("Line Chart Demo 2", chart);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}

	public static class AppFrame extends ApplicationFrame {
		private static final long serialVersionUID = 1L;

		/**
		 * Creates a new demo.
		 *
		 * @param title  the frame title.
		 */
		public AppFrame(String title, JPanel panel) {
			super(title);
			panel.setPreferredSize(new java.awt.Dimension(500, 270));
			setContentPane(panel);
		}
	}
}
