import java.io.*;
import java.util.*;
class Node {
	int deg = 0;
	boolean child_Cutvalue = false;
	Node next;
	Node prev;
	Node parent;
	Node child;
	int data;
	String key;
	Node(int element, String keyValue) {
		next = prev = this;
		data = element;
		key = keyValue;
	}
}