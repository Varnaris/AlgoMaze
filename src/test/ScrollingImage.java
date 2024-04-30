package test;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class ScrollingImage extends BasicGameState {
    private Image backgroundImage;
    private float x;

    public ScrollingImage(int state) {}

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        backgroundImage = new Image("sprite/icon.png"); // Assurez-vous que l'image est dans le dossier racine du projet
        //agrandit l'image pour qu'elle remplisse l'écran
        backgroundImage = backgroundImage.getScaledCopy(gc.getWidth(), gc.getHeight());
        x = 0; // Position initiale en x
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        // Dessine l'image de fond
        backgroundImage.draw(x, 0);
        // Dessine une deuxième instance de l'image à droite pour l'effet de défilement
        backgroundImage.draw(x + backgroundImage.getWidth(), 0);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        // Déplace l'image de fond vers la gauche
        x -= 0.1f * delta; // Delta est utilisé pour rendre le mouvement fluide indépendamment des FPS

        // Si l'image est complètement sortie de l'écran à gauche, réinitialise sa position
        if (x <= -backgroundImage.getWidth()) {
            x = 0;
        }
    }

    @Override
    public int getID() {
        return 1; // Identifiant unique pour cet état de jeu
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new StateBasedGame("Scrolling Image") {
                @Override
                public void initStatesList(GameContainer gc) throws SlickException {
                    addState(new ScrollingImage(1)); // Ajoute l'état de jeu au gestionnaire d'états
                }
            });
            appgc.setDisplayMode(800, 600, false); // Définit la taille de la fenêtre
            appgc.start(); // Lance l'application
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }
}
