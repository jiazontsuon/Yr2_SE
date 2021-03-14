package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestLocation {
	
	Location l1;
	Location l2;
	Location l3;
	
    @BeforeEach
    void setUp() throws Exception {
    	l1 = new Location("EH15 8AY","Uoe");
    	l2 = new Location("EH34 1FA","Bargain Shop");
    	l3 = new Location("AK47 5YU","Pollock Hall");
    	//System.out.println("hehe");
    	
        // TODO: setup some resources before each test
    	
    }
    
    @Test
	void testIsNearTo() {
		
    	//boolean a = l1.isNearTo(l2);
    	
		assertEquals(true,l1.isNearTo(l2));// l1 is near to l2
		assertEquals(false,l2.isNearTo(l3)); // l2 is not near to l3
		assertEquals(false,l3.isNearTo(l1)); // l3 is not near to l1
	}
    
    
    
    // TODO: put some tests here
}
