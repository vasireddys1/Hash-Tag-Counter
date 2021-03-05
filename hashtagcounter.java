import java.io.*;
import java.util.*;
public class hashtagcounter {
	public static void main(String[] args) throws Exception {
		FileWriter printer = new FileWriter(new File("output_file.txt"));
		printer.write("");
		printer.flush();
		printer.close();
		Fibonoic_Heap f_heap = new Fibonoic_Heap();
		f_heap.hashMap = new HashMap<String, Node>();
		String string;
		String string1;
		/* we read the file that we got from the argument*/
		File f_name = new File(args[0]);
		FileInputStream file_stream = new FileInputStream(f_name);
		BufferedReader b_read = new BufferedReader(new InputStreamReader(file_stream));
		while ((string = b_read.readLine()) != null && !string.equals("STOP")) {
			String[] s_arr = string.split(" ");
			if (string.indexOf('#') != -1) {
//				we split the input and take names in the string array and numbers in value variable.
				s_arr[0] = s_arr[0].substring(1);
				int value = Integer.parseInt(s_arr[1]);
//				we check wheather the name is already present in the heap.
				if (f_heap.hashMap.containsKey(s_arr[0])) {
					/*we increase the value of the name if it is already exists.*/
					f_heap.increaseKey(f_heap.hashMap.get(s_arr[0]).next, value);
				} else {
//					else we inserst the name into the heap.
					Node ins = f_heap.insert(value, s_arr[0]);
					Node node_pointer = new Node(-1, null);
					node_pointer.next = ins;
					f_heap.hashMap.put(s_arr[0], node_pointer);
				}
			} else {
				// we do the method removemax  num times
				Integer num = Integer.parseInt(s_arr[0]);
				f_heap.remove_nMax(num);
			}
		}
		b_read.close();
	}

}