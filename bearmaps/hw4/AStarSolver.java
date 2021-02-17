package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zhiwei_Zhou
 * @create 2021-02-07 21:54
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private List<Vertex> solution = new ArrayList<>();
    private double timespent;
    private double weight;
    private int num = 0;

    private HashMap<Vertex, Double> distTo = new HashMap<>(); //initial it
    private HashMap<Vertex, Vertex> edgeTo = new HashMap<>();
    private ArrayHeapMinPQ<Vertex> fringe = new ArrayHeapMinPQ<>();

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        distTo.put(start, 0.0);
        fringe.add(start, distTo.get(start) + input.estimatedDistanceToGoal(start, end));
        Vertex point = start;
        Vertex next = start;
        Stopwatch time = new Stopwatch();
        while(fringe.size() != 0 && (!fringe.getSmallest().equals(end)) && time.elapsedTime() < timeout) {
            num += 1;
            point = fringe.removeSmallest();
            List<WeightedEdge<Vertex>> neighbors = input.neighbors(point);
            for (WeightedEdge<Vertex> e:
                 neighbors) {
                relax(e, input, end);
            }
        }
        timespent = time.elapsedTime();
        if(fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
        } else if(fringe.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                weight = distTo.get(end);
                Vertex new_point = end;
                while(!new_point.equals(start)) {
                    solution.add(new_point);
                    new_point = edgeTo.get(new_point);
                }
                solution.add(start);
                Collections.reverse(solution);
            }
        else outcome = SolverOutcome.TIMEOUT;
    }

    private void relax(WeightedEdge<Vertex> e, AStarGraph<Vertex> G, Vertex goal) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();

        double distance = distTo.get(p);
        if(!distTo.containsKey(q) || distance + w < distTo.get(q)) {
            distTo.put(q, distance + w);
            edgeTo.put(q, p);
            if(fringe.contains(q)) {
                fringe.changePriority(q,distTo.get(q) + G.estimatedDistanceToGoal(q, goal));
            } else {
                fringe.add(q, distTo.get(q) + G.estimatedDistanceToGoal(q, goal));
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        if(outcome == SolverOutcome.TIMEOUT || outcome == SolverOutcome.UNSOLVABLE) return null;
        return solution;
    }

    @Override
    public double solutionWeight() {
        if(outcome == SolverOutcome.TIMEOUT || outcome == SolverOutcome.UNSOLVABLE) return 0;
        return weight;
    }

    @Override
    public int numStatesExplored() {
        return num;
    }

    @Override
    public double explorationTime() {
        return timespent;
    }
}
