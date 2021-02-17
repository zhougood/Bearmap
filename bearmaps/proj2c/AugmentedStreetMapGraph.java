package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.WeirdPointSet;
import bearmaps.hw4.MyTrieSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private KDTree nodetree;
    //private WeirdPointSet nodetree;
    private HashMap<Point, Node> positionMap = new HashMap<>();
    private MyTrieSet trie = new MyTrieSet();
    private HashMap<String, List<Node>> nametonode = new HashMap<>();


    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        ArrayList<Point> points = new ArrayList<>();
        for (Node a: nodes) {
            if(a.name() != null) {
                String temp_name = cleanString(a.name());
                trie.add(temp_name);
                if(nametonode.containsKey(temp_name)) {
                    nametonode.get(temp_name).add(a);
                } else {
                    ArrayList<Node> node = new ArrayList<>();
                    node.add(a);
                    nametonode.put(temp_name, node);
                }
            }
            if(this.neighbors(a.id()).size() != 0) {
                Point item = new Point(a.lon(), a.lat());
                points.add(item);
                positionMap.put(item, a);
            }
        }
        //nodetree = new WeirdPointSet(points);
        nodetree = new KDTree(points);
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = nodetree.nearest(lon, lat);
        Node node = positionMap.get(nearest);
        return node.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String temp = cleanString(prefix);
        ArrayList<String> fullname = new ArrayList<>();
        for(String i : trie.keysWithPrefix(temp)) {
            fullname.add(nametonode.get(i).get(0).name());
        }
        return fullname;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        locationName = cleanString(locationName);
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for(Node a: nametonode.get(locationName)) {
            HashMap<String, Object> b = new HashMap<>();
            b.put("name", a.name());
            b.put("lat", a.lat());
            b.put("lon", a.lon());
            b.put("id", a.id());
            result.add(b);
        }

        return result;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
