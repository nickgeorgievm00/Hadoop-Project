package Hadoop.Food;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

import Hadoop.Food.App;
import Hadoop.Food.FoodMapper;
import Hadoop.Food.FoodReducer;

/**
 * Hello world!
 *
 */
public class App extends JFrame
{
	public App() {
		init();
	}
	
	private void init() {
		JPanel panel = new JPanel();
		
		String[] choices = {"Списък закуски", "Kалории за грам"};

	    final JComboBox<String> result = new JComboBox<String>(choices);
		
		final JTextField breakfast = new JTextField();
		final JTextField sugar = new JTextField();
		final JTextField protein = new JTextField();
		final JTextField calories = new JTextField();
		
		final JLabel resultL = new JLabel("Вид резултат: ");
		final JLabel breakfastL = new JLabel("Име: ");
		final JLabel sugarL = new JLabel("Съдържание на захар: ");
		final JLabel proteinL = new JLabel("Съдържание на протеини: ");
		final JLabel caloriesL = new JLabel("Максимални калории: ");
		
		result.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(result.getSelectedItem().equals("Kалории за грам")) {
					calories.setEditable(false);
					
				}else if(result.getSelectedItem().equals("Списък закуски")) {
					calories.setEditable(true);
					}
				}
			}
		);
		
		JButton button = new JButton("Търсене");
		
		setSize(800, 600);
		
		panel.setLayout(null);
		
		resultL.setBounds(200, 40, 400, 50);
		result.setBounds(200, 80, 400, 50);
		
		breakfastL.setBounds(50, 180, 300, 40);
		breakfast.setBounds(50, 220, 300, 40);
		
		proteinL.setBounds(425, 180, 300, 40);
		protein.setBounds(425, 220, 300, 40);
		
		sugarL.setBounds(50, 280, 300, 40);
		sugar.setBounds(50, 320, 300, 40);
		
		caloriesL.setBounds(425, 280, 300, 40);
		calories.setBounds(425, 320, 300, 40);
				
		button.setBounds(280, 450, 200, 50);
		
		add(panel);
		panel.add(resultL); panel.add(result); 
		panel.add(breakfastL); panel.add(breakfast); 
		panel.add(sugarL); panel.add(sugar); 
		panel.add(proteinL); panel.add(protein); 
		panel.add(caloriesL); panel.add(calories); 
		panel.add(button);
		
		
		
		
		setVisible(true);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filter = breakfast.getText();
				
				if(filter.isEmpty()) {
					filter = "all";
				}
				
				try {
					runHadoop(filter);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
		});
	}
	
    public static void main( String[] args )
    {
    	App form = new App();
    }
    
    private void runHadoop(String filter) throws IOException {
    	
    	Configuration conf = new Configuration();
    	
    	JobConf job = new JobConf(conf, App.class);
    	
    	job.set("filter", filter);
    	
    	job.setMapperClass(FoodMapper.class);
    	job.setReducerClass(FoodReducer.class);
    	job.setOutputKeyClass(Text.class);
    	job.setOutputValueClass(DoubleWritable.class);
    	
    	Path inputPath = new Path("hdfs://127.0.0.1:9000/input/cereal.csv");
    	Path outputPath = new Path("hdfs://127.0.0.1:9000/output/cereal");
    	
    	FileInputFormat.setInputPaths(job, inputPath);
    	FileOutputFormat.setOutputPath(job, outputPath);
    	
    	FileSystem fs = FileSystem.get(URI.create("hdfs://127.0.0.1:9000"), conf);
    	
    	if(fs.exists(outputPath)) {
    		fs.delete(outputPath, true);
    	}
    	
    	JobClient.runJob(job);
    	
    }
}
