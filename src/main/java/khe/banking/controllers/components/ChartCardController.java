package khe.banking.controllers.components;

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
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private StackPane chartContainer;
    
    private final AnalyticsService as = new AnalyticsServiceImpl(new AnalyticsDaoImpl());
    
    public void configure(User u, AnalyticsType at) {
    	switch(at) {
    		case WEEKLY_CASHFLOW,
    			MONTHLY_CASHFLOW -> loadLineChart(u, at);
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
	    		titleLabel.setText("Weekly Cash Flow");
                subtitleLabel.setText("Last 7 Days");
                
                Map<String, Double> data = as.getWeeklyCashflow(u);
                data.forEach((day, amount) ->
                	series.getData().add(new XYChart.Data<>(day, amount)));	    		
	    	}
	    	
	    	case MONTHLY_CASHFLOW -> {
	    		titleLabel.setText("Monthly Cash Flow");
                subtitleLabel.setText("Last 30 Days");
                
                Map<String, Double> data = as.getMonthlyCashflow(u);
                data.forEach((day, amount) ->
                	series.getData().add(new XYChart.Data<>(day, amount)));	    		
	    	}
    	}
    	
    	lineChart.getData().add(series);
	    chartContainer.getChildren().setAll(lineChart);    		
    }
    
    private void loadPieChart(User u, AnalyticsType at) {
    	PieChart pieChart = new PieChart();

        switch (at) {
	        case EXPENSE_BY_CATEGORY -> {	
	            titleLabel.setText("Expenses By Category");	
	            subtitleLabel.setText("Current Month");
	
	            Map<String, Double> data = as.getCategoryExpenses(u);
	            data.forEach((category, amount) ->
	                pieChart.getData().add(new PieChart.Data(category, amount)));
	        }
	        
	        case INCOME_BY_CATEGORY -> {	
	            titleLabel.setText("Income By Category");	
	            subtitleLabel.setText("Current Month");
	
	            Map<String, Double> data = as.getCategoryIncome(u);
	            data.forEach((category, amount) ->
	                pieChart.getData().add(new PieChart.Data(category, amount)));
	        }
        }
        
	    chartContainer.getChildren().setAll(pieChart);    	
    }
 

}
