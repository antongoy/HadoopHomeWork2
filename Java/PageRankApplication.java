import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class PageRankApplication {

    public static class PageRankMapper extends Mapper<LongWritable, Text, Text, Text> {

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");

            if (fields.length == 2) {
                return;
            }

            String[] outNodes = fields[2].split(",");

            double nodeDegree = outNodes.length;

            context.write(new Text(fields[0]), new Text(fields[1] + "\t" + fields[2]));

            Double newPageRank = Double.parseDouble(fields[1]) / nodeDegree;

            for (String node : outNodes) {
                context.write(new Text(node), new Text(newPageRank.toString()));
            }

        }
    }

    public static class PageRankReducer extends Reducer<Text, Text, Text, Text> {
        private double newPageRank;
        private String outNodes;

        public void reduce(Text key, Iterable<Text> value, Context context) throws IOException, InterruptedException {
            newPageRank = 0.0;
            outNodes = "";

            for (Text v : value) {
                String[] fields = v.toString().split("\t");

                if (fields.length == 2) {
                    outNodes = fields[1];
                } else {
                    newPageRank += Double.parseDouble(fields[0]);
                }
            }

            newPageRank = 0.15 + 0.85 * newPageRank;

            context.write(key, new Text(String.valueOf(newPageRank) + "\t" + outNodes));

        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "page_rank");
        job.setJarByClass(PageRankApplication.class);
        job.setMapperClass(PageRankMapper.class);
        job.setReducerClass(PageRankReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
