package android.g6.cricspot.CricClasses;

import android.g6.cricspot.CricObjects.MatchDetails;
import android.g6.cricspot.CricObjects.Player;
import android.g6.cricspot.CricObjects.Team;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    //--------------------------- DATABASE MEMBER KEY ----------------------------------------------
    private static final String dbMemberNameForTeam = "Team";
    private static final String dbMemberNameForPlayer = "Player";
    private static final String dbMemberNameForMatchDetails = "Match Details";

    public static String getDbMemberNameForMatchDetails() {
        return dbMemberNameForMatchDetails;
    }

    public static String getDbMemberNameForTeam() {
        return dbMemberNameForTeam;
    }

    public static String getDbMemberNameForPlayer() {
        return dbMemberNameForPlayer;
    }
    //--------------------------- DATABASE MEMBER KEY ----------------------------------------------

    public DatabaseManager() {
    }

    //*********************************************************************************************
    //********************** DATABASE FUNCTIONS >>>>>>>>>> PLAYER OBJECT <<<<<<<<<<<<<*************
    //*********************************************************************************************

    //-------- STATIC LIST TO ACCESS FIRE BASE DATA === PLAYER --------------------------------------
    private Player player;
    private static List<Player> playersList = new ArrayList<>();

    public static List<Player> getPlayersList() {
        return playersList;
    }

    public static void setPlayersList(List<Player> playersList) {
        DatabaseManager.playersList = playersList;
    }

    private static Boolean isPlayersRetrieved = false;

    public static Boolean getIsPlayersRetrieved() {
        return isPlayersRetrieved;
    }

    public static void setIsPlayersRetrieved(Boolean isPlayersRetrieved) {
        DatabaseManager.isPlayersRetrieved = isPlayersRetrieved;
    }

    public void addPlayerToFirebase(String dbMemberName, Player player) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);
        System.out.println(">>>> Adding player into fire base");
        /*dbReference.push().setValue(player);  // Add player with unknown key number*/
        //Because userName is the as per the unique name
        dbReference.child(player.getUserName()).setValue(player);
    }

    /*
     * dbMemberName is the database Name. Ex: 'Player'
     * dbChildName is the player key. Ex: 'test1'
     */
    public Player retrieveOnePlayerFromDatabase(String dbMemberName, String dbChildName) {

        player = null;
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName).child(dbChildName);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String userName = dataSnapshot.child("userName").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                String age = dataSnapshot.child("age").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String team = dataSnapshot.child("team").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String type = dataSnapshot.child("type").getValue().toString();

                Log.d(">>>>>", "[" + name + ", " + userName + ", " + password + ", " + age + ", " + phone +
                        ", " + team + ", " + location + ", " + type + "]");
                player = new Player(name, userName, password, age, phone, team, location, type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return player;
    }

    /*
     * Link: https://stackoverflow.com/questions/38652007/how-to-retrieve-specific-list-of-data-from-firebase
     * Run this method & access the static List
     */
    public void retrieveAllPlayersFromDatabase(String dbMemberName) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);
        Log.d(">>>>>", "Starting method");
        isPlayersRetrieved = false;

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(">>>>>", "On 1 st method");
                playersList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d(">>>>>", "On 2 nd method");
                    player = postSnapshot.getValue(Player.class);
                    playersList.add(player);

                    // here you can access to name property like university.name
                    System.out.println(">>>>> Retrieving player -> " + player);

                    if (!isPlayersRetrieved) {
                        isPlayersRetrieved = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void updatePlayerAttributeInFirebase(String dbMemberName, Player player) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);

        dbReference.child(player.getUserName()).setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(">>>>> Update is successful...");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(">>>>> Update is unsuccessful!");
            }
        });
    }

    //*********************************************************************************************
    //********************** DATABASE FUNCTIONS >>>>>>>>>> TEAM OBJECT <<<<<<<<<<<<<***************
    //*********************************************************************************************

    //----------------------------------------------------------------------------------------------
    //-------- STATIC LIST TO ACCESS FIREBASE DATA === TEAM ----------------------------------------
    private Team team;
    private static List<Team> teamsList = new ArrayList<>();

    public static List<Team> getTeamsList() {
        return teamsList;
    }

    public static void setTeamsList(List<Team> teamsList) {
        DatabaseManager.teamsList = teamsList;
    }

    private static Boolean isTeamsRetrieved = false;

    public static Boolean getIsTeamsRetrieved() {
        return isTeamsRetrieved;
    }

    public static void setIsTeamsRetrieved(Boolean isTeamsRetrieved) {
        DatabaseManager.isTeamsRetrieved = isTeamsRetrieved;
    }
    //----------------------------------------------------------------------------------------------

    public void addTeamToFirebase(String dbMemberName, Team team) {

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);
        System.out.println(">>>> Adding data into fire base");
        /*dbReference.push().setValue(player);  // Add player with unknown key number*/
        dbReference.child(team.getName()).setValue(team);
    }

    public Team retrieveOneTeamFromDatabase(String dbMemberName, String dbChildName) {

        team = null;
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName).child(dbChildName);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();
                String player1 = dataSnapshot.child("player1").getValue().toString();
                String player2 = dataSnapshot.child("player2").getValue().toString();
                String player3 = dataSnapshot.child("player3").getValue().toString();
                String player4 = dataSnapshot.child("player4").getValue().toString();
                String player5 = dataSnapshot.child("player5").getValue().toString();
                String challenger = dataSnapshot.child("challenger").getValue().toString();
                Boolean isPlaying = Boolean.valueOf(dataSnapshot.child("isPlaying").getValue().toString());

                Log.d(">>>>>", "[" + name + ", " + location + ", " + player1 + ", "
                        + player2 + ", " + player3 + "," + player4 + ", " + player5 + ", " +
                        challenger +", " + isPlaying + "]");
                team = new Team(name, location, player1, player2, player3, player4, player5,
                        challenger, isPlaying);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return team;
    }

    /*
     * Link: https://stackoverflow.com/questions/38652007/how-to-retrieve-specific-list-of-data-from-firebase
     * Run this method & access the static List
     */
    public void retrieveAllTeamsFromDatabase(String dbMemberName) {

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);
        Log.d(">>>>>", "Starting method");
        isTeamsRetrieved = false;
//        teamsList.clear();

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamsList.clear();
                Log.d(">>>>>", "On 1 st method");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d(">>>>>", "On 2 nd method");
                    team = postSnapshot.getValue(Team.class);
                    teamsList.add(team);

                    // here you can access to name property like university.name
                    System.out.println(">>>>> Retrieving team -> " + team);

                    if (!isTeamsRetrieved) {
                        isTeamsRetrieved = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void updateTeamAttributeInFirebase(String dbMemberName, Team team) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);

        dbReference.child(team.getName()).setValue(team).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(">>>>> Update is successful...");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(">>>>> Update is unsuccessful!");
            }
        });
    }

    public void addMatchDetailsToFirebase(String dbMemberName, MatchDetails md) {

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberName);
        System.out.println(">>>> Adding data into fire base");
        /*dbReference.push().setValue(player);  // Add player with unknown key number*/
        dbReference.child(md.getYoTeam()+" vs "+md.getChlTeam()).setValue(md);
    }

}
