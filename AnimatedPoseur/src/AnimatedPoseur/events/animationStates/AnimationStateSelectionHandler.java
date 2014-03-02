
package AnimatedPoseur.events.animationStates;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This handler responds to when the user wants to select the animation 
 * state which are displayed in the list. If such a state is selected, correspondingly
 * all poses of current selected state will be automatically loaded into next 
 * list, and animation toolbar will be enabled.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class AnimationStateSelectionHandler implements ListSelectionListener 
{
    /**
     * Called when the user request to select an animation state via the animation 
     * state list.
     * 
     * @param e The Event Object.
     * 
     */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if(e.getValueIsAdjusting() == false)
        {
            // FIRST OF ALL WE GET THE INDEX OF ITEM WE SELECTED IN LIST
            AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
            PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
            poseurFileManager.selectAnimationState();
        }
    }
}


