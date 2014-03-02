package AnimatedPoseur.events.edit;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.state.PoseurStateManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This event handler responds to when the user has clicked on
 * the arrow button, denoting they want to select a shape to
 * edit in some way.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class StartSelectionHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will update its state and the gui.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        poseurStateManager.startShapeSelection();
    }   
}