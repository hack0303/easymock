/*
 * Copyright (c) 2003-2009 OFFIS, Henri Tremblay. 
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.tests2;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.List;

import org.easymock.MockControl;
import org.junit.Test;

public class SerializationTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception {
        
        List<String> mock = createMock(List.class);
        
        mock = serialize(mock);
        
        expect(mock.get(1)).andReturn("a");
        
        mock = serialize(mock);
        
        replay(mock);
        
        mock = serialize(mock);
        
        assertEquals("a", mock.get(1));
        
        mock = serialize(mock);
        
        verify(mock);
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Test
    public void testLegacyMatcher() throws Exception {
        MockControl<List> control = MockControl
                .createControl(List.class);
                
        control = serialize(control);
        
        control.setDefaultMatcher(MockControl.EQUALS_MATCHER);
        
        control.expectAndReturn(control.getMock().get(1), "a");

        control.setMatcher(MockControl.EQUALS_MATCHER);
        
        control = serialize(control);
        
        control.replay();
        
        control = serialize(control);
        
        assertEquals("a", control.getMock().get(1));
        
        control = serialize(control);
        
        control.verify();
    }

    @SuppressWarnings("unchecked")
    private <T> T serialize(T o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bOut);
        out.writeObject(o);
        out.close();
        
        ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bIn);
        o = (T) in.readObject();
        in.close();
        
        return o;
    }
}
