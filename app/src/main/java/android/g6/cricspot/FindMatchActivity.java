package android.g6.cricspot;

import android.content.Intent;
import android.g6.cricspot.CricClasses.DatabaseManager;
import android.g6.cricspot.CricClasses.TwoRowListAdapter;
import android.g6.cricspot.CricObjects.NameAndLocation;
import android.g6.cricspot.CricObjects.Team;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FindMatchActivity extends AppCompatActivity {

    TextView txtTopic;
    ListView listView;
    List<Team> listOfAllTeams = new ArrayList<>();
    List<NameAndLocation> playersNameLocationList = new ArrayList<>();
    TwoRowListAdapter listAdapter;
    Intent intent;
    DatabaseManager dbManager = new DatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        txtTopic = findViewById(R.id.txtInFindMatchPage);
        listView = findViewById(R.id.listViewInFindMatchPage);


        listOfAllTeams.clear();
        listOfAllTeams = DatabaseManager.getTeamsList();

        playersNameLocationList.clear();

        //Get the teams who doesn't have a team yet!!!
        for (Team team : listOfAllTeams) { // Run whole teams
            if (!(team.getName().equals(MainActivity.getUserTeamObject().getName()))) {
                if (!team.getPlaying()) { // teams with false as isPlaying ! :D
                    playersNameLocationList.add(new NameAndLocation(team.getName(), team.getLocation()));
                    System.out.println(">>>>> team: " + team);
                }
            }
        }

        listAdapter = new TwoRowListAdapter(FindMatchActivity.this, R.layout.listview_2row_activity,
                playersNameLocationList);
        listView.setAdapter(listAdapter);

        /* ListViewer onClick Listener */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view.findViewById(R.id.row1)).getText().toString();

                intent = new Intent(FindMatchActivity.this, TeamDetailsForMatchActivity.class);
                intent.putExtra("tester", selectedItem);
                startActivity(intent);
                //Toast.makeText(FindMatchActivity.this, "you selected: "+selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
