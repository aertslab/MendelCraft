package com.quintenlauwers.backend;

import java.util.*;

/**
 * Created by quinten on 13/08/16.
 */
class DnaAsset {

    private HashMap<String, AlleleInfo> allelesKey = new HashMap<String, AlleleInfo>();
    private HashMap<GenePosition, ArrayList<AlleleInfo>> positonKey = new HashMap<GenePosition, ArrayList<AlleleInfo>>();

    public String[] getPossibleGenesOnPosition(int[] position) {
        ArrayList<AlleleInfo> templist = positonKey.get(new GenePosition(position));
        ArrayList<String> possibleList = new ArrayList<String>();
        for (AlleleInfo allele : templist) {
            possibleList.addAll(allele.getPossibleCodesAsList());
        }
        String[] returnArray = new String[possibleList.size()];
        return possibleList.toArray(returnArray);
    }

    public int[][] getRelevantPositions() {
        Collection<AlleleInfo> allAlleles = allelesKey.values();
        Set<GenePosition> positions = new HashSet<GenePosition>() {
        };
        for (AlleleInfo allele : allAlleles) {
            GenePosition[] possiblePositions = allele.getPossiblePositions();
            for (GenePosition position : possiblePositions) {
                if (position != null) {
                    positions.add(position);
                }
            }
        }
        Set<int[]> postionsAsCoordinates = new HashSet<int[]>();
        for (GenePosition genePosition : positions) {
            postionsAsCoordinates.add(genePosition.getCoordinate());
        }
        int[][] returnArray = new int[postionsAsCoordinates.size()][2];
        postionsAsCoordinates.toArray(returnArray);
//        int[][] returnArray = (int[][]) positions.stream().map(GenePosition::getCoordinate).toArray();
        return returnArray;
    }

    public String getProperty() {
        return "color";
    }

    public String[] getPossibleAllelesOnPosition() {
        return null;
    }

    class AlleleInfo {
        String name;
        ArrayList<String> possibleCodes;
        ArrayList<GenePosition> possiblePositions;

        AlleleInfo(String Allele) {
            new AlleleInfo(Allele, new ArrayList<String>(), new ArrayList<GenePosition>());
        }

        AlleleInfo(String Allele, ArrayList<String> code) {
            new AlleleInfo(Allele, code, new ArrayList<GenePosition>());
        }

        AlleleInfo(String Allele, ArrayList<String> code, ArrayList<GenePosition> positions) {
            this.name = Allele;
            this.possibleCodes = code;
            this.possiblePositions = positions;
        }

        void addCode(String code) {
            if (code != null) {
                possibleCodes.add(code);
            }
        }

        void addPosition(GenePosition position) {
            if (position != null) {
                possiblePositions.add(position);
            }
        }

        public String getName() {
            return this.name;
        }

        public String[] getPossibleCodes() {
            String[] returnArray = new String[this.possibleCodes.size()];
            return this.possibleCodes.toArray(returnArray);
        }

        public GenePosition[] getPossiblePositions() {
            GenePosition[] returnArray = new GenePosition[this.possiblePositions.size()];
            return this.possiblePositions.toArray(returnArray);
        }

        ArrayList<String> getPossibleCodesAsList() {
            return this.possibleCodes;
        }
    }

    final class GenePosition {
        int chromosome;
        int gene;

        GenePosition(int[] position) {
            if (position.length < 2) {
                throw new IllegalArgumentException("Position should at least have lenght 2 (chromosome, gene).");
            }
            this.chromosome = position[0];
            this.gene = position[1];
        }

        GenePosition(int chromosome, int gene) {
            this.chromosome = chromosome;
            this.gene = gene;
        }

        public int getChromosomeNb() {
            return this.chromosome;
        }

        public int getGeneNb() {
            return this.gene;
        }

        public int[] getCoordinate() {
            int[] returnArray = {this.getChromosomeNb(), this.getGeneNb()};
            return returnArray;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof GenePosition
                    && ((GenePosition) obj).getChromosomeNb() == this.getChromosomeNb()
                    && ((GenePosition) obj).getGeneNb() == this.getGeneNb());
        }

        @Override
        public int hashCode() {
            return this.getChromosomeNb() * 100000 + this.getGeneNb();
        }
    }
}