package com.mmpp.motivationapp.ui.views;

import java.util.ArrayList;

import com.mmpp.motivationapp.backend.Task;
import com.mmpp.motivationapp.controllers.MotivationController;
import com.mmpp.motivationapp.controllers.TaskListManager;
import com.mmpp.motivationapp.ui.SceneManager;
import com.mmpp.motivationapp.ui.SceneView;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainView extends SceneView implements Style{
	private TaskListManager taskManager;
	private MotivationController motivationController;
	
	public MainView(SceneManager sceneManager, TaskListManager taskManager, MotivationController motivationController) {
		super("Main", sceneManager);
		this.taskManager = taskManager;
		this.motivationController = motivationController;
	}
	
	public Pane createTask(Task task, boolean darkBg) {
		HBox root = new HBox();
		root.setPadding(new Insets(5));
		
		if (darkBg) {
			root.setStyle("-fx-background-color:" + COLOR_GRAY + ";");
		}
		
		CheckBox check = new CheckBox();
		check.setSelected(task.getIsComplete());
		check.setOnAction((ActionEvent e) -> {
			task.setComplete(!task.getIsComplete());
		});
		root.getChildren().add(check);
		
		Label priorityLbl = new Label("(" + task.getPriority() + ")");
		priorityLbl.setPadding(new Insets(0, 0, 0, 5));
		priorityLbl.setPrefWidth(40);
		root.getChildren().add(priorityLbl);
		
		Label nameLbl = new Label(task.getName());
		nameLbl.setPrefWidth(250);
		nameLbl.setPadding(new Insets(0, 20, 0, 20));
		root.getChildren().add(nameLbl);
		
		Button editBtn = new Button("Edit");
		editBtn.setPrefHeight(check.getHeight());
		editBtn.setOnAction((ActionEvent e) -> {
			System.out.println("Edit: " + task.getName());
			((TaskView)sceneManager.getView("Task")).setTask(task);
			sceneManager.changeScene("Task");
		});
		root.getChildren().add(editBtn);
		
		Button deleteBtn = new Button("X");
		deleteBtn.setPrefHeight(check.getHeight());
		deleteBtn.setOnAction((ActionEvent e) -> {
			// TODO: delete screen
			System.out.println("Delete: " + task.getName());
			taskManager.removeTodaysTask(task);
			sceneManager.reloadScene();
		});
		root.getChildren().add(deleteBtn);
		HBox.setMargin(deleteBtn, new Insets(0, 0, 0, 5));
		
		return root;
	}

	@Override
	public Scene getScene() {
		BorderPane root = new BorderPane();
		root.getStyleClass().add("body");
		root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		
		// Center pane
		VBox centerPane = new VBox();
		ScrollPane scrollPane = new ScrollPane();
		
		centerPane.getChildren().add(new Text("Tasks:"));
		centerPane.getChildren().add(scrollPane);
		
		VBox taskList = new VBox();
		ArrayList<Task> tasks = taskManager.getTodaysTasks();
		
		if (tasks.size() != 0) {
			for (int i = 0; i < tasks.size(); i++) {
				Pane taskPane = createTask(tasks.get(i), i % 2 == 1);
				
				taskList.getChildren().add(taskPane);
			}
			
		} else {
			Label emptyLbl = new Label("No tasks!");
			emptyLbl.setStyle("-fx-font-size: 20;");
			taskList.getChildren().add(emptyLbl);
		}
		
		scrollPane.setContent(taskList);
		root.setCenter(centerPane);
		BorderPane.setMargin(centerPane, new Insets(20));
		
		// Right pane
		VBox rightPane = new VBox();
		Label motivateLbl = new Label("");
		motivateLbl.setPrefWidth(100);
		motivateLbl.setMaxWidth(100);
		motivateLbl.setWrapText(true);
		
		Button motivateBtn = new Button("Motivate");
		motivateBtn.setOnAction((ActionEvent event) -> {
			motivateLbl.setText(motivationController.getRandomMessage());
		});
		
		rightPane.getChildren().add(motivateBtn);
		rightPane.getChildren().add(motivateLbl);
		
		root.setRight(rightPane);
		BorderPane.setMargin(rightPane, new Insets(20, 20, 0, 0));
		
		// Bottom pane
		StackPane bottomPane = new StackPane();
		
		Button newTaskBtn = new Button("New Task");
		newTaskBtn.setPrefWidth(640);
		newTaskBtn.setMinHeight(40);
		newTaskBtn.setOnAction((ActionEvent event) -> {
			((TaskView)sceneManager.getView("Task")).setTask(null);
			sceneManager.changeScene("Task");
		});
		bottomPane.getChildren().add(newTaskBtn);
		
		root.setBottom(bottomPane);
		BorderPane.setMargin(bottomPane, new Insets(0, 20, 20, 20));
		
		return new Scene(root, 640, 480);
	}
}
