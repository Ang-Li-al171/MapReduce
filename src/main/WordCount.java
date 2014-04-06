import java.io.IOException;
import java.util.*;
import map.*;
import reduce.*;
import output.*;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
*/

public class WordCount {

	public static class Map<K1,V1,K2,V2> implements Mapper<K1,V1,K2,V2> {
		public void map(int key, String value, OutputCollector output) throws IOException {

		}
	}

	// public static class Combine implements Combiner<K2,V2,K3,V3> { //This is less important, we can do it later
	// 	public void combine(int key, Iterator<String> values) throws IOException {

	// 	}
	// }

	public static class Reduce<K2,V2,K3,V3> implements Reducer<K2,V2,K3,V3> {
		public void reduce(int key, Iterator<String> values, OutputCollector output) throws IOException {
			//Shuffle

			//Sort

			//Reduce

		}
	}

	public static void main(String[] args) throws Exception { //Specify the job configurations, mapper, reducer, etc
		System.out.println("Running MR job");
	}

}