package AnimatedPoseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user wants to paste the
 * item on the clipboard onto the rendering surface.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class PasteHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will put the item on the clipboard onto the rendering 
     * surface and will make that item the selected item.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        poseurStateManager.pasteSelectedItem();
    }   
}
