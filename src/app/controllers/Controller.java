package app.controllers;
import app.models.Model;
import app.views.MainView;

public class Controller {
    private MainView mainView = null;
    private Model model = null;

    public Controller(Model model) throws Exception {
        this.model = model;
        Object settings = model.getSettings();
        Object library = model.getLibrary();
        this.mainView = new MainView(this, settings, library);
    }

    public void showView() {
        mainView.setVisible(true);
    }

    public void notifyStateChanged (Object settings, Object library) {
        model.changeState(settings, library);
    }
}
