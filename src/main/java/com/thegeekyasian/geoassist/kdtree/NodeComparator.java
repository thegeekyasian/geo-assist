package com.thegeekyasian.geoassist.kdtree;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author thegeekyasian
 */
public class NodeComparator<T, O> implements Comparator<KDTreeNode<T, O>>, Serializable {
    @Override
    public int compare(final KDTreeNode<T, O> o1, final KDTreeNode<T, O> o2) {
        final int depth = o1.getDepth() % 2;
        if (depth == 0) {
            return Double.compare(o1.getKdTreeObject().getPoint().getLongitude(),
                    o2.getKdTreeObject().getPoint().getLongitude());
        }
        return Double.compare(o1.getKdTreeObject().getPoint().getLatitude(),
                o2.getKdTreeObject().getPoint().getLatitude());
    }
}