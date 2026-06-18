package khe.banking.controllers.components;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.records.TxnFilter;
import khe.banking.util.FilterFactory;
import khe.banking.util.FormatUtil;
import khe.banking.util.UIUtil;

public class FiltersController {

	@FXML
	private GridPane root;

	@FXML
	private ComboBox<String> sortFilter;
	
	@FXML
	private HBox amountBox;
	@FXML
	private Spinner<BigDecimal> minSpinner;
	@FXML
	private Spinner<BigDecimal> maxSpinner;
	@FXML
	private MenuButton categoryFilter;
	
	@FXML
	private HBox dateBox;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;

	@FXML
	private Button clearBtn;
	
	private Set<Category> selectedCategories = new HashSet<>();
    private Runnable onFilterChanged;

    private BigDecimal defaultMinAmount = BigDecimal.ZERO;
    private BigDecimal defaultMaxAmount = BigDecimal.ZERO;

    private LocalDate defaultStartDate;
    private LocalDate defaultEndDate;

    private CurrencySpinnerValueFactory minFactory;
    private CurrencySpinnerValueFactory maxFactory;

    private boolean updating = false;

    private static final BigDecimal STEP = new BigDecimal("1.00");

    private static final List<String> DEFAULT_SORT = List.of("Newest First", "Oldest First", "Last Modified");

	public void initialize() {
        setupSortFilter();
        setupAmountSpinners();
        setupListeners();
    }

    private void setupSortFilter() {
        sortFilter.getItems().setAll(DEFAULT_SORT);
        sortFilter.setValue(DEFAULT_SORT.get(0));
    }

    private void setupAmountSpinners() {
        minFactory = new CurrencySpinnerValueFactory(
                defaultMinAmount,
                defaultMaxAmount,
                defaultMinAmount,
                STEP
        );

        maxFactory = new CurrencySpinnerValueFactory(
                defaultMinAmount,
                defaultMaxAmount,
                defaultMaxAmount,
                STEP
        );

        minSpinner.setValueFactory(minFactory);
        maxSpinner.setValueFactory(maxFactory);

        commitOnFocusLost(minSpinner);
        commitOnFocusLost(maxSpinner);
    }

