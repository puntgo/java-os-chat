package mapreduce.counters.com;

import java.util.Arrays;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class CountersPartitioner extends Partitioner<Text, IntWritable>{
	
	int numReduceTash = 3 ;
	
	
	

	@Override
	public int getPartition(Text key, IntWritable value, int numReduceTash ){
		
		String[] atoj = {"a","b","c","d","e","f","g","h","i","j"};
		String[] ktot = {"k","l","m","n","o","p","q","r","s","t"};
		
		
		if (numReduceTash == 0){
			return 0;
		}
		if ( Arrays.asList(atoj).contains(key.toString().toLowerCase().substring(0, 1)) ){   
			return 1;
			//context.getCounter(Mycounters.ATOJ).increment(1);
		}
		if ( Arrays.asList(ktot).contains(key.toString().toLowerCase().substring(0, 1)) ){    
			return 2;
		}else{
			return 3;
		}
	}
	
	
	


	}
