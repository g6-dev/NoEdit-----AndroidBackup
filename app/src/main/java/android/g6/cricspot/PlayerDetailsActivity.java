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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayerDetailsActivity extends AppCompatActivity {

    TextView playerInfoTxt, userNameTxt, nameTxt, locationTxt, txtErr;
    EditText userNameE, nameE, locationE;
    Button addPlayerButton;
    Intent intent;
    String userName, name, location, intentString;
    Player selectedPlayer;
    Team selectedTeam;
    List<Player> listOfAllPlayers = new ArrayList<>();
    DatabaseManager dbManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        playerInfoTxt = findViewById(R.id.playerInfoTxtInPlayerDetailsPage);
        userNameTxt = findViewById(R.id.userNameTxtInPlayerDetailsPage);
        nameTxt = findViewById(R.id.nameTxtInPlayerDetailsPage);
        locationTxt = findViewById(R.id.locationTxtInPlayerDetailsPage);
        userNameE = findViewById(R.id.userNameInPlayerDetailsPage);
        nameE = findViewById(R.id.nameInPlayerDetailsPage);
        locationE = findViewById(R.id.locationInPlayerDetailsPage);
        addPlayerButton = findViewById(R.id.addPlayerBtnInPlayerDetailsPage);
        txtErr = findViewById(R.id.txtErrInPlayerDetailsPage);

        // name of the player selected
        intentString = getIntent().getStringExtra("tester");
        System.out.println(">>>>>  Intent String passer is "+intentString);

        listOfAllPlayers = DatabaseManager.getPlayersList();

        //Find the player
        Boolean foundPlayer = false;
        for (Player player: listOfAllPlayers){
            if(player.getUserName().equalsIgnoreCase(intentString)){
                selectedPlayer = player; // found
                foundPlayer = true; // make yes to condition
            }
        }

        if(foundPlayer){
            userNameE.setText(selectedPlayer.getUserName());
            nameE.setText(selectedPlayer.getName());
            locationE.setText(selectedPlayer.getLocation());
            userNameE.setEnabled(false);
            nameE.setEnabled(false);
            locationE.setEnabled(false);
        }else{
            System.out.println(">>>>> Player not found!!!");
        }
    }

    public void addPlayerClickedInPlayerDetailsPage(View view) {

        if(isInternetOn()) {
            selectedTeam = MainActivity.getUserTeamObject();
            /*TODO: Add the player into the team + team into the player*/
            selectedPlayer.setTeam(selectedTeam.getName());

            if(selectedTeam.getPlayer1().equalsIgnoreCase("no")){
                selectedTeam.setPlayer1(selectedPlayer.getUserName());
            }else if(selectedTeam.getPlayer2().equalsIgnoreCase("no")){
                selectedTeam.setPlayer2(selectedPlayer.getUserName());
            }else if(selectedTeam.getPlayer3().equalsIgnoreCase("no")){
                selectedTeam.setPlayer3(selectedPlayer.getUserName());
            }else if(selectedTeam.getPlayer4().equalsIgnoreCase("no")){
                selectedTeam.setPlayer4(selectedPlayer.getUserName());
            }else if(selectedTeam.getPlayer5().equalsIgnoreCase("no")){
                selectedTeam.setPlayer5(selectedPlayer.getUserName());
            }

            /*TODO: Update the player in firebase - ! - nOT MAINACTIVITY*/
            dbManager.updatePlayerAttributeInFirebase(DatabaseManager.getDbMemberNameForPlayer(), selectedPlayer);
            /*TODO: Update the team in firebase + MainActivity*/
            dbManager.updateTeamAttributeInFirebase(DatabaseManager.getDbMemberNameForTeam(), selectedTeam);
            MainActivity.setUserTeamObject(selectedTeam);
            /*TODO: Redirect the page to UserWithTeam Activity*/
            intent = new Intent(PlayerDetailsActivity.this, UserWithTeamActivity.class);
            startActivity(intent);
        }else{
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
}
