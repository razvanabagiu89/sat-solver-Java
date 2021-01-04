// Copyright 2020
// Author: Matei Simtinică

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Task task = null;

        if (args.length < 5) {
            System.err.println("Usage: ./main <task> <input_file> <SAT_input_file> <SAT_output_file> <output_file>\n");
            System.exit(-1);
        }

        var taskNumber = args[0];
        var input = args[1];
        var satInput = args[2];
        var satOutput = args[3];
        var output = args[4];

        switch (taskNumber) {
            case "task1" -> task = new Task1();
            case "task2" -> task = new Task2();
            case "task3" -> task = new Task3();
            case "bonus" -> task = new BonusTask();
            default -> {
                System.err.println("Not a valid task");
                System.exit(-1);
            }
        }

        task.addFiles(input, satInput, satOutput, output);
        task.solve();
    }
}
