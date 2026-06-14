package khe.banking.controllers.tag;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import khe.banking.models.AccountType;
import khe.banking.models.Category;
import khe.banking.models.Category.CategoryType;
import khe.banking.models.enums.AccountTypeEnum;
import khe.banking.services.AccountTypeService;
import khe.banking.services.CategoryService;
import khe.banking.services.ServiceFactory;
import khe.banking.util.HeaderManager;
import khe.banking.util.TableFactory;

public class TagsController {
		
	@FXML
	private Accordion accordion;
	@FXML
	private TitledPane atPane;
	
	@FXML
	private TableView<AccountType> typeTable;	
	@FXML 
	private TableColumn<AccountType, Integer> at_idCol;
	@FXML 
	private TableColumn<AccountType, AccountTypeEnum> codeCol;	
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
	
	@FXML
	private TitledPane cPane;
	@FXML
	private TableView<Category> categoryTable;	
	@FXML 
	private TableColumn<Category, Integer> c_idCol;
	@FXML 
	private TableColumn<Category, String> c_nameCol;
	@FXML 
	private TableColumn<Category, CategoryType> c_typeCol;
	
	private final ObservableList<Category> cList = FXCollections.observableArrayList();
	private final ObservableList<AccountType> atList = FXCollections.observableArrayList();
	
	private final CategoryService cs = ServiceFactory.CATEGORY_SERVICE;
	private final AccountTypeService ats = ServiceFactory.ACCOUNT_TYPE_SERVICE;
	
	private AccountType highlight;
	
	public void initialize() {
		HeaderManager.setTitle("TAGS VIEW");
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
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		at_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		interestCol.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
		feeCol.setCellValueFactory(new PropertyValueFactory<>("monthlyFee"));
		limitCol.setCellValueFactory(new PropertyValueFactory<>("withdrawalLimit"));
		minCol.setCellValueFactory(new PropertyValueFactory<>("minimumBalance"));
		
		TableFactory.enableWrapping(interestCol);
		TableFactory.enableWrapping(feeCol);
		TableFactory.enableWrapping(limitCol);
		TableFactory.enableWrapping(minCol);		
	}
	
	private void loadData() {
		cList.setAll(cs.getAllCategories());
		atList.setAll(ats.getAllAccountTypes());
	}
	
	public void setHighlight(AccountType at) {
		this.highlight = at;
		highlightRow();
	}
	
	private void highlightRow() {
		if(highlight == null) {
			return;
		}		
		
		accordion.setExpandedPane(atPane);
		
		for(AccountType at : typeTable.getItems()) {
			if(at.getId() == highlight.getId()) {
				typeTable.getSelectionModel().select(at);
				typeTable.scrollTo(at);
				break;
			}
		}
	}
	
	@FXML
	private void newCategory() {
		
	}
	
	@FXML
	private void newType() {
		
	}
	
	

}
