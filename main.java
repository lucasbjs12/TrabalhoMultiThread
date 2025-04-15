import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {
    public static void main(String[] args) throws IOException {
        String directoryPath = "c:/Users/lucas.ribeiro/Desktop/trabalho";
        String searchTerm = "Sharon Sullivan"; // Substitua pelo termo que deseja buscar

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        long startTime = System.nanoTime(); // Início do timer

        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .forEach(path -> executor.submit(() -> searchInFile(path, searchTerm)));

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Aguarda a conclusão de todas as tarefas
        }

        long endTime = System.nanoTime(); // Fim do timer
        long duration = (endTime - startTime) / 1_000_000; // Converte para milissegundos
        System.out.println("Tempo total para encontrar o termo: " + duration + " ms");
    }

    private static void searchInFile(Path filePath, String searchTerm) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.contains(searchTerm)) {
                    System.out.println("Encontrado em: " + filePath + " na linha " + lineNumber);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + filePath);
        }
    }
}
