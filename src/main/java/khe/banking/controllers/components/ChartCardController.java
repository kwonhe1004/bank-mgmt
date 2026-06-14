package khe.banking.controllers.components;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import khe.banking.dao.AnalyticsDaoImpl;
import khe.banking.models.User;
import khe.banking.models.enums.AnalyticsType;
import khe.banking.services.AnalyticsService;
import khe.banking.services.AnalyticsServiceImpl;

public class ChartCardController {

	@FXML
    private Label subtitleLabel;
    @FXML
    private StackPane chartContainer;
    
    private final AnalyticsService as = new AnalyticsServiceImpl(new AnalyticsDaoImpl());
    
    private int month = LocalDate.now().getMonthValue() - 1;
    private Month monthName;
    private int year = LocalDate.now().getYear();
    private String inEx = null;
    
    public void setDT(int month, int year, String inEx) {
        if (month > 0) {
            this.month = month;
            monthName = Month.of(month);
        } else if(month == 0) {
        	this.month = month;
        	monthName = null;
        }

        if (year > 0) {
            this.year = year;
        }

        this.inEx = inEx;
    }
    
    public void configure(User u, AnalyticsType at) {
    	switch(at) {
    		case WEEKLY_CASHFLOW,
    			MONTHLY_CASHFLOW,
    			YEARLY_CASHFLOW -> loadLineChart(u, at);
    		case EXPENSE_BY_CATEGORY,
    			INCOME_BY_CATEGORY -> loadPieChart(u, at);
    	}
    }
    
    private void loadLineChart(User u, AnalyticsType at) {
    	CategoryAxis x = new CategoryAxis();
    	NumberAxis y = new NumberAxis();
    	
    	LineChart<String, Number> lineChart = new LineChart<>(x, y);
    	XYChart.Series<String, Number> series = new XYChart.Series<>();
    	
    	switch(at) {
	    	case WEEKLY_CASHFLOW -> {
	    		subtitleLabel.setText(monthName + ", " + year);
                
                Map<String, Double> data = as.getWeeklyCashflowByMonth(u, year, month);
                addLineData(series, data);
//                data.forEach((day, amount) ->
//                	series.getData().add(new XYChart.Data<>(day, amount)));	    		
	    	}
	    	
	    	case MONTHLY_CASHFLOW -> {
	    		subtitleLabel.setText(String.valueOf(year));
	    		Map<String, Double> data;

                if ("INCOME".equals(inEx)) {
                    data = as.getMonthlyIncome(u, year);
                } else {
                    data = as.getMonthlyExpense(u, year);
                }

                addLineData(series, data); 		
	    	}
	    	
	    	case YEARLY_CASHFLOW -> {
	    		subtitleLabel.setText(String.valueOf(year));
                
                Map<String, Double> data = as.getYearlyCashflow(u, year);
                addLineData(series, data);
	    	}
    	}
    	
    	lineChart.getData().add(series);
	    chartContainer.getChildren().setAll(lineChart);    		
    }
    
    private void addLineData(XYChart.Series<String, Number> series, Map<String, Double> data) {
        data.forEach((label, amount) ->
                series.getData().add(new XYChart.Data<>(label, amount)));
    }
    
    private void loadPieChart(User u, AnalyticsType at) {
    	PieChart pieChart = new PieChart();
    	if(month > 0 ) {
    		subtitleLabel.setText(monthName + ", " + year);
    	} else if(month == 0) {
    		subtitleLabel.setText(String.valueOf(year));
    	}    	
    	
        switch (at) {
	        case EXPENSE_BY_CATEGORY -> {	
	            Map<String, Double> data = as.getCategoryExpenses(u, year, month);
	            addPieData(pieChart, data);
	        }
	        
	        case INCOME_BY_CATEGORY -> {	
	        	Map<String, Double> data = as.getCategoryIncome(u, year, month);
	            addPieData(pieChart, data);
	        }
        }
        
	    chartContainer.getChildren().setAll(pieChart);    	
    }
    
    private void addPieData(PieChart pieChart, Map<String, Double> data) {
        data.forEach((label, amount) ->
                pieChart.getData().add(new PieChart.Data(label, amount)));
    }
 

}
