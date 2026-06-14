package khe.banking.controllers.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import khe.banking.models.Transaction;
import khe.banking.models.enums.TxnType;
import khe.banking.util.FormatUtil;

public class AmountRangeFilterController {

	@FXML
	private VBox root;
	@FXML
	private Label summaryLabel;

	@FXML
	private HBox distributionBar;
	@FXML
	private Rectangle rangeTrack;
	@FXML
	private Rectangle selectedRangeBar;
	@FXML
	private Label minRangeLabel;
	@FXML
	private Label maxRangeLabel;

	@FXML
	private BarChart<Number, String> amountChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private CategoryAxis yAxis;
	
	@FXML
	private BarChart<Number, String> incomeExpenseChart;
	@FXML
	private NumberAxis xAxis1;
	@FXML
	private CategoryAxis yAxis1;
	
	@FXML
	private TextField minField;
	@FXML
	private TextField maxField;

	@FXML
	private Button under50;
	@FXML
	private Button under100;
	@FXML
	private Button under500;
	@FXML
	private Button over500;

	@FXML
	private Button clearbtn;
	@FXML
	private Button applybtn;

	private Runnable onFilterChanged;

	private BigDecimal defaultMin = BigDecimal.ZERO;
	private BigDecimal defaultMax = BigDecimal.ZERO;

	private final List<BigDecimal> loadedAmounts = new ArrayList<>();
	private final List<Transaction> loadedTransactions = new ArrayList<>();

	public void initialize() {
		setupCurrencyField(minField);
		setupCurrencyField(maxField);

		clearbtn.setDisable(true);

		minField.textProperty().addListener((obs, oldVal, newVal) -> {
			updateClearButton();
			updateSelectedRangeBar();
		});

		maxField.textProperty().addListener((obs, oldVal, newVal) -> {
			updateClearButton();
			updateSelectedRangeBar();
		});
	}

	public void setOnFilterChanged(Runnable onFilterChanged) {
		this.onFilterChanged = onFilterChanged;
	}

	public void setTransactions(Collection<Transaction> transactions) {
		loadedAmounts.clear();
	    loadedTransactions.clear();

		if (transactions != null) {
			loadedTransactions.addAll(transactions);

			loadedAmounts.addAll(transactions.stream()
					.map(Transaction::getAmount).filter(Objects::nonNull)
					.map(BigDecimal::abs).toList());
		}

		if (loadedAmounts.isEmpty()) {
			defaultMin = BigDecimal.ZERO;
			defaultMax = BigDecimal.ZERO;
		} else {
			defaultMin = loadedAmounts.stream()
					.min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

			defaultMax = loadedAmounts.stream()
					.max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
		}

		setFields(defaultMin, defaultMax);
		updateSummary();
		updateMiniDistributionBar();
		updateSelectedRangeBar();
		updateBarChart();
		updateIncomeExpenseDistributionChart();
	}

	public BigDecimal getMinAmount() {
		return FormatUtil.parseCurrency(minField.getText());
	}

	public BigDecimal getMaxAmount() {
		return FormatUtil.parseCurrency(maxField.getText());
	}

	public boolean isDefaultRange() {
		BigDecimal min = getMinAmount();
		BigDecimal max = getMaxAmount();

		return min != null && max != null && min.compareTo(defaultMin) == 0 && max.compareTo(defaultMax) == 0;
	}

	@FXML
	private void apply() {
		BigDecimal min = clamp(getMinAmount(), defaultMin, defaultMax);
		BigDecimal max = clamp(getMaxAmount(), defaultMin, defaultMax);

		if (min.compareTo(max) > 0) {
			BigDecimal temp = min;
			min = max;
			max = temp;
		}

		setFields(min, max);
		runFilterChanged();
	}

	@FXML
	private void clear() {
		setFields(defaultMin, defaultMax);
		runFilterChanged();
	}

	@FXML
	private void select50() {
		setFields(BigDecimal.ZERO, new BigDecimal("50"));
		runFilterChanged();
	}

	@FXML
	private void select100() {
		setFields(new BigDecimal("50"), new BigDecimal("100"));
		runFilterChanged();
	}

