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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    //To store the user's player object for the whole application program
    private static Player userPlayerObject;

    public static Player getUserPlayerObject() {
        return userPlayerObject;
    }

    public static void setUserPlayerObject(Player userPlayerObject) {
        MainActivity.userPlayerObject = userPlayerObject;
    }
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //To store the user's team object for the whole application program
    private static Team userTeamObject;

    public static Team getUserTeamObject() {
        return userTeamObject;
    }

    public static void setUserTeamObject(Team userTeamObject) {
        MainActivity.userTeamObject = userTeamObject;
    }
    //----------------------------------------------------------------------------------------------

    EditText userNameE, passwordE;
    Button logIn, createAccount, testBtn;
    TextView noAccount, txtErr;
    Intent intent;
    DatabaseManager dbManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initializing the variables */
        userNameE = findViewById(R.id.nameInLoginPage);
        passwordE = findViewById(R.id.passwordInLoginPage);
        logIn = findViewById(R.id.loginInLoginPage);
        createAccount = findViewById(R.id.createAccountInLoginPage);
        noAccount = findViewById(R.id.noAccountTextInLoginPage);
        txtErr = findViewById(R.id.txtErrInLoginPage);
        testBtn = findViewById(R.id.testerInMain);

        //Initially keep offline database of online lists -> -+- Team, Players -+-
        if ( ! DatabaseManager.getIsTeamsRetrieved() ) {
            dbManager.retrieveAllTeamsFromDatabase(DatabaseManager.getDbMemberNameForTeam());
        }

        if ( ! DatabaseManager.getIsPlayersRetrieved()) {
            dbManager.retrieveAllPlayersFromDatabase(DatabaseManager.getDbMemberNameForPlayer());
        }
    }

    public void logInClickedInLoginPage(View view) {
        /* TODO: Authentication  must take place */
        String userName = userNameE.getText().toString();
        String password = passwordE.getText().toString();

        if(isInternetOn()) {
            if (userName.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                txtErr.setText(R.string.fieldsEmpty);
            } else {
                txtErr.setText("Checking username & password...");
                txtErr.setTextColor(getResources().getColor(R.color.colorPrimary));

                Boolean isRetrieved = false;
                do{ // Run till isRetrieved becomes true
                  isRetrieved = DatabaseManager.getIsPlayersRetrieved();
                  System.out.println(">>>>> Player isRetrieved not yet 'true'");
                }while(!isRetrieved);

                List<Player> listOfPlayers = DatabaseManager.getPlayersList();

                Boolean logIn = false;
                Boolean hasTeam = false;
                for (int i=0; i<listOfPlayers.size(); i++){
                    //First Check the userName of player
                    if(listOfPlayers.get(i).getUserName().equalsIgnoreCase(userName)){
                        //Second check the password of the player
                        if (listOfPlayers.get(i).getPassword().equalsIgnoreCase(password)){
                            //Found the user! name is okay + password is okay
                            logIn = true;
                            setUserPlayerObject(listOfPlayers.get(i)); // set user as static

                            String teamName = listOfPlayers.get(i).getTeam();
                            // Is user already in a team?
                            if ( ! teamName.equalsIgnoreCase("no")){
                                hasTeam = true;

                                /*TODO: This might lag the app, change it!*/
                                isRetrieved = false;
                                do{ // Run till isRetrieved becomes true
                                    isRetrieved = DatabaseManager.getIsTeamsRetrieved();
                                    System.out.println(">>>>> Team isRetrieved not yet 'true'");
                                }while(!isRetrieved);

                                List<Team> listOfTeams = DatabaseManager.getTeamsList();

                                for(int j=0; j<listOfTeams.size(); j++){
                                    if(teamName.equalsIgnoreCase(listOfTeams.get(j).getName())){
                                        setUserTeamObject(listOfTeams.get(j));// Set team as static
                                    }
                                }
                            }
                        }
                    }
                }

                if (logIn){
                    if (! hasTeam ) { //If without a team
                        intent = new Intent(MainActivity.this, UserWithoutTeamActivity.class);
                    }else{ // If with a team
                        intent = new Intent(MainActivity.this, UserWithTeamActivity.class);
                    }
                    startActivity(intent); // Now redirect ->
                }else{
                    txtErr.setText("Invalid Username or Password!");
                    txtErr.setTextColor(getResources().getColor(R.color.redColor));
                }


            }
        }else{
            txtErr.setText(R.string.noInternet);
        }


    }

    public void createAccountClickedInLoginPage(View view) {
        /* Page redirected to Create Account page */
        if(isInternetOn()) {
            txtErr.setText("");
            intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        }else{
            txtErr.setText(R.string.noInternet);
        }
    }

    /* To check the internet connection */
    public Boolean isInternetOn(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    public void testingPages(View view) {
        intent = new Intent(MainActivity.this, FindMatchActivity.class);
        startActivity(intent);

       // System.out.println(">>>>> Nothing is tested here!");
    }

    @Override
    public void onBackPressed() {
        //Do nothing, don't go back
    }
}
