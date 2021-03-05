import java.io.*;
import java.util.*;
class Fibonoic_Heap {
	Queue que = new LinkedList();
	/* we initialize the maximum of heap to null pointer */
	Node m_ax = null;
	HashMap<String, Node> hashMap;
	int size = 0;
	public Node insert(int value, String k_ey) {
		Node result = new Node(value, k_ey);

		/* we merge treelist and singleton list */
			m_ax = meld(m_ax, result);

		/* heapsize incrementation */
		++size;

		return result;
	}
	//Returns when heap is empty.

	public boolean isEmpty() {
		return m_ax == null;
	}
	
	/**
	 * Returns the maximum value of the heap
	 */
	public Node m_ax() {
		if (m_ax == null)
			throw new NoSuchElementException("Heap is empty.");
		return m_ax;
	}

	/**
	 * Dequeues and returns the maximum element of the Fibonacci heap. If the
	 * heap is empty, this throws a NoSuchElementException.
	 */
	public Node removeMax() {
		if (m_ax == null){
			throw new NoSuchElementException("Heap is empty.");
		}
		--size;
		/*we remove max element of heap*/
		Node max_element = m_ax;

		/*maximum element is removed from root list
		 * max is null if it is only element in it
		 * else, assign max to the node next to max
		 * */
		if (m_ax.next == m_ax) {
			m_ax = null;
		} else { 
			m_ax.prev.next = m_ax.next;
			m_ax.next.prev = m_ax.prev;
			m_ax = m_ax.next;
		}
		//reassign parent field of child to null.
		if (max_element.child != null) {
			Node curr = max_element.child;
			do {
				curr.parent = null;
				curr = curr.next;
			} while (curr != max_element.child);
		}
		//move children
		m_ax = meld(m_ax, max_element.child);

		if (m_ax == null)
			//empty list
			return max_element;

		/*keeping track degree of trees*/
		List<Node> treeTab = new ArrayList<Node>();

		/*traversal*/
		List<Node> nodeToVisit = new ArrayList<Node>();

		for (Node curr = m_ax; nodeToVisit.isEmpty() || nodeToVisit.get(0) != curr; curr = curr.next)
			nodeToVisit.add(curr);

		for (Node curr : nodeToVisit) {
			while (true) {
				while (curr.deg >= treeTab.size()){
					treeTab.add(null);
				}

				/*traversing untill same degree trees are found.*/
				if (treeTab.get(curr.deg) == null) {
					treeTab.set(curr.deg, curr);
					break;
				}

				Node other = treeTab.get(curr.deg);
				treeTab.set(curr.deg, null);

				Node min = (other.data < curr.data) ? other : curr;
				Node m_ax = (other.data < curr.data) ? curr : other;

				/*Remove the minimum element from the list*/
				min.next.prev = min.prev;
				min.prev.next = min.next;
				/*pairwise combine*/
				min.next = min.prev = min;
				m_ax.child = meld(m_ax.child, min);
				min.parent = m_ax;

				min.child_Cutvalue = false;
				++m_ax.deg;
				curr = m_ax;
			}
			//max element updation
			if (curr.data >= m_ax.data){
				m_ax = curr;
			}
		}
		return max_element;
	}

	/**
	 * Increases the key of a node by the specified value
	 */
	public void increaseKey(Node heapNode, int addVal) {
		/* Increase the node value by addVal*/
		heapNode.data += addVal;

		/*If the node's new value is greater than that of it's parent then cut the node and insert it into the root list*/
		if (heapNode.parent != null && heapNode.data >= heapNode.parent.data){
			childCut(heapNode);
		}

		/*If the new value of the node is greater than the max node's value then the max node is pointed to the current node with the increased value*/
		if (heapNode.data >= m_ax.data){
			m_ax = heapNode;
		}
	}
	
	
	/**
	 * Merges two doubly linked lists in O(1)time
	 * 
	 * @param tree1
	 *            A pointer into p of the q linked lists.
	 * @param tree2
	 *            A pointer into the other of the q linked lists.
	 * @return A pointer to the smallest element of the resulting list.
	 */
	private static Node meld(Node tree1, Node tree2) {
		if (tree1 != null && tree2 != null) { // Both null, resulting list is null.
			Node temp = tree1.next; 
			tree1.next = tree2.next;
			tree1.next.prev = tree1;
			tree2.next = temp;
			tree2.next.prev = tree2;

			/* A pointer to larger node is returned */
			if(tree1.data>tree2.data)
				return tree1;
			else
				return tree2;
		} else if (tree1 != null && tree2 == null) { // q is null, result is p.
			return tree1;
		} else if (tree1 == null && tree2 != null) { // p is null, result is q.
			return tree2;
		} else { // combine the lists if both p and q is non null
			return null;
		}
	}
	
	/**
	 * Removes the first N max elements from the Fibonacci heap.
	 * @param numb
	 *            The number of maxes to remove
	 */
	public void remove_nMax(int numb) throws Exception {
		/*The output is written to the output_file.txt*/
		FileWriter printer = new FileWriter(new File("output_file.txt"), true);
		PrintWriter pw = new PrintWriter(printer);
		for (int i = 0; i < numb; i++) {
			/* Call removeMax n times to remove n maxes */
			Node m_ax = removeMax();
			pw.write(m_ax.key);
			if (i != numb - 1) {
				pw.write(",");
			}
			// print(m_ax,1);
			que.add(m_ax);
		}
		pw.println();
		pw.flush();
		pw.close();
		while (true) {
			if(que.isEmpty())
				break;
			Node p = (Node) que.remove();
			Node q = insert(p.data, p.key);
			hashMap.get(p.key).next = q;
		}
	}
	
	
	/**
	 * Recursively cuts the marked parents of a node
	 *
	 * @param heap_Node
	 *            The node to cut from its parent.
	 */
	private void childCut(Node heap_Node) {
		heap_Node.child_Cutvalue = false;

		if (heap_Node.parent == null)
			return;

		/*remove the node from it's siblings list*/
		if (heap_Node.next != heap_Node) {
			heap_Node.next.prev = heap_Node.prev;
			heap_Node.prev.next = heap_Node.next;
		}

		/*Change  the child pointer of the parent of the cut node, if necessary*/
		if (heap_Node.parent.child == heap_Node) {
			if (heap_Node.next != heap_Node) {
				heap_Node.parent.child = heap_Node.next;
			}else {
				heap_Node.parent.child = null;
			}
		}

		--heap_Node.parent.deg;

		/*Add the cut node to the root list*/
		heap_Node.prev = heap_Node.next = heap_Node;
		m_ax = meld(m_ax, heap_Node);

		/*Recursively cut the parents if marked already else mark their childCut to true*/
		if (heap_Node.parent.child_Cutvalue)
			childCut(heap_Node.parent);
		else
			heap_Node.parent.child_Cutvalue = true;

		heap_Node.parent = null;
	}
}
