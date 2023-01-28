package Hadoop.Food;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class FoodMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
	
	String filter;

	@Override
	public void configure(JobConf job) {
		filter = job.get("filter", "all").toLowerCase();
	}
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter)
			throws IOException {
		String[] data = value.toString().toLowerCase().split(";");
		
		if(filter.equalsIgnoreCase("all")
				|| data[1].contains(filter)) {
			try {
			String cerealName = data[0];
			
			double calories = Double.parseDouble(data[3]);
			float weight = Float.parseFloat(data[13]);
			
			double sum = (calories/(weight*100));
			
			
			output.collect(new Text(cerealName), new DoubleWritable(sum));
			}
			catch(NumberFormatException e) {
				System.err.println(value.toString());
			}
		}
	}

}
