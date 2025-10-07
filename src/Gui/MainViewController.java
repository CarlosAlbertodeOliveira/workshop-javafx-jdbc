package Gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import Gui.Util.Alerts;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemDepartment;

	@FXML
	private MenuItem menuItemSeller;

	@FXML
	private MenuItem menuItemAbout;
	
	 @FXML 
	 private MenuItem menuItemRegister;
	 
	 @FXML 
	 private MenuItem menuAbout;
	 
	 

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/Gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentservice(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSellerAction() {
		loadView("/Gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerservice(new SellerService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/Gui/About.fxml", (x) -> {
		});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	   // setBackgroundImageVBox(vboxMain);
		//menuImgs();
	}
	
	private <T> void loadView(String nomeAbsoluto, Consumer<T> initializeAction) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
		try {
			VBox newVbox = loader.load();
			Scene mainScene = Main.getScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());
			T controller = loader.getController();
			initializeAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlerts("IO exception", "Erro ao carregar a tela", e.getMessage(), AlertType.ERROR);
		}
	}
	
	/*
	 * private void setBackgroundImageVBox(VBox vbox) { try { Image image = new
	 * Image(getClass().getResourceAsStream("/img/LojaInformatica.jpg"));
	 * BackgroundImage backgroundImage = new BackgroundImage(image,
	 * BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
	 * BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
	 * BackgroundSize.AUTO, false, false, true, true)); vbox.setBackground(new
	 * Background(backgroundImage)); } catch (Exception e) { e.printStackTrace();
	 * System.out.println("⚠️ Erro ao carregar imagem de fundo: " + e.getMessage());
	 * } }
	 */
	
	
	/*
	 * public void menuImgs() { ImageView imageRegister = new
	 * ImageView("/img/register.png"); ImageView imageAbout = new
	 * ImageView("/img/about.png");
	 * 
	 * menuItemRegister.setGraphic(imageRegister);
	 * menuItemAbout.setGraphic(imageAbout); imageRegister.setFitHeight(30);
	 * imageRegister.setFitWidth(30); imageAbout.setFitHeight(30);
	 * imageAbout.setFitWidth(30);
	 * 
	 * }
	 */
	 


}
