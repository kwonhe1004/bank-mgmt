package khe.banking.controllers.report;

import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import khe.banking.dao.ReportDaoImpl;
import khe.banking.models.CategoryReport;
import khe.banking.models.MonthlyReport;
import khe.banking.services.ReportService;
import khe.banking.services.ReportServiceImpl;

public class ReportsController {

	@FXML
	private PieChart expenseChart;
	
	@FXML
	private BarChart<String, Number> monthlyChart;
	
	private final ReportService rs = new ReportServiceImpl(new ReportDaoImpl());
	
	public void initialize() {
		loadExpenseChart();
		loadMonthlyChart();
	}
	
	private void loadExpenseChart() {
		expenseChart.getData().clear();
		for(CategoryReport cr : rs.getExpenseByCategory()) {
			expenseChart.getData().add(
					new PieChart.Data(cr.getCategory().getName(), cr.getTotal().doubleValue()));
		}		
	}
	
	private void loadMonthlyChart() {
		XYChart.Series<String, Number> income = new XYChart.Series<>();
		income.setName("Income");
		
		XYChart.Series<String, Number> expense = new XYChart.Series<>();
		expense.setName("Expense");
		
		for(MonthlyReport mr : rs.getMonthlyReports(2026)) {
			income.getData().add(new XYChart.Data<>(mr.getMonth().name(), mr.getIncome()));
			expense.getData().add(new XYChart.Data<>(mr.getMonth().name(), mr.getExpense()));
		}
		
		monthlyChart.getData().clear();
		monthlyChart.getData().addAll(Arrays.asList(income, expense));
	}
	
}
