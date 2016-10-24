  package mapreduce.counters.com;


import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CountersReducer extends Reducer<Text,IntWritable, Text, IntWritable  >{
	
	@Override
	public void reduce(Text key ,Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
		
		int count = 0;
		for (IntWritable value : values){
			
			count = count + Integer.parseInt(value.toString());
		}
		context.write(key, new IntWritable(count));
		
	}
	
}
