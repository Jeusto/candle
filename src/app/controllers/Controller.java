package app.controllers;
import app.models.Model;
import app.views.View;

public class Controller{
    View view = null;
    Model model = null;

    public Controller(View view, Model model) throws Exception {
        this.view = view;
        this.model = model;

        view.addController(this);
        model.addChangeListener(this.view);
    }


    public void viewStateChanged() {
        model.changeModelState();
    }

    public void setViewInitialState() throws Exception {
        view.createContent(model.getCategories(), model.getLocalBooks(), model.getUserSettings());
    }

    public void showView() {
        view.setVisible(true);
    }
}
