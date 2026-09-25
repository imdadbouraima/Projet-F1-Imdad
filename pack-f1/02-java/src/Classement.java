/* =========================================================================
   MAILLON 2 — JAVA : le moteur de calcul
   Complétez les quatre méthodes. Les classes Ligne, Resultat et Chargeur
   sont fournies : ne les modifiez pas.
       javac -encoding UTF-8 -d out src/*.java
       java -Dstdout.encoding=UTF-8 -cp out Tests     (les tests)
       java -Dstdout.encoding=UTF-8 -cp out Main      (la production)
   ========================================================================= */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classement {

    /** Barème officiel des dix premiers. FOURNI — NE PAS MODIFIER. */
    public static final int[] BAREME = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    // 1. pointsPourPosition(position) : points marqués pour cette position.
    //    1 -> 25, 2 -> 18, ..., 10 -> 1. Au-delà de la 10e place : 0.
    //    Un abandon vaut la position 0, donc 0 point.
    public static int pointsPourPosition(int position) {
        if (position >= 1 && position <= 10) {
            return BAREME[position - 1];
        }
        return 0;
    }

    // 2. classementPilotes(lignes) : un Resultat par pilote, avec ses points,
    //    ses victoires (position 1) et ses 2e places, trié par :
    //    points décroissants, puis victoires, puis 2e places, puis nom (A→Z).
    public static List<Resultat> classementPilotes(List<Ligne> lignes) {
        Map<String, Resultat> mapPilotes = new HashMap<>();

        // Parcours de toutes les lignes de course
        for (Ligne ligne : lignes) {
            String nom = ligne.pilote();
            String ecurie = ligne.ecurie();
            int pos = ligne.position();

            // Recherche si le pilote existe déjà dans la Map
            Resultat r = mapPilotes.get(nom);
            if (r == null) {
                r = new Resultat(nom, ecurie);
                mapPilotes.put(nom, r);
            }

            // Cumul des points
            r.points += pointsPourPosition(pos);

            // Comptage des victoires et des 2es places
            if (pos == 1) {
                r.victoires++;
            } else if (pos == 2) {
                r.deuxiemes++;
            }
        }

        // On transforme les valeurs de la Map en liste
        List<Resultat> liste = new ArrayList<>(mapPilotes.values());

        // Tri classique avec Comparator
        Collections.sort(liste, new Comparator<Resultat>() {
            @Override
            public int compare(Resultat r1, Resultat r2) {
                // 1. Points (décroissant)
                if (r1.points != r2.points) {
                    return r2.points - r1.points;
                }
                // 2. Victoires (décroissant)
                if (r1.victoires != r2.victoires) {
                    return r2.victoires - r1.victoires;
                }
                // 3. Deuxièmes places (décroissant)
                if (r1.deuxiemes != r2.deuxiemes) {
                    return r2.deuxiemes - r1.deuxiemes;
                }
                // 4. Nom (ordre alphabétique A->Z)
                return r1.nom.compareTo(r2.nom);
            }
        });

        return liste;
    }

    // 3. classementEcuries(pilotes) : additionne les points, victoires et
    //    2e places des pilotes de chaque écurie. Même ordre de tri.
    public static List<Resultat> classementEcuries(List<Resultat> pilotes) {
        Map<String, Resultat> mapEcuries = new HashMap<>();

        for (Resultat p : pilotes) {
            String ecurie = p.ecurie;

            Resultat r = mapEcuries.get(ecurie);
            if (r == null) {
                r = new Resultat(ecurie, "");
                mapEcuries.put(ecurie, r);
            }

            r.points += p.points;
            r.victoires += p.victoires;
            r.deuxiemes += p.deuxiemes;
        }

        List<Resultat> liste = new ArrayList<>(mapEcuries.values());

        // Tri identique
        Collections.sort(liste, new Comparator<Resultat>() {
            @Override
            public int compare(Resultat r1, Resultat r2) {
                if (r1.points != r2.points) {
                    return r2.points - r1.points;
                }
                if (r1.victoires != r2.victoires) {
                    return r2.victoires - r1.victoires;
                }
                if (r1.deuxiemes != r2.deuxiemes) {
                    return r2.deuxiemes - r1.deuxiemes;
                }
                return r1.nom.compareTo(r2.nom);
            }
        });

        return liste;
    }

    // 4. positionMoyenne(lignes, pilote) : moyenne des positions de ce pilote,
    //    ABANDONS EXCLUS, arrondie à 2 décimales. 0 s'il n'a jamais terminé.
    //    Ex. positions 1, 2 et un abandon -> 1.5
    public static double positionMoyenne(List<Ligne> lignes, String pilote) {
        int somme = 0;
        int nbCourses = 0;

        for (Ligne ligne : lignes) {
            if (ligne.pilote().equals(pilote) && ligne.position() > 0) {
                somme += ligne.position();
                nbCourses++;
            }
        }

        if (nbCourses == 0) {
            return 0.0;
        }

        double moyenne = (double) somme / nbCourses;
        return Math.round(moyenne * 100.0) / 100.0;
    }
}