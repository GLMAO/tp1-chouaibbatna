package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

public class Horloge implements TimerChangeListener {

    private String name;
    private TimerService timerService;

    public Horloge(String name) {
        this.name = name;
        System.out.println("Horloge " + name + " initialized!");
    }

    // setter d'injection du service (on s'inscrit ici)
    public void setTimerService(TimerService timerService) {
        if (this.timerService != null) {
            // si déjà inscrit à un ancien service, se désinscrire pour éviter doublons
            this.timerService.removeTimeChangeListener(this);
        }
        this.timerService = timerService;
        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }
    }

    public void afficherHeure() {
        if (timerService != null) {
            System.out.println(name + " affiche " +
                String.format("%02d:%02d:%02d",
                              timerService.getHeures(),
                              timerService.getMinutes(),
                              timerService.getSecondes()));
        }
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        // n'afficher que sur changement de seconde (selon l'interface)
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            afficherHeure();
        }
    }
}
