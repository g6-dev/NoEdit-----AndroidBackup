package android.g6.cricspot;

import android.content.Context;
import android.content.Intent;
import android.g6.cricspot.CricClasses.DatabaseManager;
import android.g6.cricspot.CricObjects.Player;
import android.g6.cricspot.CricObjects.Team;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserWithTeamActivity extends AppCompatActivity {

    Button exitTeamBtn, findPlayerBtn, findMatchBtn, signOutBtn;
    TextView teamNameTxt, teamLocationTxt, txtErr;
    ListView playerListViewer;
    Team selectedTeam;
    Player selectedPlayer;
    List<String> listOfPlayers = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    Intent intent;
    DatabaseManager dbManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_with_team);

        exitTeamBtn = findViewById(R.id.exitTeamBtnInUserWithTeamPage);
        findPlayerBtn = findViewById(R.id.findPlayerBtnInUserWithTeamPage);
        findMatchBtn = findViewById(R.id.findMatchBtnInUserWithTeamPage);
        signOutBtn = findViewById(R.id.signOutInUserWithTeamPage);
        teamNameTxt = findViewById(R.id.teamNameTxtInUserWithTeamPage);
        teamLocationTxt = findViewById(R.id.locationTxtInUserWithTeamPage);
        txtErr = findViewById(R.id.txtErrInUserWithTeamPage);
        playerListViewer = findViewById(R.id.playerListInUserWithTeamPage);

        selectedTeam = MainActivity.getUserTeamObject();
        selectedPlayer = MainActivity.getUserPlayerObject();

        teamNameTxt.setText(selectedTeam.getName());
        teamLocationTxt.setText(selectedTeam.getLocation());

        listOfPlayers.add(selectedTeam.getPlayer1());
        listOfPlayers.add(selectedTeam.getPlayer2());
        listOfPlayers.add(selectedTeam.getPlayer3());
        listOfPlayers.add(selectedTeam.getPlayer4());
        listOfPlayers.add(selectedTeam.getPlayer5());

        listAdapter = new ArrayAdapter<String>(UserWithTeamActivity.this,
                android.R.layout.simple_list_item_1, listOfPlayers);
        playerListViewer.setAdapter(listAdapter);
    }

    public void exitTeamIsClickedInUserWithTeamPage(View view) {
        if (isInternetOn()) {
            //Toast.makeText(UserWithTeamActivity.this, "Yet in maintenance", Toast.LENGTH_LONG).show();
            /*TODO: Remove the player from the team, update in firebase + MainActivity*/

            // update the team object -> remove the player
            if (selectedTeam.getPlayer1().equalsIgnoreCase(selectedPlayer.getUserName())) {
                selectedTeam.setPlayer1("no");
            } else if (selectedTeam.getPlayer2().equalsIgnoreCase(selectedPlayer.getUserName())) {
                selectedTeam.setPlayer2("no");
            } else if (selectedTeam.getPlayer3().equalsIgnoreCase(selectedPlayer.getUserName())) {
                selectedTeam.setPlayer3("no");
            } else if (selectedTeam.getPlayer4().equalsIgnoreCase(selectedPlayer.getUserName())) {
                selectedTeam.setPlayer4("no");
            } else if (selectedTeam.getPlayer5().equalsIgnoreCase(selectedPlayer.getUserName())) {
                selectedTeam.setPlayer5("no");
            }

            //Update team in firebase
            dbManager.updateTeamAttributeInFirebase(DatabaseManager.getDbMemberNameForTeam(), selectedTeam);

            //update team in MainActivity
            MainActivity.setUserTeamObject(selectedTeam);

            /*TODO: Remove the team from the player, update in the firebase + MainActivity*/

            //update the player object -> remove the team
            selectedPlayer.setTeam("no");

            //update player in firebase
            dbManager.updatePlayerAttributeInFirebase(DatabaseManager.getDbMemberNameForPlayer(), selectedPlayer);

            //update player in MainActivity
            MainActivity.setUserPlayerObject(selectedPlayer);

            intent = new Intent(UserWithTeamActivity.this, UserWithoutTeamActivity.class);
            startActivity(intent);

        } else {
            txtErr.setText(R.string.noInternet);
        }
    }

    public void findPlayerClickedInUserWithTeamPage(View view) {
        if (isInternetOn()) {
            txtErr.setText("");
            intent = new Intent(UserWithTeamActivity.this, FindPlayerActivity.class);
            startActivity(intent);
        } else {
            txtErr.setText(R.string.noInternet);
        }
    }

    public void findMatchClickedInUserWithTeamPage(View view) {
        if (isInternetOn()) {
            txtErr.setText("");
            System.out.println(">>>>> Selected team: " + selectedTeam);
            if (!selectedTeam.getChallenger().equalsIgnoreCase("no")) {
                intent = new Intent(UserWithTeamActivity.this, MatchActivity.class);
            } else {
                intent = new Intent(UserWithTeamActivity.this, FindMatchActivity.class);
            }
            startActivity(intent);
        } else {
            txtErr.setText(R.string.noInternet);
        }
    }

    public void signOutClickedInUserWithTeamPage(View view) {
        if (isInternetOn()) {
            txtErr.setText("");
            intent = new Intent(UserWithTeamActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            txtErr.setText(R.string.noInternet);
        }
    }

    /* To check the internet connection */
    public Boolean isInternetOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //Don't move behind till signing out! :D
    }
}
