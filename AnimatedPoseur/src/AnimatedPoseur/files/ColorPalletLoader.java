package AnimatedPoseur.files;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.state.ColorPalletState;

/**
 * This class can be used to load color pallet data from an XML 
 * file into a constructed ColorPalletState. Note that the XML
 * file must validate against the color_pallet_settings.xsd
 * schema. This class demonstrates how application settings can
 * be loaded dynamically from a file.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class ColorPalletLoader 
{    
    /**
     * This method will extract the data found in the provided
     * XML file argument and use it to load the color pallet
     * argument.
     * 
     * @param colorPalletXMLFile Path and file name to an XML
     * file containing information about a custom color pallet. Note
     * this XML file must validate against the aforementioned schema.
     * 
     * @param colorPalletState The state manager for the color
     * pallet, we'll load all the data found in the XML file
     * inside here.
     */
    public void initColorPallet( String colorPalletXMLFile, 
                                    String colorPalletSchema,
                                    ColorPalletState colorPalletState)
    {
        try
        {
            // Here we need load all imformation from XML color pallet file 
            // 1, we need extract the pallet size and pallet rows
            // 2, we need extract default color node and store RGB in colorPalletState
            // 3, we need extract all other colors and store all RGBs in colorPalletState
            // Initialize an new XMLUtilities structure
            XMLUtilities colorLoader = new XMLUtilities();
            // Load this tree 
            Document colorDoc = colorLoader.loadXMLDocument(colorPalletXMLFile,colorPalletSchema);
            // load sizes and rows
            int palletSize = colorLoader.getIntData(colorDoc,  PALLET_SIZE_NODE);
            int palletRow = colorLoader.getIntData(colorDoc, PALLET_ROWS_NODE);
            // read the number of total colors, remember to plus one because we don't count the default color yet
            int numberOfColor = colorLoader.getNumNodesOfElement(colorDoc,  PALLET_COLOR_NODE) ;
            // declare and initialize 3 components of color object
            int red,green,blue;
            // the imformation we read blow is separately read the default color from others
            red= colorLoader.getIntData(colorDoc, RED_NODE);
            green = colorLoader.getIntData(colorDoc, GREEN_NODE);
            blue= colorLoader.getIntData(colorDoc, BLUE_NODE);
            Color defaultColor = new Color(red,green,blue);;
            // initialize an color array as same size as the pallet size we read from the tree.
            Color[] colorPallet = new Color[palletSize];
            // start to go through all colors in xml file
            for (int i = 0; i < numberOfColor; i++)
            {
                // load all available colors from xml file, and we divide this problem into 3 subproblems
                // each of them are red green blue, we store the numbers we read and store the new color
                // in the array we build
                    Node colorNode = colorLoader.getNodeInSequence(colorDoc, PALLET_COLOR_NODE, i);
                    Node redNode = colorLoader.getChildNodeWithName(colorNode, RED_NODE);
                    Node greenNode = colorLoader.getChildNodeWithName(colorNode, GREEN_NODE);
                    Node blueNode = colorLoader.getChildNodeWithName(colorNode, BLUE_NODE);
                    red = Integer.parseInt(redNode.getTextContent());
                    green = Integer.parseInt(greenNode.getTextContent());
                    blue  = Integer.parseInt(blueNode.getTextContent());
                    Color tempColor = new Color(red,green,blue);
                    colorPallet[i] = tempColor;
            }
            colorPalletState.loadColorPalletState(  colorPallet,
                                                     palletRow,
                                                     numberOfColor,
                                                    defaultColor);
        } catch (InvalidXMLFileFormatException ex) 
        {
            Logger.getLogger(ColorPalletLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}