package AnimatedPoseur.renderer;
    // we make the instance variables public for convenience,
    // which is ok because these objects are never used
    // outside of the PoseList class.
/**
 *
 * @author   Richard McKenna
 *                  Yunlong Zhang
 *                Stony Brook University
 * @version 1.0
 */
public class PoseNode
{
      private Pose pose;
      private PoseNode next;
        
      public PoseNode(Pose initPose, PoseNode initNext)
      {
           pose = initPose;
           next = initNext;
       }

    /**
     * @return the pose
     */
    public Pose getPose() {
        return pose;
    }

    /**
     * @param pose the pose to set
     */
    public void setPose(Pose pose) {
        this.pose = pose;
    }

    /**
     * @return the next
     */
    public PoseNode getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(PoseNode next) {
        this.next = next;
    }
 }
