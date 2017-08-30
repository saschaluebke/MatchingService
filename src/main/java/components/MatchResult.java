package components;

public class MatchResult implements Comparable<MatchResult> {
    private double score;
    private int sourceStart, sourceEnd, targetStart, targetEnd;
    private final String searchString;
    private final Word wordFromDB;
    private final int ID;

    /**
     *
     * @param wordFromDB
     * @param targetText    Input String
     * @param score         Score of Distance Function
     * @param sourceStart   Parameter where source and target match
     * @param sourceEnd
     * @param targetStart
     * @param targetEnd
     */
    public MatchResult(Word wordFromDB, String targetText, double score,int sourceStart,int sourceEnd, int targetStart, int targetEnd){
        this.wordFromDB = wordFromDB;
        this.searchString = targetText;
        this.score=score;
        this.sourceStart=sourceStart;
        this.sourceEnd=sourceEnd;
        this.targetStart = targetStart;
        this.targetEnd = targetEnd;
        this.ID = wordFromDB.getId();
    }

    public String getDbString(){
        return wordFromDB.getName().substring(sourceStart,sourceEnd);
    }

    public String getSearchString(){
        return searchString.substring(targetStart,targetEnd);
    }



    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getSourceStart() {
        return sourceStart;
    }

    public void setSourceStart(int start) {
        this.sourceStart = start;
    }

    public int getSourceEnd() {
        return sourceEnd;
    }

    public void setSourceEnd(int end) {
        this.sourceEnd = end;
    }

    public int getTargetStart() {
        return targetStart;
    }

    public void setTargetStart(int targetStart) {
        this.targetStart = targetStart;
    }

    public int getTargetEnd() {
        return targetEnd;
    }

    public void setTargetEnd(int targetEnd) {
        this.targetEnd = targetEnd;
    }

    public int getTargetSize(){
        return targetEnd-targetStart;
    }

    public int getSourceSize() {return sourceEnd-sourceStart;}

    @Override
    public int compareTo(MatchResult other) {
        return (this.getSourceEnd()-this.getSourceStart()+this.getTargetEnd()-this.getTargetStart())
                +(other.getSourceEnd()-other.getSourceStart()+other.getTargetEnd()-other.getTargetStart());
    }

    @Override
    public String toString(){
        return this.searchString.substring(targetStart,targetEnd)+" "+this.wordFromDB.getName().substring(sourceStart,sourceEnd)+":"+score;
    }

    public int getID() {
        return ID;
    }
}