	@FXML
	private void select500() {
		setFields(new BigDecimal("100"), new BigDecimal("500"));
		runFilterChanged();
	}

	@FXML
	private void selectOver500() {
		setFields(new BigDecimal("500"), defaultMax);
		runFilterChanged();
	}

	private void updateSummary() {
		summaryLabel.setText(
				"Available: " + FormatUtil.formatCurrency(defaultMin) + " — " + FormatUtil.formatCurrency(defaultMax));

		minRangeLabel.setText(FormatUtil.formatCurrency(defaultMin));
		maxRangeLabel.setText(FormatUtil.formatCurrency(defaultMax));
	}

	private void updateMiniDistributionBar() {
		distributionBar.getChildren().clear();

		int bucketCount = 8;
		int[] buckets = buildBuckets(bucketCount);

		int maxFrequency = 0;
		for (int count : buckets) {
			maxFrequency = Math.max(maxFrequency, count);
		}

		for (int count : buckets) {
			Rectangle bar = new Rectangle();

			double height = maxFrequency == 0 ? 6 : 8 + ((double) count / maxFrequency) * 28;

			bar.setWidth(35);
			bar.setHeight(height);
			bar.getStyleClass().add("mini-distribution-segment");

			distributionBar.getChildren().add(bar);
		}
	}

	private void updateSelectedRangeBar() {
		double totalWidth = rangeTrack.getWidth();

		if (totalWidth <= 0) {
			totalWidth = 360;
		}

		BigDecimal min = getMinAmount();
		BigDecimal max = getMaxAmount();

		if (min == null || max == null || defaultMax.compareTo(defaultMin) == 0) {
			selectedRangeBar.setWidth(totalWidth);
			return;
		}

		double fullMin = defaultMin.doubleValue();
		double fullMax = defaultMax.doubleValue();
		double selectedMin = min.doubleValue();
		double selectedMax = max.doubleValue();

		double totalRange = fullMax - fullMin;

		double startPercent = (selectedMin - fullMin) / totalRange;
		double endPercent = (selectedMax - fullMin) / totalRange;

		startPercent = clampDouble(startPercent, 0, 1);
		endPercent = clampDouble(endPercent, 0, 1);

		double selectedWidth = Math.max(5, (endPercent - startPercent) * totalWidth);

		selectedRangeBar.setWidth(selectedWidth);
		selectedRangeBar.setTranslateX((startPercent * totalWidth) - ((totalWidth - selectedWidth) / 2));
	}

	private void updateBarChart() {
		amountChart.getData().clear();

		int bucketCount = 5;
		int[] buckets = buildBuckets(bucketCount);

		XYChart.Series<Number, String> series = new XYChart.Series<>();

		for (int i = 0; i < bucketCount; i++) {
			String label = bucketLabel(i, bucketCount);
			series.getData().add(new XYChart.Data<>(buckets[i], label));
		}

		amountChart.getData().add(series);
	}
	
	private void updateIncomeExpenseDistributionChart() {
		incomeExpenseChart.getData().clear();
		int bucketCount = 5;
		
		int[] incomeBuckets = buildBucketsByType(bucketCount, TxnType.INCOME);
	    int[] expenseBuckets = buildBucketsByType(bucketCount, TxnType.EXPENSE);
	    
	    configureIncomeExpenseAxis(incomeBuckets, expenseBuckets);

	    XYChart.Series<Number, String> expenseSeries = new XYChart.Series<>();
	    expenseSeries.setName("Expense");

	    XYChart.Series<Number, String> incomeSeries = new XYChart.Series<>();
	    incomeSeries.setName("Income");

	    for (int i = 0; i < bucketCount; i++) {
	        String label = bucketLabel(i, bucketCount);

	        expenseSeries.getData().add(
	                new XYChart.Data<>(-expenseBuckets[i], label)
	        );

	        incomeSeries.getData().add(
	                new XYChart.Data<>(incomeBuckets[i], label)
	        );
	    }
	    incomeExpenseChart.getData().add(expenseSeries);
	    incomeExpenseChart.getData().add(incomeSeries);
	}
	
