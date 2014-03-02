package AnimatedPoseur.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.gui.PoseDimensionsDialog;

/**
 * This event handler responds to when the user wants to change the
 * pose dimensions.
 * 
 * @author  Richard McKenna
 *           Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class ChangePoseDimensionsHandler implements ActionListener
{
    /**
     * When called, this method starts the dialog that allows
     * the user to enter a pose width and height to change the
     * pose dimensions.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        PoseDimensionsDialog dialog = new PoseDimensionsDialog();
        dialog.initLocation();
        dialog.setVisible(true);
    }
}