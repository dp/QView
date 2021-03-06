/*
 * HTNodeLabel.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.qview.gui.hypertree;

import java.awt.*;
import java.io.CharArrayReader;
import java.util.HashMap;
import org.qview.control.GuiDataAdapter;

/**
 * The HTNodeLabel class implements the drawed label 
 * representing a fNodeDraw.
 * 
 * 
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 1.0
 */
class HTNodeLabel {

    private HTDrawNode fNodeDraw   = null;  // represented fNodeDraw
    private int        fx      = 0;     // fx up-left corner
    private int        fy      = 0;     // fy up-left corner
    private int        fWidth  = 0;     // fWidth of the label
    private int        fHeight = 0;     // fHeight of the label
    private boolean    fActive = false; // should be drawn ?


  /* ---  Constructor --- */
  
    /**
     * Constructor.
     * 
     * 
     * @param fNodeDraw    the represented fNodeDraw
     */
    HTNodeLabel(HTDrawNode nodeDraw) {
        this.fNodeDraw = nodeDraw;
    }
    
    
  /* --- Draw --- */
  
    /**
     * Draw this label, if there is enought drawSpace.
     * 
     * 
     * @param g    the graphic context
     */
    void draw(Graphics g) {
        Font defFont = g.getFont();
        Font smallFont= new Font("SanSerif", Font.PLAIN, 9);
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int newFontHeight;
        int stringWidth = 0;
        int drawSpace = fNodeDraw.getSpace();
        if (drawSpace >= fontHeight) {

            fActive = true;
            HTCoordS zs = fNodeDraw.getScreenCoordinates();          
            String nodeName = fNodeDraw.getName();
            Color nodeColour = fNodeDraw.getColor();
            char[] nameC = nodeName.toCharArray();            
            int nameLength = nameC.length;
            int nameWidth = fontMetrics.charsWidth(nameC, 0, nameLength);  
                        
            HashMap metricMap = new HashMap();
            String metric = null;
            String state = null;
            int rad = 6;            
            
            if (fNodeDraw.getLongNameMode() == false) {
                while((nameWidth >= drawSpace) && (nameLength > 0)) {
                    nameLength--;               
                    nameWidth = fontMetrics.charsWidth(nameC, 0, nameLength);
                }
            }
            
            fHeight = fontHeight;
            fWidth = nameWidth + 10;
            fx = zs.fx - (fWidth / 2);
            fy = zs.fy - (fontHeight / 2);  
                      
            g.setColor(nodeColour);
            HTNode node= fNodeDraw.getNode();            
            int shape= fNodeDraw.getShape();
           
            int sx = zs.fx - (nameWidth / 2);            
            int sy = fy + fontMetrics.getAscent() + (fontMetrics.getLeading() / 2);
            
            switch (shape){
                 case HTree.SHAPE_ROUND_RECT:
                    state = GuiDataAdapter.findInstance().GetNodeState(node);
                    if (state.equals("none")){
                        g.setColor(HTColours.BACKGROUND);
                    } else if (state.equals("warn")){                        
                        g.setColor(HTColours.WARN);
                    } else if (state.equals("crit")){
                        g.setColor(HTColours.CRIT);
                    } else {                       
                        g.setColor(nodeColour);                                             
                    } 
                    g.fillRoundRect(fx, fy, fWidth, fHeight, 10, 10);
                    if (node.getSelected()) {
                        g.setColor(HTColours.NODE_SELECTED_OUTLINE);
                    } else {
                        g.setColor(HTColours.NODE_OUTLINE);
                    }
                    g.drawRoundRect(fx, fy, fWidth, fHeight, 10, 10);
                    break;
                 case HTree.SHAPE_RECT: 
                    g.fillRect(fx, fy, fWidth, fHeight);
                    if (node.getSelected()) {
                        g.setColor(HTColours.NODE_SELECTED_OUTLINE);
                    } else {
                        g.setColor(HTColours.NODE_OUTLINE);
                    }
                    g.drawRect(fx, fy, fWidth, fHeight);
                    break;
                case HTree.SHAPE_OVAL: 
                    g.fillOval(fx-2, fy-4, fWidth+4, fHeight+8);
                    if (node.getSelected()) {
                        g.setColor(HTColours.NODE_SELECTED_OUTLINE);
                    } else {
                        g.setColor(HTColours.NODE_OUTLINE);
                    }
                    g.drawOval(fx-2, fy-4, fWidth+4, fHeight+8);
                    break;
                case HTree.SHAPE_METRIC_CIRC: 
                    metricMap =  GuiDataAdapter.findInstance().getNodeMetricMap(node);
                    metric = (String) metricMap.get("metric");  
                    rad = ((Integer) metricMap.get("size")).intValue();
                    state = (String) metricMap.get("state");
                    
                    if (state.equals("none")){
                        g.setColor(HTColours.BACKGROUND);
                    } else if (state.equals("warn")){                        
                        g.setColor(HTColours.WARN);
                    } else if (state.equals("crit")){
                        g.setColor(HTColours.CRIT);
                    } else {                       
                        g.setColor(nodeColour);                                             
                    }                    
                    g.fillOval(zs.fx-rad, zs.fy-rad, rad*2, rad*2);
                    if (node.getSelected()) {
                        g.setColor(HTColours.NODE_SELECTED_OUTLINE);
                    } else {
                        g.setColor(HTColours.NODE_WEAK_OUTLINE);
                    }
                    g.drawOval(zs.fx-rad, zs.fy-rad, rad*2, rad*2);
                    sy = fy - 2*rad-2+fontMetrics.getAscent() + (fontMetrics.getLeading() / 2);
                    
                    stringWidth = fontMetrics.stringWidth(metric);
                    g.setColor(HTColours.NODE_TEXT);
                    g.setFont(smallFont);
                    newFontHeight = fontMetrics.getHeight();
                    stringWidth = fontMetrics.stringWidth(metric);
                    g.drawString(metric, zs.fx+1-stringWidth/2, zs.fy-3+newFontHeight/2);
                    g.setFont(defFont);
                    break;
                case HTree.SHAPE_METRIC_SQAR: 
                    metricMap =  GuiDataAdapter.findInstance().getNodeMetricMap(node);
                    metric = (String) metricMap.get("metric");  
                    rad = ((Integer) metricMap.get("size")).intValue();
                    state = (String) metricMap.get("state");
                    
                    if (state.equals("none")){
                        g.setColor(HTColours.BACKGROUND);
                    } else if (state.equals("warn")){
                        g.setColor(HTColours.WARN);
                    } else if (state.equals("crit")){
                        g.setColor(HTColours.CRIT);
                    } else {                       
                        g.setColor(nodeColour);                                             
                    }                     
                    g.fillRect(zs.fx-rad, zs.fy-rad, rad*2, rad*2);
                    if (node.getSelected()) {
                        g.setColor(HTColours.NODE_SELECTED_OUTLINE);
                    } else {
                        g.setColor(HTColours.NODE_WEAK_OUTLINE);
                    }
                    g.drawRect(zs.fx-rad, zs.fy-rad, rad*2, rad*2);
                    sy = fy - 2*rad-2+fontMetrics.getAscent() + (fontMetrics.getLeading() / 2);                    
                    
                    g.setColor(HTColours.NODE_TEXT);
                    g.setFont(smallFont);
                    newFontHeight = fontMetrics.getHeight();
                    stringWidth = fontMetrics.stringWidth(metric);
                    g.drawString(metric, zs.fx+1-stringWidth/2, zs.fy-3+newFontHeight/2);
                    g.setFont(defFont);
                    break;
            } //switch
//------------------------------
                       
            g.setColor(HTColours.NODE_TEXT);
            g.drawString(new String(nameC, 0, nameLength), sx, sy);
//            g.drawString(new String(qdepth, 0, qdepthlength), zs.fx-rad, zs.fy-rad);
//            g.drawString("43", zs.fx-rad, zs.fy-rad+4);
        } else {
            fActive = false;
        }
    }
  
    /* --- Zone containing --- */

    /**
     * Is the given HTCoordS within this label ?
     *
     * @return    <CODE>true</CODE> if it is,
     *            <CODE>false</CODE> otherwise
     */
    boolean contains(HTCoordS zs) {
        if (fActive) {
            if ((zs.fx >= fx) && (zs.fx <= (fx + fWidth)) &&
                (zs.fy >= fy) && (zs.fy <= (fy + fHeight)) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return fNodeDraw.getScreenCoordinates().contains(zs);
        }
    }
    
        
  /* --- ToString --- */

    /**
     * Returns a string representation of the object.
     *
     * @return    a String representation of the object
     */
    public String toString() {
        String result = "label of " + fNodeDraw.getName() + 
                        "\n\tx = " + fx + " : y = " + fy +
                        "\n\tw = " + fWidth + " : h = " + fHeight; 
        return result;
    }

}
