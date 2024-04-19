
import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class Data {

    public static void main(String[] args) {
        // Chemin vers le dossier contenant le projet
        String folderPath = "C:\\Users\\norhe\\pfaa";

        // Exécution de JDeodorant
        Map<String, List<String>> analysisResults = runJDeodorant(folderPath);

        // Exportation des résultats vers un fichier Excel
        exportToExcel(analysisResults);
    }

    public static Map<String, List<String>> runJDeodorant(String folderPath) {
        Map<String, List<String>> analysisResults = new HashMap<>();

        try {
            // Exécution de JDeodorant en tant que processus externe
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "chemin/vers/jdeodorant.jar", folderPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Lecture de la sortie de JDeodorant
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Analyse de la ligne pour extraire les résultats
                // Exemple : "Metric: LOC - Value: 1000"
                // Exemple : "Code Smell: Long Method - File: Example.java - Method: methodName"
                // Remarque : Vous devez adapter ce code pour analyser correctement la sortie de JDeodorant
                if (line.startsWith("Metric:")) {
                    String[] parts = line.split(" - Value: ");
                    String metric = parts[0].substring(8);
                    String value = parts[1];
                    analysisResults.computeIfAbsent("Metrics", k -> new ArrayList<>()).add(metric + ": " + value);
                } else if (line.startsWith("Code Smell:")) {
                    analysisResults.computeIfAbsent("Code Smells", k -> new ArrayList<>()).add(line);
                }
            }

            // Attente de la fin du processus
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return analysisResults;
    }

    public static void exportToExcel(Map<String, List<String>> analysisResults) {
        try {
            // Création d'un nouveau classeur Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Code Analysis");

            // Ajout des résultats dans la feuille Excel
            int rowNum = 0;
            for (Map.Entry<String, List<String>> entry : analysisResults.entrySet()) {
                String category = entry.getKey();
                List<String> results = entry.getValue();
                
                // Écrire le nom de la catégorie
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(0);
                cell.setCellValue(category);

                // Écrire les résultats
                for (int i = 0; i < results.size(); i++) {
                    row = sheet.createRow(rowNum++);
                    cell = row.createCell(0);
                    cell.setCellValue(results.get(i));
                }
            }

            // Écriture du classeur dans un fichier Excel
            FileOutputStream fileOut = new FileOutputStream("code_analysis_results.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Fermeture du classeur
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


		// Afficher les "code smells" détectés
		System.out.println("Code smells détectés :");
		for (String smell : detectedSmells) {
			System.out.println(smell);
		}
	}

	private static void detectCodeSmells(File file, SmellDetection detector, List<String> detectedSmells) {
		// Charger le fichier Java
		detector.detectCodeSmells(file);

		// Récupérer les informations sur les "code smells" détectés
		List<SmellyCodeInfo> smellyCodeInfoList = detector.getSmellyCodeInfoList();

		// Parcourir les informations sur les "code smells" détectés
		for (SmellyCodeInfo smellyCodeInfo : smellyCodeInfoList) {
			// Vérifier le type de "code smell" détecté
			switch (smellyCodeInfo.getSmellType()) {
			case LongMethod:
				detectedSmells.add("Méthode Longue dans le fichier " + file.getName());
				break;
			case FeatureEnvy:
				detectedSmells.add("Couplage Élevé dans le fichier " + file.getName());
				break;
			// Ajouter d'autres cas pour d'autres types de "code smells" selon vos besoins
			default:
				break;
			}
		}
	}
}

