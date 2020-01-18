package dataStructure;


import elements.Fruit;
import oop_elements.OOP_NodeData;
import oop_utils.OOP_Point3D;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Point3D;
import utils.Range;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DGraph implements graph, Serializable {
	private HashMap<Integer , node_data> Nodes_Map = new HashMap<>();
	private HashMap<Integer , HashMap<Integer , edge_data>> Edges_Map = new HashMap<>();
	private int NM_size;
	private int EM_size;
	private int MC;



	public DGraph(DGraph g) {
		this.Nodes_Map = g.Nodes_Map;
		this.Edges_Map = g.Edges_Map;
		this.NM_size = g.NM_size;
		this.EM_size = g.EM_size;
		this.MC = g.MC;
	}

	public DGraph(){ ;}

	public DGraph(String jsonSTR) {
		try {
			JSONObject graph = new JSONObject(jsonSTR);
			JSONArray nodes = graph.getJSONArray("Nodes");
			JSONArray edges = graph.getJSONArray("Edges");

			int i;
			int s;
			for(i = 0; i < nodes.length(); ++i) {
				s = nodes.getJSONObject(i).getInt("id");
				String pos = nodes.getJSONObject(i).getString("pos");
				Point3D p = new Point3D(pos);
				this.addNode((node_data) new DataNode(s, p));
			}

			for(i = 0; i < edges.length(); ++i) {
				s = edges.getJSONObject(i).getInt("src");
				int d = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				this.connect(s, d, w);
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		}

	}

	public void init(String jsonSTR) {
		try {
			JSONObject graph = new JSONObject(jsonSTR);
			JSONArray nodes = graph.getJSONArray("Nodes");
			JSONArray edges = graph.getJSONArray("Edges");

			int i;
			int s;
			for(i = 0; i < nodes.length(); ++i) {
				s = nodes.getJSONObject(i).getInt("id");
				String pos = nodes.getJSONObject(i).getString("pos");
				Point3D p = new Point3D(pos);
				this.addNode((node_data) new DataNode(s, p));
			}

			for(i = 0; i < edges.length(); ++i) {
				s = edges.getJSONObject(i).getInt("src");
				int d = edges.getJSONObject(i).getInt("dest");
				double w = edges.getJSONObject(i).getDouble("w");
				this.connect(s, d, w);
			}
		} catch (Exception var10) {
			var10.printStackTrace();
		}

	}

	/**
	 * this method returns the node data by the key
	 * @param key - the node_id
	 * @return node data
	 */
	public node_data getNode(int key) {
		if (Nodes_Map.containsKey(key))
			return this.Nodes_Map.get(key);
		return null;
	}

	/**
	 * this method returns the edge of 2 nodes data only if the source node exists
	 * @param src the source node
	 * @param dest the destination node
	 * @return
	 */
	public edge_data getEdge(int src, int dest) {
		if (Edges_Map.containsKey(src))
			return this.Edges_Map.get(src).get(dest);
		return null;
	}

	/**
	 * this method add a node to the nodes map if the node isn't existing yet
	 * @param n the node to add
	 */
	public void addNode(node_data n) {
		if (!Nodes_Map.containsKey(n)) {
			this.Nodes_Map.put(n.getKey(), n);
			NM_size++;
			MC++;
		}
		else {
			System.out.println("Node already exist");
		}
	}

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * Note: this method should run in O(1) time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	public void connect(int src, int dest, double w) {
		if (this.getNode(src) != null && this.getNode(dest)!= null ){
			edge_data t = new EdgeNode(src , dest , w);
			if (this.getEdge(src , dest) == null) {
				if (this.Edges_Map.get(src) == null) {
					this.Edges_Map.put(src, new HashMap<Integer, edge_data>());
					this.Edges_Map.get(src).put(dest, t);
				}
				else
					this.Edges_Map.get(src).put(dest, t);
			}
			EM_size++;
			MC++;
		}
		else {
			throw new NullPointerException("ERR:Not Existing Nodes");
		}
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph.
	 * Note: this method should run in O(1) time.
	 * @return Collection<node_data>
	 */
	public Collection<node_data> getV() {
		return this.Nodes_Map.values();
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of
	 * the given node (all the edges starting (source) at the given node).
	 * Note: this method should run in O(1) time.
	 * @return Collection<edge_data>
	 */
	public Collection<edge_data> getE(int node_id) {
		if (this.Edges_Map.containsKey(node_id))
			return this.Edges_Map.get(node_id).values();
		return null;
	}



	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none).
	 * @param key
	 */
	public node_data removeNode(int key) {
		if (this.Nodes_Map.containsKey(key) ){
			Map<Integer, node_data> map = Nodes_Map;
			for (int newKey : map.keySet()) {
				node_data newNode = map.get(newKey);
				if (this.getEdge(key, newNode.getKey()) != null)
					removeEdge(key, newNode.getKey());
				if (this.getEdge(newNode.getKey(), key) != null)
					removeEdge(newNode.getKey(), key);
			}
			this.Nodes_Map.remove(key);
			MC++;
			NM_size--;
		}
		return null;
		}

	/**
	 * Delete the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return the data of the removed edge (null if none).
	 */
	public edge_data removeEdge(int src, int dest) {
		if (this.Nodes_Map.containsKey(src)  & this.Nodes_Map.containsKey(dest)){
			edge_data rm = this.Edges_Map.get(src).get(dest);
			if (this.Edges_Map.get(src).get(dest) != null){
				this.Edges_Map.get(src).remove(dest);
				this.EM_size--;
				this.MC++;
				return rm;
			}
		}
		return null;
	}

	/** return the number of vertices (nodes) in the graph.
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	public int nodeSize() {
		return NM_size;
	}

	/**
	 * return the number of edges (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	public int edgeSize() {
		return EM_size;
	}

	/**
	 * return the Mode Count - for testing changes in the graph.
	 * @return
	 */
	public int getMC() {
		return MC;
	}

}
