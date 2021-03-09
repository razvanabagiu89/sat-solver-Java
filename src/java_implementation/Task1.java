
import java.io.*;
import java.util.Arrays;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {

    // number of families
    int N;
    // number of relations
    int M;
    // number of spies
    int K;
    // adjacency matrix to store the graph
    int[][] graph;
    // result from decipher()
    String result;

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

        this.M = Integer.parseInt(arr[1]);
        this.K = Integer.parseInt(arr[2]);

        while ((line = reader.readLine()) != null) {
            arr = line.split(" ", 2);
            int i = Integer.parseInt(arr[0]);
            int j = Integer.parseInt(arr[1]);
            this.graph[i - 1][j - 1] = 1;
        }
        reader.close();
    }

    @Override
    public void formulateOracleQuestion() throws IOException {

        int nrVars = N * K;
        /*
         C_n^k, where k = 2 always because we check for a family to not have 2 spies
         */
        int combinations = (K * (K - 1)) / 2;
        int nrClauses = N + N * combinations + M * K; // README.md

        StringBuilder sb = new StringBuilder();
        sb.append("p cnf ").append(nrVars).append(" ").append(nrClauses).append("\n");

        // each family must have a spy
        int count = 0;
        for (int i = 1; i <= nrVars + 1; i++) {
            if (count == K) {
                count = 0;
                sb.append(0);
                sb.append("\n");
            }
            if (i == nrVars + 1) {
                break;
            }
            sb.append(i).append(" ");
            count++;
        }

        // each family must have a single spy
        int limit = K;
        count = 0;
        boolean next_family = false;

        for (int i = 1; i <= nrVars; i++) {
            for (int j = i + 1; j <= nrVars; j++) {

                if (count == combinations) {
                    count = 0;
                    next_family = true;
                    // when passing to a new family it advances the limit too
                    limit += K;
                    break;
                }
                // exit the for when limit is reached
                if (j == limit + 1) {
                    break;
                }
                sb.append(-i).append(" ");
                sb.append(-j).append(" ");
                sb.append(0).append("\n");
                count++;
            }
            if (next_family == true) {
                next_family = false;
                i++;
            }
        }

        // two families that have a relation must have different spies
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.graph[i][j] == 1) {

                    int family1 = i + 1;
                    int family2 = 1 + K * j;

                    if (family1 != 1) {
                        family1 = 1 + K * i;
                    }

                    for (int k = 0; k < K; k++) {
                        sb.append(-(family1 + k)).append(" ");
                        sb.append(-(family2 + k)).append(" 0\n");
                    }
                }
            }
        }

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
            // skip the number of vars
            reader.readLine();
            // read the distribution of spies
            line = reader.readLine();
            String[] arr = line.split(" ", N * K + 1);

            // convert the oracle's answer to the actual spies
            int limit = K;
            for (int i = 0; i < arr.length; i += K) {
                for (int j = i; j < arr.length; j++) {
                    if (j == limit) {
                        limit += K;
                        break;
                    }
                    // get only the positive spies
                    if (!arr[j].equals("") && Integer.parseInt(arr[j]) > 0) {
                        int positive = Integer.parseInt(arr[j]);
                        int color = positive % K;
                        sb.append(color).append(" ");
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
