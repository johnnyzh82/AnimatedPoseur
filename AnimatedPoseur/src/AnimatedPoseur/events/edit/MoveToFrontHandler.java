
package AnimatedPoseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.shapes.PoseurShape;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event Handler responds to the movement of current selected shape, which moves the selected shape all
 * way to the front. All the other graphs are all located in front of this current selected shape
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class MoveToFrontHandler implements ActionListener{
 /**
  * This method relays this event to the state manager, which
  * will update the poseur class 
  * 
  * @param ae The event object for this button clicked
  */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        // MOVE TO FRONT IS RELATIVELY EASIER, BECAUSE WE CAN SIMPLY REMOVE THE SELECTED SHAPE 
        // AND STORE THIS SELECTED SHAPE TO TAIL OF CURRENT LIST, THUS WE GET THIS SHAPE ON THE  TOP OF 
        // CANVAS
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        // STORE THIS TO A TEMERARY VARIABLE
        PoseurShape currentSelect = poseurStateManager.getSelectedShape();
        // REMOVE THIS SHAPE
        poseurStateManager.getPose().removeShape(currentSelect);
        // ADD TO TAIL
        poseurStateManager.getPose().addShape(currentSelect);
        // REFRESH CANVAS
        poseurStateManager.refreshState();
    }   
}