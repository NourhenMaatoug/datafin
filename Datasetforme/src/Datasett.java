

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import gr.uom.java.xmi.decomposition.SmellDetection;
import gr.uom.java.xmi.decomposition.SmellyCodeInfo;

package Datasett;
public class Datasett {

	public static void main(String[] args) {
		String folderPath = "C:/Users/norhe/pfaa";

		try (PrintWriter writer = new PrintWriter(new File("resultats.csv"))) {
			StringBuilder sb = new StringBuilder();
			sb.append("Nom du fichier,");
			sb.append("Nombre de lignes de code,");
			sb.append("Densité de commentaires,");
			sb.append("Nombre de fonctions,");
			sb.append("Méthodes par classe,");
			sb.append("Nombre d'enfants,");
			sb.append("Couplage entre les classes d'objets,");
			sb.append("Manque de cohésion dans les méthodes,");
			sb.append("Nombre d'opérations ajoutées par une sous-classe,");
			sb.append("Nombre d'opérations remplacées par une sous-classe,");
			sb.append("Couverture des tests,");
			sb.append("Dette technique,");
			sb.append("Nombre de défauts,");
			sb.append("Lisibilité du code");

			sb.append('\n');

			File folder = new File(folderPath);
			File[] files = folder.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile() && file.getName().endsWith(".java")) {
						analyzeJavaFile(file, sb);
					}
				}
			}

			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			System.err.println("Le fichier CSV n'a pas pu être créé : " + e.getMessage());
		}
	}

	private static void analyzeJavaFile(File file, StringBuilder sb) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int totalLines = 0;
			int commentLines = 0;
			int functionCount = 0;
			int methodCount = 0;
			int numChildren = 0;
			int coupling = 0;
			int lackOfCohesion = 0;
			int operationsAddedBySubclass = 0;
			int operationsOverriddenBySubclass = 0;
			double testCoverage = 0.0;
			double technicalDebt = 0.0;
			int defectCount = 0;
			double codeReadability = 0.0;

			while ((line = br.readLine()) != null) {
				totalLines++;
				line = line.trim();

				// Calcul des métriques
				if (line.startsWith("//")) {
					commentLines++;
				} else if (line.startsWith("/*")) {
					commentLines++;
					while ((line = br.readLine()) != null && !line.endsWith("*/")) {
						commentLines++;
					}
					if (line != null && line.endsWith("*/")) {
						commentLines++;
					}
				} else if (line.isEmpty()) {
					// Lignes vides
				} else if (line.contains("class ")) {
					// Classe
					numChildren++;
				} else if (line.contains(" extends ")) {
					// Nombre d'enfants
					numChildren++;
				} else if (line.contains(" implements ")) {
					// Nombre d'enfants
					numChildren++;
				} else if (line.contains("this.")) {
					// Couplage entre les classes d'objets
					coupling++;
				} else if (line.contains("(") && line.contains(")") && !line.contains("if") && !line.contains("for")
						&& !line.contains("while") && !line.contains("switch")) {
					// Méthodes ou fonctions
					functionCount++;
					if (!line.contains("static")) {
						methodCount++;
					}
				}
				// Ajoutez d'autres conditions pour détecter d'autres métriques
			}

			// Calcul des métriques
			double commentDensity = (double) commentLines / totalLines * 100;
			double methodsPerClass = (double) methodCount / functionCount;
			// Calculer d'autres métriques selon les besoins

			// Enregistrement des résultats dans le fichier CSV
			sb.append(file.getName()).append(",");
			sb.append(totalLines).append(",");
			sb.append(commentDensity).append(",");
			sb.append(functionCount).append(",");
			sb.append(methodsPerClass).append(",");
			sb.append(numChildren).append(",");
			sb.append(coupling).append(",");
			sb.append(lackOfCohesion).append(",");
			sb.append(operationsAddedBySubclass).append(",");
			sb.append(operationsOverriddenBySubclass).append(",");
			sb.append(testCoverage).append(",");
			sb.append(technicalDebt).append(",");
			sb.append(defectCount).append(",");
			sb.append(codeReadability);

			sb.append('\n');
		} catch (IOException e) {
			System.err.println(
					"Une erreur s'est produite lors de l'analyse du fichier " + file.getName() + ": " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		// Chemin vers le dossier contenant les fichiers Java
		String folderPath = "C:\\Users\\norhe\\pfaa";

		// Créer un détecteur de "code smells"
		SmellDetection detector = new SmellDetection();

		// Créer une liste pour stocker les "code smells" détectés
		List<String> detectedSmells = new ArrayList<>();

		// Analyser chaque fichier Java
		File folder = new File(folderPath);
		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().endsWith(".java")) {
					// Détecter les "code smells" dans le fichier Java
					detectCodeSmells(file, detector, detectedSmells);
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