    private void setupListeners() {
        sortFilter.valueProperty().addListener((obs, oldVal, newVal) -> notifyChanged());

        minSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (updating) return;
            enforceAmountRange();
            notifyChanged();
        });

        maxSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (updating) return;
            enforceAmountRange();
            notifyChanged();
        });

        startDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (updating) return;
            enforceDateRange();
            notifyChanged();
        });

        endDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (updating) return;
            enforceDateRange();
            notifyChanged();
        });
    }

    public void setOnFilterChanged(Runnable onFilterChanged) {
        this.onFilterChanged = onFilterChanged;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        setDefaultAmounts(transactions);
        setDefaultDates(transactions);

        resetAmount();
        resetDate();

        setupDateConstraints();
    }

    private void setDefaultAmounts(Collection<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            defaultMinAmount = BigDecimal.ZERO;
            defaultMaxAmount = BigDecimal.ZERO;
            return;
        }

        defaultMinAmount = transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .map(BigDecimal::abs)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        defaultMaxAmount = transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .map(BigDecimal::abs)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private void setDefaultDates(Collection<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            defaultStartDate = null;
            defaultEndDate = null;
            return;
        }

        defaultStartDate = transactions.stream()
                .map(Transaction::getDate)
                .filter(Objects::nonNull)
                .min(LocalDate::compareTo)
                .orElse(null);

        defaultEndDate = transactions.stream()
                .map(Transaction::getDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    public TxnFilter getFilter() {
        return new TxnFilter(
                getSelectedSort(),
                new HashSet<>(selectedCategories),
                getMinAmount(),
                getMaxAmount(),
                startDate.getValue(),
                endDate.getValue()
        );
    }

    public String getSelectedSort() {
        return sortFilter.getValue();
    }

    public BigDecimal getMinAmount() {
        return minSpinner.getValue() == null ? defaultMinAmount : minSpinner.getValue();
    }

    public BigDecimal getMaxAmount() {
        return maxSpinner.getValue() == null ? defaultMaxAmount : maxSpinner.getValue();
    }

    public void addSortOptions(Collection<String> addedOptions) {
        Set<String> items = new LinkedHashSet<>(DEFAULT_SORT);

        if (addedOptions != null) {
            items.addAll(addedOptions);
        }

        sortFilter.getItems().setAll(items);
        sortFilter.setValue(DEFAULT_SORT.get(0));
    }

    public Set<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setCategories(List<Category> categories) {
        List<Category> uniqueCategories = categories.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Category::getId,
                        c -> c,
                        (existing, duplicate) -> existing,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();

        selectedCategories.clear();

        selectedCategories = FilterFactory.setupMultiSelectMenu(
                categoryFilter,
                uniqueCategories,
                "Categories",
                this::notifyChanged
        );
    }

    public void showAmountFilter(boolean show) {
        UIUtil.setVisibleAndManaged(amountBox, show);
    }

    public void showCategoryFilter(boolean show) {
        UIUtil.setVisibleAndManaged(categoryFilter, show);
    }

    public void showDateFilter(boolean show) {
        UIUtil.setVisibleAndManaged(dateBox, show);
    }

    @FXML
    private void resetAmount() {
        updating = true;

        minFactory.setRange(defaultMinAmount, defaultMaxAmount);
        maxFactory.setRange(defaultMinAmount, defaultMaxAmount);

        minFactory.setValue(defaultMinAmount);
        maxFactory.setValue(defaultMaxAmount);

        updating = false;
        notifyChanged();
    }

    @FXML
    private void resetDate() {
        updating = true;

        startDate.setValue(defaultStartDate);
        endDate.setValue(defaultEndDate);

        updating = false;
        notifyChanged();
    }

    @FXML
    private void clearFilters() {
        sortFilter.setValue(DEFAULT_SORT.get(0));

        resetAmount();
        resetDate();

        categoryFilter.getItems().forEach(item -> {
            if (item instanceof CheckMenuItem checkItem) {
                checkItem.setSelected(false);
            }
        });

        notifyChanged();
    }

    private void enforceAmountRange() {
        updating = true;

        BigDecimal minValue = getMinAmount();
        BigDecimal maxValue = getMaxAmount();

        if (minValue.compareTo(maxValue) > 0) {
            maxFactory.setValue(minValue);
        }

        updating = false;
    }

    private void enforceDateRange() {
        updating = true;

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (start != null && end != null && start.isAfter(end)) {
            endDate.setValue(start);
        }

        updating = false;
    }

    private void setupDateConstraints() {
        Callback<DatePicker, DateCell> startFactory = picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                boolean beforeDefault = defaultStartDate != null && date.isBefore(defaultStartDate);
                boolean afterEnd = endDate.getValue() != null && date.isAfter(endDate.getValue());
                boolean afterDefault = defaultEndDate != null && date.isAfter(defaultEndDate);

                setDisable(empty || beforeDefault || afterEnd || afterDefault);
            }
        };

        Callback<DatePicker, DateCell> endFactory = picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                boolean beforeStart = startDate.getValue() != null && date.isBefore(startDate.getValue());
                boolean beforeDefault = defaultStartDate != null && date.isBefore(defaultStartDate);
                boolean afterDefault = defaultEndDate != null && date.isAfter(defaultEndDate);

                setDisable(empty || beforeStart || beforeDefault || afterDefault);
            }
        };

        startDate.setDayCellFactory(startFactory);
        endDate.setDayCellFactory(endFactory);
    }

    private void commitOnFocusLost(Spinner<BigDecimal> spinner) {
        spinner.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                commitSpinnerEditorText(spinner);
            }
        });
    }

    private void commitSpinnerEditorText(Spinner<BigDecimal> spinner) {
        SpinnerValueFactory<BigDecimal> valueFactory = spinner.getValueFactory();

        if (valueFactory == null) return;

        String text = spinner.getEditor().getText();

        BigDecimal value = valueFactory.getConverter().fromString(text);
        valueFactory.setValue(value);
    }

    private void notifyChanged() {
        if (onFilterChanged != null) onFilterChanged.run();
    }

    private static class CurrencySpinnerValueFactory 
            extends SpinnerValueFactory<BigDecimal> {

        private BigDecimal min;
        private BigDecimal max;
        private final BigDecimal step;

        CurrencySpinnerValueFactory(BigDecimal min, BigDecimal max, BigDecimal initialValue, BigDecimal step) {
            this.min = min == null ? BigDecimal.ZERO : min;
            this.max = max == null ? BigDecimal.ZERO : max;
            this.step = step == null ? BigDecimal.ONE : step;

            setConverter(new StringConverter<>() {
                @Override
                public String toString(BigDecimal value) {
                    return FormatUtil.formatCurrency(
                            value == null ? BigDecimal.ZERO : value
                    );
                }

                @Override
                public BigDecimal fromString(String text) {
                    BigDecimal value = FormatUtil.parseCurrency(text);

                    if (value == null) {
                        value = BigDecimal.ZERO;
                    }

                    return clamp(value);
                }
            });

            setValue(clamp(initialValue));
        }

        void setRange(BigDecimal min, BigDecimal max) {
            this.min = min == null ? BigDecimal.ZERO : min;
            this.max = max == null ? BigDecimal.ZERO : max;

            if (this.min.compareTo(this.max) > 0) {
                BigDecimal temp = this.min;
                this.min = this.max;
                this.max = temp;
            }

            setValue(clamp(getValue()));
        }

        @Override
        public void decrement(int steps) {
            BigDecimal value = getValue() == null ? min : getValue();
            BigDecimal amount = step.multiply(BigDecimal.valueOf(steps));

            setValue(clamp(value.subtract(amount)));
        }

        @Override
        public void increment(int steps) {
            BigDecimal value = getValue() == null ? min : getValue();
            BigDecimal amount = step.multiply(BigDecimal.valueOf(steps));

            setValue(clamp(value.add(amount)));
        }

        private BigDecimal clamp(BigDecimal value) {
            if (value == null) return min;

            if (value.compareTo(min) < 0) return min;
            if (value.compareTo(max) > 0) return max;

            return value;
        }
    }

}
