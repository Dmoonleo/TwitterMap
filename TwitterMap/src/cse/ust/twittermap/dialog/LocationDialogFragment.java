package cse.ust.twittermap.dialog;

import twitter4j.GeoLocation;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import cse.ust.twittermap.R;
import cse.ust.twittermap.activities.LocationSearchActivity;

@SuppressLint({ "NewApi", "ValidFragment" }) //require mini api11
public class LocationDialogFragment extends DialogFragment {
	
	private GeoLocation geo = null;
	
	public LocationDialogFragment(GeoLocation geo) {
		this.geo = geo;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View geo_dialog = inflater.inflate(R.layout.geo_dialog2, null);
		final EditText latituteET=(EditText)geo_dialog.findViewById(R.id.latitute);
		final EditText longitudeET=(EditText)geo_dialog.findViewById(R.id.longitude);
		final EditText radiusET=(EditText)geo_dialog.findViewById(R.id.radius);
		//set default value for edittext
		if(geo != null){
			latituteET.setText(Double.toString(geo.getLatitude()));
			longitudeET.setText(Double.toString(geo.getLongitude()));
		}else{
			latituteET.setText(Double.toString(0));
			longitudeET.setText(Double.toString(0));
		}
		radiusET.setText(Double.toString(3));

		builder.setView(geo_dialog)
		.setTitle("Location").setCancelable(true)
		//add action buttons
		.setPositiveButton("Map", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.setNeutralButton("Nearby tweets", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				double lat = Double.valueOf(latituteET.getText().toString());
				double log = Double.valueOf(longitudeET.getText().toString());
				double radius = Double.valueOf(radiusET.getText().toString());
				
				Intent intent = new Intent(getActivity().getApplicationContext(), LocationSearchActivity.class);
				intent.putExtra(SearchManager.QUERY, new double[] {lat, log, radius});
				getActivity().startActivity(intent);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {	
			}
		})
		;
		return builder.create();

	}
	

}
