/*
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.  
 * Use is subject to license terms.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met: Redistributions of source code 
 * must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution. Neither the name of the Sun Microsystems nor the names of 
 * is contributors may be used to endorse or promote products derived from this software 
 * without specific prior written permission. 

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER 
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.script.jep;

import java.util.Stack;
import javax.script.Invocable;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/*
 * InvocableCommand.java
 *
 * This class wraps any JSR-223 script engine's script function
 * as a function for JEP. For example, JavaScript engine can pass
 * it's own functions as built-ins to JEP.
 *
 * @author A. Sundararajan
 */
public class InvocableCommand extends PostfixMathCommand {
    private Invocable invocable;
    private String funcName;

    public InvocableCommand(Invocable invocable, String funcName) {
        this.invocable = invocable;
        this.funcName = funcName;
        numberOfParameters = -1;
    }        

    public void run(Stack s) throws ParseException {
        checkStack(s);
        Object[] args = new Object[curNumberOfParameters];
        for (int i=0; i < args.length; i++) {
            args[i] = s.pop();
        }
        try {
            Object res = invocable.invokeFunction(funcName, args);
            s.push(res);
        } catch (Exception exp) {
            throw new ParseException(exp.getMessage());
        }
    }
}
