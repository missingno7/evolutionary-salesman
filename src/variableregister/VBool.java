/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package variableregister;

/**
 *
 * @author J_Vestfal
 */
public class VBool extends Variable {

    VBool() {
        super();
        tp=Type.BOOL;
    }
    
    public void setVal(boolean[] val){
        this.val=val;
        hasVal=true;
    }
    
    public boolean[] getVal(){
        if(!hasVal)throw new java.lang.UnsupportedOperationException("Value was not assigned.");
        return val;
    }
    
    boolean[] val;

}
