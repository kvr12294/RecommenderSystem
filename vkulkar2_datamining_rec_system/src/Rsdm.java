/**
 * Recommender system
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class Rsdm {
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Too Few Arguments");
		}
		String input = args[0];
		Rsdm pearson_coeff = new Rsdm();
		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] temp = line.split(" ");
			pearson_coeff.init_matrix[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = Integer.parseInt(temp[2]);
		}
		String output = args[1];
		pearson_coeff.Pearson_correlation();
		pearson_coeff.writetofile(output);
		br.close();
	}
	int[][] init_matrix = new int[944][1683];
	int[][] final_matrix = new int[944][1683];
	double[][] user_user_matrix = new double[944][944];

	/**
	 * This function is to check pearson similarity coefficient between users.
	 * It is user-based recommendation system
	 * it calculates the final predicted matrix as per the given Pearson formula
	 */
	public void Pearson_correlation() {
		double d = 0, n1 = 0, d1 = 0, n = 0, loop_rating1 = 0, loop_rating2 = 0;
		Vector<Integer> similar_items = new Vector<Integer>(0);
		HashMap<Integer, List<Integer>> similarity_Map = new HashMap<Integer, List<Integer>>();
		for (int i = 1; i < 944; i++) {
			double average = 0.0;
			for (int j = 1; j < 1683; j++) {
				average += init_matrix[i][j];
			}
			average = average / 1682;
			similar_items = new Vector<Integer>(0);

			for (int k = 1; k < 944; k++) {
				double average_rating = 0.0;
				if (i == k)
					continue;
				for (int l = 1; l < 1683; l++) {
					average_rating += init_matrix[k][l];
				}
				average_rating = average_rating / 1682;
				for (int m = 1; m < 1683; m++) {
					n += (init_matrix[i][m] - average) * (init_matrix[k][m] - average_rating);
					loop_rating1 += Math.pow((init_matrix[i][m] - average), 2);
					loop_rating2 += Math.pow((init_matrix[k][m] - average_rating), 2);
				}
				d = Math.sqrt(loop_rating1 * loop_rating2);

				double similarity_rating = n / d;
				user_user_matrix[i][k] = similarity_rating;
			}
			similarity_Map.put(i, similar_items);
		}

		for (int row = 1; row < 944; row++) {
			for (int col = 1; col < 1683; col++) {
				if (init_matrix[row][col] == 0) {
					double predicted_rating_value = 0;
					for (int count = 1; count < 944; count++) {
						if (row != count && init_matrix[count][col] != 0) {
							n1 += init_matrix[count][col] * user_user_matrix[row][count];
							d1 += Math.abs(user_user_matrix[row][count]);
						}
					}
					predicted_rating_value = n1 / d1;
					if (predicted_rating_value < 1)
						predicted_rating_value = 1;
					else if (predicted_rating_value > 5)
						predicted_rating_value = 5;
					final_matrix[row][col] = (int) Math.round(predicted_rating_value);
					if(final_matrix[row][col] == 0){
						final_matrix[row][col] += 1;
					}
				} else {
					final_matrix[row][col] = init_matrix[row][col];
				}
			}
		}
	}
/**
 * 
 * @param output
 * @throws IOException
 * This function just writes the final predicted matrix to a file given in the argument.
 */
	public void writetofile(String output) throws IOException {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(output));
			bw.write("User\tItem\tValue");
			for (int row = 1; row < 944; row++) {
				for (int col = 1; col < 1683; col++) {
					bw.write("\n\t"+row+"\t\t"+col+"\t\t"+final_matrix[row][col]);
				}
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}