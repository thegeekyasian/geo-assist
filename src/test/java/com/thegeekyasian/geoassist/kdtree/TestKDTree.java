package com.thegeekyasian.geoassist.kdtree;

import java.util.Arrays;
import java.util.List;

import com.thegeekyasian.geoassist.core.GeoAssistException;
import com.thegeekyasian.geoassist.kdtree.geometry.BoundingBox;
import com.thegeekyasian.geoassist.kdtree.geometry.Point;
import com.thegeekyasian.geoassist.kdtree.geometry.Point.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The TestKDTree class is a test class for the KD-Tree data structure.
 * It is used to test the insertion and searching of objects in the KD-Tree.
 *
 * @author The Geeky Asian
 */
public class TestKDTree {

	private KDTree<String, Object> kdTree;

	@Test
	public void testInsertion() {
		Assertions.assertEquals(this.kdTree.getSize(), 9);
	}

	@Test
	public void testDeletion() {
		boolean deleted = this.kdTree.delete("2");
		Assertions.assertTrue(deleted);
	}

	@Test
	public void testGetByID() {
		KDTreeObject<String, Object> kdTreeObject = this.kdTree.getById("4");

		Assertions.assertEquals(kdTreeObject.getId(), "4");
		Assertions.assertEquals(kdTreeObject.getPoint().getLatitude(), 25.1903843);
		Assertions.assertEquals(kdTreeObject.getPoint().getLongitude(), 55.2798557);
		Assertions.assertNull(kdTreeObject.getData());
	}

	@Test
	public void testGetByID_returnsNullForNonExistentObject() {
		KDTreeObject<String, Object> kdTreeObject = this.kdTree.getById("11");
		Assertions.assertNull(kdTreeObject);
	}

	@Test
	public void testUpdate() {
		this.kdTree.update("3", "test-data");
		KDTreeObject<String, Object> kdTreeObject = this.kdTree.getById("3");
		Assertions.assertEquals(kdTreeObject.getData(), "test-data");
	}

	@Test
	public void testUpdate_throwsNotFoundException() {

		GeoAssistException geoAssistException = Assertions.assertThrows(GeoAssistException.class,
				() -> this.kdTree.update("100", "test-data"));

		Assertions.assertNotNull(geoAssistException);
		Assertions.assertEquals(geoAssistException.getMessage(), "No object found for provided ID");
	}

	@Test
	public void testFindNearestNeighbors() {
		List<KDTreeObject<String, Object>> nearestNeighbors = this.kdTree.findNearestNeighbor(
				new Builder()
						.latitude(25.2012544)
						.longitude(55.2569389)
						.build(), 2);

		Assertions.assertEquals(4, nearestNeighbors.size());
		Assertions.assertEquals("1", nearestNeighbors.get(0).getId());
		Assertions.assertEquals("5", nearestNeighbors.get(1).getId());
		Assertions.assertEquals("7", nearestNeighbors.get(2).getId());
		Assertions.assertEquals("2", nearestNeighbors.get(3).getId());
	}

	@Test
	public void testFindNearestNeighbors_returnsEmptyListForNoNearestNeighbors() {
		List<KDTreeObject<String, Object>> nearestNeighbors = this.kdTree.findNearestNeighbor(
				new Builder()
						.latitude(0.2012544)
						.longitude(55.2569389)
						.build(), 2);

		Assertions.assertEquals(0, nearestNeighbors.size());
	}

	@Test
	public void testFindNearest() {
		KDTreeNearestNeighbor<String, Object> nearestNeighbor = this.kdTree.findNearest(
			new Builder()
				.latitude(25.2028848)
				.longitude(55.289930)
				.build(), 2);

		Assertions.assertNotNull(nearestNeighbor);
		Assertions.assertEquals(1.1603729958857059, nearestNeighbor.getDistance());
		Assertions.assertEquals("6", nearestNeighbor.getKdTreeObject().getId());
	}

	@Test
	public void testFindNearest_whenExistingPointIsProvided_returnsZeroDistance() {
		KDTreeNearestNeighbor<String, Object> nearestNeighbor = this.kdTree.findNearest(
			new Builder()
				.latitude(25.2012544)
				.longitude(55.2569389)
				.build(), 2);

		Assertions.assertNotNull(nearestNeighbor);
		Assertions.assertNotNull(nearestNeighbor.getDistance());
		Assertions.assertEquals(0.0, nearestNeighbor.getDistance());
		Assertions.assertNotNull(nearestNeighbor.getKdTreeObject());
		Assertions.assertEquals("7", nearestNeighbor.getKdTreeObject().getId());
	}


	@Test
	public void testFindNearest_returnsNullWhenNotFound() {
		KDTreeNearestNeighbor<String, Object> nearestNeighbor = this.kdTree.findNearest(
			new Builder()
				.latitude(25.2028848)
				.longitude(55.089930)
				.build(), 2);

		Assertions.assertNotNull(nearestNeighbor);
		Assertions.assertNull(nearestNeighbor.getKdTreeObject());
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

		Assertions.assertFalse(beforeBalancing);
		Assertions.assertTrue(afterBalancing);
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

		Assertions.assertNotNull(objects);
		Assertions.assertFalse(objects.isEmpty());
		Assertions.assertEquals(expectedIDs.size(), objects.size());

		for (int i = 0; i < objects.size(); i++) {
			Assertions.assertEquals(expectedIDs.get(i), objects.get(i).getId());
		}
	}

	@Test
	public void testSizeWhenIdIsNotProvided() {
		KDTree<String, Object> kdTree = new KDTree<>();

		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.id("1")
				.latitude(25.1967512)
				.longitude(55.2732038)
				.build());

		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.latitude(25.1962077)
				.longitude(55.2714443)
				.build());

		kdTree.insert(new KDTreeObject.Builder<String, Object>()
				.latitude(25.1954312)
				.longitude(55.2811432)
				.build());

		Assertions.assertEquals(3, kdTree.getSize());
	}

	@BeforeEach
	public void beforeEach() {
		this.kdTree = new KDTree<>();
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

		init(this.kdTree, coordinates);
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
