
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {

    // number of families
    int N;
    // number of relations
    int M;
    // size of the extended family
    int K;
    // adjacency matrix to store the graph
    int[][] graph;
    // result from decipher()
    String result;
    Map<String, Integer> clauses = new HashMap<>(); // README.md

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(inFilename));
        String line = reader.readLine();
        String[] arr = line.split(" ", 3);
        this.N = Integer.parseInt(arr[0]);
        this.graph = new int[N][N];
        for (int[] row : this.graph) {
            Arrays.fill(row, 0);
        }
        for (int i = 0; i < N; i++) {
            this.graph[i][i] = 1;
        }

        this.M = Integer.parseInt(arr[1]);
        this.K = Integer.parseInt(arr[2]);

        while ((line = reader.readLine()) != null) {
            arr = line.split(" ", 2);
            int i = Integer.parseInt(arr[0]);
            int j = Integer.parseInt(arr[1]);
            this.graph[i - 1][j - 1] = 1;
            this.graph[j - 1][i - 1] = 1;
        }
        reader.close();
    }

    @Override
    public void formulateOracleQuestion() throws IOException {

        int nrVars = N * K;
        /*
        C_n^2, this is the total number of families that a position on the extended family can
        have
        */
        int combinations = (N * (N - 1)) / 2;
        StringBuilder sb = new StringBuilder();

        // there must exist K families on the extended family with size K
        int count = 0;
        for (int i = 1; i <= nrVars + 1; i++) {
            if (count == N) {
                count = 0;
                sb.append(0);
                sb.append("\n");
                // add something to clause so we can add up to the size later
                clauses.put(i + " ", 1);
            }
            if (i == nrVars + 1) {
                break;
            }
            sb.append(i).append(" ");
            count++;
        }

        // two families that don't have a relation don't belong to the extended family
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (this.graph[i][j] == 0) {

                    int family1 = i + 1;
                    int family2 = j + 1;

                    for (int k = family1; k <= nrVars; k += N) {
                        for (int l = family2; l <= nrVars; l += N) {

                            sb.append(-(k)).append(" ");
                            sb.append(-(l)).append(" 0\n");
                            clauses.put("-" + k + " " + "-" + l + " 0\n", 1);
                        }
                    }
                }
            }
        }

        // a position in the extended family can't have 2 families
        int limit = N;
        count = 0;
        boolean next_position = false;

        for (int i = 1; i <= nrVars; i++) {
            for (int j = i + 1; j <= nrVars; j++) {

                if (count == combinations) {
                    count = 0;
                    next_position = true;
                    // when passing to the next position, the limit is updated with N
                    limit += N;
                    break;
                }
                // exit for loop when iterator is at limit
                if (j == limit + 1) {
                    break;
                }
                // get only non-duplicates clauses
                if (!clauses.containsKey("-" + i + " " + "-" + j + " 0\n")) {
                    sb.append(-i).append(" ");
                    sb.append(-j).append(" ");
                    sb.append(0).append("\n");
                    clauses.put("-" + i + " " + "-" + j + " 0\n", 1);

                }
                count++;
            }
            if (next_position == true) {
                next_position = false;
                i++;
            }
        }

        // a family can't appear 2 times in the extended family
        for (int i = 1; i <= N; i++) {
            for (int j = i; j <= nrVars; j += N) {
                for (int k = j + N; k <= nrVars; k += N) {

                    // get only non-duplicates clauses
                    if (!clauses.containsKey("-" + j + " " + "-" + k + " 0\n")) {
                        sb.append(-j).append(" ");
                        sb.append(-k).append(" ");
                        sb.append(0).append("\n");
                        clauses.put("-" + j + " " + "-" + k + " 0\n", 1);
                    }
                }
            }
        }
        // get final number of clauses from hashmap
        int nrClauses = clauses.size();
        String data = "p cnf " + nrVars + " " + nrClauses + "\n";
        sb.insert(0, data);

        BufferedWriter writer = new BufferedWriter(new FileWriter(oracleInFilename));
        writer.write(sb.toString());
        writer.flush();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(oracleOutFilename));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();

        if (line.equals("False")) {
            sb.append("False\n");
        } else {
            sb.append("True\n");
            // skip number of vars
            reader.readLine();
            // check the extended family
            line = reader.readLine();
            String[] arr = line.split(" ", N * K + 1);
            for (String str : arr) {
                if (!str.equals("") && Integer.parseInt(str) > 0) {
                    int num = Integer.parseInt(str);
                    if (num % N == 0) {
                        sb.append(N).append(" ");
                    } else {
                        sb.append(num % N).append(" ");
                    }
                }
            }
        }
        result = sb.toString();
    }

    @Override
    public void writeAnswer() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(outFilename));
        writer.write(result);
        writer.flush();
    }
}
