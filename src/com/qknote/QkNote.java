package com.qknote;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class QkNote extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    /** Called when the activity is first created. */
	ArrayList<String> notes;
	ArrayAdapter<String> adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button newButton = (Button) findViewById(R.id.newButton);
        newButton.setOnClickListener(this);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        Button delButton = (Button) findViewById(R.id.delButton);
        delButton.setOnClickListener(this);
        
        notes = new ArrayList<String>(Arrays.asList(fileList()));
        
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, notes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner menu = (Spinner) findViewById(R.id.menu);
        menu.setAdapter(adapter);
        
        menu.setOnItemSelectedListener(this);
       
        
    }//onCreate
    
	@Override 
    public void onClick(View v) {
		if(v.equals(findViewById(R.id.newButton))) {
			EditText title = (EditText) findViewById(R.id.title);
			EditText note = (EditText) findViewById(R.id.note);
			outputNote();
			title.setText("Title");
			note.setText("Note");
		}//if
		else if(v.equals(findViewById(R.id.saveButton))) {
			outputNote();
		}//else if
		else if(v.equals(findViewById(R.id.delButton))) {
			EditText title = (EditText) findViewById(R.id.title);
			EditText note = (EditText) findViewById(R.id.note);
			
			String noteToDelete = title.getText().toString();
			
			title.setText("Title");
			note.setText("Note");
			
			deleteFile(noteToDelete);
			if(adapter.getPosition(noteToDelete) != -1)
				adapter.remove(noteToDelete);
		}//else if
	}//onClick
    
    public void onPause() {
    	super.onPause();
    	outputNote();
    }//onPause
    
    private void outputNote() {
    	EditText title = (EditText) findViewById(R.id.title);
    	EditText note = (EditText) findViewById(R.id.note);
    	
    	if(title.getText().toString().equals("Title") && note.getText().toString().equals("Note"))
    		return;
    	
    	if(adapter.getPosition(title.getText().toString()) == -1)
    		adapter.add(title.getText().toString());

		try {
			OutputStreamWriter output = new OutputStreamWriter(openFileOutput(title.getText().toString(),0));
	    	output.write(note.getText().toString());
	    	output.close();
	    	
		} catch (Exception e) { } 
    }//outputNote

	private void inputNote(String filename) {
		EditText title = (EditText) findViewById(R.id.title);
		title.setText(filename);
		
    	EditText note = (EditText) findViewById(R.id.note);

    	Scanner s = null;
		try {
			s = new Scanner(openFileInput(filename));
		} catch (Exception e) { 
			note.setText(e.getMessage());
		}
		
    	String str = "";
    	while(s.hasNext())
    		str += s.nextLine();
    	
    	note.setText(str);	
    }//inputNote


	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		outputNote();
		inputNote(notes.get(position));
	}//onItemSelected


	public void onNothingSelected(AdapterView<?> parent) { 
		
	}//onNothingSelected
}