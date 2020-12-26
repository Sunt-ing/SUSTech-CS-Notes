package observer3;

public class Main {
    public static void main(String[] args) {
        MainPanel mainPanel = new MainPanel();
        SwingFacade.launch(SwingFacade.createTitledPanel("Observer Pattern Example", mainPanel), "");
    }
}
