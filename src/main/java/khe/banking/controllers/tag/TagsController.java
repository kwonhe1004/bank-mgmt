package khe.banking.controllers.tag;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.dao.AccountTypeDaoImpl;
import khe.banking.dao.CategoryDaoImpl;
import khe.banking.models.AccountType;
import khe.banking.models.Category;
import khe.banking.models.Category.CategoryType;
import khe.banking.services.AccountTypeService;
import khe.banking.services.AccountTypeServiceImpl;
import khe.banking.services.CategoryService;
import khe.banking.services.CategoryServiceImpl;

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
	
	private final ObservableList<Category> cList = FXCollections.observableArrayList();
	private final ObservableList<AccountType> atList = FXCollections.observableArrayList();
	
	private final CategoryService cs = new CategoryServiceImpl(new CategoryDaoImpl());
	private final AccountTypeService ats = new AccountTypeServiceImpl(new AccountTypeDaoImpl());
	
	public void initialize() {
		setupTable();
		loadData();
	}
	
	private void setupTable() {
		categoryTable.setItems(cList);
		c_idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		c_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		c_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		typeTable.setItems(atList);
		at_idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		at_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		interestCol.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
		feeCol.setCellValueFactory(new PropertyValueFactory<>("monthlyFee"));
		limitCol.setCellValueFactory(new PropertyValueFactory<>("withdrawalLimit"));
		minCol.setCellValueFactory(new PropertyValueFactory<>("minimumBalance"));
	}
	
	private void loadData() {
		cList.setAll(cs.getAllCategories());
		atList.setAll(ats.getAllAccountTypes());
	}
	
	@FXML
	private void newCategory() {
		
	}
	
	@FXML
	private void newType() {
		
	}
	
	

}