	private void configureIncomeExpenseAxis(int[] incomeBuckets, int[] expenseBuckets) {

	    int maxIncome = Arrays.stream(incomeBuckets).max().orElse(0);
	    int maxExpense = Arrays.stream(expenseBuckets).max().orElse(0);
	    int maxCount = Math.max(maxIncome, maxExpense);

	    if (maxCount == 0) maxCount = 1;

	    xAxis1.setAutoRanging(false);
	    xAxis1.setLowerBound(-maxCount);
	    xAxis1.setUpperBound(maxCount);
	    xAxis1.setTickUnit(
	            Math.max(1, maxCount / 4.0)
	    );
	    
	    xAxis1.setLabel(null);
	    yAxis1.setLabel(null);

	    xAxis1.setTickLabelsVisible(false);
	    xAxis1.setTickMarkVisible(false);
	    xAxis1.setMinorTickVisible(false);


	    yAxis1.setTickLabelsVisible(false);
	    yAxis1.setTickMarkVisible(false);
	}
	
	private int[] buildBucketsByType(int bucketCount, TxnType type) {
	    int[] buckets = new int[bucketCount];

	    if (loadedTransactions.isEmpty()) {
	        return buckets;
	    }

	    double min = defaultMin.doubleValue();
	    double max = defaultMax.doubleValue();

	    if (min == max) {
	        long count = loadedTransactions.stream().filter(t -> t.getType() == type).count();

	        buckets[0] = (int) count;
	        return buckets;
	    }

	    double bucketSize = (max - min) / bucketCount;

	    for (Transaction tr : loadedTransactions) {
	        if (tr.getType() != type || tr.getAmount() == null) {
	            continue;
	        }

	        double value = tr.getAmount().abs().doubleValue();

	        int index = (int) ((value - min) / bucketSize);

	        if (index >= bucketCount) {
	            index = bucketCount - 1;
	        }

	        buckets[index]++;
	    }

	    return buckets;
	}

	private int[] buildBuckets(int bucketCount) {
		int[] buckets = new int[bucketCount];

		if (loadedAmounts.isEmpty()) {
			return buckets;
		}

		double min = defaultMin.doubleValue();
		double max = defaultMax.doubleValue();

		if (min == max) {
			buckets[0] = loadedAmounts.size();
			return buckets;
		}

		double bucketSize = (max - min) / bucketCount;

		for (BigDecimal amount : loadedAmounts) {
			double value = amount.doubleValue();
			int index = (int) ((value - min) / bucketSize);

			if (index >= bucketCount) {
				index = bucketCount - 1;
			}

			buckets[index]++;
		}

		return buckets;
	}

	private String bucketLabel(int index, int bucketCount) {
		double min = defaultMin.doubleValue();
		double max = defaultMax.doubleValue();

		if (min == max) {
			return FormatUtil.formatCurrency(defaultMin);
		}

		double bucketSize = (max - min) / bucketCount;
		double start = min + index * bucketSize;
		double end = start + bucketSize;

		return String.format("$%.0f-$%.0f", start, end);
	}

	private void setFields(BigDecimal min, BigDecimal max) {
		minField.setText(FormatUtil.formatCurrency(min));
		maxField.setText(FormatUtil.formatCurrency(max));
		updateClearButton();
		updateSelectedRangeBar();
	}

	private void setupCurrencyField(TextField field) {
		final boolean[] updating = { false };

		field.textProperty().addListener((obs, oldText, newText) -> {
			if (updating[0])
				return;

			String digits = newText == null ? "" : newText.replaceAll("\\D", "");

			if (digits.isEmpty()) {
				digits = "0";
			}

			BigDecimal value = new BigDecimal(digits).movePointLeft(2);

			updating[0] = true;
			field.setText(FormatUtil.formatCurrency(value));
			field.positionCaret(field.getText().length());
			updating[0] = false;
		});
	}

	private BigDecimal clamp(BigDecimal value, BigDecimal min, BigDecimal max) {
		if (value == null)
			return min;

		if (value.compareTo(min) < 0)
			return min;
		if (value.compareTo(max) > 0)
			return max;

		return value;
	}

	private double clampDouble(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	private void updateClearButton() {
		clearbtn.setDisable(isDefaultRange());
	}

	private void runFilterChanged() {
		if (onFilterChanged != null) {
			onFilterChanged.run();
		}
	}
}
