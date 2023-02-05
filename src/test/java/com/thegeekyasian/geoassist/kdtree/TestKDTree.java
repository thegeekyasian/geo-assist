package com.thegeekyasian.geoassist.kdtree;

import java.util.List;

import com.thegeekyasian.geoassist.core.GeoAssistException;
import com.thegeekyasian.geoassist.kdtree.Point.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author thegeekyasian
 */
public class TestKDTree {

	private KDTree<String, Object> kdTree;

	@Test
	public void testInsertion() {
		assertEquals(kdTree.getSize(), 9);
	}

	@Test
	public void testDeletion() {
		boolean deleted = kdTree.delete("2");
		assertTrue(deleted);
	}

	@Test
	public void testGetByID() {
		KDTreeObject<String, Object> kdTreeObject = kdTree.getById("4");

		assertEquals(kdTreeObject.getId(), "4");
		assertEquals(kdTreeObject.getPoint().getLatitude(), 25.1903843);
		assertEquals(kdTreeObject.getPoint().getLongitude(), 55.2798557);
		assertNull(kdTreeObject.getData());
	}

	@Test
	public void testGetByID_returnsNullForNonExistentObject() {
		KDTreeObject<String, Object> kdTreeObject = kdTree.getById("11");
		assertNull(kdTreeObject);
	}

	@Test
	public void testUpdate() {
		kdTree.update("3", "test-data");
		KDTreeObject<String, Object> kdTreeObject = kdTree.getById("3");
		assertEquals(kdTreeObject.getData(), "test-data");
	}

	@Test
	public void testUpdate_throwsNotFoundException() {

		GeoAssistException geoAssistException = assertThrows(GeoAssistException.class,
				() -> kdTree.update("100", "test-data"));

		assertNotNull(geoAssistException);
		assertEquals(geoAssistException.getMessage(), "No object found for provided ID");
	}

	@Test
	public void testFindNearestNeighbors() {
		List<KDTreeObject<String, Object>> nearestNeighbors = kdTree.findNearestNeighbor(
				new Builder()
						.latitude(25.2012544)
						.longitude(55.2569389)
						.build(), 2);

		assertEquals(4, nearestNeighbors.size());
		assertEquals("1", nearestNeighbors.get(0).getId());
		assertEquals("5", nearestNeighbors.get(1).getId());
		assertEquals("7", nearestNeighbors.get(2).getId());
		assertEquals("2", nearestNeighbors.get(3).getId());
	}

	@Test
	public void testFindNearestNeighbors_returnsEmptyListForNoNearestNeighbors() {
		List<KDTreeObject<String, Object>> nearestNeighbors = kdTree.findNearestNeighbor(
				new Builder()
						.latitude(0.2012544)
						.longitude(55.2569389)
						.build(), 2);

		assertEquals(0, nearestNeighbors.size());
	}

	@BeforeEach
	public void beforeEach() {
		kdTree = new KDTree<>();
		init(kdTree);
	}

	public static void init(KDTree<String, Object> kdTree) {

		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("1")
				.latitude(25.1967512)
				.longitude(55.2732038)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("2")
				.latitude(25.1962077)
				.longitude(55.2714443)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("3")
				.latitude(25.1954312)
				.longitude(55.2811432)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("4")
				.latitude(25.1903843)
				.longitude(55.2798557)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("5")
				.latitude(25.2002450)
				.longitude(55.2734184)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("6")
				.latitude(25.2028848)
				.longitude(55.2783966)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("7")
				.latitude(25.2012544)
				.longitude(55.2569389)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("8")
				.latitude(25.1644242)
				.longitude(55.2450943)
				.build());


		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("9")
				.latitude(25.0763827)
				.longitude(55.1616669)
				.build());
	}
}
