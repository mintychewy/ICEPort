package util;

import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

public class ScalerTest {

	@Test
	public void testDet() {

		int[][] matrix = {{1,1,1},
				{1,1,1},
				{1,1,1}};
		int det = Scaler.det(matrix);
		assertTrue(det == 0);


		int[][] matrix2 = {{1,2,3},{4,5,6},{7,8,1}};

		int det2 = Scaler.det(matrix2);
		assertTrue(det2 == 24);

	}

	@Test
	public void testToTileSpace(){
		Point viewportPoint = new Point(3482,1237);
		Point tilePoint = Scaler.toTileSpace(viewportPoint);
		assertTrue(tilePoint.x == 28 && tilePoint.y == 20);
	}


}
