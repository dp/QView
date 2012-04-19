/*
 * RefreshNodeAction.java
 *
 * Created on May 27, 2005, 4:09 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.qview.gui.nbexplorer;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author T.Goodwill
 */
public class RefreshNodeAction extends CallableSystemAction {
    
    public void performAction() {
        PropertiesNotifier.changed();
    }
    public String getName() {
        return NbBundle.getBundle(RefreshNodeAction.class).getString("LBL_RefreshProps");
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}