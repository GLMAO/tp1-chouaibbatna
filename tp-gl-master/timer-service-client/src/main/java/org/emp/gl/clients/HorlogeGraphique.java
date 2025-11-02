package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;

import javax.swing.*;
import java.awt.*;

public class HorlogeGraphique extends JFrame implements TimerChangeListener {

    private final TimePanel timePanel;
    private TimerService timerService;

    public HorlogeGraphique(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 220);             
        setResizable(false);
        setLocationRelativeTo(null);

        timePanel = new TimePanel();
        add(timePanel);

        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void setTimerService(TimerService timerService) {
        if (this.timerService != null) {
            this.timerService.removeTimeChangeListener(this);
        }
        this.timerService = timerService;
        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);

            SwingUtilities.invokeLater(this::updateDisplayFromService);
        }
    }

    @Override
    public void propertyChange(String prop, Object oldValue, Object newValue) {
        if (TimerChangeListener.SECONDE_PROP.equals(prop)) {
            SwingUtilities.invokeLater(this::updateDisplayFromService);
        }
    }

    private void updateDisplayFromService() {
        if (timerService == null) return;
        String hh = String.format("%02d", timerService.getHeures());
        String mm = String.format("%02d", timerService.getMinutes());
        String ss = String.format("%02d", timerService.getSecondes());
        timePanel.setTimeText(hh + ":" + mm + ":" + ss);
    }

   
    private static class TimePanel extends JPanel {
        private String timeText = "--:--:--";
        private final Font timeFont;

        public TimePanel() {
            timeFont = new Font("SansSerif", Font.BOLD, 72);
            setBackground(new Color(8, 12, 20));
        }

        public void setTimeText(String t) {
            this.timeText = t;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();

            Graphics2D g2 = (Graphics2D) g.create();
           
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


            Color bg1 = new Color(8, 12, 20);   
            Color bg2 = new Color(6, 24, 54);   
            GradientPaint gp = new GradientPaint(0, 0, bg1, 0, h, bg2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            g2.setFont(timeFont);

            Color neon = new Color(56, 189, 248); 
            Color neonBright = new Color(130, 230, 255); 

            for (int i = 12; i >= 1; i--) {
                float alpha = Math.max(0.02f, 0.0125f * (i)); 
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(new Color(neon.getRed(), neon.getGreen(), neon.getBlue(), (int) (alpha * 255)));
                g2.setFont(timeFont.deriveFont((float) (timeFont.getSize() + i * 1.8)));
          
                FontMetrics fm2 = g2.getFontMetrics();
                int tx = (w - fm2.stringWidth(timeText)) / 2;
                int ty = (h + fm2.getAscent()) / 2 - 6;
                g2.drawString(timeText, tx, ty);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
            g2.setColor(neonBright);
            g2.setFont(timeFont.deriveFont((float) (timeFont.getSize() + 2f)));
            FontMetrics fmInner = g2.getFontMetrics();
            int txInner = (w - fmInner.stringWidth(timeText)) / 2;
            int tyInner = (h + fmInner.getAscent()) / 2 - 6;
            g2.drawString(timeText, txInner, tyInner);

         
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.setColor(new Color(220, 245, 255));

            g2.setFont(timeFont.deriveFont((float) (timeFont.getSize() - 2f)));
            FontMetrics fmC = g2.getFontMetrics();
            int txC = (w - fmC.stringWidth(timeText)) / 2;
            int tyC = (h + fmC.getAscent()) / 2 - 6;
            g2.drawString(timeText, txC, tyC);

            g2.dispose();
        }
    }
}
