package bearmaps.proj2ab;

import java.util.List;

/**
 * @author Zhiwei_Zhou
 * @create 2021-02-04 20:45
 */
public class KDTree implements PointSet{

    private node head = null;

    private class node implements Comparable<node>{
        Point point;
        node left;
        node right;
        boolean leftright = true;

        public node(Point point) {
            this.point = point;
            this.left = null;
            this.right = null;
        }

        public void setdirection() {
            leftright = !leftright;
        }

        @Override
        public int compareTo(node o) {
            if(leftright) {
                return Double.compare(this.point.getX(), o.point.getX());
            } else {
                return Double.compare(this.point.getY(), o.point.getY());
            }
        }
    }

    private node add(node x, node item) {
        if(x == null) return item;
        int cmp = x.compareTo(item);
        if(cmp > 0) {
            item.setdirection();
            x.left = add(x.left, item);
        }
        else if(cmp < 0) {
            item.setdirection();
            x.right = add(x.right, item);
        }
        else if(x.point.equals(item.point)) return x;
        else {
            item.setdirection();
            x.right = add(x.right, item);
        }
        return x;
    }

    public KDTree(List<Point> points) {
        for (Point a :
                points) {
            node next = new node(a);
            head = add(head, next);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        node best = head;
        Point goal = new Point(x, y);
        best = nearhelper(head, goal, best);
        return best.point;
    }

    private node nearhelper(node x, Point goal, node best) {
        if(x == null) return best;
        if(distance(x.point, goal) < distance(best.point, goal)) best = x;
        if(x.compareTo(new node(goal)) <= 0) {
            best = nearhelper(x.right, goal, best);  //goodside
            if((x.leftright && Math.abs(x.point.getX()-goal.getX()) < distance(best.point, goal)) ||
                    (!x.leftright && Math.abs(x.point.getY()-goal.getY()) < distance(best.point, goal))) {
                best = nearhelper(x.left, goal, best); //badside
            }
        }
        else {
            best = nearhelper(x.left, goal, best);  //goodside
            if((x.leftright && Math.abs(x.point.getX()-goal.getX()) < distance(best.point, goal)) ||
                    (!x.leftright && Math.abs(x.point.getY()-goal.getY()) < distance(best.point, goal))) {
                best = nearhelper(x.right, goal, best); //badside
            }
        }
        return best;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.getX()-p2.getX()), 2) + Math.pow((p1.getY()-p2.getY()), 2));
    }
}
