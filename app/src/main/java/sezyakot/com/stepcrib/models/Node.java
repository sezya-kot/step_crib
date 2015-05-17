package sezyakot.com.stepcrib.models;

/**
 * Created by cat on 5/15/2015.
 */
public class Node {

	public int n; // value of node
	public Node left; // left subtree
	public Node right; // right subtree
	public Node level; // level pointer (node "to the right")

	public static void linkSameLevel(Node t) {
		// Set the nextRight for root
		t.level = null;
		// Set the next right for rest of the nodes (other than root)
		connectRecur(t);

	}

	/* This function returns the leftmost child of nodes at the same level as p.
   This function is used to getNext right of p's right child
   If right child of p is NULL then this can also be used for the left child */
	private static Node getNextRight(Node p) {
		Node temp = p.level;

    /* Traverse nodes at p's level and find and return
       the first node's first child */
		while (temp != null) {
			if (temp.left != null)
				return temp.left;
			if (temp.right != null)
				return temp.right;
			temp = temp.level;
		}

		// If all the nodes at p's level are leaf nodes then return NULL
		return null;
	}

	/* Set next right of all descendants of p. This function makes sure that
	nextRight of nodes ar level i is set before level i+1 nodes. */
	private static void connectRecur(Node p) {
		// Base case
		if (p == null)
			return;

    /* Before setting nextRight of left and right children, set nextRight
    of children of other nodes at same level (because we can access
    children of other nodes using p's nextRight only) */
		if (p.level != null)
			connectRecur(p.level);

    /* Set the nextRight pointer for p's left child */
		if (p.left != null) {
			if (p.right != null) {
				p.left.level = p.right;
				p.right.level = getNextRight(p);
			} else
				p.left.level = getNextRight(p);

       /* Recursively call for next level nodes.  Note that we call only
       for left child. The call for left child will call for right child */
			connectRecur(p.left);
		}

    /* If left child is NULL then first node of next level will either be
      p->right or getNextRight(p) */
		else if (p.right != null) {
			p.right.level = getNextRight(p);
			connectRecur(p.right);
		} else
			connectRecur(getNextRight(p));
	}

}
