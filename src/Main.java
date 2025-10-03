import views.MainView;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SYSTEME DE MICROCREDIT ===");
        System.out.println("Initialisation du système...\n");
        
        try {
            MainView mainView = new MainView();
            mainView.showMainMenu();
        } catch (Exception e) {
            System.err.println("Erreur lors du lancement du système: " + e.getMessage());
            e.printStackTrace();
        }
    }
}