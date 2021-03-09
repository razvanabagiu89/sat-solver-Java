
import java.io.*;
import java.util.Arrays;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;

    // number of families
    int N;
    // number of relations
    int M;
    // adjacency matrix to store the graph
    int[][] graph;
    // complementary graph to send to task2
    int[][] graph_prim;
    // number of relations of the complementary graph to send to task2
    int M_prim;

    int min = Integer.MAX_VALUE;
    String resultTask2;
    String resultTask3;

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        // send the complementary graph to task2, N and M_prim
        reduceToTask2(task2Solver);

        for (int i = 0; i < N; i++) {

            // query the oracle with various arrest numbers to find the best one
            task2Solver.K = N - i;
            task2Solver.clauses.clear();

            task2Solver.formulateOracleQuestion();
            task2Solver.askOracle();
            task2Solver.decipherOracleAnswer();
            String[] arr = task2Solver.result.split("\n");
            if (arr[0].equals("True")) {
                if (min > i) {
                    min = i;
                    this.resultTask2 = task2Solver.result;
                }
            }
        }
        StringBuilder sb = new StringBuilder();

        // remove "True"
        String aux = this.resultTask2.replace("True\n", "");
        String[] arr = aux.split(" ");
        for(int i = 1; i <= N; i++) {
            int ok = 0;
            for(String str : arr) {
                if(!str.equals("") && i == Integer.parseInt(str)) {
                    ok = 1;
                }
            }
            // if not found
            if(ok == 0) {
                sb.append(i).append(" ");
            }
        }
        this.resultTask3 = sb.toString();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(inFilename));
        String line = reader.readLine();
        String[] arr = line.split(" ", 2);
        this.N = Integer.parseInt(arr[0]);
        this.graph = new int[N][N];
        this.graph_prim = new int[N][N];
        for (int[] row : this.graph) {
            Arrays.fill(row, 0);
        }
        for (int i = 0; i < N; i++) {
            this.graph[i][i] = 1;
        }

        this.M = Integer.parseInt(arr[1]);

        while ((line = reader.readLine()) != null) {
            arr = line.split(" ", 2);
            int i = Integer.parseInt(arr[0]);
            int j = Integer.parseInt(arr[1]);
            this.graph[i - 1][j - 1] = 1;
            this.graph[j - 1][i - 1] = 1;
        }
        reader.close();
    }


    public void reduceToTask2(Task2 task2) {
        this.M_prim = this.N * (this.N - 1) / 2 - this.M;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.graph[i][j] == 0) {
                    this.graph_prim[i][j] = 1;
                } else {
                    this.graph_prim[i][j] = 0;
                }
            }
        }

        task2.graph = this.graph_prim;
        task2.M = this.M_prim;
        task2.N = this.N;
    }

    public void extractAnswerFromTask2() {
    }

    @Override
    public void writeAnswer() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFilename));
        writer.write(this.resultTask3);
        writer.flush();
    }
}
