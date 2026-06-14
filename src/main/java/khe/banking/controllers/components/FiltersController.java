package khe.banking.controllers.components;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import khe.banking.models.Category;
import khe.banking.models.Transaction;
import khe.banking.models.records.TxnFilter;
import khe.banking.util.FilterFactory;
import khe.banking.util.UIUtil;

public class FiltersController {

	@FXML
	private GridPane root;

	@FXML
	private ComboBox<String> sortFilter;
	
	@FXML
	private Node amountFilter;
	@FXML
	private AmountFilterController amountFilterController;

	@FXML
	private MenuButton categoryFilter;
	private Set<Category> selectedCategories = new HashSet<>();

	@FXML
	private HBox dateBox;
	@FXML
	private DatePicker startDate;
	@FXML
	private DatePicker endDate;

	@FXML
	private Button clearBtn;

	private Runnable onFilterChanged;
	private static final List<String> DEFAULT_SORT = List.of("Newest First", "Oldest First", "Last Modified");

	public void initialize() {
		setupSortFilter();
		setupListeners();
		amountFilterController.setOnFilterChanged(this::notifyChanged);
	}
	
	private void setupSortFilter() {
		sortFilter.getItems().setAll(DEFAULT_SORT);
		sortFilter.setValue(DEFAULT_SORT.get(0));
	}

	private void setupListeners() {
		sortFilter.valueProperty().addListener((obs, oldVal, newVal) -> notifyChanged());
		startDate.valueProperty().addListener((obs, oldVal, newVal) -> notifyChanged());
		endDate.valueProperty().addListener((obs, oldVal, newVal) -> notifyChanged());
	}

	public void setOnFilterChanged(Runnable onFilterChanged) {
		this.onFilterChanged = onFilterChanged;
	}

	public TxnFilter getFilter() {
		return new TxnFilter(
				getSelectedSort(),
				new HashSet<>(selectedCategories), 
				amountFilterController.getMinAmount(),
	            amountFilterController.getMaxAmount(),
				startDate.getValue(), endDate.getValue());
	}

	public void setTransactions(Collection<Transaction> transactions) {
	    amountFilterController.setTransactions(transactions);
	}
	
	public String getSelectedSort() {
		return sortFilter.getValue();
	}

	public void addSortOptions(Collection<String> addedOptions) {
		Set<String> items = new LinkedHashSet<>(DEFAULT_SORT);
		if (addedOptions != null) items.addAll(addedOptions);
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
	            		Category::getId, c -> c, 
	            		(existing, duplicate) -> existing, LinkedHashMap::new))
	            .values().stream().toList();
		
		selectedCategories.clear();
    	selectedCategories = FilterFactory.setupMultiSelectMenu(categoryFilter, 
    			uniqueCategories, "Categories", this::notifyChanged);
	}

	public void showAmountFilter(boolean show) {
		UIUtil.setVisibleAndManaged(amountFilter, show);
	}

	public void showCategoryFilter(boolean show) {
		UIUtil.setVisibleAndManaged(categoryFilter, show);
	}

	public void showDateFilter(boolean show) {
		UIUtil.setVisibleAndManaged(dateBox, show);
	}
	
	@FXML
	private void clearFilters() {
		sortFilter.setValue(DEFAULT_SORT.get(0));
		amountFilterController.clear();
		startDate.setValue(null);
		endDate.setValue(null);
		
		categoryFilter.getItems().forEach(item -> {
			if(item instanceof CheckMenuItem checkItem) checkItem.setSelected(false);
		});
		
		notifyChanged();
	}

	

	private void notifyChanged() {
		if (onFilterChanged != null) onFilterChanged.run();
	}

}
