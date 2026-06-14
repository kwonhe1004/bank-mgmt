package khe.banking.controllers.components;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import khe.banking.models.Transaction;
import khe.banking.util.FormatUtil;

public class AmountFilterController {
	
	@FXML
	private HBox distributionBar;
	@FXML
	private TextField minField;
	@FXML
	private TextField maxField;	
	@FXML
	private Button clearbtn;
	@FXML
	private Button applybtn;

	private Runnable onFilterChanged;

	private BigDecimal defaultMin = BigDecimal.ZERO;
    private BigDecimal defaultMax = BigDecimal.ZERO;

	public void initialize() {
		setupCurrencyField(minField);
		setupCurrencyField(maxField);
		clearbtn.setDisable(true);

		minField.textProperty().addListener((obs, oldVal, newVal) -> updateClearButton());
		maxField.textProperty().addListener((obs, oldVal, newVal) -> updateClearButton());
	}

	public void setOnFilterChanged(Runnable onFilterChanged) {
		this.onFilterChanged = onFilterChanged;
	}

	public void setTransactions(Collection<Transaction> t) {
		if (t == null || t.isEmpty()) {
			defaultMin = BigDecimal.ZERO;
			defaultMax = BigDecimal.ZERO;
		} else {
			defaultMin = t.stream().map(Transaction::getAmount).filter(Objects::nonNull)
					.map(BigDecimal::abs).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

			defaultMax = t.stream().map(Transaction::getAmount).filter(Objects::nonNull)
					.map(BigDecimal::abs).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
		}
		
		updateDistributionBar(t);
		setFields(defaultMin, defaultMax);
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

        return min != null && max != null 
        		&& min.compareTo(defaultMin) == 0 && max.compareTo(defaultMax) == 0;
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
	public void clear() {
		setFields(defaultMin, defaultMax);
		runFilterChanged();
	}

	private void updateDistributionBar(Collection<Transaction> transactions) {
	    distributionBar.getChildren().clear();

	    int bucketCount = 10;
	    int[] buckets = new int[bucketCount];

	    if (transactions == null || transactions.isEmpty() 
	    		|| defaultMax.compareTo(defaultMin) == 0) { return; }

	    double min = defaultMin.doubleValue();
	    double max = defaultMax.doubleValue();
	    double bucketSize = (max - min) / bucketCount;

	    for (Transaction tr : transactions) {
	        if (tr.getAmount() == null) continue;

	        double amount = tr.getAmount().abs().doubleValue();
	        int index = (int) ((amount - min) / bucketSize);

	        if (index >= bucketCount) index = bucketCount - 1;
	        buckets[index]++;
	    }

	    int maxFrequency = 0;

		for (int count : buckets) { 
			maxFrequency = Math.max(maxFrequency, count);
		}

	    if (maxFrequency == 0) {
	        maxFrequency = 1;
	    }

	    for (int count : buckets) {
	        Rectangle bar = new Rectangle();
	        double height = 6 + ((double) count / maxFrequency) * 35;

	        bar.setWidth(18);
	        bar.setHeight(height);
	        bar.setArcWidth(4);
	        bar.setArcHeight(4);
	        bar.getStyleClass().add("amount-distribution-bar");

	        distributionBar.getChildren().add(bar);
	    }
	}

	
	private void setFields(BigDecimal min, BigDecimal max) {
		minField.setText(FormatUtil.formatCurrency(min));
		maxField.setText(FormatUtil.formatCurrency(max));
		updateClearButton();
	}
	

	private void setupCurrencyField(TextField field) {
	    final boolean[] updating = { false };

	    field.textProperty().addListener((obs, oldText, newText) -> {
	        if (updating[0]) return;

	        String digits = newText == null ? "" : newText.replaceAll("\\D", "");

	        if (digits.isEmpty()) digits = "0";

	        BigDecimal value = new BigDecimal(digits).movePointLeft(2);

	        updating[0] = true;
	        field.setText(FormatUtil.formatCurrency(value));
	        field.positionCaret(field.getText().length());
	        updating[0] = false;
	    });
	}
		
	private BigDecimal clamp(BigDecimal value, BigDecimal min, BigDecimal max) {
	    if (value == null) return min;

	    if (value.compareTo(min) < 0) return min;
	    if (value.compareTo(max) > 0) return max;

	    return value;
	}

	private void updateClearButton() {
		clearbtn.setDisable(isDefaultRange());
	}

	private void runFilterChanged() {
		if (onFilterChanged != null) onFilterChanged.run();
	}


	
}
