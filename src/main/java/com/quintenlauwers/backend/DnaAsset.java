package com.quintenlauwers.backend;

import java.util.*;

/**
 * Created by quinten on 13/08/16.
 */
class DnaAsset {

    private HashMap<String, AlleleInfo> allelesKey = new HashMap<String, AlleleInfo>();
    private HashMap<GenePosition, ArrayList<AlleleInfo>> positionKey = new HashMap<GenePosition, ArrayList<AlleleInfo>>();
    private HashMap<AlleleCombination, String> propertyValueFromAlleles = new HashMap<AlleleCombination, String>();
    private String property;

    DnaAsset(String property) {
        this.property = property;
    }

    public String[] getPossibleCodonValuesOnPosition(int[] position) {
        ArrayList<AlleleInfo> templist = positionKey.get(new GenePosition(position));
        ArrayList<String> possibleList = new ArrayList<String>();
        for (AlleleInfo allele : templist) {
            possibleList.addAll(allele.getPossibleCodesAsList());
        }
        String[] returnArray = new String[possibleList.size()];
        return possibleList.toArray(returnArray);
    }

    public String getAlleleOnPosition(int[] position, String code) {
        GenePosition genePosition = new GenePosition(position);
        ArrayList<AlleleInfo> templist = positionKey.get(genePosition);
        Set<String> tempSet = new LinkedHashSet<String>();
        for (AlleleInfo allele : templist) {
            String[] possibleCodes = allele.getPossibleCodes();
            for (String possibleCode : possibleCodes) {
                if (possibleCode != null && possibleCode.toUpperCase().equals(code.toUpperCase())) {
                    tempSet.add(allele.getName());
                }
            }
        }
        String returnString = "";
        for (String alleleLetter : tempSet) {
            returnString += alleleLetter;
        }
        return returnString;
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
        int[][] returnArray = new int[postionsAsCoordinates.size()][3];
        postionsAsCoordinates.toArray(returnArray);
//        int[][] returnArray = (int[][]) positions.stream().map(GenePosition::getCoordinate).toArray();
        return returnArray;
    }

    public String getProperty() {
        return "color";
    }

    public String[] getPossibleAllelesOnPosition(int[] positionCoordinate) {
        GenePosition position = new GenePosition(positionCoordinate);
        ArrayList<AlleleInfo> tempList = this.positionKey.get(position);
        String[] returnArray = new String[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            returnArray[i] = tempList.get(i).name;
        }
        return returnArray;
    }

    public void addFromPosition(int[] positionCoordinate, String alleleName) {
        if (positionCoordinate.length < 3) {
            return;
        }
        GenePosition position = new GenePosition(positionCoordinate);
        AlleleInfo allele = new AlleleInfo(alleleName);
        if (allelesKey.containsKey(alleleName)) {
            allele = this.allelesKey.get(alleleName);
        } else {
            this.allelesKey.put(alleleName, allele);
        }
        allele.addPosition(position);
        if (this.positionKey.containsKey(position)) {
            if (!this.positionKey.get(position).contains(allele)) {
                this.positionKey.get(position).add(allele);
            }
        } else {
            ArrayList<AlleleInfo> value = new ArrayList<AlleleInfo>();
            value.add(allele);
            this.positionKey.put(position, value);
        }
    }

    public void addAlleleInfo(String alleleName, String[] genes) {
        if (genes == null || !this.allelesKey.containsKey(alleleName)) {
            return;
        }
        AlleleInfo allele = this.allelesKey.get(alleleName);
        for (String gene : genes) {
            allele.addCode(gene);
        }
    }

    public void addPropertyValue(String alleleCombination, String value) {
        AlleleCombination cleaned = new AlleleCombination(alleleCombination);
        if (!this.propertyValueFromAlleles.containsKey(cleaned)) {
            this.propertyValueFromAlleles.put(cleaned, value);
        }
    }

    public String getPropertyValue(String alleleCombination) {
        AlleleCombination cleaned = new AlleleCombination(alleleCombination);
        return this.propertyValueFromAlleles.get(cleaned);
    }


    class AlleleInfo {
        private String name;
        private ArrayList<String> possibleCodes;
        private ArrayList<GenePosition> possiblePositions;

        AlleleInfo(String Allele) {
            this(Allele, new ArrayList<String>(), new ArrayList<GenePosition>());
        }

        AlleleInfo(String Allele, ArrayList<String> code) {
            this(Allele, code, new ArrayList<GenePosition>());
        }

        AlleleInfo(String Allele, ArrayList<String> code, ArrayList<GenePosition> positions) {
            this.name = Allele;
            this.possibleCodes = code;
            this.possiblePositions = positions;
        }

        void addCode(String code) {
            if (code != null) {
                this.possibleCodes.add(code);
            }
        }

        void addPosition(GenePosition position) {
            if (position != null) {
                this.possiblePositions.add(position);
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


    final class AlleleCombination {

        String combination;

        AlleleCombination(String alleleCombination) {
            Set<String> unique = new TreeSet<String>(Arrays.asList(alleleCombination.split("")));
            for (String element : unique) {
                combination += element;
            }
        }

        @Override
        public int hashCode() {
            int hash = 0;
            for (char c : this.combination.toCharArray()) {
                hash += ((int) c) * ((int) c);
            }
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AlleleCombination) {
                if (this.combination.equals(((AlleleCombination) obj).combination)) {
                    return true;
                }
            }
            return false;
        }
    }
}

final class GenePosition {
    int chromosome;
    int gene;
    int codon;

    GenePosition(int[] position) {
        if (position.length < 3) {
            throw new IllegalArgumentException("Position should at least have lenght 3 (chromosome, gene, codon).");
        }
        this.chromosome = position[0];
        this.gene = position[1];
        this.codon = position[2];
    }

    GenePosition(int chromosome, int gene, int codon) {
        this.chromosome = chromosome;
        this.gene = gene;
        this.codon = codon;
    }

    public int getChromosomeNb() {
        return this.chromosome;
    }

    public int getGeneNb() {
        return this.gene;
    }

    public int getCodonNb() {
        return this.codon;
    }

    public int[] getCoordinate() {
        int[] returnArray = {this.getChromosomeNb(), this.getGeneNb(), this.getCodonNb()};
        return returnArray;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GenePosition
                && ((GenePosition) obj).getChromosomeNb() == this.getChromosomeNb()
                && ((GenePosition) obj).getGeneNb() == this.getGeneNb()
                && ((GenePosition) obj).getCodonNb() == this.getCodonNb());
    }

    @Override
    public int hashCode() {
        return this.getChromosomeNb() * 1024 * 1024
                + this.getGeneNb() * 1024
                + this.getCodonNb();
    }
}
