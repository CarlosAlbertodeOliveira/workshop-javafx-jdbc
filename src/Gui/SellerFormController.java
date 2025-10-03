package Gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import Gui.Util.Alerts;
import Gui.Util.Constraints;
import Gui.Util.Utils;
import Gui.listeners.DataChangeListener;
import db.DbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	private SellerService service;
	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	private ObservableList<Department> obsList;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label lblErrorName;
	
	@FXML
	private Label lblErrorEmail;
	
	@FXML
	private Label lblErrorNascimento;
	
	@FXML
	private Label lblErrorSalario;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}
	
	public void subcribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	@FXML
	public void onbtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		if (service == null) {
			throw new IllegalStateException("service was null :/");
		}
		try {
			entity = getFormData();
			service.saveorUpdate(entity);
			notifySubcribeDataChangeListener();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setMessageError(e.getError());
		} catch (DbException e) {
			Alerts.showAlerts("error saving Seller", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	 
	private void notifySubcribeDataChangeListener() {
		for(DataChangeListener Listener : dataChangeListeners) {
			Listener.onDataChanged();
		}
		
	}

	private Seller getFormData() {
	    Seller obj = new Seller();
	    ValidationException exception = new ValidationException("Validation error");

	    // ID
	    obj.setId(Utils.tryParseToInt(txtId.getText()));

	    // Name
	    if (txtName.getText() == null || txtName.getText().trim().equals("")) {
	        exception.addError("name", "O campo Nome é obrigatório!");
	    }
	    obj.setName(txtName.getText());

	    // Email
	    if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
	        exception.addError("email", "O campo Email é obrigatório!");
	    }
	    obj.setEmail(txtEmail.getText());

	    // BirthDate (DatePicker → java.util.Date)
	    if (dpBirthDate.getValue() == null) {
	        exception.addError("birthDate", "Data de nascimento é obrigatória!");
	    } else {
	        obj.setBirthDate(java.util.Date.from(dpBirthDate.getValue()
	                .atStartOfDay(ZoneId.systemDefault())
	                .toInstant()));
	    }

	    // BaseSalary
	    if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
	        exception.addError("baseSalary", "Salário é obrigatório!");
	    } else {
	        try {
	            obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText().replace(",", ".")));
	        } catch (NumberFormatException e) {
	            exception.addError("baseSalary", "Valor inválido para salário!");
	        }
	    }
	    if (!exception.getError().isEmpty()) {
	        throw exception;
	    }
	    
	    // Department (associa um default para evitar NullPointerException)
	    Department dep = new Department();
	    dep.setId(1); // ajuste o id conforme seu banco
	    obj.setDepartment(dep);

	    // Lança exceções de validação se houver erros
	    if (!exception.getError().isEmpty()) {
	        throw exception;
	    }

	    return obj;
	}
	
	
	/*
	 * private Seller getFormData() { Seller obj = new Seller(); ValidationException
	 * exception = new ValidationException("Validation error");
	 * 
	 * obj.setId(Utils.tryParseToInt(txtId.getText())); if (txtName.getText() ==
	 * null || txtName.getText().trim().equals("")) { exception.addError("name",
	 * "  O campo Departamento Obrigatório!"); } if (exception.getError().size() >
	 * 0) { throw exception; } obj.setName(txtName.getText()); return obj; }
	 */
	 
	 
	@FXML
	public void onbtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();	
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFildInteger(txtId);
		Constraints.setTextFildMaxLength(txtName, 35);
		Constraints.setTextFildMaxLength(txtEmail, 50);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		Constraints.setTextFildDouble(txtBaseSalary);
		initializeComboBoxDepartment();
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		if(entity.getBirthDate() != null) {
		dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else {
			comboBoxDepartment.setValue(entity.getDepartment());	
		}
	}
	
	public void setMessageError(Map<String, String>errors) {
		Set<String> filds = errors.keySet();
		if(filds.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
	}
	
	public void loadAssociatedObjects() {
		if(departmentService == null) {
			throw new IllegalStateException("departmentService was null :/");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}
	
	private void initializeComboBoxDepartment() { 
		 Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() { 
		     @Override 
		     protected void updateItem(Department item, boolean empty) { 
		         super.updateItem(item, empty); 
		         setText(empty ? "" : item.getName()); 
		     } 
		 }; 
		 
		 comboBoxDepartment.setCellFactory(factory); 
		 comboBoxDepartment.setButtonCell(factory.call(null));   
		}
	

}
