package android.g6.cricspot.CricClasses;

import android.content.Context;
import android.g6.cricspot.CricObjects.NameAndLocation;
import android.g6.cricspot.R;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TwoRowListAdapter extends ArrayAdapter<NameAndLocation> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView name, location;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public TwoRowListAdapter(Context context, int resource, List<NameAndLocation> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        String location = getItem(position).getLocation();

        //Create the person object with the information
        NameAndLocation nameDesc = new NameAndLocation(name, location);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.row1);
            holder.location = (TextView) convertView.findViewById(R.id.row2);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.name.setText(nameDesc.getName());
        holder.location.setText(nameDesc.getLocation());

        return convertView;
    }
}