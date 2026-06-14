package khe.banking.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import khe.banking.models.enums.TxnType;

/* UTILITY HELPER CLASS FOR BUILDING 
 * AND SETTING UP REUSABLE FILTER UI COMPONENTS
 */

public class FilterFactory {

	private FilterFactory() {
	}
	
	// =========================
	// TYPE FILTER COLUMN 
	// =========================
	public static <S> Set<TxnType> setupTypeFilter(TableColumn<S, TxnType> col, Runnable onFilterChanged) {
		MenuButton typeFilter = new MenuButton("Type");
		typeFilter.getStyleClass().add("type-filter");

		Set<TxnType> selectedTypes = setupRadioMenuFilter(
				typeFilter, List.of(TxnType.INCOME, TxnType.EXPENSE), onFilterChanged);

		col.setText(null);
		col.setGraphic(typeFilter);

		return selectedTypes;
	}
	
	// =========================
	// RADIO MENU ITEM (ONLY ONE SELECTION)
	// =========================
	public static <T> Set<T> setupRadioMenuFilter(MenuButton menu, 
			Collection<T> values, Runnable onChanged) {
		menu.getItems().clear();
		
		Set<T> selectedSet = new HashSet<>();
		selectedSet.addAll(values);
		
		ToggleGroup tg = new ToggleGroup();
		
		RadioMenuItem allItem = new RadioMenuItem("Select All");
		allItem.setToggleGroup(tg);
		allItem.setSelected(true);
		menu.getItems().add(allItem);
		
		for (T value : values) {
			RadioMenuItem item = new RadioMenuItem(FormatUtil.toTitleCase(String.valueOf(value)));
			item.setToggleGroup(tg);
			item.setUserData(value);
			menu.getItems().add(item);			
		}
		
		tg.selectedToggleProperty().addListener((obs, oldVal, selected) -> {
			selectedSet.clear();
			
			if(selected == null || selected == allItem) {
				allItem.setSelected(true);
				selectedSet.addAll(values);
			} else {
				@SuppressWarnings("unchecked")
				T selectedVal = (T) selected.getUserData();
				selectedSet.add(selectedVal);
			}
			
			run(onChanged);			
		});
		
		return selectedSet;
	}

	// =========================
	// MULTI-SELECT (CHECK MENU ITEM) 
	// =========================
	public static <T> Set<T> setupMultiSelectMenu(MenuButton menu, 
			Collection<T> values, String defaultText, Runnable onChanged) {
		menu.getItems().clear();
		
		Set<T> selectedValues = new HashSet<>();
		List<CheckMenuItem> valueItems = new ArrayList<>();

		CheckMenuItem selectAllItem = new CheckMenuItem("Select All");
		selectAllItem.setSelected(false);

		for (T value : values) {
			CheckMenuItem item = new CheckMenuItem(FormatUtil.toTitleCase(String.valueOf(value)));
			item.setSelected(false);
			valueItems.add(item);
			menu.getItems().add(item);

			item.selectedProperty().addListener((obs, oldVal, selected) -> {
				if (selected) {
					selectedValues.add(value);
				} else {
					selectedValues.remove(value);
				}
				
				updateMenuText(menu, selectedValues, values.size(), defaultText);				
				run(onChanged);
			});
		}

		List<BooleanProperty> itemProperties = valueItems.stream()
				.map(CheckMenuItem::selectedProperty).toList();

		syncSelectAll(selectAllItem.selectedProperty(), itemProperties, () -> {
			updateMenuText(menu, selectedValues, values.size(), defaultText);
			run(onChanged);
		});

		menu.getItems().add(0, selectAllItem);

		updateMenuText(menu, selectedValues, values.size(), defaultText);

		return selectedValues;
	}

	// =========================
	// SELECT ALL SYNC (for CheckMenuItem, CheckBox)
	// =========================
	public static void syncSelectAll(BooleanProperty selectAll, 
			List<? extends BooleanProperty> itemProperties, Runnable onChanged) {
		final boolean[] updating = { false };

		selectAll.addListener((obs, oldVal, selected) -> {
			if (updating[0]) return;
			updating[0] = true;

			itemProperties.forEach(p -> p.set(selected));
			
			updating[0] = false;
			run(onChanged);
		});
		
		itemProperties.forEach(p -> p.addListener((obs, oldVal, selected) -> {
			if (updating[0]) return;
			updating[0] = true;
			
			selectAll.set(itemProperties.stream().allMatch(BooleanProperty::get));
			
			updating[0] = false;
			run(onChanged);			
		}));
	}

	// =========================
	// UPDATE MENU TEXT
	// =========================
	private static <T> void updateMenuText(MenuButton menu, Set<T> selectedValues, int total, String defaultText) {
		if (selectedValues.isEmpty()) {
			menu.setText(defaultText);
		} else if (selectedValues.size() == total) {
			menu.setText("All " + defaultText);
		} else if (selectedValues.size() == 1) {
			String item = String.valueOf(selectedValues.iterator().next());
			menu.setText(FormatUtil.toTitleCase(item));
		} else if (selectedValues.size() > 1) {
			menu.setText(selectedValues.size() + " " + defaultText);
		}
	}

	// =========================
	// RUNNABLE
	// =========================
	private static void run(Runnable action) {
		if (action != null)
			action.run();
	}

}
