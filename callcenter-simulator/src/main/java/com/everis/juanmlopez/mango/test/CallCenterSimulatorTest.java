package com.everis.juanmlopez.mango.test;

import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class CallCenterSimulatorTest extends TestCase {

	private CallCenterSimulator callCenterSimulator  = new CallCenterSimulator();

	@Test
	public void testTiempoMaximoDeTrabajoParaCajero() throws InterruptedException, ExecutionException {
		
		int[] tiempos = { 5, 1, 1, 2 };
		Assert.assertEquals( callCenterSimulator.tiempoMaximoDeTrabajoParaCajero(tiempos, 2), 5);
	}

}
