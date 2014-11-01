package com.example.geolocation;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebViewFragment;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Spinner spinner1, spinner2;
	private ArrayList latnlangList = new ArrayList();
	private boolean initializedView = false;
	private Button btnSubmit;
	

	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), 
        R.layout.listview_item_row, getResources().getStringArray(R.array.navigation_drawer_items_array));
        mDrawerList.setAdapter(adapter);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
       
		@Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            System.out.println("2");
           // mDrawerLayout.closeDrawer(mDrawerList);
 
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
  
    public List getAddresses(String addressString){
    	
    	String addressString1 = "ambience  india";
    	Geocoder coder = new Geocoder(this);
     	List<Address> address = null;
     	
     	try {
			address = coder.getFromLocationName(addressString1,8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
     	List<String> addressSuggestion =new ArrayList<String>();
     	int  foundAddressesLength = address.size();

     	for(int i=0 ; i< foundAddressesLength ; i++){
     		Address location = address.get(i);
     		
     		String latnlang = getLatnLang(location);
     		String addressLine = getAddressLine(location);
     		
     		latnlangList.add(latnlang);
     		addressSuggestion.add(addressLine);
     		
     	}
    
		return addressSuggestion;    	
    }
    
    
    public String getAddressLine(Address fetchedAddress){
    	
    	StringBuilder strAddress = new StringBuilder();
    	for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
           strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
      }
    	
    	return strAddress.toString();
    }
    
    public String getLatnLang(Address address){
    	
    	double latitute = address.getLatitude();
    	double longitude = address.getLongitude();
    	
    	String latnlang = String.valueOf(latitute) + "," + String.valueOf(longitude);
    	System.out.println(latnlang);
    	return latnlang;
    	
    }
    // add items into spinner dynamically
    public void addItemsOnSpinner2(List allAddresses) {
   
  	spinner2 = (Spinner) findViewById(R.id.spinner2);
  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
  		R.layout.multiline_spinner_dropdown_item, allAddresses);

  	spinner2.setAdapter(dataAdapter);  
  	
  	btnSubmit = (Button) findViewById(R.id.btnSubmit);
  	final MySqlLiteHelper sqlitedb = new MySqlLiteHelper(this);
 	 
    btnSubmit.setOnClickListener(new OnClickListener() {

    	
    	@Override
        public void onClick(View v) {

        	System.out.println("add button click");
            System.out.println(spinner2.getSelectedItem().toString());
            String addressString = spinner2.getSelectedItem().toString();
            int selectItemPosition = spinner2.getSelectedItemPosition();
            System.out.println("position " + selectItemPosition);
            System.out.println(latnlangList.get(selectItemPosition));	
            String latitudenLongitude = (String) latnlangList.get(selectItemPosition);
            String latitude = latitudenLongitude.split(",")[0];
            String longitude = latitudenLongitude.split(",")[1];
            boolean addressExists = sqlitedb.existsAddress(latitude, longitude );
            if(!addressExists){
            	
            	sqlitedb.addAddress(addressString, latitude, longitude);
            	Toast.makeText(MainActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
            }
            else{
            	Toast.makeText(MainActivity.this, "Address Already Exists", Toast.LENGTH_SHORT).show();
            }
             
        }

    });
  
  	/*spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
  		
  		
  		 @Override
         public void onItemSelected(AdapterView<?> arg0, View arg1,
                 int position, long arg3) {
  			
  			System.out.println(arg2);
  			if(mLastSpinnerPosition == arg2){
  		        return; //do nothing
  			}

  			System.out.println(initializedView);
  			if (initializedView ==  false)
  		    {
  		        initializedView = true;
  		    }
  		    else
  		    {
  		        //only detect selection events that are not done whilst initializing
  		    	 String imc_met=spinner2.getSelectedItem().toString();
  	             System.out.println("here");
  	             System.out.println(latnlangList.get(position));	
  	             System.out.println(imc_met);

  		    }
  			
            
         }

         @Override
         public void onNothingSelected(AdapterView<?> arg0) {
             // TODO Auto-generated method stub

         }
         
         
    });*/
  	
  	
}
    
    
      
    public String getCompleteAddress(View v){
    	
    	EditText addressEditText = (EditText) findViewById(R.id.edit_address1);
    	String address1 = addressEditText.getText().toString();
    	
    	EditText address2EditText = (EditText) findViewById(R.id.edit_address2);
    	String address2 = address2EditText.getText().toString();
    	
    	EditText stateaddress2EditText = (EditText) findViewById(R.id.edit_State);
    	String state = stateaddress2EditText.getText().toString();
    	
    	EditText countryEditText = (EditText) findViewById(R.id.edit_Country);
    	String country = countryEditText.getText().toString();
    	
    	EditText zipCodeEditText = (EditText) findViewById(R.id.edit_ZipCode);
    	String zipCode = zipCodeEditText.getText().toString();
    	
        //TODO : String to StringBuilder
        String completeAddressString = address1 + " " + address2 + " " + state + " " + country + " " + zipCode;
        System.out.println(completeAddressString);
       
        return completeAddressString;
    	
    	
    }
    
    
   
 public void sendMessage(View view){
    	
    	System.out.println("button clicked");
    	    	
    	String completeAddressString = getCompleteAddress(view);
    	List allAddresses = getAddresses(completeAddressString);
    	
    	 addItemsOnSpinner2(allAddresses);
    
    	 
    }
}
