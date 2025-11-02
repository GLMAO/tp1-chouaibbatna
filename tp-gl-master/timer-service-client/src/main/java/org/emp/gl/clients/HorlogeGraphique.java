package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import java.awt.*;

public class HorlogeGraphique extends JFrame implements TimerChangeListener {

    private TimerService timerService;
    private JLabel labelHeure;

    public HorlogeGraphique(String title) {
        super(title);

        // Configuration de la fenêtre
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Label d'affichage de l'heure
        labelHeure = new JLabel("--:--:--", SwingConstants.CENTER);
        labelHeure.setFont(new Font("Arial", Font.BOLD, 40));

        add(labelHeure, BorderLayout.CENTER);
        setVisible(true);
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
            SwingUtilities.invokeLater(() -> updateDisplay());
        }
    }

    private void updateDisplay() {
        if (timerService != null) {
            String heure = String.format("%02d:%02d:%02d",
                    timerService.getHeures(),
                    timerService.getMinutes(),
                    timerService.getSecondes());

            labelHeure.setText(heure);
        }
    }
}
