package khe.banking.controllers.txn;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.CategoryDaoImpl;
import khe.banking.models.Category;
import khe.banking.models.enums.TxnType;
import khe.banking.services.CategoryServiceImpl;

public class CategoriesController {
	
	@FXML
	private TableView<Category> categoryTable;
	
	@FXML 
	private TableColumn<Category, Integer> idCol;
	@FXML 
	private TableColumn<Category, String> nameCol;
	@FXML 
	private TableColumn<Category, TxnType> typeCol;
	
	private ObservableList<Category> list = FXCollections.observableArrayList();
	private final CategoryServiceImpl cs = new CategoryServiceImpl(new CategoryDaoImpl());
	
	public void initialize() {
		setupTable();		
		loadData();
	}
	
	private void setupTable() {
		categoryTable.setItems(list);
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		typeCol.setCellValueFactory(cell ->	new SimpleObjectProperty<>(cell.getValue().getType()));
	}
	
	private void loadData() {
		list.clear();
		list.setAll(cs.getAllCategories());
	}

}
