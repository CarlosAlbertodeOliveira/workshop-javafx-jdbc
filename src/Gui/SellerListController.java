package Gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Gui.Util.Alerts;
import Gui.Util.Utils;
import Gui.listeners.DataChangeListener;
import application.Main;
import db.DbIntegrityException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tableViewSellerList;
	@FXML
	private TableColumn<Seller, Integer> tableColumnid;
	@FXML
	private TableColumn<Seller, String> tableColumnname;
	@FXML
	private TableColumn<Seller, Seller> tableColumnnEDIT;
	@FXML
	private TableColumn<Seller, Seller> tableColumnnREMOVE;
	@FXML
	private Button btNew;

	private ObservableList<Seller> obslist;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createSellerForm(obj, "/Gui/SellerForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	public void setSellerservice(SellerService service) {
		this.service = service;
	}

	private void initializeNodes() {
		tableColumnid.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnname.setCellValueFactory(new PropertyValueFactory<>("name"));

		Stage stage = (Stage) Main.getScene().getWindow();
		tableViewSellerList.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("service was null :/");
		}
		List<Seller> list = service.findAll();
		obslist = FXCollections.observableArrayList(list);
		tableViewSellerList.setItems(obslist);
		initEditButtons();
		initRemoveButtons();
	}

	private void createSellerForm(Seller obj, String absoluteName, Stage parentStage) {
		/*
		 * FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName)); try
		 * { Pane pane = loader.load();
		 * 
		 * SellerFormController controller = loader.getController();
		 * controller.setSeller(obj); controller.setSellerService(new SellerService());
		 * controller.subcribeDataChangeListener(this); controller.updateFormData();
		 * 
		 * Stage dialogStage = new Stage();
		 * dialogStage.setTitle("Entre com os dados do departamento");
		 * dialogStage.setScene(new Scene(pane)); dialogStage.setResizable(false);
		 * dialogStage.initOwner(parentStage);
		 * dialogStage.initModality(Modality.WINDOW_MODAL); dialogStage.showAndWait();
		 * 
		 * } catch (IOException e) { Alerts.showAlerts("IO EXCEPTION",
		 * "Error loading view", e.getMessage(), AlertType.ERROR);
		 * 
		 * }
		 */

	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Editar");

			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createSellerForm(obj, "/Gui/SellerForm.fxml", Utils.currentStage(event)));

			}
		});
	}

	private void initRemoveButtons() {
		tableColumnnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remover");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}

		});
	}

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja remover?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("service was null :/");
			}
			try {
				service.remove(obj);
				updateTableView();	
			} catch (DbIntegrityException e) {
				Alerts.showAlerts("erro", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
