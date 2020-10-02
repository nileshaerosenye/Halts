package temp

import javax.swing.JDialog
import javax.swing.JOptionPane

class DialogBox {

    static void main(String[] args) {

        println("Showing a dialog box")

        JOptionPane op = new JOptionPane("This dialog will close after 30 seconds", JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = op.createDialog("server self-test exception");

        // Create a new timer
        Timer timer = new Timer();

        // Perform this task after 30 seconds
        timer.schedule(new TimerTask() {
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }, 30000);

        println("This should continue")

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.setVisible(true);

    }

}
