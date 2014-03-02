package AnimatedPoseur.state;

/**
 * This enum represents the states that are possible for our
 * Poseur editor. States represent the following
 * 
 *  * STARTUP_STATE - This is the mode when the application first
 * opens, before a file has been loaded or a new sprite has
 * been started. In this mode, most controls are inactive. Once
 * the user starts a new sprite or loads an existing one we enter
 * the selected animation state state
 * 
 * SELECT_ANIMATION_STATE_STATE - This is mode when user 
 * just open up a file of sprite and has not clicked animation state, 
 * all controls basically are all disable in this mode, because we don't 
 * have anything to read and modify yet. however, under this mode, we
 * can create new animation state because creating new animation state
 * will not required any animation state selection.
 * 
 * SELECT_POSEUR_POSE_STATE - This is mode when user click
 * on 1 animation state and hasn't selected any pose yet. Under this 
 * mode user can actually simply modify the animation state now, Like
 * rename, duplicate and delete. Moreover, the pose list will be fully loaded
 * with all poses that belong to current selected animation state, note that
 * there will be nothing if there isn't any pose existed
 * 
 * POSEUR_POSE_SELECTED_STATE - This is mode when user click
 * on a specific pose, after selection all pose control will be enabled 
 * user can do whatever they want to, after use click to new pose or pose
 * editor button, application will be moved to drawing state, in our application
 * we divided those into two situation, new pose and pose editor, basically,
 * those two state are all serve the same purpose, which is drawing something.
 * and it will help application to recognize the state and do the corresponding 
 * activity in our sprite datat structure.
 * 
 * NEW_POSE_SELECT_SHAPE_STATE - This is the default mode when a pose
 * is first started or loaded from a file. In this mode, the
 * user has an arrow cursor and can press various editing buttons
 * to get to work on creating content. In this mode, the user
 * may also select shapes on the pose area.
 * 
 * NEW_POSE_SHAPE_SELECTED_STATE - If the user selects a shape by clicking
 * on it, it moves into this mode. In this mode, the shape would
 * be highlighted and can be resized. This mode still uses the
 * arrow cursor.
 * 
 * NEW_POSE_CREATE_SHAPE_STATE - When the user selects one of the toggle
 * buttons to create a shape like a rectangle, the cursor switches
 * to crosshairs and enters this mode. In this mode, the user must
 * press on the pose area to start making a shape.
 * 
 * NEW_POSE_COMPLETE_SHAPE_STATE - Once the user has pressed on the pose area
 * it moves into this mode, where the user may drag the mouse
 * across the pose area to make the shape. If the user releases
 * the mouse on the pose area, a shape will be made there and we'll
 * return to select shape mode. If the mouse moves outside the 
 * pose area, the shape in progress will be reset but we'll stay
 * in this mode.
 * 
 * NEW_POSE_DRAG_SHAPE_STATE - When the user clicks on the pose area in 
 * select shape mode and selects a shape, we enter this mode
 * and remain until either leaving the pose area. In the meantime,
 * the selected shape moves wherever the mouse goes.
 * 
 * POSE_EDITOR_SELECT_SHAPE_STATE - This is the default mode when a pose
 * is first started or loaded from a file. In this mode, the
 * user has an arrow cursor and can press various editing buttons
 * to get to work on creating content. In this mode, the user
 * may also select shapes on the pose area.
 * 
 * POSE_EDITOR_SHAPE_SELECTED_STATE - If the user selects a shape by clicking
 * on it, it moves into this mode. In this mode, the shape would
 * be highlighted and can be resized. This mode still uses the
 * arrow cursor.
 * 
 * POSE_EDITOR_CREATE_SHAPE_STATE - When the user selects one of the toggle
 * buttons to create a shape like a rectangle, the cursor switches
 * to crosshairs and enters this mode. In this mode, the user must
 * press on the pose area to start making a shape.
 * 
 * POSE_EDITOR_COMPLETE_SHAPE_STATE - Once the user has pressed on the pose area
 * it moves into this mode, where the user may drag the mouse
 * across the pose area to make the shape. If the user releases
 * the mouse on the pose area, a shape will be made there and we'll
 * return to select shape mode. If the mouse moves outside the 
 * pose area, the shape in progress will be reset but we'll stay
 * in this mode.
 * 
 * POSE_EDITOR_DRAG_SHAPE_STATE - When the user clicks on the pose area in 
 * select shape mode and selects a shape, we enter this mode
 * and remain until either leaving the pose area. In the meantime,
 * the selected shape moves wherever the mouse goes.
 * 
 * @author  Richard McKennaz
 *              Yunlong Zhang
 *              CSE 219 Stony Brook
 * @version 1.0
 */
public enum AnimatedPoseurState 
{
    STARTUP_STATE,
    
    SELECT_ANIMATION_STATE_STATE,
    SELECT_POSEUR_POSE_STATE,
    POSEUR_POSE_SELECTED_STATE,
    
    NEW_POSE_SELECT_SHAPE_STATE,
    NEW_POSE_SHAPE_SELECTED_STATE,
    NEW_POSE_CREATE_SHAPE_STATE,
    NEW_POSE_COMPLETE_SHAPE_STATE,
    NEW_POSE_DRAG_SHAPE_STATE,
     
    POSE_EDITOR_SELECT_SHAPE_STATE,
    POSE_EDITOR_SHAPE_SELECTED_STATE,
    POSE_EDITOR_CREATE_SHAPE_STATE,
    POSE_EDITOR_COMPLETE_SHAPE_STATE,
    POSE_EDITOR_DRAG_SHAPE_STATE,
    
}
