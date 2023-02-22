package com.thegeekyasian.geoassist.kdtree;

import java.util.Arrays;
import java.util.List;

import com.thegeekyasian.geoassist.core.GeoAssistException;
import com.thegeekyasian.geoassist.kdtree.geometry.BoundingBox;
import com.thegeekyasian.geoassist.kdtree.geometry.Point;
import com.thegeekyasian.geoassist.kdtree.geometry.Point.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

	@Test
	public void testBalancing() {

		KDTree<String, Object> kdTree = new KDTree<>();
		init(kdTree, new String[] {
				"24.876282,67.022481",
				"24.867244,67.131593",
				"24.876997,67.120129",
				"24.904153,67.012868",
				"24.933166,67.101917",
				"24.889639,67.069044",
				"24.894643,67.085695",
				"24.950982,67.051792",

				"24.952772,67.054882",
				"24.969653,67.071791",
				"24.990187,67.017117",
				"24.916059,67.171783",
				"24.829804,67.037544",
				"24.950607,66.835670",
				"25.027137,8.393555",
				"-2.054868,34.189453",
				"25.050570,67.009735",
				"25.191657,66.834984",
				"24.927578,66.989136",
				"24.952772,67.054882",
		});

		boolean beforeBalancing = kdTree.isBalanced();
		kdTree.balance();
		boolean afterBalancing = kdTree.isBalanced();

		assertFalse(beforeBalancing);
		assertTrue(afterBalancing);
	}

	@Test
	public void testBoundingBoxSearch_returnsAllObjectsWithinBounds() {

		KDTree<String, Object> kdTree = new KDTree<>();
		init(kdTree, new String[] {
				"24.876282,67.022481", // <--
				"24.867244,67.131593", // <--
				"24.876997,67.120129", // <--
				"24.904153,67.012868", // <--
				"24.933166,67.101917", // <--
				"24.889639,67.069044", // <--
				"24.894643,67.085695", // <--
				"24.950982,67.051792", // <--

				"24.952772,67.054882",
				"24.969653,67.071791",
				"24.990187,67.017117",
				"24.916059,67.171783",
				"24.829804,67.037544",
				"24.950607,66.835670",
				"25.027137,8.393555",
				"-2.054868,34.189453",
				"25.050570,67.009735",
				"25.191657,66.834984",
				"24.927578,66.989136", // <--
				"24.952772,67.054882",
				"24.941142,66.976218", // <--
				"24.954251,66.972613",
				"24.953162,66.995788",
				"24.951256,67.031279", // <--
		});

		BoundingBox boundingBox = new BoundingBox.Builder()
				.lowerPoint(new Point.Builder()
						.latitude(24.836135)
						.longitude(66.976089)
						.build())
				.upperPoint(new Point.Builder()
						.latitude(24.951953)
						.longitude(67.157364)
						.build())
				.build();


		List<KDTreeObject<String, Object>> objects = kdTree.findInRange(boundingBox);

		assertBoundingBoxSearch(
				Arrays.asList("1", "2", "3", "4", "6", "7", "5", "8", "19", "21", "24"),
				objects);
	}


	@Test
	public void testBoundingBoxSearchWithBalancedTree_returnsAllObjectsWithinBounds() {

		KDTree<String, Object> kdTree = new KDTree<>();
		init(kdTree, new String[] {
				"24.876282,67.022481", // <--
				"24.867244,67.131593", // <--
				"24.876997,67.120129", // <--
				"24.904153,67.012868", // <--
				"24.933166,67.101917", // <--
				"24.889639,67.069044", // <--
				"24.894643,67.085695", // <--
				"24.950982,67.051792", // <--

				"24.952772,67.054882",
				"24.969653,67.071791",
				"24.990187,67.017117",
				"24.916059,67.171783",
				"24.829804,67.037544",
				"24.950607,66.835670",
				"25.027137,8.393555",
				"-2.054868,34.189453",
				"25.050570,67.009735",
				"25.191657,66.834984",
				"24.927578,66.989136", // <--
				"24.952772,67.054882",
				"24.941142,66.976218", // <--
				"24.954251,66.972613",
				"24.953162,66.995788",
				"24.951256,67.031279", // <--
		});

		BoundingBox boundingBox = new BoundingBox.Builder()
				.lowerPoint(new Point.Builder()
						.latitude(24.836135)
						.longitude(66.976089)
						.build())
				.upperPoint(new Point.Builder()
						.latitude(24.951953)
						.longitude(67.157364)
						.build())
				.build();

		kdTree.balance();


		List<KDTreeObject<String, Object>> objects = kdTree.findInRange(boundingBox);

		assertBoundingBoxSearch(
				Arrays.asList("6", "4", "1", "19", "21", "7", "2", "3", "5", "8", "24"),
				objects);
	}

	private void assertBoundingBoxSearch(List<String> expectedIDs,
			List<KDTreeObject<String, Object>> objects) {

		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(expectedIDs.size(), objects.size());

		for (int i = 0; i < objects.size(); i++) {
			assertEquals(expectedIDs.get(i), objects.get(i).getId());
		}
	}

	@BeforeEach
	public void beforeEach() {
		kdTree = new KDTree<>();
		String[] coordinates = new String[] {
				"25.1967512,55.2732038",
				"25.1962077,55.2714443",
				"25.1954312,55.2811432",
				"25.1903843,55.2798557",
				"25.2002450,55.2734184",
				"25.2028848,55.2783966",
				"25.2012544,55.2569389",
				"25.1644242,55.2450943",
				"25.0763827,55.1616669"
		};

		init(kdTree, coordinates);
	}

	public static void init(KDTree<String, Object> kdTree, String[] coordinates) {
		for (int i = 0; i < coordinates.length; i++) {
			String[] point = coordinates[i].split(",");

			if (point.length < 2) {
				throw new RuntimeException("Incorrect geo data provided.");
			}

			kdTree.insert(new KDTreeObject.Builder<String, Object>()
					.id(String.valueOf(i + 1))
					.latitude(Double.parseDouble(point[0].trim()))
					.longitude(Double.parseDouble(point[1].trim()))
					.build());
		}
	}
}
