package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

public class CompteARebours implements TimerChangeListener {

    private TimerService timerService;
    private int compteur;
    private String name;

    public CompteARebours(String name, int start) {
        this.name = name;
        this.compteur = start;
        System.out.println("CompteARebours " + name + " initialized with " + start);
    }

    public void setTimerService(TimerService timerService) {
        if (this.timerService != null) {
            this.timerService.removeTimeChangeListener(this);
        }
        this.timerService = timerService;
        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            // décrémenter à chaque seconde
            if (compteur > 0) {
                compteur--;
                System.out.println(name + " -> " + compteur);
                if (compteur == 0) {
                    // se désinscrire du service
                    if (timerService != null) {
                        timerService.removeTimeChangeListener(this);
                        System.out.println(name + " : terminé et désinscrit.");
                    }
                }
            }
        }
    }
}
