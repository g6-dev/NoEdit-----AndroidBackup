package android.g6.cricspot.CricObjects;

public class Team {

    private String name;
    private String location;
    private String player1;
    private String player2;
    private String player3;
    private String player4;
    private String player5;
    private String challenger;
    private Boolean isPlaying;

    public Team() {
    }

    public Team(String name, String location, String player1, String player2, String player3,
                String player4, String player5, String challenger, Boolean isPlaying) {
        this.name = name;
        this.location = location;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.player5 = player5;
        this.challenger = challenger;
        this.isPlaying = isPlaying;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    public String getPlayer5() {
        return player5;
    }

    public void setPlayer5(String player5) {
        this.player5 = player5;
    }

    public String getChallenger() {
        return challenger;
    }

    public void setChallenger(String challenger) {
        this.challenger = challenger;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    @Override
    public String toString() {
        return "[ name: "+this.name+", isPlaying: "+ this.isPlaying+ ", challenger: " + this.challenger +" ]";
    }
}
