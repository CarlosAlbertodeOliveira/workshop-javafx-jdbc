package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Gui.Util.Alerts;
import Gui.Util.Constraints;
import Gui.Util.Utils;
import db.DbException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;
import javafx.event.ActionEvent;

public class DepartmentFormController implements Initializable {
	

	private Department entity;
	private DepartmentService service;
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private Label lblErrorName;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlerts("error saving department", null, e.getMessage(), AlertType.ERROR);
		}
	}
	 
	private Department getFormData() { 
	    Department obj = new Department();
	    obj.setId(Utils.tryParseToInt(txtId.getText()));

	    String name = txtName.getText().trim();
	    if (name.isEmpty()) {
	        throw new DbException("O nome do departamento não pode estar vazio");
	    }

	    obj.setName(name); 
	    return obj; 
	}

	
	/*
	 * private Department getFormData() { Department obj = new Department();
	 * obj.setId(Utils.tryParseToInt(txtId.getText()));
	 * obj.setName(txtName.getText()); return obj; }
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
		Constraints.setTextFildMaxLength(txtName, 15);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	

}
