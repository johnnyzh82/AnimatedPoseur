package AnimatedPoseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user has selected an item
 * on the canvas and has asked to copy it, which should place
 * it on the clipboard.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class CopyHandler implements ActionListener
{
    /**
     * This method relays this event to the state manager, which
     * will update the clipboard accordingly.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        poseurStateManager.copySelectedItem();
    }   
}