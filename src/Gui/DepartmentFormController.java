package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Gui.Util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	

	private Department entity;
	
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
	
	public void onbtnSaveAction() {
		System.out.println("onbtnSaveAction");	
	}
	
	public void onbtnCancelAction() {
		System.out.println("onbtnCancelAction");	
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
