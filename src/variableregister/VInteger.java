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
public class VInteger extends Variable {

    VInteger() {
        super();
        tp = Type.INT;
    }

    public void setVal(int[] val) {
        this.val = val;
        hasVal = true;
    }

    public int[] getVal() {
        if (!hasVal) {
            throw new java.lang.UnsupportedOperationException("Value was not assigned.");
        }
        return val;
    }

    int[] val;

}
