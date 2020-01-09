package algorithms;

import java.io.*;
import java.util.*;

import dataStructure.*;
import utils.Range;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author shoval
 *
 */
public class Graph_Algo implements graph_algorithms , Serializable {
	private graph graphalgo = new DGraph();


	public Graph_Algo() {

	}

	public Graph_Algo(graph graph) {
		this.graphalgo = graph;
	}




	/**
	 * init the existing graph with g graph
	 *
	 * @param g the graph to init from
	 */
	public void init(graph g) {
		this.graphalgo = g;
	}

	public int getnodeSize() {
		return graphalgo.nodeSize();
	}

	public int getedgeSize() {
		return graphalgo.edgeSize();
	}

	/**
	 * init from file using Serializable
	 *
	 * @param file_name to init from
	 */
	public void init(String file_name) {
		Graph_Algo g;
		try {
			FileInputStream file = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(file);

			g = (Graph_Algo) in.readObject();
			this.init(g.graphalgo);

			in.close();
			file.close();

			System.out.println("Object has been deserialized");
			System.out.println(g);
		} catch (IOException ex) {
			System.out.println("IOException is caught");
		} catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}

	}

	/**
	 * save to file using Serializable
	 *
	 * @param file_name to save
	 */
	public void save(String file_name) {
		try {
			FileOutputStream file = new FileOutputStream(file_name);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(this);

			out.close();
			file.close();

			System.out.println("Object has been serialized");

		} catch (IOException ex) {
			System.out.println("IOException is caught");
		}

	}

	/**
	 * Returns true if and only if (iff) there is a valid path from EVREY node to each
	 * other node. NOTE: assume directional graph - a valid path (a-->b) does NOT imply a valid path (b-->a).
	 *
	 * @return true if it is, false otherwise
	 */
	public boolean isConnected() {
		Boolean[] vNodes = new Boolean[this.graphalgo.nodeSize()];
		for (int i = 0; i < vNodes.length; i++) // Initialize as not visited
			vNodes[i] = false;

		DFSUtil((DGraph) this.graphalgo, 1, vNodes);
		for (int i = 0; i < vNodes.length; i++) // Check graphAlgo's connectivity
			if (!vNodes[i]) return false;

		DGraph tGraph = transpose();
		for (int i = 0; i < vNodes.length; i++) // Initialize as not visited
			vNodes[i] = false;

		DFSUtil(tGraph, 1, vNodes);
		for (int i = 0; i < vNodes.length; i++) // Check graphAlgo's transpose connectivity
			if (!vNodes[i]) return false;

		return true;
	}

	/**
	 * returns the length of the shortest path between src to dest
	 *
	 * @param src  - start node
	 * @param dest - end (target) node
	 * @return length
	 */
	public double shortestPathDist(int src, int dest) {
		node_data startNode = this.graphalgo.getNode(src);
		startNode.setWeight(0);
		startNode.setInfo("Not visited");

		Collection<node_data> nodesCol = this.graphalgo.getV();
		PriorityQueue<node_data> gN = new PriorityQueue<>();

		for (node_data graphNode : nodesCol) { // Initialize graphAlgo nodes parameters
			if (graphNode.getKey() != src) {
				graphNode.setWeight(Double.POSITIVE_INFINITY);
				graphNode.setInfo("Not visited");
				graphNode.setTag(-1);
			}
		}

		gN.add(startNode); // gN (Priority queue) contains startNode

		while (!gN.isEmpty()) {
			node_data anyNode = gN.remove();
			anyNode.setInfo("Visited");
			Collection<edge_data> edgesCol = this.graphalgo.getE(anyNode.getKey()); // anyNode edges

			if (edgesCol != null) {
				for (edge_data graphEdge : edgesCol) {
					node_data nNode = this.graphalgo.getNode(graphEdge.getDest()); // Destination node

					if (anyNode.getWeight() + graphEdge.getWeight() < nNode.getWeight()) { // Relaxation
						nNode.setWeight(anyNode.getWeight() + graphEdge.getWeight());
						nNode.setTag(anyNode.getKey());

						gN.add(nNode);
					}
				}

			}

		}
		return this.graphalgo.getNode(dest).getWeight();
	}

	/**
	 * returns the the shortest path between src to dest - as an ordered List of nodes:
	 * src--> n1-->n2-->...dest
	 *
	 * @param src  - start node
	 * @param dest - end (target) node
	 * @return list of nodes
	 */
	public List<node_data> shortestPath(int src, int dest) {
		if (this.shortestPathDist(src, dest) == Double.POSITIVE_INFINITY) // Check path nonexistence
			return null;

		LinkedList<node_data> pathNodes = new LinkedList<>();
		node_data anyNode = this.graphalgo.getNode(dest);

		while (anyNode.getKey() != src) { // Build a destination -> source path using anyNode's predecessor
			pathNodes.add(anyNode);
			anyNode = this.graphalgo.getNode(anyNode.getTag());
		}

		pathNodes.add(anyNode);
		LinkedList<node_data> newPathNodes = new LinkedList<>();
		int i = 1;

		while (i <= pathNodes.size()) { // Change path to source -> destination
			newPathNodes.add(pathNodes.get(pathNodes.size() - i));
			i++;
		}

		return newPathNodes;
	}

	/**
	 * computes a relatively short path which visit each node in the targets List.
	 * Note: this is NOT the classical traveling salesman problem,
	 * as you can visit a node more than once, and there is no need to return to source node -
	 * just a simple path going over all nodes in the list.
	 *
	 * @param targets the list we want to check
	 * @return list of nodes
	 */
	public List<node_data> TSP(List<Integer> targets) {
		if (targets.size() == 0) return null;

		LinkedList<node_data> nodesPath = new LinkedList<node_data>();
		int i = 0;
		int srcNode = targets.get(i++);

		if (targets.size() == 1) { // In case target list contains 1 element
			nodesPath.add(this.graphalgo.getNode(srcNode));
			return nodesPath;
		}

		while (i < targets.size()) { // In case target list contains more than 1 element
			int destNode = targets.get(i++);
			if (shortestPath(srcNode, destNode) == null) return null; // No path
			LinkedList<node_data> newPath = (LinkedList<node_data>)shortestPath(srcNode, destNode);
			if (i != 2) // Remove newPath's srcNode to prevent duplications
				newPath.remove(newPath.get(0));
			nodesPath.addAll(newPath);
			srcNode = destNode; // srcNode is the second element on the target list
		}

		return nodesPath;
	}

	/**
	 * Compute a deep copy of this graph.
	 *
	 * @return graph
	 */
	public graph copy() {

		graph Graph = new DGraph();
		Collection<node_data> new_data = this.graphalgo.getV(); //extract the DataNodes
		Iterator<node_data> iter = new_data.iterator();

		while (iter.hasNext()) {
			Graph.addNode(iter.next());
		} // adding the nodes to the existing graph
		iter = new_data.iterator();
		while (iter.hasNext()) {
			node_data temp = iter.next();
			if (this.graphalgo.getE(temp.getKey()) != null) {
				Collection<edge_data> new_edge = this.graphalgo.getE(temp.getKey());
				Iterator<edge_data> iter_edge = new_edge.iterator();
				while (iter_edge.hasNext()) {
					edge_data t = iter_edge.next();
					Graph.connect(t.getSrc(), t.getDest(), t.getWeight());
				}
			}
		}
		return Graph;
	}

	/**
	 * build a transpose graph
	 * @return transpose graph
	 */

	private DGraph transpose() {
		DGraph tranGraph = new DGraph();
		Collection<node_data> dataCol = this.graphalgo.getV();
		Iterator<node_data> iterNodes = dataCol.iterator();

		while (iterNodes.hasNext()) tranGraph.addNode(iterNodes.next()); // Adding all nodes
		iterNodes = dataCol.iterator();

		while (iterNodes.hasNext()) {
			node_data temp = iterNodes.next();
			if (this.graphalgo.getE(temp.getKey()) != null) {
				Collection<edge_data> edgeCol = this.graphalgo.getE(temp.getKey());
				Iterator<edge_data> iterEdges = edgeCol.iterator();
				while (iterEdges.hasNext()) {
					edge_data tempe = iterEdges.next();
					tranGraph.connect(tempe.getDest(), tempe.getSrc(), tempe.getWeight()); // connect between destination to source
				}
			}
		}
		return tranGraph;
	}

	/**
	 * help to check if the graph is connected
	 * @param anyGraph graph
	 * @param startNode source node
	 * @param vNodes the array of nodes
	 */
	private void DFSUtil(DGraph anyGraph, int startNode, Boolean[] vNodes) {
		if (vNodes.length < 1)
			return;
		vNodes[startNode - 1] = true; // Mark specified node as visited

		if (anyGraph.getE(startNode) != null) { // Specified node must contain at least one edge
			Collection<edge_data> edgesCol = anyGraph.getE(startNode);
			Iterator<edge_data> edgesIter = edgesCol.iterator();

			while (edgesIter.hasNext()) {
				edge_data eD = edgesIter.next();
				int edgeDest = eD.getDest();

				if (!vNodes[edgeDest - 1])
					DFSUtil(anyGraph, edgeDest, vNodes);
			}
		}
	}

	/**
	 * width to gui
	 * @return the width
	 */
	public int get_Width() {
		int min = 0;
		int max = 0;
		Collection<node_data> col = this.graphalgo.getV();
		for (node_data temp : col) {
			if (temp.getLocation().x() < min)
				min = (int) temp.getLocation().x();

			if (temp.getLocation().x() > max)
				max = (int) temp.getLocation().x();

		}
		Range rx = new Range(min*2, max*2);
		return (int) rx.get_length();
	}

	/**
	 * height to gui
	 * @return height
	 */
	public int get_Height() {
		int min = 0;
		int max = 0;
		Collection<node_data> col = this.graphalgo.getV();
		for (node_data temp : col) {
			if (temp.getLocation().y() < min)
				min = (int) temp.getLocation().y();

			if (temp.getLocation().y() > max)
				max = (int) temp.getLocation().y();

		}
		Range ry = new Range(min *2 , max *2);
		return (int) ry.get_length();
	}


}