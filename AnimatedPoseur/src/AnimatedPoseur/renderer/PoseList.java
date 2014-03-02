package AnimatedPoseur.renderer;

import java.util.Iterator;

/**
 * A PoseList is a linked list of Poses for an animated sprite. Such a
 * list would be used to represent the poses for a particular animated
 * sprite state. So, to render the sprite, just go through the pose list
 * rendering each one in order and then starting all over again, circularly
 * looping through the poses.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseList 
{
    // THE FIRST POSE IN THE LIST
    private PoseNode headPose;
    
    // THE LAST POSE IN THE LIST
    private PoseNode tailPose;
    
    // LIST COUNTER
    private int numPoses;

    /**
     * This default (and only) constructor simply initializes
     * and empty list.
     */
    public PoseList()
    {
        // THIS STARTS OUT AS AN EMPTY LIST
        headPose = null;
        tailPose = null;
        numPoses = 0;
    }
    
    /**
     * This method adds a new pose to this list. The pose
     * will have the imageID and durationInFrames arguments as
     * its state.
     * 
     * @param imageID The image to use for the pose being added.
     * 
     * @param durationInFrames The number of frames to keep the
     * pose being added on the screen.
     */
    public void addPose(int imageID, int durationInFrames)
    {
        // CREATE THE NEW POSE
        Pose poseToAdd = new Pose(imageID, durationInFrames);
        
        // AND NOW ADD IT TO OUR SINGLY LINKED LIST
        if (headPose == null)
        {
            // HERE IT'S THE FIRST POSE IN THE LIST
            headPose = new PoseNode(poseToAdd, null);
            tailPose = headPose;
        }
        else
        {
            // AND HERE WE'RE JUST APPENDING IT ONTO THE END
            tailPose.setNext(new PoseNode(poseToAdd, null));
            tailPose = tailPose.getNext();
        }
        // DON'T FORGET TO INCREMENT THE POSE COUNTER
        numPoses++;
    }
    
    /**
     * This method remove the pose at given index 
     *
     * @param indexToRemove Index of pose we want to remove 
     * 
     * @return the removed pose
     */
    public PoseNode removePose(int indexToRemove)
    {
        if(indexToRemove > size())
        {
            return null;
        }
        if(headPose == null)
        {
            return null;
        }
        PoseNode cursor = headPose;
        PoseNode parentOfCursor = null;
        int i=0;
        // FIRST OF ALL, WE ARE GOING TO LOCATE THE POSTION OF THE NODE WE 
        // WE REMOVEA
        while(i<indexToRemove + 1)
        {
            if(i==indexToRemove - 1)
            {
                parentOfCursor = cursor;
            }
            cursor = cursor.getNext();
            i++;
        }
        // WE SIMPLY SET HEAD POSE AND TAIL POSE TO NULL IF THERE IS ONLY
        // 1 ELEMENT IN THE POSE LIST
        if(cursor == headPose)
        {
            headPose = null;
            tailPose = null;
        }
        // IF CURSOR IS LOCATED TO LAST ONE, WE JUST SET THE PARENT NOT --> NEXT
        // TO NULL
        else if(cursor == tailPose && cursor!= headPose)
        {
            parentOfCursor.setNext(null);
        }
        // IF IN MIDDLE
        else
        {
            parentOfCursor.setNext(cursor.getNext());
            tailPose = parentOfCursor;
        }
        numPoses--;
        return cursor;
    }
  
    /**
     * Accessor method getting the pose of specific index \
     * 
     * @param index
     * @return PoseNode we need
     */
    public Pose getPose(int index)
    {
        if(index > size() || index < 0)
        {
            return null;
        }
        // WE NEED TO DUPLICATE THE NODE AND CONCATENATE THE COPY TO THE END OF
       // THE POSELIST
       Iterator<Pose> poseListIterator = getPoseListIterator();
       Pose pose = null;
       int i = 0;
       while(poseListIterator.hasNext())
       {
           pose = poseListIterator.next();
           if(i == index)
           {
               break;
           }
           i++;
       }
       return pose;
    }
    
    /**
     * This method helps us duplicate the pose at given index, the duplicated
     * pose will be added at the end of poselist.
     * 
     * @param indexToDuplicate the pose we want to duplicate
     * 
     * @return the duplicated pose
     */
   public PoseNode duplicate(int indexToDuplicate)
   {
       if(indexToDuplicate > size() || indexToDuplicate <0)
        {
            return null;
        }
       Pose node = null;
       PoseNode newPoseNode = null;
       if(headPose != null)
       {
       // WE NEED TO DUPLICATE THE NODE AND CONCATENATE THE COPY TO THE END OF
       // THE POSELIST
       Iterator<Pose> poseListIterator = getPoseListIterator();
       int i = 0;
       while(poseListIterator.hasNext())
        {
          node = poseListIterator.next();
          i++;
          if(i == indexToDuplicate)
           {
              int imageID = node.getImageID();
              int duration = node.getDurationInFrames();
              Pose newPose = new Pose(imageID,duration);
              newPoseNode = new PoseNode(newPose,null);
              tailPose.setNext(newPoseNode);
              tailPose = newPoseNode;
              break;
           }
         }
        }
       return newPoseNode;
   }
   
   /**
    * This method helps us to swap two indexes in the pose list
    * after swapping, positions will be changed, data will be swapped
    * 
    * @param smallerIndex smaller index
    * @param biggerIndex bigger index
    * 
    * @return true if swapped
    */
   public boolean swapTwoPoses(int smallerIndex, int biggerIndex)
    {
        if(smallerIndex > size() || smallerIndex < 0)
        {
            return false;
        }
        // return false if there is no element or just 1 element
        if(headPose == null || headPose == tailPose)
        {
            return false;
        }
        int i = 0;
        Pose smallerNode = null;
        Pose biggerNode = null;
        Pose cursor = null;
        // GET THE TOTAL SIZE OF THE POSE LIST
        Iterator<Pose> poseListIterator = getPoseListIterator();
        while(i <= size())
        {
          cursor = poseListIterator.next();
          if(i == smallerIndex)
          {
              smallerNode = cursor;
          }
          else if(i == biggerIndex)
          {
              biggerNode = cursor;
              break;
          }
          i++;
        }
        int smallerIndexID = smallerNode.getImageID();
        int smallerIndexDuration = smallerNode.getDurationInFrames();
        int biggerIndexID = biggerNode.getImageID();
        int biggerIndexDuration = biggerNode.getDurationInFrames();
        smallerNode.setImageID(biggerIndexID);
        smallerNode.setDurationInFrames(biggerIndexDuration);
        biggerNode.setImageID(smallerIndexID);
        biggerNode.setDurationInFrames(smallerIndexDuration);
        return true;
    }
   
   /**
    * return the size of the pose list
    * 
    * @return the number of poses
    */
   public int size()
   {
       return numPoses;
   }
   
   
    /**
     * This accessor method returns a circular iterator to be used to 
     * go through all the poses repeatedly.
     * 
     * @return A circular Iterator that will start at the first
     * pose in the list and will produce elements circularly forever.
     */
    public Iterator<Pose> getPoseIterator()
    {
        // RETURN OUR OWN DEFINED TYPE OF ITERATOR
        return new CircularPoseIterator();
    }
    
    /**
     * This accessor method returns a regular iterator to be used to 
     * go through all the poses repeatedly.
     * 
     * @return A circular Iterator that will start at the first
     * pose in the list and will produce elements circularly forever.
     */
    public Iterator<Pose> getPoseListIterator()
    {
        // RETURN OUR OWN DEFINED TYPE OF ITERATOR
        return new ListIterator();
    }
    
    
    
    // This private class will serve as the iterator for
    // producing all the poses regular in sequenctial
    // order.
    private class ListIterator implements Iterator
    {
        private PoseNode currentPose;
        public ListIterator()
        {
            currentPose = headPose;
        }
        // REACH THE END IF CURSOR EQUALS TO TAIL
        @Override
        public boolean hasNext()
        {
            return (currentPose != tailPose);
        }
     
        // PRODUCE THE NEXT ELEMENT IN THE LIST, AND THEN MOVE
        // THE MARKER TO THE NEXT ELEMENT, ENDS AT END OF LIST
        @Override
        public Pose next()
        {
            Pose poseToReturn = null;
            if(currentPose!=null){
                poseToReturn = currentPose.getPose();
                if (currentPose != tailPose)
                {
                    currentPose = currentPose.getNext();
                }
            }
            return poseToReturn;
        }
        
        // WE WON'T USE THIS METHOD
        @Override
        public void remove() {}
    }
    
    // This private class will serve as the iterator for
    // producing all the poses circularly in sequenctial
    // order.
    //
    // WE'LL CONCEAL THIS private CLASS FOR API DOCUMENTATION
    // PURPOSES.
    private class CircularPoseIterator implements Iterator
    {
        // THIS VARIABLE KEEPS TRACK OF WHICH POSE THE
        // ITERATOR SHOULD PRODUCE NEXT
        private PoseNode currentPose;
        
        // THIS DEFAULT CONSTRUCTOR STARTS THE ITERATOR
        // AT THE FIRST POSE IN THE POSE LIST
        public CircularPoseIterator()
        {
            currentPose = headPose;
        }

        // SINCE THE LIST IS CIRCULAR, AS LONG AS THERE ARE
        // ELEMENTS IN THE LIST, THERE WILL BE MORE TO PRODUCE
        @Override
        public boolean hasNext()
        {
            return (currentPose != null);
        }
        
        // PRODUCE THE NEXT ELEMENT IN THE LIST, AND THEN MOVE
        // THE MARKER TO THE NEXT ELEMENT, WHICH MAY BE THE
        // FIRST ONE
        @Override
        public Pose next()
        {
            Pose poseToReturn = null;
            if(currentPose!=null){
                poseToReturn = currentPose.getPose();
                if (currentPose == tailPose)
                {
                    currentPose = headPose;
                }
                else
                {
                    currentPose = currentPose.getNext();
                }
            }
            return poseToReturn;
        }
        
        // WE WON'T USE THIS METHOD
        @Override
        public void remove() {}
    }
}