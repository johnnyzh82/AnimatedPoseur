package AnimatedPoseur.events.edit;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user wants to adjust the alpha value of current shape,
 * which is the corresponding slider of invisibility of the current selected shape
 * 
 * @author  Yunlong Zhang
 *           CSE 219 Stony Brook
 * @version 1.0
 */
public class AlphaHandler implements ChangeListener
{
    /**
     * This method relays this event to the data manager, which
     * will reset the alpha value to the corresponding value in slider.
     * 
     * @param ce The event object for any item clicked in slider
     */
    @Override
    public void stateChanged(ChangeEvent ce) 
    {
        // FIRST ACCESS POSEURGUI AND ACCESS POSERURSTATEMANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        // GET AN INTEGER VALUE FROM CURRENT USER SELECTION 
        int i = gui.getAlphaTransparency();
        // SET IT INTO CURRENT SHAPE ALPHA VALUE
        poseurStateManager.setAlphaValue(i);
    }   
}