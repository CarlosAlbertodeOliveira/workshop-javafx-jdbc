package Gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	private DepartmentService service;
	
	
	@FXML 
	private TableView<Department> tableViewDepartmentList;
	@FXML
	private TableColumn<Department, Integer > tableColumnid;
	@FXML
	private TableColumn<Department, String > tableColumnname;
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obslist;
	
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	public void setDepartmentservice(DepartmentService service) {
		this.service = service;
	}

	private void initializeNodes() {
		tableColumnid.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnname.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getScene().getWindow();
		tableViewDepartmentList.prefHeightProperty().bind(stage.heightProperty());

	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("service was null :/");
		}
		List<Department> list = service.findAll();
		obslist = FXCollections.observableArrayList(list);
		tableViewDepartmentList.setItems(obslist);
	}
}
