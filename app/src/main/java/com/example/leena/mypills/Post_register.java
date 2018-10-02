package com.example.leena.mypills;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class Post_register extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    Button Reg;
    String NAME1,DESC1,TIME1;
    Button addloc;
    EditText name1,desc,timing,date1,cost;
    Double Lat1,Lon1;
    final Calendar myCalendar = Calendar.getInstance();
    DatabaseReference Dref;
    Bundle bundle;
    Spinner spinner;
    String type="";
    private FirebaseAuth firebaseAuth;
    private ImageButton postImage;
    private Uri imageUri;
    private String saveCurrentDate,saveCurrentTime, postRandomName,downloadURL,currentUserId;
    private StorageReference postImgRef;
    private static final int galleryPic=1;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_eve);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            Lon1 = bundle.getDouble("Lon");
            Lat1 = bundle.getDouble("Lat");

        }
        TextView us=findViewById(R.id.renamee);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getEmail();
        us.setText(firebaseAuth.getCurrentUser().getEmail());
cost=findViewById(R.id.price);
        postImage=(ImageButton)findViewById(R.id.post_img1);
        postImgRef= FirebaseStorage.getInstance().getReference();
        loadingBar=new ProgressDialog(this);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Dref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mypills-c2d76.firebaseio.com/events");
        Reg=(Button)findViewById(R.id.reg);
        addloc=(Button)findViewById(R.id.addloc);
        name1=(EditText)findViewById(R.id.evename);
        desc=(EditText)findViewById(R.id.evedesc);
        date1=(EditText)findViewById(R.id.date1);
        timing=(EditText)findViewById(R.id.evetime) ;
         spinner = (Spinner) findViewById(R.id.spinner);
        name1.setText(getIntent().getStringExtra("NAME"));
        desc.setText(getIntent().getStringExtra("DESC"));
        timing.setText(getIntent().getStringExtra("TIME"));
        date1.setText(getIntent().getStringExtra("DATE"));

        final DatePickerDialog.OnDateSetListener date5 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Post_register.this, date5, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        addloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent=new Intent(Post_register.this,Add_loc.class);
                mainIntent.putExtra("NAME",name1.getText().toString());
                mainIntent.putExtra("DESC",desc.getText().toString());
                mainIntent.putExtra("TIME",timing.getText().toString());
                mainIntent.putExtra("DATE",date1.getText().toString());
                mainIntent.putExtra("SPINNER",spinner.getSelectedItemPosition());
                System.out.println("SURHUDM"+spinner.getSelectedItemPosition());

                startActivity(mainIntent);
                Intent intent=getIntent();



            }
        });


        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Exhibition");
        categories.add("Sports");
        categories.add("Seminar");
        categories.add("Treks");
        categories.add("Parties");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(getIntent().getIntExtra("SPINNER",0));


        Reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validatePostInfo();



                    if(Lat1==null){
                        Toast.makeText(getApplicationContext(), "Location cannot be empty",
                                Toast.LENGTH_LONG).show();}
                                else{

                    HashMap<String, Object> hashMap = new HashMap<>();
                    // hashMap.put("",name1.getText().toString());
                    hashMap.put("Description", name1.getText().toString()+" - "+desc.getText().toString());
                    hashMap.put("timing", timing.getText().toString());
                        hashMap.put("Organiser",currentUserId);
                    hashMap.put("Latitude",Lat1);
                    hashMap.put("Longitude",Lon1);
                    hashMap.put("Type",type);
                    hashMap.put("Cost",cost.getText().toString());
                        hashMap.put("Date",date1.getText().toString());
                    Dref.child((String) name1.getText().toString()).setValue(hashMap);
                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra(CalendarContract.Events.TITLE, name1.getText().toString());
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, desc.getText().toString());
                        //intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, myCalendar.getTimeInMillis());
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, myCalendar.getTimeInMillis());
                        intent.putExtra(CalendarContract.Events.ALL_DAY, 1);
                        intent.putExtra(CalendarContract.Events.STATUS, 1);
                        intent.putExtra(CalendarContract.Events.VISIBLE, 0);
                        intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
                        startActivity(intent);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date1.setText(sdf.format(myCalendar.getTime()));
    }

    private void storingImageToFirebase() {
        loadingBar.setTitle("Adding your event");
        loadingBar.setMessage("Task in process...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-mm-yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm:ss");
        saveCurrentTime=currentTime.format(callForTime.getTime());

        postRandomName=saveCurrentDate+saveCurrentTime;

        final StorageReference filepath=postImgRef.child("post images").child(imageUri.getLastPathSegment()+postRandomName+".jpg");
//        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    downloadURL = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
//                    loadingBar.dismiss();
//                    Toast.makeText(PostActivity.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
//
//                    savingPostInfoToDatabase();
//
//
//                }
//                else {
//                    loadingBar.dismiss();
//                    String msg=task.getException().getMessage();
//                    Toast.makeText(PostActivity.this,"Error: "+msg,Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        UploadTask uploadTask = filepath.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadURL = task.getResult().toString();
                    loadingBar.dismiss();
                    Toast.makeText(Post_register.this,"Event added successfully",Toast.LENGTH_SHORT).show();
                    savingPostInfoToDatabase();



                } else {
                    // Handle failures
                    // ...
                    loadingBar.dismiss();
                    String msg=task.getException().getMessage();
                    Toast.makeText(Post_register.this,"Error: "+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPic && resultCode==RESULT_OK && data!=null )
        {
            imageUri=data.getData();
            postImage.setImageURI(imageUri);

        }
    }

    private void savingPostInfoToDatabase() {
        if(Lat1==null){
            Toast.makeText(getApplicationContext(), "Location cannot be empty",
                    Toast.LENGTH_LONG).show();}
        else {
            HashMap<String, Object> hashMap = new HashMap<>();
            // hashMap.put("",name1.getText().toString());
            hashMap.put("Description",name1.getText().toString()+" - "+ desc.getText().toString()+" ("+type+") ");
            hashMap.put("timing", timing.getText().toString());
            hashMap.put("Latitude", Lat1);
            hashMap.put("Longitude", Lon1);
            hashMap.put("Type", type);
            hashMap.put("imageURL", downloadURL);
            hashMap.put("Organiser",currentUserId);
            hashMap.put("Cost",cost.getText().toString());
            hashMap.put("Date",date1.getText().toString());
            Dref.child((String) name1.getText().toString()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        //  Toast.makeText(PostActivity.this,"Image added",Toast.LENGTH_SHORT).show();
                        sendUserToMapActivity();
                    } else {
                        Toast.makeText(Post_register.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToMapActivity() {
        Intent k = new Intent(Post_register.this, check.class);
        startActivity(k);

        //Intent mainIntent=new Intent(Post_register.this,Map_data.class);
        // mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(mainIntent);
        //finish();
    }

    private void validatePostInfo() {
     //   description=postDescription.getText().toString();
        if(imageUri==null)
        {
            Toast.makeText(this,"Please add image",Toast.LENGTH_SHORT).show();
        }
//        else if(TextUtils.isEmpty(description))
//        {
//            Toast.makeText(this,"Please add description",Toast.LENGTH_SHORT).show();
//        }
        else{
            storingImageToFirebase();
        }

    }
    private void openGallery() {
        Intent gallery=new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,galleryPic);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         type = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
      //  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
