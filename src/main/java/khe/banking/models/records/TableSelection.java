package khe.banking.models.records;

import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableColumn;

public record TableSelection<T>(TableColumn<T, ?> col, Set<Integer> selectedIds, BooleanProperty selectAllProperty) {

}
