package khe.banking.models.records;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public record TableDataView<T>(FilteredList<T> filteredList, SortedList<T> sortedList) {

}
