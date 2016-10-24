package mapreduce.counters.com;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class CountersMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	
	static final IntWritable one = new IntWritable(1);
	static enum Mycounters{	ATOJ,KTOT,UYOZ };
	
	public void map(LongWritable key, Text value , Context context){
		
		StringTokenizer token = new StringTokenizer(value.toString());	
			
		while (token.hasMoreTokens()){	
			
			String word = token.nextToken();
			
			try {
				context.write(new Text(word),one);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {	
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}

}
