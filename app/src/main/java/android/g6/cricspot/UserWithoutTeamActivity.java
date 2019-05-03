package android.g6.cricspot;

import android.content.Context;
import android.content.Intent;
import android.g6.cricspot.CricClasses.DatabaseManager;
import android.g6.cricspot.CricClasses.TwoRowListAdapter;
import android.g6.cricspot.CricObjects.NameAndLocation;
import android.g6.cricspot.CricObjects.Team;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserWithoutTeamActivity extends AppCompatActivity {

    final String dbMemberNameForTeam = "Team";

    TextView heyUserTxt, joinTeamTxt, txtErr;
    Button createTeamBtn, loadTeamsBtn;
    ListView teamsListViewer;
    TwoRowListAdapter listAdapter;
    private static List<Team> teamList = new ArrayList<>();
    List<NameAndLocation> nameAndLocationList = new ArrayList<>();
    Intent intent;
    DatabaseManager dbManager = new DatabaseManager();

    public static List<Team> getTeamList() {
        return teamList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_without_team);

        heyUserTxt = findViewById(R.id.heyTxtInUserWithoutTeamPage);
        joinTeamTxt = findViewById(R.id.joinTeamTxtInUserWithoutTeamPage);
        txtErr = findViewById(R.id.txtErrInUserWithoutTeamPage);
        createTeamBtn = findViewById(R.id.createTeamBtnInUserWithoutTeamPage);
        loadTeamsBtn = findViewById(R.id.loadTeamsBtnInUserWithoutTeamPage);
        teamsListViewer = findViewById(R.id.teamListInUserWithoutTeamPage);
        heyUserTxt.setText("Hey " + MainActivity.getUserPlayerObject().getName());

        teamList.clear();
        nameAndLocationList.clear(); // clear before viewing

        if (DatabaseManager.getIsTeamsRetrieved()) {
            teamList = DatabaseManager.getTeamsList();
            System.out.println(">>>>> team list size = " + teamList.size());// testing purpose
            for (Team team : teamList) {
                System.out.println(">>>>> Team : " + team);// testing purpose
                nameAndLocationList.add(new NameAndLocation(team.getName(), team.getLocation()));
            }

            listAdapter = new TwoRowListAdapter(this, R.layout.listview_2row_activity,
                    nameAndLocationList); // create the adapter

            teamsListViewer.setAdapter(listAdapter);// pass adapter

            /* ListViewer onClick Listener */
            teamsListViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = ((TextView) view.findViewById(R.id.row1)).getText().toString();

                    intent = new Intent(UserWithoutTeamActivity.this, TeamDetailsActivity.class);
                    intent.putExtra("tester", selectedItem);
                    startActivity(intent);
                    //Toast.makeText(UserWithoutTeamActivity.this, "Yet in Maintenance", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            teamsListViewer.setVisibility(View.INVISIBLE);
            loadTeamsBtn.setVisibility(View.VISIBLE);
        }

        //teamList = dbManager.retrieveAllTeamsFromDatabase(dbMemberNameForTeam);
//        System.out.println(">>>>> Referencing...");
//        final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child(dbMemberNameForTeam);
//        Log.d(">>>>>", "Starting method");
//
//        teamList.clear();
//        dbReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d(">>>>>", "On 1 st method");
//
//                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    Log.d(">>>>>", "On 2 nd method");
//                    team = postSnapshot.getValue(Team.class);
//                    teamList.add(team);
//
//                    // here you can access to name property like team.name
//                    System.out.println(">>>>> Retrieving team -> "+ team);
//                }
//                //dbReference.removeEventListener(this);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });

        /* Hardcoded values for testing purpose */
//        teamList.add(new Team("G6 Cricketers", "Colombo", "no", "no",
//                "no", "no", "no", false));
//        teamList.add(new Team("Black Smith", "Jaffna", "no", "no",
//                "no", "no", "no", false));
//        teamList.add(new Team("Moon Risers", "Trincomalee", "no", "no",
//                "no", "no", "no", false));
//        teamList.add(new Team("Golden Bat", "Galle", "no", "no",
//                "no", "no", "no", false));
//        teamList.add(new Team("Street Warriors", "Nuwara Eliya", "no", "no",
//                "no", "no", "no", false));

    }

    public void createTeamClickedInUserWithoutTeamPage(View view) {
        /*TODO: Create A team */
        if (isInternetOn()) {
            txtErr.setText("");
            intent = new Intent(UserWithoutTeamActivity.this, CreateTeamActivity.class);
            startActivity(intent);
        } else {
            txtErr.setText(R.string.noInternet);
        }
    }

    public void loadTeamsIsClickedInUserWithoutTeamPage(View view) {
        if (isInternetOn()) { // Is internet on?
            if (!DatabaseManager.getIsTeamsRetrieved()) {
                dbManager.retrieveAllTeamsFromDatabase(dbMemberNameForTeam);
                txtErr.setText("Retrieving data, try in a while!!!");
            } else {
                teamList = DatabaseManager.getTeamsList();
                txtErr.setText(""); // No errors
                loadTeamsBtn.setVisibility(View.INVISIBLE); //make button invisible
                teamsListViewer.setVisibility(View.VISIBLE); // make list visible

                nameAndLocationList.clear(); // clear before viewing

                System.out.println(">>>>> team list size = " + teamList.size());// testing purpose
                for (Team team : teamList) {
                    System.out.println(">>>>> Team : " + team);// testing purpose
                    nameAndLocationList.add(new NameAndLocation(team.getName(), team.getLocation()));
                }

                listAdapter = new TwoRowListAdapter(this, R.layout.listview_2row_activity,
                        nameAndLocationList); // create the adapter

                teamsListViewer.setAdapter(listAdapter);// pass adapter

                /* ListViewer onClick Listener */
                teamsListViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = ((TextView) view.findViewById(R.id.row1)).getText().toString();

                        intent = new Intent(UserWithoutTeamActivity.this, TeamDetailsActivity.class);
                        intent.putExtra("tester", selectedItem);
                        startActivity(intent);
                        //Toast.makeText(UserWithoutTeamActivity.this, "Yet in Maintenance", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            txtErr.setText("Cannot reach the Internet!");
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
        //Nothing: NO backwards, Only sign out!
    }

    public void signOutBtnClickedInUserWithoutTeamPage(View view) {
        intent = new Intent(UserWithoutTeamActivity.this, MainActivity.class);
        startActivity(intent);
        /* TODO: This is sign out, some player attribute might be affected */
    }
}
