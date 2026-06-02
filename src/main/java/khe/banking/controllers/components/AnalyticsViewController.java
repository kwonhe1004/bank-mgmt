package khe.banking.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import khe.banking.models.User;
import khe.banking.models.enums.AnalyticsType;
import khe.banking.utils.ViewData;
import khe.banking.utils.ViewLoader;

public class AnalyticsViewController {
	
	@FXML
    private VBox root;
	@FXML
    private StackPane weeklyPane;
	@FXML
    private StackPane monthlyPane;
	@FXML
    private StackPane expensePane;
	@FXML
    private StackPane incomePane;

	public void loadCharts(User u) {
    	addChartCard(weeklyPane, u, AnalyticsType.WEEKLY_CASHFLOW);
    	addChartCard(monthlyPane, u, AnalyticsType.MONTHLY_CASHFLOW);
    	addChartCard(expensePane, u, AnalyticsType.EXPENSE_BY_CATEGORY);
    	addChartCard(incomePane, u, AnalyticsType.INCOME_BY_CATEGORY);
    }
    
    private void addChartCard(StackPane pane, User u, AnalyticsType at) {
    	ViewData<ChartCardController> data = ViewLoader.loadView("/fxml/components/ChartCard.fxml");
    	ChartCardController controller = data.getController();
    	controller.configure(u, at);
    	pane.getChildren().setAll(data.getView());
    }

}
