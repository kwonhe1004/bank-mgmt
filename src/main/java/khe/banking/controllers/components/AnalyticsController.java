package khe.banking.controllers.components;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import khe.banking.models.User;
import khe.banking.models.enums.AnalyticsType;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;

public class AnalyticsController {
	
	@FXML
    private StackPane monthlyPane;
	@FXML
    private StackPane yearlyPane;
	@FXML
    private StackPane expensePane;
	@FXML
    private StackPane incomePane;
	
	private User u;
	private int month = LocalDate.now().getMonthValue()-1;
	private int year = LocalDate.now().getYear();
	
	public void loadData(User u) {
		this.u = u;
        loadChart(monthlyPane, AnalyticsType.WEEKLY_CASHFLOW, month, year, null);
        loadChart(yearlyPane, AnalyticsType.YEARLY_CASHFLOW, 0, year, null);
        loadChart(expensePane, AnalyticsType.EXPENSE_BY_CATEGORY, 0, year, null);
        loadChart(incomePane, AnalyticsType.INCOME_BY_CATEGORY, 0, year, null);

	}
	
	private void loadChart(StackPane pane, AnalyticsType type, int month, int year, String inEx) {
		ViewData<ChartCardController> data = ViewLoader.loadView("/fxml/components/ChartCard.fxml");
        ChartCardController controller = data.getController();
        controller.setDT(month, year, inEx);
        controller.configure(u, type);
        pane.getChildren().setAll(data.getView());
	}
    

}
