package khe.banking.controllers.report;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import khe.banking.controllers.components.ChartCardController;
import khe.banking.models.User;
import khe.banking.models.enums.AnalyticsType;
import khe.banking.util.HeaderManager;
import khe.banking.util.SessionManager;
import khe.banking.util.ViewData;
import khe.banking.util.ViewLoader;

public class ReportsController {

	@FXML
    private StackPane weeklyPane;
	@FXML
	private ChoiceBox<Month> monthBox;
	@FXML
    private StackPane monthlyPane;
	@FXML
	private ChoiceBox<String> inExBox;
	@FXML
    private StackPane yearlyPane;
	@FXML
	private ChoiceBox<Integer> yearBox;
	
	@FXML
	private Label categoryLabel;
	@FXML
	private ChoiceBox<String> pieType;
	@FXML
	private ChoiceBox<String> pieMonth;
	@FXML
	private ChoiceBox<Integer> pieYear;
	@FXML
    private StackPane categoryPane;
		
	private User u;
	private boolean initialized = false;
	
	public void initialize() {
		HeaderManager.setTitle("REPORTS VIEW");
		u = SessionManager.getCurrentUser();
		setupBoxes();
	    initialized = true;
		loadCharts();		
	}
	
	private void setupBoxes() {
		setupMonthBox();
		setupInExBox();
	    setupYearBox();
	    setupPieBox();
	}
	
	private void loadCharts() {
        loadWeeklyChart();
        loadMonthlyChart();
        loadYearlyChart();
        loadPieChart();
    }
	
	private void loadWeeklyChart() {
        if (monthBox.getValue() == null) {
            return;
        }

        int month = monthBox.getValue().getValue();
        int year = LocalDate.now().getYear();

        loadChart(
                weeklyPane,
                AnalyticsType.WEEKLY_CASHFLOW,
                month,
                year,
                null
        );
    }

    private void loadMonthlyChart() {
        if (inExBox.getValue() == null) {
            return;
        }

        int year = LocalDate.now().getYear();
        String inEx = inExBox.getValue();

        loadChart(
                monthlyPane,
                AnalyticsType.MONTHLY_CASHFLOW,
                0,
                year,
                inEx
        );
    }

    private void loadYearlyChart() {
        if (yearBox.getValue() == null) {
            return;
        }

        int year = yearBox.getValue();

        loadChart(
                yearlyPane,
                AnalyticsType.YEARLY_CASHFLOW,
                0,
                year,
                null
        );
    }
    
    private int getSelectedPieMonth() {
    	String value = pieMonth.getValue();
    	if(value.equals("...")) {
    		return 0;
    	}
    	return Month.valueOf(value.toUpperCase()).getValue();
    }
    
    private void loadPieChart() {
    	int month = getSelectedPieMonth();    	
        int year = pieYear.getValue();
        String type = pieType.getValue();
        
        if(type.equals("EXPENSE")) {
        	categoryLabel.setText("EXPENSE BY CATEGORY");
        	loadChart(categoryPane, AnalyticsType.EXPENSE_BY_CATEGORY, month, year, null);
        } else {
        	categoryLabel.setText("INCOME BY CATEGORY");
        	loadChart(categoryPane, AnalyticsType.INCOME_BY_CATEGORY, month, year, null);
        }
      
    }

    private void loadChart(StackPane pane, AnalyticsType type, int month, int year, String inEx) {
        ViewData<ChartCardController> data =
                ViewLoader.loadView("/fxml/components/ChartCard.fxml");

        ChartCardController controller = data.getController();
        controller.setDT(month, year, inEx);
        controller.configure(u, type);

        pane.getChildren().setAll(data.getView());
    }

    
    // SETUP CHOICEBOXES
    private void setupMonthBox() {
        monthBox.getItems().setAll(Month.values());
        monthBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Month month) {
                return month == null
                        ? ""
                        : month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            }

            @Override
            public Month fromString(String string) {
                return null;
            }
        });

        int m = LocalDate.now().getMonthValue() - 1;
        monthBox.setValue(Month.of(m));
        monthBox.valueProperty().addListener((obs, oldMonth, newMonth) -> {
        	if (!initialized) return;
            loadWeeklyChart();
        });
    }

    private void setupInExBox() {
        inExBox.getItems().setAll("EXPENSE", "INCOME");
        inExBox.setValue("EXPENSE");
        inExBox.valueProperty().addListener((obs, oldValue, newValue) -> {
        	if (!initialized) return;
            loadMonthlyChart();
        });
    }

    private void setupYearBox() {
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 10; year <= currentYear; year++) {
            yearBox.getItems().add(year);
        }

        yearBox.setValue(currentYear);
        yearBox.valueProperty().addListener((obs, oldYear, newYear) -> {
            if (!initialized) return;
            loadYearlyChart();
        });
    }
    
    private void setupPieBox() {
    	// TYPE
    	pieType.getItems().setAll("EXPENSE", "INCOME");
    	pieType.setValue("EXPENSE");
    	pieType.valueProperty().addListener((obs, oldValue, newValue) -> {
    		if (!initialized) return;
    		loadPieChart();
    	});    	
    	
    	// MONTHS
    	pieMonth.getItems().add("...");
    	for(Month month : Month.values()) {
    		pieMonth.getItems().add(month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    	}
    	
    	int m = LocalDate.now().getMonthValue() - 1;
    	String mn = Month.of(m).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    	pieMonth.setValue(mn);
    	pieMonth.valueProperty().addListener((obs, oldMonth, newMonth) -> {
        	if (!initialized) return;
        	loadPieChart();
        });
    	
    	// YEARS
    	int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 10; year <= currentYear; year++) {
            pieYear.getItems().add(year);
        }

        pieYear.setValue(currentYear);
        pieYear.valueProperty().addListener((obs, oldYear, newYear) -> {
            if (!initialized) return;
            loadPieChart();
        });    	
    }
    
	
}
