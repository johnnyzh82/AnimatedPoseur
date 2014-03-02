package AnimatedPoseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.shapes.PoseurShape;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event Handler responds to the movement of current selected shape, which moves the selected shape all
 * way to the back. All the other graphs are all located in front of this current selected shape
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class MoveToBackHandler implements ActionListener{
 /**
  * This method relays this event to the state manager, which
  * will update the poseur class
  * 
  * @param ae The event object for this button clicked
  */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        // MOVE TO BACK IS A LITTLE BIT COMPLICATED, SINCE WE CAN'T MOVE THE SELECTED SHAPE TO FIRST 
        // DIRECTLY, WE NEED MAKE A COPY OF ORIGINAL LINKED LIST AND MAKE A NEW ONE STORE THE CURRENT
        // SELECTED SHAPE TO HEAD AND MOVE THE ORIGINAL LINK LIST TO THIS EXCEPT THE SELECTED ONE,
        // HENCE WE GOT THIS SHAPE TO BACK
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurShape currentSelect = poseurStateManager.getSelectedShape();
        LinkedList<PoseurShape> copy = new LinkedList<>();
        // FOR LOOP TO COPY ENTIRE LINKED LIST ELMENTS WITHOUT REFERENCE
        for(int k=0;k<poseurStateManager.getPose().getShapesList().size();k++)
        {
            copy.add(poseurStateManager.getPose().getShapesList().get(k));
        }
        // CLEAR THE LIST
        poseurStateManager.getPose().reset();
        // SET HEAD TO CURRENT SHAPE
        poseurStateManager.getPose().getShapesList().add(currentSelect);
        // ANOTHER FOR LOOP TO ADD ALL ELEMENT BACK TO THIS LIST
         for(int i=0; i<copy.size(); i++)
         {
             if(!copy.get(i).equals(currentSelect))
             {
                poseurStateManager.getPose().addShape(copy.get(i));
             }
         }
         // REFRESH THE CANVAS
         poseurStateManager.refreshState();
    }   
}
