package mapreduce.counters.com;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import mapreduce.counters.com.CountersMapper.Mycounters;

public class CountersDriver {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Configuration conf = new Configuration();
		
		try {
			Job job = new Job(conf,"Counters and Partitioner Implemantation");
		
		job.setJarByClass(CountersDriver.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setMapperClass(CountersMapper.class);
		job.setReducerClass(CountersReducer.class);
		job.setPartitionerClass(CountersPartitioner.class);
		
		System.out.println("check1"); 
		
		
		FileSystem hdfs =FileSystem.get(new Configuration());
		
		//Path workingDir=hdfs.getWorkingDirectory();
		Path workingDir=new Path("hdfs://nameserviceEDHDEV");
		
		Path newFolderPath= new Path(args[1]);
		
		newFolderPath=Path.mergePaths(workingDir, newFolderPath);
		System.out.println("Print oo laaa he ");
		System.out.println(newFolderPath.toString()); 

		if(hdfs.exists(newFolderPath))
		{
			hdfs.delete(newFolderPath, true); //Delete existing Directory
		}
		
		
		FileInputFormat.addInputPath(job, new Path (args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1])); 
		
			job.waitForCompletion(true);
		
		long count = job.getCounters().findCounter(Mycounters.ATOJ).getValue();
		System.out.println("Count" + count); 
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
