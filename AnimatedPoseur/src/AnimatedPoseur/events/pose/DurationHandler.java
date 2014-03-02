
package AnimatedPoseur.events.pose;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to change the duration of selected pose. Note that
 * the duration can only be a positive integer.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class DurationHandler implements ActionListener{
    /**
     * Called when the user requests to change the duration of the selected pose  via the delete button.
     * 
     * @param ae The event Object
     * 
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestSetDuration();
    }
}