package khe.banking.controllers.components;

import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import khe.banking.models.AccountType;
import khe.banking.models.Category;
import khe.banking.models.Category.CategoryType;

public class TagsController {
	
	@FXML
	private TableView<Category> categoryTable;
	
	@FXML 
	private TableColumn<Category, Integer> c_idCol;
	@FXML 
	private TableColumn<Category, String> c_nameCol;
	@FXML 
	private TableColumn<Category, CategoryType> c_typeCol;
	
	@FXML
	private TableView<AccountType> typeTable;	
	@FXML 
	private TableColumn<AccountType, Integer> at_idCol;
	@FXML 
	private TableColumn<AccountType, String> at_nameCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> interestCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> feeCol;
	@FXML 
	private TableColumn<AccountType, Integer> limitCol;
	@FXML 
	private TableColumn<AccountType, BigDecimal> minCol;
	
	public void initialize() {
		
	}
	
	@FXML
	private void newCategory() {
		
	}
	
	@FXML
	private void newType() {
		
	}
	
	

}
