
package AnimatedPoseur.state;

import AnimatedPoseur.renderer.PoseList;
import AnimatedPoseur.renderer.SpriteType;
import java.util.ArrayList;
/**
 *
 * @author Yunlong Zhang
 */
public class AnimatedPoseurSprite {
    
    // Sprite Demension
//    private int height;
//    private int width;
    private SpriteType type;
    private ArrayList<String> animationStateListNames;
    
    public AnimatedPoseurSprite()
    {
//        width = -1;
//        height = -1;
    }
    
    /**
     * This constructor is for when the width and height are known
     * at the time of construction.
     * 
     * @param initWidth Width of all images in pixels for this sprite type.
     * 
     * @param initHeight Height of all images in pixels for this sprite type.
     */
    public AnimatedPoseurSprite(int initWidth, int initHeight)
    {
        this();
        type = new SpriteType(initWidth,initHeight);
        animationStateListNames = new ArrayList();
    }
    
    /**
     * need doc here
     */
    public SpriteType getSpriteType()
    {
        return type;
    }
    
    public void setSpriteType(SpriteType type)
    {
        this.type = type;
    }
    
    public PoseList addPoseList(String animationState)
    {
        return (type.addPoseList(animationState));
    }
    
    public ArrayList<Integer> addPoseImageList(String animationState)
    {
        return (type.addPoseImageList(animationState));
    }

    /**
     * @return the animationStateNames
     */
    public ArrayList<String> getAnimationStateNames() {
        return animationStateListNames;
    }
    public void addAnimationState(String stateName)
    {
        animationStateListNames.add(stateName);
    }

//    /**
//     * @return the height
//     */
//    public int getHeight() {
//        return height;
//    }
//
//    /**
//     * @param height the height to set
//     */
//    public void setHeight(int height) {
//        this.height = height;
//    }
//
//    /**
//     * @return the width
//     */
//    public int getWidth() {
//        return width;
//    }
//
//    /**
//     * @param width the width to set
//     */
//    public void setWidth(int width) {
//        this.width = width;
//    }
}

