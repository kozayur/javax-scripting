/*
Copyright � 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa  Clara, California
95054, U.S.A. All rights reserved.U.S. Government Rights - Commercial software.
Government users are subject to the Sun Microsystems, Inc. standard license agreement
and applicable provisions of the FAR and its supplements.  Use is subject to license
terms.  Sun, Sun Microsystems,  the Sun logo,  Java and  Jini are trademarks or registered
trademarks of Sun Microsystems, Inc. in the U.S. and other countries. This product is
covered and controlled by U.S. Export Control laws and may be subject to the export or import
laws in other countries.  Nuclear, missile, chemical biological weapons or nuclear maritime 
end uses or end users, whether direct or indirect, are strictly prohibited. Export or reexport
to countries subject to U.S. embargo or to entities identified on U.S. Export exclusion lists,
including, but not limited to, the denied persons and specially designated nationals lists is
strictly prohibited.

Copyright � 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,  California 95054,
Etats-Unis. Tous droits r�serv�s.L'utilisation est soumise aux termes de la Licence.Sun, Sun
Microsystems,  le logo Sun,  Java et  Jini sont des  marques de fabrique ou des marques 
d�pos�es de Sun Microsystems, Inc. aux Etats-Unis et dans d'autres pays.Ce produit est soumis
� la l�gislation am�ricaine en mati�re de contr�le des exportations et peut �tre soumis � la 
r�glementation en vigueur dans d'autres pays dans le domaine des exportations et importations.
Les utilisations, ou utilisateurs finaux, pour des armes nucl�aires,des missiles, des armes 
biologiques et chimiques ou du nucl�aire maritime, directement ou indirectement, sont 
strictement interdites. Les exportations ou r�exportations vers les pays sous embargo 
am�ricain, ou vers des entit�s figurant sur les listes d'exclusion d'exportation am�ricaines,
y compris, mais de mani�re non exhaustive, la liste de personnes qui font objet d'un ordre de
ne pas participer, d'une fa�on directe ou indirecte, aux exportations des produits ou des 
services qui sont r�gis par la l�gislation am�ricaine en mati�re de contr�le des exportations
et la liste de ressortissants sp�cifiquement d�sign�s, sont rigoureusement interdites.
*/
/*
 * ModuleEventSupport.java
 *
 * Created on June 28, 2006, 8:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.chuk.lee.scriptengine.api;

import java.util.*;

/**
 *
 * @author projects
 */
public class ModuleEventSupport implements ModuleEvent {
    
    private final HashMap<String, Vector<ModuleEventListener>> listeners;
    
    private class FireEvent implements Runnable {
        private Vector<ModuleEventListener> list;
        public FireEvent(Vector<ModuleEventListener> l) {
            list = l;
        }
        
        public void run() {
            for (ModuleEventListener l: list)
                l.invoke();        
        }
    }
    
    /** Creates a new instance of ModuleEventSupport */
    public ModuleEventSupport() {
        listeners = new HashMap<String, Vector<ModuleEventListener>>();
    }
    
    public String[] list() {
        return (new String[0]);
    }
    
    public EventReference register(String event, ModuleEventListener callback) {
        EventReference ref = null;
        Vector<ModuleEventListener> l;
        
        if ((l = listeners.get(event)) == null) {
            //Bug
            //l = new HashSet<ModuleEventListener>();
            l = new Vector<ModuleEventListener>();
            listeners.put(event, l);
        }
        
        l.add(callback);
        
        return (new EventReference(event, callback, l));
    }
    
    public void fireEvent(String e) {
        
        Vector<ModuleEventListener> l;
        
        if ((l = listeners.get(e)) == null)
            return;
        
        Thread t = new Thread(new FireEvent(l));
        t.start();
    }
    
    public void fireEventSync(String e) {
        
        Vector<ModuleEventListener> l;
        
        if ((l = listeners.get(e)) == null)
            return;
        
        FireEvent fe = new FireEvent(l);
        fe.run();
    }
}
