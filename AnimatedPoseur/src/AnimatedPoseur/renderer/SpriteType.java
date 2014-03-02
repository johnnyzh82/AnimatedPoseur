package AnimatedPoseur.renderer;

import AnimatedPoseur.state.AnimatedPoseurPose;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a type of animated sprite. Note that a scene
 * many have many different sprites of similar type that would share
 * art and animation pose sequences.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class SpriteType 
{
    // THIS KEEPS TRACK OF ALL OUR LOADED IMAGES FOR THIS SPRITE
    private HashMap<Integer, Image> spriteImages;
    private HashMap<Integer,String> imagesName;
    private HashMap<Integer, AnimatedPoseurPose> spritePoses;
    
    // THIS STORES ALL THE ANIMATIONS
    private HashMap<String, PoseList> animationsToRender;
    private HashMap<String, ArrayList<Integer>> posesImageList;
    
    
    // THE SPRITE TYPE'S DIMENSIONS SHOULD BE THE SAME
    // FOR ALL IMAGES
    private int width;
    private int height;

    /**
     * The default constructor, this method constructs the data
     * structures for storing this sprite type's art and animation data. 
     */
    public SpriteType()
    {
        spriteImages = new HashMap<Integer, Image>();
        spritePoses = new HashMap<Integer, AnimatedPoseurPose>();
        animationsToRender = new HashMap<String, PoseList>();
        posesImageList = new HashMap<String, ArrayList<Integer>>();
        imagesName = new HashMap<Integer,String>();
        
        // THIS MEAN IT HASN'T YET BEEN DETERMINED
        width = -1;
        height = -1;
    }

    /**
     * This constructor is for when the width and height are known
     * at the time of construction.
     * 
     * @param initWidth Width of all images in pixels for this sprite type.
     * 
     * @param initHeight Height of all images in pixels for this sprite type.
     */
    public SpriteType(int initWidth, int initHeight)
    {
        this();
        width = initWidth;
        height = initHeight;
    }

    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the sprite type width.
     */
    public int getWidth() { return width; }
    
    /**
     * Accessor method for getting the sprite type height.
     */
    public int getHeight() { return height; }
    
    /**
     * Accessor method for getting the sprite image that corresponds
     * to the provided imageId variable.
     * 
     * @param imageID The id number of the image to be retrieved.
     * 
     * @return The valid, loaded Image that corresponds to the one
     * represented by the imageID argument.
     */
    public Image getImage(int imageID)
    {
        return getSpriteImages().get(imageID);
    }
    
    /**
     * Accessor method for getting the sprite pose that corresponds
     * to the provided poseId variable.
     * 
     * @param poseID The id number of the pose to be retrieved.
     * 
     * @return The valid, loaded pose that corresponds to the one
     * represented by the poseID argument.
     */
    public AnimatedPoseurPose getPose(int poseID)
    {
        return getSpritePoses().get(poseID);
    }
    
    /**
     * Accessor method for getting all the animation states
     * available for this sprite type.
     * 
     * @return An iterator for getting all the animation states.
     */
    public Iterator<String> getAnimationStates()
    {
        return getAnimationsToRender().keySet().iterator();
    }
    
    
    /**
     * Accessor method for getting all the animation states
     * available for this sprite type.
     * 
     * @return An iterator for getting all the animation poses.
     */
    public ArrayList<Integer> getPoseImageList(String animationState)
    {
        return getPosesImageList().get(animationState);
    }

    /**
     * Accessor method for getting the sprite image that corresponds
     * to the provided imageId variable.
     * 
     * @param imageID The id number of the image to be retrieved.
     * 
     * @return The valid, loaded Image that corresponds to the one
     * represented by the imageID argument.
     */
    public PoseList getPoseList(String animationState)
    {
        return getAnimationsToRender().get(animationState);
    }

    /**
     * Adds the imageToAdd image argument for use with this sprite type 
     * and binds that image to the imageID value.
     * 
     * @param imageID A unique identifier bound to the Image being
     * added for use with this sprite type.
     * 
     * @param imageToAdd An image to be associated with this sprite type.
     */
    public void addImage(int imageID, Image imageToAdd, String name)
    {
        getSpriteImages().put(imageID, imageToAdd);
        getImagesName().put(imageID, name.substring(0, name.indexOf(".")));
    }
    
    /**
     * Adds the poseToAdd image argument for use with this sprite type 
     * and binds that pose to the poseID value.
     * 
     * @param poseID A unique identifier bound to the Pose being
     * added for use with this sprite type.
     * 
     * @param poseToAdd An AnimatedPoseurPose to be associated with this sprite type.
     */
    public void addPoses(int poseID, AnimatedPoseurPose poseToAdd)
    {
        getSpritePoses().put(poseID, poseToAdd);
    }

    /**
     * This method constructs and returns a new PoseList that will
     * be bound to the animation state as specified by the 
     * animationState argument.
     * 
     * @param animationState The animation state to bind 
     * @return 
     */
    public PoseList addPoseList(String animationState)
    {
        PoseList animationPoses = getAnimationsToRender().get(animationState);
        if (animationPoses == null)
        {
            animationPoses = new PoseList();
            getAnimationsToRender().put(animationState, animationPoses);
        }
        return animationPoses;
    }
    
     /**
     * This method constructs and returns a new image PoseList that will
     * be bound to the animation state as specified by the 
     * animationState argument.
     * 
     * @param animationState The animation state to bind 
     * @return 
     */
    public ArrayList<Integer> addPoseImageList(String animationState)
    {
        ArrayList<Integer> animationPoses = getPosesImageList().get(animationState);
        if (animationPoses == null)
        {
            animationPoses = new ArrayList();
            getPosesImageList().put(animationState, animationPoses);
        }
        return animationPoses;
    }
 
    /**
     * Mutator method for setting the width of this sprite type. Note
     * that all images in this sprite type must be the same dimensions.
     * 
     * @param initWidth The width of all the images for this sprite type.
     */
    public void setWidth(int initWidth)
    {
        width = initWidth;
    }
    
     /**
     * Mutator method for setting the height of this sprite type. Note
     * that all images in this sprite type must be the same dimensions.
     * 
     * @param initHeight The height of all the images for this sprite type.
     */
    public void setHeight(int initHeight)
    {
        height = initHeight;
    }

    /**
     * @return the spriteImages
     */
    public HashMap<Integer, Image> getSpriteImages() {
        return spriteImages;
    }

    /**
     * @return the spritePoses
     */
    public HashMap<Integer, AnimatedPoseurPose> getSpritePoses() {
        return spritePoses;
    }
    
    public String getImagesFileName(int index)
    {
        String name = imagesName.get(index);
        return name;
    }
    
    public String getPosesFileName(int index)
    {
        return convertNames(index);
    }

    /**
     * @return the imagesName
     */
    public HashMap<Integer,String> getImagesName() {
        return imagesName;
    }
    
    public String convertNames(int index)
    {
        String imageName =  getImagesName().get(index);
        String poseName = imageName + ".pose";
        return poseName;
    }

    /**
     * @return the animationsToRender
     */
    public HashMap<String, PoseList> getAnimationsToRender() {
        return animationsToRender;
    }

    /**
     * @return the posesImageList
     */
    public HashMap<String, ArrayList<Integer>> getPosesImageList() {
        return posesImageList;
    }
}